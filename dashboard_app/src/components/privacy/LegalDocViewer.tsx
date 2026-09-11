import React, { useState, useEffect } from "react";
import { getLegalDocument } from "../../api";
import type { LegalDocType, LegalDocument } from "../../types";

export interface LegalDocViewerProps {
  docType: LegalDocType;
  onClose: () => void;
}

export const LegalDocViewer: React.FC<LegalDocViewerProps> = ({ docType, onClose }) => {
  const [doc, setDoc] = useState<LegalDocument | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    let mounted = true;
    setLoading(true);
    setError(null);

    getLegalDocument(docType)
      .then((data) => {
        if (mounted) {
          setDoc(data);
          setLoading(false);
        }
      })
      .catch((err) => {
        if (mounted) {
          setError(err.message || "Failed to load document");
          setLoading(false);
        }
      });

    return () => {
      mounted = false;
    };
  }, [docType]);

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/70 backdrop-blur-sm">
      <div className="bg-[#0D1117] border border-[#30363D] w-full max-w-3xl max-h-[85vh] rounded-2xl flex flex-col shadow-2xl overflow-hidden animate-fadeIn">
        {/* Header */}
        <div className="flex items-center justify-between px-6 py-4 border-b border-[#30363D] bg-[#161B22]">
          <div className="flex items-center gap-3">
            <span className="text-xl">📜</span>
            <div>
              <h3 className="text-lg font-bold text-white font-serif tracking-wide">
                {doc ? doc.title : "Legal Documentation"}
              </h3>
              <p className="text-xs text-gray-400">SpeDex DPDP Act 2023 & Rules 2025 Compliance</p>
            </div>
          </div>
          <button
            type="button"
            onClick={onClose}
            className="p-1.5 text-gray-400 hover:text-white rounded-lg hover:bg-[#30363D] transition-colors"
            aria-label="Close dialog"
          >
            ✕
          </button>
        </div>

        {/* Content Body */}
        <div className="p-6 overflow-y-auto flex-1 text-gray-300 text-sm leading-relaxed space-y-4 font-sans">
          {loading && (
            <div className="flex items-center justify-center py-12 text-gray-400">
              <div className="w-6 h-6 border-2 border-[#58A6FF] border-t-transparent rounded-full animate-spin mr-3"></div>
              Loading policy document...
            </div>
          )}

          {error && (
            <div className="p-4 bg-red-900/30 border border-red-700/50 rounded-xl text-red-200 text-xs">
              <p className="font-semibold">Unable to fetch policy document</p>
              <p className="mt-1 text-gray-400">{error}</p>
            </div>
          )}

          {doc && !loading && (
            <div className="prose prose-invert max-w-none">
              <div className="p-3 bg-[#161B22] border border-[#30363D] rounded-xl text-xs text-gray-400 flex items-center justify-between mb-4">
                <span>Category: <strong>{doc.docType.toUpperCase()}</strong></span>
                <span>Last Updated: <strong>{new Date(doc.lastUpdated).toLocaleDateString()}</strong></span>
              </div>
              <div className="whitespace-pre-wrap font-sans text-sm text-gray-200">
                {doc.content}
              </div>
            </div>
          )}
        </div>

        {/* Footer */}
        <div className="px-6 py-3 border-t border-[#30363D] bg-[#161B22] flex justify-end">
          <button
            type="button"
            onClick={onClose}
            className="px-4 py-2 text-xs font-semibold text-white bg-[#238636] hover:bg-[#2ea043] rounded-lg transition-colors"
          >
            I Understand & Close
          </button>
        </div>
      </div>
    </div>
  );
};
