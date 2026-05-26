import { MaterialIcons } from "@expo/vector-icons";
import { useEffect, useState } from "react";
import {
  Alert,
  Modal,
  Pressable,
  SafeAreaView,
  ScrollView,
  StyleSheet,
  Switch,
  Text,
  TextInput,
  View,
} from "react-native";

import { spedexApi } from "../api/client";
import { formatCurrency } from "../theme/helpers";
import { colors, radii, shadows, spacing } from "../theme/tokens";
import type { Reminder, RemindersScreenData } from "../types";

const emptyForm = {
  title: "",
  subtitle: "",
  upiHandle: "",
  amount: "",
  dueDate: "",
  autopayEnabled: false,
};

export function RemindersScreen({ navigation }: any) {
  const [data, setData] = useState<RemindersScreenData | null>(null);
  const [isEditorVisible, setIsEditorVisible] = useState(false);
  const [editingReminder, setEditingReminder] = useState<Reminder | null>(null);
  const [title, setTitle] = useState(emptyForm.title);
  const [subtitle, setSubtitle] = useState(emptyForm.subtitle);
  const [upiHandle, setUpiHandle] = useState(emptyForm.upiHandle);
  const [amount, setAmount] = useState(emptyForm.amount);
  const [dueDate, setDueDate] = useState(emptyForm.dueDate);
  const [autopayEnabled, setAutopayEnabled] = useState(emptyForm.autopayEnabled);

  const reload = () => {
    spedexApi.getReminders().then(setData).catch(console.error);
  };

  useEffect(() => {
    reload();
  }, []);

  const closeEditor = () => {
    setIsEditorVisible(false);
    setEditingReminder(null);
    setTitle(emptyForm.title);
    setSubtitle(emptyForm.subtitle);
    setUpiHandle(emptyForm.upiHandle);
    setAmount(emptyForm.amount);
    setDueDate(emptyForm.dueDate);
    setAutopayEnabled(emptyForm.autopayEnabled);
  };

  const openCreate = () => {
    closeEditor();
    setIsEditorVisible(true);
  };

  const openEdit = (reminder: Reminder) => {
    setEditingReminder(reminder);
    setTitle(reminder.title);
    setSubtitle(reminder.subtitle ?? "");
    setUpiHandle(reminder.upi_handle ?? "");
    setAmount(String(reminder.amount ?? ""));
    setDueDate(reminder.due_date ? reminder.due_date.slice(0, 10) : "");
    setAutopayEnabled(Boolean(reminder.autopay_enabled));
    setIsEditorVisible(true);
  };

  const saveReminder = async () => {
    if (!title.trim() || !amount.trim() || !dueDate.trim()) {
      Alert.alert("Missing details", "Title, amount, and due date are required.");
      return;
    }

    const payload = {
      title: title.trim(),
      subtitle: subtitle.trim(),
      upi_handle: upiHandle.trim(),
      amount: parseFloat(amount),
      due_date: dueDate,
      autopay_enabled: autopayEnabled,
      status: editingReminder?.status ?? "scheduled",
    };

    try {
      if (editingReminder) {
        await spedexApi.updateReminder(editingReminder.id, payload);
      } else {
        await spedexApi.addReminder(payload);
      }
      closeEditor();
      reload();
    } catch {
      Alert.alert("Unable to save reminder", "Please check the values and try again.");
    }
  };

  const togglePaid = async (reminder: Reminder) => {
    const nextStatus = reminder.status === "paid" ? "scheduled" : "paid";
    try {
      await spedexApi.updateReminder(reminder.id, { status: nextStatus });
      reload();
    } catch {
      Alert.alert("Unable to update reminder", "Please try again.");
    }
  };

  const toggleAutopay = async (reminder: Reminder) => {
    try {
      await spedexApi.updateReminder(reminder.id, {
        autopay_enabled: !reminder.autopay_enabled,
      });
      reload();
    } catch {
      Alert.alert("Unable to update autopay", "Please try again.");
    }
  };

  const deleteReminder = async (reminder: Reminder) => {
    try {
      await spedexApi.deleteReminder(reminder.id);
      reload();
    } catch {
      Alert.alert("Unable to delete reminder", "Please try again.");
    }
  };

  const payReminder = (reminder: Reminder) => {
    navigation.getParent()?.navigate("PaymentConfirm", {
      amount: reminder.amount,
      payeeName: reminder.title,
      upiHandle: reminder.upi_handle || "bills@upi",
      category: "Bills",
      icon: "bolt",
    });
  };

  if (!data) return <SafeAreaView style={styles.safeArea} />;

  return (
    <SafeAreaView style={styles.safeArea}>
      <ScrollView contentContainerStyle={styles.content} showsVerticalScrollIndicator={false}>
        <View style={styles.header}>
          <View>
            <Text style={styles.brand}>Spedex</Text>
            <Text style={styles.eyebrow}>Recurring Payments</Text>
          </View>
          <Pressable style={styles.addButton} onPress={openCreate}>
            <MaterialIcons name="notification-add" size={18} color={colors.surfaceLowest} />
            <Text style={styles.addButtonText}>New Reminder</Text>
          </Pressable>
        </View>

        <View style={styles.heroCard}>
          <Text style={styles.heroTitle}>Stay ahead of every due date</Text>
          <Text style={styles.heroBody}>{data.next_due_message}</Text>
          <View style={styles.metricRow}>
            <View style={styles.metricCard}>
              <Text style={styles.metricLabel}>Scheduled</Text>
              <Text style={styles.metricValue}>{data.scheduled_count}</Text>
            </View>
            <View style={styles.metricCard}>
              <Text style={styles.metricLabel}>Autopay Live</Text>
              <Text style={styles.metricValue}>{data.autopay_enabled_count}</Text>
            </View>
          </View>
        </View>

        <View style={styles.listSection}>
          {data.reminders.length === 0 ? (
            <View style={styles.emptyState}>
              <MaterialIcons name="notifications-none" size={48} color={colors.onSurfaceVariant} />
              <Text style={styles.emptyTitle}>No reminders yet</Text>
              <Text style={styles.emptyBody}>Create reminders for rent, subscriptions, and split bills.</Text>
            </View>
          ) : (
            data.reminders.map((reminder) => {
              const isPaid = reminder.status === "paid";
              return (
                <View key={reminder.id} style={styles.reminderCard}>
                  <View style={styles.reminderTop}>
                    <View style={styles.dateBadge}>
                      <Text style={styles.dateMonth}>
                        {new Date(reminder.due_date).toLocaleString("en-US", { month: "short" }).toUpperCase()}
                      </Text>
                      <Text style={styles.dateDay}>{new Date(reminder.due_date).getDate()}</Text>
                    </View>
                    <View style={styles.reminderMeta}>
                      <Text style={styles.reminderTitle}>{reminder.title}</Text>
                      <Text style={styles.reminderSubtitle}>
                        {reminder.subtitle || reminder.upi_handle || "Spedex payment reminder"}
                      </Text>
                      <Text style={styles.reminderAmount}>{formatCurrency(reminder.amount)}</Text>
                    </View>
                    <Pressable onPress={() => openEdit(reminder)} hitSlop={10}>
                      <MaterialIcons name="edit" size={18} color={colors.onSurfaceVariant} />
                    </Pressable>
                  </View>

                  <View style={styles.statusRow}>
                    <View style={[styles.statusBadge, isPaid ? styles.statusPaid : styles.statusScheduled]}>
                      <Text style={[styles.statusText, isPaid ? styles.statusTextPaid : styles.statusTextScheduled]}>
                        {isPaid ? "Paid" : "Scheduled"}
                      </Text>
                    </View>
                    <View style={styles.inlineSwitch}>
                      <Text style={styles.inlineLabel}>Autopay</Text>
                      <Switch
                        value={reminder.autopay_enabled}
                        onValueChange={() => toggleAutopay(reminder)}
                        thumbColor={colors.surfaceLowest}
                        trackColor={{ false: colors.surfaceHighest, true: colors.primary }}
                      />
                    </View>
                  </View>

                  <View style={styles.actionRow}>
                    <Pressable style={styles.secondaryAction} onPress={() => togglePaid(reminder)}>
                      <MaterialIcons name={isPaid ? "restart-alt" : "check-circle"} size={18} color={colors.primary} />
                      <Text style={styles.secondaryActionText}>{isPaid ? "Reopen" : "Mark Paid"}</Text>
                    </Pressable>
                    <Pressable style={styles.secondaryAction} onPress={() => deleteReminder(reminder)}>
                      <MaterialIcons name="delete-outline" size={18} color={colors.error} />
                      <Text style={[styles.secondaryActionText, { color: colors.error }]}>Delete</Text>
                    </Pressable>
                    {!isPaid ? (
                      <Pressable style={styles.primaryAction} onPress={() => payReminder(reminder)}>
                        <MaterialIcons name="payments" size={18} color={colors.surfaceLowest} />
                        <Text style={styles.primaryActionText}>Pay Now</Text>
                      </Pressable>
                    ) : null}
                  </View>
                </View>
              );
            })
          )}
        </View>
      </ScrollView>

      <Modal visible={isEditorVisible} animationType="slide" transparent>
        <View style={styles.modalOverlay}>
          <View style={styles.sheetCard}>
            <View style={styles.modalHeader}>
              <Text style={styles.modalTitle}>{editingReminder ? "Edit Reminder" : "Create Reminder"}</Text>
              <Pressable onPress={closeEditor}>
                <MaterialIcons name="close" size={22} color={colors.onSurface} />
              </Pressable>
            </View>

            <TextInput
              style={styles.modalInput}
              placeholder="Reminder title"
              placeholderTextColor={colors.onSurfaceVariant}
              value={title}
              onChangeText={setTitle}
            />
            <TextInput
              style={styles.modalInput}
              placeholder="Short note or bill name"
              placeholderTextColor={colors.onSurfaceVariant}
              value={subtitle}
              onChangeText={setSubtitle}
            />
            <TextInput
              style={styles.modalInput}
              placeholder="UPI handle for Pay Now"
              placeholderTextColor={colors.onSurfaceVariant}
              value={upiHandle}
              onChangeText={setUpiHandle}
              autoCapitalize="none"
            />
            <View style={styles.dualRow}>
              <TextInput
                style={[styles.modalInput, styles.dualInput]}
                placeholder="Amount"
                placeholderTextColor={colors.onSurfaceVariant}
                value={amount}
                onChangeText={setAmount}
                keyboardType="numeric"
              />
              <TextInput
                style={[styles.modalInput, styles.dualInput]}
                placeholder="YYYY-MM-DD"
                placeholderTextColor={colors.onSurfaceVariant}
                value={dueDate}
                onChangeText={setDueDate}
                autoCapitalize="none"
              />
            </View>
            <View style={styles.switchRow}>
              <Text style={styles.inlineLabel}>Enable autopay</Text>
              <Switch
                value={autopayEnabled}
                onValueChange={setAutopayEnabled}
                thumbColor={colors.surfaceLowest}
                trackColor={{ false: colors.surfaceHighest, true: colors.primary }}
              />
            </View>
            <Pressable style={styles.primaryAction} onPress={saveReminder}>
              <MaterialIcons name="save" size={18} color={colors.surfaceLowest} />
              <Text style={styles.primaryActionText}>{editingReminder ? "Save Changes" : "Save Reminder"}</Text>
            </Pressable>
          </View>
        </View>
      </Modal>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  safeArea: {
    flex: 1,
    backgroundColor: colors.background,
  },
  content: {
    paddingHorizontal: spacing.xl,
    paddingTop: spacing.md,
    paddingBottom: 124,
    gap: spacing.lg,
  },
  header: {
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
    gap: spacing.md,
  },
  brand: {
    color: colors.primary,
    fontSize: 26,
    fontWeight: "800",
  },
  eyebrow: {
    color: colors.onSurfaceVariant,
    textTransform: "uppercase",
    letterSpacing: 1.8,
    fontSize: 11,
    marginTop: 6,
  },
  addButton: {
    flexDirection: "row",
    alignItems: "center",
    gap: 6,
    backgroundColor: colors.primary,
    borderRadius: radii.md,
    paddingHorizontal: 16,
    paddingVertical: 14,
  },
  addButtonText: {
    color: colors.surfaceLowest,
    fontWeight: "800",
  },
  heroCard: {
    backgroundColor: colors.surfaceLowest,
    borderRadius: 24,
    padding: spacing.xl,
    gap: spacing.md,
    ...shadows.card,
  },
  heroTitle: {
    color: colors.onSurface,
    fontSize: 28,
    fontWeight: "800",
  },
  heroBody: {
    color: colors.onSurfaceVariant,
    lineHeight: 21,
  },
  metricRow: {
    flexDirection: "row",
    gap: spacing.md,
  },
  metricCard: {
    flex: 1,
    backgroundColor: colors.surfaceLow,
    borderRadius: 18,
    padding: spacing.md,
  },
  metricLabel: {
    color: colors.onSurfaceVariant,
    textTransform: "uppercase",
    fontSize: 11,
    letterSpacing: 1.5,
    fontWeight: "700",
  },
  metricValue: {
    marginTop: 8,
    color: colors.primary,
    fontSize: 28,
    fontWeight: "900",
  },
  listSection: {
    gap: spacing.md,
  },
  emptyState: {
    backgroundColor: colors.surfaceLowest,
    borderRadius: 24,
    padding: spacing.xl,
    alignItems: "center",
    gap: spacing.sm,
    ...shadows.card,
  },
  emptyTitle: {
    color: colors.onSurface,
    fontSize: 20,
    fontWeight: "800",
  },
  emptyBody: {
    color: colors.onSurfaceVariant,
    textAlign: "center",
    lineHeight: 20,
  },
  reminderCard: {
    backgroundColor: colors.surfaceLowest,
    borderRadius: 24,
    padding: spacing.lg,
    gap: spacing.md,
    ...shadows.card,
  },
  reminderTop: {
    flexDirection: "row",
    gap: spacing.md,
    alignItems: "flex-start",
  },
  dateBadge: {
    width: 60,
    borderRadius: 18,
    backgroundColor: colors.surfaceLow,
    paddingVertical: 8,
    alignItems: "center",
  },
  dateMonth: {
    color: colors.primary,
    fontSize: 10,
    fontWeight: "800",
    letterSpacing: 1.5,
  },
  dateDay: {
    color: colors.onSurface,
    fontSize: 26,
    fontWeight: "900",
  },
  reminderMeta: {
    flex: 1,
    gap: 4,
  },
  reminderTitle: {
    color: colors.onSurface,
    fontSize: 18,
    fontWeight: "800",
  },
  reminderSubtitle: {
    color: colors.onSurfaceVariant,
    lineHeight: 20,
  },
  reminderAmount: {
    marginTop: 4,
    color: colors.primary,
    fontSize: 22,
    fontWeight: "900",
  },
  statusRow: {
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
    gap: spacing.md,
  },
  statusBadge: {
    borderRadius: radii.pill,
    paddingHorizontal: 12,
    paddingVertical: 8,
  },
  statusScheduled: {
    backgroundColor: colors.surfaceLow,
  },
  statusPaid: {
    backgroundColor: "#dff6ea",
  },
  statusText: {
    fontSize: 11,
    fontWeight: "800",
    textTransform: "uppercase",
    letterSpacing: 1.3,
  },
  statusTextScheduled: {
    color: colors.primary,
  },
  statusTextPaid: {
    color: colors.secondary,
  },
  inlineSwitch: {
    flexDirection: "row",
    alignItems: "center",
    gap: spacing.sm,
  },
  inlineLabel: {
    color: colors.onSurface,
    fontWeight: "700",
  },
  actionRow: {
    flexDirection: "row",
    flexWrap: "wrap",
    gap: spacing.sm,
  },
  secondaryAction: {
    flexDirection: "row",
    alignItems: "center",
    gap: 6,
    backgroundColor: colors.surfaceLow,
    borderRadius: radii.md,
    paddingHorizontal: 14,
    paddingVertical: 12,
  },
  secondaryActionText: {
    color: colors.primary,
    fontWeight: "700",
  },
  primaryAction: {
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "center",
    gap: 6,
    backgroundColor: colors.primary,
    borderRadius: radii.md,
    paddingHorizontal: 16,
    paddingVertical: 12,
  },
  primaryActionText: {
    color: colors.surfaceLowest,
    fontWeight: "800",
  },
  modalOverlay: {
    flex: 1,
    justifyContent: "flex-end",
    backgroundColor: "rgba(16, 24, 40, 0.45)",
    padding: spacing.lg,
  },
  sheetCard: {
    backgroundColor: colors.surfaceLowest,
    borderRadius: radii.xl,
    padding: spacing.xl,
    gap: spacing.md,
    ...shadows.card,
  },
  modalHeader: {
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "space-between",
  },
  modalTitle: {
    color: colors.onSurface,
    fontSize: 22,
    fontWeight: "800",
  },
  modalInput: {
    backgroundColor: colors.surfaceLow,
    color: colors.onSurface,
    borderRadius: radii.md,
    paddingHorizontal: spacing.md,
    paddingVertical: spacing.md,
    fontSize: 16,
  },
  dualRow: {
    flexDirection: "row",
    gap: spacing.md,
  },
  dualInput: {
    flex: 1,
  },
  switchRow: {
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
  },
});
