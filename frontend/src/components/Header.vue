<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

function logout() {
  localStorage.removeItem('jwt_token')
  router.push('/auth')
}
</script>

<template>
  <header class="bg-white shadow p-4 flex justify-between items-center sticky top-0 z-30">
    <div class="text-2xl font-bold text-green-700">Yummy!</div>
    <nav class="space-x-4 px-4">
      <RouterLink
          to="/menu"
          class="px-4 py-2 font-semibold text-green-800 rounded-md transition duration-300 hover:bg-green-100 hover:text-green-900"
          :class="{ 'bg-green-600 text-white': $route.path === '/menu' }">
        Меню
      </RouterLink>
      <RouterLink
          to="/profile"
          class="px-4 py-2 font-semibold text-green-800 rounded-md transition duration-300 hover:bg-green-100 hover:text-green-900"
          :class="{ 'bg-green-600 text-white': $route.path === '/profile' }">
        Профиль
      </RouterLink>
      <RouterLink
          to="/cart"
          class="px-4 py-2 font-semibold text-green-800 rounded-md transition duration-300 hover:bg-green-100 hover:text-green-900"
          :class="{ 'bg-green-600 text-white': $route.path === '/cart' }">
        Корзина
      </RouterLink>
      <button
          @click="logout"
          class="px-4 py-2 font-semibold rounded-md bg-red-500 text-white hover:bg-red-600 transition duration-300">
        Выход
      </button>
    </nav>
  </header>
</template>
