import React, { useState } from 'react'
import { createPaciente, updatePaciente } from '../../api/pacientesApi'
import { useAlert } from '../../components/ui/Alert'

export default function PacienteForm({ paciente, onSuccess, onCancel }) {
  const { addToast } = useAlert()
  const [form, setForm] = useState({
    idPersona: paciente?.idPersona || '',
    numeroHistoria: paciente?.numeroHistoria || '',
    tipoAfiliacion: paciente?.tipoAfiliacion || 'CONTRIBUTIVO',
    eps: paciente?.eps || '',
    activo: paciente?.activo || 'S',
  })
  const [loading, setLoading] = useState(false)

  const handleSubmit = async (e) => {
    e.preventDefault()
    setLoading(true)
    try {
      const payload = {
        ...form,
        idPersona: Number(form.idPersona),
      }
      if (paciente) {
        await updatePaciente(paciente.idPaciente, payload)
        addToast('Paciente actualizado', 'success')
      } else {
        await createPaciente(payload)
        addToast('Paciente creado', 'success')
      }
      onSuccess?.()
    } catch (err) {
      addToast(err.response?.data?.message || 'Error al guardar paciente', 'error')
    } finally {
      setLoading(false)
    }
  }

  const inputCls = 'w-full px-3 py-2.5 border border-gray-200 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-teal-500 bg-white'

  return (
    <form onSubmit={handleSubmit} className="px-6 py-5 space-y-4">
      <div className="grid grid-cols-2 gap-4">
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">ID Persona *</label>
          <input type="number" min="1" value={form.idPersona}
            onChange={(e) => setForm({ ...form, idPersona: e.target.value })}
            required className={inputCls} />
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">Historia clínica *</label>
          <input type="text" value={form.numeroHistoria}
            onChange={(e) => setForm({ ...form, numeroHistoria: e.target.value })}
            required className={inputCls} placeholder="HIS-0001" />
        </div>
      </div>

      <div className="grid grid-cols-2 gap-4">
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">Tipo afiliación</label>
          <select value={form.tipoAfiliacion}
            onChange={(e) => setForm({ ...form, tipoAfiliacion: e.target.value })}
            className={inputCls}>
            <option value="CONTRIBUTIVO">Contributivo</option>
            <option value="SUBSIDIADO">Subsidiado</option>
            <option value="VINCULADO">Vinculado</option>
            <option value="PARTICULAR">Particular</option>
          </select>
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">EPS</label>
          <input type="text" value={form.eps}
            onChange={(e) => setForm({ ...form, eps: e.target.value })}
            className={inputCls} placeholder="Nombre EPS" />
        </div>
      </div>

      {paciente && (
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">Estado</label>
          <select value={form.activo}
            onChange={(e) => setForm({ ...form, activo: e.target.value })}
            className={inputCls}>
            <option value="S">Activo</option>
            <option value="N">Inactivo</option>
          </select>
        </div>
      )}

      <div className="flex gap-3 pt-2">
        <button type="button" onClick={onCancel}
          className="flex-1 py-2.5 border border-gray-200 text-gray-600 rounded-lg text-sm font-medium hover:bg-gray-50">
          Cancelar
        </button>
        <button type="submit" disabled={loading}
          className="flex-1 py-2.5 bg-teal-700 text-white rounded-lg text-sm font-medium hover:bg-teal-800 disabled:opacity-50">
          {paciente ? 'Actualizar' : 'Crear'}
        </button>
      </div>
    </form>
  )
}
