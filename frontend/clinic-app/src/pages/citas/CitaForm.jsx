import React, { useState, useEffect } from 'react'
import { createCita, getMedicos, getPacientes, getConsultorios } from '../../api/citasApi'
import axiosInstance from '../../api/axiosConfig'
import { useAlert } from '../../components/ui/Alert'

export default function CitaForm({ onSuccess, onCancel }) {
  const { addToast } = useAlert()
  const [form, setForm] = useState({
    idPaciente: '',
    idMedico: '',
    idConsultorio: '',
    especialidadId: '',
    fechaCita: '',
    horaInicio: '',
    horaFin: '',
    motivoConsulta: '',
    observaciones: '',
  })
  const [medicos, setMedicos] = useState([])
  const [pacientes, setPacientes] = useState([])
  const [consultorios, setConsultorios] = useState([])
  const [especialidades, setEspecialidades] = useState([])
  const [loading, setLoading] = useState(false)

  useEffect(() => {
    Promise.allSettled([
      getMedicos(),
      getPacientes(),
      getConsultorios(),
      axiosInstance.get('/especialidades'),
    ]).then(([med, pac, con, esp]) => {
      setMedicos(med.status === 'fulfilled' ? med.value.data?.data || [] : [])
      setPacientes(pac.status === 'fulfilled' ? pac.value.data?.data || [] : [])
      setConsultorios(con.status === 'fulfilled' ? con.value.data?.data || [] : [])
      setEspecialidades(esp.status === 'fulfilled' ? esp.value.data?.data || [] : [])
    })
  }, [])

  const handleSubmit = async (e) => {
    e.preventDefault()
    setLoading(true)
    try {
      await createCita({
        idPaciente: Number(form.idPaciente),
        idMedico: Number(form.idMedico),
        idConsultorio: Number(form.idConsultorio),
        fechaCita: form.fechaCita,
        horaInicio: form.horaInicio,
        horaFin: form.horaFin,
        motivoConsulta: form.motivoConsulta,
        observaciones: form.observaciones,
      })
      addToast('Cita agendada correctamente', 'success')
      onSuccess?.()
    } catch (err) {
      addToast(err.response?.data?.message || 'Error al agendar cita', 'error')
    } finally {
      setLoading(false)
    }
  }

  const inputCls = "w-full px-3 py-2.5 border border-gray-200 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-teal-500 bg-white"

  return (
    <form onSubmit={handleSubmit} className="px-6 py-5 space-y-4">
      <div className="grid grid-cols-2 gap-4">
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">Paciente *</label>
          <select value={form.idPaciente} onChange={(e) => setForm({ ...form, idPaciente: e.target.value })} required className={inputCls}>
            <option value="">Seleccionar...</option>
            {pacientes.map((p) => (
              <option key={p.idPaciente} value={p.idPaciente}>
                {p.nombreCompleto || `${p.nombre || ''} ${p.apellido || ''}`}
              </option>
            ))}
          </select>
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">Médico *</label>
          <select value={form.idMedico} onChange={(e) => setForm({ ...form, idMedico: e.target.value })} required className={inputCls}>
            <option value="">Seleccionar...</option>
            {medicos.map((m) => (
              <option key={m.idMedico} value={m.idMedico}>
                Dr(a). {m.nombreCompleto || `${m.nombre || ''} ${m.apellido || ''}`}
              </option>
            ))}
          </select>
        </div>
      </div>

      <div className="grid grid-cols-2 gap-4">
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">Especialidad</label>
          <select value={form.especialidadId} onChange={(e) => setForm({ ...form, especialidadId: e.target.value })} className={inputCls}>
            <option value="">Seleccionar...</option>
            {especialidades.map((e) => (
              <option key={e.idEspecialidad} value={e.idEspecialidad}>{e.nombreEspecialidad || e.nombre}</option>
            ))}
          </select>
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">Consultorio</label>
          <select value={form.idConsultorio} onChange={(e) => setForm({ ...form, idConsultorio: e.target.value })} required className={inputCls}>
            <option value="">Seleccionar...</option>
            {consultorios.map((c) => (
              <option key={c.idConsultorio} value={c.idConsultorio}>{c.nombreConsultorio || c.nombre}</option>
            ))}
          </select>
        </div>
      </div>

      <div className="grid grid-cols-3 gap-4">
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">Fecha *</label>
          <input type="date" value={form.fechaCita} onChange={(e) => setForm({ ...form, fechaCita: e.target.value })} required className={inputCls} />
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">Hora inicio</label>
          <input type="time" value={form.horaInicio} onChange={(e) => setForm({ ...form, horaInicio: e.target.value })} required className={inputCls} />
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">Hora fin</label>
          <input type="time" value={form.horaFin} onChange={(e) => setForm({ ...form, horaFin: e.target.value })} required className={inputCls} />
        </div>
      </div>

      <div>
        <label className="block text-sm font-medium text-gray-700 mb-1">Motivo</label>
        <textarea
          value={form.motivoConsulta}
          onChange={(e) => setForm({ ...form, motivoConsulta: e.target.value })}
          rows={2}
          className="w-full px-3 py-2.5 border border-gray-200 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-teal-500 resize-none"
          placeholder="Motivo de la consulta..."
        />
      </div>

      <div className="flex gap-3 pt-2">
        <button type="button" onClick={onCancel}
          className="flex-1 py-2.5 border border-gray-200 text-gray-600 rounded-lg text-sm font-medium hover:bg-gray-50">
          Cancelar
        </button>
        <button type="submit" disabled={loading}
          className="flex-1 py-2.5 bg-teal-700 text-white rounded-lg text-sm font-medium hover:bg-teal-800 disabled:opacity-50 flex items-center justify-center gap-2">
          {loading && (
            <svg className="animate-spin w-4 h-4" fill="none" viewBox="0 0 24 24">
              <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4" />
              <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v4a4 4 0 00-4 4H4z" />
            </svg>
          )}
          Agendar Cita
        </button>
      </div>
    </form>
  )
}
