import { reactive, computed, watch } from 'vue';
import { ref } from 'vue';

export type NotificationType = 'success'|'error';
export interface Notification {
message: string;
type:NotificationType;
}
export const useBuyList = () => {
  interface Part {
    id: string;
    name: string;
    price: number;
    image: string;
  }
  const notification = ref<{ message: string; type: 'success' | 'error' } | { message: ''; type: '' }>({
    message: '',
    type: ''
  });
  const buyList = reactive<Part[]>([]);

  const loadBuyList = () => {
    const data = localStorage.getItem('buyList');
    if (data) {
      const items = JSON.parse(data);
      buyList.splice(0, buyList.length, ...items); // Обновляем реактивный массив
    }
  };

  const saveBuyList = () => {
    localStorage.setItem('buyList', JSON.stringify(buyList));
  };

  const addToBuyList = (item: Part) => {
   const exists = buyList.find((buyItem: any) => buyItem.id === item.id );
     if(exists){
  notification.value={
    message:'Товар уже есть в корзине',
    type:'error',
    };
    clearNotificationAfterDelay();
      }else{
    buyList.push(item);
    saveBuyList();
    notification.value={
       message:'Товар добавлен',
       type:'success'
    };
    clearNotificationAfterDelay();
      }
  };

  function clearNotificationAfterDelay() {
    setTimeout(() => {
      notification.value = { message: 'vj', type: 'success' };
    }, 2000); // Уведомление исчезнет через 2 секунды
  }

  const removeFromBuyList = (itemId: string) => {
    const index = buyList.findIndex((item) => item.id === itemId);
    if (index !== -1) {
      buyList.splice(index, 1);
      saveBuyList();
    }
  };

  const clearBuyList = () => {
    buyList.splice(0, buyList.length);
    saveBuyList();
  };

  const totalPrice = computed(() =>
    buyList.reduce((sum, item) => sum + item.price, 0)
  );

  const totalCount = computed(() => buyList.length);

  // Авто-загрузка при создании хука
  loadBuyList();

  return {
    buyList,
    addToBuyList,
    removeFromBuyList,
    totalPrice,
    totalCount,
    clearBuyList,
    notification
  };
};
