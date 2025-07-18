<script>
import wrappingApi from '@/axios.js'
import OrderDetails from '@/components/profile/OrderDetails.vue'
import WorkerHeader from "@/components/WorkerHeader.vue";
import {formatDateTime} from "../utils/utils.js";

const ORDERS_ENDPOINT = '/courier/order'
const COURIER_ENDPOINT = '/courier'

export default {
  components: {WorkerHeader, OrderDetails},
  computed: {
    sortedOrders() {
      return [...this.orders].sort((a, b) => {
        if (a.deliverySequence == null) return 1;
        if (b.deliverySequence == null) return -1;
        return a.deliverySequence - b.deliverySequence;
      });
    }
  },
  data() {
    return {
      orders: [],
      showDetails: false,
      selectedOrder: null,
      refreshIntervalId: null,
      courierInfo: null
    }
  },
  methods: {
    formatDateTime,
    async setCourierStatus(status) {
      try {
        await wrappingApi.put(
            `${COURIER_ENDPOINT}/status/${status}`,
            {})
        await this.getCourierInfo()
      } catch (e) {
        alert('Ошибка при обновлении статуса курьера')
      }
    },
    async getCourierInfo() {
      try {
        const res = await wrappingApi.get(`${COURIER_ENDPOINT}/info`)
        this.courierInfo = res.data
      } catch (e) {
        alert('Ошибка при загрузке информации о курьере')
      }
    },
    async fetchOrders() {
      try {
        const res = await wrappingApi.get(`${ORDERS_ENDPOINT}/active`)
        this.orders = res.data
      } catch (e) {
      }
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
        await wrappingApi.put(`${ORDERS_ENDPOINT}/push/${order.id}`)
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
    this.getCourierInfo()
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
    <h1 class="text-2xl font-bold mb-6 text-green-700 text-center">Курьер</h1>

    <div class="flex justify-center mb-6">
      <div v-if="courierInfo" class="bg-white p-6 rounded shadow space-y-2 text-gray-800 w-full max-w-md">
        <p><strong class="text-green-700">Имя:</strong> {{ courierInfo.name }}</p>
        <p><strong class="text-green-700">Фамилия:</strong> {{ courierInfo.surname }}</p>
        <p><strong class="text-green-700">Телефон:</strong> {{ courierInfo.phone }}</p>
        <p><strong class="text-green-700">Статус:</strong> {{ courierInfo.status }}</p>

        <div class="mt-4">
          <p class="font-semibold text-gray-700 mb-2 text-center">Изменить статус:</p>
          <div class="flex justify-center space-x-4">
            <button
                class="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded shadow w-1/2"
                @click="setCourierStatus('READY')"
                :disabled="courierInfo.status === 'READY'"
            >
              Свободен
            </button>
            <button
                class="bg-red-500 hover:bg-red-600 text-white px-4 py-2 rounded shadow w-1/2"
                @click="setCourierStatus('BUSY')"
                :disabled="courierInfo.status === 'BUSY'"
            >
              Занят
            </button>
          </div>
        </div>
      </div>
    </div>

    <h1 class="text-2xl font-bold mb-6 text-green-700 text-center">Заказы для доставки</h1>

    <div v-if="orders.length === 0" class="text-gray-500 mb-4 text-center">
      Пока что нет назначенных заказов.
    </div>

    <div v-else class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-6 p-6">
      <div
          v-for="order in sortedOrders"
          :key="order.id"
          class="bg-white p-4 rounded-lg shadow-md border text-sm space-y-2">

        <p><strong>ID заказа:</strong> {{ order.id }}</p>
        <p><strong>Сумма:</strong> {{ order.cost }} ₽</p>
        <p><strong>Статус:</strong> {{ order.status }}</p>
        <p><strong>Адрес:</strong> {{ order.address.city }}, {{ order.address.street }} {{ order.address.building }}</p>
        <p><strong>Оплата:</strong> {{ order.paymentType }}</p>
        <p><strong>Email клиента:</strong> {{ order.clientEmail }}</p>
        <p><strong>Создан:</strong> {{ formatDateTime(order.orderCreatedAt) }}</p>
        <p v-if="order.deliverySequence"><strong>Рекомендованный порядок доставки:</strong> {{ order.deliverySequence }}</p>

        <div class="flex justify-between mt-4">
          <button
              class="bg-yellow-400 hover:bg-yellow-500 text-gray-900 px-3 py-1 rounded w-1/2"
              @click="showOrderDetails(order)">
            Посмотреть детали
          </button>

          <button
              class="bg-blue-500 hover:bg-blue-600 text-white px-3 py-1 rounded w-1/2"
              @click="nextStatus(order)">
            Доставлено
          </button>
        </div>
      </div>
    </div>

    <OrderDetails
        v-if="showDetails"
        :order="selectedOrder"
        cancel-endpoint-prefix="/courier/order/cancel"
        update-endpoint-prefix="/courier/order/update"
        :is-admin-mode="false"
        @close="handleCloseDetails"
        @updated="handleUpdate"
        @cancelled="handleUpdate"
    />
  </div>
</template>