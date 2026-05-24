import React from 'react'
import { Navigate } from 'react-router-dom'
import { useAuth } from '../../hooks/useAuth'

export default function RequireAccess({ roles = [], permisos = [], children }) {
  const { canAccess } = useAuth()

  if (!canAccess({ roles, permisos })) {
    return <Navigate to="/dashboard" replace />
  }

  return children
}
