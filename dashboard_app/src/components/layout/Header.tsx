import React from 'react';
import type { SpedexUser } from '../../types';

export interface HeaderProps {
  user: SpedexUser | null;
  selectedTheme: string;
  onSelectTheme: (themeId: string) => void;
  onLogout: () => void;
}

const THEME_OPTIONS = [
  { id: 'theme-2000', label: '₹2000 Magenta', color: '#E91E63' },
  { id: 'theme-500', label: '₹500 Stone Grey', color: '#607D8B' },
  { id: 'theme-200', label: '₹200 Bright Yellow', color: '#FFC107' },
  { id: 'theme-100', label: '₹100 Lavender', color: '#9C27B0' },
  { id: 'theme-50', label: '₹50 Cyan', color: '#00BCD4' },
  { id: 'theme-20', label: '₹20 Greenish Yellow', color: '#8BC34A' },
  { id: 'theme-10', label: '₹10 Chocolate', color: '#795548' },
];

export const Header: React.FC<HeaderProps> = ({
  user,
  selectedTheme,
  onSelectTheme,
  onLogout,
}) => {
  return (
    <header className="header-container" style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '16px 32px', borderBottom: '1px solid rgba(255, 255, 255, 0.1)', background: 'rgba(15, 23, 42, 0.6)', backdropFilter: 'blur(12px)' }}>
      <div style={{ display: 'flex', alignItems: 'center', gap: '16px' }}>
        <h1 style={{ margin: 0, fontSize: '22px', fontWeight: 700, background: 'linear-gradient(135deg, #38BDF8 0%, #818CF8 100%)', WebkitBackgroundClip: 'text', WebkitTextFillColor: 'transparent' }}>
          SpeDex Smart Wallet
        </h1>
        <span style={{ fontSize: '12px', padding: '4px 10px', borderRadius: '12px', background: 'rgba(56, 189, 248, 0.15)', color: '#38BDF8', border: '1px solid rgba(56, 189, 248, 0.3)', fontWeight: 600 }}>
          v2.0 Enterprise
        </span>
      </div>

      <div style={{ display: 'flex', alignItems: 'center', gap: '20px' }}>
        {/* Theme Selector Dropdown */}
        <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
          <label htmlFor="theme-select" style={{ fontSize: '13px', color: '#94A3B8', fontWeight: 500 }}>Theme:</label>
          <select
            id="theme-select"
            value={selectedTheme}
            onChange={(e) => onSelectTheme(e.target.value)}
            style={{
              padding: '6px 12px',
              borderRadius: '8px',
              backgroundColor: 'rgba(30, 41, 59, 0.8)',
              color: '#F8FAFC',
              border: '1px solid rgba(255, 255, 255, 0.15)',
              fontSize: '13px',
              cursor: 'pointer',
              outline: 'none',
            }}
          >
            {THEME_OPTIONS.map((t) => (
              <option key={t.id} value={t.id}>
                {t.label}
              </option>
            ))}
          </select>
        </div>

        {/* User Badge & Logout */}
        {user && (
          <div style={{ display: 'flex', alignItems: 'center', gap: '12px', borderLeft: '1px solid rgba(255, 255, 255, 0.1)', paddingLeft: '16px' }}>
            <div style={{ width: '36px', height: '36px', borderRadius: '50%', background: 'linear-gradient(135deg, #6366F1 0%, #A855F7 100%)', display: 'flex', alignItems: 'center', justifyContent: 'center', fontWeight: 700, color: '#FFF' }}>
              {user.name ? user.name.charAt(0).toUpperCase() : 'U'}
            </div>
            <div style={{ display: 'flex', flexDirection: 'column' }}>
              <span style={{ fontSize: '14px', fontWeight: 600, color: '#F8FAFC' }}>{user.name || 'User'}</span>
              <span style={{ fontSize: '12px', color: '#94A3B8' }}>{user.email}</span>
            </div>
            <button
              onClick={onLogout}
              style={{
                marginLeft: '8px',
                padding: '6px 12px',
                borderRadius: '8px',
                background: 'rgba(239, 68, 68, 0.15)',
                color: '#F87171',
                border: '1px solid rgba(239, 68, 68, 0.3)',
                fontSize: '13px',
                cursor: 'pointer',
                fontWeight: 500,
                transition: 'all 0.2s ease',
              }}
            >
              Logout
            </button>
          </div>
        )}
      </div>
    </header>
  );
};
