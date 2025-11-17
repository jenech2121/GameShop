<script setup lang="ts">
import { reactive } from 'vue';
import { useRouter, RouterLink } from 'vue-router';

interface Notification {
  message: string;
  type: 'success' | 'error';
}

const router = useRouter();
const API_URL = 'http://localhost:8080/api';

const loginForm = reactive({
  email: 'admin@gmail.com',
  password: 'admin'
});

const notification = reactive<Notification>({
  message: '',
  type: 'success'
});

function showNotification(message: string, type: 'success' | 'error') {
  notification.message = message;
  notification.type = type;
  setTimeout(() => {
    notification.message = '';
  }, 3000);
}

async function loginAdmin() {
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

    if (!response.ok) {
      showNotification(data.error || 'Неверные данные', 'error');
      return;
    }

    if (data.user.role !== 'ADMIN') {
      showNotification('У вас нет прав администратора', 'error');
      return;
    }

    localStorage.setItem('token', data.token);
    localStorage.setItem('user', JSON.stringify(data.user));
    showNotification('Добро пожаловать, администратор!', 'success');
    setTimeout(() => router.push('/cabinet'), 800);
  } catch (error) {
    console.error('Admin login error:', error);
    showNotification('Ошибка подключения к серверу', 'error');
  }
}
</script>

<template>
  <div class="min-h-screen" style="background-image: url('./src/assets/fon.png'); background-size: cover; background-position: center;">
    <div class="flex justify-between items-center px-10 py-6 bg-white shadow-md">
      <div class="flex items-center gap-3">
        <img src="@/assets/shop.png" alt="Логотип" class="w-10 h-10 object-contain" />
        <span class="text-2xl font-bold text-gray-800">GameShop · Admin</span>
      </div>
      <RouterLink to="/cabinet" class="text-blue-600 hover:underline text-lg">Вернуться к сайту</RouterLink>
    </div>

    <div
      v-if="notification.message"
      :class="[
        'fixed bottom-4 right-4 p-4 rounded-md shadow-md z-50',
        notification.type === 'success' ? 'bg-green-500 text-white' : 'bg-red-500 text-white'
      ]"
    >
      {{ notification.message }}
    </div>

    <div class="flex items-center justify-center py-16 px-4">
      <div class="w-full max-w-lg bg-white border-8 rounded-3xl border-double border-blue-400 p-10 shadow-lg">
        <h2 class="text-4xl font-bold text-center mb-6">Вход администратора</h2>
        <p class="text-center text-gray-500 mb-8">
          Используйте учётные данные <strong>admin@gmail.com / admin</strong> для доступа к панели управления.
        </p>
        <form @submit.prevent="loginAdmin" class="space-y-6">
          <div>
            <label class="block text-sm font-medium mb-2">Электронная почта</label>
            <input
              v-model="loginForm.email"
              type="email"
              required
              class="w-full h-12 rounded-3xl px-4 border border-gray-400 focus:outline-none focus:border-blue-500"
            />
          </div>
          <div>
            <label class="block text-sm font-medium mb-2">Пароль</label>
            <input
              v-model="loginForm.password"
              type="password"
              required
              class="w-full h-12 rounded-3xl px-4 border border-gray-400 focus:outline-none focus:border-blue-500"
            />
          </div>
          <button
            type="submit"
            class="w-full bg-blue-500 text-white px-4 py-3 rounded-3xl hover:bg-red-600 transition delay-150 duration-300 ease-in-out hover:scale-105"
          >
            Войти в панель
          </button>
        </form>
      </div>
    </div>
  </div>
</template>


