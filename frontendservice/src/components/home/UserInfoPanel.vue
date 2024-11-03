<template>
  <div class="user-info-panel p-6 rounded-lg select-none">
    <template v-if="user">
      <h2 class="text-2xl font-semibold mb-4">User Information</h2>
      <div class="flex items-start">
        <!-- User Image -->
        <img
            class="w-24 h-24 rounded-full mr-4"
            src="https://placehold.co/600/orange/white"
            alt="User Avatar"
        />
        <!-- User Details -->
        <div>
          <p class="mb-2">
            <strong>Name:</strong> {{ user.name }}
          </p>
          <p class="mb-2">
            <strong>Email:</strong> {{ user.email }}
          </p>
          <p class="mb-2">
            <strong>Developer:</strong> {{ user.developer ? 'Yes' : 'No' }}
          </p>
          <p class="mb-2">
            <strong>Premium User:</strong> {{ user.premium ? 'Yes' : 'No' }}
          </p>
          <!-- Additional user details can be added here -->
        </div>
      </div>
    </template>
    <template v-else>
      <p class="text-gray-300">Select a post to see user information.</p>
    </template>
  </div>
</template>

<script>
export default {
  name: 'UserInfoPanel',
  props: {
    user: {
      type: Object,
      default: null,
    },
  },
};
</script>