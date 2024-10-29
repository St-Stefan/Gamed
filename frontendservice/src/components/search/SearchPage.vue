<script setup>

import WelcomeItem from "@/components/TopBar.vue";
import SampleCard from "@/components/GameCard.vue";
import TopBar from "@/components/TopBar.vue";
import GameCard from "@/components/GameCard.vue";
import AuthenticationPage from "@/components/authentication/AuthenticationPage.vue";
import UserInfoPanel from "@/components/home/UserInfoPanel.vue";

defineProps({
  query: String
})
</script>

<template>
  <div class="flex-1 overflow-y-auto bg-gImage bg-cover h-screen">
    <AuthenticationPage v-if="!loadedUID" @uidChanged="onUIDChanged"/>
      <div class="sticky top-0 z-10 backdrop-blur-xl drop-shadow-xl bg-base-200/30 border-b border-gray-700">
        <TopBar @uidChanged="onUIDChanged" @searchChanged="search"/>
      </div>
    <div class="flex pl-20 pr-20">
      <div class="p-10">
        <div class="text-xl font-semibold mb-4">
          {{ games.length === 0 ? '0 results match your search' : games.length + ' results match your search' }}
        </div>
        <div class="flex flex-wrap gap-4 justify-center">
          <GameCard v-for="game in games" :game="game" />
        </div>

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
      query: null,
      games: [],
      loadedUID: false,
      loading: true,
      error: null,
    };
  },
  created() {
    this.onUIDChanged()
  },
  methods:{
    search(){
      console.log('http://localhost:8082/search/' + this.$route.params.query)
      fetch('http://localhost:8082/search/' + this.$route.params.query)
          .then((response) => {
            if (!response.ok) {
              throw new Error('Network response was not ok');
            }
            return response.json();
          })
          .then((data) => {
            this.games = data;
            console.log(data)
          })
          .catch((error) => {
            this.error = error.message || 'An error occurred while fetching posts.';
          })
          .finally(() => {
            this.loading = false;
          });
    },
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
       this.search(this.$route.params.query)
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