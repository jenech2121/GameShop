<script setup>
import { useSubscription } from '@/components/composition/CabEnter.ts';
const { email, password, notification, checkLogin, checkPassword, showNotification } = useSubscription();
function registration() {
  if (checkLogin() && checkPassword()) {
    showNotification('Вы успешно вошли в систему!', 'success');
    console.log('Логин выполнен успешно.');
  }
}
</script>

<template> 
  <div class="flex flex-row items-center justify-center gap-3 p-4 border-8 w-full bg-blue-200 border-red-300 hover:border-pink-500 focus:border-blue-700 ">
  <div class="flex items-center gap-3"> 
        <img src="@/assets/shop.png" alt="Логотип" class="w-10 h-10 object-contain" />
        <span class="text-2xl font-bold text-gray-800">GameShop</span>
      </div>

      <RouterLink to="/" class=" hover:text-blue-600 transition border-2 bg-pink-300/50 border-blue-300 rounded-full b pl-8  w-40 h-13 flex flex-row items-center ml-4"><span class="flex flex-col items-center text-2xl font-bold text-gray-800">Главная</span></RouterLink>

    </div>   
    
    <div class="h-screen w-full bg-cover bg-center p-10 flex flex-col items-center  " style="background-image: url('./src/assets/fon.png')">
      <div class="flex flex-col items-center justify-center w-150 h-200 bg-white border-8 rounded-3xl border-double border-blue-400 hover:border-red-500">

    <!-- Заголовок -->
    <h2 class="text-5xl font-bold mb-10">Вход </h2>

    <!-- Форма -->
    <form @submit.prevent="handleSubmit">
      <!-- Поле электронной почты -->
      <div class="mb-10">
        <label for="email" class="block text-sm font-medium">Электронная почта</label>
        <input
          id="email"
          v-model="email"
          type="email"
          placeholder="Введите электронную почту"
          class="w-100 h-12 rounded-3xl px-4 py-2 mt-1 border border-gray-500 rounded focus:outline-none focus:border-blue-500"
        />
      </div>

      <!-- Поле пароля -->
      <div class="mb-7 relative">
        <label for="password" class="block text-sm font-medium">Пароль</label>
        <input
          id="password"
          v-model="password"
          :type="showPassword ? 'text' : 'password'"
          placeholder="Введите пароль"
          class="w-100 h-12 rounded-3xl px-4 py-2 mt-1 border border-gray-500 rounded focus:outline-none focus:border-blue-500"
        />
        <button
          type="button"
          @click="toggleShowPassword"
          class="absolute right-4 top-7  text-gray-500 hover:text-blue-500 focus:outline-none "
        >
          
          
        </button>
      </div>

      <!-- Ссылка "Не помню пароль" -->
      <div class="mb-9">
        <a href="#" class="text-sm text-blue-500 hover:underline">Не помню пароль</a>
      </div>

      <!-- Кнопка "Войти" -->
      <button
        type="submit"
        @click="registration"  
        class="w-full bg-blue-500 text-white px-4 py-2 rounded-3xl hover:bg-red-600 transition delay-150 duration-300 ease-in-out hover:-translate hover:scale-110"
      >
        Войти
      </button>
      <div v-if="notification.message" :class="[` fixed bottom-4 right-4 p-4 rounded-md shadow-md`, notification.type === 'success' ? 'bg-green-500 text-white' : 'bg-red-500 text-white']" >
    {{ notification.message }}
  </div>
    </form>

    <!-- Кнопка "Создать профиль" -->
    <div class="mt-6">
      <button
        class="w-full bg-transparent border border-gray-500  px-4 py-2 rounded hover:bg-gray-500 transition duration-300"
      >
        Регистрация
      </button>
    </div>
   
    
  </div>
  

</div>
 

    
</template>