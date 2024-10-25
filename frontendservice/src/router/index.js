import { createRouter, createWebHistory } from 'vue-router';
import homePage from "@/components/home/HomePage.vue";
import HelloWorld from "@/components/HelloWorld.vue";
import AuthenticationPage from "@/components/authentication/AuthenticationPage.vue";
import HomePage from "@/components/home/HomePage.vue";
import UserPage from "@/components/user_page/UserPage.vue";
const routes = [
    {
        path: '/',
        name: 'Home',
        component: HomePage,
    },
    {
        path: '/Profile',
        name: 'Profile',
        component: UserPage,
    },
    {
        path: '/Login',
        name: 'Login',
        component: AuthenticationPage,
    },
    // Add more routes here
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;