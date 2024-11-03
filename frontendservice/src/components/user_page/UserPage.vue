<template>
  <div class="flex-1 overflow-y-auto bg-gImage bg-cover h-screen">
  <div v-if="!loadedUID">
    <AuthenticationPage @uidChanged="getProfile" />
  </div>

  <div v-else>
    <div class="sticky top-0 z-10 backdrop-blur-xl drop-shadow-xl bg-base-200/30 border-b border-gray-700">
      <TopBar @uidChanged="getProfile" />
    </div>

    <div v-if="loading" class="flex flex-col items-center justify-center h-full">
      <p class="text-xl mb-4">Loading</p>
      <span class="loading loading-spinner loading-lg"></span>
    </div>

    <!-- Main Content -->
    <div v-else class="flex-1 overflow-y-auto p-6">
      <!-- User Info -->
      <div class="flex flex-wrap">
        <div class="w-full md:w-1/2 pr-4 mb-6">
          <div class="card bg-base-100/30 backdrop-blur-xl border border-gray-700 h-full">
            <div class="card-body flex items-center">
              <div class="avatar mr-4">
                <div class="w-24 rounded-full">
                  <img src="../../pics/user.png" alt="User Avatar" />
                </div>
              </div>
              <div>
                <h2 class="card-title text-2xl">{{ user.name }}</h2>
                <p>Premium: {{ user.premium ? 'Yes' : 'No' }}</p>
                <p>Developer: {{ user.developer ? 'Yes' : 'No' }}</p>
              </div>
            </div>
          </div>
        </div>

        <div class="w-full md:w-1/2 flex flex-wrap">
          <!-- Followers -->
          <div class="w-full md:w-1/3 mb-6 pr-4">
            <div class="card bg-base-100/30 backdrop-blur-xl border border-gray-700 h-full">
              <div class="card-body">
                <h2 class="card-title">Followers</h2>
                <div v-if="followerUsers.length">
                  <div v-for="followerUser in followerUsers" :key="followerUser.id" class="flex items-center my-2">
                    <div class="avatar mr-2">
                      <div class="w-8 rounded-full">
                        <img src="../../pics/user.png" alt="User Avatar" />
                      </div>
                    </div>
                    <p>{{ followerUser.name }}</p>
                  </div>
                </div>
                <div v-else>
                  <p>No followers yet.</p>
                </div>
              </div>
            </div>
          </div>

          <!-- Followed Users -->
          <div class="w-full md:w-1/3 mb-6 pr-4">
            <div class="card bg-base-100/30 backdrop-blur-xl border border-gray-700 h-full">
              <div class="card-body">
                <h2 class="card-title">Followed Users</h2>
                <div v-if="followedUsers.length">
                  <div v-for="followedUser in followedUsers" :key="followedUser.id" class="flex items-center my-2">
                    <div class="avatar mr-2">
                      <div class="w-8 rounded-full">
                        <img src="../../pics/user.png" alt="User Avatar" />
                      </div>
                    </div>
                    <p>{{ followedUser.name }}</p>
                  </div>
                </div>
                <div v-else>
                  <p>No followed users.</p>
                </div>
              </div>
            </div>
          </div>

          <!-- Liked Games -->
          <div class="w-full md:w-1/3 pr-4 mb-6">
            <div class="card bg-base-100/30 backdrop-blur-xl border border-gray-700 h-full">
              <div class="card-body">
                <h2 class="card-title">Liked Games</h2>
                <div v-if="likedGames.length">
                  <ul class="list-disc pl-5 space-y-1">
                    <li v-for="game in getItemsToShow(likedGames, 'likedGames')" :key="game.id">
                      <router-link :to="{ name: 'GamePage', params: {gameId: game.id}}"
                        @click.native="sendGameId(game.id)">
                        {{ game.name }}
                      </router-link>
                    </li>
                    <li v-if="likedGames.length > 6" class="text-gray-600">
                      and {{ likedGames.length - 6 }} more
                    </li>
                  </ul>
                </div>
                <div v-else>
                  <p>No liked games.</p>
                </div>
          </div>
        </div>
          </div>
        </div>
      </div>

      <div class="flex flex-wrap items-stretch">
        <!-- Followed Lists -->
        <div class="w-full md:w-1/3 pr-4 mb-6">
          <div class="card bg-base-100/30 backdrop-blur-xl border border-gray-700 h-full">
            <div class="card-body">
              <h2 class="card-title">Followed Lists</h2>
              <div v-if="followedLists.length">
                <div v-for="list in followedLists" :key="list.id" class="card bg-base-200/80 my-2">
                  <div class="p-2 flex justify-between">
                    <router-link :to="{ name: 'ListPage', params: {listId: list.id}}"
                                 @click.native="sendListId(list.id)">
                      <h3 class="font-semibold">{{ list.name }}: {{ list.description }}</h3>
                    </router-link>
                    <p class="text-sm">{{ list.author }}</p>
                  </div>

                  <!-- Games in the list -->
                  <div class="p-2 flex items-center">
                    <div v-if="list.games.length !== 0">
                      <span class="font-semibold">Games:</span>
                      <span v-for="(game, index) in getGamesToShow(list)" :key="game.id" class="ml-2">
                        <router-link :to="{ name: 'GamePage', params: {gameId: game.id}}"
                                     @click.native="sendGameId(game.id)">
                        {{ game.name }}
                      </router-link>
                        <span v-if="index < getGamesToShow(list).length - 1">,</span>
                      </span>
                      <span v-if="list.games.length > 2">, and {{ list.games.length - 2 }} more
                      </span>
                    </div>
                    <div v-else> No games added yet! </div>
                  </div>
                </div>
              </div>
              <div v-else>
                <p>No followed lists.</p>
              </div>
            </div>
          </div>
        </div>

        <!-- Liked Lists -->
        <div class="w-full md:w-1/3 pr-4 mb-6">
          <div class="card bg-base-100/30 backdrop-blur-xl border border-gray-700 h-full">
            <div class="card-body">
              <h2 class="card-title">Liked Lists</h2>
              <div v-if="likedLists.length">
                <div v-for="list in likedLists" :key="list.id" class="card bg-base-200/80 my-2">
                  <div class="p-2 flex justify-between">
                    <router-link :to="{ name: 'ListPage', params: {listId: list.id}}"
                                 @click.native="sendListId(list.id)">
                      <h3 class="font-semibold">{{ list.name }}: {{ list.description }}</h3>
                    </router-link>
                    <p class="text-sm">{{ list.author }}</p>
                  </div>

                  <!-- Games in the list -->
                  <div class="p-2 flex items-center">
                    <div v-if="list.games.length !== 0">
                      <span class="font-semibold">Games:</span>
                      <span v-for="(game, index) in getGamesToShow(list)" :key="game.id" class="ml-2">
                        <router-link :to="{ name: 'GamePage', params: {gameId: game.id}}"
                                     @click.native="sendGameId(game.id)">
                        {{ game.name }}
                      </router-link>
                        <span v-if="index < getGamesToShow(list).length - 1">,</span>
                      </span><span v-if="list.games.length > 2">, and {{ list.games.length - 2 }} more
                      </span>
                    </div>
                    <div v-else> No games added yet! </div>
                  </div>
                </div>
              </div>
              <div v-else>
                <p>No liked lists.</p>
              </div>
            </div>
          </div>
        </div>

        <!-- Created Game Lists -->
        <div class="w-full md:w-1/3 pr-4 mb-6">
          <div class="card bg-base-100/30 backdrop-blur-xl border border-gray-700 h-full">
            <div class="card-body">
              <h2 class="card-title">User Created Lists</h2>
              <div v-if="createdGameLists.length">
                <div v-for="list in createdGameLists" :key="list.id" class="card bg-base-200/80 my-2">
                  <div class="p-2 flex justify-between">
                    <router-link :to="{ name: 'ListPage', params: {listId: list.id}}"
                                 @click.native="sendListId(list.id)">
                      <h3 class="font-semibold">{{ list.name }}: {{ list.description }}</h3>
                    </router-link>
                    <p class="text-sm">{{ list.author }}</p>
                  </div>

                  <!-- Games in the list -->
                  <div class="p-2 flex items-center">
                    <div v-if="list.games.length !== 0">
                      <span class="font-semibold">Games:</span>
                      <span v-for="(game, index) in getGamesToShow(list)" :key="game.id" class="ml-2">
                        <router-link :to="{ name: 'GamePage', params: {gameId: game.id}}"
                                     @click.native="sendGameId(game.id)">
                        {{ game.name }}
                      </router-link>
                        <span v-if="index < getGamesToShow(list).length - 1">,</span>
                      </span>
                      <span v-if="list.games.length > 2">, and {{ list.games.length - 2 }} more
                      </span>
                    </div>
                    <div v-else> No games added yet! </div>
                  </div>
                </div>
              </div>
              <div v-else>
                <p>No user created lists.</p>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Playtimes -->
      <div class="flex flex-wrap items-stretch">
        <div class="w-full md:w-1/2 pr-4 mb-6">
          <div class="card bg-base-100/30 backdrop-blur-xl border border-gray-700 h-full">
            <div class="card-body">
              <h2 class="card-title">Playtimes</h2>
              <div v-if="playtimes.length">
                <table class="table w-full">
                  <thead>
                  <tr>
                    <th>Game Name</th>
                    <th>Playtime (hours)</th>
                  </tr>
                  </thead>
                  <tbody>
                  <tr v-for="playtime in playtimes" :key="playtime.id">
                    <router-link :to="{ name: 'GamePage', params: {gameId: playtime.gameId}}"
                                 @click.native="sendGameId(playtime.gameId)">
                      <td>{{ getGameNameById(playtime.gameId) }}</td>
                    </router-link>
                    <td>{{ playtime.playtime }}</td>
                  </tr>
                  </tbody>
                </table>
              </div>
              <div v-else>
                <p>No playtimes recorded.</p>
              </div>
            </div>
          </div>
        </div>
      </div>

    </div>
  </div>
  </div>
