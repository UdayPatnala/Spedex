import React from 'react';

export type TabId = 'landing' | 'overview' | 'trips' | 'vendors' | 'budget' | 'analytics' | 'reminders' | 'settings';

export interface SidebarProps {
  activeTab: TabId;
  onTabChange: (tab: TabId) => void;
  activeTripName?: string | null;
  showMobileSync?: boolean;
  onToggleMobileSync?: () => void;
}

const NAV_ITEMS: Array<{ id: TabId; label: string; icon: string }> = [
  { id: 'overview', label: 'Overview', icon: '📊' },
  { id: 'trips', label: 'Trips Ledger', icon: '✈️' },
  { id: 'vendors', label: 'Quick Pay Vendors', icon: '⚡' },
  { id: 'budget', label: 'Budgets', icon: '🎯' },
  { id: 'analytics', label: 'Analytics', icon: '📈' },
  { id: 'reminders', label: 'Reminders', icon: '🔔' },
  { id: 'settings', label: 'Settings', icon: '⚙️' },
  { id: 'landing', label: 'Landing Page', icon: '🌐' },
];

export const Sidebar: React.FC<SidebarProps> = ({
  activeTab,
  onTabChange,
  activeTripName,
  showMobileSync,
  onToggleMobileSync,
}) => {
  return (
    <aside
      style={{
        width: '240px',
        backgroundColor: 'rgba(15, 23, 42, 0.7)',
        backdropFilter: 'blur(16px)',
        borderRight: '1px solid rgba(255, 255, 255, 0.08)',
        padding: '24px 16px',
        display: 'flex',
        flexDirection: 'column',
        gap: '8px',
      }}
    >
      <div style={{ padding: '0 12px 16px 12px', borderBottom: '1px solid rgba(255, 255, 255, 0.08)', marginBottom: '8px' }}>
        <span style={{ fontSize: '11px', fontWeight: 700, textTransform: 'uppercase', letterSpacing: '1px', color: '#64748B' }}>
          Navigation Menu
        </span>
      </div>

      {NAV_ITEMS.map((item) => {
        const isActive = activeTab === item.id;
        return (
          <button
            key={item.id}
            onClick={() => onTabChange(item.id)}
            style={{
              display: 'flex',
              alignItems: 'center',
              gap: '12px',
              padding: '12px 16px',
              borderRadius: '12px',
              border: 'none',
              backgroundColor: isActive ? 'rgba(56, 189, 248, 0.15)' : 'transparent',
              color: isActive ? '#38BDF8' : '#94A3B8',
              fontWeight: isActive ? 700 : 500,
              fontSize: '14px',
              cursor: 'pointer',
              textAlign: 'left',
              transition: 'all 0.2s ease',
              outline: 'none',
            }}
          >
            <span style={{ fontSize: '16px' }}>{item.icon}</span>
            <span>{item.label}</span>
            {item.id === 'trips' && activeTripName && (
              <span
                style={{
                  marginLeft: 'auto',
                  width: '8px',
                  height: '8px',
                  borderRadius: '50%',
                  backgroundColor: '#10B981',
                  boxShadow: '0 0 8px #10B981',
                }}
              />
            )}
          </button>
        );
      })}
    </aside>
  );
};
