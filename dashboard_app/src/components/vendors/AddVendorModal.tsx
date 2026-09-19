import { useState } from "react";

export function AddVendorModal({
  onClose,
  onSave,
}: {
  onClose: () => void;
  onSave: (payload: { name: string; category: string; upi_handle: string; default_amount: number }) => Promise<void>;
}) {
  const [name, setName] = useState("");
  const [category, setCategory] = useState("Dining");
  const [upi, setUpi] = useState("");
  const [amount, setAmount] = useState("");
  const [submitting, setSubmitting] = useState(false);

  async function handleSubmit() {
    setSubmitting(true);
    await onSave({ name, category, upi_handle: upi, default_amount: parseFloat(amount) || 0 });
    setSubmitting(false);
  }

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="qr-modal-card" onClick={(e) => e.stopPropagation()}>
        <h3>Add New Vendor</h3>
        <p className="subtle" style={{ marginBottom: 16 }}>Create a payee for 1-tap transfers.</p>
        <div className="auth-form">
          <input placeholder="Vendor Name" value={name} onChange={(e) => setName(e.target.value)} />
          <input placeholder="Category (e.g. Dining, Shopping)" value={category} onChange={(e) => setCategory(e.target.value)} />
          <input placeholder="UPI Handle (e.g. xyz@oksbi)" value={upi} onChange={(e) => setUpi(e.target.value)} />
          <input type="number" placeholder="Default Amount" value={amount} onChange={(e) => setAmount(e.target.value)} />
          <button className="auth-submit" onClick={handleSubmit} disabled={submitting}>
            {submitting ? "Saving..." : "Save Vendor"}
          </button>
        </div>
      </div>
    </div>
  );
}
