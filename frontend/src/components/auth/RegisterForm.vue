<template>
  <div class="w-full max-w-md bg-white p-10 rounded-xl shadow-2xl" :class="{ 'animate-shake': shake }" @animationend="shake = false">
    <h2 class="text-3xl font-bold mb-8 text-green-800 text-center tracking-wide">Регистрация</h2>
    <form @submit.prevent="handleLogin">
      <!-- email + password поля (аналогично) -->
      <FormFields v-model:email="email" v-model:password="password" :email-error="emailError" :password-error="passwordError" />
      <div class="mb-6 mt-4 flex flex-col space-y-3">
        <button type="submit" class="w-full bg-green-700 hover:bg-green-800 text-white py-3 px-4 rounded-md text-base font-semibold transition-colors duration-300">Зарегистрироваться</button>
      </div>
      <div class="text-center text-sm text-gray-600">
        <p class="mt-4">Уже есть аккаунт?<button @click="onToggle" type="button" class="ml-1 text-green-700 font-semibold hover:underline">Войти</button></p>
      </div>
    </form>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import wrappingApi from '@/axios.js'
import FormFields from './FormFields.vue'

const props = defineProps({ onToggle: Function })
const router = useRouter()

const email = ref('')
const password = ref('')
const emailError = ref(false)
const passwordError = ref(false)
const shake = ref(false)

const isValidEmail = (email) => /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)

const handleLogin = async () => {
  let hasError = false
  if (!isValidEmail(email.value)) {
    emailError.value = true
    hasError = true
  }
  if (!password.value) {
    passwordError.value = true
    hasError = true
  }
  if (hasError) {
    shake.value = true
    return
  }

  try {
    const { data } = await wrappingApi.post('/auth/register', {
      email: email.value,
      password: password.value
    })
    localStorage.setItem('jwt_token', data.token)
    localStorage.setItem('user_role', data.userRole)
    switch (data.userRole) {
      case 'ROLE_CUSTOMER':
        await router.push('/menu')
        break
      case 'ROLE_COURIER':
        await router.push('/courier')
        break
      case 'ROLE_RESTAURANT_ADMIN':
        await router.push('/admin')
        break
      default:
        alert('Неизвестная роль: ' + data.userRole)
    }
  } catch (err) {
    alert(err.response.data)
    console.error(err)
  }
}
</script>
