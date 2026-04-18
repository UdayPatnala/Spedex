import { formatCurrency, iconFor, accentPalette } from "./helpers";
import { colors } from "./tokens";

describe("helpers", () => {
  describe("formatCurrency", () => {
    it("formats a positive number to a currency string with two decimal places", () => {
      expect(formatCurrency(123.45)).toBe("$123.45");
      expect(formatCurrency(100)).toBe("$100.00");
    });

    it("formats a negative number correctly", () => {
      expect(formatCurrency(-123.45)).toBe("$-123.45");
    });

    it("formats zero correctly", () => {
      expect(formatCurrency(0)).toBe("$0.00");
    });
  });

  describe("iconFor", () => {
    it("returns the correct icon for a known category", () => {
      expect(iconFor("coffee")).toBe("local-cafe");
      expect(iconFor("restaurant")).toBe("restaurant");
    });

    it("returns 'payments' for an unknown category", () => {
      expect(iconFor("unknown-category")).toBe("payments");
    });
  });

  describe("accentPalette", () => {
    it("returns the correct palette for a known accent", () => {
      const mintPalette = accentPalette("mint");
      expect(mintPalette).toEqual({
        bg: colors.secondaryFixed,
        text: colors.secondary,
      });
    });

    it("returns the indigo palette for an unknown accent", () => {
      const defaultPalette = accentPalette("unknown-accent");
      expect(defaultPalette).toEqual({
        bg: "#dde2ff",
        text: colors.primary,
      });
    });
  });
});
