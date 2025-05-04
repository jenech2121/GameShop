<script setup lang="ts">
import { useCalc } from '@/components/composition/calc.ts';
import { useBuyList } from '@/components/composition/BuyList.ts';
//import { useChekItem } from '@/components/composition/check.ts';
 
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

//const { checkAndAddItem, notification  } = useChekItem();
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

  <div class="flex flex-col gap-5 p-5 min-h-screen" style ="background-color: #fffbeb;">
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
          <button @click="removeSelectedData(getKeyByItemId(item.id)) || ('' as keyof typeof selectedData)" class="mt-2 px-4 py-1 bg-red-500 text-white rounded-md transition delay-150 duration-300 ease-in-out hover:-translate hover:scale-110">
            Удалить
          </button>
        </div>
      </div>
    </div>

    <!-- Каталог товаров -->
    <div class="grid grid-cols-3 gap-5" >
      <!-- Материнские платы -->
      <div class="bg-gray-200 p-5 rounded-lg" >
        <h2 class="text-2xl font-bold mb-5">Материнские платы</h2>
        <div
          v-for="part in partsMotherboards"
          :key="part.id"
          class="bg-white p-4 rounded-lg shadow-md mb-4"
        >
          <img :src="part.image" :alt="part.name" class="w-full h-90 object-cover mb-2" />
          <h3 class="text-lg font-medium">{{ part.name }}</h3>
          <p class="text-sm text-gray-600">{{ part.price }} ₽</p>
          <button @click="addSelectedData(part, 'motherboard'); addToBuyList(part);" class="mt-2 px-4 py-1 bg-blue-500 text-white rounded-md transition delay-150 duration-300 ease-in-out hover:-translate hover:scale-110">
            Выбрать
          </button>
          
        </div>
      </div>

      <!-- Процессоры -->
      <div class="bg-gray-200 p-5 rounded-lg">
        <h2 class="text-2xl font-bold mb-5">Процессоры</h2>
        <div
          v-for="part in partsCpu"
          :key="part.id"
          class="bg-white p-4 rounded-lg shadow-md mb-4"
        >
          <img :src="part.image" :alt="part.name" class="w-full w-90 object-cover mb-2" />
          <h3 class="text-lg font-medium">{{ part.name }}</h3>
          <p class="text-sm text-gray-600">{{ part.price }} ₽</p>
          <button @click="addSelectedData(part, 'cpu'); addToBuyList(part)" class="mt-2 px-4 py-1 bg-blue-500 text-white rounded-md transition delay-150 duration-300 ease-in-out hover:-translate hover:scale-110">
            Выбрать
          </button>
        </div>
      </div>

      <!-- Видеокарты -->
      <div class="bg-gray-200 p-5 rounded-lg">
        <h2 class="text-2xl font-bold mb-5">Видеокарты</h2>
        <div
          v-for="part in partsGpu"
          :key="part.id"
          class="bg-white p-4 rounded-lg shadow-md mb-4"
        >
          <img :src="part.image" :alt="part.name" class="w-full h-90 object-cover mb-2" />
          <h3 class="text-lg font-medium">{{ part.name }}</h3>
          <p class="text-sm text-gray-600">{{ part.price }} ₽</p>
          <button @click="addSelectedData(part, 'gpu'); addToBuyList(part)" class="mt-2 px-4 py-1 bg-blue-500 text-white rounded-md transition delay-150 duration-300 ease-in-out hover:-translate hover:scale-110">
            Выбрать
          </button>
        </div>
      </div>

      <div class="bg-gray-200 p-5 rounded-lg">
        <h2 class="text-2xl font-bold mb-5">Видеокарты</h2>
        
        <div
          v-for="part in partsRam"
          :key="part.id"
          class="bg-white p-4 rounded-lg shadow-md mb-4"
        >
          <img :src="part.image" :alt="part.name" class="w-full h-90 object-cover mb-2" />
          <h3 class="text-lg font-medium">{{ part.name }}</h3>
          <p class="text-sm text-gray-600">{{ part.price }} ₽</p>
          <button @click="addSelectedData(part, 'ram'); addToBuyList(part)" class="mt-2 px-4 py-1 bg-blue-500 text-white rounded-md transition delay-150 duration-300 ease-in-out hover:-translate hover:scale-110">
            Выбрать
          </button>
          
        </div>
      </div>
    </div>
    
  </div>
</template> 