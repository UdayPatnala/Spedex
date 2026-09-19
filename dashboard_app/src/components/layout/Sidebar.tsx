import type { ViewId } from "../../types";
import { BrandLockup } from "./BrandLockup";

export const navItems: Array<{ id: ViewId; label: string; icon: string }> = [
  { id: "home", label: "Overview", icon: "home" },
  { id: "payments", label: "UPI Desk", icon: "wallet" },
  { id: "trips", label: "Trips", icon: "flight_takeoff" },
  { id: "analytics", label: "Signals", icon: "insights" },
  { id: "budget", label: "Budgets", icon: "calendar_month" },
  { id: "settings", label: "Profile", icon: "settings" },
  { id: "privacy", label: "Privacy & DPDP", icon: "shield" },
  { id: "landing", label: "Landing Page", icon: "public" },
];

export function Sidebar({
  activeView,
  onSelect,
}: {
  activeView: ViewId;
  onSelect: (view: ViewId) => void;
}) {
  return (
    <aside className="sidebar">
      <BrandLockup />

      <div className="sidebar-panel">
        <p className="eyebrow">Workspace</p>
        <strong>Personal Wallet</strong>
        <span className="subtle">1-tap utility payments, smart budgeting, and financial analytics.</span>
      </div>

      <nav className="sidebar-nav">
        {navItems.map((item) => (
          <button
            key={item.id}
            className={`nav-button ${item.id === activeView ? "active" : ""}`}
            onClick={() => onSelect(item.id)}
            type="button"
          >
            <span className="material-symbols-outlined">{item.icon}</span>
            <span>{item.label}</span>
          </button>
        ))}
      </nav>

      <a href="/spedex.apk" download className="sidebar-cta" style={{ textDecoration: 'none', textAlign: 'center' }}>
        Download Mobile APK
      </a>
    </aside>
  );
}
