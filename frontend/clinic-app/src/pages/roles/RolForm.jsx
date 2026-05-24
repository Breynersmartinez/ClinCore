import React, { useState, useEffect } from 'react'
import { createRol, updateRol, getPermisos } from '../../api/rolesApi'
import { useAlert } from '../../components/ui/Alert'

export default function RolForm({ rol, onSuccess, onCancel }) {
  const { addToast } = useAlert()
  const [form, setForm] = useState({
    nombreRol: rol?.nombreRol || '',
    descripcion: rol?.descripcion || '',
    permisoIds: [],
  })
  const [permisos, setPermisos] = useState([])
  const [loading, setLoading] = useState(false)

  useEffect(() => {
    getPermisos()
      .then((res) => setPermisos(res.data?.data || []))
      .catch(() => {})
    if (rol?.permisos) {
      setForm((prev) => ({ ...prev, permisoIds: rol.permisos.map((p) => p.idPermiso) }))
    }
  }, [rol])

  const handleSubmit = async (e) => {
    e.preventDefault()
    if (!form.nombreRol.trim()) return
    setLoading(true)
    try {
      if (rol) {
        await updateRol(rol.idRol, form)
        addToast('Rol actualizado correctamente', 'success')
      } else {
        await createRol(form)
        addToast('Rol creado correctamente', 'success')
      }
      onSuccess?.()
    } catch (err) {
      addToast(err.response?.data?.message || 'Error al guardar rol', 'error')
    } finally {
      setLoading(false)
    }
  }

  const togglePermiso = (id) => {
    setForm((prev) => ({
      ...prev,
      permisoIds: prev.permisoIds.includes(id)
        ? prev.permisoIds.filter((p) => p !== id)
        : [...prev.permisoIds, id],
    }))
  }

  // Group permisos by module
  const grouped = permisos.reduce((acc, p) => {
    if (!acc[p.modulo]) acc[p.modulo] = []
    acc[p.modulo].push(p)
    return acc
  }, {})

  return (
    <form onSubmit={handleSubmit} className="px-6 py-5 space-y-4">
      <div>
        <label className="block text-sm font-medium text-gray-700 mb-1">Nombre del rol *</label>
        <input
          type="text"
          value={form.nombreRol}
          onChange={(e) => setForm({ ...form, nombreRol: e.target.value })}
          required
          className="w-full px-3 py-2.5 border border-gray-200 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-teal-500"
          placeholder="Ej: MEDICO"
        />
      </div>
      <div>
        <label className="block text-sm font-medium text-gray-700 mb-1">Descripción</label>
        <textarea
          value={form.descripcion}
          onChange={(e) => setForm({ ...form, descripcion: e.target.value })}
          rows={2}
          className="w-full px-3 py-2.5 border border-gray-200 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-teal-500 resize-none"
          placeholder="Descripción del rol..."
        />
      </div>

      <div>
        <label className="block text-sm font-medium text-gray-700 mb-2">Permisos iniciales</label>
        <div className="max-h-48 overflow-y-auto space-y-3 border border-gray-200 rounded-lg p-3">
          {Object.entries(grouped).map(([modulo, items]) => (
            <div key={modulo}>
              <p className="text-xs font-semibold text-gray-500 uppercase mb-1.5">{modulo}</p>
              <div className="flex flex-wrap gap-2">
                {items.map((p) => (
                  <label key={p.idPermiso} className="flex items-center gap-1.5 cursor-pointer">
                    <input
                      type="checkbox"
                      checked={form.permisoIds.includes(p.idPermiso)}
                      onChange={() => togglePermiso(p.idPermiso)}
                      className="w-3.5 h-3.5 text-teal-600 rounded border-gray-300 focus:ring-teal-500"
                    />
                    <span className="text-xs text-gray-600">{p.accion}</span>
                  </label>
                ))}
              </div>
            </div>
          ))}
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
          {rol ? 'Actualizar' : 'Crear'} Rol
        </button>
      </div>
    </form>
  )
}
