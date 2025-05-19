<template>
  <form @submit.prevent="register">
    <div>
      <label>Email:</label>
      <input v-model="email" type="email" required />
    </div>
    <div>
      <label>Password:</label>
      <input v-model="password" type="password" required />
    </div>
    <div v-if="error" style="color:red">{{ error }}</div>
    <button type="submit">Зарегистрироваться</button>
  </form>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

const email = ref('')
const password = ref('')
const error = ref('')
const router = useRouter()

const register = async () => {
  if (!email.value || !password.value) {
    error.value = 'Все поля обязательны'
    return
  }
  try {
    await axios.post('http://localhost:8080/api/auth/register', {
      email: email.value,
      password: password.value
    })
    router.push('/home')
  } catch (e) {
    error.value = e.response?.data || 'Ошибка регистрации'
  }
}
</script>
