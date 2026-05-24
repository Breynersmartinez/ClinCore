export const AUTH_ROLES = ['MEDICO', 'PACIENTE', 'ADMINISTRATIVO', 'AUXILIAR_MEDICO']
export const ADMIN_ROLES = ['ADMINISTRATIVO']
export const ASSIST_ROLES = ['ADMINISTRATIVO', 'AUXILIAR_MEDICO']

export function isActivo(value) {
  return value === true || value === 'S' || value === 'ACTIVO'
}
