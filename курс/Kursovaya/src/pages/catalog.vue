<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue';
import { useCalc } from '@/components/composition/calc.ts';
import { useBuyList } from '@/components/composition/BuyList.ts';

interface Product {
  id: number;
  name: string;
  price: number;
  image: string;
  category: string;
  description: string;
  stock: number;
}

interface User {
  id: number;
  email: string;
  role: string;
  status: string;
}

const {
    selectedData,
    addSelectedData,
    removeSelectedData,
    getKeyByItemId,
    partsMotherboards,
    partsCpu,
    partsGpu,
    partsRam,
    getSelectedItems,
} = useCalc();

const { addToBuyList } = useBuyList();

// API
const API_URL = 'http://localhost:8080/api';
const products = ref<Product[]>([]);
const currentUser = ref<User | null>(null);
const token = ref<string | null>(null);
const notification = reactive<{ message: string; type: 'success' | 'error' }>({
  message: '',
  type: 'success'
});

// Поиск и фильтрация
const searchQuery = ref('');
const isSearchModalOpen = ref(false);
const searchParams = reactive({
  searchBy: 'name', // name, category, price, stock
  category: '',
  minPrice: '',
  maxPrice: '',
  minStock: ''
});

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

// Загрузка товаров из API
async function loadProducts() {
  try {
    const response = await fetch(`${API_URL}/products`, {
      headers: {
        'Content-Type': 'application/json'
      }
    });
    
    if (response.ok) {
      products.value = await response.json();
    }
  } catch (error) {
    console.error('Error loading products:', error);
  }
}

// Добавление товара в корзину (backend)
async function addToCart(productId: number, quantity: number = 1) {
  if (!currentUser.value || !token.value) {
    showNotification('Авторизуйтесь, чтобы добавить товар в корзину', 'error');
    return;
  }

  try {
    const response = await fetch(`${API_URL}/cart/add`, {
      method: 'POST',
      headers: getAuthHeaders(),
      body: JSON.stringify({
        userId: currentUser.value.id,
        productId: productId,
        quantity: quantity
      })
    });

    if (response.ok) {
      showNotification('Товар добавлен в корзину!', 'success');
      // Также добавляем в локальную корзину для совместимости
      const product = products.value.find(p => p.id === productId);
      if (product) {
        addToBuyList({
          id: productId.toString(),
          name: product.name,
          price: product.price,
          image: product.image
        });
      }
    } else {
      const data = await response.json();
      showNotification(data.error || 'Ошибка добавления в корзину', 'error');
    }
  } catch (error) {
    showNotification('Ошибка подключения к серверу', 'error');
    console.error('Add to cart error:', error);
  }
}

// Функции для поиска
function openSearchModal() {
  isSearchModalOpen.value = true;
}

function closeSearchModal() {
  isSearchModalOpen.value = false;
}

function applySearch() {
  closeSearchModal();
}

function resetSearch() {
  searchQuery.value = '';
  searchParams.searchBy = 'name';
  searchParams.category = '';
  searchParams.minPrice = '';
  searchParams.maxPrice = '';
  searchParams.minStock = '';
}

// Фильтрация товаров
const filteredProducts = computed(() => {
  let filtered = [...products.value];

  // Фильтр по поисковому запросу
  if (searchQuery.value.trim()) {
    const query = searchQuery.value.toLowerCase().trim();
    filtered = filtered.filter(product => {
      switch (searchParams.searchBy) {
        case 'name':
          return product.name.toLowerCase().includes(query);
        case 'category':
          return product.category.toLowerCase().includes(query);
        case 'description':
          return product.description.toLowerCase().includes(query);
        default:
          return product.name.toLowerCase().includes(query);
      }
    });
  }

  // Фильтр по категории
  if (searchParams.category) {
    filtered = filtered.filter(product => product.category === searchParams.category);
  }

  // Фильтр по цене
  if (searchParams.minPrice) {
    const minPrice = parseFloat(searchParams.minPrice);
    if (!isNaN(minPrice)) {
      filtered = filtered.filter(product => product.price >= minPrice);
    }
  }
  if (searchParams.maxPrice) {
    const maxPrice = parseFloat(searchParams.maxPrice);
    if (!isNaN(maxPrice)) {
      filtered = filtered.filter(product => product.price <= maxPrice);
    }
  }

  // Фильтр по наличию на складе
  if (searchParams.minStock) {
    const minStock = parseInt(searchParams.minStock);
    if (!isNaN(minStock)) {
      filtered = filtered.filter(product => product.stock >= minStock);
    }
  }

  return filtered;
});

