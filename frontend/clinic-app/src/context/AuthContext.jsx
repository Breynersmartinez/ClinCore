import React, { createContext, useState, useEffect, useCallback } from 'react'
import { login as loginApi } from '../api/authApi'
import { buildPermisosSet } from '../utils/permisos'

export const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => {
    try {
      const stored = localStorage.getItem('user')
      if (!stored) return null
      const parsed = JSON.parse(stored)
      return {
        ...parsed,
        permisosSet: [...buildPermisosSet(parsed.permisos || parsed.permisosSet || [])],
      }
    } catch {
      return null
    }
  })
  const [accessToken, setAccessToken] = useState(
    () => localStorage.getItem('accessToken') || null
  )
  const [loading, setLoading] = useState(false)

  // Listen for forced logout from axios interceptor
  useEffect(() => {
    const handler = () => logout()
    window.addEventListener('auth:logout', handler)
    return () => window.removeEventListener('auth:logout', handler)
  }, [])

  const login = useCallback(async (username, password) => {
    setLoading(true)
    try {
      const res = await loginApi({ username, password })
      const { accessToken, refreshToken, roles, permisos } = res.data.data

      const userData = {
        username,
        roles: roles || [],
        permisos: permisos || [],
        permisosSet: [...buildPermisosSet(permisos || [])],
      }

      localStorage.setItem('accessToken', accessToken)
      if (refreshToken) localStorage.setItem('refreshToken', refreshToken)
      localStorage.setItem('user', JSON.stringify(userData))

      setAccessToken(accessToken)
      setUser(userData)
      return { success: true }
    } catch (error) {
      const message =
        error.response?.data?.message || 'Credenciales inválidas'
      return { success: false, message }
    } finally {
      setLoading(false)
    }
  }, [])

  const logout = useCallback(() => {
    localStorage.removeItem('accessToken')
    localStorage.removeItem('refreshToken')
    localStorage.removeItem('user')
    setAccessToken(null)
    setUser(null)
  }, [])

  const hasPermiso = useCallback(
    (key) => {
      if (!user?.permisosSet) return false
      return user.permisosSet.includes(key)
    },
    [user]
  )

  const hasRole = useCallback(
    (role) => {
      if (!user?.roles) return false
      return user.roles.includes(role)
    },
    [user]
  )

  const hasAnyRole = useCallback(
    (roles = []) => roles.length === 0 || roles.some((role) => hasRole(role)),
    [hasRole]
  )

  const hasAnyPermiso = useCallback(
    (permisos = []) => permisos.length === 0 || permisos.some((permiso) => hasPermiso(permiso)),
    [hasPermiso]
  )

  const canAccess = useCallback(
    ({ roles = [], permisos = [] } = {}) => hasAnyRole(roles) && hasAnyPermiso(permisos),
    [hasAnyRole, hasAnyPermiso]
  )

  const value = {
    user,
    accessToken,
    loading,
    isAuthenticated: !!accessToken,
    login,
    logout,
    hasPermiso,
    hasRole,
    hasAnyRole,
    hasAnyPermiso,
    canAccess,
  }

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}
