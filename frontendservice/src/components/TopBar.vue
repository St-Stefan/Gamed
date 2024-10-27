<!--<template>-->
<!--  <div class="navbar navbg-base-100 pr-20 pl-20">-->
<!--    <div class="flex-1">-->
<!--      <a class="btn btn-ghost text-3xl font-bold">Gamed</a>-->
<!--    </div>-->
<!--    <div class="flex-none gap-2">-->
<!--      <div class="form-control">-->
<!--        <input type="text" placeholder="Search" class="input input-bordered w-24 md:w-auto" />-->
<!--      </div>-->
<!--      <div class="dropdown dropdown-end">-->
<!--        <div tabindex="0" role="button" class="btn btn-ghost btn-circle avatar">-->
<!--          <div class="w-10 rounded-full">-->
<!--            <img-->
<!--                alt="Tailwind CSS Navbar component"-->
<!--                src="https://static.wikia.nocookie.net/metalgear/images/b/bc/HideoKojimaInfobox.jpg" />-->
<!--          </div>-->
<!--        </div>-->
<!--        <ul-->
<!--            tabindex="0"-->
<!--            class="menu menu-sm dropdown-content bg-base-100 rounded-box z-[1] mt-3 w-52 p-2 shadow">-->
<!--          <li>-->
<!--            <a class="justify-between">-->
<!--              Profile-->
<!--              <span class="badge">New</span>-->
<!--            </a>-->
<!--          </li>-->
<!--          <li><a>Settings</a></li>-->
<!--          <li @click="clearStorage()"><a>Logout</a></li>-->
<!--        </ul>-->
<!--      </div>-->
<!--    </div>-->
<!--  </div>-->
<!--</template>-->
<template>
  <div class="navbar px-20">
    <!-- Left Side: Logo and Search Bar -->
    <div class="flex-1 flex items-center">
      <!-- Logo -->
      <a class="btn btn-ghost text-3xl font-bold">Gamed</a>
      <!-- Search Bar -->
      <div class="ml-8 flex-1">
        <input
            type="text"
            placeholder="Search"
            class="input input-bordered w-full max-w-md"
        />
      </div>
    </div>

    <!-- Right Side: Menu Options and Avatar -->
    <div class="flex-none flex items-center gap-4">
      <!-- Menu Options -->
      <a class="btn btn-ghost text-xl normal-case" href="#profile">Profile</a>
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
        }
    }
}

</script>
