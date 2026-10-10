// Import the functions you need from the SDKs you need
import { initializeApp, getApps, getApp } from "firebase/app";
import { getAnalytics, isSupported, Analytics } from "firebase/analytics";
import { getFirestore, Firestore } from "firebase/firestore";

// Read Firebase client configuration dynamically from environment variables
const apiKey = import.meta.env.VITE_FIREBASE_API_KEY;
const authDomain = import.meta.env.VITE_FIREBASE_AUTH_DOMAIN || "spedex-db.firebaseapp.com";
const projectId = import.meta.env.VITE_FIREBASE_PROJECT_ID || "spedex-db";
const storageBucket = import.meta.env.VITE_FIREBASE_STORAGE_BUCKET || "spedex-db.firebasestorage.app";
const messagingSenderId = import.meta.env.VITE_FIREBASE_MESSAGING_SENDER_ID;
const appId = import.meta.env.VITE_FIREBASE_APP_ID;
const measurementId = import.meta.env.VITE_FIREBASE_MEASUREMENT_ID;

export const isFirebaseConfigured = Boolean(apiKey && projectId && appId);

const firebaseConfig = {
  apiKey: apiKey || "mock-api-key-for-development",
  authDomain,
  projectId,
  storageBucket,
  messagingSenderId: messagingSenderId || "000000000000",
  appId: appId || "1:000000000000:web:mockapp",
  measurementId,
};

// Initialize Firebase app singleton
export const app = getApps().length === 0 ? initializeApp(firebaseConfig) : getApp();

// Initialize Cloud Firestore Database
export const db: Firestore = getFirestore(app);

// Conditionally initialize Analytics in supported browser environments
export let analytics: Analytics | null = null;

if (typeof window !== "undefined" && isFirebaseConfigured) {
  isSupported()
    .then((supported) => {
      if (supported) {
        analytics = getAnalytics(app);
      }
    })
    .catch(() => {
      // Gracefully handle test / unsupported environments
    });
}

/**
 * Safe logging helper for SpeDex telemetry & analytics
 */
export async function logEventSafely(eventName: string, params?: Record<string, any>): Promise<void> {
  if (typeof window === "undefined" || !isFirebaseConfigured) return;
  try {
    if (!analytics) {
      const supported = await isSupported();
      if (supported) {
        analytics = getAnalytics(app);
      }
    }
    if (analytics) {
      const { logEvent } = await import("firebase/analytics");
      logEvent(analytics, eventName, params);
    }
  } catch {
    // Fail silently to prevent disrupting core financial flows
  }
}

export default app;
