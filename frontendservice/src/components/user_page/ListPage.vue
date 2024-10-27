<template>
  <div>
    <div v-if="!loadedUID">
      <AuthenticationPage @uidChanged="getList" />
    </div>

    <div class="sticky top-0 z-10 backdrop-blur-xl drop-shadow-xl bg-base-200/70 border-b border-gray-700">
      <TopBar @uidChanged="getList" />
    </div>

    <div class="p-6">
      <div v-if="loading" class="flex flex-col items-center justify-center h-full">
        <p class="text-xl mb-4">Loading</p>
        <span class="loading loading-spinner loading-lg"></span>
      </div>

      <div v-else>
        <h1 class="text-2xl font-bold mb-4">{{ list.name }} by {{ author.name }}</h1>
        <p class="text-md mb-4"><strong>Description:</strong> {{ list.description }}</p>

        <div v-if="games.length > 0" class="flex flex-wrap gap-4">
          <div
              v-for="game in games"
              :key="game.id"
              class="bg-base-200 p-4 rounded-lg shadow-md flex flex-col items-center w-full sm:w-1/2 md:w-1/3 lg:w-1/4 xl:w-1/5"
          >
            <img
                src="../../pics/game.png"
                width="110"
                height="110"
                class="mb-2 rounded-md object-cover w-28 h-28"
            />

            <router-link :to="{ name: 'GamePage', params: {gameId: game.id}}"
                         @click.native="sendGameId(game.id)">
            <h3 class="text-lg font-semibold mb-1 text-center">{{ game.name }}</h3>
            <p class="text-sm mb-1 text-center"><strong>Developer:</strong> {{ game.developer }}</p>
            <p class="text-sm mb-1 text-center"><strong>Platform:</strong> {{ game.platforms }}</p>
            <p class="text-sm mb-1 text-center"><strong>Release Date:</strong> {{ formatReleaseDate(game.releaseDate) }}</p>
            </router-link>
          </div>
        </div>

        <div v-else>
          <p>No games in this list.</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import AuthenticationPage from "@/components/authentication/AuthenticationPage.vue";
import TopBar from "@/components/TopBar.vue";

export default {
  name: "SingleList",
  components: { TopBar, AuthenticationPage },
  data() {
    return {
      loading: true,
      list: null,
      games: [],
      author: null,
      loadedUID: false,
    };
  },
  created() {
    const listId = this.$route.params.listId;
    this.getList(listId);
  },
  methods: {
    async getList(listId) {
      this.loadedUID = localStorage.getItem("GamedUID") != null;

      if (this.loadedUID) {
        try {
          const listResponse = await fetch(`http://localhost:8092/lists/${listId}`);
          if (!listResponse.ok) {
            throw new Error("Network response was not ok for list");
          }
          const list = await listResponse.json();
          this.list = list;

          const authorResponse = await fetch(`http://localhost:8090/users/${list.userId}`);
          if (!authorResponse.ok) {
            throw new Error("Network response was not ok for user");
          }
          const author = await authorResponse.json();
          this.author = author;

          const gamesResponse = await fetch(`http://localhost:8092/listToGames/list/${listId}`);
          if (!gamesResponse.ok) {
            throw new Error("Network response was not ok for listToGames");
          }
          const gamesData = await gamesResponse.json();
          const gameIds = gamesData.map((game) => game.game);

          const gamePromises = gameIds.map((id) =>
              fetch(`http://localhost:8092/games/${id}`).then((response) => {
                if (!response.ok) {
                  throw new Error(`Network response was not ok for game ID ${id}`);
                }
                return response.json();
              })
          );

          const fetchedGames = await Promise.all(gamePromises);
          this.games = fetchedGames;
        } catch (error) {
          console.error("An error occurred while fetching data:", error);
        } finally {
          this.loading = false;
        }
      }
    },
    formatReleaseDate(dateString) {
      const options = { year: "numeric", month: "long", day: "numeric" };
      return new Date(dateString).toLocaleDateString(undefined, options);
    },
  },
};
</script>

<style scoped>
.loading-spinner {
  --btn-loading: currentColor;
}
</style>
