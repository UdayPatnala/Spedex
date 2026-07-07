import { usePlatformStore } from "@aroh/asdk";

export function useArohSpedexBridge() {
  const {
    user: arohUser,
    profile: arohProfile,
    wallet: arohWallet,
    token: arohToken,
    isAuthenticated,
    logout: arohLogout,
    rewardUser
  } = usePlatformStore();

  const user = arohUser && arohProfile && arohWallet ? {
    id: arohUser.id,
    name: arohProfile.displayName,
    email: arohUser.email,
    balance: arohWallet.balance
  } : null;

  // Debit Aros for Spedex balance transfers
  const executePayment = async (amount: number, description: string) => {
    if (!arohUser) throw new Error("Authentication Required");
    if (!arohWallet || arohWallet.balance < amount) {
      throw new Error("Insufficient Aros Balance");
    }
    await rewardUser(arohUser.id, -amount, `Spedex Debit: ${description}`);
  };

  return {
    user,
    token: arohToken,
    isAuthenticated,
    executePayment,
    logout: arohLogout
  };
}
