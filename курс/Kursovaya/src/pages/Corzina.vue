<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue';
import { RouterLink } from 'vue-router';

interface Product {
  id: number;
  name: string;
  price: number;
  image: string;
  category: string;
  description: string;
  stock: number;
}

interface CartItem {
  id: number;
  userId: number;
  product: Product;
  quantity: number;
}

interface User {
  id: number;
  email: string;
  role: string;
  status: string;
}

interface Notification {
  message: string;
  type: 'success' | 'error';
}

// API
const API_URL = 'http://localhost:8080/api';
const cartItems = ref<CartItem[]>([]);
const currentUser = ref<User | null>(null);
const token = ref<string | null>(null);
const isLoading = ref(false);
const showOrderModal = ref(false);
const orderCompleted = ref(false);

const notification = reactive<Notification>({
  message: '',
  type: 'success'
});

const paymentForm = reactive({
  paymentMethod: 'Банковская карта',
  paymentType: 'Онлайн',
  deliveryAddress: '',
  cardNumber: '',
  cardExpiry: '',
  cardCvv: ''
});

const paymentMethods = ['Банковская карта', 'Наличными курьеру', 'Перевод на счёт'];
const paymentTypes = ['Онлайн', 'При получении'];

function getAuthHeaders() {
  return {
    'Content-Type': 'application/json',
    'Authorization': token.value ? `Bearer ${token.value}` : ''
  };
}

function showNotification(message: string, type: 'success' | 'error', timeout = 3000) {
  notification.message = message;
  notification.type = type;
  setTimeout(() => {
    notification.message = '';
  }, timeout);
}

// Загрузка корзины из API
async function loadCart() {
  if (!currentUser.value || !token.value) {
    return;
  }

  isLoading.value = true;
  try {
    const response = await fetch(`${API_URL}/cart/${currentUser.value.id}`, {
      headers: getAuthHeaders()
    });

    if (response.ok) {
      cartItems.value = await response.json();
    } else if (response.status === 404) {
      cartItems.value = [];
    }
  } catch (error) {
    console.error('Error loading cart:', error);
    showNotification('Ошибка загрузки корзины', 'error');
  } finally {
    isLoading.value = false;
  }
}

// Удаление товара из корзины
async function removeFromCart(cartItemId: number) {
  if (!currentUser.value || !token.value) {
    showNotification('Авторизуйтесь для управления корзиной', 'error');
    return;
  }

  try {
    const response = await fetch(`${API_URL}/cart/${currentUser.value.id}/${cartItemId}`, {
      method: 'DELETE',
      headers: getAuthHeaders()
    });

    if (response.ok || response.status === 204) {
      showNotification('Товар удален из корзины', 'success');
      await loadCart();
    } else {
      showNotification('Ошибка удаления товара', 'error');
    }
  } catch (error) {
    showNotification('Ошибка подключения к серверу', 'error');
    console.error('Remove from cart error:', error);
  }
}

// Вычисляемые свойства
const cartTotal = computed(() => {
  return cartItems.value.reduce((sum, item) => {
    const price = item.product?.price || 0;
    return sum + price * item.quantity;
  }, 0);
});

const totalCount = computed(() => {
  return cartItems.value.reduce((sum, item) => sum + item.quantity, 0);
});

// Оформление заказа
async function placeOrder() {
  if (!currentUser.value || !token.value) {
    showNotification('Авторизуйтесь, чтобы оформить заказ', 'error');
    return;
  }

  if (cartItems.value.length === 0) {
    showNotification('Корзина пуста', 'error');
    return;
  }

  isLoading.value = true;
  try {
    const response = await fetch(`${API_URL}/orders/${currentUser.value.id}`, {
      method: 'POST',
      headers: getAuthHeaders(),
      body: JSON.stringify(paymentForm)
    });

    const data = await response.json();

    if (response.ok) {
      showNotification('Заказ успешно оформлен!', 'success');
      orderCompleted.value = true;
      cartItems.value = [];
      showOrderModal.value = false;
      
      // Сброс формы
      paymentForm.deliveryAddress = '';
      paymentForm.cardNumber = '';
      paymentForm.cardExpiry = '';
      paymentForm.cardCvv = '';
    } else {
      showNotification(data.error || 'Ошибка оформления заказа', 'error');
    }
  } catch (error) {
    showNotification('Ошибка подключения к серверу', 'error');
    console.error('Place order error:', error);
  } finally {
    isLoading.value = false;
  }
}

