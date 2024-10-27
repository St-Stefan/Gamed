<template>
  <div class="p-6">
    <div v-if="this.loading" class="flex flex-col items-center justify-center h-full">
      <p class="text-xl mb-4">Loading</p>
      <span class="loading loading-spinner loading-lg"></span>
    </div>

    <div v-else>
      <h1 class="text-2xl font-bold mb-4">{{ list.name }} by {{ this.author.name }}</h1>
<!--      <p class="text-lg mb-2"><strong>Author:</strong> {{ this.author.name }}</p>-->
      <p class="text-md mb-4"><b>Description</b>: {{ list.description }}</p>
      <div v-if="games.length > 0" class="bg-base-200 p-4 rounded-lg shadow-md">
        <h2 class="text-xl font-semibold mb-2">Games in this List:</h2>
        <ul class="list-disc list-inside">
          <li v-for="game in games" :key="game.id">{{ game.name }}</li>
        </ul>
      </div>
      <div v-else>
        <p>No games in this list.</p>
      </div>
    </div>
  </div>
</template>

<script>

export default {
  name: "SingleList",
  data() {
    return {
      // list: localStorage.getItem('list')
      loading: true,
      list: null,
      games: [],
      author: null,
      // gameIds: null
    }
  },
  created() {
    // this.list =
    const listId = this.$route.params.listId;
    const listInStore = localStorage.getItem('list')
    // if(listInStore === null || listInStore.id !== listId) {
    //   this.getList(listId)
    // }
    // else {
    //   this.list = localStorage.getItem('list')
    //   this.games = this.list.games;
    // }
    this.getList(listId)
  },
  methods: {
    getAuthor(userId) {

    },
    getList(listId) {
      let gameIds = []
      fetch("http://localhost:8092/lists/" + listId)
          .then((response) => {
            if (!response.ok) {
              throw new Error("Network response was not ok");
            }
            return response.json();
          })
          .then(list => this.list = list)
          .then(list => {
            fetch("http://localhost:8090/users/" + list.userId)
                .then((response) => {
                  if (!response.ok) {
                    throw new Error("Network response was not ok");
                  }
                  return response.json();
                })
                .then((user) => {
                  console.log(user); this.author = user})
          });

      fetch("http://localhost:8092/listToGames/list/" + listId)
          .then((response) => {
            if (!response.ok) {
              throw new Error("Network response was not ok");
            }
            return response.json();
          })
          .then(games => {
            gameIds = games.map(game => game.game);
            console.log(gameIds);
          })
          .finally(() => {
            for(let id of gameIds) {
              console.log("here")
              fetch("http://localhost:8092/games/" + id)
                  .then((response) => {
                    if (!response.ok) {
                      throw new Error("Network response was not ok");
                    }
                    return response.json();
                  })
                  .then(game => this.games.push(game))
            }
            this.loading = false
          });
    },
  }
};
</script>

<style scoped>
.loading-spinner {
  --btn-loading: currentColor;
}
</style>
