import React from "react";
import { render, waitFor } from "@testing-library/react-native";
import { TripsScreen } from "../TripsScreen";
import { spedexApi } from "../../api/client";

jest.mock("../../api/client", () => ({
  spedexApi: {
    getTrips: jest.fn(),
    getTripDetails: jest.fn(),
  },
}));

describe("TripsScreen", () => {
  it("renders trips title and start trip button", async () => {
    (spedexApi.getTrips as jest.Mock).mockResolvedValue([
      {
        id: 1,
        name: "Goa Trip",
        status: "ACTIVE",
        created_at: "2026-07-20T10:00:00",
        completed_at: null,
      },
    ]);
    (spedexApi.getTripDetails as jest.Mock).mockResolvedValue({
      id: 1,
      name: "Goa Trip",
      status: "ACTIVE",
      created_at: "2026-07-20T10:00:00",
      completed_at: null,
      total_spend: 1500,
      cash_spend: 500,
      card_online_spend: 1000,
      category_breakdown: [{ category: "Dining", amount: 500, percentage: 33.3 }],
      transactions: [],
    });

    const { getByText, getAllByText } = render(<TripsScreen />);

    await waitFor(() => {
      expect(getByText("Trips Ledger")).toBeTruthy();
      expect(getAllByText("Goa Trip").length).toBeGreaterThan(0);
    });
  });
});
