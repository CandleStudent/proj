<template>
  <div class="min-h-screen bg-[#f3fdf5] flex flex-col">
    <!-- Header -->
    <header class="w-full py-6 text-center bg-white shadow">
      <h1 class="text-2xl font-bold text-green-700">🍽️ Yummy!</h1>
    </header>

    <!-- Form Card -->
    <div class="flex-grow flex items-center justify-center">
      <transition name="slide-fade" mode="out-in">
        <div
            :key="isRegistering"
            class="w-full max-w-md bg-white p-10 rounded-xl shadow-2xl"
            :class="{ 'animate-shake': shake }"
            @animationend="shake = false"
        >
          <h2 class="text-3xl font-bold mb-8 text-green-800 text-center tracking-wide">
            {{ isRegistering ? 'Регистрация' : 'Вход' }}
          </h2>


          <form @submit.prevent="handleSubmit">
            <!-- Email -->
            <div class="mb-2">
              <label for="email" class="block text-base font-medium text-gray-600 mb-2 text-center">Email</label>
              <input
                  type="email"
                  id="email"
                  v-model="email"
                  @input="emailError = false"
                  placeholder="example@domain.com"
                  :class="[
                  'w-full px-4 py-3 text-base border rounded-md focus:outline-none focus:ring-2 transition-colors duration-300',
                  emailError ? 'border-red-400 focus:ring-red-400' : 'focus:ring-green-500'
                ]"
              />
              <p v-if="emailError" class="mt-1 text-sm text-red-500">Введите корректный email</p>
            </div>

            <!-- Password -->
            <div class="mb-6">
              <label for="password" class="block text-base font-medium text-gray-600 mb-2 text-center">Пароль</label>
              <input
                  type="password"
                  id="password"
                  v-model="password"
                  @input="passwordError = false"
                  placeholder="••••••••"
                  :class="[
                  'w-full px-4 py-3 text-base border rounded-md focus:outline-none focus:ring-2 transition-colors duration-300',
                  passwordError ? 'border-red-400 focus:ring-red-400' : 'focus:ring-green-500'
                ]"
              />
              <p v-if="passwordError" class="mt-1 text-sm text-red-500">Поле обязательно для заполнения</p>
            </div>

            <!-- Buttons -->
            <div class="mb-6 mt-4 flex flex-col space-y-3">
              <button
                  type="submit"
                  class="w-full bg-green-700 hover:bg-green-800 text-white py-3 px-4 rounded-md text-base font-semibold transition-colors duration-300"
              >
                {{ isRegistering ? 'Зарегистрироваться' : 'Войти' }}
              </button>
            </div>

            <!-- Links -->
            <div class="text-center text-sm text-gray-600">
              <a href="#" class="text-green-600 hover:text-green-800 transition-colors duration-300">
                Забыли пароль?
              </a>
              <p class="mt-4">
                {{ isRegistering ? 'Уже есть аккаунт?' : 'Нет аккаунта?' }}
                <button @click="toggleMode" type="button" class="ml-1 text-green-700 font-semibold hover:underline">
                  {{ isRegistering ? 'Войти' : 'Зарегистрироваться' }}
                </button>
              </p>
            </div>
          </form>
        </div>
      </transition>
    </div>
  </div>
</template>

<script setup>
  import { ref } from 'vue'
  import axios from 'axios'
  import { useRouter } from 'vue-router'

  const router = useRouter()

  // 👉 Переменные для API
  const API_HOST = 'http://localhost:8080'
  const REGISTER_ENDPOINT = '/api/auth/register'
  const LOGIN_ENDPOINT = '/api/auth/login'

  // 👉 Данные формы
  const email = ref('')
  const password = ref('')

  const emailError = ref(false)
  const passwordError = ref(false)
  const shake = ref(false)
  const isRegistering = ref(true)

  const toggleMode = () => {
    isRegistering.value = !isRegistering.value
    emailError.value = false
    passwordError.value = false
    shake.value = false
  }

  const isValidEmail = (email) => /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)

  const handleSubmit = async () => {
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
    const endpoint = isRegistering.value ? REGISTER_ENDPOINT : LOGIN_ENDPOINT
    const url = `${API_HOST}${endpoint}`

    var response = await axios.post(url, {
      email: email.value,
      password: password.value
    })

    const token = response.data.token;
    localStorage.setItem("jwt_token", token);

    router.push('/home')
  } catch (err) {
    alert(err.response.data)
    console.error(err)
  }
}

</script>

<style scoped>
@keyframes shake {
  0% {
    transform: translateX(0);
  }
  20% {
    transform: translateX(-8px);
  }
  40% {
    transform: translateX(8px);
  }
  60% {
    transform: translateX(-6px);
  }
  80% {
    transform: translateX(6px);
  }
  100% {
    transform: translateX(0);
  }
}

.animate-shake {
  animation: shake 0.4s ease-in-out;
}

.slide-fade-enter-active,
.slide-fade-leave-active {
  transition: all 0.4s ease;
}

.slide-fade-enter-from {
  opacity: 0;
  transform: translateY(20px);
}

.slide-fade-leave-to {
  opacity: 0;
  transform: translateY(-20px);
}
</style>