// Группировка товаров по категориям
const productsByCategory = computed(() => {
  const grouped: Record<string, Product[]> = {
    MOTHERBOARD: [],
    CPU: [],
    GPU: [],
    RAM: [],
    POWER: [],
    SSD: []
  };
  
  filteredProducts.value.forEach(product => {
    if (grouped[product.category]) {
      grouped[product.category].push(product);
    }
  });
  
  return grouped;
});

onMounted(() => {
  // Загрузка пользователя из localStorage
  const savedUser = localStorage.getItem('user');
  const savedToken = localStorage.getItem('token');
  
  if (savedUser && savedToken) {
    currentUser.value = JSON.parse(savedUser);
    token.value = savedToken;
  }
  
  loadProducts();
});
</script>

<template>
  <div class="flex flex-col bg-pink-100  items-center justify-center">
    <div class="flex flex-row gap-4 p-5 justify-center ">
      <RouterLink
        to="/"
        class="hover:text-blue-600 transition border-2 bg-pink-300/50 border-blue-300 rounded-full px-8 py-3 text-2xl font-bold font-mono">
        Главная
      </RouterLink>
      <RouterLink
        to="/Corzina"
        class="hover:text-blue-600 transition border-2 bg-pink-300/50 border-blue-300 rounded-full px-8 py-3 text-2xl font-bold font-mono">
        Корзина
      </RouterLink>
    </div>


