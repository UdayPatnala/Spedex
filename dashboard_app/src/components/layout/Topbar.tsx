import type { SpedexUser } from "../../types";
import { monthYear } from "../../utils/formatters";
import { BrandLockup } from "./BrandLockup";

export function Topbar({
  query,
  onQueryChange,
  user,
  onSignOut,
}: {
  query: string;
  onQueryChange: (value: string) => void;
  user: SpedexUser | null;
  onSignOut: () => void;
}) {
  return (
    <header className="topbar">
      <div className="topbar-main">
        <BrandLockup compact />
        <div className="searchbar">
          <span className="material-symbols-outlined">search</span>
          <input
            aria-label="Search transactions"
            placeholder="Search payees, reminders, categories, or accounts..."
            value={query}
            onChange={(event) => onQueryChange(event.target.value)}
          />
        </div>
      </div>

      <div className="topbar-actions">
        <span className="status-chip">UPI Ready</span>
        {user ? (
          <>
            <span className="status-chip soft">Member since {monthYear(user.member_since)}</span>
            <button className="signout-button" type="button" onClick={onSignOut}>
              Sign out
            </button>
            <div className="profile-chip">
              <div className="profile-meta">
                <strong>{user.name}</strong>
                <small>{user.plan}</small>
              </div>
              <div className="profile-avatar">{user.avatar_initials}</div>
            </div>
          </>
        ) : (
          <span className="status-chip soft">Connecting...</span>
        )}
      </div>
    </header>
  );
}
