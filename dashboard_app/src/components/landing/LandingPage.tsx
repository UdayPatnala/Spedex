import React, { useState } from 'react';

export interface LandingPageProps {
  onLaunchDashboard: () => void;
  onOpenMobileSync?: () => void;
}

export const LandingPage: React.FC<LandingPageProps> = ({
  onLaunchDashboard,
  onOpenMobileSync,
}) => {
  const [demoAmount, setDemoAmount] = useState('450');
  const [demoVendor, setDemoVendor] = useState('Swiggy');
  const [paidStatus, setPaidStatus] = useState(false);
  const [activeFeatureIndex, setActiveFeatureIndex] = useState<number | null>(null);

  const handleSimulatePay = () => {
    setPaidStatus(true);
    setTimeout(() => setPaidStatus(false), 3500);
  };

  return (
    <div
      style={{
        minHeight: '100vh',
        backgroundColor: '#090D16',
        color: '#F8FAFC',
        fontFamily: "'Sora', -apple-system, BlinkMacSystemFont, sans-serif",
        display: 'flex',
        flexDirection: 'column',
        overflowX: 'hidden',
        position: 'relative',
      }}
    >
      {/* Inline Keyframe Styles for Rich Effects & Micro-Animations */}
      <style>{`
        @keyframes floatSlow {
          0%, 100% { transform: translateY(0px) rotate(0deg); }
          50% { transform: translateY(-12px) rotate(1deg); }
        }
        @keyframes pulseGlow {
          0%, 100% { opacity: 0.4; transform: scale(1); }
          50% { opacity: 0.8; transform: scale(1.05); }
        }
        @keyframes shimmerGradient {
          0% { background-position: 0% 50%; }
          50% { background-position: 100% 50%; }
          100% { background-position: 0% 50%; }
        }
        @keyframes ripplePay {
          0% { transform: scale(0.95); opacity: 0.7; }
          50% { transform: scale(1.02); opacity: 1; }
          100% { transform: scale(1); opacity: 1; }
        }
        .animated-gradient-text {
          background: linear-gradient(135deg, #FFFFFF 0%, #EC4899 35%, #F59E0B 70%, #06B6D4 100%);
          background-size: 200% 200%;
          animation: shimmerGradient 6s ease infinite;
          -webkit-background-clip: text;
          -webkit-text-fill-color: transparent;
        }
        .hover-card-glow {
          transition: transform 0.3s cubic-bezier(0.16, 1, 0.3, 1), box-shadow 0.3s ease, border-color 0.3s ease;
        }
        .hover-card-glow:hover {
          transform: translateY(-8px) scale(1.01);
          box-shadow: 0 20px 40px -15px rgba(236, 72, 153, 0.25), 0 0 20px rgba(139, 92, 246, 0.2);
          border-color: rgba(236, 72, 153, 0.4) !important;
        }
        .btn-hover-effect {
          transition: transform 0.2s cubic-bezier(0.16, 1, 0.3, 1), box-shadow 0.2s ease, filter 0.2s ease;
        }
        .btn-hover-effect:hover {
          transform: translateY(-2px);
          filter: brightness(1.1);
          box-shadow: 0 10px 25px -5px rgba(236, 72, 153, 0.4);
        }
        .floating-element {
          animation: floatSlow 5s ease-in-out infinite;
        }
      `}</style>

      {/* Ambient background glow spheres */}
      <div
        style={{
          position: 'absolute',
          top: '-150px',
          left: '50%',
          transform: 'translateX(-50%)',
          width: '800px',
          height: '400px',
          background: 'radial-gradient(circle, rgba(236, 72, 153, 0.18) 0%, rgba(139, 92, 246, 0.12) 40%, rgba(9, 13, 22, 0) 75%)',
          filter: 'blur(70px)',
          pointerEvents: 'none',
          zIndex: 0,
          animation: 'pulseGlow 8s ease-in-out infinite',
        }}
      />
      <div
        style={{
          position: 'absolute',
          top: '700px',
          right: '-120px',
          width: '550px',
          height: '550px',
          background: 'radial-gradient(circle, rgba(6, 182, 212, 0.15) 0%, rgba(16, 185, 129, 0.1) 50%, rgba(9, 13, 22, 0) 75%)',
          filter: 'blur(80px)',
          pointerEvents: 'none',
          zIndex: 0,
          animation: 'pulseGlow 10s ease-in-out infinite 2s',
        }}
      />

      {/* Header Navigation */}
      <header
        style={{
          position: 'sticky',
          top: 0,
          zIndex: 50,
          backdropFilter: 'blur(24px)',
          backgroundColor: 'rgba(9, 13, 22, 0.85)',
          borderBottom: '1px solid rgba(255, 255, 255, 0.08)',
          padding: '16px 32px',
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'center',
          transition: 'background-color 0.3s ease',
        }}
      >
        <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
          <img src="/spedex-mark.svg" alt="Spedex Logo" style={{ width: '34px', height: '34px', filter: 'drop-shadow(0 0 10px rgba(236, 72, 153, 0.5))' }} />
          <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
            <span style={{ fontSize: '22px', fontWeight: 800, tracking: '-0.5px', background: 'linear-gradient(135deg, #FFF 30%, #EC4899 100%)', WebkitBackgroundClip: 'text', WebkitTextFillColor: 'transparent' }}>
              Spedex
            </span>
            <span
              style={{
                fontSize: '10px',
                fontWeight: 800,
                backgroundColor: 'rgba(236, 72, 153, 0.18)',
                color: '#EC4899',
                border: '1px solid rgba(236, 72, 153, 0.4)',
                padding: '3px 9px',
                borderRadius: '12px',
                letterSpacing: '0.6px',
                boxShadow: '0 0 12px rgba(236, 72, 153, 0.25)',
              }}
            >
              v2.2 PRO
            </span>
          </div>
        </div>

        <nav style={{ display: 'flex', alignItems: 'center', gap: '28px' }}>
          <a href="#features" style={{ color: '#94A3B8', textDecoration: 'none', fontSize: '14px', fontWeight: 600, transition: 'color 0.2s' }}>
            Features
          </a>
          <a href="#trips" style={{ color: '#94A3B8', textDecoration: 'none', fontSize: '14px', fontWeight: 600, transition: 'color 0.2s' }}>
            Trips Ledger
          </a>
          <a href="#upi" style={{ color: '#94A3B8', textDecoration: 'none', fontSize: '14px', fontWeight: 600, transition: 'color 0.2s' }}>
            UPI Quick Pay
          </a>
          <a href="#interactive-demo" style={{ color: '#94A3B8', textDecoration: 'none', fontSize: '14px', fontWeight: 600, transition: 'color 0.2s' }}>
            Live Demo
          </a>
        </nav>

        <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
          <a
            href="/downloads/spedex-release.apk"
            download="spedex-v2.2.0.apk"
            className="btn-hover-effect"
            style={{
              backgroundColor: 'rgba(16, 185, 129, 0.14)',
              color: '#10B981',
              border: '1px solid rgba(16, 185, 129, 0.4)',
              padding: '10px 18px',
              borderRadius: '12px',
              fontSize: '13px',
              fontWeight: 700,
              textDecoration: 'none',
              display: 'inline-flex',
              alignItems: 'center',
              gap: '6px',
            }}
          >
            <span>📥</span>
            <span>Download Mobile APK</span>
          </a>
          <button
            onClick={onLaunchDashboard}
            className="btn-hover-effect"
            style={{
              background: 'linear-gradient(135deg, #EC4899 0%, #8B5CF6 100%)',
              color: '#FFF',
              border: 'none',
              padding: '10px 22px',
              borderRadius: '12px',
              fontSize: '13px',
              fontWeight: 700,
              cursor: 'pointer',
              boxShadow: '0 4px 20px rgba(236, 72, 153, 0.35)',
            }}
          >
            Launch Web App →
          </button>
        </div>
      </header>

      {/* Hero Section */}
      <section
        style={{
          position: 'relative',
          zIndex: 10,
          padding: '90px 24px 70px 24px',
          maxWidth: '1100px',
          margin: '0 auto',
          textAlign: 'center',
        }}
      >
        <div
          className="floating-element"
          style={{
            display: 'inline-flex',
            alignItems: 'center',
            gap: '8px',
            backgroundColor: 'rgba(255, 255, 255, 0.06)',
            backdropFilter: 'blur(12px)',
            border: '1px solid rgba(255, 255, 255, 0.12)',
            padding: '7px 18px',
            borderRadius: '24px',
            fontSize: '12px',
            fontWeight: 700,
            color: '#38BDF8',
            marginBottom: '28px',
            boxShadow: '0 8px 25px rgba(0,0,0,0.3)',
          }}
        >
          <span style={{ fontSize: '14px' }}>⚡</span>
          <span>Next-Gen Financial Intelligence & Smart Wallet</span>
        </div>

        <h1
          style={{
            fontSize: '56px',
            fontWeight: 800,
            lineHeight: 1.12,
            letterSpacing: '-1.8px',
            margin: '0 0 24px 0',
          }}
        >
          Smart Financial Control for <br />
          <span className="animated-gradient-text">
            Trips, UPI Quick-Pay & Analytics
          </span>
        </h1>

        <p
          style={{
            fontSize: '19px',
            color: '#94A3B8',
            maxWidth: '740px',
            margin: '0 auto 40px auto',
            lineHeight: 1.65,
          }}
        >
          SpeDex brings seamless 1-tap UPI vendor payments, live cash trip ledgers, monthly budget guardrails, and real-time mobile-to-web synchronization into one sleek workspace.
        </p>

        {/* Indian Rupee Note Currency Aesthetics Palette Strip */}
        <div
          style={{
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center',
            gap: '10px',
            flexWrap: 'wrap',
            marginBottom: '44px',
          }}
        >
          <span style={{ fontSize: '11px', fontWeight: 800, color: '#64748B', textTransform: 'uppercase', letterSpacing: '1.2px', marginRight: '6px' }}>
            INR Note Themes:
          </span>
          {[
            { name: '₹2000 Magenta', color: '#EC4899' },
            { name: '₹500 Stone', color: '#94A3B8' },
            { name: '₹200 Yellow', color: '#F59E0B' },
            { name: '₹100 Lavender', color: '#8B5CF6' },
            { name: '₹50 Cyan', color: '#06B6D4' },
            { name: '₹20 Green', color: '#10B981' },
            { name: '₹10 Chocolate', color: '#D97706' },
          ].map((theme) => (
            <div
              key={theme.name}
              style={{
                display: 'flex',
                alignItems: 'center',
                gap: '6px',
                backgroundColor: 'rgba(255, 255, 255, 0.04)',
                border: `1px solid ${theme.color}45`,
                padding: '5px 12px',
                borderRadius: '18px',
                fontSize: '11px',
                fontWeight: 700,
                color: theme.color,
                boxShadow: `0 0 12px ${theme.color}15`,
                transition: 'transform 0.2s ease',
              }}
            >
              <div style={{ width: '8px', height: '8px', borderRadius: '50%', backgroundColor: theme.color, boxShadow: `0 0 8px ${theme.color}` }} />
              <span>{theme.name}</span>
            </div>
          ))}
        </div>

        {/* Hero CTAs */}
        <div style={{ display: 'flex', justifyContent: 'center', gap: '18px', flexWrap: 'wrap' }}>
          <button
            onClick={onLaunchDashboard}
            className="btn-hover-effect"
            style={{
              background: 'linear-gradient(135deg, #EC4899 0%, #8B5CF6 100%)',
              color: '#FFF',
              border: 'none',
              padding: '16px 36px',
              borderRadius: '16px',
              fontSize: '16px',
              fontWeight: 800,
              cursor: 'pointer',
              boxShadow: '0 10px 35px rgba(236, 72, 153, 0.4)',
            }}
          >
            Launch Web App Now →
          </button>
          <a
            href="#interactive-demo"
            className="btn-hover-effect"
            style={{
              backgroundColor: 'rgba(255, 255, 255, 0.06)',
              color: '#F8FAFC',
              border: '1px solid rgba(255, 255, 255, 0.14)',
              padding: '16px 30px',
              borderRadius: '16px',
              fontSize: '16px',
              fontWeight: 700,
              textDecoration: 'none',
              display: 'inline-flex',
              alignItems: 'center',
              gap: '8px',
            }}
          >
            <span>⚡ Try Live Demo</span>
          </a>
        </div>
      </section>

      {/* Hero Showcase Mockup */}
      <section
        style={{
          position: 'relative',
          zIndex: 10,
          padding: '0 24px 90px 24px',
          maxWidth: '1020px',
          margin: '0 auto',
        }}
      >
        <div
          className="hover-card-glow floating-element"
          style={{
            backgroundColor: 'rgba(15, 23, 42, 0.75)',
            backdropFilter: 'blur(24px)',
            borderRadius: '28px',
            border: '1px solid rgba(255, 255, 255, 0.14)',
            boxShadow: '0 30px 70px -15px rgba(0, 0, 0, 0.8), 0 0 50px rgba(236, 72, 153, 0.15)',
            overflow: 'hidden',
            padding: '28px',
          }}
        >
          {/* Browser Bar */}
          <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '22px', paddingBottom: '16px', borderBottom: '1px solid rgba(255, 255, 255, 0.08)' }}>
            <div style={{ width: '12px', height: '12px', borderRadius: '50%', backgroundColor: '#EF4444' }} />
            <div style={{ width: '12px', height: '12px', borderRadius: '50%', backgroundColor: '#F59E0B' }} />
            <div style={{ width: '12px', height: '12px', borderRadius: '50%', backgroundColor: '#10B981' }} />
            <span style={{ marginLeft: '12px', fontSize: '12px', color: '#64748B', fontWeight: 600 }}>https://spe-dex.vercel.app/dashboard</span>
          </div>

          {/* Cards Grid */}
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '20px' }}>
            <div style={{ backgroundColor: 'rgba(30, 41, 59, 0.65)', borderRadius: '18px', padding: '20px', border: '1px solid rgba(255, 255, 255, 0.08)' }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '12px' }}>
                <span style={{ fontSize: '12px', color: '#94A3B8', fontWeight: 700 }}>MONTHLY SPEND</span>
                <span style={{ fontSize: '11px', color: '#10B981', fontWeight: 800 }}>● 68% of Budget</span>
              </div>
              <div style={{ fontSize: '30px', fontWeight: 800, color: '#FFF' }}>₹14,250</div>
              <div style={{ height: '6px', backgroundColor: 'rgba(255,255,255,0.1)', borderRadius: '3px', marginTop: '12px', overflow: 'hidden' }}>
                <div style={{ width: '68%', height: '100%', backgroundColor: '#EC4899', borderRadius: '3px', boxShadow: '0 0 10px #EC4899' }} />
              </div>
            </div>

            <div style={{ backgroundColor: 'rgba(99, 102, 241, 0.14)', borderRadius: '18px', padding: '20px', border: '1px solid rgba(99, 102, 241, 0.35)' }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '8px' }}>
                <span style={{ fontSize: '11px', fontWeight: 800, color: '#818CF8', textTransform: 'uppercase' }}>ACTIVE TRIP LEDGER</span>
                <span style={{ backgroundColor: '#10B981', color: '#FFF', fontSize: '9px', fontWeight: 800, padding: '2px 7px', borderRadius: '4px' }}>LIVE</span>
              </div>
              <div style={{ fontSize: '19px', fontWeight: 800, color: '#FFF', marginBottom: '4px' }}>✈️ Goa Beach Weekend</div>
              <div style={{ fontSize: '12px', color: '#94A3B8' }}>Total: ₹8,400 | Cash Spent: ₹3,200</div>
            </div>

            <div style={{ backgroundColor: 'rgba(30, 41, 59, 0.65)', borderRadius: '18px', padding: '20px', border: '1px solid rgba(255, 255, 255, 0.08)' }}>
              <div style={{ fontSize: '12px', color: '#94A3B8', fontWeight: 700, marginBottom: '12px' }}>QUICK PAY VENDORS</div>
              <div style={{ display: 'flex', gap: '10px' }}>
                {['Swiggy (₹350)', 'Uber (₹250)', 'Starbucks (₹450)'].map((vendor, idx) => (
                  <div key={idx} style={{ flex: 1, backgroundColor: 'rgba(15, 23, 42, 0.85)', padding: '9px', borderRadius: '12px', textAlign: 'center', border: '1px solid rgba(255, 255, 255, 0.06)' }}>
                    <div style={{ fontSize: '11px', fontWeight: 700, color: '#FFF' }}>{vendor}</div>
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
          position: 'relative',
          zIndex: 10,
          padding: '90px 24px',
          maxWidth: '1100px',
          margin: '0 auto',
        }}
      >
        <div style={{ textAlign: 'center', marginBottom: '64px' }}>
          <span style={{ fontSize: '12px', fontWeight: 800, color: '#EC4899', textTransform: 'uppercase', letterSpacing: '1.8px' }}>
            Core Capabilities
          </span>
          <h2 style={{ fontSize: '38px', fontWeight: 800, color: '#FFF', marginTop: '10px', letterSpacing: '-0.8px' }}>
            Built for Modern Financial Intelligence
          </h2>
          <p style={{ fontSize: '16px', color: '#94A3B8', maxWidth: '620px', margin: '14px auto 0 auto' }}>
            Everything you need to track spending, manage trip cash, and settle vendor payments instantly.
          </p>
        </div>

        <div
          style={{
            display: 'grid',
            gridTemplateColumns: 'repeat(auto-fit, minmax(320px, 1fr))',
            gap: '24px',
          }}
        >
          {[
            { icon: '⚡', title: 'Instant UPI Quick Pay Desk', color: '#EC4899', desc: '1-tap payment initiation with dynamic QR code generation for frequent vendors like Swiggy, Uber, DMart, and local cafes.' },
            { icon: '✈️', title: 'Dedicated Trips Ledger', color: '#8B5CF6', desc: 'Log cash and card expenses on vacation in real time. Track total trip spend, cash ratios, and category breakdowns effortlessly.' },
            { icon: '🎯', title: 'Smart Budget Guardrails', color: '#06B6D4', desc: 'Set category limits across Dining, Transport, Rent, and Subscriptions. Receive automated warnings when approaching caps.' },
            { icon: '📈', title: 'Real-Time Financial Analytics', color: '#10B981', desc: 'Visualize weekly spending curves, peak day metrics, monthly projections, and recurring bill reminder calendars.' },
            { icon: '📱', title: 'Mobile & Web Live Sync', color: '#F59E0B', desc: 'Instant zero-latency state synchronization between Expo React Native mobile application and Web Dashboard.' },
            { icon: '🛡️', title: 'Bank-Grade Security', color: '#D97706', desc: 'JWT authentication, IDOR resource protection, strict user-data isolation, and encrypted security tokens.' },
          ].map((item, idx) => (
            <div
              key={idx}
              className="hover-card-glow"
              onMouseEnter={() => setActiveFeatureIndex(idx)}
              onMouseLeave={() => setActiveFeatureIndex(null)}
              style={{
                backgroundColor: activeFeatureIndex === idx ? 'rgba(30, 41, 59, 0.75)' : 'rgba(15, 23, 42, 0.65)',
                padding: '30px',
                borderRadius: '22px',
                border: '1px solid rgba(255, 255, 255, 0.08)',
                cursor: 'default',
              }}
            >
              <div
                style={{
                  width: '46px',
                  height: '46px',
                  borderRadius: '14px',
                  backgroundColor: `${item.color}20`,
                  color: item.color,
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  fontSize: '22px',
                  marginBottom: '18px',
                  boxShadow: `0 0 15px ${item.color}30`,
                }}
              >
                {item.icon}
              </div>
              <h3 style={{ fontSize: '19px', fontWeight: 700, color: '#FFF', margin: '0 0 10px 0' }}>{item.title}</h3>
              <p style={{ fontSize: '14px', color: '#94A3B8', lineHeight: 1.65, margin: 0 }}>{item.desc}</p>
            </div>
          ))}
        </div>
      </section>

      {/* Interactive Quick Pay Demo Widget */}
      <section
        id="interactive-demo"
        style={{
          position: 'relative',
          zIndex: 10,
          padding: '60px 24px 90px 24px',
          maxWidth: '920px',
          margin: '0 auto',
          width: '100%',
        }}
      >
        <div
          style={{
            backgroundColor: 'rgba(15, 23, 42, 0.85)',
            backdropFilter: 'blur(20px)',
            padding: '40px',
            borderRadius: '28px',
            border: '1px solid rgba(255, 255, 255, 0.14)',
            boxShadow: '0 25px 60px rgba(0,0,0,0.6), 0 0 40px rgba(16, 185, 129, 0.1)',
            animation: paidStatus ? 'ripplePay 0.4s ease' : 'none',
          }}
        >
          <div style={{ textAlign: 'center', marginBottom: '32px' }}>
            <span style={{ fontSize: '11px', fontWeight: 800, color: '#10B981', textTransform: 'uppercase', letterSpacing: '1.5px' }}>
              ● LIVE SIMULATION
            </span>
            <h3 style={{ fontSize: '26px', fontWeight: 800, color: '#FFF', margin: '6px 0 0 0' }}>
              Simulate Instant UPI Quick Pay
            </h3>
          </div>

          <div style={{ display: 'flex', gap: '18px', flexWrap: 'wrap', justifyContent: 'center', alignItems: 'center', marginBottom: '24px' }}>
            <div>
              <label style={{ fontSize: '12px', color: '#94A3B8', display: 'block', marginBottom: '6px', fontWeight: 600 }}>Vendor Name</label>
              <input
                type="text"
                value={demoVendor}
                onChange={(e) => setDemoVendor(e.target.value)}
                style={{ backgroundColor: '#0F172A', border: '1px solid rgba(255,255,255,0.12)', color: '#FFF', padding: '12px 16px', borderRadius: '12px', fontSize: '14px', width: '190px' }}
              />
            </div>
            <div>
              <label style={{ fontSize: '12px', color: '#94A3B8', display: 'block', marginBottom: '6px', fontWeight: 600 }}>Amount (₹)</label>
              <input
                type="number"
                value={demoAmount}
                onChange={(e) => setDemoAmount(e.target.value)}
                style={{ backgroundColor: '#0F172A', border: '1px solid rgba(255,255,255,0.12)', color: '#FFF', padding: '12px 16px', borderRadius: '12px', fontSize: '14px', width: '150px' }}
              />
            </div>
            <div style={{ marginTop: '22px' }}>
              <button
                onClick={handleSimulatePay}
                className="btn-hover-effect"
                style={{
                  backgroundColor: paidStatus ? '#10B981' : '#EC4899',
                  color: '#FFF',
                  border: 'none',
                  padding: '13px 28px',
                  borderRadius: '12px',
                  fontSize: '14px',
                  fontWeight: 800,
                  cursor: 'pointer',
                  boxShadow: paidStatus ? '0 0 20px #10B981' : '0 6px 20px rgba(236, 72, 153, 0.4)',
                }}
              >
                {paidStatus ? '✓ Payment Processed!' : `Pay ₹${demoAmount} via UPI`}
              </button>
            </div>
          </div>

          {paidStatus && (
            <div style={{ backgroundColor: 'rgba(16, 185, 129, 0.18)', color: '#10B981', border: '1px solid rgba(16, 185, 129, 0.4)', padding: '14px', borderRadius: '14px', textAlign: 'center', fontSize: '14px', fontWeight: 800, boxShadow: '0 0 20px rgba(16, 185, 129, 0.2)' }}>
              🎉 Success! Payment of ₹{demoAmount} to {demoVendor} logged and synchronized with Dashboard.
            </div>
          )}
        </div>
      </section>

      {/* Minimalist Footer */}
      <footer
        style={{
          marginTop: 'auto',
          backgroundColor: '#070A11',
          borderTop: '1px solid rgba(255, 255, 255, 0.08)',
          padding: '45px 32px',
          textAlign: 'center',
        }}
      >
        <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', gap: '12px', marginBottom: '16px' }}>
          <img src="/spedex-mark.svg" alt="Spedex Logo" style={{ width: '26px', height: '26px' }} />
          <span style={{ fontSize: '17px', fontWeight: 800, color: '#FFF' }}>Spedex Smart Wallet Platform</span>
        </div>
        <p style={{ fontSize: '13px', color: '#64748B', margin: '0 0 18px 0' }}>
          © 2026 SpeDex Inc. All rights reserved. ● Live on Vercel & Render.
        </p>
        <div style={{ display: 'flex', justifyContent: 'center', gap: '22px' }}>
          <button onClick={onLaunchDashboard} style={{ background: 'none', border: 'none', color: '#38BDF8', cursor: 'pointer', fontSize: '13px', fontWeight: 700 }}>
            Launch Web Dashboard
          </button>
          <a href="/downloads/spedex-release.apk" download="spedex-v2.2.0.apk" style={{ color: '#10B981', textDecoration: 'none', fontSize: '13px', fontWeight: 700 }}>
            Download Mobile APK (Android)
          </a>
        </div>
      </footer>
    </div>
  );
};
