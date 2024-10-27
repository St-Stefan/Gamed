<template>
  <div class="p-6">
    <div v-if="this.loading" class="flex flex-col items-center justify-center h-full">
      <p class="text-xl mb-4">Loading</p>
      <span class="loading loading-spinner loading-lg"></span>
    </div>

    <div v-else>
      <div v-if="game !== null" class="game-container bg-base-200 p-4 rounded-lg shadow-md flex items-center">
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
    </div>
  </div>

<!--  reviews component -->

</template>

<script>
export default {
  name: "GameDetails",
  data() {
    return {
      game: null,
      loading: true
    };
  },
  async created() {
    const gameId = this.$route.params.gameId
    await this.getGame(gameId);
  },
  methods: {
    async getGame(gameId) {
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
