<script setup lang="ts">
import { ref } from 'vue'
import { onMounted, onBeforeUnmount } from 'vue';

const colors = ['border-red-500','border-green-500', 'border-blue-500', 'border-cyan-500', 'border-purple-500'
]
const currentcolor = ref(colors[0])
let interaval: number | null = null

onMounted(() => {
  let index = 0
  interaval = setInterval(()=>{
    index = (index+1)%colors.length
    currentcolor.value = colors[index]
  }, 500)
});

onBeforeUnmount(()=>{
  if(interaval) {
    clearInterval(interaval)
  }
})
// Баннеры
const currentBanner = ref(0)

function nextBanner() {
  const totalBanners = 3 // Количество баннеров
  currentBanner.value = (currentBanner.value + 1) % totalBanners
}
function prevBanner() {
  const totalBanners = 3
  currentBanner.value = (currentBanner.value - 1 + totalBanners) % totalBanners
}
function goToBanner(index: number) {
  currentBanner.value = index
}
const gotoVideo = (): void => {
  window.location.href = 'https://www.dns-shop.ru/actions/500776b3-bf1b-11ed-90b1-00155d8ed20c/';
}

// Популярное
const popularScroll = ref<HTMLDivElement | null>(null)

function scrollPopular(direction: 'left' | 'right') {
  const container = popularScroll.value
  const scrollAmount = 200
  if (container) {
    if (direction === 'left') {
      container.scrollLeft -= scrollAmount
    } else {
      container.scrollLeft += scrollAmount
    }
  }
}
</script>


<template >
  <div class="px-10 py-5 bg-white " style ="background-color: #fffbeb;" >
    <!-- 🔼 БАННЕРЫ -->
    <div class="relative flex items-center justify-center bg-gray-300 rounded-xl overflow-hidden h-[550px]" :class="`border-8 ${currentcolor}`" >
      <button class="absolute left-4 top-1/2 -translate-y-1/2 text-2xl text-gray-700" @click="prevBanner">
        &#9664;
      </button>

      <!-- Показываем баннер в зависимости от currentBanner -->
      <img
        v-if="currentBanner === 0"
        @click="gotoVideo"
        src="@/components/icons/SaleVi.png"
        alt="Баннер 1"
        class="w-full h-full object-cover"
      />
      <img
        v-else-if="currentBanner === 1"
        @click="gotoVideo"
        src="@/components/icons/Sale2.png"
        alt="Баннер 2"
        class="w-full h-full object-cover"
      />
      <img
        v-else-if="currentBanner === 2"
        @click="gotoVideo"
        src="@/components/icons/Sale3.png"
        alt="Баннер 3"
        class="w-full h-full object-cover"
      />

      <button class="absolute right-4 top-1/2 -translate-y-1/2 text-2xl text-gray-700" @click="nextBanner">
        &#9654;
      </button>
    </div>

    <!-- 🔘 Навигация по баннерам -->
    <div class="flex justify-center gap-3 mt-4">
      <span
        v-for="index in 3"
        :key="index"
        @click="goToBanner(index - 1)"
        :class="[
          'w-4 h-1 cursor-pointer transition-all',
          (index - 1) === currentBanner ? 'bg-black' : 'bg-gray-400'
        ]"
      ></span>
    </div>

    <!-- 🔽 ПОПУЛЯРНОЕ -->
    <div class="mt-20" >
      <h2 class="text-lg font-semibold mb-5">Популярное</h2>
      <div class="flex items-center " >
        <button class="text-2xl text-gray-700" @click="scrollPopular('left')">
          &#9664;
        </button>

        <div ref="popularScroll" class="flex overflow-x-auto gap-5 scroll-smooth py-2  mx-4">
  <!-- Товар 1 -->
  <div class="min-w-[300px] h-[170px] bg-gray-300 rounded-lg flex flex-col items-center justify-center flex-shrink-0 shadow-lg drop-shadow-2xl/25 shadow-cyan-500/50"
  :class="`border-4 ${currentcolor}`">
    <img src="@/components/icons/videokarta.png" alt="Товар 1" class="w-full h-[120px] object-cover rounded-2xl p-1 " />
    <div class="text-center text-sm mt-2 font-semibold">Видеокарта</div>
  </div>

  <!-- Товар 2 -->
  <div class="min-w-[300px] h-[170px] bg-gray-300 rounded-lg flex flex-col items-center justify-center flex-shrink-0 shadow-xl drop-shadow-2xl/25 shadow-purple-500/50" :class="`border-4 ${currentcolor}`">
    <img src="@/components/icons/mat.png" alt="Товар 2" class="w-full h-[120px] object-cover rounded-2xl p-1" />
    <div class="text-center text-sm mt-2 font-semibold">Материнская плата</div>
  </div>

  <!-- Товар 3 -->
  <div class="min-w-[300px] h-[170px] bg-gray-300 rounded-lg flex flex-col items-center justify-center flex-shrink-0 shadow-lg drop-shadow-2xl/25 shadow-red-500/50" :class="`border-4 ${currentcolor}`">
    <img src="@/components/icons/ozu.png" alt="Товар 3" class="w-full h-[120px] object-cover rounded-2xl p-1" />
    <div class="text-center text-sm mt-2 font-semibold">Оперативная память</div>
  </div>

  <!-- Товар 4 -->
  <div class="min-w-[300px] h-[170px] bg-gray-300 rounded-lg flex flex-col items-center justify-center flex-shrink-0 shadow-lg drop-shadow-2xl/25 shadow-blue-500/50" :class="`border-4 ${currentcolor}`">
    <img src="@/components/icons/proc.png" alt="Товар 4" class="w-full h-[120px] object-cover rounded-2xl p-1" />
    <div class="text-center text-sm mt-2 font-semibold">Процессор</div>
  </div>

  <!-- Товар 5 -->
  <div class="min-w-[300px] h-[170px] bg-gray-300 rounded-lg flex flex-col items-center justify-center flex-shrink-0 shadow-lg drop-shadow-2xl/25 shadow-pink-500/50" :class="`border-4 ${currentcolor}`">
    <img src="@/components/icons/blok.png" alt="Товар 5" class="w-full h-[120px] object-cover rounded-2xl p-1" />
    <div class="text-center text-sm mt-2 font-semibold">Блок питания</div>
  </div>

  <!-- Товар 6 -->
  <div class="min-w-[300px] h-[170px] bg-gray-300 rounded-lg flex flex-col items-center justify-center flex-shrink-0 shadow-lg drop-shadow-2xl/25 shadow-indigo-500/50" :class="`border-4 ${currentcolor}`">
    <img src="@/components/icons/ssd.png" alt="Товар 6" class="w-full h-[120px] object-cover rounded-2xl p-1" />
    <div class="text-center text-sm mt-2 font-semibold">SSD накопитель</div>
  </div>
</div>

        <button class="text-2xl text-gray-700" @click="scrollPopular('right')">
          &#9654;
        </button>
      </div>
    </div>
  </div>
</template>
