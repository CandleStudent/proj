<script>
  import axios from 'axios'
  import Header from "@/components/Header.vue";
  // Вынесенные переменные
  const API_HOST = 'http://localhost:8080'
  const PROFILE_ENDPOINT = '/api/profile'

  export default {
    components: {Header},
    data() {
      return {
        form: {
          name: '',
          surname: '',
          phone: '',
          email: '',
        },
        passwordData: {
          oldPassword: '',
          newPassword: '',
          newPasswordRepeated: '',
        },
        orders: [],
        orderIndex: 0,
      }
    },
    methods: {
      async fetchProfile() {
        const token = localStorage.getItem('jwt_token')
        const res = await axios.get(`${API_HOST}${PROFILE_ENDPOINT}`, {
          headers: { Authorization: `Bearer ${token}` }
        })
        this.form = res.data
      },
      async updateProfile() {
        const token = localStorage.getItem('jwt_token')
        await axios.put(`${API_HOST}${PROFILE_ENDPOINT}`, this.form, {
          headers: { Authorization: `Bearer ${token}` }
        })
        alert('Данные профиля обновлены')
      },
      async updatePassword() {
        const token = localStorage.getItem('jwt_token')
        try {
          await axios.put(`${API_HOST}${PROFILE_ENDPOINT}/password`, this.passwordData, {
            headers: {Authorization: `Bearer ${token}`}
          })
          alert('Пароль обновлен')
        } catch (err) {
          alert(err.response.data ? err.response.data : err)
        }
      },
    },
    mounted() {
      this.fetchProfile()
    }
  }
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

    <h2 class="text-xl font-semibold text-green-700 mb-2">Изменить пароль</h2>

    <div>
      <label class="block text-sm mb-1 text-gray-600">Старый пароль</label>
      <input type="password" v-model="passwordData.oldPassword" class="w-full p-2 border rounded focus:outline-none focus:ring-2 focus:ring-green-500" placeholder="Введите старый пароль" />
    </div>

    <div>
      <label class="block text-sm mb-1 text-gray-600">Новый пароль</label>
      <input type="password" v-model="passwordData.newPassword" class="w-full p-2 border rounded focus:outline-none focus:ring-2 focus:ring-green-500" placeholder="Введите новый пароль" />
    </div>

    <div>
      <label class="block text-sm mb-1 text-gray-600">Повторите новый пароль</label>
      <input type="password" v-model="passwordData.newPasswordRepeated" class="w-full p-2 border rounded focus:outline-none focus:ring-2 focus:ring-green-500" placeholder="Введите новый пароль снова" />
    </div>

    <button @click="updatePassword" class="w-full bg-green-600 hover:bg-green-700 text-white py-2 rounded transition">
      Обновить пароль
    </button>
  </div>
</template>

<style scoped>

</style>