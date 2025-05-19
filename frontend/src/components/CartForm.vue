<template>
  <div class="min-h-screen bg-green-50 text-gray-800 flex flex-col">
    <!-- Header -->
    <header class="bg-white shadow p-4 flex justify-between items-center sticky top-0 z-30">
      <div class="text-2xl font-bold text-green-700">Yummy!</div>
      <nav class="space-x-4">
        <RouterLink to="/menu" class="menu-btn">Меню</RouterLink>
        <RouterLink to="/profile" class="menu-btn">Профиль</RouterLink>
        <RouterLink to="/cart" class="menu-btn active">Корзина</RouterLink>
        <button @click="logout" class="menu-btn bg-red-500 text-white hover:bg-red-600">Выход</button>
      </nav>
    </header>

    <!-- Контейнер с негативным пространством по бокам -->
    <div class="flex-grow flex justify-center py-8 px-4 sm:px-8 lg:px-24 xl:px-48">
      <div
          class="bg-white rounded-lg shadow-lg w-full max-w-4xl flex flex-col"
          style="min-height: 60vh;"
      >
        <!-- Список товаров -->
        <div class="flex-grow overflow-y-auto p-6 space-y-4">
          <h2 class="text-2xl font-semibold mb-4">Ваша корзина</h2>
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
                    class="w-8 h-8 bg-gray-200 rounded hover:bg-gray-300 flex items-center justify-center text-lg"
                >−</button>
                <span class="w-6 text-center">{{ item.quantity }}</span>
                <button
                    @click="changeQuantity(item, item.quantity + 1)"
                    class="w-8 h-8 bg-gray-200 rounded hover:bg-gray-300 flex items-center justify-center text-lg"
                >+</button>
              </div>
            </div>
          </div>
        </div>

        <!-- Нижняя панель с оплатой и итогом -->
        <div class="border-t p-6 flex flex-col sm:flex-row sm:items-center sm:justify-between space-y-6 sm:space-y-0">
          <div class="flex space-x-6">
            <label class="custom-radio-label">
              <input
                  type="radio"
                  value="cash"
                  v-model="paymentMethod"
                  class="hidden"
              />
              <span
                  :class="{'bg-yellow-400 text-white': paymentMethod === 'cash', 'bg-gray-200': paymentMethod !== 'cash'}"
                  class="cursor-pointer px-4 py-2 rounded select-none"
              >
                Наличными
              </span>
            </label>
            <label class="custom-radio-label">
              <input
                  type="radio"
                  value="card"
                  v-model="paymentMethod"
                  class="hidden"
              />
              <span
                  :class="{'bg-yellow-400 text-white': paymentMethod === 'card', 'bg-gray-200': paymentMethod !== 'card'}"
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
              @click="checkout"
              :disabled="cart.length === 0"
              class="bg-green-600 text-white px-6 py-2 rounded hover:bg-green-700 disabled:opacity-50 disabled:cursor-not-allowed transition"
          >
            Оформить заказ
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      cart: [],
      paymentMethod: 'cash',
    };
  },
  computed: {
    totalPrice() {
      return this.cart.reduce(
          (sum, item) => sum + item.price * item.quantity,
          0
      );
    },
  },
  methods: {
    loadCart() {
      const saved = localStorage.getItem('cart');
      this.cart = saved ? JSON.parse(saved) : [];
    },
    saveCart() {
      localStorage.setItem('cart', JSON.stringify(this.cart));
    },
    changeQuantity(item, newQty) {
      if (newQty <= 0) {
        this.cart = this.cart.filter((i) => i.id !== item.id);
      } else {
        const target = this.cart.find((i) => i.id === item.id);
        if (target) target.quantity = newQty;
      }
      this.saveCart();
    },
    checkout() {
      const order = this.cart.map(({ id, quantity }) => ({ id, quantity }));
      alert(
          `Заказ оформлен!\nОплата: ${this.paymentMethod}\nТовары:\n${JSON.stringify(
              order,
              null,
              2
          )}`
      );
      this.cart = [];
      this.saveCart();
      this.$router.push('/menu');
    },
    logout() {
      localStorage.removeItem('jwt_token');
      this.$router.push('/login');
    },
  },
  mounted() {
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
  text-decoration: none;
  color: #1f2937;
  font-weight: 500;
}

.menu-btn:hover {
  background-color: #e5e7eb;
}

.menu-btn.active {
  background-color: #22c55e; /* green-500 */
  color: white;
}

/* Кастомные стили для радио переключателей */
.custom-radio-label span {
  transition: background-color 0.3s, color 0.3s;
  user-select: none;
}
</style>
