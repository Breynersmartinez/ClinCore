import axiosInstance from './axiosConfig'

export const login = (credentials) =>
  axiosInstance.post('/auth/login', credentials)

export const refresh = (refreshToken) =>
  axiosInstance.post(`/auth/refresh?refreshToken=${refreshToken}`)
