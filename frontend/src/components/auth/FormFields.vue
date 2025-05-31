<template>
  <div>
    <!-- Email -->
    <div class="mb-2">
      <label for="email" class="block text-base font-medium text-gray-600 mb-2 text-center">Email</label>
      <input
          type="email"
          id="email"
          :value="email"
          @input="$emit('update:email', $event.target.value); $emit('clearEmailError')"
          placeholder="example@domain.com"
          :class="[
          'w-full px-4 py-3 text-base border rounded-md focus:outline-none focus:ring-2 transition-colors duration-300',
          emailError ? 'border-red-400 focus:ring-red-400' : 'focus:ring-green-500'
        ]"
      />
      <p v-if="emailError" class="mt-1 text-sm text-red-500">Введите корректный email</p>
    </div>

    <!-- Password -->
    <div class="mb-6">
      <label for="password" class="block text-base font-medium text-gray-600 mb-2 text-center">Пароль</label>
      <input
          type="password"
          id="password"
          :value="password"
          @input="$emit('update:password', $event.target.value); $emit('clearPasswordError')"
          placeholder="••••••••"
          :class="[
          'w-full px-4 py-3 text-base border rounded-md focus:outline-none focus:ring-2 transition-colors duration-300',
          passwordError ? 'border-red-400 focus:ring-red-400' : 'focus:ring-green-500'
        ]"
      />
      <p v-if="passwordError" class="mt-1 text-sm text-red-500">Поле обязательно для заполнения</p>
    </div>
  </div>
</template>

<script setup>
defineProps(['email', 'password', 'emailError', 'passwordError'])
defineEmits(['update:email', 'update:password', 'clearEmailError', 'clearPasswordError'])
</script>
