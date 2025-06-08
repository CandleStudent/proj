<template>
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
            v-model="selectedId"
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
        @click="$emit('add-address-click')"
        class="bg-yellow-400 hover:bg-yellow-500 text-gray-900 px-4 py-2 rounded"
    >
      Добавить адрес
    </button>
  </div>
</template>

<script>
export default {
  name: 'AddressSelector',
  props: {
    addresses: {
      type: Array,
      required: true,
    },
    selectedAddressId: {
      type: [Number, String],
      default: null,
    },
  },
  emits: ['update:selectedAddressId', 'add-address-click'],
  computed: {
    selectedId: {
      get() {
        return this.selectedAddressId;
      },
      set(value) {
        this.$emit('update:selectedAddressId', value);
      },
    },
  },
};
</script>
