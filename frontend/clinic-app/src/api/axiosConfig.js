import axios from 'axios'

const BASE_URL = 'http://localhost:8080/api/v1'

const axiosInstance = axios.create({
  baseURL: BASE_URL,
  withCredentials: false,
  headers: {
    'Content-Type': 'application/json',
  },
})

// Request interceptor: add Authorization header
axiosInstance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('accessToken')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => Promise.reject(error)
)

// Flag to prevent infinite refresh loops
let isRefreshing = false
let failedQueue = []

function processQueue(error, token = null) {
  failedQueue.forEach((prom) => {
    if (error) {
      prom.reject(error)
    } else {
      prom.resolve(token)
    }
  })
  failedQueue = []
}

// Response interceptor: handle 401 with token refresh
axiosInstance.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config

    if (error.response?.status === 401 && !originalRequest._retry) {
      if (isRefreshing) {
        return new Promise((resolve, reject) => {
          failedQueue.push({ resolve, reject })
        })
          .then((token) => {
            originalRequest.headers.Authorization = `Bearer ${token}`
            return axiosInstance(originalRequest)
          })
          .catch((err) => Promise.reject(err))
      }

      originalRequest._retry = true
      isRefreshing = true

      const refreshToken = localStorage.getItem('refreshToken')

      if (!refreshToken) {
        isRefreshing = false
        // Dispatch logout event
        window.dispatchEvent(new Event('auth:logout'))
        return Promise.reject(error)
      }

      try {
        const response = await axios.post(
          `${BASE_URL}/auth/refresh?refreshToken=${refreshToken}`
        )
        const newToken = response.data?.data?.accessToken

        if (newToken) {
          localStorage.setItem('accessToken', newToken)
          axiosInstance.defaults.headers.common.Authorization = `Bearer ${newToken}`
          originalRequest.headers.Authorization = `Bearer ${newToken}`
          processQueue(null, newToken)
          isRefreshing = false
          return axiosInstance(originalRequest)
        }
      } catch (refreshError) {
        processQueue(refreshError, null)
        isRefreshing = false
        localStorage.removeItem('accessToken')
        localStorage.removeItem('refreshToken')
        localStorage.removeItem('user')
        window.dispatchEvent(new Event('auth:logout'))
        return Promise.reject(refreshError)
      }
    }

    return Promise.reject(error)
  }
)

export default axiosInstance
