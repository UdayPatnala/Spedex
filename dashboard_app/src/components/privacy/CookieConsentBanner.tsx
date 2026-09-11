import React, { useState, useEffect } from "react";

export interface CookieConsentBannerProps {
  onOpenPreferences?: () => void;
  onOpenDoc?: (doc: string) => void;
}

export const CookieConsentBanner: React.FC<CookieConsentBannerProps> = ({ onOpenPreferences, onOpenDoc }) => {
  const [visible, setVisible] = useState(false);

  useEffect(() => {
    const consent = localStorage.getItem("spedex_cookie_consent");
    if (!consent) {
      setVisible(true);
    }
  }, []);

  const handleAcceptAll = () => {
    localStorage.setItem(
      "spedex_cookie_consent",
      JSON.stringify({ necessary: true, analytics: true, marketing: false, timestamp: new Date().toISOString() })
    );
    setVisible(false);
  };

  const handleRejectNonEssential = () => {
    localStorage.setItem(
      "spedex_cookie_consent",
      JSON.stringify({ necessary: true, analytics: false, marketing: false, timestamp: new Date().toISOString() })
    );
    setVisible(false);
  };

  if (!visible) return null;

  return (
    <div
      role="region"
      aria-label="Cookie consent banner"
      className="fixed bottom-4 left-4 right-4 md:left-auto md:right-4 md:max-w-md z-50 bg-[#161B22] border border-[#30363D] p-5 rounded-2xl shadow-2xl backdrop-blur-md"
    >
      <div className="flex items-start gap-3">
        <span className="text-2xl" aria-hidden="true">🛡️</span>
        <div>
          <h4 className="text-sm font-semibold text-white tracking-wide">DPDP Act Privacy & Cookies</h4>
          <p className="text-xs text-gray-300 mt-1 leading-relaxed">
            We use strictly necessary cookies to operate SpeDex. Analytics cookies help us improve our non-custodial
            utility. You retain the right to manage or withdraw consent anytime under the DPDP Act, 2023.
          </p>
          <div className="flex flex-wrap gap-2 mt-2">
            {onOpenDoc && (
              <button
                type="button"
                onClick={() => onOpenDoc("cookies")}
                className="text-xs text-[#58A6FF] hover:underline font-medium"
              >
                Cookie Policy
              </button>
            )}
            {onOpenDoc && (
              <button
                type="button"
                onClick={() => onOpenDoc("privacy")}
                className="text-xs text-[#58A6FF] hover:underline font-medium"
              >
                Privacy Notice
              </button>
            )}
          </div>
        </div>
      </div>

      <div className="flex items-center justify-end gap-2 mt-4 pt-3 border-t border-[#30363D]/60">
        <button
          type="button"
          onClick={handleRejectNonEssential}
          className="px-3 py-1.5 text-xs text-gray-300 hover:text-white bg-[#21262D] hover:bg-[#30363D] rounded-lg transition-colors"
        >
          Essential Only
        </button>
        {onOpenPreferences && (
          <button
            type="button"
            onClick={() => {
              setVisible(false);
              onOpenPreferences();
            }}
            className="px-3 py-1.5 text-xs text-gray-200 hover:text-white bg-[#238636]/30 hover:bg-[#238636]/50 border border-[#238636]/60 rounded-lg transition-colors"
          >
            Preferences
          </button>
        )}
        <button
          type="button"
          onClick={handleAcceptAll}
          className="px-4 py-1.5 text-xs font-semibold text-white bg-[#238636] hover:bg-[#2ea043] rounded-lg shadow transition-colors"
        >
          Accept All
        </button>
      </div>
    </div>
  );
};
