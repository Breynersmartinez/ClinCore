import axiosInstance from './axiosConfig'

export const getMedicos = () => axiosInstance.get('/medicos')
export const getMedicosActivos = () => axiosInstance.get('/medicos/activos')
export const getMedicosByEspecialidad = (id) => axiosInstance.get(`/medicos/especialidad/${id}`)
export const createMedico = (data) => axiosInstance.post('/medicos', data)
export const updateMedico = (id, data) => axiosInstance.put(`/medicos/${id}`, data)
export const inactivarMedico = (id) => axiosInstance.delete(`/medicos/${id}`)

export const getEspecialidades = () => axiosInstance.get('/especialidades')
