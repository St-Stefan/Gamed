import { createRouter, createWebHistory } from 'vue-router';
import homePage from "@/components/home/HomePage.vue";
import HelloWorld from "@/components/HelloWorld.vue";

const routes = [
    {
        path: '/',
        name: 'Home',
        component: homePage,
    },
    {
        path: '/Profile',
        name: 'Profile',
        component: HelloWorld,
    },
    // Add more routes here
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;