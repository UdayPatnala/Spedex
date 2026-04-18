import { MaterialIcons } from "@expo/vector-icons";
import { CameraView, useCameraPermissions } from "expo-camera";
import * as ImagePicker from "expo-image-picker";
import jsQR from "jsqr";
import { PNG } from "pngjs/browser";
import { Buffer } from "buffer";
import { startTransition, useDeferredValue, useEffect, useMemo, useState } from "react";
import {
  Alert,
  Modal,
  Pressable,
  SafeAreaView,
  ScrollView,
  StyleSheet,
  Text,
  TextInput,
  View,
  Linking,
} from "react-native";

import { spedexApi } from "../api/client";
import { accentPalette, formatCurrency, iconFor } from "../theme/helpers";
import { colors, radii, shadows, spacing } from "../theme/tokens";
import type { Vendor, VendorDirectoryData } from "../types";

export function PaymentsScreen({ navigation }: any) {
  const [data, setData] = useState<VendorDirectoryData | null>(null);
  const [query, setQuery] = useState("");
  const deferredQuery = useDeferredValue(query);
  const [permission, requestPermission] = useCameraPermissions();
  const [isScannerVisible, setIsScannerVisible] = useState(false);
  const [isOptionsVisible, setIsOptionsVisible] = useState(false);
  const [isManualVisible, setIsManualVisible] = useState(false);
  const [isEditVisible, setIsEditVisible] = useState(false);

  const [editingVendorId, setEditingVendorId] = useState<number | null>(null);
  const [vendorName, setVendorName] = useState("");
  const [vendorUpi, setVendorUpi] = useState("");
  const [vendorAmount, setVendorAmount] = useState("");
  const [vendorPhone, setVendorPhone] = useState("");
  const [amountStep, setAmountStep] = useState(false);

  useEffect(() => {
    spedexApi.getVendorDirectory().then(setData).catch(console.error);
  }, []);

  const openScanner = async () => {
    if (!permission?.granted) {
      const { granted } = await requestPermission();
      if (!granted) {
        Alert.alert("Permission required", "Camera access is needed to scan QR codes.");
        return;
      }
    }
    setIsOptionsVisible(false);
    setIsScannerVisible(true);
  };

  const openImagePicker = async () => {
    setIsOptionsVisible(false);
    const result = await ImagePicker.launchImageLibraryAsync({
      mediaTypes: ImagePicker.MediaTypeOptions.Images,
      allowsEditing: false,
      quality: 1,
      base64: true
    });

    if (!result.canceled && result.assets && result.assets.length > 0 && result.assets[0].base64) {
      try {
        // We will try our best to decode standard PNG/JPEGs
        // This is a naive polyfill, in production a native module like react-native-vision-camera is required
        const b64 = result.assets[0].base64;
        const decoded = Buffer.from(b64, 'base64');

        try {
            // PNG Decoding
            const png = new PNG();
            png.parse(decoded, (error: any, parsed: any) => {
              if (error) throw error;
              const code = jsQR(parsed.data, parsed.width, parsed.height);
              if (code) {
                  handleQRData(code.data);
              } else {
                  Alert.alert("Scan Failed", "No QR code found in PNG image.");
              }
            });
        } catch (e) {
            // If it's a JPEG or decoding fails, fallback
            Alert.alert("Feature Limited", "Native JPEG/PNG decoding not fully available in this sandbox environment without native modules. Please use Camera scan.");
        }
      } catch(e) {
         Alert.alert("Error", "Could not process image.");
      }
    }
  };

  const handleQRData = async (qrData: string) => {
    setIsScannerVisible(false);
    
    if (qrData.startsWith("upi://pay")) {
      const url = new URL(qrData.replace("upi://pay", "http://fake.com")); // Trick URL parser
      const pa = url.searchParams.get("pa");
      const pn = url.searchParams.get("pn");
      const am = url.searchParams.get("am");

      if (pa) {
        // Prepare state for Amount Step
        setVendorName(pn || "Unknown Merchant");
        setVendorUpi(pa);
        if (am) {
            setVendorAmount(am);
        } else {
            setVendorAmount("");
        }
        setAmountStep(true);
        setIsManualVisible(true);
        return;
      }
    }
    
    Alert.alert("Invalid QR", "This does not appear to be a valid payment QR code.");
  };

  const handleBarCodeScanned = ({ data: qrData }: { data: string }) => {
    handleQRData(qrData);
  };

  const submitManualVendor = async () => {
    if (!vendorName.trim()) {
      Alert.alert("Error", "Please enter a name.");
      return;
    }
    if (!vendorPhone.trim() && !vendorUpi.trim()) {
      Alert.alert("Error", "Please enter a phone number or UPI ID.");
      return;
    }
    setAmountStep(true);
  };

  const saveVendor = async () => {
    try {
        const upi = vendorUpi ? vendorUpi : (vendorPhone.includes('@') ? vendorPhone : `${vendorPhone}@upi`);
        let res;

        if (editingVendorId) {
            res = await spedexApi.editVendor(editingVendorId, {
                name: vendorName,
                upi_handle: upi,
                default_amount: parseFloat(vendorAmount) || 0
            });
        } else {
            res = await spedexApi.addVendor({
                name: vendorName,
                upi_handle: upi,
                default_amount: parseFloat(vendorAmount) || 0
            });
        }

        if (res && res.status === "success") {
            Alert.alert("Success", editingVendorId ? "Vendor updated!" : "Vendor saved!");
            setIsManualVisible(false);
            setIsEditVisible(false);
            setAmountStep(false);
            setVendorName("");
            setVendorUpi("");
            setVendorPhone("");
            setVendorAmount("");
            setEditingVendorId(null);
            spedexApi.getVendorDirectory().then(setData).catch(console.error);
        }
    } catch (e) {
        Alert.alert("Error", "Could not save vendor.");
    }
  };

  const openEdit = (vendor: Vendor) => {
    setEditingVendorId(vendor.id);
    setVendorName(vendor.name);
    setVendorUpi(vendor.upi_handle);
    setVendorAmount(vendor.default_amount.toString());
    setIsEditVisible(true);
  };


  if (!data) return <SafeAreaView style={styles.safeArea} />;

  const groups = useMemo<Record<string, Vendor[]>>(() => {
    const lowered = deferredQuery.trim().toLowerCase();
    if (!lowered) {
      return data.groups;
    }
    return Object.fromEntries(
      Object.entries(data.groups)
        .map(([groupName, vendors]) => [
          groupName,
          vendors.filter(
            (vendor) =>
              vendor.name.toLowerCase().includes(lowered) ||
              vendor.category.toLowerCase().includes(lowered),
          ),
        ])
        .filter(([, vendors]) => vendors.length > 0),
    ) as Record<string, Vendor[]>;
  }, [data.groups, deferredQuery]);

  return (
    <SafeAreaView style={styles.safeArea}>
      <ScrollView contentContainerStyle={styles.content} showsVerticalScrollIndicator={false}>
        <View style={styles.header}>
          <View style={styles.avatar}>
            <Text style={styles.avatarText}>{data.user.avatar_initials}</Text>
          </View>
          <Text style={styles.brand}>Spedex</Text>
          <View style={styles.headerSpacer} />
          <MaterialIcons name="notifications" size={24} color={colors.primary} />
        </View>

        <View style={styles.titleRow}>
          <View>
            <Text style={styles.eyebrow}>Manage Directory</Text>
            <Text style={styles.title}>Vendors</Text>
          </View>
          <Pressable style={styles.addButton} onPress={() => setIsOptionsVisible(true)}>
            <MaterialIcons name="add" size={18} color={colors.surfaceLowest} />
            <Text style={styles.addLabel}>Add New Vendor</Text>
          </Pressable>
        </View>

        <View style={styles.searchRow}>
          <View style={styles.searchWrap}>
            <MaterialIcons name="search" size={20} color={colors.onSurfaceVariant} />
            <TextInput
              placeholder="Search by name or category..."
              placeholderTextColor={colors.onSurfaceVariant}
              value={query}
              onChangeText={(value) => startTransition(() => setQuery(value))}
              style={styles.searchInput}
            />
          </View>
          <Pressable style={styles.scanButton} onPress={openScanner}>
            <MaterialIcons name="qr-code-scanner" size={24} color={colors.primary} />
          </Pressable>
        </View>

        <Modal visible={isScannerVisible} animationType="slide">
          <SafeAreaView style={styles.scannerContainer}>
            <View style={styles.scannerHeader}>
              <Text style={styles.scannerTitle}>Scan Payment QR</Text>
              <Pressable onPress={() => setIsScannerVisible(false)} style={styles.closeButton}>
                <MaterialIcons name="close" size={28} color={colors.onSurface} />
              </Pressable>
            </View>
            <CameraView
              style={styles.camera}
              onBarcodeScanned={handleBarCodeScanned}
              barcodeScannerSettings={{ barcodeTypes: ["qr"] }}
            />
            <View style={styles.scannerOverlay}>
              <View style={styles.scanFrame} />
              <Text style={styles.scanHint}>Point at a UPI or Spedex QR code</Text>
            </View>
          </SafeAreaView>
        </Modal>

        {/* Options Modal */}
        <Modal visible={isOptionsVisible} animationType="fade" transparent={true}>
          <View style={styles.modalOverlay}>
            <View style={styles.optionsContent}>
              <Text style={styles.modalTitle}>Add Vendor</Text>
              <Pressable style={styles.optionButton} onPress={openScanner}>
                <MaterialIcons name="camera-alt" size={24} color={colors.primary} />
                <Text style={styles.optionText}>Scan QR Code</Text>
              </Pressable>
              <Pressable style={styles.optionButton} onPress={openImagePicker}>
                <MaterialIcons name="image" size={24} color={colors.primary} />
                <Text style={styles.optionText}>Upload QR from Gallery</Text>
              </Pressable>
              <Pressable style={styles.optionButton} onPress={() => { setIsOptionsVisible(false); setIsManualVisible(true); }}>
                <MaterialIcons name="keyboard" size={24} color={colors.primary} />
                <Text style={styles.optionText}>Enter Manually</Text>
              </Pressable>
              <Pressable style={[styles.optionButton, { marginTop: 16 }]} onPress={() => setIsOptionsVisible(false)}>
                <Text style={[styles.optionText, { color: colors.error, textAlign: 'center', width: '100%' }]}>Cancel</Text>
              </Pressable>
            </View>
          </View>
        </Modal>

        {/* Manual Add / Amount Step Modal */}
        <Modal visible={isManualVisible} animationType="slide" transparent={true}>
          <View style={styles.modalOverlay}>
            <View style={styles.modalContent}>
              <View style={styles.modalHeader}>
                <Text style={styles.modalTitle}>{amountStep ? "Default Amount" : "Vendor Details"}</Text>
                <Pressable onPress={() => { setIsManualVisible(false); setAmountStep(false); }}>
                  <MaterialIcons name="close" size={24} color={colors.onSurface} />
                </Pressable>
              </View>

              {!amountStep ? (
                  <>
                      <TextInput
                        style={styles.modalInput}
                        placeholder="Vendor Name"
                        value={vendorName}
                        onChangeText={setVendorName}
                        placeholderTextColor={colors.onSurfaceVariant}
                      />
                      <TextInput
                        style={styles.modalInput}
                        placeholder="Phone Number or UPI ID"
                        value={vendorUpi || vendorPhone}
                        onChangeText={(t) => {
                            if (t.includes('@')) {
                                setVendorUpi(t);
                                setVendorPhone("");
                            } else {
                                setVendorPhone(t);
                                setVendorUpi("");
                            }
                        }}
                        placeholderTextColor={colors.onSurfaceVariant}
                      />
                      <Pressable style={styles.modalSubmitButton} onPress={submitManualVendor}>
                        <Text style={styles.modalSubmitText}>Next</Text>
                      </Pressable>
                  </>
              ) : (
                  <>
                      <TextInput
                        style={styles.modalInput}
                        placeholder="Default Amount (Optional)"
                        value={vendorAmount}
                        onChangeText={setVendorAmount}
                        keyboardType="numeric"
                        placeholderTextColor={colors.onSurfaceVariant}
                      />
                      <Pressable style={styles.modalSubmitButton} onPress={saveVendor}>
                        <Text style={styles.modalSubmitText}>Save Vendor</Text>
                      </Pressable>
                  </>
              )}
            </View>
          </View>
        </Modal>

        {/* Edit Vendor Modal */}
        <Modal visible={isEditVisible} animationType="slide" transparent={true}>
          <View style={styles.modalOverlay}>
            <View style={styles.modalContent}>
              <View style={styles.modalHeader}>
                <Text style={styles.modalTitle}>Edit Vendor</Text>
                <Pressable onPress={() => setIsEditVisible(false)}>
                  <MaterialIcons name="close" size={24} color={colors.onSurface} />
                </Pressable>
              </View>
              <TextInput
                style={styles.modalInput}
                placeholder="Vendor Name"
                value={vendorName}
                onChangeText={setVendorName}
                placeholderTextColor={colors.onSurfaceVariant}
              />
              <TextInput
                style={styles.modalInput}
                placeholder="UPI ID"
                value={vendorUpi}
                onChangeText={setVendorUpi}
                placeholderTextColor={colors.onSurfaceVariant}
              />
              <TextInput
                style={styles.modalInput}
                placeholder="Default Amount"
                value={vendorAmount}
                onChangeText={setVendorAmount}
                keyboardType="numeric"
                placeholderTextColor={colors.onSurfaceVariant}
              />
              <Pressable style={styles.modalSubmitButton} onPress={saveVendor}>
                <Text style={styles.modalSubmitText}>Save Changes</Text>
              </Pressable>
            </View>
          </View>
        </Modal>

        {Object.keys(groups).length === 0 ? (
          <View style={{ padding: 32, alignItems: "center", marginTop: 24 }}>
            <MaterialIcons name="folder-open" size={64} color={colors.onSurfaceVariant} style={{ opacity: 0.3 }} />
            <Text style={{ marginTop: 16, fontSize: 18, fontWeight: "600", color: colors.onSurface }}>Directory is empty</Text>
            <Text style={{ marginTop: 8, color: colors.onSurfaceVariant, textAlign: "center" }}>Tap 'Add New Vendor' to create your first payee.</Text>
          </View>
        ) : (
          Object.entries(groups).map(([groupName, vendors]) => (
            <View key={groupName} style={styles.groupSection}>
              <View style={styles.groupHeader}>
                <View style={styles.groupLine} />
                <Text style={styles.groupTitle}>{groupName}</Text>
                <View style={styles.groupLine} />
              </View>
              {vendors.map((vendor) => {
                const palette = accentPalette(vendor.accent);
                return (
                  <Pressable
                    key={vendor.id}
                    style={styles.vendorCard}
                    onPress={() => navigation.getParent()?.navigate("PaymentConfirm", { vendor, amount: vendor.default_amount })}
                  >
                    <View style={styles.vendorInfo}>
                      <View style={[styles.vendorIcon, { backgroundColor: palette.bg }]}>
                        <MaterialIcons name={iconFor(vendor.icon) as any} size={26} color={palette.text} />
                      </View>
                      <View>
                        <Text style={styles.vendorName}>{vendor.name}</Text>
                        <Text style={[styles.vendorChip, { color: palette.text, backgroundColor: `${palette.bg}AA` }]}>
                          {vendor.category}
                        </Text>
                      </View>
                    </View>
                    <View style={styles.vendorMeta}>
                      <Text style={styles.vendorAmount}>{formatCurrency(vendor.default_amount)}</Text>
                      <Pressable onPress={(e) => { e.stopPropagation(); openEdit(vendor); }} hitSlop={10}><MaterialIcons name="edit" size={16} color={colors.onSurfaceVariant} /></Pressable>
                    </View>
                  </Pressable>
                );
              })}
            </View>
          ))
        )}
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
  header: {
    flexDirection: "row",
    alignItems: "center",
  },
  avatar: {
    width: 40,
    height: 40,
    borderRadius: 20,
    backgroundColor: colors.surfaceContainer,
    alignItems: "center",
    justifyContent: "center",
  },
  avatarText: {
    color: colors.primary,
    fontWeight: "800",
  },
  brand: {
    marginLeft: spacing.sm,
    color: colors.primary,
    fontWeight: "800",
    fontSize: 28,
  },
  headerSpacer: {
    flex: 1,
  },
  titleRow: {
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "flex-end",
    gap: spacing.md,
  },
  eyebrow: {
    color: colors.onSurfaceVariant,
    textTransform: "uppercase",
    letterSpacing: 2.1,
    fontSize: 11,
    marginBottom: 6,
  },
  title: {
    color: colors.onSurface,
    fontSize: 40,
    fontWeight: "800",
  },
  addButton: {
    flexDirection: "row",
    alignItems: "center",
    gap: 6,
    borderRadius: radii.md,
    paddingHorizontal: 16,
    paddingVertical: 14,
    backgroundColor: colors.primary,
    ...shadows.card,
  },
  addLabel: {
    color: colors.surfaceLowest,
    fontWeight: "700",
  },
  searchRow: {
    flexDirection: "row",
    gap: spacing.md,
    alignItems: "center",
  },
  scanButton: {
    width: 56,
    height: 56,
    borderRadius: radii.md,
    backgroundColor: colors.surfaceLow,
    alignItems: "center",
    justifyContent: "center",
    ...shadows.card,
  },
  searchWrap: {
    flexDirection: "row",
    alignItems: "center",
    gap: spacing.sm,
    backgroundColor: colors.surfaceLow,
    borderRadius: radii.md,
    paddingHorizontal: spacing.md,
    flex: 1,
    height: 56,
  },
  searchInput: {
    flex: 1,
    color: colors.onSurface,
    fontSize: 15,
  },
  scannerContainer: {
    flex: 1,
    backgroundColor: colors.background,
  },
  scannerHeader: {
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "space-between",
    padding: spacing.xl,
  },
  scannerTitle: {
    fontSize: 24,
    fontWeight: "800",
    color: colors.onSurface,
  },
  closeButton: {
    padding: 4,
  },
  camera: {
    flex: 1,
  },
  scannerOverlay: {
    position: "absolute",
    top: 150,
    left: 0,
    right: 0,
    bottom: 0,
    alignItems: "center",
    gap: spacing.xl,
  },
  scanFrame: {
    width: 250,
    height: 250,
    borderWidth: 2,
    borderColor: colors.primary,
    borderRadius: 24,
    backgroundColor: "rgba(0,0,0,0.1)",
  },
  scanHint: {
    color: colors.onSurface,
    backgroundColor: colors.surfaceLow,
    paddingHorizontal: 16,
    paddingVertical: 8,
    borderRadius: radii.pill,
    fontWeight: "700",
    overflow: "hidden",
  },
  groupSection: {
    gap: spacing.md,
  },
  groupHeader: {
    flexDirection: "row",
    alignItems: "center",
    gap: spacing.md,
  },
  groupLine: {
    flex: 1,
    height: 1,
    backgroundColor: colors.surfaceHighest,
  },
  groupTitle: {
    color: colors.primary,
    textTransform: "uppercase",
    letterSpacing: 2.4,
    fontSize: 11,
    fontWeight: "800",
  },
  vendorCard: {
    backgroundColor: colors.surfaceLowest,
    borderRadius: radii.lg,
    padding: spacing.md,
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "space-between",
    ...shadows.card,
  },
  vendorInfo: {
    flexDirection: "row",
    gap: spacing.md,
    alignItems: "center",
    flex: 1,
  },
  vendorIcon: {
    width: 56,
    height: 56,
    borderRadius: 18,
    alignItems: "center",
    justifyContent: "center",
  },
  vendorName: {
    color: colors.onSurface,
    fontSize: 20,
    fontWeight: "700",
  },
  vendorChip: {
    alignSelf: "flex-start",
    paddingHorizontal: 10,
    paddingVertical: 4,
    borderRadius: radii.pill,
    overflow: "hidden",
    fontSize: 10,
    textTransform: "uppercase",
    marginTop: 6,
    fontWeight: "800",
  },
  vendorMeta: {
    alignItems: "flex-end",
    gap: 6,
  },

  modalOverlay: {
    flex: 1,
    justifyContent: "flex-end",
    backgroundColor: "rgba(0,0,0,0.5)",
  },
  modalContent: {
    backgroundColor: colors.surfaceLowest,
    padding: spacing.xl,
    borderTopLeftRadius: radii.xl,
    borderTopRightRadius: radii.xl,
  },
  optionsContent: {
    backgroundColor: colors.surfaceLowest,
    padding: spacing.xl,
    margin: spacing.xl,
    borderRadius: radii.xl,
    justifyContent: "center",
  },
  optionButton: {
    flexDirection: "row",
    alignItems: "center",
    padding: spacing.md,
    backgroundColor: colors.surfaceLow,
    borderRadius: radii.md,
    marginTop: spacing.sm,
    gap: spacing.md,
  },
  optionText: {
    fontSize: 16,
    fontWeight: "700",
    color: colors.onSurface,
  },
  modalHeader: {
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
    marginBottom: spacing.lg,
  },
  modalTitle: {
    fontSize: 20,
    fontWeight: "800",
    color: colors.onSurface,
  },
  modalInput: {
    backgroundColor: colors.surfaceLow,
    color: colors.onSurface,
    padding: spacing.md,
    borderRadius: radii.md,
    fontSize: 16,
    marginBottom: spacing.md,
  },
  modalSubmitButton: {
    backgroundColor: colors.primary,
    padding: spacing.md,
    borderRadius: radii.md,
    alignItems: "center",
    marginTop: spacing.sm,
  },
  modalSubmitText: {
    color: colors.surfaceLowest,
    fontWeight: "700",
    fontSize: 16,
  },
  vendorAmount: {
    color: colors.onSurface,
    fontWeight: "800",
    fontSize: 20,
  },
});
