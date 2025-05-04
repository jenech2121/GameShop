<script setup lang="ts">
import { ref } from 'vue'
import { onMounted, onBeforeUnmount } from 'vue';

const newsScroll = ref(null)

const colors = ['border-red-500','border-green-500', 'border-blue-500', 'border-cyan-500', 'border-purple-500']
const currentcolor = ref(colors[0])
let interaval: number | null = null

onMounted(() => {
  let index = 0
  interaval = setInterval(()=>{
    index = (index+1)%colors.length
    currentcolor.value = colors[index]
  }, 500)
})

onBeforeUnmount(()=>{
  if(interaval) {
    clearInterval(interaval)
  }
})


const scroll = (direction) => {
  const scrollAmount = 250
  direction === "left"
    ? newsScroll.value.scrollBy({ left: -scrollAmount, behavior: "smooth" })
    : newsScroll.value.scrollBy({ left: scrollAmount, behavior: "smooth" })
}
</script>

<template>
 <div class="news-section flex flex-col relative px-8 py-6" style ="background-color: #fffbeb;">
    <h2 class="text-2xl font-bold mb-4">Новости</h2>

    <div class="flex items-center w-full gap-24">
      <!-- Кнопка прокрутки влево -->
      <button 
        class="arrow left text-3xl p-2 cursor-pointer z-10"
        @click="scroll('left')">&#9664;
      </button>

      <!-- Контейнер с новостями -->
      <div 
        class="news-wrapper flex overflow-x-auto scroll-smooth gap-32 pl-4 pr-4 pb-4"
        ref="newsScroll">
        <!-- Отдельные блоки новостей -->

        <!-- Блок новости 1 -->
        <div class="news-box min-w-[400px] h-[200px] rounded-lg bg-gray-300 flex flex-col items-center overflow-hidden" :class="`border-4 ${currentcolor}`" >
          <img 
            src="@/components/icons/come.png" 
            alt="новость 1" 
            class="w-full h-[150px] object-cover rounded-lg" ></img>
          <div class="text-sm mt-2 font-semibold text-center">Coming soon...</div>
        </div>

        <!-- Блок новости 2 -->
        <div class="news-box min-w-[400px] h-[200px] rounded-lg bg-gray-300 flex flex-col items-center overflow-hidden" :class="`border-4 ${currentcolor}`" >
          <img 
            src="@/components/icons/come.png" 
            alt="новость 2" 
            class="w-full h-[150px] object-cover rounded-lg" />
          <div class="text-sm mt-2 font-semibold text-center">Coming soon...</div>
        </div>

        <!-- Блок новости 3 -->
        <div class="news-box min-w-[400px] h-[200px] rounded-lg bg-gray-300 flex flex-col items-center overflow-hidden" :class="`border-4 ${currentcolor}`" >
          <img 
            src="@/components/icons/come.png" 
            alt="новость 3" 
            class="w-full h-[150px] object-cover rounded-lg" />
          <div class="text-sm mt-2 font-semibold text-center">Coming soon...</div>
        </div>

        <!-- Блок новости 4 -->
        <div class="news-box min-w-[400px] h-[200px] rounded-lg bg-gray-300 flex flex-col items-center overflow-hidden" :class="`border-4 ${currentcolor}`">
          <img 
            src="@/components/icons/come.png" 
            alt="новость 4" 
            class="w-full h-[150px] object-cover rounded-lg" />
          <div class="text-sm mt-2 font-semibold text-center">Coming soon...</div>
        </div>

        <!-- Блок новости 5 -->
        <div class="news-box min-w-[400px] h-[200px] rounded-lg bg-gray-300 flex flex-col items-center overflow-hidden" :class="`border-4 ${currentcolor}`">
          <img 
            src="@/components/icons/come.png" 
            alt="новость 5" 
            class="w-full h-[150px] object-cover rounded-lg" />
          <div class="text-sm mt-2 font-semibold text-center">Coming soon...</div>
        </div>

      </div>

      <!-- Кнопка прокрутки вправо -->
      <button 
        class="arrow right text-3xl p-2 cursor-pointer z-10 border border-white"
        @click="scroll('right')">&#9654;
      </button>
    </div>
</div>
</template>
