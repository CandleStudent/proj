<template>
  <div class="min-h-screen bg-[#f3fdf5] flex flex-col">
    <header class="w-full py-6 text-center bg-white shadow">
      <h1 class="text-2xl font-bold text-green-700">Yummy!</h1>
    </header>

    <div class="flex-grow flex items-center justify-center relative">
      <transition name="slide-fade" mode="out-in">
        <LoginForm v-if="!isRegistering" :on-toggle="toggleMode" key="login" />
        <RegisterForm v-else :on-toggle="toggleMode" key="register" />
      </transition>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import LoginForm from '../components/auth/LoginForm.vue'
import RegisterForm from '../components/auth/RegisterForm.vue'

const isRegistering = ref(false)
const toggleMode = () => (isRegistering.value = !isRegistering.value)
</script>
