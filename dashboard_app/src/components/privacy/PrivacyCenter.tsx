import React, { useState, useEffect } from "react";
import {
  getPrivacySettings,
  updatePrivacyConsents,
  requestGuardianConsent,
  exportUserData,
  submitGrievance,
  getGrievances,
  eraseUserData,
} from "../../api";
import type { PrivacySettings, PrivacyGrievance, LegalDocType } from "../../types";

export interface PrivacyCenterProps {
  onOpenDoc: (doc: LegalDocType) => void;
  onLogout?: () => void;
}

export const PrivacyCenter: React.FC<PrivacyCenterProps> = ({ onOpenDoc, onLogout }) => {
  const [settings, setSettings] = useState<PrivacySettings | null>(null);
  const [grievances, setGrievances] = useState<PrivacyGrievance[]>([]);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [message, setMessage] = useState<{ type: "success" | "error"; text: string } | null>(null);

  // Minor guardian form
  const [guardianName, setGuardianName] = useState("");
  const [guardianEmail, setGuardianEmail] = useState("");

  // Grievance form
  const [grievanceCat, setGrievanceCat] = useState("CONSENT_REVOCATION");
  const [grievanceDesc, setGrievanceDesc] = useState("");

  // Erasure confirmation modal
  const [showErasureModal, setShowErasureModal] = useState(false);
  const [confirmText, setConfirmText] = useState("");

  const loadData = async () => {
    setLoading(true);
    try {
      const [s, g] = await Promise.all([getPrivacySettings(), getGrievances()]);
      setSettings(s);
      setGrievances(g);
    } catch (e: any) {
      setMessage({ type: "error", text: e.message || "Failed to load privacy settings" });
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  const handleToggle = async (key: "analyticsConsent" | "marketingConsent") => {
    if (!settings || settings.isMinor) return;
    const newSettings = {
      analyticsConsent: key === "analyticsConsent" ? !settings.analyticsConsent : settings.analyticsConsent,
      marketingConsent: key === "marketingConsent" ? !settings.marketingConsent : settings.marketingConsent,
    };

    setSaving(true);
    try {
      const updated = await updatePrivacyConsents(newSettings);
      setSettings(updated);
      setMessage({ type: "success", text: "Consent preferences updated and logged per DPDP Act standards." });
    } catch (e: any) {
      setMessage({ type: "error", text: e.message || "Failed to update consents" });
    } finally {
      setSaving(false);
    }
  };

  const handleGuardianRequest = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!guardianName.trim() || !guardianEmail.trim()) return;

    setSaving(true);
    try {
      const res = await requestGuardianConsent({ guardianName, guardianEmail });
      setMessage({ type: "success", text: res.message || "Parental consent request sent." });
      await loadData();
    } catch (e: any) {
      setMessage({ type: "error", text: e.message || "Failed to submit guardian consent request" });
    } finally {
      setSaving(false);
    }
  };

  const handleExport = async () => {
    setSaving(true);
    try {
      const exportData = await exportUserData();
      const blob = new Blob([JSON.stringify(exportData, null, 2)], { type: "application/json" });
      const url = URL.createObjectURL(blob);
      const a = document.createElement("a");
      a.href = url;
      a.download = `spedex-data-export-${new Date().toISOString().slice(0, 10)}.json`;
      a.click();
      URL.revokeObjectURL(url);
      setMessage({ type: "success", text: "Personal data exported successfully (DPDP Act Section 11)." });
    } catch (e: any) {
      setMessage({ type: "error", text: e.message || "Failed to export data" });
    } finally {
      setSaving(false);
    }
  };

  const handleGrievance = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!grievanceDesc.trim()) return;

    setSaving(true);
    try {
      const g = await submitGrievance({ category: grievanceCat, description: grievanceDesc });
      setGrievanceDesc("");
      setMessage({ type: "success", text: `Grievance ticket ${g.ticketId} logged. DPO SLA is 48 hours acknowledgement.` });
      await loadData();
    } catch (e: any) {
      setMessage({ type: "error", text: e.message || "Failed to submit grievance" });
    } finally {
      setSaving(false);
    }
  };

  const handleErasure = async () => {
    if (confirmText !== "DELETE MY DATA") return;
    setSaving(true);
    try {
      await eraseUserData({ confirmationText: confirmText });
      alert("Your account and all personal data have been erased and anonymized per DPDP Act Section 12.");
      if (onLogout) {
        onLogout();
      } else {
        window.location.reload();
      }
    } catch (e: any) {
      setMessage({ type: "error", text: e.message || "Failed to erase account" });
      setSaving(false);
    }
  };

  if (loading) {
    return (
      <div className="p-8 text-center text-gray-400">
        <div className="w-8 h-8 border-2 border-[#58A6FF] border-t-transparent rounded-full animate-spin mx-auto mb-3"></div>
        Loading Privacy Center...
      </div>
    );
  }

  return (
    <div className="space-y-8 max-w-4xl mx-auto pb-12 font-sans">
      {/* Header */}
      <div className="bg-[#161B22] border border-[#30363D] p-6 rounded-2xl shadow-xl">
        <div className="flex items-center justify-between">
          <div>
            <h2 className="text-2xl font-serif font-bold text-white tracking-wide">
              Privacy & DPDP Compliance Center
            </h2>
            <p className="text-xs text-gray-400 mt-1">
              Manage your consents, request parental verification, exercise access rights, or file grievances.
            </p>
          </div>
          <span className="text-3xl">🇮🇳</span>
        </div>

        {message && (
          <div
            className={`mt-4 p-3 rounded-xl text-xs font-medium ${
              message.type === "success"
                ? "bg-emerald-950/60 border border-emerald-700/60 text-emerald-300"
                : "bg-red-950/60 border border-red-700/60 text-red-300"
            }`}
          >
            {message.text}
          </div>
        )}
      </div>

      {/* Grid: Consents & Minor Status */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        {/* Consent Management */}
        <div className="bg-[#161B22] border border-[#30363D] p-6 rounded-2xl shadow-xl space-y-4">
          <h3 className="text-base font-bold text-white flex items-center gap-2">
            <span>⚙️</span> Consent Preferences
          </h3>
          <p className="text-xs text-gray-400 leading-relaxed">
            Under the Digital Personal Data Protection Act, 2023, you have full control over optional data processing.
          </p>

          <div className="space-y-3 pt-2">
            <div className="flex items-center justify-between p-3 bg-[#0D1117] border border-[#30363D] rounded-xl">
              <div>
                <p className="text-xs font-semibold text-white">Essential Platform Operations</p>
                <p className="text-[11px] text-gray-400">Transaction records, authentication, encryption</p>
              </div>
              <span className="px-2 py-0.5 bg-emerald-900/50 text-emerald-300 border border-emerald-700/50 text-[10px] font-bold rounded">
                REQUIRED
              </span>
            </div>

            <div className="flex items-center justify-between p-3 bg-[#0D1117] border border-[#30363D] rounded-xl">
              <div>
                <p className="text-xs font-semibold text-white">Telemetry & Performance Analytics</p>
                <p className="text-[11px] text-gray-400">Anonymous crash diagnostics and speed metrics</p>
              </div>
              <button
                type="button"
                disabled={settings?.isMinor || saving}
                onClick={() => handleToggle("analyticsConsent")}
                className={`px-3 py-1 rounded-lg text-xs font-semibold transition-colors ${
                  settings?.analyticsConsent
                    ? "bg-[#238636] text-white"
                    : "bg-[#21262D] text-gray-400 hover:text-white"
                }`}
              >
                {settings?.analyticsConsent ? "Granted" : "Withdrawn"}
              </button>
            </div>

            <div className="flex items-center justify-between p-3 bg-[#0D1117] border border-[#30363D] rounded-xl">
              <div>
                <p className="text-xs font-semibold text-white">Educational & Product Digests</p>
                <p className="text-[11px] text-gray-400">Weekly student financial literacy insights</p>
              </div>
              <button
                type="button"
                disabled={settings?.isMinor || saving}
                onClick={() => handleToggle("marketingConsent")}
                className={`px-3 py-1 rounded-lg text-xs font-semibold transition-colors ${
                  settings?.marketingConsent
                    ? "bg-[#238636] text-white"
                    : "bg-[#21262D] text-gray-400 hover:text-white"
                }`}
              >
                {settings?.marketingConsent ? "Granted" : "Withdrawn"}
              </button>
            </div>
          </div>
        </div>

        {/* Minor / Parental Verification Status */}
        <div className="bg-[#161B22] border border-[#30363D] p-6 rounded-2xl shadow-xl space-y-4">
          <h3 className="text-base font-bold text-white flex items-center gap-2">
            <span>🛡️</span> Child & Minor Status
          </h3>

          {settings?.isMinor ? (
            <div className="space-y-3">
              <div className="p-3 bg-[#1F242C] border border-[#F0883E]/50 rounded-xl text-xs space-y-1">
                <div className="flex items-center justify-between text-[#F0883E] font-bold">
                  <span>Minor Account (Age {settings.age})</span>
                  <span className="uppercase text-[10px] px-2 py-0.5 rounded bg-[#F0883E]/20">
                    {settings.guardianConsentStatus}
                  </span>
                </div>
                <p className="text-gray-300 text-[11px]">
                  Behavioral tracking and targeted advertising are permanently prohibited.
                </p>
              </div>

              {settings.guardianConsentStatus !== "VERIFIED" && (
                <form onSubmit={handleGuardianRequest} className="space-y-2 pt-2 text-xs">
                  <p className="font-semibold text-gray-300">Update Guardian Verification</p>
                  <input
                    type="text"
                    value={guardianName}
                    onChange={(e) => setGuardianName(e.target.value)}
                    placeholder="Guardian Name"
                    className="w-full bg-[#0D1117] border border-[#30363D] rounded-lg px-3 py-1.5 text-white"
                    required
                  />
                  <input
                    type="email"
                    value={guardianEmail}
                    onChange={(e) => setGuardianEmail(e.target.value)}
                    placeholder="Guardian Email"
                    className="w-full bg-[#0D1117] border border-[#30363D] rounded-lg px-3 py-1.5 text-white"
                    required
                  />
                  <button
                    type="submit"
                    disabled={saving}
                    className="w-full py-2 bg-[#238636] hover:bg-[#2ea043] font-semibold text-white rounded-lg transition-colors"
                  >
                    Send Verification Request
                  </button>
                </form>
              )}
            </div>
          ) : (
            <div className="p-4 bg-[#0D1117] border border-[#30363D] rounded-xl text-xs text-gray-400 space-y-2">
              <p className="text-white font-semibold">Standard Adult Account (Age {settings?.age})</p>
              <p>Full user autonomy enabled. No parental verification required under DPDP Act Section 9.</p>
            </div>
          )}
        </div>
      </div>

      {/* Data Rights: Section 11 Export & Section 12 Erasure */}
      <div className="bg-[#161B22] border border-[#30363D] p-6 rounded-2xl shadow-xl space-y-4">
        <h3 className="text-base font-bold text-white flex items-center gap-2">
          <span>📂</span> Data Rights & Access
        </h3>
        <p className="text-xs text-gray-400">
          Exercise your statutory rights under the DPDP Act 2023 with direct, automated execution.
        </p>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-4 pt-2">
          <div className="p-4 bg-[#0D1117] border border-[#30363D] rounded-xl space-y-3">
            <h4 className="text-sm font-semibold text-white">Right to Access / Data Export</h4>
            <p className="text-xs text-gray-400 leading-relaxed">
              Download a complete JSON export of your personal profile, trips, transactions, and consent audit logs (Section 11).
            </p>
            <button
              type="button"
              onClick={handleExport}
              disabled={saving}
              className="px-4 py-2 bg-[#58A6FF]/20 hover:bg-[#58A6FF]/30 border border-[#58A6FF]/50 text-[#58A6FF] rounded-lg text-xs font-semibold transition-colors"
            >
              Export My Data (JSON)
            </button>
          </div>

          <div className="p-4 bg-[#0D1117] border border-red-900/50 rounded-xl space-y-3">
            <h4 className="text-sm font-semibold text-red-300">Right to Erasure (Anonymize)</h4>
            <p className="text-xs text-gray-400 leading-relaxed">
              Permanently delete all personal identifiers and anonymize your financial records in accordance with Section 12.
            </p>
            <button
              type="button"
              onClick={() => setShowErasureModal(true)}
              className="px-4 py-2 bg-red-950/60 hover:bg-red-900/80 border border-red-700/60 text-red-300 rounded-lg text-xs font-semibold transition-colors"
            >
              Request Account Erasure
            </button>
          </div>
        </div>
      </div>

      {/* Grievance Redressal (Section 13) */}
      <div className="bg-[#161B22] border border-[#30363D] p-6 rounded-2xl shadow-xl space-y-4">
        <h3 className="text-base font-bold text-white flex items-center gap-2">
          <span>⚖️</span> Grievance Redressal Mechanism (Section 13)
        </h3>
        <p className="text-xs text-gray-400">
          File an official privacy complaint directly to the SpeDex Data Protection Officer. We commit to a 48h acknowledgement SLA and resolution within 15 days.
        </p>

        <form onSubmit={handleGrievance} className="space-y-3 pt-2">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
            <div>
              <label className="block text-xs font-semibold text-gray-400 mb-1">Grievance Category</label>
              <select
                value={grievanceCat}
                onChange={(e) => setGrievanceCat(e.target.value)}
                className="w-full bg-[#0D1117] border border-[#30363D] rounded-lg px-3 py-2 text-xs text-white"
              >
                <option value="CONSENT_REVOCATION">Consent Revocation Issue</option>
                <option value="ACCESS_REQUEST">Data Access / Export Delay</option>
                <option value="ERASURE_REQUEST">Erasure / Anonymization Inquiry</option>
                <option value="MINOR_PROTECTION">Child / Minor Data Inquiry</option>
                <option value="SECURITY_CONCERN">Security & Safeguards Concern</option>
              </select>
            </div>
            <div>
              <label className="block text-xs font-semibold text-gray-400 mb-1">Description</label>
              <textarea
                value={grievanceDesc}
                onChange={(e) => setGrievanceDesc(e.target.value)}
                placeholder="Describe your privacy concern or grievance in detail..."
                rows={2}
                className="w-full bg-[#0D1117] border border-[#30363D] rounded-lg px-3 py-1.5 text-xs text-white"
                required
              />
            </div>
          </div>
          <button
            type="submit"
            disabled={saving}
            className="px-4 py-2 bg-[#238636] hover:bg-[#2ea043] text-xs font-semibold text-white rounded-lg transition-colors"
          >
            Submit Grievance Ticket
          </button>
        </form>

        {grievances.length > 0 && (
          <div className="pt-4 border-t border-[#30363D]/60 space-y-2">
            <h4 className="text-xs font-bold text-gray-300">Your Filed Grievances</h4>
            <div className="space-y-2 max-h-48 overflow-y-auto">
              {grievances.map((g) => (
                <div key={g.ticketId} className="p-3 bg-[#0D1117] border border-[#30363D] rounded-xl text-xs space-y-1">
                  <div className="flex items-center justify-between text-white font-semibold">
                    <span>{g.ticketId}</span>
                    <span className="text-[10px] px-2 py-0.5 rounded bg-[#58A6FF]/20 text-[#58A6FF]">{g.status}</span>
                  </div>
                  <p className="text-gray-400 text-[11px]">{g.description}</p>
                  {g.responseMessage && (
                    <p className="text-emerald-400 text-[11px] italic">DPO: {g.responseMessage}</p>
                  )}
                </div>
              ))}
            </div>
          </div>
        )}
      </div>

      {/* Legal Policies Footer Links */}
      <div className="p-4 bg-[#161B22] border border-[#30363D] rounded-2xl flex flex-wrap items-center justify-between gap-4 text-xs text-gray-400">
        <span className="font-semibold text-gray-300">Legal Documentation:</span>
        <div className="flex flex-wrap gap-3">
          <button type="button" onClick={() => onOpenDoc("terms")} className="text-[#58A6FF] hover:underline">
            Terms
          </button>
          <button type="button" onClick={() => onOpenDoc("privacy")} className="text-[#58A6FF] hover:underline">
            Privacy
          </button>
          <button type="button" onClick={() => onOpenDoc("consent")} className="text-[#58A6FF] hover:underline">
            Consent Notice
          </button>
          <button type="button" onClick={() => onOpenDoc("cookies")} className="text-[#58A6FF] hover:underline">
            Cookies
          </button>
          <button type="button" onClick={() => onOpenDoc("child-privacy")} className="text-[#58A6FF] hover:underline">
            Child Privacy
          </button>
          <button type="button" onClick={() => onOpenDoc("data-retention")} className="text-[#58A6FF] hover:underline">
            Data Retention
          </button>
          <button type="button" onClick={() => onOpenDoc("grievance")} className="text-[#58A6FF] hover:underline">
            Grievance SLA
          </button>
          <button type="button" onClick={() => onOpenDoc("third-parties")} className="text-[#58A6FF] hover:underline">
            Subprocessors
          </button>
        </div>
      </div>

      {/* Erasure Modal */}
      {showErasureModal && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/80 backdrop-blur-md">
          <div className="bg-[#0D1117] border border-red-700/60 w-full max-w-md rounded-2xl p-6 shadow-2xl text-gray-200 space-y-4">
            <h3 className="text-lg font-bold text-red-400 font-serif">Confirm Permanent Erasure</h3>
            <p className="text-xs text-gray-300 leading-relaxed">
              This action is permanent and irreversible. Your credentials will be destroyed, personal identifying data deleted, and financial records anonymized per DPDP Act Section 12.
            </p>
            <div>
              <label className="block text-xs text-gray-400 mb-1">
                Type <strong>DELETE MY DATA</strong> to confirm:
              </label>
              <input
                type="text"
                value={confirmText}
                onChange={(e) => setConfirmText(e.target.value)}
                placeholder="DELETE MY DATA"
                className="w-full bg-[#161B22] border border-[#30363D] rounded-lg px-3 py-2 text-xs text-white"
              />
            </div>
            <div className="flex justify-end gap-2 pt-2">
              <button
                type="button"
                onClick={() => setShowErasureModal(false)}
                className="px-3 py-1.5 text-xs text-gray-400 hover:text-white rounded-lg hover:bg-[#21262D]"
              >
                Cancel
              </button>
              <button
                type="button"
                disabled={confirmText !== "DELETE MY DATA" || saving}
                onClick={handleErasure}
                className="px-4 py-1.5 text-xs font-semibold text-white bg-red-600 hover:bg-red-700 disabled:opacity-50 rounded-lg transition-colors"
              >
                Permanently Erase
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
