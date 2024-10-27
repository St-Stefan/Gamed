import { createRouter, createWebHistory } from 'vue-router';
import homePage from "@/components/home/HomePage.vue";
import HelloWorld from "@/components/HelloWorld.vue";
import AuthenticationPage from "@/components/authentication/AuthenticationPage.vue";
import HomePage from "@/components/home/HomePage.vue";
import SearchPage from "@/components/search/SearchPage.vue";
const routes = [
    {
        path: '/',
        name: 'Home',
        component: HomePage,
    },
    {
        path: '/Profile',
        name: 'Profile',
        component: HelloWorld,
    },
    {
        path: '/Login',
        name: 'Login',
        component: AuthenticationPage,
    },
    {
        path: '/Search/:query',
        name: 'Search',
        component: SearchPage,
    }
    // Add more routes here
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;