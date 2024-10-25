<template>
  <div v-if="!loadedUID">
    <AuthenticationPage @uidChanged="getProfile" />
  </div>

  <div v-else class="flex flex-col h-screen bg-base-200">
    <div class="sticky top-0 z-10 backdrop-blur-xl drop-shadow-xl bg-base-200/70 border-b border-gray-700">
      <TopBar @uidChanged="getProfile" />
    </div>

    <div class="flex-1 overflow-y-auto p-6">
      <div class="card bg-base-100 shadow-xl mb-6">
        <div class="card-body flex items-center">
          <div class="avatar mr-4">
            <div class="w-24 rounded-full">
              <img src="https://placekitten.com/200/200" alt="User Avatar" />
            </div>
          </div>
          <div>
            <h2 class="card-title text-2xl">{{ user.name }}</h2>
            <p>Email: {{ user.email }}</p>
            <p>Premium: {{ user.premium ? 'Yes' : 'No' }}</p>
            <p>Developer: {{ user.developer ? 'Yes' : 'No' }}</p>
            <p>Profile Created At: {{ formatDate(user.timeCreated) }}</p>
          </div>
        </div>
      </div>

      <div class="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
        <div class="card bg-base-100 shadow-xl">
          <div class="card-body">
            <h2 class="card-title">Followed Users</h2>
            <div v-if="followedUsers.length">
              <div v-for="followedUser in followedUsers" :key="followedUser.id" class="flex items-center my-2">
                <div class="avatar mr-4">
                  <div class="w-12 rounded-full">
                    <img src="https://placekitten.com/100/100" alt="User Avatar" />
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

        <div class="card bg-base-100 shadow-xl">
          <div class="card-body">
            <h2 class="card-title">Follower Users</h2>
            <div v-if="followerUsers.length">
              <div v-for="followerUser in followerUsers" :key="followerUser.id" class="flex items-center my-2">
                <div class="avatar mr-4">
                  <div class="w-12 rounded-full">
                    <img src="https://placekitten.com/100/100" alt="User Avatar" />
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

      <div class="card bg-base-100 shadow-xl mb-6">
        <div class="card-body">
          <h2 class="card-title">Liked Games</h2>
          <div v-if="likedGames.length" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            <div v-for="game in likedGames" :key="game.id" class="card bg-base-200">
              <figure>
                <img src="https://placekitten.com/300/200" alt="Game Image" />
              </figure>
              <div class="card-body">
                <h3 class="card-title">{{ game.name }}</h3>
                <p>Developer: {{ game.developer }}</p>
                <p>Release Date: {{ formatDate(game.releaseDate) }}</p>
                <p>Platforms: {{ game.platforms }}</p>
              </div>
            </div>
          </div>
          <div v-else>
            <p>No liked games.</p>
          </div>
        </div>
      </div>

      <div class="card bg-base-100 shadow-xl mb-6">
        <div class="card-body">
          <h2 class="card-title">Liked Lists</h2>
          <div v-if="likedLists.length" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            <div v-for="list in likedLists" :key="list.id" class="card bg-base-200">
              <div class="card-body">
                <h3 class="card-title">{{ list.name }}</h3>
                <p>{{ list.description }}</p>
              </div>
            </div>
          </div>
          <div v-else>
            <p>No liked lists.</p>
          </div>
        </div>
      </div>

      <div class="card bg-base-100 shadow-xl mb-6">
        <div class="card-body">
          <h2 class="card-title">Followed Lists</h2>
          <div v-if="followedLists.length" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            <div v-for="list in followedLists" :key="list.id" class="card bg-base-200">
              <div class="card-body">
                <h3 class="card-title">{{ list.name }}</h3>
                <p>{{ list.description }}</p>
              </div>
            </div>
          </div>
          <div v-else>
            <p>No followed lists.</p>
          </div>
        </div>
      </div>

      <div class="card bg-base-100 shadow-xl mb-6">
        <div class="card-body">
          <h2 class="card-title">Playtimes</h2>
          <div v-if="playtimes.length">
            <table class="table w-full">
              <thead>
              <tr>
                <th>Game ID</th>
                <th>Playtime (hours)</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="playtime in playtimes" :key="playtime.id">
                <td>{{ playtime.gameId }}</td>
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

      <div class="card bg-base-100 shadow-xl">
        <div class="card-body">
          <h2 class="card-title">Created Game Lists</h2>
          <div v-if="createdGameLists.length" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            <div v-for="list in createdGameLists" :key="list.id" class="card bg-base-200">
              <div class="card-body">
                <h3 class="card-title">{{ list.name }}</h3>
                <p>{{ list.description }}</p>
                <p>Created At: {{ formatDate(list.time_created) }}</p>
              </div>
            </div>
          </div>
          <div v-else>
            <p>No game lists created.</p>
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
  components: {TopBar},
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
        fetch('http://localhost:8084/user_page/' + localStorage.getItem("GamedUID"))
            .then(response => {
              if (!response.ok) {
                throw new Error('Network response was not ok');
              }
              return response.json();
            })
            .then(data => {
              console.log(data);
              this.user = data.userDTO;
              this.likedGames = data.likedGames;
              this.likedLists = data.likedLists;
              this.followedLists = data.followedLists;
              this.followedUsers = data.followedUsers;
              this.followerUsers = data.followerUsers;
              this.playtimes = data.playtimes;
              this.createdGameLists = data.createdGameLists;
            })
            .catch(error => {
              this.error = error.message || 'An error occurred while fetching the profile.';
            })
            .finally(() => {
              this.loading = false;
            });
      }
    },
    formatDate(dateString) {
      if (!dateString) return 'N/A';
      const date = new Date(dateString);
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');

      return `${year}/${month}/${day}`;
    }
  }
};
</script>

<style scoped>

</style>
