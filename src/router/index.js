import Vue from 'vue';
import VueRouter from 'vue-router';
import Login from '@/views/Login.vue';
import Error from '@/views/Error.vue';
import Home from '@/views/Home.vue';

Vue.use(VueRouter);

const routes = [
  {
    path: '/',
    name: 'Login',
    component: Login,
  },
  {
    path: '/phonebook/:pid',
    name: 'Home',
    component: Home,
  },
  {
    path: '/*',
    name: 'Error',
    component: Error,
  },
];

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes,
});

export default router;
