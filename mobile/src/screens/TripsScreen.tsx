import React, { useEffect, useState } from "react";
import {
  ActivityIndicator,
  Alert,
  Modal,
  RefreshControl,
  SafeAreaView,
  ScrollView,
  StyleSheet,
  Text,
  TextInput,
  TouchableOpacity,
  View,
} from "react-native";
import { MaterialIcons } from "@expo/vector-icons";
import { spedexApi } from "../api/client";
import { colors } from "../theme/tokens";
import type { Trip, TripDetails } from "../types";

export function TripsScreen() {
  const [trips, setTrips] = useState<Trip[]>([]);
  const [activeTripDetails, setActiveTripDetails] = useState<TripDetails | null>(null);
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);

  // Start Trip Modal State
  const [startModalVisible, setStartModalVisible] = useState(false);
  const [newTripName, setNewTripName] = useState("");
  const [startingTrip, setStartingTrip] = useState(false);

  // Add Transaction Modal State
  const [txModalVisible, setTxModalVisible] = useState(false);
  const [txAmount, setTxAmount] = useState("");
  const [txCategory, setTxCategory] = useState("");
  const [txDescription, setTxDescription] = useState("");
  const [addingTx, setAddingTx] = useState(false);

  const activeTrip = trips.find((t) => t.status === "ACTIVE");

  const fetchTripsData = async () => {
    try {
      const data = await spedexApi.getTrips();
      setTrips(data || []);
      const currentActive = (data || []).find((t) => t.status === "ACTIVE");
      if (currentActive) {
        const details = await spedexApi.getTripDetails(currentActive.id);
        setActiveTripDetails(details);
      } else {
        setActiveTripDetails(null);
      }
    } catch (e) {
      console.error("Failed to load trips", e);
    } finally {
      setLoading(false);
      setRefreshing(false);
    }
  };

  useEffect(() => {
    fetchTripsData();
  }, []);

  const onRefresh = () => {
    setRefreshing(true);
    fetchTripsData();
  };

  const handleStartTrip = async () => {
    if (!newTripName.trim()) {
      Alert.alert("Error", "Please enter a trip name");
      return;
    }
    setStartingTrip(true);
    try {
      await spedexApi.startTrip(newTripName.trim());
      setNewTripName("");
      setStartModalVisible(false);
      await fetchTripsData();
    } catch (e: any) {
      Alert.alert("Error", e.message || "Failed to start trip");
    } finally {
      setStartingTrip(false);
    }
  };

  const handleCompleteTrip = async (tripId: number) => {
    Alert.alert("Complete Trip", "Are you sure you want to complete this trip?", [
      { text: "Cancel", style: "cancel" },
      {
        text: "Complete",
        style: "destructive",
        onPress: async () => {
          try {
            await spedexApi.completeTrip(tripId);
            await fetchTripsData();
          } catch (e: any) {
            Alert.alert("Error", e.message || "Failed to complete trip");
          }
        },
      },
    ]);
  };

  const handleAddTransaction = async () => {
    if (!activeTrip) return;
    const amountNum = parseFloat(txAmount);
    if (isNaN(amountNum) || amountNum <= 0) {
      Alert.alert("Error", "Please enter a valid amount");
      return;
    }
    setAddingTx(true);
    try {
      await spedexApi.addTripTransaction(activeTrip.id, {
        amount: amountNum,
        category: txCategory.trim() || "Miscellaneous",
        description: txDescription.trim(),
      });
      setTxAmount("");
      setTxCategory("");
      setTxDescription("");
      setTxModalVisible(false);
      await fetchTripsData();
    } catch (e: any) {
      Alert.alert("Error", e.message || "Failed to add expense");
    } finally {
      setAddingTx(false);
    }
  };

  if (loading) {
    return (
      <SafeAreaView style={styles.centerContainer}>
        <ActivityIndicator size="large" color={colors.primary} />
        <Text style={styles.loadingText}>Loading Trips Ledger...</Text>
      </SafeAreaView>
    );
  }

  return (
    <SafeAreaView style={styles.container}>
      <ScrollView
        contentContainerStyle={styles.scrollContent}
        refreshControl={<RefreshControl refreshing={refreshing} onRefresh={onRefresh} colors={[colors.primary]} />}
      >
        {/* Header Bar */}
        <View style={styles.headerRow}>
          <View>
            <Text style={styles.headerTitle}>Trips Ledger</Text>
            <Text style={styles.headerSubtitle}>Real-time travel spending tracking</Text>
          </View>
          <TouchableOpacity
            style={styles.startTripBtn}
            onPress={() => setStartModalVisible(true)}
            activeOpacity={0.8}
          >
            <MaterialIcons name="flight-takeoff" size={20} color="#FFF" />
            <Text style={styles.startTripBtnText}>New Trip</Text>
          </TouchableOpacity>
        </View>

        {/* Active Trip Banner / Details */}
        {activeTrip ? (
          <View style={styles.activeTripCard}>
            <View style={styles.activeTripHeader}>
              <View style={styles.activeBadge}>
                <View style={styles.activeDot} />
                <Text style={styles.activeBadgeText}>ACTIVE TRIP</Text>
              </View>
              <TouchableOpacity
                style={styles.completeBtn}
                onPress={() => handleCompleteTrip(activeTrip.id)}
              >
                <Text style={styles.completeBtnText}>Complete</Text>
              </TouchableOpacity>
            </View>

            <Text style={styles.activeTripName}>{activeTrip.name}</Text>
            <Text style={styles.activeTripDate}>
              Started {new Date(activeTrip.created_at).toLocaleDateString()}
            </Text>

            {/* Spend Stats */}
            {activeTripDetails && (
              <View style={styles.spendStatsRow}>
                <View style={styles.statBox}>
                  <Text style={styles.statLabel}>Total Spend</Text>
                  <Text style={styles.statValue}>₹{activeTripDetails.total_spend.toLocaleString()}</Text>
                </View>
                <View style={styles.statBox}>
                  <Text style={styles.statLabel}>Cash Spend</Text>
                  <Text style={styles.statValue}>₹{activeTripDetails.cash_spend.toLocaleString()}</Text>
                </View>
                <View style={styles.statBox}>
                  <Text style={styles.statLabel}>Card / Online</Text>
                  <Text style={styles.statValue}>₹{activeTripDetails.card_online_spend.toLocaleString()}</Text>
                </View>
              </View>
            )}

            {/* Quick Add Expense Action */}
            <TouchableOpacity
              style={styles.addExpenseBtn}
              onPress={() => setTxModalVisible(true)}
              activeOpacity={0.8}
            >
              <MaterialIcons name="add-circle-outline" size={20} color="#FFF" />
              <Text style={styles.addExpenseBtnText}>Log Cash Expense</Text>
            </TouchableOpacity>

            {/* Category Breakdown */}
            {activeTripDetails && activeTripDetails.category_breakdown.length > 0 && (
              <View style={styles.breakdownSection}>
                <Text style={styles.sectionTitle}>Category Split</Text>
                {activeTripDetails.category_breakdown.map((item, index) => (
                  <View key={index} style={styles.breakdownRow}>
                    <Text style={styles.breakdownCat}>{item.category}</Text>
                    <Text style={styles.breakdownVal}>
                      ₹{item.amount.toLocaleString()} ({item.percentage}%)
                    </Text>
                  </View>
                ))}
              </View>
            )}
          </View>
        ) : (
          <View style={styles.emptyCard}>
            <MaterialIcons name="card-travel" size={48} color={colors.onSurfaceVariant} />
            <Text style={styles.emptyTitle}>No Active Trip</Text>
            <Text style={styles.emptySubtitle}>Start a new trip to track location-based expenses automatically.</Text>
            <TouchableOpacity
              style={styles.emptyStartBtn}
              onPress={() => setStartModalVisible(true)}
            >
              <Text style={styles.emptyStartBtnText}>Start New Trip</Text>
            </TouchableOpacity>
          </View>
        )}

        {/* Trips History */}
        <View style={styles.historySection}>
          <Text style={styles.sectionTitle}>Trip History</Text>
          {trips.length === 0 ? (
            <Text style={styles.noHistoryText}>No past trips recorded.</Text>
          ) : (
            trips.map((trip) => (
              <View key={trip.id} style={styles.historyRow}>
                <View style={{ flex: 1 }}>
                  <Text style={styles.historyName}>{trip.name}</Text>
                  <Text style={styles.historyDate}>
                    {new Date(trip.created_at).toLocaleDateString()}
                  </Text>
                </View>
                <View style={[styles.statusPill, trip.status === "ACTIVE" ? styles.pillActive : styles.pillCompleted]}>
                  <Text style={styles.statusPillText}>{trip.status}</Text>
                </View>
              </View>
            ))
          )}
        </View>
      </ScrollView>

      {/* Start Trip Modal */}
      <Modal visible={startModalVisible} transparent animationType="slide">
        <View style={styles.modalOverlay}>
          <View style={styles.modalContent}>
            <Text style={styles.modalTitle}>Start New Trip</Text>
            <TextInput
              style={styles.input}
              placeholder="e.g. Goa Vacation, Mumbai Business Trip"
              placeholderTextColor="#94A3B8"
              value={newTripName}
              onChangeText={setNewTripName}
            />
            <View style={styles.modalActions}>
              <TouchableOpacity
                style={styles.cancelModalBtn}
                onPress={() => setStartModalVisible(false)}
              >
                <Text style={styles.cancelModalText}>Cancel</Text>
              </TouchableOpacity>
              <TouchableOpacity
                style={styles.confirmModalBtn}
                onPress={handleStartTrip}
                disabled={startingTrip}
              >
                {startingTrip ? (
                  <ActivityIndicator color="#FFF" />
                ) : (
                  <Text style={styles.confirmModalText}>Start Trip</Text>
                )}
              </TouchableOpacity>
            </View>
          </View>
        </View>
      </Modal>

      {/* Add Transaction Modal */}
      <Modal visible={txModalVisible} transparent animationType="slide">
        <View style={styles.modalOverlay}>
          <View style={styles.modalContent}>
            <Text style={styles.modalTitle}>Log Cash Expense</Text>
            <TextInput
              style={styles.input}
              placeholder="Amount (₹)"
              placeholderTextColor="#94A3B8"
              keyboardType="numeric"
              value={txAmount}
              onChangeText={setTxAmount}
            />
            <TextInput
              style={styles.input}
              placeholder="Category (e.g. Dining, Transport)"
              placeholderTextColor="#94A3B8"
              value={txCategory}
              onChangeText={setTxCategory}
            />
            <TextInput
              style={styles.input}
              placeholder="Description (e.g. Lunch at Udipi)"
              placeholderTextColor="#94A3B8"
              value={txDescription}
              onChangeText={setTxDescription}
            />
            <View style={styles.modalActions}>
              <TouchableOpacity
                style={styles.cancelModalBtn}
                onPress={() => setTxModalVisible(false)}
              >
                <Text style={styles.cancelModalText}>Cancel</Text>
              </TouchableOpacity>
              <TouchableOpacity
                style={styles.confirmModalBtn}
                onPress={handleAddTransaction}
                disabled={addingTx}
              >
                {addingTx ? (
                  <ActivityIndicator color="#FFF" />
                ) : (
                  <Text style={styles.confirmModalText}>Add Expense</Text>
                )}
              </TouchableOpacity>
            </View>
          </View>
        </View>
      </Modal>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: colors.background,
  },
  centerContainer: {
    flex: 1,
    backgroundColor: colors.background,
    alignItems: "center",
    justifyContent: "center",
  },
  loadingText: {
    marginTop: 12,
    color: colors.onSurfaceVariant,
    fontSize: 14,
  },
  scrollContent: {
    padding: 20,
    paddingBottom: 110,
  },
  headerRow: {
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
    marginBottom: 20,
  },
  headerTitle: {
    fontSize: 24,
    fontWeight: "800",
    color: colors.onBackground,
  },
  headerSubtitle: {
    fontSize: 13,
    color: colors.onSurfaceVariant,
  },
  startTripBtn: {
    flexDirection: "row",
    alignItems: "center",
    gap: 6,
    backgroundColor: colors.primary,
    paddingVertical: 10,
    paddingHorizontal: 16,
    borderRadius: 20,
  },
  startTripBtnText: {
    color: "#FFF",
    fontWeight: "700",
    fontSize: 14,
  },
  activeTripCard: {
    backgroundColor: "rgba(30, 41, 59, 0.9)",
    borderRadius: 24,
    padding: 20,
    borderWidth: 1,
    borderColor: "rgba(255,255,255,0.12)",
    marginBottom: 24,
  },
  activeTripHeader: {
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
    marginBottom: 12,
  },
  activeBadge: {
    flexDirection: "row",
    alignItems: "center",
    gap: 6,
    backgroundColor: "rgba(16, 185, 129, 0.15)",
    paddingVertical: 4,
    paddingHorizontal: 10,
    borderRadius: 12,
  },
  activeDot: {
    width: 8,
    height: 8,
    borderRadius: 4,
    backgroundColor: "#10B981",
  },
  activeBadgeText: {
    color: "#10B981",
    fontSize: 11,
    fontWeight: "700",
  },
  completeBtn: {
    backgroundColor: "rgba(239, 68, 68, 0.15)",
    paddingVertical: 6,
    paddingHorizontal: 12,
    borderRadius: 12,
  },
  completeBtnText: {
    color: "#F87171",
    fontSize: 12,
    fontWeight: "600",
  },
  activeTripName: {
    fontSize: 22,
    fontWeight: "800",
    color: "#F8FAFC",
  },
  activeTripDate: {
    fontSize: 12,
    color: "#94A3B8",
    marginTop: 2,
    marginBottom: 16,
  },
  spendStatsRow: {
    flexDirection: "row",
    gap: 10,
    marginBottom: 16,
  },
  statBox: {
    flex: 1,
    backgroundColor: "rgba(15, 23, 42, 0.6)",
    padding: 12,
    borderRadius: 14,
  },
  statLabel: {
    fontSize: 11,
    color: "#94A3B8",
  },
  statValue: {
    fontSize: 15,
    fontWeight: "700",
    color: "#F8FAFC",
    marginTop: 2,
  },
  addExpenseBtn: {
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "center",
    gap: 8,
    backgroundColor: "#6366F1",
    paddingVertical: 12,
    borderRadius: 16,
    marginBottom: 16,
  },
  addExpenseBtnText: {
    color: "#FFF",
    fontWeight: "700",
    fontSize: 14,
  },
  breakdownSection: {
    borderTopWidth: 1,
    borderTopColor: "rgba(255,255,255,0.08)",
    paddingTop: 14,
  },
  sectionTitle: {
    fontSize: 15,
    fontWeight: "700",
    color: colors.onBackground,
    marginBottom: 10,
  },
  breakdownRow: {
    flexDirection: "row",
    justifyContent: "space-between",
    paddingVertical: 4,
  },
  breakdownCat: {
    color: "#94A3B8",
    fontSize: 13,
  },
  breakdownVal: {
    color: "#F8FAFC",
    fontSize: 13,
    fontWeight: "600",
  },
  emptyCard: {
    backgroundColor: "rgba(30, 41, 59, 0.5)",
    borderRadius: 24,
    padding: 32,
    alignItems: "center",
    marginBottom: 24,
    borderWidth: 1,
    borderColor: "rgba(255,255,255,0.08)",
  },
  emptyTitle: {
    fontSize: 18,
    fontWeight: "700",
    color: "#F8FAFC",
    marginTop: 12,
  },
  emptySubtitle: {
    fontSize: 13,
    color: "#94A3B8",
    textAlign: "center",
    marginTop: 6,
    marginBottom: 16,
  },
  emptyStartBtn: {
    backgroundColor: colors.primary,
    paddingVertical: 10,
    paddingHorizontal: 20,
    borderRadius: 16,
  },
  emptyStartBtnText: {
    color: "#FFF",
    fontWeight: "700",
    fontSize: 14,
  },
  historySection: {
    marginTop: 8,
  },
  noHistoryText: {
    color: colors.onSurfaceVariant,
    fontSize: 13,
  },
  historyRow: {
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "space-between",
    backgroundColor: "rgba(30, 41, 59, 0.4)",
    padding: 14,
    borderRadius: 14,
    marginBottom: 10,
  },
  historyName: {
    fontSize: 15,
    fontWeight: "600",
    color: "#F8FAFC",
  },
  historyDate: {
    fontSize: 12,
    color: "#94A3B8",
    marginTop: 2,
  },
  statusPill: {
    paddingVertical: 4,
    paddingHorizontal: 10,
    borderRadius: 10,
  },
  pillActive: {
    backgroundColor: "rgba(16, 185, 129, 0.15)",
  },
  pillCompleted: {
    backgroundColor: "rgba(148, 163, 184, 0.15)",
  },
  statusPillText: {
    fontSize: 11,
    fontWeight: "700",
    color: "#94A3B8",
  },
  modalOverlay: {
    flex: 1,
    backgroundColor: "rgba(0,0,0,0.7)",
    justifyContent: "center",
    padding: 20,
  },
  modalContent: {
    backgroundColor: "#1E293B",
    borderRadius: 20,
    padding: 24,
    borderWidth: 1,
    borderColor: "rgba(255,255,255,0.1)",
  },
  modalTitle: {
    fontSize: 18,
    fontWeight: "700",
    color: "#F8FAFC",
    marginBottom: 16,
  },
  input: {
    backgroundColor: "rgba(15, 23, 42, 0.8)",
    borderRadius: 12,
    padding: 12,
    color: "#FFF",
    fontSize: 14,
    borderWidth: 1,
    borderColor: "rgba(255,255,255,0.1)",
    marginBottom: 12,
  },
  modalActions: {
    flexDirection: "row",
    justifyContent: "flex-end",
    gap: 12,
    marginTop: 12,
  },
  cancelModalBtn: {
    paddingVertical: 10,
    paddingHorizontal: 16,
  },
  cancelModalText: {
    color: "#94A3B8",
    fontSize: 14,
  },
  confirmModalBtn: {
    backgroundColor: colors.primary,
    paddingVertical: 10,
    paddingHorizontal: 20,
    borderRadius: 12,
  },
  confirmModalText: {
    color: "#FFF",
    fontWeight: "700",
    fontSize: 14,
  },
});
