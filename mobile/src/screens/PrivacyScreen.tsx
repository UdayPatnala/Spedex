import { MaterialIcons } from "@expo/vector-icons";
import React, { useState, useEffect } from "react";
import {
  ActivityIndicator,
  Alert,
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
import type { PrivacySettings, PrivacyGrievance } from "../types";
import { colors, radii, shadows, spacing } from "../theme/tokens";

export function PrivacyScreen() {
  const [settings, setSettings] = useState<PrivacySettings | null>(null);
  const [grievances, setGrievances] = useState<PrivacyGrievance[]>([]);
  const [loading, setLoading] = useState(true);
  const [statusMsg, setStatusMsg] = useState<string | null>(null);

  const [subject, setSubject] = useState("");
  const [description, setDescription] = useState("");
  const [submittingGrievance, setSubmittingGrievance] = useState(false);

  useEffect(() => {
    loadPrivacyData();
  }, []);

  const loadPrivacyData = async () => {
    setLoading(true);
    try {
      const [sett, grievs] = await Promise.all([
        spedexApi.getPrivacySettings(),
        spedexApi.getUserGrievances(),
      ]);
      setSettings(sett);
      setGrievances(grievs);
    } catch (err: any) {
      setStatusMsg(err.message || "Failed to load privacy settings");
    } finally {
      setLoading(false);
    }
  };

  const handleToggleConsent = async (type: "analytics" | "marketing", value: boolean) => {
    if (!settings) return;
    try {
      const res = await spedexApi.updatePrivacyConsent({
        [type === "analytics" ? "analytics_consent" : "marketing_consent"]: value,
        purpose: "User updated consent via mobile app",
        source: "Mobile_App_UI",
      });
      setSettings(res.settings);
      setStatusMsg("Consent preferences saved.");
    } catch (err: any) {
      setStatusMsg(err.message || "Failed to update consent");
    }
  };

  const handleExportData = async () => {
    try {
      await spedexApi.exportUserData();
      Alert.alert("Data Export", "Your data export has been prepared under DPDP Act 2023 Section 11.");
    } catch (err: any) {
      Alert.alert("Error", err.message || "Failed to export data");
    }
  };

  const handleErasure = () => {
    Alert.alert(
      "Request Data Erasure",
      "Are you sure you want to request data erasure under Section 12(3) of DPDP Act 2023? This will pseudonymize your account.",
      [
        { text: "Cancel", style: "cancel" },
        {
          text: "Confirm Erasure",
          style: "destructive",
          onPress: async () => {
            try {
              const res = await spedexApi.requestErasure("User initiated mobile erasure");
              Alert.alert("Success", res.message);
              loadPrivacyData();
            } catch (err: any) {
              Alert.alert("Error", err.message || "Failed to process erasure");
            }
          },
        },
      ]
    );
  };

  const handleSubmitGrievance = async () => {
    if (!subject.trim() || !description.trim()) {
      Alert.alert("Required", "Please enter both a subject and description.");
      return;
    }
    setSubmittingGrievance(true);
    try {
      const res = await spedexApi.submitGrievance({
        subject: subject.trim(),
        description: description.trim(),
      });
      setGrievances([res.grievance, ...grievances]);
      setSubject("");
      setDescription("");
      Alert.alert("Ticket Created", "Grievance ticket created. Our Data Protection Officer will review it.");
    } catch (err: any) {
      Alert.alert("Error", err.message || "Failed to submit grievance");
    } finally {
      setSubmittingGrievance(false);
    }
  };

  if (loading) {
    return (
      <SafeAreaView style={styles.safeArea}>
        <View style={styles.center}>
          <ActivityIndicator size="large" color={colors.primary} />
        </View>
      </SafeAreaView>
    );
  }

  return (
    <SafeAreaView style={styles.safeArea}>
      <ScrollView contentContainerStyle={styles.content} showsVerticalScrollIndicator={false}>
        <View style={styles.headerRow}>
          <View>
            <Text style={styles.brand}>DPDP Compliance</Text>
            <Text style={styles.subtitle}>Privacy and Data Protection Center</Text>
          </View>
          <MaterialIcons name="shield" size={28} color={colors.primary} />
        </View>

        {statusMsg ? (
          <View style={styles.statusBanner}>
            <Text style={styles.statusText}>{statusMsg}</Text>
          </View>
        ) : null}

        <View style={styles.panel}>
          <Text style={styles.sectionTitle}>Account Privacy Status</Text>
          <View style={styles.statusRow}>
            <Text style={styles.label}>Classification:</Text>
            <View style={[styles.badge, settings?.is_minor ? styles.badgeMinor : styles.badgeAdult]}>
              <Text style={styles.badgeText}>{settings?.is_minor ? "Minor (Under 18)" : "Adult (18+)"}</Text>
            </View>
          </View>
          <View style={styles.statusRow}>
            <Text style={styles.label}>Guardian Consent:</Text>
            <Text style={styles.valueText}>{settings?.guardian_consent_status || "NOT_REQUIRED"}</Text>
          </View>
          {settings?.is_minor ? (
            <View style={styles.minorNotice}>
              <Text style={styles.minorNoticeText}>
                Protection Active: Under DPDP Act Section 9, targeted marketing and behavioral tracking are permanently disabled for minors.
              </Text>
            </View>
          ) : null}
        </View>

        <View style={styles.panel}>
          <Text style={styles.sectionTitle}>Consent Preferences</Text>
          <View style={styles.settingRow}>
            <View style={styles.settingMeta}>
              <Text style={styles.settingLabel}>Analytics and Telemetry</Text>
              <Text style={styles.settingHint}>Help optimize trip recommendations</Text>
            </View>
            <Switch
              value={Boolean(settings?.analytics_consent)}
              disabled={Boolean(settings?.is_minor)}
              onValueChange={(val) => handleToggleConsent("analytics", val)}
              thumbColor={colors.surfaceLowest}
              trackColor={{ false: colors.surfaceHighest, true: colors.primary }}
            />
          </View>
          <View style={styles.settingRow}>
            <View style={styles.settingMeta}>
              <Text style={styles.settingLabel}>Product Updates</Text>
              <Text style={styles.settingHint}>Notifications about new wallet tools</Text>
            </View>
            <Switch
              value={Boolean(settings?.marketing_consent)}
              disabled={Boolean(settings?.is_minor)}
              onValueChange={(val) => handleToggleConsent("marketing", val)}
              thumbColor={colors.surfaceLowest}
              trackColor={{ false: colors.surfaceHighest, true: colors.primary }}
            />
          </View>
        </View>

        <View style={styles.panel}>
          <Text style={styles.sectionTitle}>Your Data Rights</Text>
          <Pressable style={styles.actionButton} onPress={handleExportData}>
            <MaterialIcons name="file-download" size={20} color={colors.surfaceLowest} />
            <Text style={styles.actionButtonText}>Export Personal Data (Portability)</Text>
          </Pressable>
          <Pressable style={[styles.actionButton, styles.dangerButton]} onPress={handleErasure}>
            <MaterialIcons name="delete-outline" size={20} color="#f43f5e" />
            <Text style={styles.dangerButtonText}>Request Account Erasure (Right to Forget)</Text>
          </Pressable>
        </View>

        <View style={styles.panel}>
          <Text style={styles.sectionTitle}>Lodge Grievance</Text>
          <TextInput
            style={styles.input}
            placeholder="Subject (e.g. Consent withdrawal)"
            placeholderTextColor={colors.onSurfaceVariant}
            value={subject}
            onChangeText={setSubject}
          />
          <TextInput
            style={[styles.input, styles.textArea]}
            placeholder="Describe your grievance in detail..."
            placeholderTextColor={colors.onSurfaceVariant}
            multiline
            numberOfLines={3}
            value={description}
            onChangeText={setDescription}
          />
          <Pressable
            style={[styles.actionButton, submittingGrievance ? styles.disabledButton : null]}
            onPress={handleSubmitGrievance}
            disabled={submittingGrievance}
          >
            <Text style={styles.actionButtonText}>
              {submittingGrievance ? "Submitting..." : "Submit Grievance to DPO"}
            </Text>
          </Pressable>
        </View>
      </ScrollView>
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
  center: {
    flex: 1,
    alignItems: "center",
    justifyContent: "center",
  },
  headerRow: {
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
  },
  brand: {
    color: colors.primary,
    fontSize: 22,
    fontWeight: "800",
  },
  subtitle: {
    color: colors.onSurfaceVariant,
    fontSize: 12,
  },
  statusBanner: {
    backgroundColor: "rgba(16, 185, 129, 0.15)",
    borderRadius: radii.md,
    padding: spacing.md,
    borderWidth: 1,
    borderColor: "rgba(16, 185, 129, 0.3)",
  },
  statusText: {
    color: "#10b981",
    fontSize: 13,
    fontWeight: "600",
  },
  panel: {
    backgroundColor: colors.surfaceLowest,
    borderRadius: 20,
    padding: spacing.lg,
    gap: spacing.md,
    ...shadows.card,
  },
  sectionTitle: {
    color: colors.onSurface,
    fontSize: 18,
    fontWeight: "800",
  },
  statusRow: {
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
  },
  label: {
    color: colors.onSurfaceVariant,
    fontSize: 14,
  },
  valueText: {
    color: colors.onSurface,
    fontSize: 14,
    fontWeight: "700",
  },
  badge: {
    paddingHorizontal: 10,
    paddingVertical: 4,
    borderRadius: radii.pill,
  },
  badgeAdult: {
    backgroundColor: "rgba(16, 185, 129, 0.2)",
  },
  badgeMinor: {
    backgroundColor: "rgba(245, 158, 11, 0.2)",
  },
  badgeText: {
    color: colors.onSurface,
    fontWeight: "700",
    fontSize: 12,
  },
  minorNotice: {
    backgroundColor: "rgba(245, 158, 11, 0.1)",
    padding: spacing.md,
    borderRadius: 12,
    marginTop: 4,
  },
  minorNoticeText: {
    color: "#f59e0b",
    fontSize: 12,
    lineHeight: 16,
  },
  settingRow: {
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "space-between",
  },
  settingMeta: {
    flex: 1,
  },
  settingLabel: {
    color: colors.onSurface,
    fontWeight: "700",
    fontSize: 15,
  },
  settingHint: {
    color: colors.onSurfaceVariant,
    fontSize: 12,
  },
  actionButton: {
    backgroundColor: colors.primary,
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "center",
    gap: 8,
    paddingVertical: 14,
    borderRadius: radii.md,
  },
  actionButtonText: {
    color: colors.surfaceLowest,
    fontWeight: "700",
    fontSize: 14,
  },
  dangerButton: {
    backgroundColor: "rgba(244, 63, 94, 0.1)",
    borderWidth: 1,
    borderColor: "rgba(244, 63, 94, 0.3)",
  },
  dangerButtonText: {
    color: "#f43f5e",
    fontWeight: "700",
    fontSize: 14,
  },
  disabledButton: {
    opacity: 0.6,
  },
  input: {
    backgroundColor: colors.surfaceLow,
    borderRadius: 10,
    paddingHorizontal: 12,
    paddingVertical: 10,
    color: colors.onSurface,
    fontSize: 14,
  },
  textArea: {
    minHeight: 70,
    textAlignVertical: "top",
  },
});
