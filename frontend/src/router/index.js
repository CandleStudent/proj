import { createRouter, createWebHistory } from 'vue-router'
import HomePage from '../components/HomePage.vue'
import ProfileForm from '@/components/ProfileForm.vue'
import MenuForm from '@/components/MenuForm.vue'
import CartForm from '@/components/CartForm.vue'
import AuthForm from "@/components/AuthForm.vue";

const routes = [
  { path: '/auth', component: AuthForm },
  { path: '/home', component: HomePage },
  { path: '/profile', component: ProfileForm },
  { path: '/menu', component: MenuForm },
  { path: '/cart', component: CartForm },
  { path: '/', redirect: '/auth' }
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