import React, { useState } from "react";
import { formatCurrency } from "../../utils/formatters";

interface SpendingVelocityWidgetProps {
  totalSpent: number;
  monthlyBudget?: number;
  daysPassed?: number;
  totalDaysInMonth?: number;
}

export const SpendingVelocityWidget: React.FC<SpendingVelocityWidgetProps> = ({
  totalSpent,
  monthlyBudget = 25000,
  daysPassed = Math.max(1, new Date().getDate()),
  totalDaysInMonth = new Date(new Date().getFullYear(), new Date().getMonth() + 1, 0).getDate(),
}) => {
  // Current velocity calculations
  const actualDailyPace = Math.round(totalSpent / daysPassed);
  const targetDailyPace = Math.round(monthlyBudget / totalDaysInMonth);
  const remainingBudget = Math.max(0, monthlyBudget - totalSpent);
  const remainingDays = Math.max(1, totalDaysInMonth - daysPassed);
  const sustainableDailyPace = Math.round(remainingBudget / remainingDays);

  // Velocity ratio: actual pace vs target pace
  const velocityRatio = targetDailyPace > 0 ? actualDailyPace / targetDailyPace : 1;
  const isOverpacing = actualDailyPace > sustainableDailyPace;

  // Simulator state
  const [simulatedDailyExtra, setSimulatedDailyExtra] = useState<number>(0);
  const simulatedPace = actualDailyPace + simulatedDailyExtra;
  const projectedTotal = totalSpent + (simulatedPace * remainingDays);
  const projectedSavings = monthlyBudget - projectedTotal;

  // SVG Gauge parameters (angle: -90deg to +90deg)
  const clampedRatio = Math.min(Math.max(velocityRatio, 0.2), 2.2);
  const gaugeAngle = -90 + ((clampedRatio - 0.2) / 2.0) * 180;

  return (
    <div
      className="card full-width"
      style={{
        background: "linear-gradient(135deg, rgba(13, 40, 24, 0.95) 0%, rgba(10, 25, 47, 0.95) 100%)",
        border: "1px solid rgba(212, 175, 55, 0.3)",
        borderRadius: "20px",
        padding: "28px",
        color: "#FDFBF7",
        boxShadow: "0 12px 36px rgba(0, 0, 0, 0.25)",
        marginTop: "20px",
      }}
    >
      {/* Header */}
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "flex-start", flexWrap: "wrap", gap: "12px", marginBottom: "24px" }}>
        <div>
          <div style={{ display: "flex", alignItems: "center", gap: "8px", marginBottom: "4px" }}>
            <span style={{ fontSize: "12px", fontWeight: 800, color: "#D4AF37", textTransform: "uppercase", letterSpacing: "1.2px" }}>
              VELOCITY ENGINE
            </span>
            <span
              style={{
                fontSize: "11px",
                fontWeight: 700,
                padding: "2px 8px",
                borderRadius: "12px",
                backgroundColor: isOverpacing ? "rgba(207, 48, 130, 0.2)" : "rgba(5, 150, 105, 0.2)",
                color: isOverpacing ? "#CF3082" : "#10B981",
                border: `1px solid ${isOverpacing ? "rgba(207, 48, 130, 0.4)" : "rgba(5, 150, 105, 0.4)"}`,
              }}
            >
              {isOverpacing ? "⚡ Elevated Burn Rate" : "✓ Sustainable Pace"}
            </span>
          </div>
          <h3 className="font-serif" style={{ fontSize: "28px", fontWeight: 700, margin: 0, color: "#FDFBF7" }}>
            Real-Time Spending Velocity
          </h3>
        </div>

        <div style={{ textAlign: "right" }}>
          <div style={{ fontSize: "11px", color: "rgba(253, 251, 247, 0.6)", textTransform: "uppercase" }}>CURRENT RUN RATE</div>
          <div className="font-serif" style={{ fontSize: "24px", fontWeight: 700, color: "#D4AF37" }}>
            {formatCurrency(actualDailyPace)} <span style={{ fontSize: "13px", fontWeight: 500, color: "rgba(253, 251, 247, 0.7)" }}>/ day</span>
          </div>
        </div>
      </div>

      {/* Main Grid: Gauge + Metrics */}
      <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fit, minmax(280px, 1fr))", gap: "28px", alignItems: "center" }}>
        
        {/* SVG Semi-Circle Velocity Gauge */}
        <div style={{ display: "flex", flexDirection: "column", alignItems: "center", justifyContent: "center", position: "relative" }}>
          <svg width="220" height="125" viewBox="0 0 220 125" style={{ overflow: "visible" }}>
            {/* Background Arc */}
            <path
              d="M 20 110 A 90 90 0 0 1 200 110"
              fill="none"
              stroke="rgba(255, 255, 255, 0.1)"
              strokeWidth="16"
              strokeLinecap="round"
            />
            {/* Green Zone (Conservative: 0.2 to 0.8) */}
            <path
              d="M 20 110 A 90 90 0 0 1 74 38"
              fill="none"
              stroke="#059669"
              strokeWidth="16"
              strokeLinecap="round"
            />
            {/* Yellow Zone (Target: 0.8 to 1.3) */}
            <path
              d="M 74 38 A 90 90 0 0 1 146 38"
              fill="none"
              stroke="#D4AF37"
              strokeWidth="16"
            />
            {/* Magenta Zone (High Velocity: 1.3 to 2.2) */}
            <path
              d="M 146 38 A 90 90 0 0 1 200 110"
              fill="none"
              stroke="#CF3082"
              strokeWidth="16"
              strokeLinecap="round"
            />
            {/* Needle Pivot */}
            <circle cx="110" cy="110" r="8" fill="#FDFBF7" />
            {/* Animated Needle */}
            <line
              x1="110"
              y1="110"
              x2="110"
              y2="30"
              stroke="#FDFBF7"
              strokeWidth="3.5"
              strokeLinecap="round"
              transform={`rotate(${gaugeAngle} 110 110)`}
              style={{ transition: "transform 0.6s cubic-bezier(0.16, 1, 0.3, 1)" }}
            />
          </svg>

          <div style={{ textAlign: "center", marginTop: "12px" }}>
            <span style={{ fontSize: "14px", fontWeight: 700, color: isOverpacing ? "#CF3082" : "#10B981" }}>
              {(velocityRatio * 100).toFixed(0)}% of Target Pace
            </span>
            <div style={{ fontSize: "12px", color: "rgba(253, 251, 247, 0.6)", marginTop: "2px" }}>
              Sustainable ceiling: {formatCurrency(sustainableDailyPace)}/day
            </div>
          </div>
        </div>

        {/* Pacing Breakdown Cards */}
        <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: "14px" }}>
          <div style={{ backgroundColor: "rgba(255, 255, 255, 0.05)", padding: "16px", borderRadius: "14px", border: "1px solid rgba(255, 255, 255, 0.08)" }}>
            <div style={{ fontSize: "11px", color: "rgba(253, 251, 247, 0.6)", textTransform: "uppercase" }}>TARGET DAILY CAP</div>
            <div className="font-serif" style={{ fontSize: "20px", fontWeight: 700, color: "#FDFBF7", marginTop: "4px" }}>
              {formatCurrency(targetDailyPace)}
            </div>
            <div style={{ fontSize: "11px", color: "#94A3B8", marginTop: "2px" }}>For 30-day budget</div>
          </div>

          <div style={{ backgroundColor: "rgba(255, 255, 255, 0.05)", padding: "16px", borderRadius: "14px", border: "1px solid rgba(255, 255, 255, 0.08)" }}>
            <div style={{ fontSize: "11px", color: "rgba(253, 251, 247, 0.6)", textTransform: "uppercase" }}>REMAINING DAYS</div>
            <div className="font-serif" style={{ fontSize: "20px", fontWeight: 700, color: "#FDFBF7", marginTop: "4px" }}>
              {remainingDays} days
            </div>
            <div style={{ fontSize: "11px", color: "#94A3B8", marginTop: "2px" }}>Cycle balance: {formatCurrency(remainingBudget)}</div>
          </div>

          <div style={{ gridColumn: "1 / -1", backgroundColor: "rgba(212, 175, 55, 0.08)", padding: "16px", borderRadius: "14px", border: "1px solid rgba(212, 175, 55, 0.2)" }}>
            <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: "8px" }}>
              <span style={{ fontSize: "12px", fontWeight: 700, color: "#D4AF37" }}>
                Interactive Forecast Simulator
              </span>
              <span style={{ fontSize: "12px", fontWeight: 700, color: simulatedDailyExtra >= 0 ? "#FDFBF7" : "#10B981" }}>
                {simulatedDailyExtra > 0 ? `+${formatCurrency(simulatedDailyExtra)}/day` : simulatedDailyExtra < 0 ? `${formatCurrency(simulatedDailyExtra)}/day` : "Baseline Pace"}
              </span>
            </div>

            <input
              type="range"
              min="-500"
              max="1000"
              step="50"
              value={simulatedDailyExtra}
              onChange={(e) => setSimulatedDailyExtra(Number(e.target.value))}
              style={{ width: "100%", accentColor: "#D4AF37", cursor: "pointer" }}
            />

            <div style={{ display: "flex", justifyContent: "space-between", fontSize: "11px", color: "rgba(253, 251, 247, 0.7)", marginTop: "8px" }}>
              <span>Projected Month Total: <strong style={{ color: "#FDFBF7" }}>{formatCurrency(projectedTotal)}</strong></span>
              <span>Projected Net: <strong style={{ color: projectedSavings >= 0 ? "#10B981" : "#CF3082" }}>{projectedSavings >= 0 ? `+${formatCurrency(projectedSavings)}` : `-${formatCurrency(Math.abs(projectedSavings))}`}</strong></span>
            </div>
          </div>
        </div>

      </div>
    </div>
  );
};
