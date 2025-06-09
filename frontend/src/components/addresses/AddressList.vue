<template>
  <div>
    <h3 class="text-xl font-semibold mb-3">Выберите адрес доставки</h3>

    <div v-if="addresses.length === 0" class="text-gray-500 mb-4">
      Нет адресов. Добавьте новый адрес.
    </div>

    <div v-else class="space-y-2 mb-4">
      <div
          v-for="addr in addresses"
          :key="addr.id"
          class="flex items-start justify-between p-2 border rounded hover:bg-gray-50"
      >
        <label class="flex items-start space-x-3 cursor-pointer w-full">
          <input
              type="radio"
              name="address"
              :value="addr.id"
              v-model="localSelectedId"
              class="form-radio mt-1"
          />
          <div class="text-sm">
            <div>
              {{ addr.city }}, {{ addr.street }}, д. {{ addr.building }}
              <span v-if="addr.entrance">, подъезд {{ addr.entrance }}</span>
              <span v-if="addr.floor">, этаж {{ addr.floor }}</span>
              <span v-if="addr.apartment">, кв. {{ addr.apartment }}</span>
            </div>
            <div v-if="addr.comment" class="text-gray-600 italic">
              {{ addr.comment }}
            </div>
          </div>
        </label>
        <div class="flex items-center space-x-2 ml-2">
          <button @click="deleteAddress(addr.id)" class="text-red-500 hover:text-red-700">
            🗑️
          </button>
        </div>
      </div>
    </div>

    <button
        @click="showAddAddressModal = true"
        class="bg-yellow-400 hover:bg-yellow-500 text-gray-900 px-4 py-2 rounded"
    >
      Добавить адрес
    </button>
  </div>

  <!-- Модальное окно добавления адреса -->
  <AddAddressModal
      :show="showAddAddressModal"
      @close="showAddAddressModal = false"
      @address-added="$emit('add-address')"
  />
</template>

<script>
import AddAddressModal from "@/components/addresses/AddAddressModal.vue";

export default {
  name: 'AddressList',
  components: {AddAddressModal},
  props: {
    addresses: {
      type: Array,
      required: true,
    },
    modelValue: {
      type: [String, Number, null],
      default: null,
    },
  },
  emits: ['update:modelValue', 'add-address', 'address-deleted'],
  computed: {
    localSelectedId: {
      get() {
        return this.modelValue;
      },
      set(value) {
        this.$emit('update:modelValue', value);
      },
    },
  },
  data() {
    return {
      showAddAddressModal: false,
    };
  },
  methods: {
    async deleteAddress(id) {
      if (!confirm('Удалить адрес?')) return;
      try {
        const token = localStorage.getItem('jwt_token');
        const response = await fetch(`http://localhost:8080/api/address/detach/${id}`, {
          method: 'PUT',
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        if (!response.ok) throw new Error('Ошибка удаления');
        this.$emit('address-deleted');
      } catch (e) {
        alert(e.message || 'Ошибка при удалении');
      }
    },
  },
};
</script>
