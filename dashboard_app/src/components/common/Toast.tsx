import React, { useEffect } from 'react';

export interface ToastProps {
  message: string;
  type?: 'success' | 'error' | 'info';
  onClose: () => void;
  durationMs?: number;
}

export const Toast: React.FC<ToastProps> = ({
  message,
  type = 'info',
  onClose,
  durationMs = 4000,
}) => {
  useEffect(() => {
    const timer = setTimeout(onClose, durationMs);
    return () => clearTimeout(timer);
  }, [onClose, durationMs]);

  const bgStyle: React.CSSProperties = {
    position: 'fixed',
    bottom: '24px',
    right: '24px',
    zIndex: 9999,
    padding: '12px 20px',
    borderRadius: '10px',
    boxShadow: '0 10px 25px rgba(0, 0, 0, 0.4)',
    display: 'flex',
    alignItems: 'center',
    gap: '12px',
    fontSize: '14px',
    fontWeight: 500,
    color: '#FFFFFF',
    backgroundColor: type === 'success' ? '#059669' : type === 'error' ? '#DC2626' : '#2563EB',
    border: '1px solid rgba(255, 255, 255, 0.2)',
    backdropFilter: 'blur(10px)',
  };

  return (
    <div role="alert" aria-live="polite" style={bgStyle}>
      <span>{message}</span>
      <button
        onClick={onClose}
        style={{ background: 'none', border: 'none', color: '#FFFFFF', cursor: 'pointer', opacity: 0.8, fontSize: '16px' }}
        aria-label="Close notification"
      >
        ✕
      </button>
    </div>
  );
};
