<script>
import wrappingApi from '@/axios.js'
import OrderDetails from "@/components/profile/OrderDetails.vue";
import { formatDateTime } from '@/utils/utils.js'

const ORDERS_ENDPOINT = '/order/active'

export default {
  components: {OrderDetails},
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
      showOrderDetails: false
    }
  },
  computed: {
    currentOrder() {
      return this.orders[this.orderIndex]
    },
  },
  methods: {
    formatDateTime,
    async updateActiveOrders() {
      await this.fetchOrders()
      this.orderIndex = 0
    },
    async fetchOrders() {
      const res = await wrappingApi.get(`${ORDERS_ENDPOINT}`)
      this.orders = res.data
    },
    prevOrder() {
      if (this.orderIndex > 0) this.orderIndex--
    },
    nextOrder() {
      if (this.orderIndex < this.orders.length - 1) this.orderIndex++
    },
    async cancelOrder() {
      try {
        const res = await wrappingApi.delete(`/order/cancel/${this.orders[this.orderIndex].id}`)

        alert('Заказ отменён')
      } catch (e) {
        alert(e.message || 'Ошибка при отмене заказа')
      }
      // Обновляем список заказов
      await this.fetchOrders()
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
        <p><strong>Создан:</strong> {{ formatDateTime(currentOrder.orderCreatedAt) }}</p>
      </div>

      <div class="flex justify-between">
        <button @click="prevOrder" class="text-green-600 hover:underline">&larr; Назад</button>
        <button @click="nextOrder" class="text-green-600 hover:underline">Вперёд &rarr;</button>
      </div>

      <div class="space-x-4 mb-4 mt-8">
        <button
            @click="showOrderDetails = true"
            class="bg-yellow-400 hover:bg-yellow-500 text-gray-900 px-4 py-2 rounded">
          Состав заказа
        </button>
      </div>

    </div>

  </div>

  <OrderDetails
      v-if="showOrderDetails"
      :order="currentOrder"
      @close="showOrderDetails = false"
      @cancelled="updateActiveOrders"
      @updated="updateActiveOrders"
  />
</template>

<style scoped>

</style>