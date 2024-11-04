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
      class="card border border-gray-700 bg-base-100/30  backdrop-blur-xl rounded-lg p-4 mb-4 cursor-pointer w-3/4"
      @click="selectPost"
  >
    <h2 v-if="post.isReview" class="text-xl font-semibold mb-2">Review of {{post.games[0].name}} by {{post.author}}</h2>
    <h2 v-if="post.isList" class="text-xl font-semibold mb-2">{{post.title}} by {{post.author}}</h2>
    <h2 v-if="post.isListLike" class="text-xl font-semibold mb-2">{{post.author}} has liked:</h2>
    <h2 v-if="post.isGameLike" class="text-xl font-semibold mb-2">{{post.author}} has liked:</h2>
    <p  v-if="!post.isGameLike&&!post.isListLike"class="text-gray-300 mb-4">{{post.content}}</p>

    <!-- Review-specific Game Info -->
    <div
        v-if="post.isReview && post.games.length > 0"
        class="game-info bg-base-200/80 border border-gray-800 backdrop-blur-lg p-3 rounded mb-4"
    >
      <h3 class="text-lg font-medium">{{ post.games[0].name }}</h3>
      <p class="text-sm text-gray-600">
        Developer: {{ post.games[0].developer }}
      </p>
      <p class="text-sm text-gray-600">
        Release Date: {{ formatDate(post.games[0].releaseDate) }}
      </p>
      <p class="text-sm text-gray-600">
        Platforms: {{ post.games[0].platforms }}
      </p>
    </div>

    <!-- List-specific Game Info -->
    <div v-if="post.isList" class="game-list rounded">
      <div
          v-for="game in post.games"
          class="game-info bg-base-200/80 border border-gray-800 backdrop-blur-lg p-3 rounded mb-4"
      >
        <h3 class="text-lg font-medium">{{ game.name }}</h3>
        <p class="text-sm text-gray-600">
          Developer: {{ game.developer }}
        </p>
        <p class="text-sm text-gray-600">
<!--          {{ game.release_date }}-->
          Release Date: {{ formatDate(game.releaseDate) }}
        </p>
        <p class="text-sm text-gray-600">
          Platforms: {{ game.platforms }}
        </p>
      </div>
    </div>

    <div
        v-if="post.isGameLike && post.games.length > 0"
        class="game-info bg-base-200/80 border border-gray-800 backdrop-blur-lg p-3 rounded mb-4"
    >
      <h3 class="text-lg font-medium">{{ post.games[0].name }}</h3>
      <p class="text-sm text-gray-600">
        Developer: {{ post.games[0].developer }}
      </p>
      <p class="text-sm text-gray-600">
        Release Date: {{ formatDate(post.games[0].releaseDate) }}
      </p>
      <p class="text-sm text-gray-600">
        Platforms: {{ post.games[0].platforms }}
      </p>
    </div>

    <div
        v-if="post.isListLike && post.games.length > 0"
        class="game-info border border-gray-800 backdrop-blur-lg p-3 rounded mb-4"
    >
      <h3 class="text-lg font-medium">{{ post.title }}</h3>
      <p class="text-sm">
        Games: {{ post.games.map(x => " "+ x.name).toString() }}
      </p>
    </div>

    <div class="card-footer flex justify-between items-center text-sm text-gray-600">
      <span>By {{ post.author }}</span>
      <span>{{ formatTimestamp(post.timestamp) }}</span>
      <span v-if="!post.isGameLike&&!post.isListLike">Likes: {{ post.likes }}</span>
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