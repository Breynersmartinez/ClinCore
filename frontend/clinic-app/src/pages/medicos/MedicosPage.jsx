import React, { useEffect, useMemo, useState } from 'react'
import { getMedicos, inactivarMedico } from '../../api/medicosApi'
import { useAlert } from '../../components/ui/Alert'
import { useAuth } from '../../hooks/useAuth'
import { ADMIN_ROLES, AUTH_ROLES, isActivo } from '../../utils/access'
import Badge from '../../components/ui/Badge'
import Modal from '../../components/ui/Modal'
import Table from '../../components/ui/Table'
import MedicoForm from './MedicoForm'

export default function MedicosPage() {
  const { addToast } = useAlert()
  const { canAccess } = useAuth()
  const [medicos, setMedicos] = useState([])
  const [loading, setLoading] = useState(true)
  const [search, setSearch] = useState('')
  const [showForm, setShowForm] = useState(false)
  const [editItem, setEditItem] = useState(null)

  const canRead = canAccess({ roles: AUTH_ROLES, permisos: ['MEDICOS_READ'] })
  const canCreate = canAccess({ roles: ADMIN_ROLES, permisos: ['MEDICOS_CREATE'] })
  const canUpdate = canAccess({ roles: ADMIN_ROLES, permisos: ['MEDICOS_UPDATE'] })
  const canDelete = canAccess({ roles: ADMIN_ROLES, permisos: ['MEDICOS_DELETE'] })

  const fetchData = async () => {
    if (!canRead) return
    setLoading(true)
    try {
      const res = await getMedicos()
      setMedicos(res.data?.data || [])
    } catch {
      addToast('Error al cargar médicos', 'error')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => { fetchData() }, [canRead])

  const filtered = useMemo(() => {
    const term = search.trim().toLowerCase()
    if (!term) return medicos
    return medicos.filter((m) =>
      `${m.nombreCompleto || ''} ${m.numeroDocumento || ''} ${m.nombreEspecialidad || ''} ${m.numeroRegistro || ''}`
        .toLowerCase()
        .includes(term)
    )
  }, [medicos, search])

  const handleDelete = async (medico) => {
    if (!window.confirm(`¿Inactivar al médico "${medico.nombreCompleto}"?`)) return
    try {
      await inactivarMedico(medico.idMedico)
      addToast('Médico inactivado', 'success')
      fetchData()
    } catch (err) {
      addToast(err.response?.data?.message || 'Error al inactivar médico', 'error')
    }
  }

  const columns = [
    {
      key: 'nombreCompleto',
      title: 'Médico',
      render: (v, row) => (
        <div>
          <p className="font-medium text-gray-800">Dr(a). {v}</p>
          <p className="text-xs text-gray-400">{row.tipoDocumento} {row.numeroDocumento}</p>
        </div>
      ),
    },
    { key: 'nombreEspecialidad', title: 'Especialidad' },
    { key: 'numeroRegistro', title: 'Registro', render: (v) => <span className="font-mono text-xs">{v}</span> },
    { key: 'tarifaConsulta', title: 'Tarifa', render: (v) => v != null ? `$${Number(v).toLocaleString('es-CO')}` : '-' },
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
          <h1 className="text-2xl font-bold text-gray-800">Médicos</h1>
          <p className="text-gray-400 text-sm mt-0.5">Gestión de médicos</p>
        </div>
        {canCreate && (
          <button onClick={() => { setEditItem(null); setShowForm(true) }}
            className="flex items-center gap-2 px-4 py-2.5 bg-teal-700 text-white rounded-lg text-sm font-medium hover:bg-teal-800">
            Nuevo Médico
          </button>
        )}
      </div>

      <div className="mb-4 max-w-sm">
        <input type="text" value={search} onChange={(e) => setSearch(e.target.value)}
          placeholder="Buscar médico..."
          className="w-full px-3 py-2.5 border border-gray-200 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-teal-500" />
      </div>

      <Table columns={columns} data={filtered} loading={loading} emptyMessage="No hay médicos registrados" />

      <Modal isOpen={showForm}
        onClose={() => { setShowForm(false); setEditItem(null) }}
        title={editItem ? 'Editar Médico' : 'Nuevo Médico'}>
        <MedicoForm
          medico={editItem}
          onSuccess={() => { setShowForm(false); setEditItem(null); fetchData() }}
          onCancel={() => { setShowForm(false); setEditItem(null) }}
        />
      </Modal>
    </div>
  )
}
