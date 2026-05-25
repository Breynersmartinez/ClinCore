import React, { useState, useEffect, useMemo } from 'react'
import {
  getUsuarios,
  inactivarUsuario,
  activarUsuario,
  updateUsuarioRoles,
} from '../../api/usuariosApi'
import { getRoles } from '../../api/rolesApi'
import { useAlert } from '../../components/ui/Alert'
import { useAuth } from '../../hooks/useAuth'
import { ADMIN_ROLES, isActivo } from '../../utils/access'
import Modal from '../../components/ui/Modal'
import Badge from '../../components/ui/Badge'
import Table from '../../components/ui/Table'
import UsuarioForm from './UsuarioForm'

export default function UsuariosPage() {
  const { addToast } = useAlert()
  const { canAccess, hasPermiso } = useAuth()
  const [usuarios, setUsuarios] = useState([])
  const [roles, setRoles] = useState([])
  const [loading, setLoading] = useState(true)
  const [search, setSearch] = useState('')
  const [showForm, setShowForm] = useState(false)
  const [roleModal, setRoleModal] = useState(null) // usuario
  const [selectedRoles, setSelectedRoles] = useState([])
  const [savingRoles, setSavingRoles] = useState(false)

  const canCreate = canAccess({ roles: ADMIN_ROLES, permisos: ['USUARIOS_CREATE'] })
  const canUpdate = canAccess({ roles: ADMIN_ROLES, permisos: ['USUARIOS_UPDATE'] })
  const canDelete = canAccess({ roles: ADMIN_ROLES, permisos: ['USUARIOS_DELETE'] })
  const canReadRoles = hasPermiso('ROLES_READ')

  const fetchData = async () => {
    setLoading(true)
    try {
      const [usuRes, rolRes] = await Promise.all([
        getUsuarios(),
        canReadRoles ? getRoles() : Promise.resolve(null),
      ])
      setUsuarios(usuRes.data?.data || [])
      setRoles(rolRes?.data?.data || [])
    } catch {
      addToast('Error al cargar usuarios', 'error')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    fetchData()
  }, [canReadRoles])

  const filtered = useMemo(() => {
    if (!search) return usuarios
    return usuarios.filter((u) =>
      u.username?.toLowerCase().includes(search.toLowerCase()) ||
      u.email?.toLowerCase().includes(search.toLowerCase())
    )
  }, [usuarios, search])

  const handleToggleActivo = async (usuario) => {
    const active = isActivo(usuario.activo)
    const action = active ? 'inactivar' : 'activar'
    if (!window.confirm(`¿Deseas ${action} al usuario "${usuario.username}"?`)) return
    try {
      if (active) {
        await inactivarUsuario(usuario.idUsuario)
      } else {
        await activarUsuario(usuario.idUsuario)
      }
      addToast(`Usuario ${action}do correctamente`, 'success')
      fetchData()
    } catch (err) {
      addToast(err.response?.data?.message || `Error al ${action} usuario`, 'error')
    }
  }

  const openRoleModal = (usuario) => {
    setRoleModal(usuario)
    setSelectedRoles((usuario.roles || []).map((r) => r.idRol))
  }

  const handleSaveRoles = async () => {
    if (!roleModal) return
    setSavingRoles(true)
    try {
      await updateUsuarioRoles(roleModal.idUsuario, selectedRoles)
      addToast('Roles actualizados correctamente', 'success')
      setRoleModal(null)
      fetchData()
    } catch (err) {
      addToast(err.response?.data?.message || 'Error al actualizar roles', 'error')
    } finally {
      setSavingRoles(false)
    }
  }

  const columns = [
    {
      key: 'username',
      title: 'Usuario',
      render: (v, row) => (
        <div>
          <p className="font-medium text-gray-800">{v}</p>
          <p className="text-xs text-gray-400">{row.email}</p>
        </div>
      ),
    },
    {
      key: 'roles',
      title: 'Roles',
      render: (_, row) => (
        <div className="flex flex-wrap gap-1">
          {(row.roles || []).map((r) => (
            <Badge key={r.idRol} role={r.nombreRol}>{r.nombreRol}</Badge>
          ))}
          {(!row.roles || row.roles.length === 0) && (
            <span className="text-xs text-gray-400">Sin roles</span>
          )}
        </div>
      ),
    },
    {
      key: 'activo',
      title: 'Estado',
      render: (v) => (
        <Badge estado={isActivo(v) ? 'activo' : 'inactivo'}>
          {isActivo(v) ? 'Activo' : 'Inactivo'}
        </Badge>
      ),
    },
    {
      key: 'acciones',
      title: 'Acciones',
      render: (_, row) => (
        <div className="flex gap-1.5">
          {canUpdate && canReadRoles && (
            <button
              onClick={() => openRoleModal(row)}
              className="px-2.5 py-1.5 text-xs font-medium bg-blue-50 text-blue-700 rounded-lg hover:bg-blue-100 transition-colors"
            >
              Roles
            </button>
          )}
          {canDelete && (
            <button
              onClick={() => handleToggleActivo(row)}
              className={`px-2.5 py-1.5 text-xs font-medium rounded-lg transition-colors ${
                isActivo(row.activo)
                  ? 'bg-red-50 text-red-700 hover:bg-red-100'
                  : 'bg-green-50 text-green-700 hover:bg-green-100'
              }`}
            >
              {isActivo(row.activo) ? 'Inactivar' : 'Activar'}
            </button>
          )}
        </div>
      ),
    },
  ]

  return (
    <div>
      {/* Header */}
      <div className="flex items-center justify-between mb-6">
        <div>
          <h1 className="text-2xl font-bold text-gray-800">Usuarios</h1>
          <p className="text-gray-400 text-sm mt-0.5">Gestión de usuarios del sistema</p>
        </div>
        {canCreate && (
          <button
            onClick={() => setShowForm(true)}
            className="flex items-center gap-2 px-4 py-2.5 bg-teal-700 text-white rounded-lg text-sm font-medium hover:bg-teal-800 transition-colors"
          >
            <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" />
            </svg>
            Nuevo Usuario
          </button>
        )}
      </div>

      {/* Search */}
      <div className="mb-4">
        <div className="relative max-w-sm">
          <svg className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400"
            fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
              d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
          </svg>
          <input
            type="text"
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            placeholder="Buscar por usuario o email..."
            className="w-full pl-9 pr-4 py-2.5 border border-gray-200 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-teal-500"
          />
        </div>
      </div>

      {/* Table */}
      <Table columns={columns} data={filtered} loading={loading} emptyMessage="No hay usuarios registrados" />

      {/* Create Modal */}
      <Modal isOpen={showForm} onClose={() => setShowForm(false)} title="Nuevo Usuario">
        <UsuarioForm
          onSuccess={() => { setShowForm(false); fetchData() }}
          onCancel={() => setShowForm(false)}
        />
      </Modal>

      {/* Roles Modal */}
      <Modal
        isOpen={!!roleModal}
        onClose={() => setRoleModal(null)}
        title={`Asignar Roles — ${roleModal?.username}`}
      >
        <div className="px-6 py-5">
          <p className="text-sm text-gray-500 mb-4">Selecciona los roles para este usuario:</p>
          <div className="space-y-1.5 mb-6">
            {roles.map((rol) => (
              <label
                key={rol.idRol}
                className="flex items-center gap-3 px-3 py-2.5 rounded-lg hover:bg-gray-50 cursor-pointer border border-transparent hover:border-gray-200"
              >
                <input
                  type="checkbox"
                  checked={selectedRoles.includes(rol.idRol)}
                  onChange={() =>
                    setSelectedRoles((prev) =>
                      prev.includes(rol.idRol)
                        ? prev.filter((id) => id !== rol.idRol)
                        : [...prev, rol.idRol]
                    )
                  }
                  className="w-4 h-4 text-teal-600 rounded border-gray-300 focus:ring-teal-500"
                />
                <div>
                  <p className="text-sm font-medium text-gray-700">{rol.nombreRol}</p>
                  {rol.descripcion && <p className="text-xs text-gray-400">{rol.descripcion}</p>}
                </div>
              </label>
            ))}
          </div>
          <div className="flex gap-3">
            <button
              onClick={() => setRoleModal(null)}
              className="flex-1 py-2.5 border border-gray-200 text-gray-600 rounded-lg text-sm font-medium hover:bg-gray-50"
            >
              Cancelar
            </button>
            <button
              onClick={handleSaveRoles}
              disabled={savingRoles}
              className="flex-1 py-2.5 bg-teal-700 text-white rounded-lg text-sm font-medium hover:bg-teal-800 disabled:opacity-50 flex items-center justify-center gap-2"
            >
              {savingRoles && (
                <svg className="animate-spin w-4 h-4" fill="none" viewBox="0 0 24 24">
                  <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4" />
                  <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v4a4 4 0 00-4 4H4z" />
                </svg>
              )}
              Guardar
            </button>
          </div>
        </div>
      </Modal>
    </div>
  )
}
