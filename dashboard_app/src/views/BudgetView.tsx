import type { BudgetScreenData } from "../types";
import {
  accentClass,
  categoryEmoji,
  categoryLabel,
  clampPercent,
  formatCurrency,
  formatDate,
} from "../utils/formatters";

export function BudgetView({ budget }: { budget: BudgetScreenData }) {
  return (
    <div className="budget-shell">
      <section className="card">
        <div className="section-header">
          <div>
            <p className="eyebrow">Planning</p>
            <h2 className="page-title section-title">Budget Map</h2>
          </div>
          <div>
            <p className="eyebrow">Remaining budget</p>
            <strong>{formatCurrency(budget.remaining_budget)}</strong>
          </div>
        </div>
        <div className="budget-grid">
          {budget.budgets.map((item) => (
            <div key={item.id} className="budget-card">
              <div className={`icon-badge ${accentClass(item.accent)}`}>
                <span className="emoji-glyph">{categoryEmoji(item.category)}</span>
              </div>
              <div>
                <p className="transaction-title">{categoryLabel(item.category)}</p>
                <p className="subtle">
                  {formatCurrency(item.spent)} / {formatCurrency(item.limit_amount)}
                </p>
                <div className="progress-line">
                  <span style={{ width: `${clampPercent(item.progress)}%` }} />
                </div>
              </div>
              <strong>{Math.round(item.progress * 100)}%</strong>
            </div>
          ))}
        </div>
      </section>

      <section className="card">
        <div className="section-header">
          <h2 className="page-title section-title">Upcoming Reminders</h2>
          <span className="status-chip soft">Calendar linked</span>
        </div>
        <div className="reminders-list">
          {budget.reminders.map((reminder) => (
            <div key={reminder.id} className="reminder-row">
              <div className="icon-badge accent-lavender">
                <span className="emoji-glyph">{"\u{23F0}"}</span>
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

      <section className="card weekly-panel">
        <p className="eyebrow light">Saving Tip</p>
        <h3 className="headline white-copy">Protect more breathing room</h3>
        <p>{budget.savings_tip}</p>
      </section>
    </div>
  );
}
