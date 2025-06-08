<template>
  <Header />
  <div class="min-h-screen bg-green-50 text-gray-800 flex flex-col">

    <!-- Контейнер с негативным пространством по бокам -->
    <div class="flex-grow flex justify-center py-8 px-4 sm:px-8 lg:px-24 xl:px-48">
      <div
          class="bg-white rounded-lg shadow-lg w-full max-w-4xl flex flex-col"
          style="min-height: 60vh;"
      >
        <!-- Список товаров -->
        <h2 class="text-2xl font-semibold mb-4">Ваша корзина</h2>
        <div class="flex-grow overflow-y-auto p-6 space-y-4">
          <CartList :cart="cart" :can-edit="true" @update-cart="saveCart" />
        </div>

        <!-- Список адресов -->
        <AddressList
            :addresses="addresses"
            v-model="selectedAddressId"
            @add-address="handleAddressAdded"
            @edit-address="editAddress"
            @address-deleted="fetchAddresses"/>

        <!-- Нижняя панель с оплатой и итогом -->
        <div
            class="border-t p-6 flex flex-col sm:flex-row sm:items-center sm:justify-between space-y-6 sm:space-y-0">
          <div class="flex space-x-6">
            <label class="custom-radio-label">
              <input
                  type="radio"
                  value="CASH"
                  v-model="paymentMethod"
                  class="hidden"
              />
              <span
                  :class="{'bg-yellow-400 text-white': paymentMethod === 'CASH', 'bg-gray-200': paymentMethod !== 'CASH'}"
                  class="cursor-pointer px-4 py-2 rounded select-none"
              >
                Наличными
              </span>
            </label>
            <label class="custom-radio-label">
              <input
                  type="radio"
                  value="CARD"
                  v-model="paymentMethod"
                  class="hidden"
              />
              <span
                  :class="{'bg-yellow-400 text-white': paymentMethod === 'CARD', 'bg-gray-200': paymentMethod !== 'CARD'}"
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

  </div>
</template>

<script>
import Header from "@/components/Header.vue";
import CartList from "@/components/cart/CartList.vue";
import AddressList from "@/components/addresses/AddressList.vue";

export default {
  components: {
    Header,
    CartList,
    AddressList
  },
  data() {
    return {
      cart: [],
      paymentMethod: 'CASH',
      addresses: [],
      selectedAddressId: null,
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
    saveCart(updatedCart) {
      this.cart = updatedCart
      localStorage.setItem('cart', JSON.stringify(this.cart));
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
        this.saveCart(this.cart);
        this.$router.push('/profile');
      } catch (e) {
        alert(e.message || 'Ошибка оформления заказа');
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