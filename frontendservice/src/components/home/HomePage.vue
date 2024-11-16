<script setup>

import WelcomeItem from "@/components/TopBar.vue";
import SampleCard from "@/components/ListCard.vue";
import TopBar from "@/components/TopBar.vue";
import ListCard from "@/components/ListCard.vue";
</script>

<template>
  <div class="flex flex-col h-screen">
    <div class="flex-1 overflow-y-auto">
      <div class="sticky top-0 z-10 backdrop-blur-xl drop-shadow-xl bg-base-200/70 border-b border-gray-700">
        <top-bar />
      </div>
      <div class="flex pl-20 pr-20">
        <div class="w-2/3 flex-none grid place-items-center p-10 gap-10">
          <ListCard v-for="post in posts" :text="post.content" :username="post.author" />
        </div>
<!--        <div v-if="loading" class="w-2/3 flex-none grid place-items-center p-10 gap-10">-->
<!--          Loading posts...-->
<!--        </div>-->
<!--        <div v-else>-->
<!--          <div v-for="post in posts" :key="post.title" class="w-2/3 flex-none grid place-items-center p-10 gap-10">-->
<!--            <ListCard text={{post.text}} :username="post.author" class="w-2/3" />-->

<!--          </div>-->
<!--        </div>-->


        <div class="divider divider-horizontal"></div>

        <div class="flex-1 rounded-box place-items-center">
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      posts: [],
      loading: true,
      error: null,
    };
  },
  created() {
    fetch('http://localhost:8083/home/1')
        .then((response) => {
          if (!response.ok) {
            throw new Error('Network response was not ok');
          }
          return response.json();
        })
        .then((data) => {
          this.posts = data;
        })
        .catch((error) => {
          this.error = error.message || 'An error occurred while fetching posts.';
        })
        .finally(() => {
          this.loading = false;
        });
  },
};
</script>

<style scoped>

</style>