import axiosInstance from './axiosConfig'

export const getPacientes = () => axiosInstance.get('/pacientes')
export const getPacientesActivos = () => axiosInstance.get('/pacientes/activos')
export const createPaciente = (data) => axiosInstance.post('/pacientes', data)
export const updatePaciente = (id, data) => axiosInstance.put(`/pacientes/${id}`, data)
export const inactivarPaciente = (id) => axiosInstance.delete(`/pacientes/${id}`)
