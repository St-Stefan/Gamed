<script setup>

import WelcomeItem from "@/components/TopBar.vue";
import SampleCard from "@/components/ListCard.vue";
import TopBar from "@/components/TopBar.vue";
import ListCard from "@/components/ListCard.vue";
import AuthenticationPage from "@/components/authentication/AuthenticationPage.vue";
import UserInfoPanel from "@/components/home/UserInfoPanel.vue";
</script>

<template>
    <AuthenticationPage v-if="!loadedUID" @uidChanged="onUIDChanged"/>
  <div class="flex flex-col h-screen">
    <div class="flex-1 overflow-y-auto">
      <div class="sticky top-0 z-10 backdrop-blur-xl drop-shadow-xl bg-base-200/70 border-b border-gray-700">
        <top-bar @uidChanged="onUIDChanged"/>
      </div>
      <div class="flex pl-20 pr-20">
        <div class="w-2/3 flex-none grid place-items-center p-10 gap-10">
          <ListCard v-for="post in posts" :post="post" @select-post="handleSelectPost(post)"/>
        </div>

        <div class="divider divider-horizontal"></div>

        <div class="flex-1 rounded-box place-items-center">
            <UserInfoPanel :user="selectedAuthor" />
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
      loadedUID: false,
      loading: true,
      error: null,
      selectedAuthor: null,
    };
  },
  created() {
    this.onUIDChanged()
  },
  methods:{
    onUIDChanged(){
       this.loadedUID = localStorage.getItem("GamedUID")!=null;
       if(this.loadedUID){
         fetch('http://localhost:8083/home/' + localStorage.getItem("GamedUID"))
             .then((response) => {
               if (!response.ok) {
                 throw new Error('Network response was not ok');
               }
               return response.json();
             })
             .then((data) => {
               this.posts = data;
               console.log(data)
             })
             .catch((error) => {
               this.error = error.message || 'An error occurred while fetching posts.';
             })
             .finally(() => {
               this.loading = false;
             });
       }
    },
    handleSelectPost(post) {
      // Update the selected user when a post is selected
      console.log("here")
      this.selectedAuthor = post.user;
    },
  }
};
</script>

<style scoped>

</style>