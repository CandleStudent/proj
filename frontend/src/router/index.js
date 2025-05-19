import { createRouter, createWebHistory } from 'vue-router'
import HomePage from '../components/HomePage.vue'
import AuthForm from "@/components/AuthForm.vue";

const routes = [
  { path: '/auth', component: AuthForm },
  { path: '/home', component: HomePage },
  { path: '/', redirect: '/auth' }
]

export const router = createRouter({
  history: createWebHistory(),
  routes
})
