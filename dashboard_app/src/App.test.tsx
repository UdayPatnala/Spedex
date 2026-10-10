import { fireEvent, render, screen, waitFor } from "@testing-library/react";
import { beforeEach, describe, expect, it, vi } from "vitest";

import App from "./App";
import * as api from "./api";

vi.mock("./api", () => ({
  login: vi.fn(),
  signUp: vi.fn(),
  getCurrentUser: vi.fn(),
  getCapabilities: vi.fn().mockResolvedValue({
    canInitiatePayment: true,
    canOpenPaymentLink: true,
    canSavePaymentMethod: true,
    canUseMerchantQR: true,
    canRecordManualTransaction: true,
    canManageBudgets: true,
    canUseAnalytics: true,
    canReceiveMarketing: true,
    mode: "FULL_TRANSACTIONAL",
  }),
  getTrips: vi.fn().mockResolvedValue([]),
  loadDashboardBundle: vi.fn().mockResolvedValue({
    overview: {
      user: { id: 1, name: "Demo User", email: "demo@gmail.com", plan: "Pro Member", avatar_initials: "DU", member_since: "2026-07-01T00:00:00" },
      activeTrip: null,
      monthly_total: 4200,
      monthly_budget: 20000,
      budget_used_ratio: 0.21,
      budget_copy: "You're on track",
      weekly_spending: [1200, 2400, 1800, 3100, 2900, 1500, 4200],
      peak_day_label: "Saturday",
      weekly_average: 2450,
      security_message: "Protected by end-to-end encryption",
      recent_transactions: [
        {
          id: 1,
          description: "Cafeteria Lunch",
          category: "Food",
          amount: 180,
          direction: "expense",
          payment_method: "UPI",
          account_label: "Primary",
          status: "completed",
          occurred_at: "2026-09-30T12:00:00",
        },
      ],
      quick_pay: [],
      reminders: [],
    },
    vendors: { user: { id: 1, name: "Demo User", email: "demo@gmail.com", plan: "Pro Member", avatar_initials: "DU", member_since: "2026-07-01T00:00:00" }, groups: {} },
    budget: { remaining_budget: 15800, budgets: [], reminders: [], savings_tip: "Great pace!" },
    analytics: { total_spent: 4200, smart_insight: "Normal spending", category_breakdown: [], weekly_spend: [], highest_sector: { title: "Food", subtitle: "40%", accent: "rose", icon: "restaurant" }, busiest_day: { title: "Sat", subtitle: "Peak", accent: "amber", icon: "calendar" }, weekday_ratio: 0.6, weekend_ratio: 0.4 },
  }),
  warmUpBackend: vi.fn().mockResolvedValue(true),
  setAuthToken: vi.fn(),
  addVendor: vi.fn(),
  updateProfile: vi.fn(),
  getCategories: vi.fn().mockResolvedValue([]),
  getPaymentReferences: vi.fn().mockResolvedValue([]),
  getTransactions: vi.fn().mockResolvedValue({
    content: [],
    total_elements: 0,
    total_pages: 0,
    page_number: 0,
    page_size: 15,
  }),
}));

describe("App Authentication & Workspace Flow", () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it("renders Landing Page when unauthenticated", async () => {
    Object.defineProperty(window, "localStorage", {
      value: {
        getItem: vi.fn().mockReturnValue(null),
        setItem: vi.fn(),
        removeItem: vi.fn(),
        clear: vi.fn(),
      },
      writable: true,
    });

    render(<App />);

    await waitFor(() => {
      expect(screen.getByText(/The Elegant Smart Wallet/i)).toBeInTheDocument();
    });

    expect(screen.getByRole("button", { name: /Enter Workspace/i })).toBeInTheDocument();
  });

  it("loads Dashboard Workspace directly when session token exists", async () => {
    Object.defineProperty(window, "localStorage", {
      value: {
        getItem: vi.fn().mockReturnValue(JSON.stringify({ token: "valid-jwt-token" })),
        setItem: vi.fn(),
        removeItem: vi.fn(),
        clear: vi.fn(),
      },
      writable: true,
    });

    vi.mocked(api.getCurrentUser).mockResolvedValue({
      id: 1,
      name: "Demo User",
      email: "demo@gmail.com",
      plan: "Pro Member",
      avatar_initials: "DU",
      member_since: "2026-07-01T00:00:00",
    });

    render(<App />);

    await waitFor(() => {
      expect(screen.getByText(/Recent Transactions/i)).toBeInTheDocument();
    });
  });

  it("navigates cleanly across workspace views including Ledger", async () => {
    Object.defineProperty(window, "localStorage", {
      value: {
        getItem: vi.fn().mockReturnValue(JSON.stringify({ token: "valid-jwt-token" })),
        setItem: vi.fn(),
        removeItem: vi.fn(),
        clear: vi.fn(),
      },
      writable: true,
    });

    vi.mocked(api.getCurrentUser).mockResolvedValue({
      id: 1,
      name: "Demo User",
      email: "demo@gmail.com",
      plan: "Pro Member",
      avatar_initials: "DU",
      member_since: "2026-07-01T00:00:00",
    });

    render(<App />);

    await waitFor(() => {
      expect(screen.getByText(/Recent Transactions/i)).toBeInTheDocument();
    });

    const ledgerBtn = screen.getByRole("button", { name: /Ledger/i });
    fireEvent.click(ledgerBtn);

    await waitFor(() => {
      expect(screen.getByText(/Transaction History/i)).toBeInTheDocument();
    });
  });
});
