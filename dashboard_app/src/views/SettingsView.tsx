import { useState } from "react";
import { downloadTransactionsCsv, exportUserData, updateProfile } from "../api";
import type { DashboardOverview } from "../types";

export function SettingsView({
  overview,
  onRefresh,
  onNavigateToPrivacy,
}: {
  overview: DashboardOverview;
  onRefresh: () => Promise<void>;
  onNavigateToPrivacy?: () => void;
}) {
  const [isEditing, setIsEditing] = useState(false);
  const [editName, setEditName] = useState(overview.user.name);
  const [editPic, setEditPic] = useState(overview.user.profile_picture_url || "");
  const [saving, setSaving] = useState(false);

  // Data Safety & Accidental Data Loss Prevention state
  const [exportingJson, setExportingJson] = useState(false);
  const [exportingCsv, setExportingCsv] = useState(false);
  const [feedbackMsg, setFeedbackMsg] = useState<string | null>(null);

  // Preference switches state
  const [preferences, setPreferences] = useState({
    paymentAlerts: true,
    deviceLock: true,
    weeklyDigest: false,
  });

  const togglePreference = (key: keyof typeof preferences) => {
    setPreferences((prev) => {
      const next = { ...prev, [key]: !prev[key] };
      setFeedbackMsg(`Updated ${key} preference.`);
      return next;
    });
  };

  async function handleSave() {
    setSaving(true);
    try {
      await updateProfile({ name: editName, profile_picture_url: editPic });
      await onRefresh();
      setIsEditing(false);
      setFeedbackMsg("Profile updated successfully.");
    } catch (e) {
      alert("Failed to update profile.");
    } finally {
      setSaving(false);
    }
  }

  async function handleExportJson() {
    setExportingJson(true);
    setFeedbackMsg(null);
    try {
      const data = await exportUserData();
      const blob = new Blob([JSON.stringify(data, null, 2)], { type: "application/json" });
      const url = URL.createObjectURL(blob);
      const a = document.createElement("a");
      a.href = url;
      a.download = `spedex-backup-${new Date().toISOString().slice(0, 10)}.json`;
      a.click();
      URL.revokeObjectURL(url);
      setFeedbackMsg("Complete account backup exported successfully (DPDP Section 11).");
    } catch (e: any) {
      alert(e.message || "Failed to export JSON backup.");
    } finally {
      setExportingJson(false);
    }
  }

  async function handleExportCsv() {
    setExportingCsv(true);
    setFeedbackMsg(null);
    try {
      await downloadTransactionsCsv();
      setFeedbackMsg("Transaction ledger CSV exported successfully.");
    } catch (e: any) {
      alert(e.message || "Failed to export CSV.");
    } finally {
      setExportingCsv(false);
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

        {feedbackMsg && (
          <div style={{ padding: "10px 14px", background: "rgba(5, 150, 105, 0.1)", border: "1px solid rgba(5, 150, 105, 0.3)", borderRadius: "8px", color: "#10b981", fontSize: "13px", marginBottom: "16px" }}>
            ✓ {feedbackMsg}
          </div>
        )}

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
            ) : (
              <div className="profile-meta-static">
                <strong>{overview.user.name}</strong>
                <p className="subtle">{overview.user.email}</p>
                <span className="status-chip soft">{overview.user.plan}</span>
              </div>
            )}
          </div>
        </div>

        {/* Security & Notification Preferences */}
        <div className="settings-list" style={{ marginTop: 32, borderTop: "1px solid var(--border-color)", paddingTop: 24 }}>
          <h3 className="section-title" style={{ fontSize: "18px", marginBottom: 16 }}>Preferences & Safeguards</h3>
          {[
            {
              id: "paymentAlerts" as const,
              title: "Payment alerts",
              subtitle: "Get a nudge for every large transfer or AutoPay reminder.",
              enabled: preferences.paymentAlerts,
            },
            {
              id: "deviceLock" as const,
              title: "Device lock",
              subtitle: "Use biometrics before sensitive UPI or bank actions.",
              enabled: preferences.deviceLock,
            },
            {
              id: "weeklyDigest" as const,
              title: "Weekly digest",
              subtitle: "Receive a soft snapshot of spend, trends, and buffers.",
              enabled: preferences.weeklyDigest,
            },
          ].map(({ id, title, subtitle, enabled }) => (
            <div key={title} className="setting-row">
              <div className="icon-badge accent-rose">
                <span className="emoji-glyph">✨</span>
              </div>
              <div style={{ flex: 1 }}>
                <p className="setting-title">{title}</p>
                <span className="subtle">{subtitle}</span>
              </div>
              <button
                type="button"
                className={`status-chip ${enabled ? "" : "soft"}`}
                onClick={() => togglePreference(id)}
                style={{ cursor: "pointer", border: "none" }}
              >
                {enabled ? "Enabled" : "Disabled"}
              </button>
            </div>
          ))}
        </div>

        {/* Accidental Data Loss Prevention & Data Management Hub */}
        <div style={{ marginTop: 32, borderTop: "1px solid var(--border-color)", paddingTop: 24 }}>
          <div className="section-header" style={{ marginBottom: 12 }}>
            <div>
              <div style={{ display: "flex", alignItems: "center", gap: 8 }}>
                <span style={{ fontSize: "16px" }}>🛡️</span>
                <p className="eyebrow" style={{ color: "var(--primary)" }}>Accidental Data Loss Prevention</p>
              </div>
              <h3 className="section-title" style={{ fontSize: "18px", margin: "4px 0" }}>Offline Backups & Data Safety Hub</h3>
            </div>
            <span className="status-chip soft">DPDP Section 11</span>
          </div>

          <p className="subtle" style={{ fontSize: "13px", lineHeight: 1.5, marginBottom: 18 }}>
            Protect your personal financial history against accidental loss. You can download comprehensive offline backups of your transactions, categories, budgets, and saved vendors at any time.
          </p>

          <div style={{ display: "flex", gap: 12, flexWrap: "wrap", alignItems: "center" }}>
            <button
              type="button"
              className="status-chip"
              onClick={handleExportJson}
              disabled={exportingJson}
              style={{ cursor: "pointer", border: "none", padding: "8px 16px" }}
            >
              {exportingJson ? "Generating Backup..." : "📥 Download Full Backup (JSON)"}
            </button>

            <button
              type="button"
              className="status-chip soft"
              onClick={handleExportCsv}
              disabled={exportingCsv}
              style={{ cursor: "pointer", border: "none", padding: "8px 16px" }}
            >
              {exportingCsv ? "Exporting CSV..." : "📄 Export Ledger (CSV)"}
            </button>

            {onNavigateToPrivacy && (
              <button
                type="button"
                className="status-chip soft"
                onClick={onNavigateToPrivacy}
                style={{ cursor: "pointer", border: "none", padding: "8px 16px", color: "var(--primary)" }}
              >
                ⚙️ Privacy & Erasure Controls
              </button>
            )}
          </div>
        </div>
      </section>
    </div>
  );
}
