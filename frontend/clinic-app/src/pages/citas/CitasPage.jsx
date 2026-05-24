import React, { useState, useEffect, useMemo } from 'react'
import { getCitas, cambiarEstadoCita, cancelarCita } from '../../api/citasApi'
import { useAlert } from '../../components/ui/Alert'
import { useAuth } from '../../hooks/useAuth'
import { ADMIN_ROLES, ASSIST_ROLES } from '../../utils/access'
import Modal from '../../components/ui/Modal'
import Badge from '../../components/ui/Badge'
import Table from '../../components/ui/Table'
import CitaForm from './CitaForm'

const ESTADOS = ['PROGRAMADA', 'CONFIRMADA', 'ATENDIDA', 'CANCELADA', 'NO_ASISTIO']
const ESTADO_LABELS = {
  PROGRAMADA: 'Programada',
  CONFIRMADA: 'Confirmada',
  ATENDIDA: 'Atendida',
  CANCELADA: 'Cancelada',
  NO_ASISTIO: 'No asistió',
}

export default function CitasPage() {
  const { addToast } = useAlert()
  const { canAccess } = useAuth()
  const [citas, setCitas] = useState([])
  const [loading, setLoading] = useState(true)
  const [showForm, setShowForm] = useState(false)
  const [filtroFecha, setFiltroFecha] = useState('')
  const [filtroEstado, setFiltroEstado] = useState('')
  const [filtroMedico, setFiltroMedico] = useState('')

  const canCreate = canAccess({ roles: ASSIST_ROLES, permisos: ['CITAS_CREATE'] })
  const canUpdate = canAccess({ roles: ['MEDICO', ...ASSIST_ROLES], permisos: ['CITAS_UPDATE'] })
  const canDelete = canAccess({ roles: ADMIN_ROLES, permisos: ['CITAS_DELETE'] })

  const fetchData = async () => {
    setLoading(true)
    try {
      const res = await getCitas()
      setCitas(res.data?.data || [])
    } catch {
      addToast('Error al cargar citas', 'error')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => { fetchData() }, [])

  const filtered = useMemo(() => {
    return citas.filter((c) => {
      if (filtroFecha && !c.fechaCita?.startsWith(filtroFecha)) return false
      if (filtroEstado && c.estado !== filtroEstado) return false
      if (filtroMedico) {
        const nombre = `${c.nombreMedico || c.medico?.nombre || ''} ${c.medico?.apellido || ''}`.toLowerCase()
        if (!nombre.includes(filtroMedico.toLowerCase())) return false
      }
      return true
    })
  }, [citas, filtroFecha, filtroEstado, filtroMedico])

  const handleCambioEstado = async (cita, nuevoEstado) => {
    try {
      await cambiarEstadoCita(cita.idCita, nuevoEstado)
      addToast(`Estado cambiado a ${ESTADO_LABELS[nuevoEstado]}`, 'success')
      fetchData()
    } catch (err) {
      addToast(err.response?.data?.message || 'Error', 'error')
    }
  }

  const handleCancelar = async (cita) => {
    const motivo = window.prompt('Motivo de cancelación:')
    if (motivo === null) return
    try {
      await cancelarCita(cita.idCita, motivo || 'Sin motivo')
      addToast('Cita cancelada', 'success')
      fetchData()
    } catch (err) {
      addToast(err.response?.data?.message || 'Error', 'error')
    }
  }

  const columns = [
    {
      key: 'idCita',
      title: '#',
      width: '60px',
      render: (v) => <span className="font-mono text-xs text-gray-400">{v}</span>,
    },
    {
      key: 'paciente',
      title: 'Paciente',
      render: (_, row) => (
        <span className="font-medium">
          {row.nombrePaciente || `${row.paciente?.nombre || ''} ${row.paciente?.apellido || ''}`}
        </span>
      ),
    },
    {
      key: 'medico',
      title: 'Médico',
      render: (_, row) => (
        <span>{row.nombreMedico || `Dr. ${row.medico?.nombre || ''} ${row.medico?.apellido || ''}`}</span>
      ),
    },
    {
      key: 'especialidad',
      title: 'Especialidad',
      render: (_, row) => row.especialidad || row.especialidad?.nombre || '-',
    },
    {
      key: 'fechaCita',
      title: 'Fecha',
      render: (v, row) => (
        <div>
          <p className="text-sm">{v}</p>
          <p className="text-xs text-gray-400">{row.horaInicio}</p>
        </div>
      ),
    },
    {
      key: 'estado',
      title: 'Estado',
      render: (v) => <Badge estado={v}>{ESTADO_LABELS[v] || v}</Badge>,
    },
    {
      key: 'acciones',
      title: 'Acciones',
      render: (_, row) => (
        <div className="flex gap-1.5 flex-wrap">
          {canUpdate && row.estado !== 'CANCELADA' && row.estado !== 'ATENDIDA' && (
            <select
              value=""
              onChange={(e) => e.target.value && handleCambioEstado(row, e.target.value)}
              className="px-2 py-1 text-xs border border-gray-200 rounded-lg bg-white focus:outline-none focus:ring-1 focus:ring-teal-500"
            >
              <option value="">Cambiar estado</option>
              {['CONFIRMADA', 'ATENDIDA', 'NO_ASISTIO'].map((e) => (
                <option key={e} value={e}>{ESTADO_LABELS[e]}</option>
              ))}
            </select>
          )}
          {canDelete && row.estado !== 'CANCELADA' && (
            <button
              onClick={() => handleCancelar(row)}
              className="px-2.5 py-1.5 text-xs font-medium bg-red-50 text-red-700 rounded-lg hover:bg-red-100"
            >
              Cancelar
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
          <h1 className="text-2xl font-bold text-gray-800">Citas</h1>
          <p className="text-gray-400 text-sm mt-0.5">Agenda y gestión de citas médicas</p>
        </div>
        {canCreate && (
          <button
            onClick={() => setShowForm(true)}
            className="flex items-center gap-2 px-4 py-2.5 bg-teal-700 text-white rounded-lg text-sm font-medium hover:bg-teal-800"
          >
            <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" />
            </svg>
            Nueva Cita
          </button>
        )}
      </div>

      {/* Filters */}
      <div className="flex flex-wrap gap-3 mb-4">
        <input
          type="date"
          value={filtroFecha}
          onChange={(e) => setFiltroFecha(e.target.value)}
          className="px-3 py-2.5 border border-gray-200 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-teal-500"
        />
        <select
          value={filtroEstado}
          onChange={(e) => setFiltroEstado(e.target.value)}
          className="px-3 py-2.5 border border-gray-200 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-teal-500 bg-white"
        >
          <option value="">Todos los estados</option>
          {ESTADOS.map((e) => <option key={e} value={e}>{ESTADO_LABELS[e]}</option>)}
        </select>
        <div className="relative">
          <svg className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
          </svg>
          <input
            type="text"
            value={filtroMedico}
            onChange={(e) => setFiltroMedico(e.target.value)}
            placeholder="Buscar médico..."
            className="pl-9 pr-4 py-2.5 border border-gray-200 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-teal-500"
          />
        </div>
        {(filtroFecha || filtroEstado || filtroMedico) && (
          <button
            onClick={() => { setFiltroFecha(''); setFiltroEstado(''); setFiltroMedico('') }}
            className="px-3 py-2.5 text-xs text-gray-500 hover:text-gray-700 border border-gray-200 rounded-lg hover:bg-gray-50"
          >
            Limpiar filtros
          </button>
        )}
      </div>

      <Table columns={columns} data={filtered} loading={loading} emptyMessage="No hay citas registradas" />

      <Modal isOpen={showForm} onClose={() => setShowForm(false)} title="Nueva Cita" size="max-w-2xl">
        <CitaForm
          onSuccess={() => { setShowForm(false); fetchData() }}
          onCancel={() => setShowForm(false)}
        />
      </Modal>
    </div>
  )
}
