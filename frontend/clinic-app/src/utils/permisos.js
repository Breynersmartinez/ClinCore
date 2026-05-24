export const MODULOS = [
  'DEPARTAMENTOS',
  'MUNICIPIOS',
  'SEDES',
  'CONSULTORIOS',
  'PERSONAS',
  'MEDICOS',
  'PACIENTES',
  'CITAS',
  'USUARIOS',
  'ROLES',
  'AUDITORIA',
]

export const ACCIONES = ['READ', 'CREATE', 'UPDATE', 'DELETE']

export const ACCION_LABELS = {
  READ: 'Ver',
  CREATE: 'Crear',
  UPDATE: 'Actualizar',
  DELETE: 'Eliminar',
}

// Build permission key: CONSULTORIOS_CREATE
export function buildPermisoKey(modulo, accion) {
  return `${modulo}_${accion}`
}

// From roles permisos array, build a Set of keys like CONSULTORIOS_CREATE
export function buildPermisosSet(permisos = []) {
  return new Set(
    permisos
      .map((p) => {
        if (typeof p === 'string') return p
        if (p?.modulo && p?.accion) return buildPermisoKey(p.modulo, p.accion)
        if (p?.nombrePermiso) return p.nombrePermiso
        return null
      })
      .filter(Boolean)
  )
}

// Check if a permisos array contains a specific key
export function hasPermisoInArray(permisos = [], key) {
  const set = buildPermisosSet(permisos)
  return set.has(key)
}

// Build matrix: { CONSULTORIOS: { CREATE: idPermiso | null, ... }, ... }
export function buildPermisosMatrix(todosLosPermisos = []) {
  const matrix = {}
  for (const mod of MODULOS) {
    matrix[mod] = {}
    for (const acc of ACCIONES) {
      matrix[mod][acc] = null
    }
  }
  for (const p of todosLosPermisos) {
    if (matrix[p.modulo] !== undefined) {
      matrix[p.modulo][p.accion] = p.idPermiso
    }
  }
  return matrix
}

export const MODULO_LABELS = {
  DEPARTAMENTOS: 'Departamentos',
  MUNICIPIOS: 'Municipios',
  SEDES: 'Sedes',
  CONSULTORIOS: 'Consultorios',
  PERSONAS: 'Personas',
  MEDICOS: 'Médicos',
  PACIENTES: 'Pacientes',
  CITAS: 'Citas',
  USUARIOS: 'Usuarios',
  ROLES: 'Roles',
  AUDITORIA: 'Auditoría',
}
