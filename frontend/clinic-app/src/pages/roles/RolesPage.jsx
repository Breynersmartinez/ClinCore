import React, { useState, useEffect } from 'react'
import { getRoles, inactivarRol, getPermisos } from '../../api/rolesApi'
import { useAlert } from '../../components/ui/Alert'
import { useAuth } from '../../hooks/useAuth'
import { ADMIN_ROLES, isActivo } from '../../utils/access'
import Modal from '../../components/ui/Modal'
import Badge from '../../components/ui/Badge'
import Table from '../../components/ui/Table'
import RolForm from './RolForm'
import PermisosRolModal from '../../components/permisos/PermisosRolModal'

export default function RolesPage() {
  const { addToast } = useAlert()
  const { canAccess } = useAuth()
  const [roles, setRoles] = useState([])
  const [todosPermisos, setTodosPermisos] = useState([])
  const [loading, setLoading] = useState(true)
  const [showForm, setShowForm] = useState(false)
  const [editRol, setEditRol] = useState(null)
  const [permisosRol, setPermisosRol] = useState(null)

  const canCreate = canAccess({ roles: ADMIN_ROLES, permisos: ['ROLES_CREATE'] })
  const canUpdate = canAccess({ roles: ADMIN_ROLES, permisos: ['ROLES_UPDATE'] })
  const canDelete = canAccess({ roles: ADMIN_ROLES, permisos: ['ROLES_DELETE'] })

  const fetchData = async () => {
    setLoading(true)
    try {
      const [rolesRes, permRes] = await Promise.all([getRoles(), getPermisos()])
      setRoles(rolesRes.data?.data || [])
      setTodosPermisos(permRes.data?.data || [])
    } catch {
      addToast('Error al cargar roles', 'error')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => { fetchData() }, [])

  const handleInactivar = async (rol) => {
    if (!window.confirm(`¿Deseas inactivar el rol "${rol.nombreRol}"?`)) return
    try {
      await inactivarRol(rol.idRol)
      addToast('Rol inactivado', 'success')
      fetchData()
    } catch (err) {
      addToast(err.response?.data?.message || 'Error al inactivar', 'error')
    }
  }

  const columns = [
    {
      key: 'nombreRol',
      title: 'Nombre',
      render: (v, row) => (
        <div>
          <p className="font-medium text-gray-800">{v}</p>
          {row.descripcion && <p className="text-xs text-gray-400">{row.descripcion}</p>}
        </div>
      ),
    },
    {
      key: 'permisos',
      title: 'Permisos',
      render: (v) => (
        <span className="inline-flex items-center justify-center w-8 h-8 rounded-full bg-teal-50 text-teal-700 text-xs font-bold">
          {v?.length || 0}
        </span>
      ),
    },
    {
      key: 'activo',
      title: 'Estado',
      render: (v) => (
        <Badge estado={isActivo(v) ? 'activo' : 'inactivo'}>{isActivo(v) ? 'Activo' : 'Inactivo'}</Badge>
      ),
    },
    {
      key: 'acciones',
      title: 'Acciones',
      render: (_, row) => (
        <div className="flex gap-1.5">
          {canUpdate && (
            <>
              <button
                onClick={() => setPermisosRol(row)}
                className="px-2.5 py-1.5 text-xs font-medium bg-teal-50 text-teal-700 rounded-lg hover:bg-teal-100 transition-colors"
              >
                Permisos
              </button>
              <button
                onClick={() => { setEditRol(row); setShowForm(true) }}
                className="px-2.5 py-1.5 text-xs font-medium bg-blue-50 text-blue-700 rounded-lg hover:bg-blue-100 transition-colors"
              >
                Editar
              </button>
            </>
          )}
          {canDelete && isActivo(row.activo) && (
            <button
              onClick={() => handleInactivar(row)}
              className="px-2.5 py-1.5 text-xs font-medium bg-red-50 text-red-700 rounded-lg hover:bg-red-100 transition-colors"
            >
              Inactivar
            </button>
          )}
        </div>
      ),
    },
  ]

  return (
    <div>
      <div className="flex items-center justify-between mb-6">
        <div>
          <h1 className="text-2xl font-bold text-gray-800">Roles</h1>
          <p className="text-gray-400 text-sm mt-0.5">Gestión de roles y permisos</p>
        </div>
        {canCreate && (
          <button
            onClick={() => { setEditRol(null); setShowForm(true) }}
            className="flex items-center gap-2 px-4 py-2.5 bg-teal-700 text-white rounded-lg text-sm font-medium hover:bg-teal-800 transition-colors"
          >
            <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" />
            </svg>
            Nuevo Rol
          </button>
        )}
      </div>

      <Table columns={columns} data={roles} loading={loading} emptyMessage="No hay roles registrados" />

      <Modal
        isOpen={showForm}
        onClose={() => { setShowForm(false); setEditRol(null) }}
        title={editRol ? `Editar Rol — ${editRol.nombreRol}` : 'Nuevo Rol'}
      >
        <RolForm
          rol={editRol}
          onSuccess={() => { setShowForm(false); setEditRol(null); fetchData() }}
          onCancel={() => { setShowForm(false); setEditRol(null) }}
        />
      </Modal>

      <PermisosRolModal
        isOpen={!!permisosRol}
        onClose={() => setPermisosRol(null)}
        rol={permisosRol}
        todosLosPermisos={todosPermisos}
        onSuccess={fetchData}
      />
    </div>
  )
}
