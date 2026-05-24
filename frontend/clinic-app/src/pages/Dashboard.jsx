import React, { useState, useEffect } from 'react'
import axiosInstance from '../api/axiosConfig'
import { useAuth } from '../hooks/useAuth'
import { isActivo } from '../utils/access'

function StatCard({ title, value, icon, color, subtitle }) {
  return (
    <div className="bg-white rounded-xl border border-gray-100 shadow-sm p-5 flex items-start gap-4 hover:shadow-md transition-shadow">
      <div className={`w-12 h-12 rounded-xl flex items-center justify-center flex-shrink-0 ${color}`}>
        {icon}
      </div>
      <div>
        <p className="text-sm text-gray-500 font-medium">{title}</p>
        <p className="text-2xl font-bold text-gray-800 mt-0.5">{value ?? '—'}</p>
        {subtitle && <p className="text-xs text-gray-400 mt-0.5">{subtitle}</p>}
      </div>
    </div>
  )
}

export default function Dashboard() {
  const { user } = useAuth()
  const [stats, setStats] = useState({
    totalCitas: null,
    citasPendientes: null,
    medicosActivos: null,
    totalPacientes: null,
  })

  useEffect(() => {
    const fetchStats = async () => {
      try {
        const [citasRes, medicosRes, pacientesRes] = await Promise.allSettled([
          axiosInstance.get('/citas'),
          axiosInstance.get('/medicos'),
          axiosInstance.get('/pacientes'),
        ])

        const citas = citasRes.status === 'fulfilled' ? citasRes.value.data?.data || [] : []
        const medicos = medicosRes.status === 'fulfilled' ? medicosRes.value.data?.data || [] : []
        const pacientes = pacientesRes.status === 'fulfilled' ? pacientesRes.value.data?.data || [] : []

        const today = new Date().toISOString().split('T')[0]
        const citasHoy = citas.filter((c) => c.fechaCita?.startsWith(today) || c.fecha?.startsWith(today))
        const citasPendientes = citas.filter(
          (c) => c.estado === 'PROGRAMADA' || c.estado === 'CONFIRMADA'
        )

        setStats({
          totalCitas: citasHoy.length,
          citasPendientes: citasPendientes.length,
          medicosActivos: medicos.filter((m) => isActivo(m.activo)).length,
          totalPacientes: pacientes.length,
        })
      } catch {
        // silently fail
      }
    }

    fetchStats()
  }, [])

  const today = new Date().toLocaleDateString('es-CO', {
    weekday: 'long',
    year: 'numeric',
    month: 'long',
    day: 'numeric',
  })

  return (
    <div>
      {/* Header */}
      <div className="mb-8">
        <h1 className="text-2xl font-bold text-gray-800">
          Buen día, {user?.username} 👋
        </h1>
        <p className="text-gray-400 text-sm mt-1 capitalize">{today}</p>
      </div>

      {/* Stats */}
      <div className="grid grid-cols-1 sm:grid-cols-2 xl:grid-cols-4 gap-4 mb-8">
        <StatCard
          title="Citas de hoy"
          value={stats.totalCitas}
          subtitle="Programadas para hoy"
          color="bg-blue-50"
          icon={
            <svg className="w-6 h-6 text-blue-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
                d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
            </svg>
          }
        />
        <StatCard
          title="Citas pendientes"
          value={stats.citasPendientes}
          subtitle="Por confirmar o atender"
          color="bg-yellow-50"
          icon={
            <svg className="w-6 h-6 text-yellow-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
                d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
          }
        />
        <StatCard
          title="Médicos activos"
          value={stats.medicosActivos}
          subtitle="Personal disponible"
          color="bg-teal-50"
          icon={
            <svg className="w-6 h-6 text-teal-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
                d="M5.121 17.804A13.937 13.937 0 0112 16c2.5 0 4.847.655 6.879 1.804M15 10a3 3 0 11-6 0 3 3 0 016 0z" />
            </svg>
          }
        />
        <StatCard
          title="Total pacientes"
          value={stats.totalPacientes}
          subtitle="Registrados en el sistema"
          color="bg-purple-50"
          icon={
            <svg className="w-6 h-6 text-purple-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
                d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0z" />
            </svg>
          }
        />
      </div>

      {/* Welcome card */}
      <div className="bg-gradient-to-r from-teal-700 to-teal-600 rounded-2xl p-6 text-white">
        <div className="flex items-start justify-between">
          <div>
            <h2 className="text-lg font-semibold">Panel de Control</h2>
            <p className="text-teal-200 text-sm mt-1 max-w-sm">
              Gestiona citas, consultorios, usuarios y más desde este panel centralizado.
            </p>
          </div>
          <div className="w-16 h-16 bg-white/10 rounded-2xl flex items-center justify-center">
            <svg className="w-8 h-8 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5}
                d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
            </svg>
          </div>
        </div>
        <div className="mt-4 flex gap-2 flex-wrap">
          {user?.roles?.map((role) => (
            <span key={role} className="bg-white/20 text-white text-xs font-medium px-3 py-1 rounded-full">
              {role}
            </span>
          ))}
        </div>
      </div>
    </div>
  )
}
