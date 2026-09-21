/**
 * SpeDex Offline Foreign Exchange (Forex) Cache & Multi-Currency Converter
 *
 * Provides offline-first exchange rates for common student & youth travel currencies.
 * Base accounting currency is Indian Rupee (INR ₹).
 */

export interface CurrencyConfig {
  code: string;
  name: string;
  symbol: string;
  rate: number; // 1 Foreign Unit = X INR (e.g. 1 USD = 86.50 INR)
  flag: string;
}

export const FOREX_CACHE: Record<string, CurrencyConfig> = {
  INR: {
    code: "INR",
    name: "Indian Rupee",
    symbol: "₹",
    rate: 1.0,
    flag: "🇮🇳",
  },
  USD: {
    code: "USD",
    name: "US Dollar",
    symbol: "$",
    rate: 86.5,
    flag: "🇺🇸",
  },
  EUR: {
    code: "EUR",
    name: "Euro",
    symbol: "€",
    rate: 92.0,
    flag: "🇪🇺",
  },
  GBP: {
    code: "GBP",
    name: "British Pound",
    symbol: "£",
    rate: 110.0,
    flag: "🇬🇧",
  },
  AED: {
    code: "AED",
    name: "UAE Dirham",
    symbol: "AED",
    rate: 23.5,
    flag: "🇦🇪",
  },
  SGD: {
    code: "SGD",
    name: "Singapore Dollar",
    symbol: "S$",
    rate: 64.0,
    flag: "🇸🇬",
  },
  THB: {
    code: "THB",
    name: "Thai Baht",
    symbol: "฿",
    rate: 2.5,
    flag: "🇹🇭",
  },
  NPR: {
    code: "NPR",
    name: "Nepalese Rupee",
    symbol: "NPR",
    rate: 0.625,
    flag: "🇳🇵",
  },
  JPY: {
    code: "JPY",
    name: "Japanese Yen",
    symbol: "¥",
    rate: 0.58,
    flag: "🇯🇵",
  },
};

/**
 * Get exchange rate for a given currency code (INR equivalent for 1 unit).
 */
export function getExchangeRate(currencyCode: string): number {
  const code = (currencyCode || "INR").toUpperCase().trim();
  return FOREX_CACHE[code]?.rate ?? 1.0;
}

/**
 * Format an amount in the specified foreign currency.
 */
export function formatForeignCurrency(amount: number, currencyCode: string): string {
  const code = (currencyCode || "INR").toUpperCase().trim();
  const config = FOREX_CACHE[code];

  if (!config || code === "INR") {
    return new Intl.NumberFormat("en-IN", {
      style: "currency",
      currency: "INR",
      maximumFractionDigits: Number.isInteger(amount) ? 0 : 2,
    }).format(amount);
  }

  const formattedNumber = new Intl.NumberFormat("en-US", {
    minimumFractionDigits: Number.isInteger(amount) ? 0 : 2,
    maximumFractionDigits: 2,
  }).format(amount);

  return `${config.symbol}${formattedNumber} ${code}`;
}

/**
 * Convert INR figure to target foreign currency amount.
 */
export function convertInrToForeign(inrAmount: number, currencyCode: string, customRate?: number): number {
  const rate = customRate && customRate > 0 ? customRate : getExchangeRate(currencyCode);
  if (rate <= 0) return inrAmount;
  const result = inrAmount / rate;
  return Math.round(result * 100) / 100;
}

/**
 * Convert foreign currency amount to base INR figure.
 */
export function convertForeignToInr(foreignAmount: number, currencyCode: string, customRate?: number): number {
  const rate = customRate && customRate > 0 ? customRate : getExchangeRate(currencyCode);
  const result = foreignAmount * rate;
  return Math.round(result * 100) / 100;
}
