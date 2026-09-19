import type { ViewId } from "../../types";

export function SkeletonLoaderView({ view }: { view: ViewId }) {
  if (view === "trips") {
    return (
      <div className="trips-shell">
        <aside className="trips-sidebar card">
          <div className="skeleton-title" style={{ width: "80%" }}><div className="skeleton-box" /></div>
          <div className="skeleton-text" style={{ width: "60%", marginBottom: 20 }}><div className="skeleton-box" /></div>
          <div style={{ display: "flex", flexDirection: "column", gap: 12 }}>
            {[1, 2, 3].map((n) => (
              <div key={n} className="skeleton-card" style={{ padding: 16 }}>
                <div className="skeleton-title" style={{ width: "70%", height: 18 }}><div className="skeleton-box" /></div>
                <div className="skeleton-text" style={{ width: "40%" }}><div className="skeleton-box" /></div>
              </div>
            ))}
          </div>
        </aside>
        <section className="trips-main">
          <div className="skeleton-card" style={{ padding: 24, marginBottom: 20 }}>
            <div className="skeleton-title"><div className="skeleton-box" /></div>
            <div className="skeleton-text" style={{ width: "30%" }}><div className="skeleton-box" /></div>
          </div>
          <div className="trip-stat-grid" style={{ display: "grid", gridTemplateColumns: "repeat(3, 1fr)", gap: 16, marginBottom: 20 }}>
            {[1, 2, 3].map((n) => (
              <div key={n} className="skeleton-card">
                <div className="skeleton-text" style={{ width: "40%" }}><div className="skeleton-box" /></div>
                <div className="skeleton-title" style={{ width: "80%", height: 28 }}><div className="skeleton-box" /></div>
              </div>
            ))}
          </div>
          <div className="trip-content-grid" style={{ display: "grid", gridTemplateColumns: "2fr 1fr", gap: 20 }}>
            <div className="skeleton-card" style={{ height: 300 }}>
              <div className="skeleton-title"><div className="skeleton-box" /></div>
              <div className="skeleton-text"><div className="skeleton-box" /></div>
              <div className="skeleton-text" style={{ width: "80%" }}><div className="skeleton-box" /></div>
              <div className="skeleton-text" style={{ width: "90%" }}><div className="skeleton-box" /></div>
            </div>
            <div className="skeleton-card" style={{ height: 300 }}>
              <div className="skeleton-title"><div className="skeleton-box" /></div>
              <div className="skeleton-text"><div className="skeleton-box" /></div>
              <div className="skeleton-text" style={{ width: "70%" }}><div className="skeleton-box" /></div>
            </div>
          </div>
        </section>
      </div>
    );
  }

  // General loader fallback for other views
  return (
    <div style={{ padding: 24 }}>
      <div className="skeleton-card" style={{ marginBottom: 24, padding: 32 }}>
        <div className="skeleton-title" style={{ width: "30%", height: 32 }}><div className="skeleton-box" /></div>
        <div className="skeleton-text" style={{ width: "50%" }}><div className="skeleton-box" /></div>
      </div>
      <div style={{ display: "grid", gridTemplateColumns: "repeat(3, 1fr)", gap: 20, marginBottom: 24 }}>
        {[1, 2, 3].map((n) => (
          <div key={n} className="skeleton-card" style={{ height: 120 }}>
            <div className="skeleton-text" style={{ width: "40%" }}><div className="skeleton-box" /></div>
            <div className="skeleton-title" style={{ width: "70%", height: 28 }}><div className="skeleton-box" /></div>
          </div>
        ))}
      </div>
      <div style={{ display: "grid", gridTemplateColumns: "1.8fr 1.2fr", gap: 24 }}>
        <div className="skeleton-card" style={{ height: 320 }}>
          <div className="skeleton-title" style={{ width: "40%" }}><div className="skeleton-box" /></div>
          <div className="skeleton-text"><div className="skeleton-box" /></div>
          <div className="skeleton-text" style={{ width: "90%" }}><div className="skeleton-box" /></div>
          <div className="skeleton-text" style={{ width: "85%" }}><div className="skeleton-box" /></div>
        </div>
        <div className="skeleton-card" style={{ height: 320 }}>
          <div className="skeleton-title" style={{ width: "50%" }}><div className="skeleton-box" /></div>
          <div className="skeleton-text"><div className="skeleton-box" /></div>
          <div className="skeleton-text" style={{ width: "70%" }}><div className="skeleton-box" /></div>
        </div>
      </div>
    </div>
  );
}
