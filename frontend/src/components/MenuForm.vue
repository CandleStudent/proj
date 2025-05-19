<template>
  <div class="min-h-screen bg-green-50 text-gray-800">
    <!-- Header -->
    <header class="bg-white shadow p-4 flex justify-between items-center sticky top-0 z-30">
      <div class="text-2xl font-bold text-green-700">Yummy!</div>
      <nav class="space-x-4">
        <RouterLink to="/profile" class="menu-btn">Профиль</RouterLink>
        <RouterLink to="/menu" class="menu-btn">Меню</RouterLink>
        <RouterLink to="/cart" class="menu-btn">Корзина</RouterLink>
        <button @click="logout" class="menu-btn bg-red-500 text-white hover:bg-red-600">Выход</button>
      </nav>
    </header>

    <!-- Category Filter (fixed top under header) -->
    <div
        class="bg-green-100 sticky top-[64px] z-20 flex justify-center py-3 shadow-inner"
    >
      <button
          v-for="cat in categories"
          :key="cat"
          @click="selectedCategory = cat"
          :class="[
          'px-4 py-2 mx-1 rounded-full border transition-colors duration-300',
          selectedCategory === cat
            ? 'bg-green-500 text-white border-green-500'
            : 'bg-white text-green-700 border-green-300 hover:bg-green-200',
        ]"
      >
        {{ cat }}
      </button>
    </div>

    <!-- Products Grid -->
    <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6 p-6">
      <div
          v-for="dish in filteredDishes"
          :key="dish.id"
          class="bg-white shadow rounded-lg overflow-hidden flex flex-col"
          style="height: 420px;"
      >
        <div class="flex-shrink-0 h-48 overflow-hidden">
          <img
              :src="dish.imageUrl"
              :alt="dish.name"
              class="w-full h-full object-contain"
          />
        </div>
        <div class="p-4 flex flex-col flex-grow">
          <h3 class="font-bold text-lg mb-1">{{ dish.name }}</h3>
          <p class="text-sm text-gray-600 flex-grow">{{ dish.description }}</p>
          <p class="text-green-700 font-semibold mt-2">{{ dish.price }} ₽</p>
          <button
              @click="toggleCart(dish)"
              :class="cartIds.includes(dish.id)
              ? 'bg-red-500 hover:bg-red-600'
              : 'bg-green-500 hover:bg-green-600'"
              class="mt-3 w-full text-white py-2 rounded transition duration-300"
          >
            {{ cartIds.includes(dish.id) ? 'Удалить' : 'Добавить в корзину' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { menuItems } from '@/data/menu_items.js';

export default {
  data() {
    return {
      dishes: [],
      cart: [],
      selectedCategory: 'Все',
      categories: ['Все', 'Горячее', 'Напитки', 'Десерты'],
    };
  },
  computed: {
    filteredDishes() {
      return this.selectedCategory === 'Все'
          ? this.dishes
          : this.dishes.filter(d => d.category === this.selectedCategory);
    },
    cartIds() {
      return this.cart.map(item => item.id);
    },
  },
  methods: {
    toggleCart(dish) {
      const index = this.cart.findIndex(item => item.id === dish.id);
      if (index !== -1) {
        this.cart.splice(index, 1);
      } else {
        this.cart.push({
          id: dish.id,
          name: dish.name,
          price: dish.price,
          imageUrl: dish.imageUrl,
          quantity: 1,
        });
      }
      this.saveCart();
    },
    saveCart() {
      localStorage.setItem('cart', JSON.stringify(this.cart));
    },
    loadCart() {
      const saved = localStorage.getItem('cart');
      if (saved) {
        this.cart = JSON.parse(saved);
      }
    },
    logout() {
      localStorage.removeItem('jwt_token');
      this.$router.push('/login');
    },
  },
  mounted() {
    this.dishes = menuItems;
    this.loadCart();
  },
};
</script>

<style scoped>
.menu-btn {
  padding: 0.5rem 1rem;
  border-radius: 0.5rem;
  transition: background 0.3s;
  background-color: #f3f4f6;
}

.menu-btn:hover {
  background-color: #e5e7eb;
}
</style>
