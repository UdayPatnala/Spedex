import React from "react";
import { render, waitFor } from "@testing-library/react-native";
import AsyncStorage from "@react-native-async-storage/async-storage";
import { AuthProvider, useAuth } from "../AuthProvider";
import { spedexApi, setAuthToken } from "../../api/client";
import { Text } from "react-native";

// Mock dependencies
jest.mock("@react-native-async-storage/async-storage", () => ({
  getItem: jest.fn(),
  setItem: jest.fn(),
  removeItem: jest.fn(),
}));

jest.mock("../../api/client", () => ({
  spedexApi: {
    getCurrentUser: jest.fn(),
    login: jest.fn(),
    signUp: jest.fn(),
  },
  setAuthToken: jest.fn(),
}));

const TestComponent = () => {
  const auth = useAuth();
  if (!auth.ready) return <Text>Loading...</Text>;
  return (
    <>
      <Text testID="ready">{auth.ready.toString()}</Text>
      <Text testID="token">{auth.token || "null"}</Text>
      <Text testID="user">{auth.user ? auth.user.email : "null"}</Text>
    </>
  );
};

describe("AuthProvider", () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  it("handles error when restoring session fails", async () => {
    // Arrange
    const mockUser = { id: "1", email: "test@example.com" };
    const mockToken = "mock-token";
    const mockStorageData = JSON.stringify({ token: mockToken, user: mockUser });

    (AsyncStorage.getItem as jest.Mock).mockResolvedValueOnce(mockStorageData);

    // Simulate API error when fetching current user
    (spedexApi.getCurrentUser as jest.Mock).mockRejectedValueOnce(new Error("API Error"));

    // Act
    const { getByTestId } = render(
      <AuthProvider>
        <TestComponent />
      </AuthProvider>
    );

    // Wait for the provider to become ready
    await waitFor(() => {
      expect(getByTestId("ready")).toHaveTextContent("true");
    });

    // Assert
    expect(setAuthToken).toHaveBeenCalledWith(mockToken); // Initial set before API call
    expect(spedexApi.getCurrentUser).toHaveBeenCalled();

    // Error handling verifications
    expect(setAuthToken).toHaveBeenCalledWith(null); // Reset after error
    expect(AsyncStorage.removeItem).toHaveBeenCalledWith("spedex.mobile.session");

    expect(getByTestId("token")).toHaveTextContent("null");
    expect(getByTestId("user")).toHaveTextContent("null");
  });

  it("handles case when component unmounts before restoreSession completes with error", async () => {
    // Arrange
    const mockUser = { id: "1", email: "test@example.com" };
    const mockToken = "mock-token";
    const mockStorageData = JSON.stringify({ token: mockToken, user: mockUser });

    // Delay the API rejection to allow component to unmount
    let rejectApi: (reason?: any) => void;
    const apiPromise = new Promise((_, reject) => {
      rejectApi = reject;
    });

    (AsyncStorage.getItem as jest.Mock).mockResolvedValueOnce(mockStorageData);
    (spedexApi.getCurrentUser as jest.Mock).mockReturnValueOnce(apiPromise);

    // Act
    const { unmount } = render(
      <AuthProvider>
        <TestComponent />
      </AuthProvider>
    );

    // Unmount before the API call finishes
    unmount();

    // Now trigger the error
    rejectApi!(new Error("API Error"));

    // Wait a tick for promises to resolve/reject
    await new Promise((resolve) => setTimeout(resolve, 0));

    // Assert
    // Cleanup should still happen
    expect(setAuthToken).toHaveBeenCalledWith(null);
    expect(AsyncStorage.removeItem).toHaveBeenCalledWith("spedex.mobile.session");
  });

  it("handles valid session correctly", async () => {
    // Arrange
    const mockUser = { id: "1", email: "test@example.com" };
    const mockToken = "mock-token";
    const mockStorageData = JSON.stringify({ token: mockToken, user: mockUser });

    (AsyncStorage.getItem as jest.Mock).mockResolvedValueOnce(mockStorageData);
    (spedexApi.getCurrentUser as jest.Mock).mockResolvedValueOnce(mockUser);

    // Act
    const { getByTestId } = render(
      <AuthProvider>
        <TestComponent />
      </AuthProvider>
    );

    // Wait for the provider to become ready
    await waitFor(() => {
      expect(getByTestId("ready")).toHaveTextContent("true");
    });

    // Assert
    expect(setAuthToken).toHaveBeenCalledWith(mockToken);
    expect(getByTestId("token")).toHaveTextContent(mockToken);
    expect(getByTestId("user")).toHaveTextContent("test@example.com");
  });
});
