export function BrandLockup({ compact = false }: { compact?: boolean }) {
  return (
    <div className={`brand-lockup ${compact ? "compact" : ""}`}>
      <img className="brand-mark-image" src="/spedex-mark.svg" alt="Spedex logo" />
      <div className="brand-copy">
        <h1>Spedex</h1>
        <p>Smart Wallet</p>
      </div>
    </div>
  );
}
