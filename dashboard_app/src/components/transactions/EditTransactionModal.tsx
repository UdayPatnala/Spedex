import { useState } from "react";
import type { Category, Transaction, TransactionUpdate, Vendor } from "../../types";

export function EditTransactionModal({
  transaction,
  onClose,
  onSave,
  categories,
  vendors,
}: {
  transaction: Transaction;
  onClose: () => void;
  onSave: (id: number, payload: TransactionUpdate) => Promise<void>;
  categories: Category[];
  vendors: Vendor[];
}) {
  const [description, setDescription] = useState(transaction.description);
  const [amount, setAmount] = useState(transaction.amount.toString());
  const [category, setCategory] = useState(transaction.category);
  const [direction, setDirection] = useState<"expense" | "income">(transaction.direction);
  const [paymentMethod, setPaymentMethod] = useState(transaction.payment_method || "UPI");
  const [vendorId, setVendorId] = useState<string>(transaction.vendor_id ? transaction.vendor_id.toString() : "");
  const [occurredAt, setOccurredAt] = useState(() => {
    if (transaction.occurred_at) {
      try {
        const d = new Date(transaction.occurred_at);
        d.setMinutes(d.getMinutes() - d.getTimezoneOffset());
        return d.toISOString().slice(0, 16);
      } catch {
        return "";
      }
    }
    return "";
  });
  const [error, setError] = useState<string | null>(null);
  const [submitting, setSubmitting] = useState(false);

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    setError(null);

    const parsedAmount = parseFloat(amount);
    if (!description.trim()) {
      setError("Please enter a description for the transaction.");
      return;
    }
    if (isNaN(parsedAmount) || parsedAmount <= 0) {
      setError("Please enter a valid positive amount.");
      return;
    }

    setSubmitting(true);
    try {
      await onSave(transaction.id, {
        description: description.trim(),
        amount: parsedAmount,
        category,
        direction,
        paymentMethod,
        occurredAt: occurredAt ? new Date(occurredAt).toISOString() : undefined,
        vendorId: vendorId ? parseInt(vendorId, 10) : undefined,
      });
      onClose();
    } catch (err: any) {
      setError(err?.message || "Failed to update transaction.");
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="qr-modal-card" style={{ maxWidth: 440, width: "90%", textAlign: "left" }} onClick={(e) => e.stopPropagation()}>
        <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", width: "100%", marginBottom: 12 }}>
          <h3 style={{ margin: 0, fontFamily: "var(--font-serif)" }}>Edit Transaction #{transaction.id}</h3>
          <button
            type="button"
            onClick={onClose}
            style={{ background: "transparent", border: "none", cursor: "pointer", color: "var(--text-subtle)", fontSize: 20 }}
          >
            &times;
          </button>
        </div>
        <p className="subtle" style={{ margin: "0 0 16px" }}>Modify transaction details.</p>

        {error && (
          <div style={{ padding: "8px 12px", background: "rgba(220, 53, 69, 0.1)", color: "#dc3545", borderRadius: 8, fontSize: 13, marginBottom: 12 }}>
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit} style={{ display: "flex", flexDirection: "column", gap: 12, width: "100%" }}>
          <div style={{ display: "flex", gap: 8 }}>
            <button
              type="button"
              className={`status-chip ${direction === "expense" ? "active" : "soft"}`}
              style={{ flex: 1, padding: "8px", cursor: "pointer", border: "none", borderRadius: 8 }}
              onClick={() => setDirection("expense")}
            >
              Expense (-)
            </button>
            <button
              type="button"
              className={`status-chip ${direction === "income" ? "active" : "soft"}`}
              style={{ flex: 1, padding: "8px", cursor: "pointer", border: "none", borderRadius: 8 }}
              onClick={() => setDirection("income")}
            >
              Income (+)
            </button>
          </div>

          <div>
            <label style={{ fontSize: 12, fontWeight: 600, color: "var(--text-subtle)", display: "block", marginBottom: 4 }}>Amount (INR)</label>
            <input
              type="number"
              step="any"
              placeholder="0.00"
              value={amount}
              onChange={(e) => setAmount(e.target.value)}
              required
              style={{ width: "100%", padding: "10px 14px", borderRadius: 10, border: "1px solid var(--border-soft)", background: "var(--surface)", fontSize: 16 }}
            />
          </div>

          <div>
            <label style={{ fontSize: 12, fontWeight: 600, color: "var(--text-subtle)", display: "block", marginBottom: 4 }}>Description</label>
            <input
              type="text"
              placeholder="Description"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              required
              style={{ width: "100%", padding: "10px 14px", borderRadius: 10, border: "1px solid var(--border-soft)", background: "var(--surface)" }}
            />
          </div>

          <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: 10 }}>
            <div>
              <label style={{ fontSize: 12, fontWeight: 600, color: "var(--text-subtle)", display: "block", marginBottom: 4 }}>Category</label>
              <select
                value={category}
                onChange={(e) => setCategory(e.target.value)}
                style={{ width: "100%", padding: "10px 12px", borderRadius: 10, border: "1px solid var(--border-soft)", background: "var(--surface)" }}
              >
                {categories.length > 0 ? (
                  categories.map((c) => (
                    <option key={c.id} value={c.name}>
                      {c.name}
                    </option>
                  ))
                ) : (
                  <>
                    <option value="Food">Food</option>
                    <option value="Groceries">Groceries</option>
                    <option value="Transport">Transport</option>
                    <option value="Shopping">Shopping</option>
                    <option value="Entertainment">Entertainment</option>
                    <option value="Education">Education</option>
                    <option value="Bills">Bills</option>
                    <option value="Health">Health</option>
                    <option value="Personal">Personal</option>
                    <option value="Other">Other</option>
                  </>
                )}
              </select>
            </div>

            <div>
              <label style={{ fontSize: 12, fontWeight: 600, color: "var(--text-subtle)", display: "block", marginBottom: 4 }}>Payment Method</label>
              <select
                value={paymentMethod}
                onChange={(e) => setPaymentMethod(e.target.value)}
                style={{ width: "100%", padding: "10px 12px", borderRadius: 10, border: "1px solid var(--border-soft)", background: "var(--surface)" }}
              >
                <option value="UPI">UPI</option>
                <option value="Cash">Cash</option>
                <option value="Card">Debit / Credit Card</option>
                <option value="NetBanking">Net Banking</option>
                <option value="Other">Other</option>
              </select>
            </div>
          </div>

          {vendors.length > 0 && (
            <div>
              <label style={{ fontSize: 12, fontWeight: 600, color: "var(--text-subtle)", display: "block", marginBottom: 4 }}>Payee / Reference</label>
              <select
                value={vendorId}
                onChange={(e) => setVendorId(e.target.value)}
                style={{ width: "100%", padding: "10px 12px", borderRadius: 10, border: "1px solid var(--border-soft)", background: "var(--surface)" }}
              >
                <option value="">None (Personal)</option>
                {vendors.map((v) => (
                  <option key={v.id} value={v.id}>
                    {v.name} ({v.category})
                  </option>
                ))}
              </select>
            </div>
          )}

          <div>
            <label style={{ fontSize: 12, fontWeight: 600, color: "var(--text-subtle)", display: "block", marginBottom: 4 }}>Date & Time</label>
            <input
              type="datetime-local"
              value={occurredAt}
              onChange={(e) => setOccurredAt(e.target.value)}
              style={{ width: "100%", padding: "8px 12px", borderRadius: 10, border: "1px solid var(--border-soft)", background: "var(--surface)" }}
            />
          </div>

          <div style={{ display: "flex", gap: 10, marginTop: 8 }}>
            <button
              type="button"
              onClick={onClose}
              style={{ flex: 1, padding: "10px", borderRadius: 10, border: "1px solid var(--border-soft)", background: "transparent", cursor: "pointer" }}
            >
              Cancel
            </button>
            <button
              type="submit"
              disabled={submitting}
              className="auth-submit"
              style={{ flex: 2, margin: 0 }}
            >
              {submitting ? "Saving..." : "Save Changes"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
