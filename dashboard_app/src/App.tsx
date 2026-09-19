import { startTransition, useDeferredValue, useEffect, useMemo, useState } from "react";

import {
  addVendor,
  getCurrentUser,
  getTrips,
  loadDashboardBundle,
  login,
  setAuthToken,
  signUp,
  warmUpBackend,
} from "./api";
import { SkeletonLoaderView } from "./components/common/SkeletonLoaderView";
import { LandingPage } from "./components/landing/LandingPage";
import { BrandLockup } from "./components/layout/BrandLockup";
import { Sidebar } from "./components/layout/Sidebar";
import { Topbar } from "./components/layout/Topbar";
import { MobileSyncSimulator } from "./components/mobile/MobileSyncSimulator";
import { CookieConsentBanner } from "./components/privacy/CookieConsentBanner";
import { LegalDocViewer } from "./components/privacy/LegalDocViewer";
import { PrivacyCenter } from "./components/privacy/PrivacyCenter";
import { AddVendorModal } from "./components/vendors/AddVendorModal";
import { APP_VERSION } from "./version";
import type {
  AnalyticsData,
  AuthMode,
  BudgetScreenData,
  DashboardOverview,
  LegalDocType,
  Trip,
  VendorDirectoryData,
  ViewId,
} from "./types";
import { filterTransactions } from "./utils/formatters";
import { AnalyticsView } from "./views/AnalyticsView";
import { AuthView } from "./views/AuthView";
import { BudgetView } from "./views/BudgetView";
import { HomeView } from "./views/HomeView";
import { PaymentsView } from "./views/PaymentsView";
import { SettingsView } from "./views/SettingsView";
import { TripsView } from "./views/TripsView";

const STORAGE_KEY = "spedex.dashboard.session";

