import React from 'react'
import { useAuth } from '../../hooks/useAuth'
import Badge from '../ui/Badge'

export default function Navbar({ onToggleSidebar, sidebarCollapsed }) {
  const { user } = useAuth()

  return (
    <header className="h-14 bg-white border-b border-gray-200 flex items-center justify-between px-4 flex-shrink-0 sticky top-0 z-10">
      <div className="flex items-center gap-3">
        {/* Hamburger */}
        <button
          onClick={onToggleSidebar}
          className="w-8 h-8 flex items-center justify-center rounded-lg text-gray-500 hover:bg-gray-100 transition-colors"
        >
          <svg className="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 6h16M4 12h16M4 18h16" />
          </svg>
        </button>
        <div className="hidden sm:flex items-center gap-2 text-xs text-gray-400">
          <span className="text-gray-300">|</span>
          <span>Sistema de Gestión Médica</span>
        </div>
      </div>

      <div className="flex items-center gap-3">
        {user?.roles?.map((role) => (
          <Badge key={role} role={role}>
            {role}
          </Badge>
        ))}
        <div className="flex items-center gap-2">
          <div className="w-8 h-8 rounded-full bg-teal-700 flex items-center justify-center text-white text-xs font-semibold uppercase">
            {user?.username?.[0] || '?'}
          </div>
          <span className="text-sm font-medium text-gray-700 hidden sm:block">
            {user?.username}
          </span>
        </div>
      </div>
    </header>
  )
}
