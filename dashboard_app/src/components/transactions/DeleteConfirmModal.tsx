import { useState } from "react";
import type { Transaction } from "../../types";
import { formatCurrency, formatDate } from "../../utils/formatters";

export function DeleteConfirmModal({
  transaction,
  onClose,
  onConfirm,
}: {
  transaction: Transaction;
  onClose: () => void;
  onConfirm: (id: number) => Promise<void>;
}) {
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  async function handleDelete() {
    setSubmitting(true);
    setError(null);
    try {
      await onConfirm(transaction.id);
      onClose();
    } catch (err: any) {
      setError(err?.message || "Failed to delete transaction.");
      setSubmitting(false);
    }
  }

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="qr-modal-card" style={{ maxWidth: 400, width: "90%", textAlign: "left" }} onClick={(e) => e.stopPropagation()}>
        <div style={{ display: "flex", alignItems: "center", gap: 12, marginBottom: 12 }}>
          <div style={{ width: 40, height: 40, borderRadius: "50%", background: "rgba(220, 53, 69, 0.1)", display: "flex", alignItems: "center", justifyContent: "center", color: "#dc3545" }}>
            <span className="material-symbols-outlined">delete_forever</span>
          </div>
          <h3 style={{ margin: 0, fontFamily: "var(--font-serif)" }}>Delete Transaction</h3>
        </div>

        <p style={{ margin: "0 0 16px", color: "var(--text)" }}>
          Are you sure you want to permanently delete this transaction from your personal ledger?
        </p>

        <div style={{ padding: 12, background: "var(--surface)", borderRadius: 10, border: "1px solid var(--border-soft)", marginBottom: 16 }}>
          <div style={{ display: "flex", justifyContent: "space-between", marginBottom: 4 }}>
            <strong>{transaction.description}</strong>
            <strong style={{ color: transaction.direction === "income" ? "var(--accent-mint)" : "var(--primary)" }}>
              {transaction.direction === "income" ? "+" : "-"}{formatCurrency(transaction.amount)}
            </strong>
          </div>
          <span className="subtle" style={{ fontSize: 12 }}>
            {formatDate(transaction.occurred_at)} | {transaction.category}
          </span>
        </div>

        {error && (
          <div style={{ padding: "8px 12px", background: "rgba(220, 53, 69, 0.1)", color: "#dc3545", borderRadius: 8, fontSize: 13, marginBottom: 12 }}>
            {error}
          </div>
        )}

        <div style={{ display: "flex", gap: 10, width: "100%" }}>
          <button
            type="button"
            onClick={onClose}
            style={{ flex: 1, padding: "10px", borderRadius: 10, border: "1px solid var(--border-soft)", background: "transparent", cursor: "pointer" }}
          >
            Cancel
          </button>
          <button
            type="button"
            onClick={handleDelete}
            disabled={submitting}
            style={{ flex: 1, padding: "10px", borderRadius: 10, border: "none", background: "#dc3545", color: "white", fontWeight: 600, cursor: "pointer" }}
          >
            {submitting ? "Deleting..." : "Yes, Delete"}
          </button>
        </div>
      </div>
    </div>
  );
}
