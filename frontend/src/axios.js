import axios from 'axios'
import {router} from './router'


const wrappingApi = axios.create({
  baseURL: 'http://localhost:8080/api',
})


wrappingApi.interceptors.request.use(config => {
  const isAuthEndpoint = config.url?.includes('/auth/login') || config.url?.includes('/auth/register')
  const token = localStorage.getItem('jwt_token')
  if (token && !isAuthEndpoint) {
    config.headers.Authorization = `Bearer ${token}`
  }

  return config
})

// ❗ Перехватчик ответа — проверка на истечение токена
wrappingApi.interceptors.response.use(
    response => response,
    error => {
      const isExpiredJwt = error.response?.status === 403

      console.log(error)
      if (isExpiredJwt) {
        // Чистим хранилище
        localStorage.removeItem('jwt_token')
        localStorage.removeItem('user_role')
        // Перенаправление
        router.push('/auth')
      }

      return Promise.reject(error)
    }
)

export default wrappingApi
