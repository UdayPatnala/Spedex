import { describe, it, expect, vi, beforeEach } from "vitest";
import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import React from "react";
import { PrivacyCenter } from "../privacy/PrivacyCenter";
import * as api from "../../api";

vi.mock("../../api", () => ({
  getPrivacySettings: vi.fn(),
  getGrievances: vi.fn(),
  updatePrivacyConsents: vi.fn(),
  requestGuardianConsent: vi.fn(),
  exportUserData: vi.fn(),
  submitGrievance: vi.fn(),
  eraseUserData: vi.fn(),
}));

describe("PrivacyCenter Component", () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it("renders privacy settings for adult account", async () => {
    (api.getPrivacySettings as any).mockResolvedValue({
      analyticsConsent: true,
      marketingConsent: false,
      isMinor: false,
      age: 22,
      guardianConsentStatus: "NOT_REQUIRED",
      dataRetentionDays: 180,
      canExport: true,
      canRequestErasure: true,
    });
    (api.getGrievances as any).mockResolvedValue([]);

    render(<PrivacyCenter onOpenDoc={vi.fn()} />);

    await waitFor(() => {
      expect(screen.getByText("Privacy & DPDP Compliance Center")).toBeInTheDocument();
      expect(screen.getByText("Standard Adult Account (Age 22)")).toBeInTheDocument();
    });
  });

  it("updates analytics consent toggle", async () => {
    (api.getPrivacySettings as any).mockResolvedValue({
      analyticsConsent: false,
      marketingConsent: false,
      isMinor: false,
      age: 22,
      guardianConsentStatus: "NOT_REQUIRED",
    });
    (api.getGrievances as any).mockResolvedValue([]);
    (api.updatePrivacyConsents as any).mockResolvedValue({
      analyticsConsent: true,
      marketingConsent: false,
      isMinor: false,
      age: 22,
      guardianConsentStatus: "NOT_REQUIRED",
    });

    render(<PrivacyCenter onOpenDoc={vi.fn()} />);

    await waitFor(() => {
      expect(screen.getByText("Telemetry & Performance Analytics")).toBeInTheDocument();
    });

    const toggleBtns = screen.getAllByRole("button", { name: /Withdrawn/i });
    fireEvent.click(toggleBtns[0]);

    await waitFor(() => {
      expect(api.updatePrivacyConsents).toHaveBeenCalledWith({
        analyticsConsent: true,
        marketingConsent: false,
      });
    });
  });

  it("displays accidental data loss prevention shield in erasure modal", async () => {
    (api.getPrivacySettings as any).mockResolvedValue({
      analyticsConsent: true,
      marketingConsent: false,
      isMinor: false,
      age: 22,
      guardianConsentStatus: "NOT_REQUIRED",
      canExport: true,
      canRequestErasure: true,
    });
    (api.getGrievances as any).mockResolvedValue([]);
    (api.exportUserData as any).mockResolvedValue({ user: { id: 1, name: "Test" } });

    render(<PrivacyCenter onOpenDoc={vi.fn()} />);

    await waitFor(() => {
      expect(screen.getByText("Request Account Erasure")).toBeInTheDocument();
    });

    fireEvent.click(screen.getByText("Request Account Erasure"));

    expect(screen.getByText("Confirm Permanent Erasure")).toBeInTheDocument();
    expect(screen.getByText("Accidental Data Loss Prevention Shield")).toBeInTheDocument();
    expect(screen.getByText(/Download Full Backup \(JSON\) First/i)).toBeInTheDocument();

    const deleteBtn = screen.getByRole("button", { name: "Permanently Erase" });
    expect(deleteBtn).toBeDisabled();

    const input = screen.getByPlaceholderText("DELETE MY DATA");
    fireEvent.change(input, { target: { value: "DELETE MY DATA" } });

    expect(deleteBtn).not.toBeDisabled();
  });
});
