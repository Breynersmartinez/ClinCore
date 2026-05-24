import React, { useState, useEffect } from 'react'
import { createUsuario } from '../../api/usuariosApi'
import { getRoles } from '../../api/rolesApi'
import { useAlert } from '../../components/ui/Alert'

export default function UsuarioForm({ onSuccess, onCancel }) {
  const { addToast } = useAlert()
  const [form, setForm] = useState({
    username: '',
    email: '',
    password: '',
    roleIds: [],
  })
  const [roles, setRoles] = useState([])
  const [loading, setLoading] = useState(false)
  const [errors, setErrors] = useState({})

  useEffect(() => {
    getRoles()
      .then((res) => setRoles(res.data?.data || []))
      .catch(() => {})
  }, [])

  const validate = () => {
    const e = {}
    if (!form.username.trim()) e.username = 'El usuario es requerido'
    if (!form.email.trim()) e.email = 'El email es requerido'
    if (!form.password.trim()) e.password = 'La contraseña es requerida'
    if (form.password.length < 6) e.password = 'Mínimo 6 caracteres'
    return e
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    const errs = validate()
    if (Object.keys(errs).length > 0) {
      setErrors(errs)
      return
    }
    setLoading(true)
    try {
      await createUsuario(form)
      addToast('Usuario creado correctamente', 'success')
      onSuccess?.()
    } catch (err) {
      addToast(err.response?.data?.message || 'Error al crear usuario', 'error')
    } finally {
      setLoading(false)
    }
  }

  const toggleRole = (id) => {
    setForm((prev) => ({
      ...prev,
      roleIds: prev.roleIds.includes(id)
        ? prev.roleIds.filter((r) => r !== id)
        : [...prev.roleIds, id],
    }))
  }

  return (
    <form onSubmit={handleSubmit} className="px-6 py-5 space-y-4">
      <div>
        <label className="block text-sm font-medium text-gray-700 mb-1">Usuario *</label>
        <input
          type="text"
          value={form.username}
          onChange={(e) => setForm({ ...form, username: e.target.value })}
          className={`w-full px-3 py-2.5 border rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-teal-500 ${
            errors.username ? 'border-red-400' : 'border-gray-200'
          }`}
          placeholder="nombre.usuario"
        />
        {errors.username && <p className="text-xs text-red-500 mt-1">{errors.username}</p>}
      </div>

      <div>
        <label className="block text-sm font-medium text-gray-700 mb-1">Email *</label>
        <input
          type="email"
          value={form.email}
          onChange={(e) => setForm({ ...form, email: e.target.value })}
          className={`w-full px-3 py-2.5 border rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-teal-500 ${
            errors.email ? 'border-red-400' : 'border-gray-200'
          }`}
          placeholder="usuario@ejemplo.com"
        />
        {errors.email && <p className="text-xs text-red-500 mt-1">{errors.email}</p>}
      </div>

      <div>
        <label className="block text-sm font-medium text-gray-700 mb-1">Contraseña *</label>
        <input
          type="password"
          value={form.password}
          onChange={(e) => setForm({ ...form, password: e.target.value })}
          className={`w-full px-3 py-2.5 border rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-teal-500 ${
            errors.password ? 'border-red-400' : 'border-gray-200'
          }`}
          placeholder="Mínimo 6 caracteres"
        />
        {errors.password && <p className="text-xs text-red-500 mt-1">{errors.password}</p>}
      </div>

      <div>
        <label className="block text-sm font-medium text-gray-700 mb-2">Roles</label>
        <div className="space-y-1.5 max-h-40 overflow-y-auto">
          {roles.map((rol) => (
            <label key={rol.idRol} className="flex items-center gap-3 px-3 py-2 rounded-lg hover:bg-gray-50 cursor-pointer border border-transparent hover:border-gray-200">
              <input
                type="checkbox"
                checked={form.roleIds.includes(rol.idRol)}
                onChange={() => toggleRole(rol.idRol)}
                className="w-4 h-4 text-teal-600 rounded border-gray-300 focus:ring-teal-500"
              />
              <div>
                <p className="text-sm font-medium text-gray-700">{rol.nombreRol}</p>
                {rol.descripcion && (
                  <p className="text-xs text-gray-400">{rol.descripcion}</p>
                )}
              </div>
            </label>
          ))}
          {roles.length === 0 && (
            <p className="text-sm text-gray-400 px-2">Sin roles disponibles</p>
          )}
        </div>
      </div>

      <div className="flex gap-3 pt-2">
        <button
          type="button"
          onClick={onCancel}
          className="flex-1 py-2.5 border border-gray-200 text-gray-600 rounded-lg text-sm font-medium hover:bg-gray-50 transition-colors"
        >
          Cancelar
        </button>
        <button
          type="submit"
          disabled={loading}
          className="flex-1 py-2.5 bg-teal-700 text-white rounded-lg text-sm font-medium hover:bg-teal-800 transition-colors disabled:opacity-50 flex items-center justify-center gap-2"
        >
          {loading && (
            <svg className="animate-spin w-4 h-4" fill="none" viewBox="0 0 24 24">
              <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4" />
              <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v4a4 4 0 00-4 4H4z" />
            </svg>
          )}
          Crear Usuario
        </button>
      </div>
    </form>
  )
}
