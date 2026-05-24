import React from 'react'
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import { AuthProvider } from './context/AuthContext'
import { AlertProvider } from './components/ui/Alert'
import PrivateRoute from './components/Layout/PrivateRoute'
import RequireAccess from './components/Layout/RequireAccess'
import { ADMIN_ROLES, AUTH_ROLES } from './utils/access'
import Login from './pages/Login'
import Dashboard from './pages/Dashboard'
import UsuariosPage from './pages/usuarios/UsuariosPage'
import RolesPage from './pages/roles/RolesPage'
import ConsultoriosPage from './pages/consultorios/ConsultoriosPage'
import CitasPage from './pages/citas/CitasPage'
import MedicosPage from './pages/medicos/MedicosPage'
import PacientesPage from './pages/pacientes/PacientesPage'

export default function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <AlertProvider>
          <Routes>
            {/* Public */}
            <Route path="/login" element={<Login />} />

            {/* Private */}
            <Route element={<PrivateRoute />}>
              <Route path="/dashboard" element={<Dashboard />} />
              <Route path="/usuarios" element={
                <RequireAccess roles={ADMIN_ROLES} permisos={['USUARIOS_READ']}>
                  <UsuariosPage />
                </RequireAccess>
              } />
              <Route path="/roles" element={
                <RequireAccess roles={ADMIN_ROLES} permisos={['ROLES_READ']}>
                  <RolesPage />
                </RequireAccess>
              } />
              <Route path="/consultorios" element={
                <RequireAccess roles={AUTH_ROLES} permisos={['CONSULTORIOS_READ']}>
                  <ConsultoriosPage />
                </RequireAccess>
              } />
              <Route path="/medicos" element={
                <RequireAccess roles={AUTH_ROLES} permisos={['MEDICOS_READ']}>
                  <MedicosPage />
                </RequireAccess>
              } />
              <Route path="/pacientes" element={
                <RequireAccess roles={AUTH_ROLES} permisos={['PACIENTES_READ']}>
                  <PacientesPage />
                </RequireAccess>
              } />
              <Route path="/citas" element={
                <RequireAccess roles={AUTH_ROLES} permisos={['CITAS_READ']}>
                  <CitasPage />
                </RequireAccess>
              } />
            </Route>

            {/* Redirect */}
            <Route path="/" element={<Navigate to="/dashboard" replace />} />
            <Route path="*" element={<Navigate to="/dashboard" replace />} />
          </Routes>
        </AlertProvider>
      </AuthProvider>
    </BrowserRouter>
  )
}
