import { describe, it, expect } from "vitest";
import { render, screen, fireEvent } from "@testing-library/react";
import React from "react";
import { SpendingVelocityWidget } from "../analytics/SpendingVelocityWidget";

describe("SpendingVelocityWidget", () => {
  it("renders correctly with sustainable pace", () => {
    // totalSpent = 5000, monthlyBudget = 25000, daysPassed = 10, totalDaysInMonth = 30
    // actual pace = 500/day, target pace = 833/day -> sustainable pace
    render(
      <SpendingVelocityWidget
        totalSpent={5000}
        monthlyBudget={25000}
        daysPassed={10}
        totalDaysInMonth={30}
      />
    );

    expect(screen.getByText("Real-Time Spending Velocity")).toBeInTheDocument();
    expect(screen.getByText("✓ Sustainable Pace")).toBeInTheDocument();
    expect(screen.getByText("TARGET DAILY CAP")).toBeInTheDocument();
    expect(screen.getByText("20 days")).toBeInTheDocument();
  });

  it("renders elevated burn rate when overpacing", () => {
    // totalSpent = 24000, monthlyBudget = 25000, daysPassed = 10, totalDaysInMonth = 30
    // actual pace = 2400/day, remaining budget = 1000 over 20 days -> 50/day -> overpacing
    render(
      <SpendingVelocityWidget
        totalSpent={24000}
        monthlyBudget={25000}
        daysPassed={10}
        totalDaysInMonth={30}
      />
    );

    expect(screen.getByText("⚡ Elevated Burn Rate")).toBeInTheDocument();
  });

  it("updates projected savings when the simulator slider is adjusted", () => {
    render(
      <SpendingVelocityWidget
        totalSpent={10000}
        monthlyBudget={25000}
        daysPassed={10}
        totalDaysInMonth={30}
      />
    );

    const slider = screen.getByRole("slider");
    expect(slider).toBeInTheDocument();

    fireEvent.change(slider, { target: { value: "200" } });
    expect(screen.getByText("+₹200/day")).toBeInTheDocument();
  });
});
