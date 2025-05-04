<script setup lang="ts">
import { ref } from 'vue';
import { useBuyList } from '@/components/composition/BuyList';

const {
  buyList,
  addToBuyList,
  removeFromBuyList,
  totalPrice,
  totalCount,
  clearBuyList
} = useBuyList();

const orderCompleted = ref(false);

const placeOrder = () => {
  clearBuyList();
  orderCompleted.value = true;
};
</script>

<template>
  <div class="flex flex-col bg-pink-100 min-h-screen">
    
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
    </div>

    <!-- Основной контент -->
    <div class="flex flex-col items-center justify-center flex-grow p-8">

      <div v-if="orderCompleted" class="text-3xl font-bold text-green-600 mt-10">
        Заказ успешно оформлен!
      </div>

      <div v-else class="flex flex-col items-center w-full max-w-5xl">

        <h2 class="text-2xl font-bold mb-5">Корзина товаров</h2>

        <!-- Карточки товаров -->
        <div v-if="buyList.length > 0" class="flex flex-wrap gap-6 p-6 bg-[#fffbeb] border-2 border-yellow-300  rounded-lg">
          <div
            v-for="item in buyList"
            :key="item.id"
            class="flex flex-col items-center bg-white p-4 rounded-lg shadow-md w-100">
            <img :src="item.image" :alt="item.name" class="w-full h-40 object-cover mb-2 rounded-md" />
            <h3 class="text-lg font-medium">{{ item.name }}</h3>
            <p class="text-sm text-gray-600">{{ item.price }} ₽</p>
            <button
              @click="removeFromBuyList(item.id)"
              class="mt-2 px-4 py-1 bg-red-500 text-white rounded-md hover:scale-105 transition">
              Удалить
            </button>
          </div>
        </div>

        <div v-else class="text-lg text-gray-500 mt-6">
          Корзина пуста
        </div>

        <!-- Информация о заказе -->
        <div class="mt-10 text-xl font-bold">
          Всего товаров: {{ totalCount }} | Общая стоимость: {{ totalPrice }} ₽
        </div>

        <button
          v-if="buyList.length > 0"
          @click="placeOrder"
          class="mt-6 px-8 py-3 bg-green-500 text-white rounded-lg text-xl hover:scale-105 transition">
          Оформить заказ
        </button>
      </div>
    </div>
  </div>
</template>
