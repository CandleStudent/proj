import { createRouter, createWebHistory } from 'vue-router'
import ProfileForm from '@/view/ProfileView.vue'
import MenuForm from '@/view/MenuView.vue'
import CartForm from '@/view/CartView.vue'
import AuthForm from '@/view/AuthView.vue'
import TestView from '@/view/TestView.vue';
import AdminView from "@/view/AdminView.vue";
import CourierView from "@/view/CourierView.vue";

const routes = [
  { path: '/auth', component: AuthForm },
  { path: '/profile', component: ProfileForm },
  { path: '/menu', component: MenuForm },
  { path: '/cart', component: CartForm },
  { path: '/', redirect: '/auth' },
  { path: '/test', component: TestView },
  { path: '/admin', component: AdminView },
  { path: '/courier', component: CourierView },
]

export const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem("jwt_token");
  if (to.meta.requiresAuth && !token) {
    next('/login');
  } else {
    next();
  }
});

// export default router;