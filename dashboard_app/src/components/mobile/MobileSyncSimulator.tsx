import React, { useState, useEffect } from 'react';
import type { DashboardOverview, Trip, TripDetails } from '../../types';
import { getTrips, getTripDetails, startTrip, addManualTransaction, completeTrip } from '../../api';

export interface MobileSyncSimulatorProps {
  overviewData: DashboardOverview | null;
  activeTrip: Trip | null;
  onRefreshData: () => void;
}

export const MobileSyncSimulator: React.FC<MobileSyncSimulatorProps> = ({
  overviewData,
  activeTrip,
  onRefreshData,
}) => {
  const [activeTab, setActiveTab] = useState<'home' | 'trips' | 'payments' | 'budget' | 'reminders'>('home');
  const [tripDetails, setTripDetails] = useState<TripDetails | null>(null);

  // Quick Action Modal States
  const [showStartTrip, setShowStartTrip] = useState(false);
  const [newTripName, setNewTripName] = useState('');
  const [showAddExpense, setShowAddExpense] = useState(false);
  const [expAmount, setExpAmount] = useState('');
  const [expCategory, setExpCategory] = useState('');
  const [expDesc, setExpDesc] = useState('');
  const [actionLoading, setActionLoading] = useState(false);
  const [syncStatus, setSyncStatus] = useState<string>('Live Sync Active');

  useEffect(() => {
    if (activeTrip) {
      getTripDetails(activeTrip.id)
        .then(setTripDetails)
        .catch(console.error);
    } else {
      setTripDetails(null);
    }
  }, [activeTrip]);

  const triggerRealtimeSync = (msg: string) => {
    setSyncStatus(msg);
    onRefreshData();
    setTimeout(() => setSyncStatus('Live Sync Active'), 3000);
  };

  const handleStartTrip = async () => {
    if (!newTripName.trim()) return;
    setActionLoading(true);
    try {
      await startTrip(newTripName.trim());
      setNewTripName('');
      setShowStartTrip(false);
      triggerRealtimeSync('Trip Started! Synced with Web');
    } catch (e: any) {
      alert(e.message || 'Failed to start trip');
    } finally {
      setActionLoading(false);
    }
  };

  const handleAddExpense = async () => {
    if (!activeTrip) return;
    const amt = parseFloat(expAmount);
    if (isNaN(amt) || amt <= 0) return;
    setActionLoading(true);
    try {
      await addManualTransaction(activeTrip.id, {
        amount: amt,
        description: expDesc.trim(),
        category: expCategory.trim() || 'Miscellaneous',
      });
      setExpAmount('');
      setExpCategory('');
      setExpDesc('');
      setShowAddExpense(false);
      triggerRealtimeSync('Expense Logged! Synced with Web');
    } catch (e: any) {
      alert(e.message || 'Failed to log expense');
    } finally {
      setActionLoading(false);
    }
  };

  const handleCompleteActiveTrip = async () => {
    if (!activeTrip) return;
    setActionLoading(true);
    try {
      await completeTrip(activeTrip.id);
      triggerRealtimeSync('Trip Completed! Synced with Web');
    } catch (e: any) {
      alert(e.message || 'Failed to complete trip');
    } finally {
      setActionLoading(false);
    }
  };

  return (
    <div
      style={{
        width: '320px',
        height: '640px',
        backgroundColor: '#0F172A',
        borderRadius: '40px',
        border: '10px solid #1E293B',
        boxShadow: '0 25px 50px -12px rgba(0, 0, 0, 0.7), 0 0 0 1px rgba(255, 255, 255, 0.1)',
        display: 'flex',
        flexDirection: 'column',
        position: 'relative',
        overflow: 'hidden',
        fontFamily: "'Sora', sans-serif",
        color: '#F8FAFC',
      }}
    >
      {/* Device Notch & Status Bar */}
      <div
        style={{
          height: '24px',
          backgroundColor: '#0F172A',
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'center',
          padding: '0 16px',
          fontSize: '10px',
          fontWeight: 700,
          color: '#94A3B8',
          zIndex: 10,
        }}
      >
        <span>09:41</span>
        <div style={{ width: '80px', height: '14px', backgroundColor: '#1E293B', borderRadius: '8px' }} />
        <span>5G ⚡</span>
      </div>

      {/* Sync Status Banner */}
      <div
        style={{
          backgroundColor: syncStatus.includes('Active') ? 'rgba(16, 185, 129, 0.15)' : 'rgba(56, 189, 248, 0.25)',
          color: syncStatus.includes('Active') ? '#10B981' : '#38BDF8',
          fontSize: '10px',
          fontWeight: 700,
          padding: '4px 10px',
          textAlign: 'center',
          borderBottom: '1px solid rgba(255, 255, 255, 0.05)',
        }}
      >
        ● {syncStatus}
      </div>

      {/* Scrollable Screen Body */}
      <div style={{ flex: 1, overflowY: 'auto', padding: '14px', paddingBottom: '60px' }}>
        {activeTab === 'home' && (
          <div>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '14px' }}>
              <div>
                <span style={{ fontSize: '10px', color: '#94A3B8', textTransform: 'uppercase', letterSpacing: '0.8px', fontWeight: 700 }}>Mobile Sync</span>
                <h3 style={{ margin: 0, fontSize: '16px', fontWeight: 800, color: '#F8FAFC' }}>Spedex Wallet</h3>
              </div>
              <div style={{ width: '28px', height: '28px', borderRadius: '50%', backgroundColor: '#38BDF8', display: 'flex', alignItems: 'center', justifyContent: 'center', fontWeight: 800, color: '#FFF', fontSize: '11px' }}>
                {overviewData?.user?.avatar_initials || 'U'}
              </div>
            </div>

            {/* Daily Outlook Card */}
            <div style={{ backgroundColor: 'rgba(30, 41, 59, 0.8)', padding: '14px', borderRadius: '16px', border: '1px solid rgba(255, 255, 255, 0.08)', marginBottom: '14px' }}>
              <span style={{ fontSize: '10px', color: '#94A3B8' }}>Today's Spending</span>
              <h2 style={{ margin: '2px 0', fontSize: '20px', fontWeight: 800, color: '#38BDF8' }}>
                ₹{overviewData?.monthly_total ? Math.round(overviewData.monthly_total).toLocaleString() : 0}
              </h2>
              <div style={{ height: '5px', backgroundColor: 'rgba(255, 255, 255, 0.1)', borderRadius: '3px', overflow: 'hidden', marginTop: '8px' }}>
                <div style={{ height: '100%', width: `${Math.min((overviewData?.budget_used_ratio || 0) * 100, 100)}%`, backgroundColor: '#38BDF8', borderRadius: '3px' }} />
              </div>
            </div>

            {/* Active Trip Widget */}
            <div style={{ backgroundColor: 'rgba(99, 102, 241, 0.15)', border: '1px solid rgba(99, 102, 241, 0.3)', padding: '12px', borderRadius: '14px', marginBottom: '14px' }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <span style={{ fontSize: '11px', fontWeight: 800, color: '#818CF8' }}>✈️ {activeTrip ? `Active: ${activeTrip.name}` : 'No Active Trip'}</span>
                <button onClick={() => setActiveTab('trips')} style={{ backgroundColor: '#6366F1', color: '#FFF', border: 'none', padding: '4px 8px', borderRadius: '6px', fontSize: '10px', fontWeight: 700, cursor: 'pointer' }}>
                  Open
                </button>
              </div>
            </div>

            {/* Quick Pay */}
            <div style={{ marginBottom: '14px' }}>
              <span style={{ fontSize: '12px', fontWeight: 700, color: '#F8FAFC' }}>Quick Pay Vendors</span>
              <div style={{ display: 'flex', gap: '8px', marginTop: '8px', overflowX: 'auto' }}>
                {overviewData?.quick_pay?.slice(0, 3).map((v) => (
                  <div key={v.id} style={{ minWidth: '70px', backgroundColor: 'rgba(30, 41, 59, 0.6)', padding: '8px', borderRadius: '12px', textAlign: 'center', border: '1px solid rgba(255, 255, 255, 0.05)' }}>
                    <div style={{ fontSize: '14px' }}>⚡</div>
                    <div style={{ fontSize: '10px', fontWeight: 700, color: '#F8FAFC', marginTop: '2px', overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>{v.name}</div>
                    <div style={{ fontSize: '9px', color: '#94A3B8' }}>₹{v.default_amount}</div>
                  </div>
                ))}
              </div>
            </div>

            {/* Recent Transactions */}
            <div>
              <span style={{ fontSize: '12px', fontWeight: 700, color: '#F8FAFC' }}>Recent Transactions</span>
              <div style={{ marginTop: '6px', display: 'flex', flexDirection: 'column', gap: '6px' }}>
                {overviewData?.recent_transactions?.slice(0, 3).map((tx) => (
                  <div key={tx.id} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', backgroundColor: 'rgba(30, 41, 59, 0.4)', padding: '8px 10px', borderRadius: '10px' }}>
                    <div>
                      <div style={{ fontSize: '11px', fontWeight: 700, color: '#F8FAFC' }}>{tx.description}</div>
                      <div style={{ fontSize: '9px', color: '#94A3B8' }}>{tx.category}</div>
                    </div>
                    <span style={{ fontSize: '11px', fontWeight: 800, color: '#EF4444' }}>-₹{tx.amount}</span>
                  </div>
                ))}
              </div>
            </div>
          </div>
        )}

        {activeTab === 'trips' && (
          <div>
            <h3 style={{ margin: 0, fontSize: '16px', fontWeight: 800, color: '#F8FAFC', marginBottom: '12px' }}>Trips Ledger</h3>
            {activeTrip ? (
              <div style={{ backgroundColor: 'rgba(30, 41, 59, 0.9)', padding: '14px', borderRadius: '16px', border: '1px solid rgba(255, 255, 255, 0.1)', marginBottom: '14px' }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '6px' }}>
                  <span style={{ backgroundColor: 'rgba(16, 185, 129, 0.2)', color: '#10B981', padding: '2px 6px', borderRadius: '4px', fontSize: '9px', fontWeight: 800 }}>ACTIVE TRIP</span>
                  <button onClick={handleCompleteActiveTrip} style={{ backgroundColor: 'rgba(239, 68, 68, 0.2)', color: '#F87171', border: 'none', padding: '3px 6px', borderRadius: '4px', fontSize: '9px', fontWeight: 700, cursor: 'pointer' }}>
                    Complete
                  </button>
                </div>
                <h4 style={{ margin: 0, fontSize: '15px', fontWeight: 800, color: '#FFF' }}>{activeTrip.name}</h4>

                {tripDetails && (
                  <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '6px', margin: '10px 0' }}>
                    <div style={{ backgroundColor: 'rgba(15, 23, 42, 0.6)', padding: '6px', borderRadius: '8px' }}>
                      <div style={{ fontSize: '9px', color: '#94A3B8' }}>Total Spend</div>
                      <div style={{ fontSize: '12px', fontWeight: 800, color: '#FFF' }}>₹{tripDetails.total_spend}</div>
                    </div>
                    <div style={{ backgroundColor: 'rgba(15, 23, 42, 0.6)', padding: '6px', borderRadius: '8px' }}>
                      <div style={{ fontSize: '9px', color: '#94A3B8' }}>Cash Spend</div>
                      <div style={{ fontSize: '12px', fontWeight: 800, color: '#10B981' }}>₹{tripDetails.cash_spend}</div>
                    </div>
                  </div>
                )}

                <button onClick={() => setShowAddExpense(true)} style={{ width: '100%', backgroundColor: '#6366F1', color: '#FFF', border: 'none', padding: '7px', borderRadius: '8px', fontSize: '11px', fontWeight: 700, cursor: 'pointer' }}>
                  + Log Cash Expense
                </button>
              </div>
            ) : (
              <div style={{ backgroundColor: 'rgba(30, 41, 59, 0.4)', padding: '16px', borderRadius: '14px', textAlign: 'center', marginBottom: '14px' }}>
                <div style={{ fontSize: '20px' }}>✈️</div>
                <div style={{ fontSize: '13px', fontWeight: 700, marginTop: '6px' }}>No Active Trip</div>
                <button onClick={() => setShowStartTrip(true)} style={{ marginTop: '10px', backgroundColor: '#38BDF8', color: '#FFF', border: 'none', padding: '6px 12px', borderRadius: '8px', fontSize: '11px', fontWeight: 700, cursor: 'pointer' }}>
                  Start New Trip
                </button>
              </div>
            )}
          </div>
        )}

        {activeTab === 'payments' && (
          <div>
            <h3 style={{ margin: 0, fontSize: '16px', fontWeight: 800, color: '#F8FAFC', marginBottom: '12px' }}>Quick Pay</h3>
            <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
              {overviewData?.quick_pay?.map((v) => (
                <div key={v.id} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', backgroundColor: 'rgba(30, 41, 59, 0.6)', padding: '10px', borderRadius: '12px', border: '1px solid rgba(255, 255, 255, 0.05)' }}>
                  <div>
                    <div style={{ fontSize: '12px', fontWeight: 700, color: '#FFF' }}>{v.name}</div>
                    <div style={{ fontSize: '10px', color: '#94A3B8' }}>{v.upi_handle}</div>
                  </div>
                  <button onClick={() => triggerRealtimeSync(`Paid ₹${v.default_amount} to ${v.name}`)} style={{ backgroundColor: '#10B981', color: '#FFF', border: 'none', padding: '5px 10px', borderRadius: '6px', fontSize: '10px', fontWeight: 700, cursor: 'pointer' }}>
                    Pay ₹{v.default_amount}
                  </button>
                </div>
              ))}
            </div>
          </div>
        )}
      </div>

      {/* Start Trip Modal */}
      {showStartTrip && (
        <div style={{ position: 'absolute', inset: 0, backgroundColor: 'rgba(0,0,0,0.85)', display: 'flex', alignItems: 'center', justifyContent: 'center', padding: '14px', zIndex: 100 }}>
          <div style={{ backgroundColor: '#1E293B', padding: '14px', borderRadius: '14px', width: '100%' }}>
            <h4 style={{ margin: 0, marginBottom: '10px', fontSize: '13px', fontWeight: 800 }}>Start New Trip</h4>
            <input
              type="text"
              placeholder="Trip name..."
              value={newTripName}
              onChange={(e) => setNewTripName(e.target.value)}
              style={{ width: '100%', padding: '7px', borderRadius: '6px', backgroundColor: '#0F172A', border: '1px solid rgba(255,255,255,0.1)', color: '#FFF', fontSize: '11px', marginBottom: '10px', boxSizing: 'border-box' }}
            />
            <div style={{ display: 'flex', gap: '6px', justifyContent: 'flex-end' }}>
              <button onClick={() => setShowStartTrip(false)} style={{ backgroundColor: 'transparent', color: '#94A3B8', border: 'none', fontSize: '11px', cursor: 'pointer' }}>Cancel</button>
              <button onClick={handleStartTrip} disabled={actionLoading} style={{ backgroundColor: '#38BDF8', color: '#FFF', border: 'none', padding: '5px 10px', borderRadius: '6px', fontSize: '11px', fontWeight: 700, cursor: 'pointer' }}>Start</button>
            </div>
          </div>
        </div>
      )}

      {/* Add Expense Modal */}
      {showAddExpense && (
        <div style={{ position: 'absolute', inset: 0, backgroundColor: 'rgba(0,0,0,0.85)', display: 'flex', alignItems: 'center', justifyContent: 'center', padding: '14px', zIndex: 100 }}>
          <div style={{ backgroundColor: '#1E293B', padding: '14px', borderRadius: '14px', width: '100%' }}>
            <h4 style={{ margin: 0, marginBottom: '10px', fontSize: '13px', fontWeight: 800 }}>Log Cash Expense</h4>
            <input
              type="number"
              placeholder="Amount (₹)..."
              value={expAmount}
              onChange={(e) => setExpAmount(e.target.value)}
              style={{ width: '100%', padding: '7px', borderRadius: '6px', backgroundColor: '#0F172A', border: '1px solid rgba(255,255,255,0.1)', color: '#FFF', fontSize: '11px', marginBottom: '6px', boxSizing: 'border-box' }}
            />
            <input
              type="text"
              placeholder="Category (Dining, Transport)..."
              value={expCategory}
              onChange={(e) => setExpCategory(e.target.value)}
              style={{ width: '100%', padding: '7px', borderRadius: '6px', backgroundColor: '#0F172A', border: '1px solid rgba(255,255,255,0.1)', color: '#FFF', fontSize: '11px', marginBottom: '6px', boxSizing: 'border-box' }}
            />
            <input
              type="text"
              placeholder="Description..."
              value={expDesc}
              onChange={(e) => setExpDesc(e.target.value)}
              style={{ width: '100%', padding: '7px', borderRadius: '6px', backgroundColor: '#0F172A', border: '1px solid rgba(255,255,255,0.1)', color: '#FFF', fontSize: '11px', marginBottom: '10px', boxSizing: 'border-box' }}
            />
            <div style={{ display: 'flex', gap: '6px', justifyContent: 'flex-end' }}>
              <button onClick={() => setShowAddExpense(false)} style={{ backgroundColor: 'transparent', color: '#94A3B8', border: 'none', fontSize: '11px', cursor: 'pointer' }}>Cancel</button>
              <button onClick={handleAddExpense} disabled={actionLoading} style={{ backgroundColor: '#6366F1', color: '#FFF', border: 'none', padding: '5px 10px', borderRadius: '6px', fontSize: '11px', fontWeight: 700, cursor: 'pointer' }}>Log Expense</button>
            </div>
          </div>
        </div>
      )}

      {/* Bottom Navigation Dock */}
      <div
        style={{
          position: 'absolute',
          bottom: 0,
          left: 0,
          right: 0,
          height: '48px',
          backgroundColor: '#1E293B',
          display: 'flex',
          justifyContent: 'space-around',
          alignItems: 'center',
          borderTop: '1px solid rgba(255, 255, 255, 0.08)',
          zIndex: 10,
        }}
      >
        <button onClick={() => setActiveTab('home')} style={{ background: 'none', border: 'none', color: activeTab === 'home' ? '#38BDF8' : '#64748B', fontSize: '10px', fontWeight: 700, cursor: 'pointer', display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
          <span>🏠</span>
          <span>Home</span>
        </button>
        <button onClick={() => setActiveTab('trips')} style={{ background: 'none', border: 'none', color: activeTab === 'trips' ? '#38BDF8' : '#64748B', fontSize: '10px', fontWeight: 700, cursor: 'pointer', display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
          <span>✈️</span>
          <span>Trips</span>
        </button>
        <button onClick={() => setActiveTab('payments')} style={{ background: 'none', border: 'none', color: activeTab === 'payments' ? '#38BDF8' : '#64748B', fontSize: '10px', fontWeight: 700, cursor: 'pointer', display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
          <span>⚡</span>
          <span>Pay</span>
        </button>
      </div>
    </div>
  );
};
