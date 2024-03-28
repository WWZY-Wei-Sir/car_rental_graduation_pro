import {defineStore} from 'pinia'
import {CarStatus} from "@/type/dataDefinition";

export const useStore = defineStore('main', () => {
  const chosenCarType = ref({
    nowCarType: "",
    nowCarTypeId: "",
  });
  const chosenCar = ref({
    id: "",
    carConfigs: [],
  });
  const lastPage = ref(1);
  const nowUser = reactive({form: {} as any} as any);
  const search = reactive({form: {} as any} as any);

  const getLastPage = () => {
    return lastPage.value;
  }
  const setLastPage = (item: number) => {
    lastPage.value = item;
  }

  return {chosenCarType, chosenCar, lastPage, getLastPage, setLastPage, nowUser, search};
}, {
  persist: true,
})
