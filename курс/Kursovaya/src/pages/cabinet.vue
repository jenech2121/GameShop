<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue';
import { RouterLink } from 'vue-router';

// Типы данных
interface User {
  id: number;
  email: string;
  role: string;
  status: string;
}

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

interface OrderItem {
  productId: number;
  productName: string;
  productImage: string;
  quantity: number;
  price: number;
}

interface Order {
  id: number;
  userId: number;
  userEmail: string | null;
  totalPrice: number;
  status: string;
  createdAt: string;
  paymentMethod?: string;
  paymentType?: string;
  deliveryAddress?: string;
  cardNumber?: string;
  cardExpiry?: string;
  cardCvv?: string;
  items: OrderItem[];
}

interface Notification {
  message: string;
  type: 'success' | 'error';
}

// Состояние
const isLoggedIn = ref(false);
const isRegisterMode = ref(false);
const currentUser = ref<User | null>(null);
const token = ref<string | null>(null);

// Форма входа/регистрации
const loginForm = reactive({
  email: '',
  password: ''
});

const registerForm = reactive({
  email: '',
  password: '',
  confirmPassword: ''
});

// Уведомления
const notification = reactive<Notification>({
  message: '',
  type: 'success'
});

// Админские данные
const users = ref<User[]>([]);
const products = ref<Product[]>([]);
const selectedUser = ref<User | null>(null);
const selectedProduct = ref<Product | null>(null);
const userOrders = ref<Order[]>([]);
const adminOrders = ref<Order[]>([]);
const isCreatingOrder = ref(false);
const cartItems = ref<CartItem[]>([]);
const cartTotal = ref(0);

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

// Формы админа
const userStatusForm = reactive({
  status: 'ACTIVE'
});

const userRoleForm = reactive({
  role: 'USER'
});

const productForm = reactive({
  name: '',
  price: 0,
  image: '',
  category: 'GPU',
  description: '',
  stock: 0
});

const productImageForm = reactive({
  image: ''
});

// API базовый URL
const API_URL = 'http://localhost:8080/api';

// Утилиты
function showNotification(message: string, type: 'success' | 'error', timeout = 3000) {
  notification.message = message;
  notification.type = type;
  setTimeout(() => {
    notification.message = '';
  }, timeout);
}

