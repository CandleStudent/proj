import { createRouter, createWebHistory } from 'vue-router'
import ProfileForm from '@/view/ProfileView.vue'
import MenuForm from '@/view/MenuView.vue'
import CartForm from '@/view/CartView.vue'
import AuthForm from "@/view/AuthView.vue";

const routes = [
  { path: '/auth', component: AuthForm },
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