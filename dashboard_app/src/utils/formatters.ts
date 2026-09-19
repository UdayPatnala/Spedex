import type { Transaction } from "../types";

export const categoryMeta: Record<string, { emoji: string; icon: string }> = {
  Dining: { emoji: "\u{1F374}", icon: "restaurant" },
  Groceries: { emoji: "\u{1F6D2}", icon: "shopping_basket" },
  Miscellaneous: { emoji: "\u{2728}", icon: "payments" },
  Transport: { emoji: "\u{1F687}", icon: "directions_bus" },
  Bills: { emoji: "\u{1F4A1}", icon: "bolt" },
  Shopping: { emoji: "\u{1F6CD}\u{FE0F}", icon: "shopping_bag" },
  Health: { emoji: "\u{1F48A}", icon: "fitness_center" },
  Rent: { emoji: "\u{1F3E0}", icon: "home_work" },
  Subscriptions: { emoji: "\u{1F4F2}", icon: "cloud" },
  Income: { emoji: "\u{1F4B0}", icon: "payments" },
};

export function formatCurrency(amount: number): string {
  return new Intl.NumberFormat("en-IN", {
    style: "currency",
    currency: "INR",
    maximumFractionDigits: Number.isInteger(amount) ? 0 : 2,
  }).format(amount);
}

export function formatDate(isoDate: string): string {
  return new Date(isoDate).toLocaleDateString("en-IN", {
    month: "short",
    day: "numeric",
  });
}

export function formatTime(isoDate: string): string {
  return new Date(isoDate).toLocaleTimeString("en-IN", {
    hour: "numeric",
    minute: "2-digit",
  });
}

export function monthYear(isoDate: string): string {
  return new Date(isoDate).toLocaleDateString("en-IN", {
    month: "short",
    year: "numeric",
  });
}

export function categoryEmoji(category: string): string {
  return categoryMeta[category]?.emoji ?? "\u{2728}";
}

export function categoryLabel(category: string): string {
  return `${categoryEmoji(category)} ${category}`;
}

export function iconFor(name: string): string {
  const map: Record<string, string> = {
    coffee: "local_cafe",
    subway: "subway",
    menu_book: "menu_book",
    restaurant: "restaurant",
    shopping_basket: "shopping_basket",
    directions_car: "directions_car",
    directions_bus: "directions_bus",
    bolt: "bolt",
    wifi: "wifi",
    cloud: "cloud",
    home_work: "home_work",
    fitness_center: "fitness_center",
    shopping_bag: "shopping_bag",
    event_busy: "event_busy",
    insights: "insights",
    payments: "payments",
  };
  return map[name] ?? "payments";
}

export function accentClass(accent: string): string {
  const map: Record<string, string> = {
    rose: "accent-rose",
    mint: "accent-mint",
    amber: "accent-amber",
    lavender: "accent-lavender",
  };
  return map[accent] ?? "accent-rose";
}

export function clampPercent(value: number): number {
  return Math.max(0, Math.min(value, 1)) * 100;
}

export function filterTransactions(transactions: Transaction[], query: string): Transaction[] {
  const lowered = query.trim().toLowerCase();
  if (!lowered) {
    return transactions;
  }
  return transactions.filter((transaction) =>
    `${transaction.description || ""} ${transaction.category || ""} ${transaction.account_label || ""}`
      .toLowerCase()
      .includes(lowered),
  );
}
