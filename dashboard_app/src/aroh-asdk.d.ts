declare module "@aroh/asdk" {
  export interface ArohUser {
    id: string | number;
    email: string;
  }

  export interface ArohProfile {
    displayName: string;
  }

  export interface ArohWallet {
    balance: number;
  }

  export interface PlatformStore {
    user: ArohUser | null;
    profile: ArohProfile | null;
    wallet: ArohWallet | null;
    token: string | null;
    isAuthenticated: boolean;
    logout: () => Promise<void> | void;
    rewardUser: (userId: string | number, amount: number, note: string) => Promise<void>;
  }

  export function usePlatformStore(): PlatformStore;
}
