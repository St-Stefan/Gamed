<script setup>
import { defineProps } from 'vue';

defineProps({
  username: {
    type: String,
    required: true,
  },
  text: {
    type: String,
    required: true,
  },
  imageSrc: {
    type: String,
    required: false,
    default: '',
  },
  imageAlt: {
    type: String,
    required: false,
    default: '',
  },
});
</script>

<template>
  <div class="card bg-base-200 w-2/3 border border-gray-700 backdrop-blur-xl">
    <figure v-if="imageSrc" class="h-28">
      <img
          src="https://img.daisyui.com/images/stock/photo-1606107557195-0e29a4b5b4aa.webp"
          alt="Shoes" />
    </figure>
    <div class="card-body">
      <h2 class="card-title">{{username}}</h2>
      <p>{{ text }}</p>
      <div class="card-actions justify-end">
        <button class="btn bg-gray-600 backdrop-blur-xl">Buy Now</button>
      </div>
    </div>
  </div>
</template>



<style scoped>

</style>