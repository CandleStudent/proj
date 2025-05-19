import { createRouter, createWebHistory } from 'vue-router'
import RegisterForm from '../components/RegisterForm.vue'
import HomePage from '../components/HomePage.vue'

const routes = [
  { path: '/register', component: RegisterForm },
  { path: '/home', component: HomePage },
  { path: '/', redirect: '/register' }
]

export const router = createRouter({
  history: createWebHistory(),
  routes
})
