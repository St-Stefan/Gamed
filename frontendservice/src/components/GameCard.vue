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
      class="card border border-gray-700 rounded-lg p-4 mb-4 cursor-pointer"
      @click="selectGame"
  >
    <h2 class="text-xl font-semibold mb-2">{{ game.game.name }}</h2>
    <p class="text-gray-300 mb-4">{{ game.game.developer }}</p>
    <p class="text-gray-300 mb-4">{{ game.game.release_date }}</p>
    <p class="text-gray-300 mb-4">{{ game.game.platforms }}</p>

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
    },
  },
};
</script>