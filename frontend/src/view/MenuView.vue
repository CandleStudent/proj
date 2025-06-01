<template>
  <Header />
  <div class="min-h-screen bg-green-50 text-gray-800">
    <ProductGrid
        :initialCart="cart"
        @update:cart="onCartUpdate"
    />
  </div>
</template>

<script>
import Header from "@/components/Header.vue";
import ProductGrid from "@/components/menu/ProductGrid.vue";

export default {
  components: { Header, ProductGrid },
  data() {
    return {
      cart: [],
    };
  },
  methods: {
    loadCart() {
      const saved = localStorage.getItem('cart');
      if (saved) {
        this.cart = JSON.parse(saved);
      }
    },
    onCartUpdate(updatedCart) {
      this.cart = updatedCart;
      localStorage.setItem('cart', JSON.stringify(this.cart));
    },
  },
  mounted() {
    this.loadCart();
  },
};
</script>
