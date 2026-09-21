import { useEffect, useState } from "react";
import {
  addManualTransaction,
  completeTrip,
  getTripDetails,
  getTrips,
  startTrip,
} from "../api";
import type { Trip, TripDetails } from "../types";
import { categoryLabel, formatCurrency, formatDate } from "../utils/formatters";
import {
  FOREX_CACHE,
  formatForeignCurrency,
  convertInrToForeign,
  convertForeignToInr,
  getExchangeRate,
} from "../utils/forex";

export function TripsView() {
  const [trips, setTrips] = useState<Trip[]>([]);
  const [selectedTripId, setSelectedTripId] = useState<number | null>(null);
  const [tripDetails, setTripDetails] = useState<TripDetails | null>(null);
  const [newTripName, setNewTripName] = useState("");
  const [newTripCurrency, setNewTripCurrency] = useState("INR");
  const [newTripRate, setNewTripRate] = useState<number>(1.0);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Manual cash transaction form
  const [cashAmount, setCashAmount] = useState("");
  const [cashCurrencyMode, setCashCurrencyMode] = useState<"INR" | "FOREIGN">("INR");
  const [cashDesc, setCashDesc] = useState("");
  const [cashCategory, setCashCategory] = useState("Dining");
  const [customCategory, setCustomCategory] = useState("");
  const [showCustomCatInput, setShowCustomCatInput] = useState(false);
  const [customTags, setCustomTags] = useState<string[]>([]);
  const [submittingTx, setSubmittingTx] = useState(false);
  const [showLogCashModal, setShowLogCashModal] = useState(false);

  const fetchTrips = async () => {
    try {
      setLoading(true);
      const list = await getTrips();
      // Sort ACTIVE trips first, then newest first
      const sorted = [...list].sort((a, b) => {
        if (a.status === "ACTIVE" && b.status !== "ACTIVE") return -1;
        if (a.status !== "ACTIVE" && b.status === "ACTIVE") return 1;
        return b.id - a.id;
      });
      setTrips(sorted);

      const active = sorted.find((t) => t.status === "ACTIVE");
      if (active) {
        setSelectedTripId(active.id);
      } else if (sorted.length > 0 && !selectedTripId) {
        setSelectedTripId(sorted[0].id);
      }
    } catch (e: any) {
      setError(e.message || "Failed to load trips.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchTrips();
  }, []);

  useEffect(() => {
    if (selectedTripId !== null) {
      const fetchDetails = async () => {
        try {
          const details = await getTripDetails(selectedTripId);
          setTripDetails(details);
        } catch (e: any) {
          setError(e.message || "Failed to load trip details.");
        }
      };
      fetchDetails();
    } else {
      setTripDetails(null);
    }
  }, [selectedTripId]);

  const handleStartTrip = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!newTripName.trim()) return;
    try {
      setLoading(true);
      setError(null);
      const newTrip = await startTrip(newTripName.trim(), newTripCurrency, newTripRate);
      setNewTripName("");
      setNewTripCurrency("INR");
      setNewTripRate(1.0);
      setSelectedTripId(newTrip.id);
      await fetchTrips();
    } catch (e: any) {
      setError(e.message || "Failed to start trip.");
    } finally {
      setLoading(false);
    }
  };

  const handleEndTrip = async () => {
    if (selectedTripId === null) return;
    try {
      setLoading(true);
      setError(null);
      await completeTrip(selectedTripId);
      await fetchTrips();
    } catch (e: any) {
      setError(e.message || "Failed to end trip.");
    } finally {
      setLoading(false);
    }
  };

  const handleAddCustomTag = (e: React.MouseEvent) => {
    e.preventDefault();
    const tag = customCategory.trim();
    if (tag && !customTags.includes(tag)) {
      setCustomTags((prev) => [...prev, tag]);
      setCashCategory(tag);
      setCustomCategory("");
      setShowCustomCatInput(false);
    }
  };

  const handleAddTransaction = async (e: React.FormEvent) => {
    e.preventDefault();
    if (selectedTripId === null) return;
    const rawAmount = parseFloat(cashAmount);
    if (isNaN(rawAmount) || rawAmount <= 0) {
      setError("Please enter a valid positive amount.");
      return;
    }

    // Convert to INR if entered in foreign currency
    let finalInrAmount = rawAmount;
    let descriptionSuffix = "";
    if (
      cashCurrencyMode === "FOREIGN" &&
      tripDetails?.currency &&
      tripDetails.currency !== "INR"
    ) {
      const rate = tripDetails.exchange_rate || getExchangeRate(tripDetails.currency);
      finalInrAmount = convertForeignToInr(rawAmount, tripDetails.currency, rate);
      descriptionSuffix = ` (${formatForeignCurrency(rawAmount, tripDetails.currency)} @ ₹${rate})`;
    }

    try {
      setSubmittingTx(true);
      setError(null);
      await addManualTransaction(selectedTripId, {
        amount: finalInrAmount,
        description: (cashDesc.trim() || "Cash Expense") + descriptionSuffix,
        category: cashCategory,
      });
      setCashAmount("");
      setCashDesc("");
      setCashCurrencyMode("INR");
      // Refresh details
      const details = await getTripDetails(selectedTripId);
      setTripDetails(details);
    } catch (e: any) {
      setError(e.message || "Failed to add cash transaction.");
    } finally {
      setSubmittingTx(false);
    }
  };

  const activeTrip = trips.find((t) => t.status === "ACTIVE");

  return (
    <div className="trips-shell">
      <aside className="trips-sidebar card">
        <h3 className="section-title">Trips History</h3>
        <p className="subtle">Track your spending during journeys.</p>

        {!activeTrip && (
          <form onSubmit={handleStartTrip} className="start-trip-form">
            <input
              type="text"
              placeholder="e.g. Mumbai, Singapore, Nepal 2026"
              value={newTripName}
              onChange={(e) => setNewTripName(e.target.value)}
              className="text-input"
              required
            />
            <div style={{ display: "flex", gap: 8, marginTop: 8 }}>
              <select
                value={newTripCurrency}
                onChange={(e) => {
                  const curr = e.target.value;
                  setNewTripCurrency(curr);
                  setNewTripRate(getExchangeRate(curr));
                }}
                className="select-input"
                style={{ flex: 1, padding: "8px 10px", borderRadius: 10, border: "0.5px solid var(--border-soft)", background: "white", fontSize: "0.82rem" }}
              >
                {Object.values(FOREX_CACHE).map((c) => (
                  <option key={c.code} value={c.code}>
                    {c.flag} {c.code} ({c.symbol})
                  </option>
                ))}
              </select>
              {newTripCurrency !== "INR" && (
                <div style={{ display: "flex", alignItems: "center", fontSize: "0.75rem", color: "var(--text-muted)", paddingRight: 4 }}>
                  <span>₹{newTripRate}</span>
                </div>
              )}
            </div>
            <button type="submit" disabled={loading} className="cta-button primary" style={{ marginTop: 8 }}>
              <span className="material-symbols-outlined">add</span> Start New Trip
            </button>
          </form>
        )}

        <div className="trips-history-list">
          {trips.map((t) => (
            <button
              key={t.id}
              onClick={() => setSelectedTripId(t.id)}
              className={`trip-history-item ${t.id === selectedTripId ? "active" : ""}`}
            >
              <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", width: "100%" }}>
                <span style={{ fontWeight: 600 }}>
                  {FOREX_CACHE[t.currency || "INR"]?.flag || "🇮🇳"} {t.name}
                </span>
                <span className={`status-chip ${t.status === "ACTIVE" ? "active" : "soft"}`}>
                  {t.status}
                </span>
              </div>
              <div style={{ display: "flex", justifyContent: "space-between", marginTop: 4 }}>
                <span className="subtle" style={{ fontSize: "0.75rem" }}>
                  Started: {formatDate(t.created_at)}
                </span>
                {t.currency && t.currency !== "INR" && (
                  <span style={{ fontSize: "0.75rem", fontWeight: 600, color: "var(--primary)" }}>
                    {t.currency}
                  </span>
                )}
              </div>
            </button>
          ))}
          {trips.length === 0 && <p className="subtle text-center">No trips started yet.</p>}
        </div>
      </aside>

      <section className="trips-main">
        {error && (
          <div className="error-alert" style={{ marginBottom: 16 }}>
            <span className="material-symbols-outlined">error</span>
            <span>{error}</span>
          </div>
        )}

        {tripDetails ? (
          <div className="trip-details-view">
            <div className="section-header" style={{ marginBottom: 20 }}>
              <div>
                <div style={{ display: "flex", alignItems: "center", gap: 8 }}>
                  <span className={`status-chip ${tripDetails.status === "ACTIVE" ? "active" : "soft"}`}>
                    {tripDetails.status}
                  </span>
                  {tripDetails.currency && tripDetails.currency !== "INR" && (
                    <span className="status-chip soft" style={{ background: "rgba(212, 175, 55, 0.15)", color: "#8a6d1c" }}>
                      ✈️ {FOREX_CACHE[tripDetails.currency]?.flag || "🌐"} {tripDetails.currency} (1 {tripDetails.currency} = ₹{tripDetails.exchange_rate || getExchangeRate(tripDetails.currency)})
                    </span>
                  )}
                </div>
                <h2 className="page-title section-title" style={{ marginTop: 6 }}>{tripDetails.name}</h2>
                <p className="subtle">
                  Started: {formatDate(tripDetails.created_at)}
                  {tripDetails.completed_at && ` - Ended: ${formatDate(tripDetails.completed_at)}`}
                </p>
              </div>

              {tripDetails.status === "ACTIVE" && (
                <div style={{ display: "flex", gap: 12 }}>
                  <button onClick={() => setShowLogCashModal(true)} className="cta-button primary">
                    <span className="material-symbols-outlined">payments</span> Log Cash Expense
                  </button>
                  <button onClick={handleEndTrip} disabled={loading} className="cta-button danger">
                    <span className="material-symbols-outlined">power_settings_new</span> End Trip
                  </button>
                </div>
              )}
            </div>

            <div className="trip-stat-grid">
              <div className="trip-stat-card card">
                <p className="eyebrow text-muted">Total Spent (INR)</p>
                <strong className="trip-stat-value">{formatCurrency(tripDetails.total_spend)}</strong>
                {tripDetails.currency && tripDetails.currency !== "INR" && (
                  <span className="subtle" style={{ fontSize: "0.85rem", color: "#8a6d1c", marginTop: 2, display: "block" }}>
                    ~{formatForeignCurrency(tripDetails.foreign_total_spend ?? convertInrToForeign(tripDetails.total_spend, tripDetails.currency, tripDetails.exchange_rate), tripDetails.currency)}
                  </span>
                )}
              </div>
              <div className="trip-stat-card card">
                <p className="eyebrow text-muted">Cash Spend</p>
                <strong className="trip-stat-value text-amber">{formatCurrency(tripDetails.cash_spend)}</strong>
              </div>
              <div className="trip-stat-card card">
                <p className="eyebrow text-muted">UPI & Card Spend</p>
                <strong className="trip-stat-value text-indigo">{formatCurrency(tripDetails.card_online_spend)}</strong>
              </div>
            </div>

            <div className="trip-content-grid">
              <div className="trip-left card">
                <h3 className="section-title text-sm" style={{ marginBottom: 12 }}>Trip Ledger Overview</h3>
                <div className="trip-transactions-list scrollable">
                  {tripDetails.transactions.map((tx) => (
                    <div key={tx.id} className="transaction-row">
                      <div className="icon-badge">
                        <span className="emoji-glyph">
                          {tx.payment_method === "CASH" ? "💵" : "📱"}
                        </span>
                      </div>
                      <div className="flex-1">
                        <div style={{ display: "flex", justifyContent: "space-between" }}>
                          <strong>{tx.description}</strong>
                          <strong className="amount-label text-rose">
                            -{formatCurrency(tx.amount)}
                          </strong>
                        </div>
                        <div style={{ display: "flex", justifyContent: "space-between", marginTop: 4 }}>
                          <span className="subtle">{categoryLabel(tx.category)}</span>
                          <span className="subtle">{formatDate(tx.occurred_at)}</span>
                        </div>
                      </div>
                    </div>
                  ))}

                  {tripDetails.transactions.length === 0 && (
                    <p className="subtle text-center" style={{ padding: "20px 0" }}>
                      No payments associated with this trip yet.
                    </p>
                  )}
                </div>
              </div>

              <div className="trip-right card">
                <h3 className="section-title text-sm" style={{ marginBottom: 12 }}>Category Breakdown</h3>
                <div className="trip-category-list">
                  {tripDetails.category_breakdown.map((item) => (
                    <div key={item.category} className="category-progress-item">
                      <div style={{ display: "flex", justifyContent: "space-between", marginBottom: 6 }}>
                        <span>{categoryLabel(item.category)}</span>
                        <strong>{formatCurrency(item.amount)} ({item.percentage}%)</strong>
                      </div>
                      <div className="progress-line">
                        <span style={{ width: `${item.percentage}%` }} />
                      </div>
                    </div>
                  ))}

                  {tripDetails.category_breakdown.length === 0 && (
                    <p className="subtle text-center" style={{ padding: "20px 0" }}>
                      No category metrics available.
                    </p>
                  )}
                </div>
              </div>
            </div>
          </div>
        ) : (
          <div className="empty-details card text-center" style={{ padding: "60px 20px" }}>
            <span className="material-symbols-outlined" style={{ fontSize: "3rem", color: "var(--primary)" }}>
              explore
            </span>
            <h3 style={{ marginTop: 12 }}>No Trip Selected</h3>
            <p className="subtle">Select a trip from history or start a new one to view details.</p>
          </div>
        )}
      </section>

      {showLogCashModal && (
        <div className="modal-overlay" onClick={() => setShowLogCashModal(false)}>
          <div className="qr-modal-card" onClick={(e) => e.stopPropagation()} style={{ width: "100%", maxWidth: 420 }}>
            <h3>Log Cash Transaction</h3>
            <p className="subtle" style={{ marginBottom: 16 }}>Log a manual cash transaction to this active trip.</p>

            {tripDetails?.currency && tripDetails.currency !== "INR" && (
              <div style={{ display: "flex", gap: 8, marginBottom: 14, padding: 4, background: "rgba(0,0,0,0.04)", borderRadius: 10 }}>
                <button
                  type="button"
                  onClick={() => setCashCurrencyMode("INR")}
                  className={`cta-button ${cashCurrencyMode === "INR" ? "primary" : "secondary"}`}
                  style={{ flex: 1, padding: "6px 8px", fontSize: "0.8rem", borderRadius: 8 }}
                >
                  🇮🇳 Enter in INR (₹)
                </button>
                <button
                  type="button"
                  onClick={() => setCashCurrencyMode("FOREIGN")}
                  className={`cta-button ${cashCurrencyMode === "FOREIGN" ? "primary" : "secondary"}`}
                  style={{ flex: 1, padding: "6px 8px", fontSize: "0.8rem", borderRadius: 8 }}
                >
                  {FOREX_CACHE[tripDetails.currency]?.flag || "🌐"} {tripDetails.currency} ({FOREX_CACHE[tripDetails.currency]?.symbol || "$"})
                </button>
              </div>
            )}

            <form onSubmit={async (e) => {
              await handleAddTransaction(e);
              setShowLogCashModal(false);
            }} className="auth-form" style={{ width: "100%" }}>
              <div style={{ display: "flex", gap: 10 }}>
                <input
                  type="number"
                  step="0.01"
                  placeholder={cashCurrencyMode === "FOREIGN" && tripDetails?.currency ? `Amount (${tripDetails.currency} ${FOREX_CACHE[tripDetails.currency]?.symbol || ""})` : "Amount (₹)"}
                  value={cashAmount}
                  onChange={(e) => setCashAmount(e.target.value)}
                  style={{ flex: 1 }}
                  required
                />
                <input
                  type="text"
                  placeholder="Description"
                  value={cashDesc}
                  onChange={(e) => setCashDesc(e.target.value)}
                  style={{ flex: 2 }}
                  required
                />
              </div>

              {cashCurrencyMode === "FOREIGN" && tripDetails?.currency && cashAmount && !isNaN(parseFloat(cashAmount)) && (
                <p style={{ fontSize: "0.8rem", color: "#8a6d1c", textAlign: "left", marginTop: 4, marginBottom: 6, paddingLeft: 4 }}>
                  ≈ ₹{convertForeignToInr(parseFloat(cashAmount) || 0, tripDetails.currency, tripDetails.exchange_rate)} INR (1 {tripDetails.currency} = ₹{tripDetails.exchange_rate || getExchangeRate(tripDetails.currency)})
                </p>
              )}

              <div className="form-group" style={{ display: "flex", flexDirection: "column", gap: 6, textAlign: "left" }}>
                <label className="eyebrow" style={{ paddingLeft: 4 }}>Category Tag</label>
                <div style={{ display: "flex", gap: 8, alignItems: "center" }}>
                  {!showCustomCatInput ? (
                    <select
                      value={cashCategory}
                      onChange={(e) => setCashCategory(e.target.value)}
                      className="select-input"
                      style={{ flex: 1, padding: 12, borderRadius: 12, border: "0.5px solid var(--border-soft)", background: "white", outline: "none" }}
                    >
                      <option value="Dining">Dining</option>
                      <option value="Groceries">Groceries</option>
                      <option value="Transport">Transport</option>
                      <option value="Bills">Bills</option>
                      <option value="Shopping">Shopping</option>
                      <option value="Health">Health</option>
                      <option value="Rent">Rent</option>
                      <option value="Subscriptions">Subscriptions</option>
                      {customTags.map((tag) => (
                        <option key={tag} value={tag}>
                          {tag}
                        </option>
                      ))}
                    </select>
                  ) : (
                    <div className="custom-tag-input-group flex-1" style={{ display: "flex", gap: 8 }}>
                      <input
                        type="text"
                        placeholder="Custom Tag Name"
                        value={customCategory}
                        onChange={(e) => setCustomCategory(e.target.value)}
                        style={{ flex: 1, margin: 0 }}
                      />
                      <button
                        type="button"
                        onClick={handleAddCustomTag}
                        className="cta-button secondary"
                        style={{ padding: "8px 12px", minWidth: "auto", border: "0.5px solid var(--border-soft)", borderRadius: 12 }}
                      >
                        Add
                      </button>
                    </div>
                  )}

                  <button
                    type="button"
                    onClick={() => setShowCustomCatInput(!showCustomCatInput)}
                    className="cta-button icon-button"
                    title="Add Custom Category Tag"
                    style={{ padding: 8 }}
                  >
                    <span className="material-symbols-outlined">
                      {showCustomCatInput ? "close" : "add"}
                    </span>
                  </button>
                </div>
              </div>

              <div style={{ display: "flex", gap: 12, marginTop: 12 }}>
                <button
                  type="button"
                  onClick={() => setShowLogCashModal(false)}
                  className="auth-submit"
                  style={{ background: "var(--surface-low)", color: "var(--text-main)", border: "0.5px solid var(--border-soft)" }}
                >
                  Cancel
                </button>
                <button type="submit" disabled={submittingTx} className="auth-submit">
                  {submittingTx ? "Logging..." : "Log Cash Payment"}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
