import React from 'react'

export default function Toggle({ checked, onChange, disabled = false, label }) {
  return (
    <button
      type="button"
      role="switch"
      aria-checked={checked}
      aria-label={label}
      disabled={disabled}
      onClick={() => !disabled && onChange(!checked)}
      className={`
        relative inline-flex items-center flex-shrink-0
        w-11 h-6 rounded-full
        transition-colors duration-200 ease-in-out
        focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-1
        ${checked ? 'bg-blue-600' : 'bg-gray-300'}
        ${disabled ? 'opacity-50 cursor-not-allowed' : 'cursor-pointer'}
      `}
    >
      <span
        className={`
          inline-block w-[18px] h-[18px] bg-white rounded-full shadow-md
          transform transition-transform duration-200 ease-in-out
          ${checked ? 'translate-x-[22px]' : 'translate-x-[3px]'}
        `}
      />
    </button>
  )
}
