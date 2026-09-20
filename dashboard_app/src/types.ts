export type SpedexUser = {
  id: number;
  name: string;
  email: string;
  plan: string;
  avatar_initials: string;
  member_since: string;
  profile_picture_url?: string | null;
  is_minor?: boolean;
  age?: number;
  guardian_email?: string | null;
  guardian_name?: string | null;
  guardian_consent_status?: "NOT_REQUIRED" | "PENDING" | "VERIFIED" | "REJECTED";
  analytics_consent?: boolean;
  marketing_consent?: boolean;
  location_consent?: boolean;
  ai_consent?: boolean;
};

export type AuthResponse = {
  access_token: string;
  token_type: "bearer";
  user: SpedexUser;
};

export type Vendor = {
  id: number;
  name: string;
  category: string;
  icon: string;
  accent: string;
  upi_handle: string;
  default_amount: number;
  is_quick_pay: boolean;
};

export type VendorCreate = {
  name: string;
  category: string;
  upi_handle: string;
  default_amount?: number;
  is_quick_pay?: boolean;
};

export type Transaction = {
  id: number;
  description: string;
  category: string;
  amount: number;
  direction: "expense" | "income";
  payment_method: string;
  account_label: string;
  status: string;
  occurred_at: string;
};

export type Reminder = {
  id: number;
  title: string;
  subtitle: string;
  amount: number;
  due_date: string;
  autopay_enabled: boolean;
  status: string;
};

export type BudgetCard = {
  id: number;
  category: string;
  icon: string;
  accent: string;
  spent: number;
  limit_amount: number;
  progress: number;
};

export type DashboardOverview = {
  user: SpedexUser;
  monthly_total: number;
  monthly_budget: number;
  budget_used_ratio: number;
  budget_copy: string;
  quick_pay: Vendor[];
  recent_transactions: Transaction[];
  reminders: Reminder[];
  weekly_spending: number[];
  peak_day_label: string;
  weekly_average: number;
  security_message: string;
};

export type VendorDirectoryData = {
  user: SpedexUser;
  groups: Record<string, Vendor[]>;
};

export type BudgetScreenData = {
  remaining_budget: number;
  budgets: BudgetCard[];
  reminders: Reminder[];
  savings_tip: string;
};

export type AnalyticsData = {
  total_spent: number;
  smart_insight: string;
  category_breakdown: Array<{ category: string; percentage: number; accent: string }>;
  weekly_spend: Array<{ week_label: string; amount: number; is_active: boolean }>;
  highest_sector: { title: string; subtitle: string; accent: string; icon: string };
  busiest_day: { title: string; subtitle: string; accent: string; icon: string };
  weekday_ratio: number;
  weekend_ratio: number;
};

export type Trip = {
  id: number;
  name: string;
  status: "ACTIVE" | "COMPLETED";
  created_at: string;
  completed_at: string | null;
};

export type CategoryBreakdown = {
  category: string;
  amount: number;
  percentage: number;
};

export type TripDetails = {
  id: number;
  name: string;
  status: "ACTIVE" | "COMPLETED";
  created_at: string;
  completed_at: string | null;
  total_spend: number;
  cash_spend: number;
  card_online_spend: number;
  category_breakdown: CategoryBreakdown[];
  transactions: Transaction[];
};

export type UserCapabilities = {
  canInitiatePayment: boolean;
  canOpenPaymentLink: boolean;
  canSavePaymentMethod: boolean;
  canUseMerchantQR: boolean;
  canRecordManualTransaction: boolean;
  canManageBudgets: boolean;
  canUseAnalytics: boolean;
  canReceiveMarketing: boolean;
  mode: "FULL_TRANSACTIONAL" | "LEARNING_JOURNAL";
  restrictionNotice?: string | null;
};

export type PrivacySettings = {
  analyticsConsent: boolean;
  marketingConsent: boolean;
  locationConsent?: boolean;
  aiConsent?: boolean;
  isMinor: boolean;
  age: number;
  guardianEmail?: string | null;
  guardianName?: string | null;
  guardianConsentStatus: "NOT_REQUIRED" | "PENDING" | "VERIFIED" | "REJECTED";
  dataRetentionDays: number;
  canExport: boolean;
  canRequestErasure: boolean;
};

export type ConsentRecord = {
  id: number;
  consentType: string;
  action: string;
  timestamp: string;
  policyVersion: string;
  details?: string;
};

export type PrivacyAuditLog = {
  id: number;
  userEmail: string;
  eventType: string;
  description: string;
  ipAddress?: string;
  userAgent?: string;
  timestamp: string;
};

export type PrivacyGrievance = {
  ticketId: string;
  category: string;
  description: string;
  status: "OPEN" | "UNDER_REVIEW" | "RESOLVED" | "REJECTED";
  createdAt: string;
  redressedAt?: string | null;
  responseMessage?: string | null;
};

export type SubprocessorInfo = {
  name: string;
  purpose: string;
  location: string;
  dataHandled: string;
  safeguards: string;
};

export type UserDataExport = {
  exportTimestamp: string;
  userData: SpedexUser;
  privacySettings: PrivacySettings;
  capabilities?: UserCapabilities;
  transactions?: Transaction[];
  vendors?: Vendor[];
  budgets?: BudgetCard[];
  reminders?: Reminder[];
  trips: Trip[];
  consentHistory: ConsentRecord[];
  auditLogs?: PrivacyAuditLog[];
  grievanceHistory: PrivacyGrievance[];
  complianceNotice: string;
};

export type LegalDocType =
  | "terms"
  | "privacy"
  | "consent"
  | "cookies"
  | "child-privacy"
  | "data-retention"
  | "grievance"
  | "third-parties";

export type LegalDocument = {
  docType: LegalDocType;
  title: string;
  content: string;
  lastUpdated: string;
};

export type ViewId =
  | "landing"
  | "home"
  | "payments"
  | "analytics"
  | "budget"
  | "settings"
  | "trips"
  | "privacy";

export type AuthMode = "login" | "signup";

