import axiosInstance from './axiosConfig'

export const getCitas = () => axiosInstance.get('/citas')
export const getCitasByPaciente = (id) => axiosInstance.get(`/citas/paciente/${id}`)
export const getCitasByMedico = (id) => axiosInstance.get(`/citas/medico/${id}`)
export const createCita = (data) => axiosInstance.post('/citas', data)
export const cambiarEstadoCita = (id, estado) =>
  axiosInstance.patch(`/citas/${id}/estado?estado=${estado}`)
export const cancelarCita = (id, motivo) =>
  axiosInstance.delete(`/citas/${id}?motivo=${encodeURIComponent(motivo)}`)

export const getMedicos = () => axiosInstance.get('/medicos')
export const getPacientes = () => axiosInstance.get('/pacientes')
export const getEspecialidades = () => axiosInstance.get('/especialidades')
export const getConsultorios = () => axiosInstance.get('/consultorios')
export const getSedes = () => axiosInstance.get('/sedes')
