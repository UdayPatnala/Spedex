// Import the functions you need from the SDKs you need
import { initializeApp, getApps, getApp } from "firebase/app";
import { getAnalytics, isSupported, Analytics } from "firebase/analytics";
import { getFirestore, Firestore } from "firebase/firestore";

// SpeDex web app's Firebase configuration
const firebaseConfig = {
  apiKey: "AIzaSyDNeeUrxuQUJDtGdm_q03kjLf3e4Vn4vyc",
  authDomain: "spedex-db.firebaseapp.com",
  projectId: "spedex-db",
  storageBucket: "spedex-db.firebasestorage.app",
  messagingSenderId: "1042102576536",
  appId: "1:1042102576536:web:d924e50945f18a7ab137ed",
  measurementId: "G-HWYE0R22VB"
};

// Initialize Firebase app singleton
export const app = getApps().length === 0 ? initializeApp(firebaseConfig) : getApp();

// Initialize Cloud Firestore Database
export const db: Firestore = getFirestore(app);

// Conditionally initialize Analytics in supported browser environments
export let analytics: Analytics | null = null;

if (typeof window !== "undefined") {
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
  if (typeof window === "undefined") return;
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
