<script setup>

</script>

<template>
    <div class="fixed inset-0 bg-black/50 backdrop-blur-sm flex items-center justify-center z-50">
        <div class="w-80 bg-base-200 rounded-lg shadow-lg p-6">
            <h2 class="text-2xl font-semibold text-center mb-6">Gamed Login</h2>

          <div class="flex justify-center mb-4">
            <button
                @click="register = true"
                :class="['px-4 py-2 rounded-l-md focus:outline-none', register ? 'bg-gray-900 text-white' : 'bg-gray-500 text-gray-700']"
            >
              Create User
            </button>
            <button
                @click="register = false"
                :class="['px-4 py-2 rounded-r-md focus:outline-none', !register ? 'bg-gray-900 text-white' : 'bg-gray-500 text-gray-700']"
            >
              Insert UID
            </button>
          </div>
          <div v-if="register">
            <form @submit.prevent="handleSubmit">
                <div class="mb-4">
                    <label for="email" class="block font-medium mb-2">Email</label>
                    <input
                        v-model="email"
                        type="text"
                        id="email"
                        required
                        class="w-full px-4 py-2 border border-base-100 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                </div>
                <div class="mb-4">
                    <label for="username" class="block font-medium mb-2">Username</label>
                    <input
                            v-model="username"
                            type="text"
                            id="username"
                            required
                            class="w-full px-4 py-2 border border-base-100 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                </div>
                <div class="mb-6">
                    <label for="password" class="block font-medium mb-2">Password</label>
                    <input
                            v-model="password"
                            type="password"
                            id="password"
                            required
                            class="w-full px-4 py-2 border border-base-100 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                </div>
                <button
                        type="submit"
                        class="w-full bg-gray-700 hover:bg-gray-400 backdrop-blur-sm text-white font-semibold py-2 rounded-md focus:outline-none"
                >
                    Login
                </button>
            </form>
            </div>
            <div v-else>
              <form @submit.prevent="handleUID">
                <div class="mb-4">
                  <label for="email" class="block font-medium mb-2">UID</label>
                  <input
                      v-model="forceUID"
                      type="text"
                      id="UID"
                      required
                      class="w-full px-4 py-2 border border-base-100 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                </div>
                <button
                    type="submit"
                    class="w-full bg-gray-700 hover:bg-gray-400 backdrop-blur-sm text-white font-semibold py-2 rounded-md focus:outline-none"
                >
                  Login
                </button>
              </form>
            </div>
        </div>
    </div>
</template>

<script>
import AuthenticationService from "@/services/AuthenticationService.js";

export default {
    name: "LoginOverlay",
    data() {
        return {
            email: "",
            username: "",
            password: "",
            register: false,
            forceUID: ""
        };
    },
    methods: {
        async handleSubmit() {
            let userdata = {
                username: this.username,
                email: this.email,
                pwdHash: this.password,
                developer: false,
                premium: false
            };
            let UID = await AuthenticationService.createUser(userdata);
            localStorage.setItem("GamedUID",UID)
            this.$emit('uidChanged');
        },
        handleUID(){
          localStorage.setItem("GamedUID",this.forceUID)
          this.$emit('uidChanged');
        }
    },
};
</script>