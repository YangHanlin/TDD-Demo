import Vue from 'vue';
import VueRouter from 'vue-router';
import Login from '@/views/Login';
import Error from '@/views/Error';
import Home from '@/views/Home';

Vue.use(VueRouter);

const routes = [
  {
    path: '/',
    name: 'Login',
    component: Login,
  },
  {
    path: '/:pid',
    name: 'Home',
    component: Home,
  },
  {
    path: '/*',
    name: 'Error',
  },
];

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes,
});

export default router;
