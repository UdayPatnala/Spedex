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
});
