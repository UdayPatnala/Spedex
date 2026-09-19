import type { AuthMode } from "../types";
import { BrandLockup } from "../components/layout/BrandLockup";

export function AuthView({
  mode,
  onModeChange,
  name,
  email,
  password,
  onNameChange,
  onEmailChange,
  onPasswordChange,
  onSubmit,
  submitting,
  error,
  warmingUp,
  onBackToLanding,
}: {
  mode: AuthMode;
  onModeChange: (mode: AuthMode) => void;
  name: string;
  email: string;
  password: string;
  onNameChange: (value: string) => void;
  onEmailChange: (value: string) => void;
  onPasswordChange: (value: string) => void;
  onSubmit: () => void;
  submitting: boolean;
  error: string | null;
  warmingUp: boolean;
  onBackToLanding?: () => void;
}) {
  return (
    <main className="auth-shell">
      <section className="auth-card">
        {onBackToLanding && (
          <button
            onClick={onBackToLanding}
            style={{
              alignSelf: "flex-start",
              background: "none",
              border: "none",
              color: "#38BDF8",
              cursor: "pointer",
              fontSize: "13px",
              fontWeight: 700,
              marginBottom: "12px",
              display: "flex",
              alignItems: "center",
              gap: "6px",
            }}
          >
            ← Back to Spedex Landing Page
          </button>
        )}
        <BrandLockup />
        <div className="auth-hero">
          <p className="eyebrow">User Portal</p>
          <h2 className="page-title">
            {mode === "login" ? "Sign in to your personal wallet" : "Create your smart workspace"}
          </h2>
          <p className="subtle">
            Track your subscriptions, utility bills, and daily expenses seamlessly with 1-tap payment routers.
          </p>
        </div>

        <div className="auth-toggle">
          <button
            className={`auth-toggle-button ${mode === "login" ? "active" : ""}`}
            onClick={() => onModeChange("login")}
            type="button"
          >
            Login
          </button>
          <button
            className={`auth-toggle-button ${mode === "signup" ? "active" : ""}`}
            onClick={() => onModeChange("signup")}
            type="button"
          >
            Sign up
          </button>
        </div>

        <div className="auth-form">
          {mode === "signup" ? (
            <input placeholder="Full name" value={name} onChange={(event) => onNameChange(event.target.value)} />
          ) : null}
          <input placeholder="Email" value={email} onChange={(event) => onEmailChange(event.target.value)} />
          <input
            placeholder="Password"
            type="password"
            value={password}
            onChange={(event) => onPasswordChange(event.target.value)}
          />
          {error ? <div className="auth-error">{error}</div> : null}
          <button className="auth-submit" type="button" onClick={onSubmit} disabled={submitting}>
            {submitting ? "Working..." : mode === "login" ? "Enter Spedex" : "Create Account"}
          </button>
        </div>

        {warmingUp && (
          <div className="error-alert" style={{ marginTop: 16, background: "rgba(245, 158, 11, 0.1)", border: "1px solid rgba(245, 158, 11, 0.2)", color: "#f59e0b" }}>
            <span className="material-symbols-outlined animate-spin" style={{ fontSize: "1.2rem" }}>sync</span>
            <span>Waking up Spedex server backend...</span>
          </div>
        )}
      </section>
    </main>
  );
}
