import type { AnalyticsData } from "../types";
import {
  accentClass,
  categoryEmoji,
  categoryLabel,
  formatCurrency,
} from "../utils/formatters";

export function AnalyticsView({ analytics }: { analytics: AnalyticsData }) {
  if (analytics.total_spent === 0) {
    return (
      <div className="analytics-shell">
        <section className="card" style={{ textAlign: "center", padding: "4rem 2rem" }}>
          <span className="material-symbols-outlined" style={{ fontSize: 64, opacity: 0.3, marginBottom: 16 }}>insights</span>
          <h2 className="page-title">No spending data yet.</h2>
          <p className="subtle">Record transactions to unlock intelligent insights, category breakdowns, and weekly pacing metrics.</p>
        </section>
      </div>
    );
  }

  const maxAmount = Math.max(...analytics.weekly_spend.map((item) => item.amount), 1);

  return (
    <div className="analytics-shell">
      <section className="card">
        <div className="section-header">
          <div>
            <p className="eyebrow">Signals</p>
            <h2 className="page-title section-title">Monthly Shape</h2>
          </div>
          <span className="status-chip">Current cycle</span>
        </div>
        <div className="analytics-grid">
          <div className="hero-ring">
            <div className="donut" />
            <div className="donut-content">
              <p className="eyebrow">Total spent</p>
              <h3 className="headline donut-value">{formatCurrency(analytics.total_spent)}</h3>
            </div>
          </div>
          <div className="soft-panel">
            <p className="eyebrow">Smart insight</p>
            <h3 className="headline panel-copy">{analytics.smart_insight}</h3>
          </div>
          <div className="full-width soft-panel">
            <div className="section-header">
              <h3 className="page-title section-title">Weekly Spend</h3>
              <span className="status-chip soft">Week 4 active</span>
            </div>
            <div className="analytics-bars">
              {analytics.weekly_spend.map((week) => (
                <div key={week.week_label} className={`bar-column ${week.is_active ? "active" : ""}`}>
                  <span style={{ height: `${Math.max((week.amount / maxAmount) * 100, 15)}%` }} />
                  <small className="subtle">{week.week_label}</small>
                </div>
              ))}
            </div>
          </div>
          {[analytics.highest_sector, analytics.busiest_day].map((card) => (
            <div className="soft-panel" key={card.title}>
              <p className="eyebrow">{card.title === analytics.highest_sector.title ? "Highest sector" : "Peak day"}</p>
              <h3 className="headline panel-copy compact">{card.title}</h3>
              <p className="subtle">{card.subtitle}</p>
            </div>
          ))}
          <div className="full-width card category-panel">
            <div className="section-header">
              <h3 className="page-title section-title">Category Breakdown</h3>
              <span className="subtle">
                {analytics.weekday_ratio}% weekday / {analytics.weekend_ratio}% weekend
              </span>
            </div>
            <div className="transactions-list">
              {analytics.category_breakdown.map((item) => (
                <div key={item.category} className="transaction-row">
                  <div className={`icon-badge ${accentClass(item.accent)}`}>
                    <span className="emoji-glyph">{categoryEmoji(item.category)}</span>
                  </div>
                  <div>
                    <p className="transaction-title">{categoryLabel(item.category)}</p>
                    <div className="mini-progress">
                      <span style={{ width: `${item.percentage}%` }} />
                    </div>
                  </div>
                  <strong>{item.percentage}%</strong>
                </div>
              ))}
            </div>
          </div>
        </div>
      </section>
    </div>
  );
}
