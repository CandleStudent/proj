<script setup>

</script>

<template>
  <div class="bg-white rounded-lg shadow-md p-6 w-full max-w-md space-y-4">
    <h2 class="text-xl font-semibold text-green-700 mb-2">Профиль</h2>

    <div>
      <label class="block text-sm mb-1 text-gray-600">Имя</label>
      <input v-model="form.name" class="w-full p-2 border rounded focus:outline-none focus:ring-2 focus:ring-green-500" placeholder="Введите имя" />
    </div>

    <div>
      <label class="block text-sm mb-1 text-gray-600">Фамилия</label>
      <input v-model="form.surname" class="w-full p-2 border rounded focus:outline-none focus:ring-2 focus:ring-green-500" placeholder="Введите фамилию" />
    </div>

    <div>
      <label class="block text-sm mb-1 text-gray-600">Телефон</label>
      <input v-model="form.phone" class="w-full p-2 border rounded focus:outline-none focus:ring-2 focus:ring-green-500" placeholder="+7 (___) ___-__-__" />
    </div>

    <div>
      <label class="block text-sm mb-1 text-gray-600">Электронная почта</label>
      <input :value="form.email" class="input bg-gray-100 cursor-not-allowed w-full p-2 border rounded focus:outline-none focus:ring-2 focus:ring-green-500" disabled />
    </div>

    <button @click="updateProfile" class="w-full bg-green-600 hover:bg-green-700 text-white py-2 rounded transition">
      Обновить данные профиля
    </button>
  </div>
</template>