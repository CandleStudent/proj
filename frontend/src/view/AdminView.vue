<script>
import axios from 'axios'
import OrderDetails from '@/components/profile/OrderDetails.vue'
import WorkerHeader from "@/components/WorkerHeader.vue";

const API_HOST = 'http://localhost:8080'
const ORDERS_ENDPOINT = '/api/admin/order'

export default {
  components: {WorkerHeader, OrderDetails },
  data() {
    return {
      ordersByStatus: {},
      showDetails: false,
      selectedOrder: null,
      refreshIntervalId: null
    }
  },
  methods: {
    async fetchOrders() {
      try {
        const token = localStorage.getItem('jwt_token')
        const res = await axios.get(`${API_HOST}${ORDERS_ENDPOINT}/active`, {
          headers: { Authorization: `Bearer ${token}` }
        })
        this.organizeOrders(res.data)
      } catch (e) {
        alert('Ошибка при загрузке заказов')
      }
    },
    organizeOrders(orders) {
      const statuses = ['Приняли в работу', 'Готовим', 'Собираем', 'У курьера']
      this.ordersByStatus = {}
      statuses.forEach(status => {
        this.ordersByStatus[status] = orders.filter(order => order.status === status)
      })
    },
    showOrderDetails(order) {
      this.selectedOrder = order
      this.selectedOrder.customerFormattedAddress =
          order.address.city + ", "
          + order.address.street + ", "
          + order.address.building
          + (order.address?.entrance ? ', подъезд ' + order.address?.entrance : '')
          + (order.address?.floor ? ', этаж ' + order.address?.floor : '')
          + (order.address?.apartments ? ', кв. ' + order.address?.apartments : '')
      this.showDetails = true
    },
    async nextStatus(order) {
      try {
        const token = localStorage.getItem('jwt_token')
        await axios.put(`${API_HOST}${ORDERS_ENDPOINT}/push/${order.id}`, {}, {
          headers: { Authorization: `Bearer ${token}` }
        })

        await this.fetchOrders()
      } catch (e) {
        alert('Ошибка при изменении статуса заказа')
        console.error(e)
      }
    },
    handleCloseDetails() {
      this.showDetails = false
      this.selectedOrder = null
    },
    handleUpdate() {
      this.fetchOrders()
    }
  },
  mounted() {
    this.fetchOrders()
    this.refreshIntervalId = setInterval(this.fetchOrders, 5000)
  },
  unmounted() {
    if (this.refreshIntervalId) {
      clearInterval(this.refreshIntervalId)
    }
  }
}
</script>

<template>
  <WorkerHeader />
  <div class="p-4 bg-[#f3fdf5]">
    <h1 class="text-2xl font-bold mb-6 text-green-700">Управление заказами ресторана</h1>
    <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-6 p-6">

      <div v-for="(orders, status) in ordersByStatus" :key="status" class="mb-8">
        <h2 class="text-xl font-semibold mb-4 text-gray-800">{{ status }}</h2>

        <div v-if="orders.length === 0" class="text-gray-400 italic">Нет заказов в этом статусе</div>

        <div v-else class="grid gap-4">
          <div
              v-for="order in orders"
              :key="order.id"
              class="bg-white p-4 rounded-lg shadow-md border text-sm space-y-2">

            <p><strong>ID заказа:</strong> {{ order.id }}</p>
            <p><strong>Сумма:</strong> {{ order.cost }} ₽</p>
            <p><strong>Адрес:</strong> {{ order.address.city }}, {{ order.address.street }} {{ order.address.building }}</p>
            <p><strong>Оплата:</strong> {{ order.paymentType }}</p>
            <p><strong>Email клиента:</strong> {{ order.clientEmail }}</p>

            <div class="flex justify-between mt-4">
              <button
                  class="bg-yellow-400 hover:bg-yellow-500 text-gray-900 px-3 py-1 rounded"
                  @click="showOrderDetails(order)">
                Посмотреть детали
              </button>

              <button
                  class="bg-blue-500 hover:bg-blue-600 text-white px-3 py-1 rounded"
                  @click="nextStatus(order)">
                В следующий статус
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <OrderDetails
        v-if="showDetails"
        :order="selectedOrder"
        cancel-endpoint-prefix="/api/admin/order/cancel"
        update-endpoint-prefix="/api/admin/order/update"
        :is-admin-mode="true"
        @close="handleCloseDetails"
        @updated="handleUpdate"
        @cancelled="handleUpdate"
    />
  </div>
</template>

