<!-- components/cart/CartSummary.vue -->
<template>
  <div class="border-t p-6 flex flex-col sm:flex-row sm:items-center sm:justify-between space-y-6 sm:space-y-0">
    <div class="flex space-x-6">
      <label class="custom-radio-label">
        <input
            type="radio"
            value="cash"
            v-model="localPaymentMethod"
            class="hidden"
        />
        <span
            :class="{
            'bg-yellow-400 text-white': localPaymentMethod === 'cash',
            'bg-gray-200': localPaymentMethod !== 'cash'
          }"
            class="cursor-pointer px-4 py-2 rounded select-none"
        >
          Наличными
        </span>
      </label>
      <label class="custom-radio-label">
        <input
            type="radio"
            value="card"
            v-model="localPaymentMethod"
            class="hidden"
        />
        <span
            :class="{
            'bg-yellow-400 text-white': localPaymentMethod === 'card',
            'bg-gray-200': localPaymentMethod !== 'card'
          }"
            class="cursor-pointer px-4 py-2 rounded select-none"
        >
          Банковской картой
        </span>
      </label>
    </div>

    <div class="text-lg font-semibold text-green-700">
      К оплате: {{ totalPrice.toLocaleString() }} ₽
    </div>

    <button
        @click="$emit('checkout')"
        :disabled="isCheckoutDisabled"
        class="bg-green-600 text-white px-6 py-2 rounded hover:bg-green-700 disabled:opacity-50 disabled:cursor-not-allowed transition"
    >
      Оформить заказ
    </button>
  </div>
</template>

<script>
export default {
  props: {
    totalPrice: {
      type: Number,
      required: true,
    },
    paymentMethod: {
      type: String,
      default: 'cash',
    },
    isCheckoutDisabled: {
      type: Boolean,
      default: false,
    },
  },
  emits: ['update:paymentMethod', 'checkout'],
  computed: {
    localPaymentMethod: {
      get() {
        return this.paymentMethod;
      },
      set(value) {
        this.$emit('update:paymentMethod', value);
      },
    },
  },
};
</script>

<style scoped>
.custom-radio-label input:checked + span {
  background-color: #f6ad55;
  color: white;
}
.custom-radio-label span {
  user-select: none;
}
</style>
