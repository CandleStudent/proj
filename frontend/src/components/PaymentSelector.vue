<!-- components/cart/PaymentSelector.vue -->
<template>
  <div class="flex space-x-6">
    <label class="custom-radio-label">
      <input
          type="radio"
          value="cash"
          v-model="localValue"
          class="hidden"
      />
      <span
          :class="{
          'bg-yellow-400 text-white': localValue === 'cash',
          'bg-gray-200': localValue !== 'cash'
        }"
          class="cursor-pointer px-4 py-2 rounded select-none"
      >
        Наличными
      </span>
    </label>
    <label class="custom-radio-label">
      <input
          type="radio"
          value="card"
          v-model="localValue"
          class="hidden"
      />
      <span
          :class="{
          'bg-yellow-400 text-white': localValue === 'card',
          'bg-gray-200': localValue !== 'card'
        }"
          class="cursor-pointer px-4 py-2 rounded select-none"
      >
        Банковской картой
      </span>
    </label>
  </div>
</template>

<script>
export default {
  name: 'PaymentSelector',
  props: {
    modelValue: {
      type: String,
      default: 'cash',
    },
  },
  emits: ['update:modelValue'],
  computed: {
    localValue: {
      get() {
        return this.modelValue;
      },
      set(value) {
        this.$emit('update:modelValue', value);
      },
    },
  },
};
</script>

<style scoped>
.custom-radio-label input:checked + span {
  background-color: #f6ad55;
  color: white;
}
.custom-radio-label span {
  user-select: none;
}
</style>
