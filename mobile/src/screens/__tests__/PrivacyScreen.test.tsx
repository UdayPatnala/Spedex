import React from "react";
import { render, waitFor, screen } from "@testing-library/react-native";
import { PrivacyScreen } from "../PrivacyScreen";
import { spedexApi } from "../../api/client";

jest.mock("../../api/client", () => ({
  spedexApi: {
    getPrivacySettings: jest.fn(),
    getUserGrievances: jest.fn(),
    updatePrivacyConsent: jest.fn(),
    exportUserData: jest.fn(),
    requestErasure: jest.fn(),
    submitGrievance: jest.fn(),
  },
}));

describe("PrivacyScreen", () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  it("renders privacy settings for adult account", async () => {
    (spedexApi.getPrivacySettings as jest.Mock).mockResolvedValue({
      user_id: 1,
      is_minor: false,
      age: 22,
      guardian_email: null,
      guardian_name: null,
      guardian_consent_status: "NOT_REQUIRED",
      analytics_consent: true,
      marketing_consent: false,
      is_erased: false,
      updated_at: "2026-09-11T10:00:00",
    });
    (spedexApi.getUserGrievances as jest.Mock).mockResolvedValue([]);

    render(<PrivacyScreen />);

    await waitFor(() => {
      expect(screen.getByText("DPDP Compliance")).toBeTruthy();
      expect(screen.getByText("Account Privacy Status")).toBeTruthy();
      expect(screen.getByText("Consent Preferences")).toBeTruthy();
    });
  });
});
