<script setup lang="ts">
import { ref } from 'vue'
import { onMounted, onBeforeUnmount } from 'vue';
import { borderAnimation } from '@/components/composition/BorderAnimation';

const colors = ['border-red-500','border-green-500', 'border-blue-500', 'border-cyan-500', 'border-purple-500']
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


const newItems = ref(null);
const suggestedItems = ref(null);

function scroll(refElName, direction) {
  const el = refElName === 'newItems' ? newItems.value : suggestedItems.value;
  const scrollAmount = 200;
  if (!el) return;
  direction === 'left'
    ? el.scrollBy({ left: -scrollAmount, behavior: "smooth" })
    : el.scrollBy({ left: scrollAmount, behavior: "smooth" });
}
</script>

<template>
  <div class="p-5" style ="background-color: #fffbeb;"  >
    <!-- Новинки -->
    <section class="mb-10">
      <h2 class="text-2xl font-medium mb-3">Новинки</h2>
      <div class="flex items-center relative">
        <button
          class="w-8 h-8 text-base rounded-full flex items-center justify-center mr-2"
          @click="scroll('newItems', 'left')"
        >
          &#9664;
        </button>
        <div
          class="flex overflow-x-auto scroll-smooth gap-12 py-5 flex-1"
          ref="newItems"
        >
          <!-- Блок новинки 1 -->
          <div
            class="bg-gray-300 border-4 rounded-lg flex-shrink-0 w-[350px] h-[200px] overflow-hidden flex flex-col items-center"
            :class="`border-2 ${currentcolor}`"
          >
            <img
              src="@/components/icons/come.png"
              alt="Новинка 1"
              class="w-full h-[150px] object-cover transition delay-150 duration-300 ease-in-out hover:-translate-y-1 hover:scale-110"
            />
            <div class="text-sm mt-2 font-semibold text-center">Coming soon...</div>
          </div>

          <!-- Блок новинки 2 -->
          <div
            class="bg-gray-300 border-4 rounded-lg flex-shrink-0 w-[350px] h-[200px] overflow-hidden flex flex-col items-center"
            :class="`border-4 ${currentcolor}`"
          >
            <img
              src="@/components/icons/come.png"
              alt="Новинка 2"
              class="w-full h-[150px] object-cover"
            />
            <div class="text-sm mt-2 font-semibold text-center">Coming soon...</div>
          </div>

          <!-- Блок новинки 3 -->
          <div
            class="bg-gray-300 rounded-lg flex-shrink-0 w-[350px] h-[200px] overflow-hidden flex flex-col items-center"
            :class="`border-4 ${currentcolor}`"
          >
            <img
              src="@/components/icons/come.png"
              alt="Новинка 3"
              class="w-full h-[150px] object-cover"
            />
            <div class="text-sm mt-2 font-semibold text-center">Coming soon...</div>
          </div>

          <!-- Блок новинки 4 -->
          <div
            class="bg-gray-300 rounded-lg flex-shrink-0 w-[350px] h-[200px] overflow-hidden flex flex-col items-center"
            :class="`border-4 ${currentcolor}`"
          >
            <img
              src="@/components/icons/come.png"
              alt="Новинка 4"
              class="w-full h-[150px] object-cover"
            />
            <div class="text-sm mt-2 font-semibold text-center">Coming soon...</div>
          </div>

          <!-- Блок новинки 5 -->
          <div
            class="bg-gray-300 rounded-lg flex-shrink-0 w-[350px] h-[200px] overflow-hidden flex flex-col items-center"
            :class="`border-4 ${currentcolor}`"
          >
            <img
              src="@/components/icons/come.png"
              alt="Новинка 5"
              class="w-full h-[150px] object-cover"
            />
            <div class="text-sm mt-2 font-semibold text-center">Coming soon...</div>
          </div>
        </div>
        <button
          class="w-8 h-8 text-base rounded-full flex items-center justify-center ml-2"
          @click="scroll('newItems', 'right')"
        >
          &#9654;
        </button>
      </div>
    </section>

    <!-- Возможно пригодится -->
    <section class="mb-10">
      <h2 class="text-2xl font-medium mb-3">Возможно пригодится</h2>
      <div class="flex items-center relative">
        <button
          class="w-8 h-8 text-base rounded-md flex items-center justify-center mr-2"
          @click="scroll('suggestedItems', 'left')"
        >
          &#9664;
        </button>
        <div
          class="flex overflow-x-auto scroll-smooth gap-50 py-5 flex-1"
          ref="suggestedItems"
        >
          <!-- Блок 1 -->
          <div
            class="bg-gray-300 rounded-lg flex-shrink-0 w-[350px] h-[200px] overflow-hidden flex flex-col items-center"
            :class="`border-4 ${currentcolor}`"
          >
            <img
              src="@/components/icons/come.png"
              alt="Возможно пригодится 1"
              class="w-full h-[150px] object-cover"
            />
            <div class="text-sm mt-2 font-semibold text-center">Coming soon...</div>
          </div>
          <div
            class="bg-gray-300 rounded-lg flex-shrink-0 w-[350px] h-[200px] overflow-hidden flex flex-col items-center"
            :class="`border-4 ${currentcolor}`"
          >
            <img
              src="@/components/icons/come.png"
              alt="Возможно пригодится 1"
              class="w-full h-[150px] object-cover"
            />
            <div class="text-sm mt-2 font-semibold text-center">Coming soon...</div>
          </div>

          <!-- Блок 2 -->
          <div
            class="bg-gray-300 rounded-lg flex-shrink-0 w-[350px] h-[200px] overflow-hidden flex flex-col items-center"
            :class="`border-4 ${currentcolor}`"
          >
            <img
              src="@/components/icons/come.png"
              alt="Возможно пригодится 2"
              class="w-full h-[150px] object-cover"
            />
            <div class="text-sm mt-2 font-semibold text-center">Coming soon...</div>
          </div>

          <!-- Блок 3 -->
          <div
            class="bg-gray-300 rounded-lg flex-shrink-0 w-[350px] h-[200px] overflow-hidden flex flex-col items-center"
            :class="`border-4 ${currentcolor}`"
          >
            <img
              src="@/components/icons/come.png"
              alt="Возможно пригодится 3"
              class="w-full h-[150px] object-cover"
            />
            <div class="text-sm mt-2 font-semibold text-center">Coming soon...</div>
          </div>

          <!-- Добавляй аналогично ещё блоки -->
        </div>
        <button
          class="w-8 h-8 text-base rounded-md flex items-center justify-center ml-2"
          @click="scroll('suggestedItems', 'right')"
        >
          &#9654;
        </button>
      </div>
    </section>
  </div>
</template>
