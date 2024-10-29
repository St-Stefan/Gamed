<script setup>
import { defineProps } from 'vue';

defineProps({
  game: {
    type: Object,
    required: true,
  }
});
</script>

<template>
  <div
      class="card border border-gray-700 bg-base-100/30 rounded-lg p-4 mb-4 cursor-pointer"
      @click="selectGame"
  >
    <h2 class="text-xl font-semibold mb-2">{{ game.game.name }}</h2>
    <p class="text-gray-300 mb-4">Developer: {{ game.game.developer }}</p>
    <p class="text-gray-300 mb-4">Release Date: {{ formatDate(game.game.releaseDate) }}</p>
    <p class="text-gray-300 mb-4">Platforms: {{ game.game.platforms }}</p>

  </div>
</template>



<script>
export default {
  name: 'Card',
  props: {
    game: {
      type: Object,
      required: true,
    },
  },
  methods: {
    selectGame() {
      this.$emit('select-game', this.game);
      console.log(this.game)
    },
    formatDate(dateString) {
      if (!dateString) return "N/A";
      const date = new Date(dateString);
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, "0");
      const day = String(date.getDate()).padStart(2, "0");

      return `${year}/${month}/${day}`;
    }
  },
};
</script>