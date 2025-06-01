<script>
import CartList from '../cart/CartList.vue'
import { menuItems } from '@/data/menu_items.js'

export default {
  components: {
    CartList
  },
  props: {
    order: {
      type: Object,
      required: true
    }
  },
  emits: ['close'],
  computed: {
    cartItems() {
      return this.order.menuItems.map(item => {
        const product = menuItems.find(m => m.id === item.id)
        return {
          id: item.id,
          name: product?.name || `Блюдо #${item.id}`,
          price: product?.price || 0,
          imageUrl: product?.imageUrl || '/images/default.jpg',
          quantity: item.amount
        }
      })
    }
  },
  mounted() {
    document.body.style.overflow = 'hidden'; // блокирует прокрутку фона
  },
  unmounted() {
    document.body.style.overflow = ''; // возвращает прокрутку
  }
}
</script>

<template>
  <div class="fixed inset-0 z-50 pointer-events-none overflow-y-auto">
    <!-- Затемнённый фон -->
    <div
        class="absolute inset-0 bg-black/30 backdrop-blur-sm pointer-events-auto"
        @click="$emit('close')"
    ></div>

    <!-- Прокручиваемая модалка -->
    <div
        class="relative z-10 mx-auto my-10 w-full max-w-md pointer-events-auto"
    >
      <div class="bg-white rounded-lg p-6 shadow-lg relative max-h-[90vh] overflow-y-auto">
        <button @click="$emit('close')" class="absolute top-2 right-2 text-gray-600">✖</button>

        <h2 class="text-xl font-bold mb-4 text-green-700">Состав заказа #{{ order.id }}</h2>

        <p><strong>Адрес клиента:</strong> {{ order.customerFormattedAddress }}</p>
        <p><strong>Адрес ресторана:</strong> {{ order.restaurantFormattedAddress }}</p>
        <p><strong>Оплата:</strong> {{ order.paymentType }}</p>
        <p><strong>Сумма:</strong> {{ order.cost }} ₽</p>

        <div class="mt-4">
          <CartList :cart="cartItems" :canEdit="false" />
        </div>
      </div>
    </div>
  </div>
</template>