function getAuthHeaders() {
  return {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${token.value}`
  };
}

async function loadUserOrders() {
  if (!token.value || !currentUser.value) return;
  try {
    const response = await fetch(`${API_URL}/orders/${currentUser.value.id}`, {
      headers: getAuthHeaders()
    });

    if (response.ok) {
      userOrders.value = await response.json();
    }
  } catch (error) {
    console.error('Error loading user orders:', error);
  }
}

async function loadCartSummary() {
  if (!token.value || !currentUser.value) return;
  try {
    const response = await fetch(`${API_URL}/cart/${currentUser.value.id}`, {
      headers: getAuthHeaders()
    });

    if (response.ok) {
      const items = await response.json();
      cartItems.value = items;
      cartTotal.value = cartItems.value.reduce((sum, item) => {
        const price = item.product?.price || 0;
        return sum + price * item.quantity;
      }, 0);
    }
  } catch (error) {
    console.error('Error loading cart summary:', error);
  }
}

async function loadAdminOrders() {
  if (!token.value || currentUser.value?.role !== 'ADMIN') return;
  try {
    const response = await fetch(`${API_URL}/admin/orders`, {
      headers: getAuthHeaders()
    });

    if (response.ok) {
      adminOrders.value = await response.json();
    }
  } catch (error) {
    console.error('Error loading admin orders:', error);
  }
}

function getOrdersForUser(userId: number) {
  return adminOrders.value.filter(order => order.userId === userId);
}

// Проверка авторизации при загрузке
onMounted(() => {
  const savedToken = localStorage.getItem('token');
  const savedUser = localStorage.getItem('user');
  
  if (savedToken && savedUser) {
    token.value = savedToken;
    currentUser.value = JSON.parse(savedUser);
    isLoggedIn.value = true;
    
    if (currentUser.value?.role === 'ADMIN') {
      loadAdminData();
    } else if (currentUser.value?.role === 'USER') {
      loadUserOrders();
      loadCartSummary();
    }
  }
});

// Авторизация
async function login() {
  try {
    const response = await fetch(`${API_URL}/users/login`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        email: loginForm.email,
        password: loginForm.password
      })
    });

    const data = await response.json();

    if (response.ok) {
      token.value = data.token;
      currentUser.value = data.user;
      isLoggedIn.value = true;
      
      localStorage.setItem('token', data.token);
      localStorage.setItem('user', JSON.stringify(data.user));
      
    showNotification('Вы успешно вошли в систему!', 'success');
      
      if (data.user.role === 'ADMIN') {
        loadAdminData();
      } else {
        loadUserOrders();
        loadCartSummary();
      }
      
      loginForm.email = '';
      loginForm.password = '';
    } else {
      showNotification(data.error || 'Ошибка входа', 'error');
    }
  } catch (error) {
    showNotification('Ошибка подключения к серверу', 'error');
    console.error('Login error:', error);
  }
}

// Регистрация
async function register() {
  if (registerForm.password !== registerForm.confirmPassword) {
    showNotification('Пароли не совпадают', 'error');
    return;
  }

  try {
    const response = await fetch(`${API_URL}/users/register`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        email: registerForm.email,
        password: registerForm.password
      })
    });

    const data = await response.json();

    if (response.ok) {
      token.value = data.token;
      currentUser.value = data.user;
      isLoggedIn.value = true;
      
      localStorage.setItem('token', data.token);
      localStorage.setItem('user', JSON.stringify(data.user));
      
      showNotification('Регистрация успешна!', 'success');
      
      if (data.user.role === 'ADMIN') {
        loadAdminData();
      } else {
        loadUserOrders();
        loadCartSummary();
      }
      
      registerForm.email = '';
      registerForm.password = '';
      registerForm.confirmPassword = '';
      isRegisterMode.value = false;
    } else {
      showNotification(data.error || 'Ошибка регистрации', 'error');
    }
  } catch (error) {
    showNotification('Ошибка подключения к серверу', 'error');
    console.error('Register error:', error);
  }
}

// Выход
function logout() {
  token.value = null;
  currentUser.value = null;
  isLoggedIn.value = false;
  users.value = [];
  products.value = [];
  userOrders.value = [];
  adminOrders.value = [];
  cartItems.value = [];
  cartTotal.value = 0;
  
  localStorage.removeItem('token');
  localStorage.removeItem('user');
  
  showNotification('Вы вышли из системы', 'success');
}

// Загрузка данных для админа
async function loadAdminData() {
  if (currentUser.value?.role !== 'ADMIN') return;
  
  try {
    // Загрузка пользователей
    const usersResponse = await fetch(`${API_URL}/admin/users`, {
      headers: getAuthHeaders()
    });
    
    if (usersResponse.ok) {
      users.value = await usersResponse.json();
    }
    
    // Загрузка товаров
    const productsResponse = await fetch(`${API_URL}/products`, {
      headers: {
        'Content-Type': 'application/json'
      }
    });
    
    if (productsResponse.ok) {
      products.value = await productsResponse.json();
    }

    await loadAdminOrders();
  } catch (error) {
    console.error('Error loading admin data:', error);
  }
}

// Управление пользователями (админ)
async function updateUserStatus(userId: number) {
  try {
    const response = await fetch(`${API_URL}/admin/users/${userId}/status`, {
      method: 'PUT',
      headers: getAuthHeaders(),
      body: JSON.stringify({ status: userStatusForm.status })
    });

    if (response.ok) {
      showNotification('Статус пользователя обновлен', 'success');
      loadAdminData();
      selectedUser.value = null;
    } else {
      const data = await response.json();
      showNotification(data.error || 'Ошибка обновления статуса', 'error');
    }
  } catch (error) {
    showNotification('Ошибка подключения к серверу', 'error');
    console.error('Update user status error:', error);
  }
}

async function updateUserRole(userId: number) {
  try {
    const response = await fetch(`${API_URL}/admin/users/${userId}/role`, {
      method: 'PUT',
      headers: getAuthHeaders(),
      body: JSON.stringify({ role: userRoleForm.role })
    });

    if (response.ok) {
      showNotification('Роль пользователя обновлена', 'success');
      loadAdminData();
      selectedUser.value = null;
    } else {
      const data = await response.json();
      showNotification(data.error || 'Ошибка обновления роли', 'error');
    }
  } catch (error) {
    showNotification('Ошибка подключения к серверу', 'error');
    console.error('Update user role error:', error);
  }
}

// Управление товарами (админ)
async function createProduct() {
  try {
    const response = await fetch(`${API_URL}/admin/products`, {
      method: 'POST',
      headers: getAuthHeaders(),
      body: JSON.stringify(productForm)
    });

    if (response.ok) {
      showNotification('Товар создан', 'success');
      loadAdminData();
      // Сброс формы
      productForm.name = '';
      productForm.price = 0;
      productForm.image = '';
      productForm.category = 'GPU';
      productForm.description = '';
      productForm.stock = 0;
    } else {
      const data = await response.json();
      showNotification(data.error || 'Ошибка создания товара', 'error');
    }
  } catch (error) {
    showNotification('Ошибка подключения к серверу', 'error');
    console.error('Create product error:', error);
  }
}

async function updateProductImage(productId: number) {
  try {
    const response = await fetch(`${API_URL}/admin/products/${productId}/image`, {
      method: 'PUT',
      headers: getAuthHeaders(),
      body: JSON.stringify({ image: productImageForm.image })
    });

    if (response.ok) {
      showNotification('Изображение товара обновлено', 'success');
      loadAdminData();
      selectedProduct.value = null;
      productImageForm.image = '';
    } else {
      const data = await response.json();
      showNotification(data.error || 'Ошибка обновления изображения', 'error');
    }
  } catch (error) {
    showNotification('Ошибка подключения к серверу', 'error');
    console.error('Update product image error:', error);
  }
}

async function deleteProduct(productId: number) {
  if (!confirm('Вы уверены, что хотите удалить этот товар?')) {
    return;
  }

  try {
    const response = await fetch(`${API_URL}/admin/products/${productId}`, {
      method: 'DELETE',
      headers: getAuthHeaders()
    });

    if (response.ok) {
      showNotification('Товар удален', 'success');
      loadAdminData();
    } else {
      showNotification('Ошибка удаления товара', 'error');
    }
  } catch (error) {
    showNotification('Ошибка подключения к серверу', 'error');
    console.error('Delete product error:', error);
  }
}

async function createOrderWithPayment() {
  if (!currentUser.value || !token.value) {
    showNotification('Авторизуйтесь, чтобы оформить заказ', 'error');
    return;
  }
  isCreatingOrder.value = true;
  try {
    const response = await fetch(`${API_URL}/orders/${currentUser.value.id}`, {
      method: 'POST',
      headers: getAuthHeaders(),
      body: JSON.stringify(paymentForm)
    });

    const data = await response.json();

    if (response.ok) {
      showNotification('Заказ оформлен!', 'success');
      await loadUserOrders();
      await loadCartSummary();
      if (currentUser.value.role === 'ADMIN') {
        await loadAdminOrders();
      }
      paymentForm.deliveryAddress = '';
      paymentForm.cardNumber = '';
      paymentForm.cardExpiry = '';
      paymentForm.cardCvv = '';
    } else {
      showNotification(data.error || 'Ошибка оформления заказа', 'error');
    }
  } catch (error) {
    showNotification('Ошибка подключения к серверу', 'error');
    console.error('Create order error:', error);
  } finally {
    isCreatingOrder.value = false;
  }
}

// Вычисляемые свойства
const isAdmin = computed(() => currentUser.value?.role === 'ADMIN');
const isUser = computed(() => currentUser.value?.role === 'USER');
</script>

<template> 
  <div class="min-h-screen" style="background-image: url('./src/assets/fon.png'); background-size: cover; background-position: center;">
    <!-- Шапка -->
    <div class="flex flex-row items-center justify-center gap-3 p-4 border-8 w-full bg-blue-200 border-red-300">
  <div class="flex items-center gap-3"> 
        <img src="@/assets/shop.png" alt="Логотип" class="w-10 h-10 object-contain" />
        <span class="text-2xl font-bold text-gray-800">GameShop</span>
      </div>
      <RouterLink to="/" class="hover:text-blue-600 transition border-2 bg-pink-300/50 border-blue-300 rounded-full pl-8 w-40 h-13 flex flex-row items-center ml-4">
        <span class="flex flex-col items-center text-2xl font-bold text-gray-800">Главная</span>
      </RouterLink>
    </div>

    <!-- Уведомления -->
    <div
      v-if="notification.message"
      :class="[
        'fixed bottom-4 right-4 p-4 rounded-md shadow-md z-50',
        notification.type === 'success' ? 'bg-green-500 text-white' : 'bg-red-500 text-white'
      ]"
    >
      {{ notification.message }}
      </div>

    <!-- Если не авторизован - форма входа/регистрации -->
    <div v-if="!isLoggedIn" class="h-screen w-full p-10 flex flex-col items-center justify-center">
      <div class="flex flex-col items-center justify-center w-full max-w-md bg-white border-8 rounded-3xl border-double border-blue-400 hover:border-red-500 p-8">
        <h2 class="text-5xl font-bold mb-10">{{ isRegisterMode ? 'Регистрация' : 'Вход' }}</h2>

        <!-- Форма входа -->
        <form v-if="!isRegisterMode" @submit.prevent="login" class="w-full">
          <div class="mb-10">
            <label for="login-email" class="block text-sm font-medium mb-2">Электронная почта</label>
            <input
              id="login-email"
              v-model="loginForm.email"
              type="email"
              placeholder="Введите электронную почту"
              required
              class="w-full h-12 rounded-3xl px-4 py-2 border border-gray-500 focus:outline-none focus:border-blue-500"
            />
    </div>   
    
          <div class="mb-7">
            <label for="login-password" class="block text-sm font-medium mb-2">Пароль</label>
            <input
              id="login-password"
              v-model="loginForm.password"
              type="password"
              placeholder="Введите пароль"
              required
              class="w-full h-12 rounded-3xl px-4 py-2 border border-gray-500 focus:outline-none focus:border-blue-500"
            />
          </div>

          <button
            type="submit"
            class="w-full bg-blue-500 text-white px-4 py-2 rounded-3xl hover:bg-red-600 transition delay-150 duration-300 ease-in-out hover:scale-110 mb-4"
          >
            Войти
          </button>
        </form>

        <!-- Форма регистрации -->
        <form v-else @submit.prevent="register" class="w-full">
          <div class="mb-6">
            <label for="register-email" class="block text-sm font-medium mb-2">Электронная почта</label>
        <input
              id="register-email"
              v-model="registerForm.email"
          type="email"
          placeholder="Введите электронную почту"
              required
              class="w-full h-12 rounded-3xl px-4 py-2 border border-gray-500 focus:outline-none focus:border-blue-500"
        />
      </div>

          <div class="mb-6">
            <label for="register-password" class="block text-sm font-medium mb-2">Пароль</label>
        <input
              id="register-password"
              v-model="registerForm.password"
              type="password"
          placeholder="Введите пароль"
              required
              class="w-full h-12 rounded-3xl px-4 py-2 border border-gray-500 focus:outline-none focus:border-blue-500"
            />
          </div>

          <div class="mb-7">
            <label for="register-confirm" class="block text-sm font-medium mb-2">Подтвердите пароль</label>
            <input
              id="register-confirm"
              v-model="registerForm.confirmPassword"
              type="password"
              placeholder="Подтвердите пароль"
              required
              class="w-full h-12 rounded-3xl px-4 py-2 border border-gray-500 focus:outline-none focus:border-blue-500"
            />
          </div>

          <button
            type="submit"
            class="w-full bg-blue-500 text-white px-4 py-2 rounded-3xl hover:bg-red-600 transition delay-150 duration-300 ease-in-out hover:scale-110 mb-4"
          >
            Зарегистрироваться
          </button>
        </form>

        <!-- Переключение между входом и регистрацией -->
        <button
          @click="isRegisterMode = !isRegisterMode"
          class="w-full bg-transparent border border-gray-500 px-4 py-2 rounded hover:bg-gray-500 transition duration-300"
        >
          {{ isRegisterMode ? 'Уже есть аккаунт? Войти' : 'Нет аккаунта? Зарегистрироваться' }}
        </button>
      </div>
    </div>

    <!-- Если авторизован - панель пользователя/админа -->
    <div v-else class="p-10">
      <!-- Информация о пользователе -->
      <div class="bg-white border-8 rounded-3xl border-double border-blue-400 p-8 mb-6">
        <div class="flex justify-between items-center">
          <div>
            <h2 class="text-3xl font-bold mb-2">Личный кабинет</h2>
            <p class="text-lg"><strong>Email:</strong> {{ currentUser?.email }}</p>
            <p class="text-lg"><strong>Роль:</strong> {{ currentUser?.role === 'ADMIN' ? 'Администратор' : 'Пользователь' }}</p>
            <p class="text-lg"><strong>Статус:</strong> {{ currentUser?.status === 'ACTIVE' ? 'Активен' : 'Заблокирован' }}</p>
          </div>
          <button
            @click="logout"
            class="bg-red-500 text-white px-6 py-3 rounded-3xl hover:bg-red-600 transition delay-150 duration-300 ease-in-out hover:scale-110"
          >
            Выйти
          </button>
        </div>
      </div>

      <!-- Панель администратора -->
      <div v-if="isAdmin" class="space-y-6">
        <!-- Управление пользователями -->
        <div class="bg-white border-8 rounded-3xl border-double border-blue-400 p-8">
          <h3 class="text-2xl font-bold mb-6">Управление пользователями</h3>
          
          <div class="overflow-x-auto mb-6">
            <table class="w-full border-collapse">
              <thead>
                <tr class="bg-gray-200">
                  <th class="border border-gray-400 px-4 py-2">ID</th>
                  <th class="border border-gray-400 px-4 py-2">Email</th>
                  <th class="border border-gray-400 px-4 py-2">Роль</th>
                  <th class="border border-gray-400 px-4 py-2">Статус</th>
                  <th class="border border-gray-400 px-4 py-2">Действия</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="user in users" :key="user.id" class="hover:bg-gray-100">
                  <td class="border border-gray-400 px-4 py-2">{{ user.id }}</td>
                  <td class="border border-gray-400 px-4 py-2">{{ user.email }}</td>
                  <td class="border border-gray-400 px-4 py-2">{{ user.role }}</td>
                  <td class="border border-gray-400 px-4 py-2">{{ user.status }}</td>
                  <td class="border border-gray-400 px-4 py-2">
        <button
                      @click="selectedUser = user; userStatusForm.status = user.status; userRoleForm.role = user.role"
                      class="bg-blue-500 text-white px-3 py-1 rounded hover:bg-blue-600 transition"
                    >
                      Изменить
                    </button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>

          <!-- Модальное окно изменения пользователя -->
          <div v-if="selectedUser" class="mt-6 p-4 bg-gray-100 rounded-lg">
            <h4 class="font-bold mb-4">Изменение пользователя: {{ selectedUser.email }}</h4>
            
            <div class="mb-4">
              <label class="block mb-2">Статус:</label>
              <select v-model="userStatusForm.status" class="w-full h-10 rounded px-4 border border-gray-500">
                <option value="ACTIVE">Активен</option>
                <option value="BLOCKED">Заблокирован</option>
              </select>
              <button
                @click="updateUserStatus(selectedUser.id)"
                class="mt-2 bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600 transition"
        >
                Обновить статус
        </button>
      </div>

            <div class="mb-4">
              <label class="block mb-2">Роль:</label>
              <select v-model="userRoleForm.role" class="w-full h-10 rounded px-4 border border-gray-500">
                <option value="USER">Пользователь</option>
                <option value="ADMIN">Администратор</option>
              </select>
              <button
                @click="updateUserRole(selectedUser.id)"
                class="mt-2 bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600 transition"
              >
                Обновить роль
              </button>
      </div>

      <button
              @click="selectedUser = null"
              class="bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-600 transition"
      >
              Закрыть
      </button>
          </div>
  </div>

        <!-- Управление товарами -->
        <div class="bg-white border-8 rounded-3xl border-double border-blue-400 p-8">
          <h3 class="text-2xl font-bold mb-6">Управление товарами</h3>

          <!-- Форма создания товара -->
          <div class="mb-6 p-4 bg-gray-100 rounded-lg">
            <h4 class="font-bold mb-4">Создать новый товар</h4>
            <div class="grid grid-cols-2 gap-4">
              <div>
                <label class="block mb-2">Название:</label>
                <input v-model="productForm.name" type="text" class="w-full h-10 rounded px-4 border border-gray-500" />
              </div>
              <div>
                <label class="block mb-2">Цена:</label>
                <input v-model.number="productForm.price" type="number" class="w-full h-10 rounded px-4 border border-gray-500" />
              </div>
              <div>
                <label class="block mb-2">Изображение (URL):</label>
                <input v-model="productForm.image" type="text" class="w-full h-10 rounded px-4 border border-gray-500" />
              </div>
              <div>
                <label class="block mb-2">Категория:</label>
                <select v-model="productForm.category" class="w-full h-10 rounded px-4 border border-gray-500">
                  <option value="GPU">Видеокарта</option>
                  <option value="MOTHERBOARD">Материнская плата</option>
                  <option value="RAM">Оперативная память</option>
                  <option value="CPU">Процессор</option>
                  <option value="POWER">Блок питания</option>
                  <option value="SSD">SSD накопитель</option>
                </select>
              </div>
              <div>
                <label class="block mb-2">Описание:</label>
                <input v-model="productForm.description" type="text" class="w-full h-10 rounded px-4 border border-gray-500" />
              </div>
              <div>
                <label class="block mb-2">Количество на складе:</label>
                <input v-model.number="productForm.stock" type="number" class="w-full h-10 rounded px-4 border border-gray-500" />
              </div>
            </div>
      <button
              @click="createProduct"
              class="mt-4 bg-blue-500 text-white px-6 py-2 rounded hover:bg-blue-600 transition"
      >
              Создать товар
      </button>
    </div>
   
          <!-- Список товаров -->
          <div class="grid grid-cols-3 gap-4">
            <div v-for="product in products" :key="product.id" class="bg-gray-100 p-4 rounded-lg">
              <img :src="product.image" :alt="product.name" class="w-full h-40 object-cover mb-2 rounded" />
              <h4 class="font-bold">{{ product.name }}</h4>
              <p class="text-sm text-gray-600">{{ product.price }} ₽</p>
              <p class="text-xs text-gray-500">Категория: {{ product.category }}</p>
              <p class="text-xs text-gray-500">На складе: {{ product.stock }}</p>
              
              <div class="mt-2 space-y-2">
                <button
                  @click="selectedProduct = product; productImageForm.image = product.image"
                  class="w-full bg-yellow-500 text-white px-3 py-1 rounded hover:bg-yellow-600 transition text-sm"
                >
                  Изменить изображение
                </button>
                <button
                  @click="deleteProduct(product.id)"
                  class="w-full bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600 transition text-sm"
                >
                  Удалить
                </button>
              </div>
            </div>
  </div>
  
          <!-- Модальное окно изменения изображения -->
          <div v-if="selectedProduct" class="mt-6 p-4 bg-gray-100 rounded-lg">
            <h4 class="font-bold mb-4">Изменение изображения: {{ selectedProduct.name }}</h4>
            <div class="mb-4">
              <label class="block mb-2">URL изображения:</label>
              <input v-model="productImageForm.image" type="text" class="w-full h-10 rounded px-4 border border-gray-500" />
            </div>
            <div class="flex gap-2">
              <button
                @click="updateProductImage(selectedProduct.id)"
                class="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600 transition"
              >
                Обновить изображение
              </button>
              <button
                @click="selectedProduct = null; productImageForm.image = ''"
                class="bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-600 transition"
              >
                Закрыть
              </button>
            </div>
          </div>
        </div>

        <!-- Все заказы клиентов -->
        <div class="bg-white border-8 rounded-3xl border-double border-blue-400 p-8">
          <h3 class="text-2xl font-bold mb-6">Все заказы клиентов</h3>
          <p class="text-sm text-gray-500 mb-4">Отображаются все заказы сайта — администратор видит способ оплаты, сумму и адрес доставки.</p>
          <div v-if="adminOrders.length > 0" class="space-y-4 max-h-[600px] overflow-y-auto pr-2">
            <div
              v-for="order in adminOrders"
              :key="order.id"
              class="border border-gray-200 rounded-2xl p-4 shadow-sm bg-gray-50"
            >
              <div class="flex flex-wrap justify-between gap-2 mb-2">
                <div>
                  <p class="font-semibold">Заказ №{{ order.id }}</p>
                  <p class="text-sm text-gray-500">Пользователь: {{ order.userEmail || '—' }} (ID {{ order.userId }})</p>
                  <p class="text-sm text-gray-500">Создан: {{ order.createdAt ? new Date(order.createdAt).toLocaleString() : '—' }}</p>
                </div>
                <div class="text-right">
                  <p class="font-semibold text-lg">{{ order.totalPrice.toFixed(2) }} ₽</p>
                  <span class="px-3 py-1 rounded-full text-sm bg-purple-100 text-purple-700">{{ order.status }}</span>
                </div>
              </div>
              <div class="grid grid-cols-1 md:grid-cols-2 gap-3 text-sm text-gray-700">
                <p><strong>Чем оплачивать:</strong> {{ order.paymentMethod || '—' }}</p>
                <p><strong>Как оплачивать:</strong> {{ order.paymentType || '—' }}</p>
                <p><strong>Адрес доставки:</strong> {{ order.deliveryAddress || 'Не указан' }}</p>
                <p><strong>Карта:</strong> {{ order.cardNumber || '—' }} {{ order.cardExpiry ? '(' + order.cardExpiry + ')' : '' }}</p>
              </div>
              <div class="mt-3">
                <p class="font-semibold mb-1">Товары:</p>
                <ul class="space-y-1 text-sm text-gray-600">
                  <li v-for="item in order.items" :key="`${order.id}-${item.productId}`">
                    {{ item.productName }} × {{ item.quantity }} — {{ item.price }} ₽
                  </li>
                </ul>
              </div>
            </div>
          </div>
          <p v-else class="text-gray-500">Заказов пока нет.</p>
</div>
 
        <!-- Заказы по пользователям -->
        <div class="bg-white border-8 rounded-3xl border-double border-blue-400 p-8">
          <h3 class="text-2xl font-bold mb-6">Заказы пользователей</h3>
          <div class="space-y-4 max-h-[500px] overflow-y-auto pr-2">
            <div
              v-for="user in users"
              :key="user.id"
              class="border border-dashed border-blue-200 rounded-2xl p-4 bg-gray-50"
            >
              <div class="flex flex-wrap justify-between items-center mb-2">
                <div>
                  <p class="font-semibold">{{ user.email }}</p>
                  <p class="text-sm text-gray-500">Роль: {{ user.role }} · Статус: {{ user.status }}</p>
                </div>
                <span class="text-sm text-blue-600">
                  {{ getOrdersForUser(user.id).length }} заказ(ов)
                </span>
              </div>
              <div v-if="getOrdersForUser(user.id).length > 0" class="space-y-2 text-sm text-gray-700">
                <div
                  v-for="order in getOrdersForUser(user.id)"
                  :key="`${user.id}-order-${order.id}`"
                  class="bg-white rounded-xl border border-gray-200 p-3"
                >
                  <div class="flex justify-between">
                    <span>Заказ №{{ order.id }}</span>
                    <span>{{ order.totalPrice.toFixed(2) }} ₽</span>
                  </div>
                  <p class="text-gray-500 text-xs">Статус: {{ order.status }} · {{ order.createdAt ? new Date(order.createdAt).toLocaleDateString() : '—' }}</p>
                </div>
              </div>
              <p v-else class="text-gray-500 text-sm">Этот пользователь ещё не оформлял заказов.</p>
            </div>
          </div>
        </div>
      </div>

      <!-- Панель обычного пользователя -->
      <div v-else-if="isUser" class="bg-white border-8 rounded-3xl border-double border-blue-400 p-8">
        <h3 class="text-2xl font-bold mb-6">Мои заказы и оплата</h3>
        <div class="grid grid-cols-1 lg:grid-cols-2 gap-8">
          <div class="bg-gray-50 p-6 rounded-2xl border border-dashed border-blue-200">
            <h4 class="text-xl font-semibold mb-4">Оформление заказа</h4>
            <p class="text-sm text-gray-500 mb-4">Укажите способ оплаты и доставки. Проверка банковских данных отключена — можно вводить любые значения.</p>
            <div class="space-y-4">
              <div>
                <label class="block mb-2 font-medium">Чем оплачивать</label>
                <select v-model="paymentForm.paymentMethod" class="w-full h-12 rounded-3xl px-4 border border-gray-400 focus:outline-none focus:border-blue-500">
                  <option v-for="method in paymentMethods" :key="method" :value="method">{{ method }}</option>
                </select>
              </div>
              <div>
                <label class="block mb-2 font-medium">Как оплачивать</label>
                <select v-model="paymentForm.paymentType" class="w-full h-12 rounded-3xl px-4 border border-gray-400 focus:outline-none focus:border-blue-500">
                  <option v-for="type in paymentTypes" :key="type" :value="type">{{ type }}</option>
                </select>
              </div>
              <div>
                <label class="block mb-2 font-medium">Сумма заказа</label>
                <input
                  :value="`${cartTotal.toFixed(2)} ₽`"
                  type="text"
                  readonly
                  class="w-full h-12 rounded-3xl px-4 border border-gray-300 bg-gray-100 text-gray-600"
                />
                <p class="text-xs text-gray-500 mt-1">Сумма рассчитывается автоматически по товарам в вашей корзине.</p>
              </div>
              <div>
                <label class="block mb-2 font-medium">Место доставки</label>
                <input
                  v-model="paymentForm.deliveryAddress"
                  type="text"
                  placeholder="Город, улица, дом, квартира"
                  class="w-full h-12 rounded-3xl px-4 border border-gray-400 focus:outline-none focus:border-blue-500"
                />
              </div>
              <div>
                <label class="block mb-2 font-medium">Номер карты (до 12 цифр)</label>
                <input
                  v-model="paymentForm.cardNumber"
                  type="text"
                  maxlength="12"
                  placeholder="000000000000"
                  class="w-full h-12 rounded-3xl px-4 border border-gray-400 focus:outline-none focus:border-blue-500"
                />
              </div>
              <div class="grid grid-cols-2 gap-4">
                <div>
                  <label class="block mb-2 font-medium">Срок (формат 00/00)</label>
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
              <button
                @click="createOrderWithPayment"
                :disabled="isCreatingOrder"
                class="w-full bg-blue-500 text-white px-6 py-3 rounded-3xl hover:bg-red-600 transition delay-150 duration-300 ease-in-out hover:scale-105 disabled:opacity-60"
              >
                {{ isCreatingOrder ? 'Оформляем...' : 'Оформить заказ' }}
              </button>
            </div>
          </div>

          <div class="bg-gray-50 p-6 rounded-2xl border border-dashed border-blue-200 max-h-[600px] overflow-y-auto">
            <h4 class="text-xl font-semibold mb-4">История заказов</h4>
            <div v-if="userOrders.length > 0" class="space-y-4">
              <div
                v-for="order in userOrders"
                :key="order.id"
                class="bg-white border border-gray-300 rounded-2xl p-4 shadow-sm"
              >
                <div class="flex justify-between items-center mb-2">
                  <div>
                    <p class="font-semibold">Заказ №{{ order.id }}</p>
                    <p class="text-sm text-gray-500">{{ order.createdAt ? new Date(order.createdAt).toLocaleString() : '—' }}</p>
                  </div>
                  <span class="px-3 py-1 rounded-full text-sm bg-blue-100 text-blue-700">
                    {{ order.status }}
                  </span>
                </div>
                <p class="text-gray-700"><strong>Сумма:</strong> {{ order.totalPrice.toFixed(2) }} ₽</p>
                <p class="text-gray-700"><strong>Оплата:</strong> {{ order.paymentMethod }} / {{ order.paymentType }}</p>
                <p class="text-gray-700" v-if="order.deliveryAddress"><strong>Доставка:</strong> {{ order.deliveryAddress }}</p>
                <div class="mt-3">
                  <p class="font-semibold mb-1">Товары:</p>
                  <ul class="space-y-1 text-sm text-gray-600">
                    <li v-for="item in order.items" :key="`${order.id}-${item.productId}`">
                      {{ item.productName }} × {{ item.quantity }} — {{ item.price }} ₽
                    </li>
                  </ul>
                </div>
              </div>
            </div>
            <p v-else class="text-gray-500">У вас еще нет оформленных заказов.</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
