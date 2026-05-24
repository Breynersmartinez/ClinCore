import React from 'react'

const VARIANTS = {
  default: 'bg-gray-100 text-gray-700',
  blue: 'bg-blue-100 text-blue-700',
  green: 'bg-green-100 text-green-700',
  red: 'bg-red-100 text-red-700',
  yellow: 'bg-yellow-100 text-yellow-700',
  teal: 'bg-teal-100 text-teal-700',
  purple: 'bg-purple-100 text-purple-700',
  orange: 'bg-orange-100 text-orange-700',
}

const ROLE_COLORS = {
  MEDICO: 'blue',
  PACIENTE: 'green',
  ADMINISTRATIVO: 'purple',
  AUXILIAR_MEDICO: 'orange',
}

const ESTADO_COLORS = {
  PROGRAMADA: 'blue',
  CONFIRMADA: 'green',
  ATENDIDA: 'default',
  CANCELADA: 'red',
  NO_ASISTIO: 'yellow',
  activo: 'green',
  inactivo: 'red',
}

export default function Badge({ children, variant = 'default', role, estado }) {
  let colorKey = variant
  if (role && ROLE_COLORS[role]) colorKey = ROLE_COLORS[role]
  if (estado && ESTADO_COLORS[estado]) colorKey = ESTADO_COLORS[estado]

  const classes = VARIANTS[colorKey] || VARIANTS.default

  return (
    <span
      className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${classes}`}
    >
      {children}
    </span>
  )
}
