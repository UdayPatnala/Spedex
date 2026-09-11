import React, { useState } from "react";
import type { LegalDocType } from "../../types";

export interface ConsentOnboardingModalProps {
  isOpen: boolean;
  onComplete: (data: {
    isMinor: boolean;
    age: number;
    guardianName?: string;
    guardianEmail?: string;
    analyticsConsent: boolean;
    marketingConsent: boolean;
  }) => void;
  onOpenDoc: (doc: LegalDocType) => void;
}

export const ConsentOnboardingModal: React.FC<ConsentOnboardingModalProps> = ({
  isOpen,
  onComplete,
  onOpenDoc,
}) => {
  const [age, setAge] = useState<number>(18);
  const [isMinor, setIsMinor] = useState<boolean>(false);
  const [guardianName, setGuardianName] = useState("");
  const [guardianEmail, setGuardianEmail] = useState("");
  const [termsAccepted, setTermsAccepted] = useState(false);
  const [analyticsConsent, setAnalyticsConsent] = useState(false);
  const [marketingConsent, setMarketingConsent] = useState(false);
  const [error, setError] = useState<string | null>(null);

  if (!isOpen) return null;

  const handleAgeChange = (newAge: number) => {
    setAge(newAge);
    const minor = newAge < 18;
    setIsMinor(minor);
    if (minor) {
      setAnalyticsConsent(false);
      setMarketingConsent(false);
    }
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);

    if (!termsAccepted) {
      setError("You must review and accept the Terms of Service & Privacy Policy to proceed.");
      return;
    }

    if (isMinor) {
      if (!guardianName.trim() || !guardianEmail.trim()) {
        setError("Under DPDP Act Section 9, minors must provide verifiable guardian name and email address.");
        return;
      }
    }

    onComplete({
      isMinor,
      age,
      guardianName: isMinor ? guardianName.trim() : undefined,
      guardianEmail: isMinor ? guardianEmail.trim() : undefined,
      analyticsConsent: isMinor ? false : analyticsConsent,
      marketingConsent: isMinor ? false : marketingConsent,
    });
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/80 backdrop-blur-md">
      <div className="bg-[#0D1117] border border-[#30363D] w-full max-w-lg rounded-2xl p-6 shadow-2xl animate-fadeIn text-gray-200">
        <div className="flex items-center gap-3 border-b border-[#30363D] pb-4 mb-4">
          <span className="text-2xl">🇮🇳</span>
          <div>
            <h3 className="text-lg font-bold text-white font-serif tracking-wide">
              DPDP Act 2023 Consent Notice
            </h3>
            <p className="text-xs text-gray-400">Notice of Data Processing & Minor Protections</p>
          </div>
        </div>

        <form onSubmit={handleSubmit} className="space-y-4">
          {error && (
            <div className="p-3 bg-red-900/40 border border-red-700/60 rounded-xl text-red-200 text-xs">
              {error}
            </div>
          )}

          {/* Age Selection */}
          <div>
            <label className="block text-xs font-semibold uppercase tracking-wider text-gray-400 mb-1">
              Your Age
            </label>
            <input
              type="number"
              min={13}
              max={100}
              value={age}
              onChange={(e) => handleAgeChange(parseInt(e.target.value) || 18)}
              className="w-full bg-[#161B22] border border-[#30363D] rounded-lg px-3 py-2 text-sm text-white focus:outline-none focus:border-[#58A6FF]"
              required
            />
          </div>

          {/* Minor Guardian Verification Block */}
          {isMinor && (
            <div className="p-4 bg-[#1F242C] border border-[#F0883E]/50 rounded-xl space-y-3">
              <div className="flex items-center gap-2 text-[#F0883E] text-xs font-bold">
                <span>⚠️</span>
                <span>Parental / Guardian Consent Required (Age &lt; 18)</span>
              </div>
              <p className="text-xs text-gray-300 leading-relaxed">
                In compliance with DPDP Act 2023 Section 9, a verification request will be sent to your guardian. Behavioral tracking and targeted ads are permanently disabled.
              </p>
              <div>
                <label className="block text-xs text-gray-400 mb-1">Guardian Full Name</label>
                <input
                  type="text"
                  value={guardianName}
                  onChange={(e) => setGuardianName(e.target.value)}
                  placeholder="e.g. Rajesh Sharma"
                  className="w-full bg-[#0D1117] border border-[#30363D] rounded-lg px-3 py-1.5 text-xs text-white focus:outline-none focus:border-[#58A6FF]"
                  required
                />
              </div>
              <div>
                <label className="block text-xs text-gray-400 mb-1">Guardian Email Address</label>
                <input
                  type="email"
                  value={guardianEmail}
                  onChange={(e) => setGuardianEmail(e.target.value)}
                  placeholder="guardian@example.com"
                  className="w-full bg-[#0D1117] border border-[#30363D] rounded-lg px-3 py-1.5 text-xs text-white focus:outline-none focus:border-[#58A6FF]"
                  required
                />
              </div>
            </div>
          )}

          {/* Granular Consents */}
          <div className="space-y-2 pt-2 border-t border-[#30363D]/60 text-xs">
            <label className="flex items-start gap-2.5 cursor-pointer">
              <input
                type="checkbox"
                checked={termsAccepted}
                onChange={(e) => setTermsAccepted(e.target.checked)}
                className="mt-0.5 rounded border-gray-600 bg-gray-800 text-[#238636] focus:ring-0"
                required
              />
              <span className="text-gray-300">
                I have read and unconditionally agree to the{" "}
                <button
                  type="button"
                  onClick={() => onOpenDoc("terms")}
                  className="text-[#58A6FF] hover:underline"
                >
                  Terms of Service
                </button>
                ,{" "}
                <button
                  type="button"
                  onClick={() => onOpenDoc("privacy")}
                  className="text-[#58A6FF] hover:underline"
                >
                  Privacy Policy
                </button>
                , and{" "}
                <button
                  type="button"
                  onClick={() => onOpenDoc("consent")}
                  className="text-[#58A6FF] hover:underline"
                >
                  Consent Notice
                </button>
                . <span className="text-red-400">*</span>
              </span>
            </label>

            {!isMinor && (
              <>
                <label className="flex items-start gap-2.5 cursor-pointer">
                  <input
                    type="checkbox"
                    checked={analyticsConsent}
                    onChange={(e) => setAnalyticsConsent(e.target.checked)}
                    className="mt-0.5 rounded border-gray-600 bg-gray-800 text-[#238636] focus:ring-0"
                  />
                  <span className="text-gray-300">
                    Opt-in to telemetry & anonymous analytics to help improve app reliability (Optional).
                  </span>
                </label>
                <label className="flex items-start gap-2.5 cursor-pointer">
                  <input
                    type="checkbox"
                    checked={marketingConsent}
                    onChange={(e) => setMarketingConsent(e.target.checked)}
                    className="mt-0.5 rounded border-gray-600 bg-gray-800 text-[#238636] focus:ring-0"
                  />
                  <span className="text-gray-300">
                    Opt-in to product updates and educational financial digests (Optional).
                  </span>
                </label>
              </>
            )}
          </div>

          <div className="flex justify-end pt-3">
            <button
              type="submit"
              className="w-full py-2.5 px-4 bg-[#238636] hover:bg-[#2ea043] font-semibold text-white text-xs rounded-xl shadow-lg transition-colors"
            >
              Continue to SpeDex
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};
