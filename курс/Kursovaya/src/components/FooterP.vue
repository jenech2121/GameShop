<script setup lang="ts">
import { RefSymbol } from '@vue/reactivity'
import { ref } from 'vue';
import { useSubscription } from '@/components/composition/subscribe.ts';



const toVK = (): void => {
  window.location.href = 'https://vk.com';
};
const toInst = (): void => {
  window.location.href = 'https://instagram.com';
};
const toYT = (): void => {
  window.location.href = 'https://youtube.com';
};
const toTG = (): void => {
  window.location.href = 'https://web.telegram.org';
};

const { email, notification, subscribe } =  useSubscription();
</script>

<template>
  <footer class="bg-gray-200 w-full  border-t border-gray-300 flex flex-col items-center">
    
    <div class="w-full py-20 border-b border-gray-300 flex justify-between items-center px-10 bg-pink-100">
      
        <div class="flex items-center gap-3">
        <img src="@/assets/shop.png" alt="Логотип" class="w-15 h-15 object-contain" />
        <span class="text-2xl font-bold text-gray-800">GameShop</span>
      </div>

      <div class="flex flex-col  items-center">
        <span class="flex flex-col items-center font-sans italic text-xl font-medium m-2 ">Находите нас в социальных сетях</span>
        <div class="flex flex-row justify-between gap-12">
        
       <img @click="toVK"   src="./icons/vk.png" alt="Icon 1" class="w-15 h-15 cursor-pointer" /> 
        <img @click="toYT"  src="./icons/ют.png" alt="Icon 2" class="w-15 h-15 cursor-pointer" />
        <img @click="toInst"  src="./icons/инст.png" alt="Icon 3" class="w-15 h-15 cursor-pointer" />
        <img @click="toTG"  src="./icons/тг.png" alt="Icon 4" class="w-15 h-15 cursor-pointer" />
      </div>
    </div>

      <div>
        <span class="flex flex-col items-center font-sans italic text-xl font-medium m-2 ">Подпишитесь на наши новые обноления</span>
        <div class="bg-gray-300 rounded-full w-full max-w-3xl h-12 border border-gray-500  flex items-center justify-end px-4 mb-4">
        <input class=" w-100 text-black mr-2 " text="email" placeholder="example: GmeShop@gmail.com" v-model="email" >
        </input>
        <button @click="subscribe"  class="bg-blue-400 text-white px-6 py-3 focus:border-pink-700 rounded-full hover:bg-blue-700  h-10 flex items-center transition delay-150 duration-300 ease-in-out hover:-translate hover:scale-110">
      Подписаться
       </button>
       </div>
       <div v-if="notification.message" :class="[` fixed bottom-4 right-4 p-4 rounded-md shadow-md`, notification.type === 'success' ? 'bg-green-500 text-white' : 'bg-red-500 text-white']" >
    {{ notification.message }}
  </div>
      
      </div>
    </div>

    
    <div class="w-full py-20 bg-red-400/10">
      <p class="text-xl font-bold text-center">Coming soon...</p>
    </div>
  </footer>
</template>