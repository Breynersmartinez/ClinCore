import React, { createContext, useContext, useState, useCallback, useEffect } from 'react'

const AlertContext = createContext(null)

export function AlertProvider({ children }) {
  const [toasts, setToasts] = useState([])

  const addToast = useCallback((message, type = 'success', duration = 4000) => {
    const id = Date.now() + Math.random()
    setToasts((prev) => [...prev, { id, message, type }])
    setTimeout(() => {
      setToasts((prev) => prev.filter((t) => t.id !== id))
    }, duration)
  }, [])

  const removeToast = useCallback((id) => {
    setToasts((prev) => prev.filter((t) => t.id !== id))
  }, [])

  return (
    <AlertContext.Provider value={{ addToast }}>
      {children}
      <ToastContainer toasts={toasts} onRemove={removeToast} />
    </AlertContext.Provider>
  )
}

export function useAlert() {
  const context = useContext(AlertContext)
  if (!context) throw new Error('useAlert must be used within AlertProvider')
  return context
}

const TOAST_STYLES = {
  success: {
    bg: 'bg-white border-l-4 border-green-500',
    icon: (
      <div className="w-8 h-8 bg-green-100 rounded-full flex items-center justify-center flex-shrink-0">
        <svg className="w-4 h-4 text-green-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2.5} d="M5 13l4 4L19 7" />
        </svg>
      </div>
    ),
    text: 'text-green-700',
  },
  error: {
    bg: 'bg-white border-l-4 border-red-500',
    icon: (
      <div className="w-8 h-8 bg-red-100 rounded-full flex items-center justify-center flex-shrink-0">
        <svg className="w-4 h-4 text-red-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2.5} d="M6 18L18 6M6 6l12 12" />
        </svg>
      </div>
    ),
    text: 'text-red-700',
  },
  warning: {
    bg: 'bg-white border-l-4 border-yellow-500',
    icon: (
      <div className="w-8 h-8 bg-yellow-100 rounded-full flex items-center justify-center flex-shrink-0">
        <svg className="w-4 h-4 text-yellow-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2.5} d="M12 9v4m0 4h.01M10.29 3.86L1.82 18a2 2 0 001.71 3h16.94a2 2 0 001.71-3L13.71 3.86a2 2 0 00-3.42 0z" />
        </svg>
      </div>
    ),
    text: 'text-yellow-700',
  },
  info: {
    bg: 'bg-white border-l-4 border-blue-500',
    icon: (
      <div className="w-8 h-8 bg-blue-100 rounded-full flex items-center justify-center flex-shrink-0">
        <svg className="w-4 h-4 text-blue-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2.5} d="M13 16h-1v-4h-1m1-4h.01M12 2a10 10 0 110 20A10 10 0 0112 2z" />
        </svg>
      </div>
    ),
    text: 'text-blue-700',
  },
}

function ToastContainer({ toasts, onRemove }) {
  return (
    <div className="fixed top-4 right-4 z-[9999] flex flex-col gap-2 max-w-sm w-full">
      {toasts.map((toast) => {
        const style = TOAST_STYLES[toast.type] || TOAST_STYLES.info
        return (
          <div
            key={toast.id}
            className={`flex items-start gap-3 px-4 py-3 rounded-lg shadow-lg ${style.bg} animate-slide-in`}
            style={{ animation: 'slideIn 0.3s ease-out' }}
          >
            {style.icon}
            <p className={`flex-1 text-sm font-medium ${style.text}`}>
              {toast.message}
            </p>
            <button
              onClick={() => onRemove(toast.id)}
              className="text-gray-400 hover:text-gray-600 flex-shrink-0 mt-0.5"
            >
              <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
              </svg>
            </button>
          </div>
        )
      })}
      <style>{`
        @keyframes slideIn {
          from { transform: translateX(100%); opacity: 0; }
          to { transform: translateX(0); opacity: 1; }
        }
      `}</style>
    </div>
  )
}
