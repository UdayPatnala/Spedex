import React, { useState } from 'react';

export interface LandingPageProps {
  onLaunchDashboard: () => void;
}

export const LandingPage: React.FC<LandingPageProps> = ({ onLaunchDashboard }) => {
  const [demoAmount, setDemoAmount] = useState('450');
  const [demoVendor, setDemoVendor] = useState('Swiggy');
  const [paidStatus, setPaidStatus] = useState(false);

  const handleSimulatePay = () => {
    setPaidStatus(true);
    setTimeout(() => setPaidStatus(false), 3500);
  };

  return (
    <div
      style={{
        minHeight: '100vh',
        backgroundColor: '#F8FAFC',
        color: '#0F172A',
        fontFamily: "'Sora', -apple-system, BlinkMacSystemFont, sans-serif",
        display: 'flex',
        flexDirection: 'column',
        overflowX: 'hidden',
      }}
    >
      {/* Light Theme & Typography Synchronization Styles */}
      <style>{`
        @import url('https://fonts.googleapis.com/css2?family=Cormorant+Garamond:wght@600;700&family=Sora:wght@400;500;600;700;800&display=swap');
        
        .font-serif {
          font-family: 'Cormorant Garamond', serif;
        }
        .font-sans {
          font-family: 'Sora', sans-serif;
        }
        @keyframes subtleFadeUp {
          from { opacity: 0; transform: translateY(16px); }
          to { opacity: 1; transform: translateY(0); }
        }
        .animate-fade-up {
          animation: subtleFadeUp 0.6s cubic-bezier(0.16, 1, 0.3, 1) forwards;
        }
        .light-card {
          background: #FFFFFF;
          border: 1px solid rgba(15, 23, 42, 0.08);
          border-radius: 20px;
          box-shadow: 0 4px 20px rgba(0, 0, 0, 0.03), 0 1px 3px rgba(0, 0, 0, 0.02);
          transition: transform 0.25s cubic-bezier(0.16, 1, 0.3, 1), box-shadow 0.25s ease, border-color 0.25s ease;
        }
        .light-card:hover {
          transform: translateY(-4px);
          box-shadow: 0 16px 36px rgba(15, 23, 42, 0.08), 0 2px 6px rgba(15, 23, 42, 0.03);
          border-color: rgba(207, 48, 130, 0.3);
        }
        .btn-primary {
          background: linear-gradient(135deg, #CF3082 0%, #96225E 100%);
          color: #FFFFFF;
          border: none;
          border-radius: 12px;
          padding: 12px 24px;
          font-size: 14px;
          font-weight: 700;
          cursor: pointer;
          box-shadow: 0 4px 14px rgba(207, 48, 130, 0.25);
          transition: transform 0.2s ease, box-shadow 0.2s ease;
        }
        .btn-primary:hover {
          transform: translateY(-2px);
          box-shadow: 0 8px 22px rgba(207, 48, 130, 0.35);
        }
        .btn-secondary {
          background: #FFFFFF;
          color: #0F172A;
          border: 1px solid rgba(15, 23, 42, 0.12);
          border-radius: 12px;
          padding: 12px 24px;
          font-size: 14px;
          font-weight: 700;
          cursor: pointer;
          text-decoration: none;
          display: inline-flex;
          align-items: center;
          gap: 8px;
          transition: background-color 0.2s ease, border-color 0.2s ease, transform 0.2s ease;
        }
        .btn-secondary:hover {
          background: #F1F5F9;
          border-color: rgba(15, 23, 42, 0.2);
          transform: translateY(-2px);
        }
      `}</style>

      {/* Header Bar */}
      <header
        style={{
          position: 'sticky',
          top: 0,
          zIndex: 50,
          backdropFilter: 'blur(20px)',
          backgroundColor: 'rgba(255, 255, 255, 0.88)',
          borderBottom: '1px solid rgba(15, 23, 42, 0.08)',
          padding: '16px 36px',
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'center',
        }}
      >
        <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
          <img src="/spedex-mark.svg" alt="Spedex Logo" style={{ width: '34px', height: '34px' }} />
          <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
            <span className="font-serif" style={{ fontSize: '26px', fontWeight: 700, color: '#0F172A', letterSpacing: '-0.5px' }}>
              Spedex
            </span>
            <span
              className="font-sans"
              style={{
                fontSize: '10px',
                fontWeight: 700,
                backgroundColor: '#FBEAF4',
                color: '#CF3082',
                padding: '3px 8px',
                borderRadius: '10px',
                letterSpacing: '0.5px',
              }}
            >
              FINANCIAL SUITE
            </span>
          </div>
        </div>

        <nav style={{ display: 'flex', alignItems: 'center', gap: '32px' }}>
          <a href="#features" style={{ color: '#475569', textDecoration: 'none', fontSize: '14px', fontWeight: 600, transition: 'color 0.2s' }}>
            Features
          </a>
          <a href="#trips" style={{ color: '#475569', textDecoration: 'none', fontSize: '14px', fontWeight: 600, transition: 'color 0.2s' }}>
            Trips Ledger
          </a>
          <a href="#upi" style={{ color: '#475569', textDecoration: 'none', fontSize: '14px', fontWeight: 600, transition: 'color 0.2s' }}>
            UPI Desk
          </a>
          <a href="#demo" style={{ color: '#475569', textDecoration: 'none', fontSize: '14px', fontWeight: 600, transition: 'color 0.2s' }}>
            Live Demo
          </a>
        </nav>

        <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
          <a
            href="/downloads/spedex-release.apk"
            download="spedex-v2.2.0.apk"
            className="btn-secondary"
            style={{ padding: '9px 16px', fontSize: '13px' }}
          >
            <span>📥</span>
            <span>Download App</span>
          </a>
          <button onClick={onLaunchDashboard} className="btn-primary" style={{ padding: '9px 20px', fontSize: '13px' }}>
            Enter Workspace →
          </button>
        </div>
      </header>

      {/* Hero Section */}
      <section
        className="animate-fade-up"
        style={{
          padding: '80px 24px 60px 24px',
          maxWidth: '1000px',
          margin: '0 auto',
          textAlign: 'center',
        }}
      >
        <div
          style={{
            display: 'inline-flex',
            alignItems: 'center',
            gap: '8px',
            backgroundColor: '#F1F5F9',
            border: '1px solid rgba(15, 23, 42, 0.08)',
            padding: '6px 16px',
            borderRadius: '20px',
            fontSize: '12px',
            fontWeight: 700,
            color: '#CF3082',
            marginBottom: '24px',
          }}
        >
          <span>✨ Premium Personal Financial Workspace</span>
        </div>

        <h1
          className="font-serif"
          style={{
            fontSize: '58px',
            fontWeight: 700,
            lineHeight: 1.12,
            letterSpacing: '-1px',
            color: '#0F172A',
            margin: '0 0 20px 0',
          }}
        >
          The Elegant Smart Wallet for <br />
          <span style={{ color: '#CF3082', fontStyle: 'normal' }}>Modern Personal Finance</span>
        </h1>

        <p
          className="font-sans"
          style={{
            fontSize: '18px',
            color: '#475569',
            maxWidth: '680px',
            margin: '0 auto 36px auto',
            lineHeight: 1.6,
          }}
        >
          Effortlessly track daily expenses, manage active travel ledgers, settle vendor UPI payments, and monitor monthly budgets—all in a refined, distraction-free environment.
        </p>

        {/* Currency Palette Badges */}
        <div
          style={{
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center',
            gap: '10px',
            flexWrap: 'wrap',
            marginBottom: '40px',
          }}
        >
          <span style={{ fontSize: '11px', fontWeight: 700, color: '#94A3B8', textTransform: 'uppercase', letterSpacing: '1px', marginRight: '4px' }}>
            Curated Themes:
          </span>
          {[
            { name: '₹2000 Magenta', bg: '#FBEAF4', text: '#CF3082' },
            { name: '₹500 Stone', bg: '#F1F5F9', text: '#475569' },
            { name: '₹200 Yellow', bg: '#FEF3C7', text: '#D97706' },
            { name: '₹100 Lavender', bg: '#EDE9FE', text: '#7C3AED' },
            { name: '₹50 Cyan', bg: '#E0F2FE', text: '#0284C7' },
            { name: '₹20 Green', bg: '#D1FAE5', text: '#059669' },
          ].map((theme) => (
            <div
              key={theme.name}
              style={{
                display: 'flex',
                alignItems: 'center',
                gap: '6px',
                backgroundColor: theme.bg,
                padding: '5px 12px',
                borderRadius: '16px',
                fontSize: '11px',
                fontWeight: 700,
                color: theme.text,
              }}
            >
              <div style={{ width: '7px', height: '7px', borderRadius: '50%', backgroundColor: theme.text }} />
              <span>{theme.name}</span>
            </div>
          ))}
        </div>

        {/* Hero CTAs */}
        <div style={{ display: 'flex', justifyContent: 'center', gap: '16px', flexWrap: 'wrap' }}>
          <button onClick={onLaunchDashboard} className="btn-primary" style={{ padding: '14px 32px', fontSize: '15px' }}>
            Open Web Dashboard →
          </button>
          <a href="#demo" className="btn-secondary" style={{ padding: '14px 28px', fontSize: '15px' }}>
            <span>⚡ Interactive Demo</span>
          </a>
        </div>
      </section>

      {/* Clean Dashboard Preview Container */}
      <section
        style={{
          padding: '0 24px 80px 24px',
          maxWidth: '960px',
          margin: '0 auto',
          width: '100%',
        }}
      >
        <div className="light-card" style={{ padding: '24px' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '20px', paddingBottom: '16px', borderBottom: '1px solid rgba(15, 23, 42, 0.08)' }}>
            <div style={{ width: '10px', height: '10px', borderRadius: '50%', backgroundColor: '#EF4444' }} />
            <div style={{ width: '10px', height: '10px', borderRadius: '50%', backgroundColor: '#F59E0B' }} />
            <div style={{ width: '10px', height: '10px', borderRadius: '50%', backgroundColor: '#10B981' }} />
            <span style={{ marginLeft: '12px', fontSize: '12px', color: '#94A3B8', fontWeight: 600 }}>https://spe-dex.vercel.app/dashboard</span>
          </div>

          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(260px, 1fr))', gap: '20px' }}>
            <div style={{ backgroundColor: '#F8FAFC', padding: '20px', borderRadius: '16px', border: '1px solid rgba(15, 23, 42, 0.06)' }}>
              <div style={{ fontSize: '11px', fontWeight: 700, color: '#64748B', textTransform: 'uppercase' }}>MONTHLY OUTLOOK</div>
              <div className="font-serif" style={{ fontSize: '32px', fontWeight: 700, color: '#CF3082', margin: '4px 0' }}>₹14,250</div>
              <div style={{ fontSize: '12px', color: '#059669', fontWeight: 700 }}>68% of monthly limit used</div>
            </div>

            <div style={{ backgroundColor: '#EDE9FE', padding: '20px', borderRadius: '16px', border: '1px solid rgba(124, 58, 237, 0.15)' }}>
              <div style={{ fontSize: '11px', fontWeight: 800, color: '#7C3AED', textTransform: 'uppercase' }}>ACTIVE TRIP</div>
              <div className="font-serif" style={{ fontSize: '22px', fontWeight: 700, color: '#0F172A', margin: '4px 0' }}>✈️ Goa Beach Weekend</div>
              <div style={{ fontSize: '12px', color: '#475569' }}>Total: ₹8,400 | Cash Spent: ₹3,200</div>
            </div>

            <div style={{ backgroundColor: '#F8FAFC', padding: '20px', borderRadius: '16px', border: '1px solid rgba(15, 23, 42, 0.06)' }}>
              <div style={{ fontSize: '11px', fontWeight: 700, color: '#64748B', textTransform: 'uppercase', marginBottom: '8px' }}>QUICK VENDORS</div>
              <div style={{ display: 'flex', gap: '8px' }}>
                {['Swiggy', 'Uber', 'Starbucks'].map((name, idx) => (
                  <div key={idx} style={{ flex: 1, backgroundColor: '#FFFFFF', padding: '8px', borderRadius: '10px', textAlign: 'center', border: '1px solid rgba(15, 23, 42, 0.08)', fontSize: '11px', fontWeight: 700 }}>
                    {name}
                  </div>
                ))}
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* Feature Grid Section */}
      <section
        id="features"
        style={{
          padding: '70px 24px',
          maxWidth: '1040px',
          margin: '0 auto',
        }}
      >
        <div style={{ textAlign: 'center', marginBottom: '50px' }}>
          <span style={{ fontSize: '12px', fontWeight: 800, color: '#CF3082', textTransform: 'uppercase', letterSpacing: '1.2px' }}>
            CORE CAPABILITIES
          </span>
          <h2 className="font-serif" style={{ fontSize: '40px', fontWeight: 700, color: '#0F172A', marginTop: '8px', letterSpacing: '-0.5px' }}>
            Designed for Financial Clarity
          </h2>
          <p style={{ fontSize: '16px', color: '#475569', maxWidth: '560px', margin: '10px auto 0 auto' }}>
            Clean, high-performance tools built specifically for modern daily spending and travel.
          </p>
        </div>

        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(300px, 1fr))', gap: '24px' }}>
          <div className="light-card" style={{ padding: '28px' }}>
            <div style={{ width: '42px', height: '42px', borderRadius: '12px', backgroundColor: '#FBEAF4', color: '#CF3082', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: '20px', marginBottom: '16px' }}>
              ⚡
            </div>
            <h3 className="font-serif" style={{ fontSize: '22px', fontWeight: 700, color: '#0F172A', margin: '0 0 8px 0' }}>Instant UPI Quick Pay</h3>
            <p style={{ fontSize: '14px', color: '#475569', lineHeight: 1.6, margin: 0 }}>
              1-tap payment initiation with auto-generated QR codes for your frequent daily merchants.
            </p>
          </div>

          <div className="light-card" style={{ padding: '28px' }}>
            <div style={{ width: '42px', height: '42px', borderRadius: '12px', backgroundColor: '#EDE9FE', color: '#7C3AED', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: '20px', marginBottom: '16px' }}>
              ✈️
            </div>
            <h3 className="font-serif" style={{ fontSize: '22px', fontWeight: 700, color: '#0F172A', margin: '0 0 8px 0' }}>Active Trips Ledger</h3>
            <p style={{ fontSize: '14px', color: '#475569', lineHeight: 1.6, margin: 0 }}>
              Dedicated cash and card expense tracker for vacations with category breakdown summaries.
            </p>
          </div>

          <div className="light-card" style={{ padding: '28px' }}>
            <div style={{ width: '42px', height: '42px', borderRadius: '12px', backgroundColor: '#E0F2FE', color: '#0284C7', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: '20px', marginBottom: '16px' }}>
              🎯
            </div>
            <h3 className="font-serif" style={{ fontSize: '22px', fontWeight: 700, color: '#0F172A', margin: '0 0 8px 0' }}>Budget Guardrails</h3>
            <p style={{ fontSize: '14px', color: '#475569', lineHeight: 1.6, margin: 0 }}>
              Set monthly category limits across Dining, Rent, and Subscriptions with live pace indicators.
            </p>
          </div>

          <div className="light-card" style={{ padding: '28px' }}>
            <div style={{ width: '42px', height: '42px', borderRadius: '12px', backgroundColor: '#D1FAE5', color: '#059669', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: '20px', marginBottom: '16px' }}>
              📈
            </div>
            <h3 className="font-serif" style={{ fontSize: '22px', fontWeight: 700, color: '#0F172A', margin: '0 0 8px 0' }}>Financial Analytics</h3>
            <p style={{ fontSize: '14px', color: '#475569', lineHeight: 1.6, margin: 0 }}>
              Visualize weekly spending curves, peak day metrics, and upcoming subscription bill reminders.
            </p>
          </div>
        </div>
      </section>

      {/* Interactive Quick Pay Demo Widget */}
      <section
        id="demo"
        style={{
          padding: '50px 24px 80px 24px',
          maxWidth: '840px',
          margin: '0 auto',
          width: '100%',
        }}
      >
        <div className="light-card" style={{ padding: '36px', textAlign: 'center' }}>
          <span style={{ fontSize: '11px', fontWeight: 800, color: '#059669', textTransform: 'uppercase', letterSpacing: '1.2px' }}>
            LIVE DEMONSTRATION
          </span>
          <h3 className="font-serif" style={{ fontSize: '28px', fontWeight: 700, color: '#0F172A', margin: '6px 0 24px 0' }}>
            Simulate Quick Payment
          </h3>

          <div style={{ display: 'flex', gap: '16px', flexWrap: 'wrap', justifyContent: 'center', alignItems: 'center', marginBottom: '20px' }}>
            <div>
              <label style={{ fontSize: '12px', color: '#64748B', display: 'block', marginBottom: '4px', fontWeight: 600, textAlign: 'left' }}>Merchant</label>
              <input
                type="text"
                value={demoVendor}
                onChange={(e) => setDemoVendor(e.target.value)}
                style={{ backgroundColor: '#F8FAFC', border: '1px solid rgba(15, 23, 42, 0.12)', color: '#0F172A', padding: '10px 14px', borderRadius: '10px', fontSize: '14px', width: '180px' }}
              />
            </div>
            <div>
              <label style={{ fontSize: '12px', color: '#64748B', display: 'block', marginBottom: '4px', fontWeight: 600, textAlign: 'left' }}>Amount (₹)</label>
              <input
                type="number"
                value={demoAmount}
                onChange={(e) => setDemoAmount(e.target.value)}
                style={{ backgroundColor: '#F8FAFC', border: '1px solid rgba(15, 23, 42, 0.12)', color: '#0F172A', padding: '10px 14px', borderRadius: '10px', fontSize: '14px', width: '140px' }}
              />
            </div>
            <div style={{ marginTop: '18px' }}>
              <button
                onClick={handleSimulatePay}
                className="btn-primary"
                style={{ backgroundColor: paidStatus ? '#059669' : '#CF3082', padding: '11px 24px' }}
              >
                {paidStatus ? '✓ Processed!' : `Pay ₹${demoAmount} via UPI`}
              </button>
            </div>
          </div>

          {paidStatus && (
            <div style={{ backgroundColor: '#D1FAE5', color: '#059669', border: '1px solid rgba(5, 150, 105, 0.2)', padding: '12px', borderRadius: '12px', fontSize: '13px', fontWeight: 700 }}>
              🎉 Payment of ₹{demoAmount} to {demoVendor} processed and synced!
            </div>
          )}
        </div>
      </section>

      {/* Footer */}
      <footer
        style={{
          marginTop: 'auto',
          backgroundColor: '#FFFFFF',
          borderTop: '1px solid rgba(15, 23, 42, 0.08)',
          padding: '40px 32px',
          textAlign: 'center',
        }}
      >
        <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', gap: '10px', marginBottom: '12px' }}>
          <img src="/spedex-mark.svg" alt="Spedex Logo" style={{ width: '24px', height: '24px' }} />
          <span className="font-serif" style={{ fontSize: '20px', fontWeight: 700, color: '#0F172A' }}>Spedex Smart Wallet</span>
        </div>
        <p style={{ fontSize: '13px', color: '#64748B', margin: '0 0 16px 0' }}>
          © 2026 SpeDex Inc. All rights reserved. ● Enterprise Personal Financial Workspace.
        </p>
        <div style={{ display: 'flex', justifyContent: 'center', gap: '20px' }}>
          <button onClick={onLaunchDashboard} style={{ background: 'none', border: 'none', color: '#CF3082', cursor: 'pointer', fontSize: '13px', fontWeight: 700 }}>
            Launch Web Workspace
          </button>
          <a href="/downloads/spedex-release.apk" download="spedex-v2.2.0.apk" style={{ color: '#059669', textDecoration: 'none', fontSize: '13px', fontWeight: 700 }}>
            Download Android APK
          </a>
        </div>
      </footer>
    </div>
  );
};
