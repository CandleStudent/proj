<script>
import CartList from '../cart/CartList.vue'
import {menuItems} from '@/data/menu_items.js'
import axios from "axios";
import AddDishes from "@/components/profile/AddDishes.vue";

const host = 'http://localhost:8080';

export default {
  components: {
    AddDishes,
    CartList
  },
  data() {
    return {
      editMode: false,
      editedItems: [],
      showAddDishes: false,
      cartForAdd: []
    }
  },
  props: {
    order: {
      type: Object,
      required: true
    }
  },
  emits: ['close', 'cancelled'],
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
    },
    calculatedTotalCost() {
      return this.editMode
          ? this.editedItems.reduce((sum, item) => {
            const menuItem = menuItems.find(m => m.id === item.id)
            return sum + (menuItem?.price || 0) * item.quantity
          }, 0)
          : this.order.cost
    },
    canBeCancelled() {
      const cancellableStatuses = ['Приняли в работу', 'Готовим', 'Собираем']
      return cancellableStatuses.includes(this.order.status)
    },
    canBeEdited() {
      const editableStatuses = ['Приняли в работу']
      return editableStatuses.includes(this.order.status)
    }
  },
  methods: {
    async cancelOrder() {
      try {
        const token = localStorage.getItem('jwt_token')
        await axios.delete(`${host}/api/order/cancel/${this.order.id}`, {
          headers: { Authorization: `Bearer ${token}` }
        })
        alert('Заказ отменён')
        this.$emit('cancelled')
        this.$emit('close')
      } catch (e) {
        alert(e.response.data || 'Ошибка при отмене заказа')
        this.$emit('updated')
        this.$emit('close')
      }
    },
    startEditing() {
      this.editMode = true
      this.editedItems = this.cartItems.map(item => ({ ...item }))
    },
    cancelEditing() {
      this.editMode = false
      this.editedItems = []
    },
    async saveChanges() {
      try {
        const token = localStorage.getItem('jwt_token')
        const payload = {
          menuItems: this.editedItems.map(item => ({
            id: item.id,
            amount: item.quantity
          }))
        }

        await axios.put(`${host}/api/order/update/${this.order.id}`, payload, {
          headers: { Authorization: `Bearer ${token}` }
        })

        alert('Заказ обновлён')
        this.$emit('updated')
        this.$emit('close')
      } catch (e) {
        alert(e.response.data || 'Ошибка при обновлении заказа')
        this.$emit('updated')
        this.$emit('close')
      }
    },
    updateEditedCart(newCart) {
      this.editedItems = newCart
    },
    addItem() {
      this.cartForAdd = [...this.editedItems];
      this.showAddDishes = true
    },
    handleAddDishesCancel() {
      this.showAddDishes = false;
    },

    handleAddDishesSave(newCart) {
      this.editedItems = newCart;
      this.showAddDishes = false;
    }
  },
  mounted() {
    document.body.style.overflow = 'hidden';
  },
  unmounted() {
    document.body.style.overflow = '';
  }
}
</script>


<template>
  <div class="fixed inset-0 z-50 pointer-events-none overflow-y-auto">
    <div
        class="absolute inset-0 bg-black/30 backdrop-blur-sm pointer-events-auto"
        @click="$emit('close')"
    ></div>

    <div class="relative z-10 mx-auto my-10 w-full max-w-md pointer-events-auto">
      <div class="bg-white rounded-lg p-6 shadow-lg relative max-h-[90vh] overflow-y-auto">
        <button @click="$emit('close')" class="absolute top-2 right-2 text-gray-600">✖</button>

        <h2 class="text-xl font-bold mb-4 text-green-700">Состав заказа #{{ order.id }}</h2>

        <p><strong>Статус:</strong> {{ order.status }}</p>
        <p><strong>Адрес клиента:</strong> {{ order.customerFormattedAddress }}</p>
        <p><strong>Адрес ресторана:</strong> {{ order.restaurantFormattedAddress }}</p>
        <p><strong>Оплата:</strong> {{ order.paymentType }}</p>
        <p><strong>Сумма:</strong> {{ calculatedTotalCost }} ₽</p>

        <div class="mt-4">
          <CartList
              :cart="editMode ? editedItems : cartItems"
              :canEdit="editMode"
              @update-cart="updateEditedCart"
          />
        </div>

        <div class="flex space-x-4 mb-4 mt-8 justify-between">
          <template v-if="!editMode">
            <button
                @click="cancelOrder"
                :class="[
                  'px-4 py-2 rounded',
                  canBeCancelled
                    ? 'bg-red-500 hover:bg-red-600 text-white'
                    : 'bg-gray-300 text-gray-500 cursor-not-allowed'
                ]"
                :disabled="!canBeCancelled">
              Отменить заказ
            </button>

            <button
                @click="startEditing"
                :class="[
                  'px-4 py-2 rounded',
                  canBeEdited
                    ? 'bg-yellow-400 hover:bg-yellow-500 text-gray-900'
                    : 'bg-gray-300 text-gray-500 cursor-not-allowed'
                ]"
                :disabled="!canBeEdited">
              Изменить заказ
            </button>
          </template>

          <template v-else>
            <button
                class="bg-gray-300 hover:bg-gray-400 text-gray-900 px-4 py-2 rounded"
                @click="cancelEditing">
              Отменить
            </button>

            <button
                class="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded"
                @click="saveChanges">
              Сохранить
            </button>

            <button
                class="bg-green-500 hover:bg-green-600 text-white px-4 py-2 rounded"
                @click="addItem">
              Добавить блюдо
            </button>
          </template>
        </div>

      </div>
    </div>
  </div>

  <AddDishes
      v-if="showAddDishes"
      :initial-cart="this.cartForAdd"
      @close="showAddDishes = false"
      @cancelled="handleAddDishesCancel"
      @saved="handleAddDishesSave"
  />
</template>
