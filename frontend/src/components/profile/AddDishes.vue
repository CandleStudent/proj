<script>
import ProductGrid from '@/components/menu/ProductGrid.vue';
import Header from "@/components/Header.vue";

export default {
  components: {Header, ProductGrid},
  props: {
    initialCart: {
      type: Array,
      default: () => [],
    },
  },
  data() {
    return {
      updatedCart: [...this.initialCart],
    };
  },
  methods: {
    handleCartUpdate(newCart) {
      this.updatedCart = newCart;
    },
    submitCart() {
      this.$emit('saved', this.updatedCart);
    },
    cancel() {
      this.$emit('cancelled');
    },
  },
};
</script>

<template>

  <div class="fixed inset-0 z-50 bg-black/30 backdrop-blur-sm flex items-center justify-center">
    <div class="bg-white rounded-lg shadow-xl w-full max-w-4xl max-h-[90vh] overflow-y-auto p-4 relative">
      <div class="flex justify-between items-center mb-4">
        <h2 class="text-2xl font-bold text-green-700">Добавление блюд</h2>
        <button @click="cancel" class="text-gray-600 text-xl">✖</button>
      </div>

      <ProductGrid
          :initialCart="initialCart"
          @update:cart="handleCartUpdate"
      />

      <div class="mt-6 flex justify-end gap-4">
        <button class="bg-gray-400 text-white px-4 py-2 rounded" @click="cancel">Отмена</button>
        <button class="bg-green-600 text-white px-4 py-2 rounded" @click="submitCart">Сохранить</button>
      </div>
    </div>
  </div>
</template>
