<!--<template>-->
<!--  <div class="navbar px-20">-->
<!--    &lt;!&ndash; Left Side: Logo and Search Bar &ndash;&gt;-->
<!--    <div class="flex-1 flex items-center">-->
<!--      &lt;!&ndash; Logo &ndash;&gt;-->
<!--      <router-link :to="{ name: 'Home' }"-->
<!--                   class="btn btn-ghost text-3xl font-bold">-->
<!--        Gamed-->
<!--      </router-link>-->

<!--      &lt;!&ndash; Search Bar &ndash;&gt;-->
<!--      <div class="ml-8 flex-1">-->
<!--        <input-->
<!--            type="text"-->
<!--            placeholder="Search"-->
<!--            class="input input-bordered w-full max-w-md"-->
<!--        />-->
<!--      </div>-->
<!--    </div>-->

<!--    &lt;!&ndash; Right Side: Menu Options and Avatar &ndash;&gt;-->
<!--    <div class="flex-none flex items-center gap-4">-->
<!--      &lt;!&ndash; Menu Options &ndash;&gt;-->
<!--      <router-link :to="{ name: 'UserPage' }"-->
<!--                   class="btn btn-ghost text-xl normal-case">-->
<!--        Profile-->
<!--      </router-link>-->
<!--      <a class="btn btn-ghost text-xl normal-case" href="#settings">Settings</a>-->

<!--      &lt;!&ndash; Avatar with Logout Dropdown &ndash;&gt;-->
<!--      <div class="dropdown dropdown-end">-->
<!--        <button tabindex="0" class="btn btn-ghost btn-circle avatar">-->
<!--          <div class="w-10 rounded-full">-->
<!--            <img-->
<!--                alt="User Avatar"-->
<!--                src="https://static.wikia.nocookie.net/metalgear/images/b/bc/HideoKojimaInfobox.jpg"-->
<!--            />-->
<!--          </div>-->
<!--        </button>-->
<!--        <ul-->
<!--            tabindex="0"-->
<!--            class="menu menu-sm dropdown-content bg-base-100 rounded-box z-10 mt-3 w-52 p-2 shadow"-->
<!--        >-->
<!--          <li @click="clearStorage()">-->
<!--            <a>Logout</a>-->
<!--          </li>-->
<!--        </ul>-->
<!--      </div>-->
<!--    </div>-->
<!--  </div>-->
<!--</template>-->

<!--<script>-->
<!--export default {-->
<!--  methods:{-->
<!--    clearStorage(){-->
<!--      localStorage.removeItem("GamedUID")-->
<!--      this.$emit('uidChanged');-->
<!--    }-->
<!--  }-->
<!--}-->

<!--</script>-->

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
      <div class="ml-8 flex-1 form-control">
        <input
            id="search-field"
            type="text"
            placeholder="Search"
            class="input input-bordered w-full max-w-md" @keydown.enter="search()" @keyup.enter="search()"
        />
      </div>
    </div>

    <!-- Right Side: Menu Options and Avatar -->
    <div class="flex-none flex items-center gap-4">
      <!-- Menu Options -->
      <router-link :to="{ name: 'UserPage' }"
                    class="btn btn-ghost text-xl normal-case">
        Profile
      </router-link>
      <a class="btn btn-ghost text-xl normal-case" href="#settings">Settings</a>

      <!-- Avatar with Logout Dropdown -->
      <div class="dropdown dropdown-end">
        <button tabindex="0" class="btn btn-ghost btn-circle avatar">
          <div class="w-10 rounded-full">
            <img
                alt="User Avatar"
                src="https://static.wikia.nocookie.net/metalgear/images/b/bc/HideoKojimaInfobox.jpg"
            />
          </div>
        </button>
        <ul
            tabindex="0"
            class="menu menu-sm dropdown-content bg-base-100 rounded-box z-10 mt-3 w-52 p-2 shadow"
        >
          <li @click="clearStorage()">
            <a>Logout</a>
          </li>
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