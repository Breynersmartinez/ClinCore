import React, { useState, useEffect } from 'react'
import Modal from '../ui/Modal'
import Toggle from '../ui/Toggle'
import { useAlert } from '../ui/Alert'
import { updateRolPermisos } from '../../api/rolesApi'
import {
  MODULOS,
  ACCIONES,
  ACCION_LABELS,
  MODULO_LABELS,
  buildPermisosMatrix,
} from '../../utils/permisos'

export default function PermisosRolModal({ isOpen, onClose, rol, todosLosPermisos = [], onSuccess }) {
  const { addToast } = useAlert()
  const [matrix, setMatrix] = useState({}) // { MODULO: { ACCION: bool } }
  const [permisoMap, setPermisoMap] = useState({}) // { MODULO: { ACCION: idPermiso } }
  const [saving, setSaving] = useState(false)

  useEffect(() => {
    if (!isOpen || !rol) return

    // Build id matrix from all permisos
    const idMatrix = buildPermisosMatrix(todosLosPermisos)
    setPermisoMap(idMatrix)

    // Build checked state from rol's current permisos
    const activeIds = new Set((rol.permisos || []).map((p) => p.idPermiso))
    const initialMatrix = {}
    for (const mod of MODULOS) {
      initialMatrix[mod] = {}
      for (const acc of ACCIONES) {
        const pid = idMatrix[mod]?.[acc]
        initialMatrix[mod][acc] = pid !== null && activeIds.has(pid)
      }
    }
    setMatrix(initialMatrix)
  }, [isOpen, rol, todosLosPermisos])

  const handleToggle = (modulo, accion) => {
    setMatrix((prev) => ({
      ...prev,
      [modulo]: {
        ...prev[modulo],
        [accion]: !prev[modulo][accion],
      },
    }))
  }

  const handleToggleAll = (modulo) => {
    const allOn = ACCIONES.every((acc) => matrix[modulo]?.[acc])
    setMatrix((prev) => ({
      ...prev,
      [modulo]: ACCIONES.reduce((acc, a) => {
        acc[a] = !allOn
        return acc
      }, {}),
    }))
  }

  const handleSave = async () => {
    setSaving(true)
    try {
      // Collect all active permiso IDs
      const selectedIds = []
      for (const mod of MODULOS) {
        for (const acc of ACCIONES) {
          if (matrix[mod]?.[acc]) {
            const pid = permisoMap[mod]?.[acc]
            if (pid !== null && pid !== undefined) {
              selectedIds.push(pid)
            }
          }
        }
      }

      await updateRolPermisos(rol.idRol, selectedIds)
      addToast(`Permisos del rol "${rol.nombreRol}" actualizados correctamente`, 'success')
      onSuccess?.()
      onClose()
    } catch (error) {
      const msg = error.response?.data?.message || 'Error al guardar permisos'
      addToast(msg, 'error')
    } finally {
      setSaving(false)
    }
  }

  return (
    <Modal
      isOpen={isOpen}
      onClose={onClose}
      title={`Permisos Rol — ${rol?.nombreRol || ''}`}
      size="max-w-3xl"
    >
      <div className="px-6 py-4">
        <div className="overflow-x-auto rounded-xl border border-gray-200">
          <table className="min-w-full">
            <thead>
              <tr className="bg-gray-50 border-b border-gray-200">
                <th className="px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider w-8">#</th>
                <th className="px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">Módulo</th>
                {ACCIONES.map((acc) => (
                  <th key={acc} className="px-4 py-3 text-center text-xs font-semibold text-gray-500 uppercase tracking-wider">
                    {ACCION_LABELS[acc]}
                  </th>
                ))}
                <th className="px-4 py-3 text-center text-xs font-semibold text-gray-500 uppercase tracking-wider">Todo</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-50 bg-white">
              {MODULOS.map((mod, idx) => (
                <tr key={mod} className="hover:bg-gray-50 transition-colors">
                  <td className="px-4 py-3 text-sm text-gray-400 font-mono">{idx + 1}</td>
                  <td className="px-4 py-3">
                    <span className="text-sm font-medium text-gray-700">
                      {MODULO_LABELS[mod] || mod}
                    </span>
                    <span className="ml-2 text-xs text-gray-400 font-mono">{mod}</span>
                  </td>
                  {ACCIONES.map((acc) => {
                    const hasPermission = permisoMap[mod]?.[acc] !== null && permisoMap[mod]?.[acc] !== undefined
                    return (
                      <td key={acc} className="px-4 py-3 text-center">
                        {hasPermission ? (
                          <div className="flex justify-center">
                            <Toggle
                              checked={matrix[mod]?.[acc] || false}
                              onChange={() => handleToggle(mod, acc)}
                              label={`${ACCION_LABELS[acc]} ${MODULO_LABELS[mod]}`}
                            />
                          </div>
                        ) : (
                          <span className="text-gray-200 text-lg">—</span>
                        )}
                      </td>
                    )
                  })}
                  <td className="px-4 py-3 text-center">
                    <button
                      type="button"
                      onClick={() => handleToggleAll(mod)}
                      className="text-xs text-teal-600 hover:text-teal-800 font-medium hover:underline transition-colors"
                    >
                      {ACCIONES.every((acc) => matrix[mod]?.[acc]) ? 'Quitar' : 'Todos'}
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        {/* Summary */}
        <div className="mt-3 flex items-center gap-2">
          <span className="text-xs text-gray-400">
            {MODULOS.reduce(
              (total, mod) =>
                total + ACCIONES.filter((acc) => matrix[mod]?.[acc]).length,
              0
            )}{' '}
            permisos seleccionados
          </span>
        </div>
      </div>

      {/* Footer */}
      <div className="px-6 py-4 border-t border-gray-100 flex items-center justify-end gap-3">
        <button
          type="button"
          onClick={onClose}
          disabled={saving}
          className="px-5 py-2.5 text-sm font-medium text-white bg-red-500 hover:bg-red-600 rounded-lg transition-colors disabled:opacity-50"
        >
          Salir
        </button>
        <button
          type="button"
          onClick={handleSave}
          disabled={saving}
          className="px-5 py-2.5 text-sm font-medium text-white bg-green-500 hover:bg-green-600 rounded-lg transition-colors disabled:opacity-50 flex items-center gap-2"
        >
          {saving && (
            <svg className="animate-spin w-4 h-4" fill="none" viewBox="0 0 24 24">
              <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4" />
              <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v4a4 4 0 00-4 4H4z" />
            </svg>
          )}
          Guardar
        </button>
      </div>
    </Modal>
  )
}
