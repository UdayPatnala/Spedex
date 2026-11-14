import { describe, it, expect, vi, beforeEach } from "vitest";
import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import React from "react";
import { SettingsView } from "../../views/SettingsView";
import * as api from "../../api";
import type { DashboardOverview } from "../../types";

vi.mock("../../api", () => ({
  updateProfile: vi.fn(),
  exportUserData: vi.fn(),
  downloadTransactionsCsv: vi.fn(),
}));

const mockOverview: DashboardOverview = {
  user: {
    id: 1,
    name: "Alex Patel",
    email: "alex@example.com",
    plan: "Free Campus Tier",
    avatar_initials: "AP",
    is_minor: false,
    profile_picture_url: "",
  },
  kpis: {
    total_spent: 12000,
    daily_spend: 400,
    budget_remaining: 8000,
    trips_active: 0,
  },
  recent_transactions: [],
  active_trip: null,
};

describe("SettingsView Component", () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it("renders user identity and profile metadata", () => {
    render(<SettingsView overview={mockOverview} onRefresh={vi.fn()} />);
    expect(screen.getByText("Alex Patel")).toBeInTheDocument();
    expect(screen.getByText("alex@example.com")).toBeInTheDocument();
    expect(screen.getByText("Free Campus Tier")).toBeInTheDocument();
  });

  it("renders Accidental Data Loss Prevention section and handles backups", async () => {
    (api.exportUserData as any).mockResolvedValue({ id: 1, name: "Alex Patel" });
    (api.downloadTransactionsCsv as any).mockResolvedValue(undefined);

    // Mock URL methods for downloading
    window.URL.createObjectURL = vi.fn(() => "blob:test");
    window.URL.revokeObjectURL = vi.fn();

    render(<SettingsView overview={mockOverview} onRefresh={vi.fn()} />);

    expect(screen.getByText("Accidental Data Loss Prevention")).toBeInTheDocument();
    expect(screen.getByText("Offline Backups & Data Safety Hub")).toBeInTheDocument();

    const jsonBtn = screen.getByText(/Download Full Backup \(JSON\)/i);
    fireEvent.click(jsonBtn);

    await waitFor(() => {
      expect(api.exportUserData).toHaveBeenCalled();
    });

    const csvBtn = screen.getByText(/Export Ledger \(CSV\)/i);
    fireEvent.click(csvBtn);

    await waitFor(() => {
      expect(api.downloadTransactionsCsv).toHaveBeenCalled();
    });
  });

  it("toggles preferences interactively", () => {
    render(<SettingsView overview={mockOverview} onRefresh={vi.fn()} />);

    const alertBtn = screen.getAllByRole("button", { name: "Enabled" })[0];
    expect(alertBtn).toBeInTheDocument();

    fireEvent.click(alertBtn);
    expect(alertBtn).toHaveTextContent("Disabled");
  });
});
