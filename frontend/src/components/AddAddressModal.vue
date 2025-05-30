<template>
  <transition name="fade">
    <div
        v-if="show"
        class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50"
        @click.self="close"
    >
      <div class="bg-white rounded-lg shadow-lg w-full max-w-md p-6 relative">
        <h3 class="text-xl font-semibold mb-4">Добавить новый адрес</h3>
        <form @submit.prevent="submitAddress">
          <div class="space-y-3">
            <div>
              <label class="block font-medium mb-1" for="city">Город *</label>
              <input
                  id="city"
                  v-model="address.city"
                  type="text"
                  required
                  maxlength="50"
                  class="w-full border rounded px-3 py-2"
              />
            </div>
            <div>
              <label class="block font-medium mb-1" for="street">Улица *</label>
              <input
                  id="street"
                  v-model="address.street"
                  type="text"
                  required
                  maxlength="50"
                  class="w-full border rounded px-3 py-2"
              />
            </div>
            <div>
              <label class="block font-medium mb-1" for="building">Строение / Дом *</label>
              <input
                  id="building"
                  v-model="address.building"
                  type="text"
                  required
                  maxlength="50"
                  class="w-full border rounded px-3 py-2"
              />
            </div>
            <div>
              <label class="block font-medium mb-1" for="entrance">Подъезд</label>
              <input
                  id="entrance"
                  v-model.number="address.entrance"
                  type="number"
                  min="1"
                  class="w-full border rounded px-3 py-2"
              />
            </div>
            <div>
              <label class="block font-medium mb-1" for="floor">Этаж</label>
              <input
                  id="floor"
                  v-model.number="address.floor"
                  type="number"
                  min="1"
                  class="w-full border rounded px-3 py-2"
              />
            </div>
            <div>
              <label class="block font-medium mb-1" for="apartments">Квартира</label>
              <input
                  id="apartments"
                  v-model.number="address.apartments"
                  type="number"
                  min="1"
                  class="w-full border rounded px-3 py-2"
              />
            </div>
            <div>
              <label class="block font-medium mb-1" for="comment">Комментарий</label>
              <textarea
                  id="comment"
                  v-model="address.comment"
                  class="w-full border rounded px-3 py-2"
                  rows="2"
              ></textarea>
            </div>
          </div>
          <div class="mt-6 flex justify-end space-x-3">
            <button
                type="button"
                @click="close"
                class="px-4 py-2 rounded border border-gray-300 hover:bg-gray-100"
            >
              Отмена
            </button>
            <button
                type="submit"
                class="bg-yellow-400 hover:bg-yellow-500 text-gray-900 px-4 py-2 rounded"
            >
              Добавить адрес
            </button>
          </div>
        </form>
      </div>
    </div>
  </transition>
</template>

<script>
export default {
  props: {
    show: Boolean,
  },
  emits: ['close', 'address-added'],
  data() {
    return {
      address: {
        city: '',
        street: '',
        building: '',
        entrance: null,
        floor: null,
        apartments: null,
        comment: '',
      },
    };
  },
  methods: {
    async submitAddress() {
      try {
        const token = localStorage.getItem('jwt_token');
        if (!token) throw new Error('Неавторизован');

        const payload = {
          city: this.address.city.trim(),
          street: this.address.street.trim(),
          building: this.address.building.trim(),
          entrance: this.address.entrance || null,
          floor: this.address.floor || null,
          apartments: this.address.apartments || null,
          comment: this.address.comment?.trim() || null,
        };

        const response = await fetch('http://localhost:8080/api/address', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify(payload),
        });

        if (!response.ok) throw new Error('Ошибка добавления адреса');

        this.$emit('address-added');
        this.close();
        this.resetForm();
      } catch (e) {
        alert(e.message || 'Ошибка при добавлении адреса');
      }
    },
    resetForm() {
      this.address = {
        city: '',
        street: '',
        building: '',
        entrance: null,
        floor: null,
        apartments: null,
        comment: '',
      };
    },
    close() {
      this.$emit('close');
    },
  },
};
</script>

<style scoped>
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
