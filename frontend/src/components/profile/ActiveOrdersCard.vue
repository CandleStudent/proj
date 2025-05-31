<script>
import axios from 'axios'

const API_HOST = 'http://localhost:8080'
const ORDERS_ENDPOINT = '/api/order/active'

export default {
  data() {
    return {
      form: {
        name: '',
        surname: '',
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
    async fetchOrders() {
      const token = localStorage.getItem('jwt_token')
      const res = await axios.get(`${API_HOST}${ORDERS_ENDPOINT}`, {
        headers: { Authorization: `Bearer ${token}` }
      })
      this.orders = res.data
    },
    prevOrder() {
      if (this.orderIndex > 0) this.orderIndex--
    },
    nextOrder() {
      if (this.orderIndex < this.orders.length - 1) this.orderIndex++
    }
  },
  mounted() {
    this.fetchOrders()
  }
}
</script>

<template>
  <!-- Карточка заказов -->
  <div class="bg-white rounded-lg shadow-md p-6 w-full max-w-md text-center">
    <h2 class="text-lg font-semibold text-green-700 mb-2">Активные заказы</h2>

    <div v-if="orders.length === 0" class="text-gray-500">
      Нет активных заказов
    </div>

    <div v-else>
      <div class="border rounded p-4 mb-4 text-left">
        <p><strong>ID заказа:</strong> {{ currentOrder.id }}</p>
        <p><strong>Сумма:</strong> {{ currentOrder.cost }} ₽</p>
        <p><strong>Статус:</strong> {{ currentOrder.status }}</p>
        <p><strong>Адрес доставки:</strong> {{ currentOrder.customerFormattedAddress }}</p>
        <p><strong>Адрес ресторана:</strong> {{ currentOrder.restaurantFormattedAddress }}</p>
      </div>

      <div class="flex justify-between">
        <button @click="prevOrder" class="text-green-600 hover:underline">&larr; Назад</button>
        <button @click="nextOrder" class="text-green-600 hover:underline">Вперёд &rarr;</button>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>