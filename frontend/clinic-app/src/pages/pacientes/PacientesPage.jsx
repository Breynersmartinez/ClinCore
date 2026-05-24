import React, { useEffect, useMemo, useState } from 'react'
import { getPacientes, inactivarPaciente } from '../../api/pacientesApi'
import { useAlert } from '../../components/ui/Alert'
import { useAuth } from '../../hooks/useAuth'
import { ADMIN_ROLES, ASSIST_ROLES, AUTH_ROLES, isActivo } from '../../utils/access'
import Badge from '../../components/ui/Badge'
import Modal from '../../components/ui/Modal'
import Table from '../../components/ui/Table'
import PacienteForm from './PacienteForm'

export default function PacientesPage() {
  const { addToast } = useAlert()
  const { canAccess } = useAuth()
  const [pacientes, setPacientes] = useState([])
  const [loading, setLoading] = useState(true)
  const [search, setSearch] = useState('')
  const [showForm, setShowForm] = useState(false)
  const [editItem, setEditItem] = useState(null)

  const canRead = canAccess({ roles: AUTH_ROLES, permisos: ['PACIENTES_READ'] })
  const canCreate = canAccess({ roles: ASSIST_ROLES, permisos: ['PACIENTES_CREATE'] })
  const canUpdate = canAccess({ roles: ADMIN_ROLES, permisos: ['PACIENTES_UPDATE'] })
  const canDelete = canAccess({ roles: ADMIN_ROLES, permisos: ['PACIENTES_DELETE'] })

  const fetchData = async () => {
    if (!canRead) return
    setLoading(true)
    try {
      const res = await getPacientes()
      setPacientes(res.data?.data || [])
    } catch {
      addToast('Error al cargar pacientes', 'error')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => { fetchData() }, [canRead])

  const filtered = useMemo(() => {
    const term = search.trim().toLowerCase()
    if (!term) return pacientes
    return pacientes.filter((p) =>
      `${p.nombreCompleto || ''} ${p.numeroDocumento || ''} ${p.numeroHistoria || ''}`
        .toLowerCase()
        .includes(term)
    )
  }, [pacientes, search])

  const handleDelete = async (paciente) => {
    if (!window.confirm(`¿Inactivar al paciente "${paciente.nombreCompleto}"?`)) return
    try {
      await inactivarPaciente(paciente.idPaciente)
      addToast('Paciente inactivado', 'success')
      fetchData()
    } catch (err) {
      addToast(err.response?.data?.message || 'Error al inactivar paciente', 'error')
    }
  }

  const columns = [
    {
      key: 'nombreCompleto',
      title: 'Paciente',
      render: (v, row) => (
        <div>
          <p className="font-medium text-gray-800">{v}</p>
          <p className="text-xs text-gray-400">{row.tipoDocumento} {row.numeroDocumento}</p>
        </div>
      ),
    },
    { key: 'numeroHistoria', title: 'Historia', render: (v) => <span className="font-mono text-xs">{v}</span> },
    { key: 'tipoAfiliacion', title: 'Afiliación' },
    { key: 'eps', title: 'EPS', render: (v) => v || '-' },
    {
      key: 'activo',
      title: 'Estado',
      render: (v) => <Badge estado={isActivo(v) ? 'activo' : 'inactivo'}>{isActivo(v) ? 'Activo' : 'Inactivo'}</Badge>,
    },
    {
      key: 'acciones',
      title: 'Acciones',
      render: (_, row) => (
        <div className="flex gap-1.5">
          {canUpdate && (
            <button onClick={() => { setEditItem(row); setShowForm(true) }}
              className="px-2.5 py-1.5 text-xs font-medium bg-blue-50 text-blue-700 rounded-lg hover:bg-blue-100">
              Editar
            </button>
          )}
          {canDelete && isActivo(row.activo) && (
            <button onClick={() => handleDelete(row)}
              className="px-2.5 py-1.5 text-xs font-medium bg-red-50 text-red-700 rounded-lg hover:bg-red-100">
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
          <h1 className="text-2xl font-bold text-gray-800">Pacientes</h1>
          <p className="text-gray-400 text-sm mt-0.5">Gestión de pacientes</p>
        </div>
        {canCreate && (
          <button onClick={() => { setEditItem(null); setShowForm(true) }}
            className="flex items-center gap-2 px-4 py-2.5 bg-teal-700 text-white rounded-lg text-sm font-medium hover:bg-teal-800">
            Nuevo Paciente
          </button>
        )}
      </div>

      <div className="mb-4 max-w-sm">
        <input type="text" value={search} onChange={(e) => setSearch(e.target.value)}
          placeholder="Buscar paciente..."
          className="w-full px-3 py-2.5 border border-gray-200 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-teal-500" />
      </div>

      <Table columns={columns} data={filtered} loading={loading} emptyMessage="No hay pacientes registrados" />

      <Modal isOpen={showForm}
        onClose={() => { setShowForm(false); setEditItem(null) }}
        title={editItem ? 'Editar Paciente' : 'Nuevo Paciente'}>
        <PacienteForm
          paciente={editItem}
          onSuccess={() => { setShowForm(false); setEditItem(null); fetchData() }}
          onCancel={() => { setShowForm(false); setEditItem(null) }}
        />
      </Modal>
    </div>
  )
}
