import React, { useState, useEffect, useMemo } from 'react'
import axiosInstance from '../../api/axiosConfig'
import { useAlert } from '../../components/ui/Alert'
import { useAuth } from '../../hooks/useAuth'
import { ADMIN_ROLES, isActivo } from '../../utils/access'
import Modal from '../../components/ui/Modal'
import Badge from '../../components/ui/Badge'
import Table from '../../components/ui/Table'
import ConsultorioForm from './ConsultorioForm'

export default function ConsultoriosPage() {
  const { addToast } = useAlert()
  const { canAccess, hasPermiso } = useAuth()
  const [consultorios, setConsultorios] = useState([])
  const [sedes, setSedes] = useState([])
  const [loading, setLoading] = useState(true)
  const [sedeFilter, setSedeFilter] = useState('')
  const [showForm, setShowForm] = useState(false)
  const [editItem, setEditItem] = useState(null)

  const canCreate = canAccess({ roles: ADMIN_ROLES, permisos: ['CONSULTORIOS_CREATE'] })
  const canUpdate = canAccess({ roles: ADMIN_ROLES, permisos: ['CONSULTORIOS_UPDATE'] })
  const canDelete = canAccess({ roles: ADMIN_ROLES, permisos: ['CONSULTORIOS_DELETE'] })
  const canReadSedes = hasPermiso('SEDES_READ')

  const fetchData = async () => {
    setLoading(true)
    try {
      const [consRes, sedesRes] = await Promise.all([
        axiosInstance.get('/consultorios'),
        canReadSedes ? axiosInstance.get('/sedes') : Promise.resolve(null),
      ])
      setConsultorios(consRes.data?.data || [])
      setSedes(sedesRes?.data?.data || [])
    } catch {
      addToast('Error al cargar consultorios', 'error')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => { fetchData() }, [canReadSedes])

  const filtered = useMemo(() => {
    if (!sedeFilter) return consultorios
    return consultorios.filter((c) => String(c.idSede) === sedeFilter)
  }, [consultorios, sedeFilter])

  const handleDelete = async (item) => {
    if (!window.confirm(`¿Inactivar el consultorio "${item.nombreConsultorio}"?`)) return
    try {
      await axiosInstance.delete(`/consultorios/${item.idConsultorio}`)
      addToast('Consultorio inactivado', 'success')
      fetchData()
    } catch (err) {
      addToast(err.response?.data?.message || 'Error', 'error')
    }
  }

  const columns = [
    { key: 'codigoConsultorio', title: 'Código', render: (v) => <span className="font-mono text-xs">{v}</span> },
    {
      key: 'nombreConsultorio',
      title: 'Nombre',
      render: (v, row) => (
        <div>
          <p className="font-medium text-gray-800">{v}</p>
          <p className="text-xs text-gray-400">Piso {row.numeroPiso || '-'}</p>
        </div>
      ),
    },
    { key: 'capacidad', title: 'Capacidad', render: (v) => v ?? '—' },
    {
      key: 'sede',
      title: 'Sede',
      render: (_, row) => {
        const sede = sedes.find((s) => s.idSede === row.idSede)
        return <span className="text-sm text-gray-600">{row.nombreSede || sede?.nombreSede || '-'}</span>
      },
    },
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
            <button
              onClick={() => { setEditItem(row); setShowForm(true) }}
              className="px-2.5 py-1.5 text-xs font-medium bg-blue-50 text-blue-700 rounded-lg hover:bg-blue-100"
            >
              Editar
            </button>
          )}
          {canDelete && isActivo(row.activo) && (
            <button
              onClick={() => handleDelete(row)}
              className="px-2.5 py-1.5 text-xs font-medium bg-red-50 text-red-700 rounded-lg hover:bg-red-100"
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
          <h1 className="text-2xl font-bold text-gray-800">Consultorios</h1>
          <p className="text-gray-400 text-sm mt-0.5">Gestión de consultorios médicos</p>
        </div>
        {canCreate && (
          <button
            onClick={() => { setEditItem(null); setShowForm(true) }}
            className="flex items-center gap-2 px-4 py-2.5 bg-teal-700 text-white rounded-lg text-sm font-medium hover:bg-teal-800"
          >
            <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" />
            </svg>
            Nuevo Consultorio
          </button>
        )}
      </div>

      {/* Filters */}
      {canReadSedes && (
      <div className="mb-4">
        <select
          value={sedeFilter}
          onChange={(e) => setSedeFilter(e.target.value)}
          className="px-3 py-2.5 border border-gray-200 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-teal-500 bg-white min-w-48"
        >
          <option value="">Todas las sedes</option>
          {sedes.map((s) => (
            <option key={s.idSede} value={s.idSede}>{s.nombreSede || s.nombre}</option>
          ))}
        </select>
      </div>
      )}

      <Table columns={columns} data={filtered} loading={loading} emptyMessage="No hay consultorios registrados" />

      <Modal
        isOpen={showForm}
        onClose={() => { setShowForm(false); setEditItem(null) }}
        title={editItem ? 'Editar Consultorio' : 'Nuevo Consultorio'}
      >
        <ConsultorioForm
          consultorio={editItem}
          onSuccess={() => { setShowForm(false); setEditItem(null); fetchData() }}
          onCancel={() => { setShowForm(false); setEditItem(null) }}
        />
      </Modal>
    </div>
  )
}
