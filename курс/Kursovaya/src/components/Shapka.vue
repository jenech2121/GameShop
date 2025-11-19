<script setup lang="ts">
import { ref, computed, onMounted, onBeforeUnmount } from 'vue';

interface User {
  id: number;
  email: string;
  role: string;
  status: string;
}

const currentUser = ref<User | null>(null);

function readStoredUser() {
  const savedUser = localStorage.getItem('user');
  currentUser.value = savedUser ? JSON.parse(savedUser) : null;
}

function handleAuthChange(event?: Event) {
  event?.preventDefault?.();
  readStoredUser();
}

const profileLabel = computed(() => {
  if (!currentUser.value) {
    return 'Личный Кабинет';
  }
  const userEmail = currentUser.value.email;
  return userEmail ? `Профиль ${userEmail}` : 'Профиль';
});

onMounted(() => {
  readStoredUser();
  window.addEventListener('storage', handleAuthChange);
  window.addEventListener('auth-changed', handleAuthChange);
});

onBeforeUnmount(() => {
  window.removeEventListener('storage', handleAuthChange);
  window.removeEventListener('auth-changed', handleAuthChange);
});
</script>



<template>
  <header class="w-full bg-white border-b border-gray-300 shadow-sm">
    <!-- Верхняя часть хедера -->
    <div class="flex justify-between items-center px-10 py-4">
      <!-- Логотип -->
      <div class="flex items-center gap-3">
        <img src="@/assets/shop.png" alt="Логотип" class="w-10 h-10 object-contain" />
        <span class="text-2xl font-bold text-gray-800">GameShop</span>
      </div>

      <!-- Навигационное меню -->
      <nav class="flex gap-8 text-lg font-medium text-gray-700 flex items-center gap-10">
        <RouterLink to="" class="hover:text-blue-600 transition">Главная</RouterLink>
        <RouterLink to="/catalog" class="hover:text-blue-600 transition">Каталог</RouterLink>
        <RouterLink to="/about" class="hover:text-blue-600 transition">О нас</RouterLink>
        <RouterLink to="/contacts" class="hover:text-blue-600 transition">Контакты</RouterLink>
        <RouterLink to="/admin-login" class="text-blue-600 font-semibold hover:underline transition">Админ вход</RouterLink>
      </nav>

      <!-- Иконки: поиск, профиль, корзина -->
      <div class="flex gap-7 items-center">
        <div class="bg-gray-300 rounded-full w-full max-w-3xl h-12 flex items-center justify-end px-4">
        <input class=" w-full text-black mr-2" type="search">
       
      </input>
      <button class="rounded-full  h-9 flex items-center justify-center gap-2 w-29 bg-blue-500 text-white px-4 py-2 rounded-3xl hover:bg-blue-600 transition delay-150 duration-300 ease-in-out hover:-translate hover:scale-110 " type="submit">Поиск 
        <svg class="w-5 h-5 text-black" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
       <path stroke-linecap="round" stroke-linejoin="round" d="M21 21l-4.35-4.35M10 18a8 8 0 100-16 8 8 0 000 16z"/>
       </svg>
      </button>
       </div>
       <div class="flex gap-2 items-center">
        <svg  xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" stroke-width="2" class="w-5 h-5 text-black">
        <path  stroke-linecap="round" stroke-linejoin="round" d="M2.25 2.25a.75.75 0 0 0 0 1.5h1.386c.17 0 .318.114.362.278l2.558 9.592a3.752 3.752 0 0 0-2.806 3.63c0 .414.336.75.75.75h15.75a.75.75 0 0 0 0-1.5H5.378A2.25 2.25 0 0 1 7.5 15h11.218a.75.75 0 0 0 .674-.421 60.358 60.358 0 0 0 2.96-7.228.75.75 0 0 0-.525-.965A60.864 60.864 0 0 0 5.68 4.509l-.232-.867A1.875 1.875 0 0 0 3.636 2.25H2.25ZM3.75 20.25a1.5 1.5 0 1 1 3 0 1.5 1.5 0 0 1-3 0ZM16.5 20.25a1.5 1.5 0 1 1 3 0 1.5 1.5 0 0 1-3 0Z" />
       </svg>
       
        <RouterLink to="/Corzina" class="hover:text-blue-600 transition text-lg font-medium text-gray-700">Корзина</RouterLink>
      </div>
      <div class="flex gap-1 items-center">
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" class="w-20 h-5 text-black">
        <path fill-rule="evenodd" d="M18.685 19.097A9.723 9.723 0 0 0 21.75 12c0-5.385-4.365-9.75-9.75-9.75S2.25 6.615 2.25 12a9.723 9.723 0 0 0 3.065 7.097A9.716 9.716 0 0 0 12 21.75a9.716 9.716 0 0 0 6.685-2.653Zm-12.54-1.285A7.486 7.486 0 0 1 12 15a7.486 7.486 0 0 1 5.855 2.812A8.224 8.224 0 0 1 12 20.25a8.224 8.224 0 0 1-5.855-2.438ZM15.75 9a3.75 3.75 0 1 1-7.5 0 3.75 3.75 0 0 1 7.5 0Z" clip-rule="evenodd" />
        </svg>

        <RouterLink to="/cabinet" class="hover:text-blue-600 transition text-lg font-medium text-gray-700">{{ profileLabel }}</RouterLink>
      </div>
      </div>
    </div>

    <!-- Нижняя часть хедера с категориями -->
    <div class="bg-gray-100 py-3 px-10">
      <div class="flex gap-10 flex justify-center text-sm font-medium text-gray-600 overflow-x-auto whitespace-nowrap">
        Coming soon...
      </div>
    </div>
  </header>
</template>