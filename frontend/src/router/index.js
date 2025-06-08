import { createRouter, createWebHistory } from 'vue-router'
import ProfileForm from '@/view/ProfileView.vue'
import MenuForm from '@/view/MenuView.vue'
import CartForm from '@/view/CartView.vue'
import AuthForm from '@/view/AuthView.vue'
import AdminView from "@/view/AdminView.vue";
import CourierView from "@/view/CourierView.vue";

const routes = [
  { path: '/auth', component: AuthForm },
  { path: '/profile', component: ProfileForm, meta: {requiresAuth: true, roles: ['ROLE_CUSTOMER']} },
  { path: '/menu', component: MenuForm, meta: {requiresAuth: true, roles: ['ROLE_CUSTOMER']} },
  { path: '/cart', component: CartForm, meta: {requiresAuth: true, roles: ['ROLE_CUSTOMER']} },
  { path: '/', redirect: '/auth' },
  { path: '/admin', component: AdminView, meta: {requiresAuth: true, roles: ['ROLE_RESTAURANT_ADMIN']} },
  { path: '/courier', component: CourierView, meta: {requiresAuth: true, roles: ['ROLE_COURIER']} },
]

export const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const isAuthenticated = !!localStorage.getItem('jwt_token')
  const userRole = localStorage.getItem("user_role")

  if (to.meta.requiresAuth && !isAuthenticated) {
    next('/auth')
  } else if (to.path === '/auth' && isAuthenticated) {
    switch (userRole) {
      case 'ROLE_CUSTOMER':
        next('/menu')
        break
      case 'ROLE_COURIER':
        next('/courier')
        break
      case 'ROLE_RESTAURANT_ADMIN':
        next('/admin')
        break
      default:
        alert('Неизвестная роль')
    }
  }

  if (to.meta.roles && !to.meta.roles.includes(userRole)) {
    switch (userRole) {
      case 'ROLE_CUSTOMER':
        next('/menu')
        break
      case 'ROLE_COURIER':
        next('/courier')
        break
      case 'ROLE_RESTAURANT_ADMIN':
        next('/admin')
        break
      default:
        alert('Неизвестная роль')
    }
  } else {
    next()
  }

})
// export default router;