<template>
  <div>
    <div v-if="cart.length === 0" class="text-center text-gray-500 py-20">
      Нет товаров в корзине
    </div>
    <div v-else>
      <div
          v-for="item in cart"
          :key="item.id"
          class="flex items-center border-b pb-4"
      >
        <div
            class="w-20 h-20 rounded mr-4 flex-shrink-0 bg-gray-100 flex items-center justify-center overflow-hidden"
        >
          <img
              :src="item.imageUrl"
              :alt="item.name"
              class="max-w-full max-h-full object-contain"
          />
        </div>
        <div class="flex-grow">
          <h3 class="font-semibold">{{ item.name }}</h3>
          <p class="text-green-700 font-semibold">
            {{ (item.price * item.quantity).toLocaleString() }} ₽
          </p>
        </div>
        <div class="flex items-center space-x-2">
          <button
              @click="changeQuantity(item, item.quantity - 1)"
              :disabled="!canEdit"
              class="w-8 h-8 bg-gray-200 rounded hover:bg-gray-300 flex items-center justify-center text-lg
disabled:bg-gray-100 disabled:text-gray-400 disabled:cursor-not-allowed"
          >−</button>
          <span class="w-6 text-center">{{ item.quantity }}</span>
          <button
              @click="changeQuantity(item, item.quantity + 1)"
              :disabled="!canEdit"
              class="w-8 h-8 bg-gray-200 rounded hover:bg-gray-300 flex items-center justify-center text-lg
disabled:bg-gray-100 disabled:text-gray-400 disabled:cursor-not-allowed"
          >+</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "CartList",
  props: {
    cart: {
      type: Array,
      required: true,
    },
    canEdit: {
      type: Boolean,
      default: true
    }
  },
  emits: ["update-cart"],
  methods: {
    changeQuantity(item, newQty) {
      const updatedCart = [...this.cart];
      const targetIndex = updatedCart.findIndex((i) => i.id === item.id);
      if (targetIndex !== -1) {
        if (newQty <= 0) {
          updatedCart.splice(targetIndex, 1);
        } else {
          updatedCart[targetIndex].quantity = newQty;
        }
        this.$emit("update-cart", updatedCart);
      }
    },
  },
};
</script>
