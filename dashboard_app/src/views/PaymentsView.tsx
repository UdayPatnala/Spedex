import { useState } from "react";
import QRCode from "react-qr-code";
import type { Vendor, VendorDirectoryData } from "../types";
import {
  accentClass,
  categoryEmoji,
  categoryLabel,
  formatCurrency,
} from "../utils/formatters";

export function PaymentsView({
  vendors,
  onAddVendor,
}: {
  vendors: VendorDirectoryData;
  onAddVendor: () => void;
}) {
  const [activeVendor, setActiveVendor] = useState<Vendor | null>(null);

  return (
    <div className="payments-shell">
      {activeVendor && (
        <div className="modal-overlay" onClick={() => setActiveVendor(null)}>
          <div className="qr-modal-card" onClick={(e) => e.stopPropagation()}>
            <h3>Pay {activeVendor.name}</h3>
            <p className="subtle">{activeVendor.upi_handle}</p>
            <div style={{ background: "white", padding: "16px", borderRadius: "8px", margin: "24px 0" }}>
              <QRCode value={`upi://pay?pa=${activeVendor.upi_handle}&pn=${activeVendor.name}&cu=INR`} size={200} />
            </div>
            <button className="auth-submit" onClick={() => setActiveVendor(null)}>Close</button>
          </div>
        </div>
      )}
      <section className="card">
        <div className="section-header">
          <div>
            <p className="eyebrow">UPI Directory</p>
            <h2 className="page-title section-title">Payees</h2>
          </div>
          <button className="status-chip" onClick={onAddVendor} style={{ cursor: "pointer", border: "none" }}>+ Add</button>
        </div>

        <div className="vendors-list">
          {Object.keys(vendors.groups).length === 0 ? (
            <div style={{ textAlign: "center", padding: "3rem 2rem", color: "var(--text-subtle)", background: "rgba(255,255,255,0.4)", borderRadius: 12 }}>
              <p>Your directory is empty.</p>
              <button className="auth-submit" onClick={onAddVendor} style={{ marginTop: 16 }}>Add your first vendor</button>
            </div>
          ) : Object.entries(vendors.groups).map(([groupName, groupVendors]) => (
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
                    <span className="status-chip soft">{categoryLabel(vendor.category)}</span>
                  </div>
                </div>
              ))}
            </div>
          ))}
        </div>
      </section>
    </div>
  );
}
