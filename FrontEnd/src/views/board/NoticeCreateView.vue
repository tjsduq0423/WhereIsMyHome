<template>
  <AppContent class="img">
    <div class="cardWidthPadding"></div>
    <h1 class="text-center text-truncate mb-5">공지작성</h1>
    <AppBoardForm v-model:title="title" v-model:content="content" @submit.prevent="createNotice">
      <template #actions>
        <button type="submit" class="btn btn-outline-success btn-lg ms-auto">저장</button>
        <button type="button" class="btn btn-outline-danger btn-lg" @click="goListPage">
          취소
        </button>
      </template>
    </AppBoardForm>
  </AppContent>
</template>

<script setup>
import AppContent from '@/components/layouts/AppContent.vue';
import AppBoardForm from '@/components/layouts/AppBoardForm.vue';

import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { registNotice } from '@/api/notice.js';
import { useAlert } from '@/composables/alert';

const { vAlert, vSuccess } = useAlert();
const router = useRouter();
const title = ref('');
const content = ref('');

const createNotice = async () => {
  if (!validate()) return;
  try {
    await registNotice(title.value, content.value);
    router.push({ name: 'Notice' });
    vSuccess('공지 등록 성공');
  } catch (err) {
    vAlert('공지 등록 실패');
    console.error(err);
  }
};

const goListPage = () => router.push({ name: 'Notice' });
const validate = () => {
  if (!title.value || !content.value) {
    vAlert('입력 형식을 지켜주세요.');
    return false;
  }
  return true;
};
</script>

<style scoped>
.img {
  background-image: linear-gradient(rgba(0, 0, 0, 0.4), rgba(0, 0, 0, 0.5)),
    url('/img/notice02.jpg');
}
.cardWidthPadding {
  margin: 0rem 28vw 0rem 28vw;
}
</style>
