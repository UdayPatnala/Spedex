import React from 'react';
import { render, fireEvent, waitFor } from '@testing-library/react-native';
import { Alert, Linking } from 'react-native';
import { PaymentConfirmScreen } from './PaymentConfirmScreen';

// Mock navigation
const mockNavigation = {
  goBack: jest.fn(),
};

// Mock route params
const mockRoute = {
  params: {
    vendor: { id: 1, name: 'Test Vendor', category: 'food', default_amount: 100 },
    amount: 100,
  },
};

// Mock the API client
jest.mock('../api/client', () => ({
  spedexApi: {
    preparePayment: jest.fn().mockResolvedValue({ transaction_id: 'txn_123', upi_url: 'upi://pay?test=true' }),
    completePayment: jest.fn(),
  },
}));

// Mock Alert
jest.spyOn(Alert, 'alert');

describe('PaymentConfirmScreen Error Handling', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('should show an alert if Linking.openURL throws an error during UPI linking', async () => {
    // Make Linking.openURL throw an error
    jest.spyOn(Linking, 'openURL').mockRejectedValueOnce(new Error('Mocked Linking Error'));

    const { getByText } = render(
      <PaymentConfirmScreen navigation={mockNavigation as any} route={mockRoute as any} />
    );

    // Press the pay button
    const payButton = getByText('Proceed to Pay');
    fireEvent.press(payButton);

    // Wait for the alert to be called
    await waitFor(() => {
      expect(Alert.alert).toHaveBeenCalledWith(
        'Unable to launch UPI app',
        'Please ensure a UPI app is installed.'
      );
    });
  });
});
