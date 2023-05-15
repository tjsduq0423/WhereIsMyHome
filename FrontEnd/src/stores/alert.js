import { defineStore } from 'pinia';
import { ref } from 'vue';

export const useAlertStore = defineStore('alert', () => {
  const alerts = ref([]);

  const vAlert = (message, type = 'error') => {
    alerts.value.push({ message, type });
    setTimeout(() => {
      alerts.value.shift();
    }, 2500);
  };
  const vSuccess = message => {
    vAlert(message, 'success');
  };

  return {
    alerts,
    vAlert,
    vSuccess,
  };
});
