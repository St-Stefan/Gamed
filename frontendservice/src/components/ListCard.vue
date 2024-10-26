<script setup>
import { defineProps } from 'vue';

defineProps({
  post: {
    type: Object,
    required: true,
  }
});
</script>

<template>
  <div
      class="card border border-gray-700 rounded-lg p-4 mb-4 cursor-pointer"
      @click="selectPost"
  >
    <h2 class="text-xl font-semibold mb-2">Review of {{post.games[0].name}} by {{post.author}}</h2>
    <p class="text-gray-300 mb-4">{{ post.content }}</p>

    <!-- Review-specific Game Info -->
    <div
        v-if="post.games.length > 0"
        class="game-info bg-base-200 p-3 rounded mb-4"
    >
      <h3 class="text-lg font-medium">{{ post.games[0].name }}</h3>
      <p class="text-sm text-gray-600">
        Developer: {{ post.games[0].developer }}
      </p>
      <p class="text-sm text-gray-600">
        Release Date: {{ formatDate(post.games[0].release_date) }}
      </p>
      <p class="text-sm text-gray-600">
        Platforms: {{ post.games[0].platforms }}
      </p>
    </div>

    <!-- List-specific Game Info -->
    <div v-if="post.isList" class="game-list bg-gray-100 p-3 rounded mb-4">
      <h3 class="text-lg font-medium mb-2">Games in this List:</h3>
      <ul class="list-disc list-inside">
        <li
            v-for="game in post.games"
            :key="game.id"
            class="text-sm text-gray-700"
        >
          {{ game.name }}
        </li>
      </ul>
    </div>

    <div class="card-footer flex justify-between items-center text-sm text-gray-600">
      <span>By {{ post.author }}</span>
      <span>{{ formatTimestamp(post.timestamp) }}</span>
      <span>Likes: {{ post.likes }}</span>
    </div>
  </div>
</template>



<script>
export default {
  name: 'Card',
  props: {
    post: {
      type: Object,
      required: true,
    },
  },
  methods: {
    selectPost() {
      this.$emit('select-post', this.post);
    },
    formatDate(date) {
      // Implement your date formatting logic here
      return new Date(date).toLocaleDateString();
    },
    formatTimestamp(timestamp) {
      // Implement your timestamp formatting logic here
      return new Date(timestamp).toLocaleString();
    },
  },
};
</script>