export default function App() {
  const [activeView, setActiveView] = useState<ViewId>("home");
  const [searchQuery, setSearchQuery] = useState("");
  const [overview, setOverview] = useState<DashboardOverview | null>(null);
  const [vendors, setVendors] = useState<VendorDirectoryData | null>(null);
  const [budget, setBudget] = useState<BudgetScreenData | null>(null);
  const [analytics, setAnalytics] = useState<AnalyticsData | null>(null);
  const [sessionReady, setSessionReady] = useState(false);
  const [sessionUser, setSessionUser] = useState<DashboardOverview["user"] | null>(null);
  const [authMode, setAuthMode] = useState<AuthMode>("login");
  const [authName, setAuthName] = useState("");
  const [authEmail, setAuthEmail] = useState("");
  const [authPassword, setAuthPassword] = useState("");
  const [authError, setAuthError] = useState<string | null>(null);
  const [authSubmitting, setAuthSubmitting] = useState(false);
  const [warmingUp, setWarmingUp] = useState(false);
  const [showAddVendor, setShowAddVendor] = useState(false);
  const [showMobileSync, setShowMobileSync] = useState(true);
  const [activeTrip, setActiveTrip] = useState<Trip | null>(null);
  const [showAuthPortal, setShowAuthPortal] = useState(false);
  const [activeLegalDoc, setActiveLegalDoc] = useState<LegalDocType | null>(null);

  const deferredSearch = useDeferredValue(searchQuery);

  useEffect(() => {
    let mounted = true;

    async function restoreSession() {
      // Warm up Render backend (free tier cold start can take ~15s)
      const isProduction =
        typeof window !== "undefined" &&
        window.location.hostname !== "localhost" &&
        window.location.hostname !== "127.0.0.1";

      if (isProduction) {
        setWarmingUp(true);
        warmUpBackend().finally(() => {
          if (mounted) {
            setWarmingUp(false);
          }
        });
      }

      try {
        const raw = window.localStorage.getItem(STORAGE_KEY);
        if (raw) {
          const parsed = JSON.parse(raw) as { token?: string };
          if (parsed.token) {
            setAuthToken(parsed.token);
          }
        }

        // Always set default session user to bypass login/signup requirement
        if (mounted) {
          setSessionUser({
            id: 0,
            name: "Demo User",
            email: "demo@gmail.com",
            plan: "Pro Member",
            avatar_initials: "DU",
            member_since: "2026-07-01T00:00:00",
          });
        }

        const currentUser = await getCurrentUser().catch(() => null);
        if (mounted && currentUser) {
          setSessionUser(currentUser);
        }
      } catch (e: any) {
        if (mounted) {
          setSessionUser({
            id: 0,
            name: "Demo User",
            email: "demo@gmail.com",
            plan: "Pro Member",
            avatar_initials: "DU",
            member_since: "2026-07-01T00:00:00",
          });
        }
      } finally {
        if (mounted) {
          setSessionReady(true);
        }
      }
    }

    void restoreSession();

    return () => {
      mounted = false;
    };
  }, []);

  useEffect(() => {
    if (!sessionUser) {
      return;
    }

    let mounted = true;

    const bundlePromise = loadDashboardBundle();
    if (bundlePromise && typeof bundlePromise.then === "function") {
      bundlePromise
        .then((bundle) => {
          if (!mounted || !bundle) {
            return;
          }
          setOverview(bundle.overview);
          setVendors(bundle.vendors);
          setBudget(bundle.budget);
          setAnalytics(bundle.analytics);
        })
        .catch(() => {
          if (!mounted) {
            return;
          }
          setOverview(null);
          setVendors(null);
          setBudget(null);
          setAnalytics(null);
        });
    }

    return () => {
      mounted = false;
    };
  }, [sessionUser]);

  const filteredTransactions = useMemo(
    () => filterTransactions(overview?.recent_transactions ?? [], deferredSearch),
    [overview?.recent_transactions, deferredSearch],
  );

  async function handleAuthSubmit() {
    setAuthSubmitting(true);
    setAuthError(null);
    try {
      const response =
        authMode === "login"
          ? await login({ email: authEmail.trim(), password: authPassword })
          : await signUp({
              name: authName.trim(),
              email: authEmail.trim(),
              password: authPassword,
            });

      setAuthToken(response.access_token);
      window.localStorage.setItem(STORAGE_KEY, JSON.stringify({ token: response.access_token }));
      setSessionUser(response.user);
    } catch (error) {
      setAuthError(error instanceof Error ? error.message : "Unable to authenticate");
    } finally {
      setAuthSubmitting(false);
    }
  }

  async function handleRefresh() {
    const bundle = await loadDashboardBundle();
    setOverview(bundle.overview);
    setVendors(bundle.vendors);
    setBudget(bundle.budget);
    setAnalytics(bundle.analytics);
    try {
      const trips = await getTrips();
      const currentActive = (trips || []).find((t) => t.status === "ACTIVE");
      setActiveTrip(currentActive || null);
    } catch (e) {
      // ignore
    }
  }

  function handleSignOut() {
    setAuthToken(null);
    window.localStorage.removeItem(STORAGE_KEY);
    setSessionUser(null);
    setActiveView("home");
  }

  async function handleAddVendor(payload: any) {
    try {
      await addVendor(payload);
      setShowAddVendor(false);
      const bundle = await loadDashboardBundle();
      setOverview(bundle.overview);
      setVendors(bundle.vendors);
      setBudget(bundle.budget);
      setAnalytics(bundle.analytics);
    } catch (e) {
      alert("Failed to add vendor.");
    }
  }

  if (!sessionReady) {
    return (
      <main className="auth-shell">
        <section className="auth-card" style={{ textAlign: "center" }}>
          <BrandLockup />
          <p className="eyebrow">Loading</p>
          <h2 className="page-title">Restoring your session...</h2>
        </section>
      </main>
    );
  }

  if (!sessionUser) {
    if (!showAuthPortal) {
      return <LandingPage onLaunchDashboard={() => setShowAuthPortal(true)} />;
    }
    return (
      <AuthView
        mode={authMode}
        onModeChange={(mode) => setAuthMode(mode)}
        name={authName}
        email={authEmail}
        password={authPassword}
        onNameChange={(value) => setAuthName(value)}
        onEmailChange={(value) => setAuthEmail(value)}
        onPasswordChange={(value) => setAuthPassword(value)}
        onSubmit={handleAuthSubmit}
        submitting={authSubmitting}
        error={authError}
        warmingUp={warmingUp}
        onBackToLanding={() => setShowAuthPortal(false)}
      />
    );
  }

  let content = null;
  const isDashboardLoading = !overview || !vendors || !budget || !analytics;

  if (!isDashboardLoading) {
    if (activeView === "landing") {
      content = <LandingPage onLaunchDashboard={() => setActiveView("home")} />;
    } else if (activeView === "home") {
      content = (
        <HomeView
          overview={overview!}
          filteredTransactions={filteredTransactions}
          onAddVendor={() => setShowAddVendor(true)}
        />
      );
    } else if (activeView === "payments") {
      content = <PaymentsView vendors={vendors!} onAddVendor={() => setShowAddVendor(true)} />;
    } else if (activeView === "trips") {
      content = <TripsView />;
    } else if (activeView === "analytics") {
      content = <AnalyticsView analytics={analytics!} />;
    } else if (activeView === "budget") {
      content = <BudgetView budget={budget!} />;
    } else if (activeView === "settings") {
      content = <SettingsView overview={overview!} onRefresh={handleRefresh} />;
    } else if (activeView === "privacy") {
      content = <PrivacyCenter onOpenDoc={(doc) => setActiveLegalDoc(doc)} onLogout={handleSignOut} />;
    }
  } else {
    content = <SkeletonLoaderView view={activeView} />;
  }

  return (
    <div
      className="app-shell"
      style={{ display: "flex", flexDirection: "row", width: "100%", height: "100vh", overflow: "hidden" }}
    >
      {showAddVendor && <AddVendorModal onClose={() => setShowAddVendor(false)} onSave={handleAddVendor} />}
      {activeLegalDoc && <LegalDocViewer docType={activeLegalDoc} onClose={() => setActiveLegalDoc(null)} />}
      <CookieConsentBanner
        onOpenPreferences={() => setActiveView("privacy")}
        onOpenDoc={(doc) => setActiveLegalDoc(doc as LegalDocType)}
      />
      <Sidebar activeView={activeView} onSelect={(view) => startTransition(() => setActiveView(view))} />
      <main className="main-pane" style={{ flex: 1, overflowY: "auto" }}>
        <Topbar
          query={searchQuery}
          onQueryChange={(value) => startTransition(() => setSearchQuery(value))}
          user={sessionUser}
          onSignOut={handleSignOut}
        />
        {content}
      </main>
      {showMobileSync && (
        <aside
          style={{
            padding: "20px",
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
            borderLeft: "1px solid rgba(255,255,255,0.08)",
            backgroundColor: "#0b0f19",
          }}
        >
          <MobileSyncSimulator
            overviewData={overview}
            activeTrip={activeTrip}
            onRefreshData={handleRefresh}
            budgetData={budget}
          />
        </aside>
      )}
      <div className="app-version-indicator" aria-label={`Spedex Version ${APP_VERSION}`}>
        v{APP_VERSION}
      </div>
    </div>
  );
}
