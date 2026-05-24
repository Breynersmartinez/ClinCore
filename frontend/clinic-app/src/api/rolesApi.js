import axiosInstance from './axiosConfig'

export const getRoles = () => axiosInstance.get('/roles')
export const createRol = (data) => axiosInstance.post('/roles', data)
export const updateRol = (id, data) => axiosInstance.put(`/roles/${id}`, data)
export const updateRolPermisos = (id, permisoIds) =>
  axiosInstance.put(`/roles/${id}/permisos`, permisoIds)
export const inactivarRol = (id) => axiosInstance.delete(`/roles/${id}`)

export const getPermisos = (modulo) => {
  const params = modulo ? { modulo } : {}
  return axiosInstance.get('/permisos', { params })
}
