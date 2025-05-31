<template>
  <div class="min-h-screen bg-green-50 text-gray-800 flex flex-col">
    <!-- Header -->
    <header class="bg-white shadow p-4 flex justify-between items-center sticky top-0 z-30">
      <div class="text-2xl font-bold text-green-700">Yummy!</div>
      <nav class="space-x-4">
        <RouterLink to="/menu" class="menu-btn">Меню</RouterLink>
        <RouterLink to="/profile" class="menu-btn">Профиль</RouterLink>
        <RouterLink to="/cart" class="menu-btn active">Корзина</RouterLink>
        <button @click="logout" class="menu-btn bg-red-500 text-white hover:bg-red-600">Выход
        </button>
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
                >−
                </button>
                <span class="w-6 text-center">{{ item.quantity }}</span>
                <button
                    @click="changeQuantity(item, item.quantity + 1)"
                    class="w-8 h-8 bg-gray-200 rounded hover:bg-gray-300 flex items-center justify-center text-lg"
                >+
                </button>
              </div>
            </div>
          </div>
        </div>

        <!-- Список адресов -->
        <div class="p-6 border-t">
          <h3 class="text-xl font-semibold mb-3">Выберите адрес доставки</h3>
          <div v-if="addresses.length === 0" class="text-gray-500 mb-4">
            Нет адресов. Добавьте новый адрес.
          </div>
          <div v-else class="space-y-2 mb-4">
            <label
                v-for="addr in addresses"
                :key="addr.id"
                class="flex items-center space-x-3 cursor-pointer"
            >
              <input
                  type="radio"
                  name="address"
                  :value="addr.id"
                  v-model="selectedAddressId"
                  class="form-radio"
              />
              <div>
                <div>
                  {{ addr.city }}, {{ addr.street }}, д. {{ addr.building }}
                  <span v-if="addr.entrance">, подъезд {{ addr.entrance }}</span>
                  <span v-if="addr.floor">, этаж {{ addr.floor }}</span>
                  <span v-if="addr.apartment">, кв. {{ addr.apartment }}</span>
                </div>
                <div v-if="addr.comment" class="text-gray-600 text-sm italic">
                  {{ addr.comment }}
                </div>
              </div>
            </label>
          </div>
          <button
              @click="showAddAddressModal = true"
              class="bg-yellow-400 hover:bg-yellow-500 text-gray-900 px-4 py-2 rounded"
          >
            Добавить адрес
          </button>
        </div>

        <!-- Нижняя панель с оплатой и итогом -->
        <div
            class="border-t p-6 flex flex-col sm:flex-row sm:items-center sm:justify-between space-y-6 sm:space-y-0">
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
              :disabled="cart.length === 0 || !selectedAddressId"
              class="bg-green-600 text-white px-6 py-2 rounded hover:bg-green-700 disabled:opacity-50 disabled:cursor-not-allowed transition"
          >
            Оформить заказ
          </button>
        </div>
      </div>
    </div>

    <!-- Модальное окно добавления адреса -->
    <AddAddressModal
        :show="showAddAddressModal"
        @close="showAddAddressModal = false"
        @address-added="handleAddressAdded"
    />
  </div>
</template>

<script>
import AddAddressModal from '@/components/addresses/AddAddressModal.vue';

export default {
  components: {
    AddAddressModal,
  },
  data() {
    return {
      cart: [],
      paymentMethod: 'CASH',
      addresses: [],
      selectedAddressId: null,
      showAddAddressModal: false,
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
        if (target) {
          target.quantity = newQty;
        }
      }
      this.saveCart();
    },
    async fetchAddresses() {
      try {
        const token = localStorage.getItem('jwt_token');
        if (!token) {
          throw new Error('Неавторизован');
        }

        const response = await fetch(`http://localhost:8080/api/address`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        if (!response.ok) {
          throw new Error('Ошибка при загрузке адресов');
        }

        this.addresses = await response.json();

        if (!this.addresses.find(addr => addr.id === this.selectedAddressId)) {
          this.selectedAddressId = null;
        }
      } catch (e) {
        alert(e.message || 'Ошибка при загрузке адресов');
        this.addresses = [];
      }
    },
    handleAddressAdded() {
      this.fetchAddresses();
    },
    async checkout() {
      try {
        if (!this.selectedAddressId) {
          alert('Выберите адрес доставки');
          return;
        }
        const token = localStorage.getItem('jwt_token');
        if (!token) {
          throw new Error('Неавторизован');
        }

        const orderPayload = {
          customerAddressId: this.selectedAddressId,
          paymentType: this.paymentMethod,
          menuItems: this.cart.map(({id, quantity}) => ({
            id,
            amount: quantity,
          })),
        };

        const response = await fetch('http://localhost:8080/api/order/create', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify(orderPayload),
        });
        if (!response.ok) {
          throw new Error('Ошибка оформления заказа');
        }

        alert('Заказ успешно оформлен!');
        this.cart = [];
        this.saveCart();
        this.$router.push('/profile');
      } catch (e) {
        alert(e.message || 'Ошибка оформления заказа');
      }
    },
    logout() {
      localStorage.removeItem('jwt_token');
      this.$router.push('/login');
    },
    parseJwt(token) {
      try {
        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(
            atob(base64)
            .split('')
            .map(function (c) {
              return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
            })
            .join('')
        );
        return JSON.parse(jsonPayload);
      } catch {
        return {};
      }
    },
  },
  mounted() {
    this.loadCart();
    this.fetchAddresses();
  },
};
</script>


<style scoped>
.menu-btn {
  padding: 0.5rem 1rem;
  font-weight: 600;
  color: #2d6a4f;
  border-radius: 0.375rem;
  transition: background-color 0.3s;
}

.menu-btn:hover {
  background-color: #95d5b2;
  color: #1b4332;
}

.menu-btn.active {
  background-color: #52b788;
  color: white;
}

.custom-radio-label input:checked + span {
  background-color: #f6ad55; /* желтый */
  color: white;
}

.custom-radio-label span {
  user-select: none;
}
</style>