<template>
  <div class="min-h-screen bg-green-50 text-gray-800">
    <!-- Header -->
    <header class="bg-white shadow p-4 flex justify-between items-center z-20 relative">
      <div class="text-2xl font-bold text-green-700">Yummy!</div>
      <nav class="space-x-4">
        <RouterLink to="/profile" class="menu-btn">Профиль</RouterLink>
        <RouterLink to="/menu" class="menu-btn">Меню</RouterLink>
        <RouterLink to="/cart" class="menu-btn">Корзина</RouterLink>
        <button @click="logout" class="menu-btn bg-red-500 text-white hover:bg-red-600">Выход</button>
      </nav>
    </header>

    <!-- Fixed Category Filter -->
    <div class="bg-green-100 sticky top-0 z-10 py-3 px-4 shadow-md flex flex-wrap justify-center gap-2">
      <button
          v-for="cat in allCategories"
          :key="cat"
          @click="changeCategory(cat)"
          :class="[
          'px-4 py-2 rounded-full border transition',
          selectedCategory === cat ? 'bg-green-500 text-white' : 'bg-white text-green-700'
        ]"
      >
        {{ cat }}
      </button>
    </div>

    <!-- Dishes Grouped by Category -->
    <div class="px-6 mt-6">
      <transition-group name="fade" tag="div">
        <div
            v-for="cat in categoriesToShow"
            :key="cat"
            class="mb-10"
        >
          <h2 class="text-2xl font-bold text-green-700 mb-4">{{ cat }}</h2>
          <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6">
            <div
                v-for="dish in dishesByCategory(cat)"
                :key="dish.id"
                class="bg-white shadow rounded-lg overflow-hidden flex flex-col"
            >
              <img :src="dish.imageUrl" alt="dish" class="w-full h-48 object-contain bg-white p-2" />
              <div class="p-4 flex flex-col flex-grow">
                <h3 class="font-bold text-lg mb-1">{{ dish.name }}</h3>
                <p class="text-sm text-gray-600 flex-grow">{{ dish.description }}</p>
                <p class="text-green-700 font-semibold mt-2">{{ dish.price }} ₽</p>
                <button
                    @click="toggleCart(dish.id)"
                    :class="[
                    'mt-3 w-full text-white py-2 rounded transition duration-300',
                    cart.has(dish.id) ? 'bg-red-500 hover:bg-red-600' : 'bg-green-500 hover:bg-green-600'
                  ]"
                >
                  {{ cart.has(dish.id) ? 'Удалить' : 'Добавить в корзину' }}
                </button>
              </div>
            </div>
          </div>
        </div>
      </transition-group>
    </div>
  </div>
</template>

<script>
import { menuItems } from '@/data/menu_items.js';

export default {
  data() {
    return {
      dishes: [],
      cart: new Set(),
      selectedCategory: 'Все',
    };
  },
  computed: {
    allCategories() {
      const cats = this.dishes.map(d => d.category);
      return ['Все', ...new Set(cats)];
    },
    categoriesToShow() {
      if (this.selectedCategory === 'Все') {
        return [...new Set(this.dishes.map(d => d.category))];
      }
      return [this.selectedCategory];
    },
  },
  methods: {
    dishesByCategory(category) {
      return this.dishes.filter(d => d.category === category);
    },
    toggleCart(dishId) {
      if (this.cart.has(dishId)) {
        this.cart.delete(dishId);
      } else {
        this.cart.add(dishId);
      }
      this.saveCartToLocalStorage();
    },
    changeCategory(cat) {
      this.selectedCategory = cat;
    },
    logout() {
      localStorage.removeItem('jwt_token');
      this.$router.push('/auth');
    },
    fetchDishes() {
      this.dishes = menuItems;
    },
    saveCartToLocalStorage() {
      localStorage.setItem('cart', JSON.stringify([...this.cart]));
    },
    loadCartFromLocalStorage() {
      const saved = localStorage.getItem('cart');
      if (saved) {
        this.cart = new Set(JSON.parse(saved));
      }
    },
  },
  mounted() {
    this.fetchDishes();
    this.loadCartFromLocalStorage();
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

/* Анимация появления/исчезновения категорий */
.fade-enter-active,
.fade-leave-active {
  transition: all 0.5s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
  transform: translateY(20px);
}
</style>