</div>

  <div class="flex flex-col gap-5 p-5 min-h-screen" style="background-color: #fffbeb;">
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

    <!-- Строка поиска -->
    <div class="flex flex-row gap-4 items-center justify-center mb-5">
      <div class="relative flex-1 max-w-2xl">
        <input
          v-model="searchQuery"
          type="text"
          placeholder="Поиск товаров..."
          @click="openSearchModal"
          class="w-full px-6 py-4 text-lg border-2 bg-pink-300/50 border-blue-300 rounded-full focus:outline-none focus:border-blue-500 transition"
        />
        <button
          @click="openSearchModal"
          class="absolute right-3 top-1/2 transform -translate-y-1/2 px-4 py-2 bg-blue-500 text-white rounded-full hover:bg-blue-600 transition"
        >
          ⚙️
        </button>
      </div>
      <button
        v-if="searchQuery || searchParams.category || searchParams.minPrice || searchParams.maxPrice || searchParams.minStock"
        @click="resetSearch"
        class="px-6 py-4 text-lg border-2 bg-pink-300/50 border-blue-300 rounded-full hover:text-blue-600 transition font-bold font-mono"
      >
        Сбросить
      </button>
    </div>

    <!-- Модальное окно выбора параметров поиска -->
    <div
      v-if="isSearchModalOpen"
      class="fixed inset-0 backdrop-blur-lg bg-opacity-70 flex items-center justify-center z-70"
      @click.self="closeSearchModal"
    >
      <div class="bg-white rounded-lg p-8 max-w-md w-full mx-4 shadow-xl" style="background-color: #F8F8FF;">
        <div class="flex justify-between items-center mb-6">
          <h2 class="text-2xl font-bold font-mono">Параметры поиска</h2>
          <button
            @click="closeSearchModal"
            class="text-2xl hover:text-red-500 transition"
          >
            ×
          </button>
        </div>

        <div class="space-y-9 ">
          <!-- Фильтр по категории -->
          <div>
            <label class="block text-lg font-semibold mb-2">Категория:</label>
            <select
              v-model="searchParams.category"
              class="w-full px-4 py-3 border-2 bg-pink-300/50 border-blue-300 rounded-full focus:outline-none focus:border-blue-500 transition"
            >
              <option value="">Все категории</option>
              <option value="MOTHERBOARD">Материнские платы</option>
              <option value="CPU">Процессоры</option>
              <option value="GPU">Видеокарты</option>
              <option value="RAM">Оперативная память</option>
              <option value="POWER">Блоки питания</option>
              <option value="SSD">SSD накопители</option>
            </select>
          </div>

          <!-- Фильтр по цене -->
          <div>
            <label class="block text-lg font-semibold mb-2">Цена:</label>
            <div class="flex gap-2">
              <input
                v-model="searchParams.minPrice"
                type="number"
                placeholder="От"
                class="flex-1 px-4 py-3 border-2 w-40 bg-pink-300/50 border-blue-300 rounded-full focus:outline-none focus:border-blue-500 transition"
              />
              <input
                v-model="searchParams.maxPrice"
                type="number"
                placeholder="До"
                class="flex-1 px-2 py-1 border-2 w-40 bg-pink-300/50 border-blue-300 rounded-full focus:outline-none focus:border-blue-500 transition"
              />
            </div>
          </div>
        </div>

        <div class="flex gap-4 mt-6">
          <button
            @click="applySearch"
            class="flex-1 px-6 py-3 bg-blue-500 text-white rounded-full hover:bg-blue-600 transition font-bold font-mono"
          >
            Применить
          </button>
          <button
            @click="resetSearch"
            class="flex-1 px-6 py-3 border-2 bg-pink-300/50 border-blue-300 rounded-full hover:text-blue-600 transition font-bold font-mono"
          >
            Сбросить
          </button>
        </div>
      </div>
    </div>

    <!-- Блок "Выбранные товары" -->
    <div v-if="getSelectedItems.length > 0" class="bg-gray-100 p-5 rounded-lg">
      <h2 class="text-2xl font-bold mb-5">Выбранные товары</h2>
      <div class="grid grid-cols-3 gap-5">
        <div
          v-for="item in getSelectedItems"
          :key="item.id"
          class="bg-white p-4 rounded-lg shadow-md"
        >
          <img :src="item.image" :alt="item.name" class="w-full h-40 object-cover mb-2" />
          <h3 class="text-lg font-medium">{{ item.name }}</h3>
          <p class="text-sm text-gray-600">{{ item.price }} ₽</p>
          <button @click="removeSelectedData(getKeyByItemId(item.id) || ('motherboard' as keyof typeof selectedData))" class="mt-2 px-4 py-1 bg-red-500 text-white rounded-md transition delay-150 duration-300 ease-in-out hover:-translate hover:scale-110">
            Удалить
          </button>
        </div>
      </div>
    </div>

    <!-- Каталог товаров из API -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
      <!-- Материнские платы -->
      <div v-if="productsByCategory.MOTHERBOARD.length > 0" class="bg-gray-200 p-5 rounded-lg">
        <h2 class="text-2xl font-bold mb-5">Материнские платы</h2>
        <div
          v-for="product in productsByCategory.MOTHERBOARD"
          :key="product.id"
          class="bg-white p-4 rounded-lg shadow-md mb-4"
        >
          <img :src="product.image" :alt="product.name" class="w-full h-40 object-cover mb-2 rounded" />
          <h3 class="text-lg font-medium">{{ product.name }}</h3>
          <p class="text-sm text-gray-600">{{ product.price }} ₽</p>
          <p class="text-xs text-gray-500 mb-2">На складе: {{ product.stock }}</p>
          <button 
            @click="addToCart(product.id)" 
            :disabled="product.stock === 0"
            class="mt-2 px-4 py-1 bg-blue-500 text-white rounded-md transition delay-150 duration-300 ease-in-out hover:-translate hover:scale-110 disabled:opacity-50 disabled:cursor-not-allowed"
          >
            {{ product.stock > 0 ? 'В корзину' : 'Нет в наличии' }}
          </button>
        </div>
      </div>

      <!-- Процессоры -->
      <div v-if="productsByCategory.CPU.length > 0" class="bg-gray-200 p-5 rounded-lg">
        <h2 class="text-2xl font-bold mb-5">Процессоры</h2>
        <div
          v-for="product in productsByCategory.CPU"
          :key="product.id"
          class="bg-white p-4 rounded-lg shadow-md mb-4"
        >
          <img :src="product.image" :alt="product.name" class="w-full h-40 object-cover mb-2 rounded" />
          <h3 class="text-lg font-medium">{{ product.name }}</h3>
          <p class="text-sm text-gray-600">{{ product.price }} ₽</p>
          <p class="text-xs text-gray-500 mb-2">На складе: {{ product.stock }}</p>
          <button 
            @click="addToCart(product.id)" 
            :disabled="product.stock === 0"
            class="mt-2 px-4 py-1 bg-blue-500 text-white rounded-md transition delay-150 duration-300 ease-in-out hover:-translate hover:scale-110 disabled:opacity-50 disabled:cursor-not-allowed"
          >
            {{ product.stock > 0 ? 'В корзину' : 'Нет в наличии' }}
          </button>
        </div>
      </div>

      <!-- Видеокарты -->
      <div v-if="productsByCategory.GPU.length > 0" class="bg-gray-200 p-5 rounded-lg">
        <h2 class="text-2xl font-bold mb-5">Видеокарты</h2>
        <div
          v-for="product in productsByCategory.GPU"
          :key="product.id"
          class="bg-white p-4 rounded-lg shadow-md mb-4"
        >
          <img :src="product.image" :alt="product.name" class="w-full h-40 object-cover mb-2 rounded" />
          <h3 class="text-lg font-medium">{{ product.name }}</h3>
          <p class="text-sm text-gray-600">{{ product.price }} ₽</p>
          <p class="text-xs text-gray-500 mb-2">На складе: {{ product.stock }}</p>
          <button 
            @click="addToCart(product.id)" 
            :disabled="product.stock === 0"
            class="mt-2 px-4 py-1 bg-blue-500 text-white rounded-md transition delay-150 duration-300 ease-in-out hover:-translate hover:scale-110 disabled:opacity-50 disabled:cursor-not-allowed"
          >
            {{ product.stock > 0 ? 'В корзину' : 'Нет в наличии' }}
          </button>
        </div>
      </div>

      <!-- Оперативная память -->
      <div v-if="productsByCategory.RAM.length > 0" class="bg-gray-200 p-5 rounded-lg">
        <h2 class="text-2xl font-bold mb-5">Оперативная память</h2>
        <div
          v-for="product in productsByCategory.RAM"
          :key="product.id"
          class="bg-white p-4 rounded-lg shadow-md mb-4"
        >
          <img :src="product.image" :alt="product.name" class="w-full h-40 object-cover mb-2 rounded" />
          <h3 class="text-lg font-medium">{{ product.name }}</h3>
          <p class="text-sm text-gray-600">{{ product.price }} ₽</p>
          <p class="text-xs text-gray-500 mb-2">На складе: {{ product.stock }}</p>
          <button 
            @click="addToCart(product.id)" 
            :disabled="product.stock === 0"
            class="mt-2 px-4 py-1 bg-blue-500 text-white rounded-md transition delay-150 duration-300 ease-in-out hover:-translate hover:scale-110 disabled:opacity-50 disabled:cursor-not-allowed"
          >
            {{ product.stock > 0 ? 'В корзину' : 'Нет в наличии' }}
          </button>
        </div>
      </div>

      <!-- Блоки питания -->
      <div v-if="productsByCategory.POWER.length > 0" class="bg-gray-200 p-5 rounded-lg">
        <h2 class="text-2xl font-bold mb-5">Блоки питания</h2>
        <div
          v-for="product in productsByCategory.POWER"
          :key="product.id"
          class="bg-white p-4 rounded-lg shadow-md mb-4"
        >
          <img :src="product.image" :alt="product.name" class="w-full h-40 object-cover mb-2 rounded" />
          <h3 class="text-lg font-medium">{{ product.name }}</h3>
          <p class="text-sm text-gray-600">{{ product.price }} ₽</p>
          <p class="text-xs text-gray-500 mb-2">На складе: {{ product.stock }}</p>
          <button 
            @click="addToCart(product.id)" 
            :disabled="product.stock === 0"
            class="mt-2 px-4 py-1 bg-blue-500 text-white rounded-md transition delay-150 duration-300 ease-in-out hover:-translate hover:scale-110 disabled:opacity-50 disabled:cursor-not-allowed"
          >
            {{ product.stock > 0 ? 'В корзину' : 'Нет в наличии' }}
          </button>
        </div>
      </div>

      <!-- SSD накопители -->
      <div v-if="productsByCategory.SSD.length > 0" class="bg-gray-200 p-5 rounded-lg">
        <h2 class="text-2xl font-bold mb-5">SSD накопители</h2>
        <div
          v-for="product in productsByCategory.SSD"
          :key="product.id"
          class="bg-white p-4 rounded-lg shadow-md mb-4"
        >
          <img :src="product.image" :alt="product.name" class="w-full h-40 object-cover mb-2 rounded" />
          <h3 class="text-lg font-medium">{{ product.name }}</h3>
          <p class="text-sm text-gray-600">{{ product.price }} ₽</p>
          <p class="text-xs text-gray-500 mb-2">На складе: {{ product.stock }}</p>
          <button 
            @click="addToCart(product.id)" 
            :disabled="product.stock === 0"
            class="mt-2 px-4 py-1 bg-blue-500 text-white rounded-md transition delay-150 duration-300 ease-in-out hover:-translate hover:scale-110 disabled:opacity-50 disabled:cursor-not-allowed"
          >
            {{ product.stock > 0 ? 'В корзину' : 'Нет в наличии' }}
          </button>
        </div>
      </div>
    </div>

    <!-- Если товаров нет -->
    <div v-if="products.length === 0" class="text-center text-gray-500 py-10">
      <p class="text-xl">Загрузка товаров...</p>
    </div>
  </div>
</template> 