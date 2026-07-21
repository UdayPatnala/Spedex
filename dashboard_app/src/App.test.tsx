import { fireEvent, render, screen, waitFor } from "@testing-library/react";

import App from "./App";
import * as api from "./api";

vi.mock("./api", () => ({
  login: vi.fn(),
  signUp: vi.fn(),
  getCurrentUser: vi.fn(),
  loadDashboardBundle: vi.fn().mockResolvedValue({
    overview: {
      user: { id: 1, name: "Demo User", email: "demo@gmail.com", plan: "Pro Member", avatar_initials: "DU", member_since: "2026-07-01T00:00:00" },
      activeTrip: null,
      summary: { totalBalance: 15400, monthlySpend: 4200, activeVendorsCount: 3 },
      weekly_spending: [1200, 2400, 1800, 3100, 2900, 1500, 4200],
      recent_transactions: [],
      quick_pay: [],
      reminders: []
    },
    vendors: [],
    budget: { monthlyLimit: 20000, currentSpend: 4200, categoryLimits: [] },
    analytics: { weeklyTrends: [], topCategories: [] }
  }),
  warmUpBackend: vi.fn().mockResolvedValue(true),
  setAuthToken: vi.fn(),
  addVendor: vi.fn(),
  updateProfile: vi.fn(),
}));

describe("App Workspace Direct Access (Login Page Removed)", () => {
  beforeEach(() => {
    vi.clearAllMocks();
    Object.defineProperty(window, "localStorage", {
      value: {
        getItem: vi.fn().mockReturnValue(null),
        setItem: vi.fn(),
        removeItem: vi.fn(),
        clear: vi.fn(),
      },
      writable: true,
    });
  });

  it("loads Dashboard Workspace directly without login prompt", async () => {
    render(<App />);

    await waitFor(() => {
      expect(screen.getByText(/Recent Transactions/i)).toBeInTheDocument();
    });

    expect(screen.queryByText(/Sign in to your personal wallet/i)).not.toBeInTheDocument();
  });

  it("navigates cleanly across workspace views", async () => {
    render(<App />);

    await waitFor(() => {
      expect(screen.getByText(/Recent Transactions/i)).toBeInTheDocument();
    });

    expect(screen.getByText(/Quick Pay/i)).toBeInTheDocument();
  });
});
