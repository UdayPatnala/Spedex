import { useState } from "react";
import { updateProfile } from "../api";
import type { DashboardOverview } from "../types";

export function SettingsView({
  overview,
  onRefresh,
}: {
  overview: DashboardOverview;
  onRefresh: () => Promise<void>;
}) {
  const [isEditing, setIsEditing] = useState(false);
  const [editName, setEditName] = useState(overview.user.name);
  const [editPic, setEditPic] = useState(overview.user.profile_picture_url || "");
  const [saving, setSaving] = useState(false);

  async function handleSave() {
    setSaving(true);
    try {
      await updateProfile({ name: editName, profile_picture_url: editPic });
      await onRefresh();
      setIsEditing(false);
    } catch (e) {
      alert("Failed to update profile.");
    } finally {
      setSaving(false);
    }
  }

  return (
    <div className="settings-shell">
      <section className="card">
        <div className="section-header">
          <div>
            <p className="eyebrow">Identity</p>
            <h2 className="page-title section-title">Profile</h2>
          </div>
          <button className="status-chip" onClick={() => (isEditing ? handleSave() : setIsEditing(true))} disabled={saving}>
            {saving ? "Saving..." : isEditing ? "Save Changes" : "Edit Profile"}
          </button>
        </div>

        <div className="profile-edit-grid">
          <div className="profile-avatar-large">
            {overview.user.profile_picture_url ? (
              <img src={overview.user.profile_picture_url} alt={overview.user.name} />
            ) : (
              <span>{overview.user.avatar_initials}</span>
            )}
          </div>
          <div className="profile-form">
            {isEditing ? (
              <>
                <div className="auth-form" style={{ gap: 12 }}>
                  <div className="input-field">
                    <label className="eyebrow">Display Name</label>
                    <input value={editName} onChange={(e) => setEditName(e.target.value)} placeholder="Full name" />
                  </div>
                  <div className="input-field">
                    <label className="eyebrow">Profile Picture URL</label>
                    <input value={editPic} onChange={(e) => setEditPic(e.target.value)} placeholder="https://..." />
                  </div>
                </div>
              </>
            ) : (
              <div className="profile-meta-static">
                <strong>{overview.user.name}</strong>
                <p className="subtle">{overview.user.email}</p>
                <span className="status-chip soft">{overview.user.plan}</span>
              </div>
            )}
          </div>
        </div>

        <div className="settings-list" style={{ marginTop: 32, borderTop: "1px solid var(--border-color)", paddingTop: 24 }}>
          {[
            ["Payment alerts", "Get a nudge for every large transfer or AutoPay reminder."],
            ["Device lock", "Use biometrics before sensitive UPI or bank actions."],
            ["Weekly digest", "Receive a soft snapshot of spend, trends, and buffers."],
          ].map(([title, subtitle]) => (
            <div key={title} className="setting-row">
              <div className="icon-badge accent-rose">
                <span className="emoji-glyph">✨</span>
              </div>
              <div>
                <p className="setting-title">{title}</p>
                <span className="subtle">{subtitle}</span>
              </div>
              <span className="status-chip">Enabled</span>
            </div>
          ))}
        </div>
      </section>
    </div>
  );
}
