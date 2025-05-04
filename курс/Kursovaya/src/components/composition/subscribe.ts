import { RefSymbol } from '@vue/reactivity'
import { ref } from 'vue';
import { reactive } from 'vue';


export type NotificationType = 'success' | 'error';
export interface Notification {
  message: string;
  type: NotificationType;
}


export function useSubscription() {
  const email = ref<string>('');

  const notification = reactive<Notification>({
    message: '',
    type: 'success',
  });

  
  function showNotification(
    message: string,
    type: NotificationType,
    timeout: number = 3000,
  ): void {
    notification.message = message;
    notification.type = type;

    setTimeout(() => {
      notification.message = '';
    }, timeout);

}
  
   
  
  function subscribe(): void {
    const trimmedEmail = email.value.trim();

    if (trimmedEmail === '') {
      showNotification('Пожалуйста, введите электронную почту.', 'error');
      return;
    }
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(trimmedEmail)) {
      showNotification('Неверный формат электронной почты.', 'error');
      return;
    }
    
   
    console.log('Отправляем email:', trimmedEmail);

        showNotification(`Вы успешно подписались с адресом: ${trimmedEmail}`, 'success');
  }

  return { email, notification, subscribe };
}