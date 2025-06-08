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
      <button
          @click="logout"
          class="px-4 py-2 font-semibold rounded-md bg-red-500 text-white hover:bg-red-600 transition duration-300">
        Выход
      </button>
    </nav>
  </header>
</template>
