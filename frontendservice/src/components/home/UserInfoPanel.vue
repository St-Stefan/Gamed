<template>
  <div class="user-info-panel bg-base-100 p-6 rounded-lg ">
    <template v-if="user">
      <h2 class="text-2xl font-semibold mb-4">User Information</h2>
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