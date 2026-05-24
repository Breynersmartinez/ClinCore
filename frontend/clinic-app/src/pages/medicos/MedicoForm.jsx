import React, { useEffect, useState } from 'react'
import { createMedico, getEspecialidades, updateMedico } from '../../api/medicosApi'
import { useAlert } from '../../components/ui/Alert'

export default function MedicoForm({ medico, onSuccess, onCancel }) {
  const { addToast } = useAlert()
  const [form, setForm] = useState({
    idPersona: medico?.idPersona || '',
    idEspecialidad: medico?.idEspecialidad || '',
    numeroRegistro: medico?.numeroRegistro || '',
    tarifaConsulta: medico?.tarifaConsulta || '',
    activo: medico?.activo || 'S',
  })
  const [especialidades, setEspecialidades] = useState([])
  const [loading, setLoading] = useState(false)

  useEffect(() => {
    getEspecialidades()
      .then((res) => setEspecialidades(res.data?.data || []))
      .catch(() => {})
  }, [])

  const handleSubmit = async (e) => {
    e.preventDefault()
    setLoading(true)
    try {
      const payload = {
        ...form,
        idPersona: Number(form.idPersona),
        idEspecialidad: Number(form.idEspecialidad),
        tarifaConsulta: form.tarifaConsulta ? Number(form.tarifaConsulta) : null,
      }
      if (medico) {
        await updateMedico(medico.idMedico, payload)
        addToast('Médico actualizado', 'success')
      } else {
        await createMedico(payload)
        addToast('Médico creado', 'success')
      }
      onSuccess?.()
    } catch (err) {
      addToast(err.response?.data?.message || 'Error al guardar médico', 'error')
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
          <label className="block text-sm font-medium text-gray-700 mb-1">Registro médico *</label>
          <input type="text" value={form.numeroRegistro}
            onChange={(e) => setForm({ ...form, numeroRegistro: e.target.value })}
            required className={inputCls} placeholder="RM-0001" />
        </div>
      </div>

      <div className="grid grid-cols-2 gap-4">
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">Especialidad *</label>
          <select value={form.idEspecialidad}
            onChange={(e) => setForm({ ...form, idEspecialidad: e.target.value })}
            required className={inputCls}>
            <option value="">Seleccionar...</option>
            {especialidades.map((e) => (
              <option key={e.idEspecialidad} value={e.idEspecialidad}>
                {e.nombreEspecialidad || e.nombre}
              </option>
            ))}
          </select>
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">Tarifa consulta</label>
          <input type="number" min="0" step="0.01" value={form.tarifaConsulta}
            onChange={(e) => setForm({ ...form, tarifaConsulta: e.target.value })}
            className={inputCls} placeholder="0.00" />
        </div>
      </div>

      {medico && (
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
          {medico ? 'Actualizar' : 'Crear'}
        </button>
      </div>
    </form>
  )
}
