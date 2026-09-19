import type { DashboardOverview, Transaction, Vendor } from "../types";
import {
  accentClass,
  categoryEmoji,
  categoryLabel,
  clampPercent,
  formatCurrency,
  formatDate,
  formatTime,
} from "../utils/formatters";

export function QuickPayCard({ vendor }: { vendor: Vendor }) {
  return (
    <button className="quick-pay-card" type="button">
      <div className={`icon-badge ${accentClass(vendor.accent)}`}>
        <span className="emoji-glyph">{categoryEmoji(vendor.category)}</span>
      </div>
      <strong>{vendor.name}</strong>
      <span className="subtle">{categoryLabel(vendor.category)}</span>
      <span className="amount-pill">{formatCurrency(vendor.default_amount)}</span>
    </button>
  );
}

export function HomeView({
  overview,
  filteredTransactions,
  onAddVendor,
}: {
  overview: DashboardOverview;
  filteredTransactions: Transaction[];
  onAddVendor: () => void;
}) {
  const weeklyMax = Math.max(...(overview?.weekly_spending || [1000]), 1);

  return (
    <div className="dashboard-grid">
      <section className="hero-panel full-width">
        <div>
          <p className="eyebrow">Smart Wallet</p>
          <h2 className="page-title hero-title">Take control of your spending, one tap at a time.</h2>
          <p className="hero-copy">
            Spedex connects your frequent payees, monthly rent reminders, and spending trends into a single tap interface.
          </p>
        </div>
        <div className="hero-metrics">
          <div>
            <span className="metric-label">Monthly spending</span>
            <strong>{formatCurrency(overview.monthly_total)}</strong>
          </div>
          <div>
            <span className="metric-label">Weekly average</span>
            <strong>{formatCurrency(overview.weekly_average)}</strong>
          </div>
        </div>
      </section>

      <div className="left-column">
        <div className="kpi-strip">
          <section className="card spotlight-card">
            <p className="eyebrow">Monthly Spending</p>
            <h2 className="headline">{formatCurrency(overview.monthly_total)}</h2>
            <p className="subtle">Live expense total across your current monthly cycle.</p>
          </section>

          <section className="card">
            <div className="section-header">
              <div>
                <p className="eyebrow">Budget Pace</p>
                <strong>{Math.round(overview.budget_used_ratio * 100)}% used</strong>
              </div>
              <strong>{formatCurrency(overview.monthly_budget)}</strong>
            </div>
            <div className="budget-bar">
              <span style={{ width: `${clampPercent(overview.budget_used_ratio)}%` }} />
            </div>
            <p className="subtle">{overview.budget_copy}</p>
          </section>
        </div>

        <section className="card">
          <div className="section-header">
            <h3 className="page-title section-title">Quick Pay</h3>
            <span className="status-chip soft">3 favourites</span>
          </div>
          <div className="quick-grid">
            {(overview?.quick_pay || []).map((vendor) => (
              <QuickPayCard key={vendor.id} vendor={vendor} />
            ))}
            <button className="quick-pay-card add" type="button" onClick={onAddVendor}>
              <div className="icon-badge accent-lavender">
                <span className="material-symbols-outlined">add</span>
              </div>
              <strong>Add payee</strong>
              <span className="subtle">Store a new UPI favourite</span>
            </button>
          </div>
        </section>

        <section className="card">
          <div className="transactions-header">
            <h3 className="page-title section-title">Recent Transactions</h3>
            <span className="status-chip soft">Filtered live</span>
          </div>
          <div className="transactions-list">
            {filteredTransactions.length === 0 ? (
              <div style={{ textAlign: "center", padding: "2rem", color: "var(--text-subtle)", background: "rgba(255,255,255,0.4)", borderRadius: 12 }}>
                <span className="material-symbols-outlined" style={{ fontSize: 48, opacity: 0.5, marginBottom: 8 }}>receipt_long</span>
                <p>No transactions yet.</p>
                <p className="subtle">Record an expense or pay a vendor to see it here.</p>
              </div>
            ) : filteredTransactions.map((transaction) => (
              <div key={transaction.id} className="transaction-row">
                <div className={`icon-badge ${transaction.direction === "income" ? "accent-mint" : "accent-rose"}`}>
                  <span className="emoji-glyph">
                    {transaction.direction === "income" ? categoryEmoji("Income") : categoryEmoji(transaction.category)}
                  </span>
                </div>
                <div>
                  <p className="transaction-title">{transaction.description}</p>
                  <span className="subtle">
                    {formatDate(transaction.occurred_at)} | {formatTime(transaction.occurred_at)} |{" "}
                    {categoryLabel(transaction.category)}
                  </span>
                </div>
                <strong className={transaction.direction === "income" ? "amount-positive" : "amount-negative"}>
                  {transaction.direction === "income" ? "+" : "-"}
                  {formatCurrency(transaction.amount)}
                </strong>
              </div>
            ))}
          </div>
        </section>
      </div>

      <div className="right-column">
        <section className="card weekly-panel">
          <div className="section-header">
            <p className="eyebrow light">Weekly Spending</p>
            <span className="status-chip inverse">Rolling 4 weeks</span>
          </div>
          <div className="weekly-bars">
            {overview.weekly_spending.map((value, index) => (
              <span
                key={`${value}-${index}`}
                className={index === overview.weekly_spending.length - 1 ? "active" : ""}
                style={{ height: `${Math.max((value / weeklyMax) * 100, 20)}%` }}
              />
            ))}
          </div>
          <div className="section-header">
            <div>
              <p className="subtle light">Peak day</p>
              <strong>{overview.peak_day_label}</strong>
            </div>
            <div>
              <p className="subtle light">Security</p>
              <strong>UPI Protected</strong>
            </div>
          </div>
        </section>

        <section className="card">
          <div className="section-header">
            <h3 className="page-title section-title">Upcoming Reminders</h3>
            <span className="status-chip soft">AutoPay aware</span>
          </div>
          <div className="reminders-list">
            {(overview?.reminders || []).map((reminder) => (
              <div key={reminder.id} className="reminder-row">
                <div className="icon-badge accent-lavender">
                  <span className="emoji-glyph">{"\u{1F4C5}"}</span>
                </div>
                <div>
                  <p className="reminder-title">{reminder.title}</p>
                  <span className="subtle">
                    {reminder.subtitle} | {formatDate(reminder.due_date)}
                  </span>
                </div>
                <strong>{formatCurrency(reminder.amount)}</strong>
              </div>
            ))}
          </div>
        </section>

        <section className="card security-card">
          <div className="section-header">
            <span className="material-symbols-outlined">shield_locked</span>
            <strong>Security Update</strong>
          </div>
          <p>{overview.security_message}</p>
        </section>
      </div>
    </div>
  );
}
