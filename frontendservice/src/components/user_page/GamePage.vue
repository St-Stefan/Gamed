<template>
<!--  <div class="p-6">-->
  <div class="flex-1 overflow-y-auto bg-gImage bg-cover h-screen">
  <div v-if="!loadedUID">
    <AuthenticationPage @uidChanged="getGame" />
  </div>
  <div v-else>
    <div class="sticky top-0 z-10 backdrop-blur-xl drop-shadow-xl bg-base-200/30 border-b border-gray-700">
      <TopBar @uidChanged="getGame" />
    </div>

    <div v-if="this.loading" class="flex flex-col items-center justify-center h-full">
      <p class="text-xl mb-4">Loading</p>
      <span class="loading loading-spinner loading-lg"></span>
    </div>

    <div v-else>
      <div v-if="game !== null" class="game-container bg-base-100/30 p-4 rounded-lg shadow-md flex items-center">
        <img src="../../pics/game.png" alt="Game Image" class="game-image rounded-lg" />
        <div class="game-details ml-6">
          <h3 class="text-3xl font-bold">{{ game.name }}</h3>
          <p class="text-xl">Developer: {{ game.developer }}</p>
          <p class="text-xl">Release Date: {{ formatDate(game.releaseDate) }}</p>
          <p class="text-xl">Platforms: {{ game.platforms }}</p>
        </div>
      </div>
      <div v-else>
        <p>No game data available.</p>
      </div>
      <div>
        <div class="text-xl text"></div>
      </div>
    </div>
  </div>
  </div>
<!--  </div>-->
<!--  reviews component -->

</template>

<script>
import TopBar from "@/components/TopBar.vue";
import AuthenticationPage from "@/components/authentication/AuthenticationPage.vue";

export default {
  name: "GameDetails",
  components: {AuthenticationPage, TopBar},
  data() {
    return {
      game: null,
      loading: true,
      loadedUID: false
    };
  },
  created() {
    const gameId = this.$route.params.gameId
    this.getGame(gameId);
  },
  methods: {
    getGame(gameId) {
      this.loadedUID = localStorage.getItem("GamedUID") != null;

      if(this.loadedUID) {
        fetch("http://localhost:8092/games/" + gameId)
            .then((response) => {
              if (!response.ok) {
                throw new Error("Network response was not ok");
              }
              return response.json();
            })
            .then((game) => {
              this.game = game;
              console.log(this.game)
            })
            .finally(() => {
              this.loading = false
            })
      }
    },
    formatDate(dateString) {
      if (!dateString) return "N/A";
      const date = new Date(dateString);
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, "0");
      const day = String(date.getDate()).padStart(2, "0");

      return `${year}/${month}/${day}`;
    },
  },
};
</script>

<style scoped>
.loading-spinner {
  --btn-loading: currentColor;
}

.game-container {
  display: flex;
  align-items: center;
}

.game-image {
  width: 200px; /* Adjust width as needed */
  height: auto;
}

.game-details {
  text-align: left;
}
</style>