// Открытие модального окна оформления заказа
function openOrderModal() {
  if (!currentUser.value || !token.value) {
    showNotification('Авторизуйтесь, чтобы оформить заказ', 'error');
    return;
  }

  if (cartItems.value.length === 0) {
    showNotification('Корзина пуста', 'error');
    return;
  }

  showOrderModal.value = true;
}

onMounted(() => {
  // Загрузка пользователя из localStorage
  const savedUser = localStorage.getItem('user');
  const savedToken = localStorage.getItem('token');
  
  if (savedUser && savedToken) {
    currentUser.value = JSON.parse(savedUser);
    token.value = savedToken;
    loadCart();
  } else {
    showNotification('Авторизуйтесь для просмотра корзины', 'error');
  }
});
</script>

<template>
  <div class="flex flex-col bg-pink-100 min-h-screen">
    <!-- Уведомления -->
    <div
      v-if="notification.message"
      :class="[
        'fixed top-4 right-4 p-4 rounded-md shadow-md z-50',
        notification.type === 'success' ? 'bg-green-500 text-white' : 'bg-red-500 text-white'
      ]"
    >
      {{ notification.message }}
    </div>

    <div class="flex flex-row gap-4 p-6 justify-center bg-pink-200 shadow-md">
      <RouterLink
        to="/"
        class="hover:text-blue-600 transition border-2 bg-pink-300/50 border-blue-300 rounded-full px-8 py-3 text-2xl font-bold font-mono">
        Главная
      </RouterLink>
      <RouterLink
        to="/catalog"
        class="hover:text-blue-600 transition border-2 bg-pink-300/50 border-blue-300 rounded-full px-8 py-3 text-2xl font-bold font-mono">
        Каталог
      </RouterLink>
      <RouterLink
        to="/cabinet"
        class="hover:text-blue-600 transition border-2 bg-pink-300/50 border-blue-300 rounded-full px-8 py-3 text-2xl font-bold font-mono">
        Личный кабинет
      </RouterLink>
    </div>

    <!-- Основной контент -->
    <div class="flex flex-col items-center justify-center flex-grow p-8">
      <div v-if="orderCompleted" class="text-center">
        <div class="text-4xl font-bold text-green-600 mb-4">
        Заказ успешно оформлен!
        </div>
        <p class="text-xl text-gray-600 mb-6">Спасибо за покупку!</p>
        <RouterLink
          to="/cabinet"
          class="px-8 py-3 bg-blue-500 text-white rounded-lg text-xl hover:scale-105 transition">
          Посмотреть мои заказы
        </RouterLink>
      </div>

      <div v-else class="flex flex-col items-center w-full max-w-5xl">
        <h2 class="text-3xl font-bold mb-5">Корзина товаров</h2>

        <!-- Карточки товаров -->
        <div v-if="isLoading" class="text-center text-gray-500 py-10">
          <p class="text-xl">Загрузка корзины...</p>
        </div>

        <div v-else-if="cartItems.length > 0" class="flex flex-wrap gap-6 p-6 bg-[#fffbeb] border-2 border-yellow-300 rounded-lg w-full">
          <div
            v-for="item in cartItems"
            :key="item.id"
            class="flex flex-col items-center bg-white p-4 rounded-lg shadow-md w-64">
            <img :src="item.product?.image" :alt="item.product?.name" class="w-full h-40 object-cover mb-2 rounded-md" />
            <h3 class="text-lg font-medium text-center">{{ item.product?.name }}</h3>
            <p class="text-sm text-gray-600 mb-1">{{ item.product?.price }} ₽</p>
            <p class="text-xs text-gray-500 mb-2">Количество: {{ item.quantity }}</p>
            <p class="text-sm font-semibold mb-2">Итого: {{ (item.product?.price || 0) * item.quantity }} ₽</p>
            <button
              @click="removeFromCart(item.id)"
              class="mt-2 px-4 py-1 bg-red-500 text-white rounded-md hover:scale-105 transition">
              Удалить
            </button>
          </div>
        </div>

        <div v-else class="text-center">
          <div class="text-lg text-gray-500 mt-6 mb-6">
            Корзина пуста
          </div>
          <RouterLink
            to="/catalog"
            class="px-8 py-3 bg-blue-500 text-white rounded-lg text-xl hover:scale-105 transition">
            Перейти в каталог
          </RouterLink>
        </div>

        <!-- Информация о заказе -->
        <div v-if="cartItems.length > 0" class="mt-10 text-center">
          <div class="text-2xl font-bold mb-4">
            Всего товаров: {{ totalCount }} | Общая стоимость: {{ cartTotal.toFixed(2) }} ₽
          </div>

          <button
            @click="openOrderModal"
            class="px-8 py-3 bg-green-500 text-white rounded-lg text-xl hover:scale-105 transition">
            Оформить заказ
          </button>
        </div>
      </div>
    </div>

    <!-- Модальное окно оформления заказа -->
    <div
      v-if="showOrderModal"
      class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50"
      @click.self="showOrderModal = false"
    >
      <div class="bg-white rounded-2xl p-8 max-w-2xl w-full mx-4 max-h-[90vh] overflow-y-auto">
        <div class="flex justify-between items-center mb-6">
          <h3 class="text-2xl font-bold">Оформление заказа</h3>
          <button
            @click="showOrderModal = false"
            class="text-gray-500 hover:text-gray-700 text-2xl"
          >
            ×
          </button>
        </div>

        <div class="space-y-4">
          <!-- Способ оплаты -->
          <div>
            <label class="block mb-2 font-medium">Способ оплаты</label>
            <select
              v-model="paymentForm.paymentMethod"
              class="w-full h-12 rounded-3xl px-4 border border-gray-400 focus:outline-none focus:border-blue-500"
            >
              <option v-for="method in paymentMethods" :key="method" :value="method">
                {{ method }}
              </option>
            </select>
          </div>

          <!-- Тип оплаты -->
          <div>
            <label class="block mb-2 font-medium">Тип оплаты</label>
            <select
              v-model="paymentForm.paymentType"
              class="w-full h-12 rounded-3xl px-4 border border-gray-400 focus:outline-none focus:border-blue-500"
            >
              <option v-for="type in paymentTypes" :key="type" :value="type">
                {{ type }}
              </option>
            </select>
          </div>

          <!-- Сумма заказа -->
          <div>
            <label class="block mb-2 font-medium">Сумма заказа</label>
            <input
              :value="`${cartTotal.toFixed(2)} ₽`"
              type="text"
              readonly
              class="w-full h-12 rounded-3xl px-4 border border-gray-300 bg-gray-100 text-gray-600"
            />
          </div>

          <!-- Адрес доставки -->
          <div>
            <label class="block mb-2 font-medium">Адрес доставки</label>
            <input
              v-model="paymentForm.deliveryAddress"
              type="text"
              placeholder="Город, улица, дом, квартира"
              class="w-full h-12 rounded-3xl px-4 border border-gray-400 focus:outline-none focus:border-blue-500"
            />
          </div>

          <!-- Данные карты (если выбрана карта) -->
          <div v-if="paymentForm.paymentMethod === 'Банковская карта'">
            <label class="block mb-2 font-medium">Номер карты (до 12 цифр)</label>
            <input
              v-model="paymentForm.cardNumber"
              type="text"
              maxlength="12"
              placeholder="000000000000"
              class="w-full h-12 rounded-3xl px-4 border border-gray-400 focus:outline-none focus:border-blue-500"
            />
          </div>

          <div
            v-if="paymentForm.paymentMethod === 'Банковская карта'"
            class="grid grid-cols-2 gap-4"
          >
            <div>
              <label class="block mb-2 font-medium">Срок действия (00/00)</label>
              <input
                v-model="paymentForm.cardExpiry"
                type="text"
                maxlength="5"
                placeholder="00/00"
                class="w-full h-12 rounded-3xl px-4 border border-gray-400 focus:outline-none focus:border-blue-500"
              />
            </div>
            <div>
              <label class="block mb-2 font-medium">CVV (3 цифры)</label>
              <input
                v-model="paymentForm.cardCvv"
                type="text"
                maxlength="3"
                placeholder="000"
                class="w-full h-12 rounded-3xl px-4 border border-gray-400 focus:outline-none focus:border-blue-500"
              />
            </div>
          </div>

          <!-- Кнопки -->
          <div class="flex gap-4 mt-6">
            <button
              @click="showOrderModal = false"
              class="flex-1 px-6 py-3 bg-gray-500 text-white rounded-lg hover:bg-gray-600 transition"
            >
              Отмена
            </button>
            <button
              @click="placeOrder"
              :disabled="isLoading"
              class="flex-1 px-6 py-3 bg-blue-500 text-white rounded-lg hover:bg-blue-600 transition disabled:opacity-50"
            >
              {{ isLoading ? 'Оформляем...' : 'Подтвердить заказ' }}
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
