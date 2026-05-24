import React, { useState, useEffect } from 'react'
import axiosInstance from '../../api/axiosConfig'
import { useAlert } from '../../components/ui/Alert'

export default function ConsultorioForm({ consultorio, onSuccess, onCancel }) {
  const { addToast } = useAlert()
  const [form, setForm] = useState({
    codigoConsultorio: consultorio?.codigoConsultorio || '',
    nombreConsultorio: consultorio?.nombreConsultorio || '',
    numeroPiso: consultorio?.numeroPiso || '',
    capacidad: consultorio?.capacidad || '',
    idSede: consultorio?.idSede || '',
    activo: consultorio?.activo || 'S',
  })
  const [sedes, setSedes] = useState([])
  const [loading, setLoading] = useState(false)

  useEffect(() => {
    axiosInstance.get('/sedes').then((r) => setSedes(r.data?.data || [])).catch(() => {})
  }, [])

  const handleSubmit = async (e) => {
    e.preventDefault()
    setLoading(true)
    try {
      if (consultorio) {
        await axiosInstance.put(`/consultorios/${consultorio.idConsultorio}`, {
          ...form,
          numeroPiso: form.numeroPiso ? Number(form.numeroPiso) : null,
          capacidad: form.capacidad ? Number(form.capacidad) : null,
          idSede: Number(form.idSede),
        })
        addToast('Consultorio actualizado', 'success')
      } else {
        await axiosInstance.post('/consultorios', {
          ...form,
          numeroPiso: form.numeroPiso ? Number(form.numeroPiso) : null,
          capacidad: form.capacidad ? Number(form.capacidad) : null,
          idSede: Number(form.idSede),
        })
        addToast('Consultorio creado', 'success')
      }
      onSuccess?.()
    } catch (err) {
      addToast(err.response?.data?.message || 'Error al guardar', 'error')
    } finally {
      setLoading(false)
    }
  }

  return (
    <form onSubmit={handleSubmit} className="px-6 py-5 space-y-4">
      <div className="grid grid-cols-2 gap-4">
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">Código *</label>
          <input
            type="text"
            value={form.codigoConsultorio}
            onChange={(e) => setForm({ ...form, codigoConsultorio: e.target.value })}
            required
            className="w-full px-3 py-2.5 border border-gray-200 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-teal-500"
            placeholder="CON-001"
          />
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">Piso</label>
          <input
            type="number"
            value={form.numeroPiso}
            onChange={(e) => setForm({ ...form, numeroPiso: e.target.value })}
            className="w-full px-3 py-2.5 border border-gray-200 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-teal-500"
            placeholder="1"
          />
        </div>
      </div>

      <div>
        <label className="block text-sm font-medium text-gray-700 mb-1">Nombre *</label>
        <input
          type="text"
          value={form.nombreConsultorio}
          onChange={(e) => setForm({ ...form, nombreConsultorio: e.target.value })}
          required
          className="w-full px-3 py-2.5 border border-gray-200 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-teal-500"
          placeholder="Consultorio de Medicina General"
        />
      </div>

      <div className="grid grid-cols-2 gap-4">
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">Capacidad</label>
          <input
            type="number"
            value={form.capacidad}
            onChange={(e) => setForm({ ...form, capacidad: e.target.value })}
            className="w-full px-3 py-2.5 border border-gray-200 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-teal-500"
            placeholder="5"
          />
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">Sede</label>
          <select
            value={form.sedeId}
            onChange={(e) => setForm({ ...form, idSede: e.target.value })}
            required
            className="w-full px-3 py-2.5 border border-gray-200 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-teal-500 bg-white"
          >
            <option value="">Seleccionar sede...</option>
            {sedes.map((s) => (
              <option key={s.idSede} value={s.idSede}>{s.nombreSede || s.nombre}</option>
            ))}
          </select>
        </div>
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
          {consultorio ? 'Actualizar' : 'Crear'}
        </button>
      </div>
    </form>
  )
}
