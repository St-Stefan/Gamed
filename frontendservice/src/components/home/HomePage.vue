<script setup>

import WelcomeItem from "@/components/TopBar.vue";
import SampleCard from "@/components/ListCard.vue";
import TopBar from "@/components/TopBar.vue";
import ListCard from "@/components/ListCard.vue";
</script>

<template>
  <div class="flex flex-col h-screen">
    <div class="flex-1 overflow-y-auto">
      <div class="sticky top-0 z-10 backdrop-blur-xl drop-shadow-xl bg-base-200/70 border-b border-gray-700">
        <top-bar />
      </div>
      <div class="flex pl-20 pr-20 bg-base-200">
        <div class="w-2/3 flex-none grid place-items-center p-10 gap-10">
          <ListCard text="Lorem ipsum" username="Test" />
          <ListCard text="Lorem ipsum" username="Test" />
          <ListCard text="Lorem ipsum" username="Test" />
          <ListCard text="Lorem ipsum" username="Test" />
        </div>

        <div class="divider divider-horizontal"></div>

        <div class="flex-1 rounded-box place-items-center">
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>