</template>

<script>
import TopBar from "@/components/TopBar.vue";
import AuthenticationPage from "@/components/authentication/AuthenticationPage.vue";

export default {
  components: { TopBar, AuthenticationPage },
  data() {
    return {
      user: {},
      userCreatedLists: [],
      likedLists: [],
      likedGames: [],
      followedLists: [],
      followedUsers: [],
      followerUsers: [],
      playtimes: [],
      createdGameLists: [],
      gameMap: {},

      loadedUID: false,
      loading: true,
      error: null,
    };
  },
  created() {
    this.getProfile();
  },
  methods: {
    getProfile() {
      this.loadedUID = localStorage.getItem("GamedUID") != null;

      if (this.loadedUID) {
        fetch("http://localhost:8084/user_page/" + localStorage.getItem("GamedUID"))
            .then((response) => {
              if (!response.ok) {
                throw new Error("Network response was not ok");
              }
              return response.json();
            })
            .then((data) => {
              this.user = data.userDTO;
              this.likedGames = data.likedGames;
              this.likedLists = data.likedLists;
              this.followedLists = data.followedLists;
              this.followedUsers = data.followedUsers;
              this.followerUsers = data.followerUsers;
              this.playtimes = data.playtimes;
              this.createdGameLists = data.createdGameLists;

              const allGames = [...this.likedGames];
              this.likedLists.forEach((list) => {
                allGames.push(...list.games);
              });
              this.followedLists.forEach((list) => {
                allGames.push(...list.games);
              });
              this.createdGameLists.forEach((list) => {
                allGames.push(...list.games);
              });
              allGames.forEach((game) => {
                this.gameMap[game.id] = game.name;
              });
            })
            .catch((error) => {
              this.error = error.message || "An error occurred while fetching the profile.";
            })
            .finally(() => {
              this.loading = false;
            });
      }
    },
    sendGameId(gameId) {
      this.$router.push({ name: 'GamePage', params: { gameId: gameId } });
    },
    sendListId(listId) {
      this.$router.push({ name: 'ListPage', params: { listId: listId } });
    },
    formatDate(dateString) {
      if (!dateString) return "N/A";
      const date = new Date(dateString);
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, "0");
      const day = String(date.getDate()).padStart(2, "0");

      return `${year}/${month}/${day}`;
    },
    getGamesToShow(list) {
      return list.games.slice(0, 2);
    },
    getItemsToShow(items, sectionName) {
      return items.slice(0, 6);
    },
    getGameNameById(gameId) {
      return this.gameMap[gameId] || "Unknown Game";
    },
  },
};
</script>

<style scoped>
.loading-spinner {
  --btn-loading: currentColor;
}
</style>