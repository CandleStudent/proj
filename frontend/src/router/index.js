import { createRouter, createWebHistory } from 'vue-router'
import ProfileForm from '@/view/ProfileForm.vue'
import MenuForm from '@/view/MenuForm.vue'
import CartForm from '@/view/CartForm.vue'
import AuthForm from "@/view/AuthForm.vue";

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