import React, { useState } from 'react'
import { NavLink, useNavigate } from 'react-router-dom'
import { useAuth } from '../../hooks/useAuth'

const NAV_ITEMS = [
  {
    path: '/dashboard',
    label: 'Dashboard',
    requiredPermiso: null,
    requiredRoles: [],
    icon: (
      <svg className="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
          d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6" />
      </svg>
    ),
  },
  {
    path: '/usuarios',
    label: 'Usuarios',
    requiredPermiso: 'USUARIOS_READ',
    requiredRoles: ['ADMINISTRATIVO'],
    icon: (
      <svg className="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
          d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0z" />
      </svg>
    ),
  },
  {
    path: '/roles',
    label: 'Roles',
    requiredPermiso: 'ROLES_READ',
    requiredRoles: ['ADMINISTRATIVO'],
    icon: (
      <svg className="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
          d="M15 7a2 2 0 012 2m4 0a6 6 0 01-7.743 5.743L11 17H9v2H7v2H4a1 1 0 01-1-1v-2.586a1 1 0 01.293-.707l5.964-5.964A6 6 0 1121 9z" />
      </svg>
    ),
  },
  {
    path: '/consultorios',
    label: 'Consultorios',
    requiredPermiso: 'CONSULTORIOS_READ',
    requiredRoles: ['MEDICO', 'PACIENTE', 'ADMINISTRATIVO', 'AUXILIAR_MEDICO'],
    icon: (
      <svg className="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
          d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4" />
      </svg>
    ),
  },
  {
    path: '/medicos',
    label: 'Médicos',
    requiredPermiso: 'MEDICOS_READ',
    requiredRoles: ['MEDICO', 'PACIENTE', 'ADMINISTRATIVO', 'AUXILIAR_MEDICO'],
    icon: (
      <svg className="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
          d="M5.121 17.804A13.937 13.937 0 0112 16c2.5 0 4.847.655 6.879 1.804M15 10a3 3 0 11-6 0 3 3 0 016 0z" />
      </svg>
    ),
  },
  {
    path: '/pacientes',
    label: 'Pacientes',
    requiredPermiso: 'PACIENTES_READ',
    requiredRoles: ['MEDICO', 'PACIENTE', 'ADMINISTRATIVO', 'AUXILIAR_MEDICO'],
    icon: (
      <svg className="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
          d="M17 20h5v-2a3 3 0 00-5.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M15 7a3 3 0 11-6 0 3 3 0 016 0z" />
      </svg>
    ),
  },
  {
    path: '/citas',
    label: 'Citas',
    requiredPermiso: 'CITAS_READ',
    requiredRoles: ['MEDICO', 'PACIENTE', 'ADMINISTRATIVO', 'AUXILIAR_MEDICO'],
    icon: (
      <svg className="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
          d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
      </svg>
    ),
  },
]

export default function Sidebar({ collapsed, onCollapse }) {
  const { user, logout, canAccess } = useAuth()
  const navigate = useNavigate()

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  const visibleItems = NAV_ITEMS.filter((item) => {
    return canAccess({
      roles: item.requiredRoles || [],
      permisos: item.requiredPermiso ? [item.requiredPermiso] : [],
    })
  })

  return (
    <>
      {/* Mobile overlay */}
      {!collapsed && (
        <div
          className="fixed inset-0 bg-black/40 z-20 lg:hidden"
          onClick={onCollapse}
        />
      )}

      <aside
        className={`
          fixed top-0 left-0 h-full z-30
          bg-teal-700 text-white
          flex flex-col
          transition-all duration-300 ease-in-out
          ${collapsed ? '-translate-x-full lg:translate-x-0 lg:w-16' : 'translate-x-0 w-60'}
        `}
      >
        {/* Logo */}
        <div className="flex items-center gap-3 px-4 py-5 border-b border-teal-600">
          <div className="w-8 h-8 bg-white/20 rounded-lg flex items-center justify-center flex-shrink-0">
            <svg className="w-5 h-5" fill="currentColor" viewBox="0 0 24 24">
              <path d="M12 2L2 7l10 5 10-5-10-5zM2 17l10 5 10-5M2 12l10 5 10-5" />
            </svg>
          </div>
          {!collapsed && (
            <div className="overflow-hidden">
              <p className="font-semibold text-sm leading-tight">ClinicApp</p>
              <p className="text-teal-300 text-xs">Sistema de Gestión</p>
            </div>
          )}
        </div>

        {/* User info */}
        {!collapsed && (
          <div className="px-4 py-3 border-b border-teal-600 bg-teal-800/30">
            <div className="flex items-center gap-2">
              <div className="w-7 h-7 rounded-full bg-teal-500 flex items-center justify-center text-xs font-bold uppercase flex-shrink-0">
                {user?.username?.[0] || '?'}
              </div>
              <div className="overflow-hidden">
                <p className="text-sm font-medium truncate">{user?.username}</p>
                <p className="text-teal-300 text-xs truncate">
                  {user?.roles?.[0] || 'Sin rol'}
                </p>
              </div>
            </div>
          </div>
        )}

        {/* Navigation */}
        <nav className="flex-1 px-2 py-4 space-y-0.5 overflow-y-auto scrollbar-thin">
          {visibleItems.map((item) => (
            <NavLink
              key={item.path}
              to={item.path}
              className={({ isActive }) =>
                `flex items-center gap-3 px-3 py-2.5 rounded-lg text-sm font-medium transition-all
                ${isActive
                  ? 'bg-white/20 text-white'
                  : 'text-teal-100 hover:bg-white/10 hover:text-white'
                }`
              }
            >
              <span className="flex-shrink-0">{item.icon}</span>
              {!collapsed && <span className="truncate">{item.label}</span>}
            </NavLink>
          ))}
        </nav>

        {/* Logout */}
        <div className="px-2 py-3 border-t border-teal-600">
          <button
            onClick={handleLogout}
            className="flex items-center gap-3 w-full px-3 py-2.5 rounded-lg text-sm font-medium text-teal-100 hover:bg-white/10 hover:text-white transition-all"
          >
            <svg className="w-5 h-5 flex-shrink-0" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
                d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
            </svg>
            {!collapsed && <span>Cerrar Sesión</span>}
          </button>
        </div>
      </aside>
    </>
  )
}
