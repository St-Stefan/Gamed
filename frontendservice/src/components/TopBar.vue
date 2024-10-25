<template>
  <div class="navbar navbg-base-100 pr-20 pl-20">
    <div class="flex-1">
      <router-link to="/" class="justify-between"> <a class="btn btn-ghost text-xl font-bold">Gamed</a> </router-link>
    </div>
    <div class="flex-none gap-2">
      <div class="form-control">
        <input type="text" placeholder="Search" class="input input-bordered w-24 md:w-auto" />
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
            <router-link to="/Profile" class="justify-between">
              Profile
              <span class="badge">New</span>
            </router-link>
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
        }
    }
}

</script>
