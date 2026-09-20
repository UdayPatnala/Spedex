import { useState } from "react";
import QRCode from "react-qr-code";
import type { UserCapabilities, Vendor, VendorDirectoryData } from "../types";
import {
  accentClass,
  categoryEmoji,
  categoryLabel,
  formatCurrency,
} from "../utils/formatters";

export function PaymentsView({
  vendors,
  capabilities,
  onAddVendor,
}: {
  vendors: VendorDirectoryData;
  capabilities?: UserCapabilities | null;
  onAddVendor: () => void;
}) {
  const [activeVendor, setActiveVendor] = useState<Vendor | null>(null);
  const isMinorMode = Boolean(
    capabilities ? !capabilities.canInitiatePayment : vendors.user?.is_minor
  );

  return (
    <div className="payments-shell">
      {isMinorMode && (
        <div
          className="minor-safety-banner"
          style={{
            background: "rgba(13, 40, 24, 0.05)",
            border: "1px solid rgba(13, 40, 24, 0.15)",
            borderRadius: 12,
            padding: "16px 20px",
            marginBottom: 20,
            display: "flex",
            alignItems: "flex-start",
            gap: 14,
          }}
        >
          <span style={{ fontSize: "1.5rem", lineHeight: 1 }}>🛡️</span>
          <div>
            <div style={{ display: "flex", alignItems: "center", gap: 8, marginBottom: 4 }}>
              <strong style={{ color: "var(--brand-emerald-dark)", fontSize: "0.95rem" }}>
                Financial Learning / Journal Mode Active
              </strong>
              <span className="status-chip soft" style={{ fontSize: "0.72rem" }}>
                Minor Account (&lt;18)
              </span>
            </div>
            <p style={{ margin: 0, fontSize: "0.85rem", color: "var(--text-subtle)", lineHeight: 1.45 }}>
              Payment shortcuts, external payment links, and live UPI QR actions are strictly disabled for accounts under 18 in accordance with minor protection safeguards. You can still record transactions manually and explore spending insights.
            </p>
          </div>
        </div>
      )}

      {activeVendor && (
        <div className="modal-overlay" onClick={() => setActiveVendor(null)}>
          <div className="qr-modal-card" onClick={(e) => e.stopPropagation()}>
            <h3>{isMinorMode ? "Payee Details" : `Pay ${activeVendor.name}`}</h3>
            <p className="subtle">{activeVendor.upi_handle}</p>

            {isMinorMode ? (
              <div
                style={{
                  background: "rgba(212, 175, 55, 0.1)",
                  border: "1px solid rgba(212, 175, 55, 0.3)",
                  borderRadius: "8px",
                  padding: "20px 16px",
                  margin: "24px 0",
                  textAlign: "center",
                }}
              >
                <span style={{ fontSize: "2rem", display: "block", marginBottom: 8 }}>🔒</span>
                <strong style={{ display: "block", color: "var(--brand-emerald-dark)", marginBottom: 6 }}>
                  Live UPI Generation Disabled
                </strong>
                <p style={{ margin: 0, fontSize: "0.85rem", color: "var(--text-subtle)", lineHeight: 1.4 }}>
                  In accordance with minor safety controls, direct payment initiation is restricted.
                  To record a payment to <strong>{activeVendor.name}</strong>, use the manual expense logger on your Overview page.
                </p>
              </div>
            ) : (
              <div style={{ background: "white", padding: "16px", borderRadius: "8px", margin: "24px 0" }}>
                <QRCode
                  value={`upi://pay?pa=${activeVendor.upi_handle}&pn=${activeVendor.name}&cu=INR`}
                  size={200}
                />
              </div>
            )}

            <button className="auth-submit" onClick={() => setActiveVendor(null)}>
              Close
            </button>
          </div>
        </div>
      )}

      <section className="card">
        <div className="section-header">
          <div>
            <p className="eyebrow">UPI Directory</p>
            <h2 className="page-title section-title">
              Payees {isMinorMode && <span style={{ fontSize: "0.7em", opacity: 0.7 }}>(Learning Mode)</span>}
            </h2>
          </div>
          <button className="status-chip" onClick={onAddVendor} style={{ cursor: "pointer", border: "none" }}>
            + Add
          </button>
        </div>

        <div className="vendors-list">
          {Object.keys(vendors.groups).length === 0 ? (
            <div
              style={{
                textAlign: "center",
                padding: "3rem 2rem",
                color: "var(--text-subtle)",
                background: "rgba(255,255,255,0.4)",
                borderRadius: 12,
              }}
            >
              <p>Your directory is empty.</p>
              <button className="auth-submit" onClick={onAddVendor} style={{ marginTop: 16 }}>
                Add your first vendor
              </button>
            </div>
          ) : (
            Object.entries(vendors.groups).map(([groupName, groupVendors]) => (
              <div key={groupName} className="full-width">
                <div className="section-header">
                  <p className="eyebrow">{categoryLabel(groupName)}</p>
                </div>
                {groupVendors.map((vendor) => (
                  <div
                    key={vendor.id}
                    className="vendor-row"
                    onClick={() => setActiveVendor(vendor)}
                    style={{ cursor: "pointer" }}
                    title={isMinorMode ? "View payee details (UPI disabled for minors)" : `Pay ${vendor.name}`}
                  >
                    <div className={`icon-badge ${accentClass(vendor.accent)}`}>
                      <span className="emoji-glyph">{categoryEmoji(vendor.category)}</span>
                    </div>
                    <div>
                      <p className="vendor-title">{vendor.name}</p>
                      <span className="subtle">{vendor.upi_handle}</span>
                    </div>
                    <div className="vendor-meta">
                      <strong>{formatCurrency(vendor.default_amount)}</strong>
                      <span className="status-chip soft">
                        {isMinorMode ? "Journal Payee" : categoryLabel(vendor.category)}
                      </span>
                    </div>
                  </div>
                ))}
              </div>
            ))
          )}
        </div>
      </section>
    </div>
  );
}
