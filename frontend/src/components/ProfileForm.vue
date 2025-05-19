<template>
  <div class="min-h-screen bg-green-50 text-gray-800 font-sans">
    <!-- Хедер -->
    <header class="bg-white shadow p-4 flex justify-between items-center">
      <h1 class="text-2xl font-bold text-green-700">Yummy!</h1>
      <div class="flex gap-4">
        <button class="header-btn" @click="goToMenu">Меню</button>
        <button class="header-btn" @click="goToCart">Корзина</button>
        <button class="header-btn" @click="logout">Выход</button>
      </div>
    </header>

    <!-- Основной контент -->
    <main class="flex flex-col lg:flex-row p-8 gap-8 justify-center items-start">
      <!-- Форма профиля -->
      <div class="bg-white rounded-lg shadow-md p-6 w-full max-w-md space-y-4">
        <h2 class="text-xl font-semibold text-green-700 mb-2">Профиль</h2>

        <div>
          <label class="block text-sm mb-1 text-gray-600">Имя</label>
          <input v-model="form.firstName" class="input" placeholder="Введите имя" />
        </div>

        <div>
          <label class="block text-sm mb-1 text-gray-600">Фамилия</label>
          <input v-model="form.lastName" class="input" placeholder="Введите фамилию" />
        </div>

        <div>
          <label class="block text-sm mb-1 text-gray-600">Телефон</label>
          <input v-model="form.phone" class="input" placeholder="+7 (___) ___-__-__" />
        </div>

        <div>
          <label class="block text-sm mb-1 text-gray-600">Электронная почта</label>
          <input :value="form.email" class="input bg-gray-100 cursor-not-allowed" disabled />
        </div>

        <button @click="updateProfile" class="w-full bg-green-600 hover:bg-green-700 text-white py-2 rounded transition">
          Обновить данные профиля
        </button>
      </div>

      <!-- Карточка заказов -->
      <div class="bg-white rounded-lg shadow-md p-6 w-full max-w-md text-center">
        <h2 class="text-lg font-semibold text-green-700 mb-2">Активные заказы</h2>

        <div v-if="orders.length === 0" class="text-gray-500">
          Нет активных заказов
        </div>

        <div v-else>
          <div class="border rounded p-4 mb-4 text-left">
            <p><strong>ID заказа:</strong> {{ currentOrder.id }}</p>
            <p><strong>Сумма:</strong> {{ currentOrder.amount }} ₽</p>
            <p><strong>Статус:</strong> {{ currentOrder.status }}</p>
          </div>

          <div class="flex justify-between">
            <button @click="prevOrder" class="text-green-600 hover:underline">&larr; Назад</button>
            <button @click="nextOrder" class="text-green-600 hover:underline">Вперёд &rarr;</button>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script>
import axios from 'axios'

// Вынесенные переменные
const API_HOST = 'http://localhost:8080'
const PROFILE_ENDPOINT = '/api/profile'
const ORDERS_ENDPOINT = '/api/orders/active'

export default {
  data() {
    return {
      form: {
        firstName: '',
        lastName: '',
        phone: '',
        email: '',
      },
      orders: [],
      orderIndex: 0,
    }
  },
  computed: {
    currentOrder() {
      return this.orders[this.orderIndex]
    }
  },
  methods: {
    async fetchProfile() {
      const token = localStorage.getItem('jwt_token')
      const res = await axios.get(`${API_HOST}${PROFILE_ENDPOINT}`, {
        headers: { Authorization: `Bearer ${token}` }
      })
      this.form = res.data
    },
    async fetchOrders() {
      const token = localStorage.getItem('jwt_token')
      const res = await axios.get(`${API_HOST}${ORDERS_ENDPOINT}`, {
        headers: { Authorization: `Bearer ${token}` }
      })
      this.orders = res.data
    },
    async updateProfile() {
      const token = localStorage.getItem('jwt_token')
      await axios.post(`${API_HOST}${PROFILE_ENDPOINT}`, this.form, {
        headers: { Authorization: `Bearer ${token}` }
      })
      alert('Данные профиля обновлены')
    },
    prevOrder() {
      if (this.orderIndex > 0) this.orderIndex--
    },
    nextOrder() {
      if (this.orderIndex < this.orders.length - 1) this.orderIndex++
    },
    goToMenu() {
      this.$router.push('/menu')
    },
    goToCart() {
      this.$router.push('/cart')
    },
    logout() {
      localStorage.removeItem('jwt_token')
      this.$router.push('/auth')
    }
  },
  mounted() {
    this.fetchProfile()
    this.fetchOrders()
  }
}
</script>

<style scoped>
.input {
  @apply w-full p-2 border rounded focus:outline-none focus:ring-2 focus:ring-green-500;
}
.header-btn {
  @apply bg-green-600 hover:bg-green-700 text-white px-4 py-2 rounded transition;
}
</style>
