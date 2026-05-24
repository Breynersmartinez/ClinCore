import axiosInstance from './axiosConfig'

export const getUsuarios = () => axiosInstance.get('/usuarios')
export const createUsuario = (data) => axiosInstance.post('/usuarios', data)
export const updateUsuarioRoles = (id, roleIds) =>
  axiosInstance.put(`/usuarios/${id}/roles`, roleIds)
export const changePassword = (id, data) =>
  axiosInstance.patch(`/usuarios/${id}/password`, data)
export const activarUsuario = (id) =>
  axiosInstance.patch(`/usuarios/${id}/activar`)
export const inactivarUsuario = (id) =>
  axiosInstance.delete(`/usuarios/${id}`)
