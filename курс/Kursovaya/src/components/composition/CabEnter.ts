import { ref, reactive } from 'vue';

export type NotificationType = 'success' | 'error';
export interface Notification {
  message: string;
  type: NotificationType;
}

export function useSubscription() {
  const email = ref<string>('');
  const password = ref<string>('');

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

  function checkLogin(): boolean {
    const trimmedEmail = email.value.trim();

    if (trimmedEmail === '') {
      showNotification('Пожалуйста, введите электронную почту.', 'error');
      return false;
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(trimmedEmail)) {
      showNotification('Неверный формат электронной почты.', 'error');
      return false;
    }

    return true; 
  }

  function checkPassword(): boolean {
    const trimmedPassword = password.value.trim();

    if (trimmedPassword.length > 0) {
      return true; 
    }

    showNotification('Пожалуйста, введите пароль.', 'error');
    return false; 
  }

  function subscribe(): void {
    if (checkLogin() && checkPassword()) {
      showNotification('Вы успешно вошли в систему!', 'success');
      console.log('Отправляем email:', email.value.trim());
      console.log('Пароль введен:', password.value.trim());
    }
  }

  return { email, password, notification, subscribe, checkLogin, checkPassword, showNotification };
}