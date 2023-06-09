import { defineStore } from 'pinia';
import { getChartInfoBySidoName } from '@/api/info';
import { computed, ref } from 'vue';

export const useHomeChartStore = defineStore('homeChar', () => {
  const curSidoName = ref(null);
  const dataList = ref([]);
  const chartData = computed(() => {
    return dataList.value.find(item => item.name === curSidoName.value);
  });

  const setChartData = async sido => {
    const response = await getChartInfoBySidoName(sido);
    const data = {
      name: sido,
      labels: [...response.data.map(el => el.gugunName)],
      datasets: [
        {
          label: '관심도',
          backgroundColor: '#F99B7D',
          data: [
            ...response.data.map(
              el => el.dealAmount + Math.ceil(el.viewCount * 0.005) + el.markCount * 37 * 3,
            ),
          ],
        },
        {
          label: '조회수',
          backgroundColor: '#B04759',
          data: [...response.data.map(el => Math.ceil(el.viewCount * 0.005))],
        },
        {
          label: '거래량',
          backgroundColor: '#E76161',
          data: [...response.data.map(el => el.dealAmount)],
        },
        {
          label: '북마크',
          backgroundColor: '#8BACAA',
          data: [...response.data.map(el => el.markCount * 37 * 3)],
        },
      ],
    };
    dataList.value.push(data);
  };

  return { chartData, setChartData, dataList, curSidoName };
});
