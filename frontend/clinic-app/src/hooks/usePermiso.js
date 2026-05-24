import { useAuth } from './useAuth'

export function usePermiso(permisoKey) {
  const { hasPermiso } = useAuth()
  return hasPermiso(permisoKey)
}

export function usePermisos(permisoKeys = []) {
  const { hasPermiso } = useAuth()
  return permisoKeys.reduce((acc, key) => {
    acc[key] = hasPermiso(key)
    return acc
  }, {})
}
