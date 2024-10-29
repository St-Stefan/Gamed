<template>
  <div class="navbar px-20">
    <!-- Left Side: Logo and Search Bar -->
    <div class="flex-1 flex items-center">
      <!-- Logo -->
      <router-link :to="{ name: 'Home' }"
                   class="btn btn-ghost text-3xl font-bold">
        Gamed
      </router-link>

      <!-- Search Bar -->
      <div class="ml-8 flex-1">
        <input
            id="search-field"
            type="text"
            placeholder="Search"
            class="input input-bordered w-full max-w-md"
            @keyup.enter="search()"
        />

      </div>
      <div class="dropdown dropdown-end">
        <div tabindex="0" role="button" class="btn btn-ghost btn-circle avatar">
          <div class="w-10 rounded-full">
            <img
                alt="Tailwind CSS Navbar component"
                src="https://static.wikia.nocookie.net/metalgear/images/b/bc/HideoKojimaInfobox.jpg" />
          </div>
        </div>
        <ul
            tabindex="0"
            class="menu menu-sm dropdown-content bg-base-100 rounded-box z-[1] mt-3 w-52 p-2 shadow">
          <li>
            <a class="justify-between">
              Profile
              <span class="badge">New</span>
            </a>
          </li>
          <li><a>Settings</a></li>
          <li @click="clearStorage()"><a>Logout</a></li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  methods:{
    clearStorage(){
      localStorage.removeItem("GamedUID")
      this.$emit('uidChanged');
    },
    search() {
      const query = document.getElementById('search-field').value;
      this.$router.push({name: 'Search', params: {query: query}});
      this.$emit('searchChanged');
    }
  }
}

</script>