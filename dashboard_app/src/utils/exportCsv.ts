import type { Transaction, TripDetails } from '../types';

export function exportTransactionsToCsv(transactions: Transaction[], filename: string = 'spedex-transactions.csv') {
  if (!transactions || transactions.length === 0) return;

  const headers = ['ID', 'Description', 'Category', 'Amount (INR)', 'Direction', 'Payment Method', 'Account', 'Status', 'Date'];
  const rows = transactions.map((tx) => [
    tx.id,
    `"${(tx.description || '').replace(/"/g, '""')}"`,
    `"${(tx.category || '').replace(/"/g, '""')}"`,
    tx.amount,
    tx.direction,
    tx.payment_method,
    `"${(tx.account_label || '').replace(/"/g, '""')}"`,
    tx.status,
    `"${tx.occurred_at || ''}"`,
  ]);

  const csvContent = 'data:text/csv;charset=utf-8,' + [headers.join(','), ...rows.map((e) => e.join(','))].join('\n');
  const encodedUri = encodeURI(csvContent);
  const link = document.createElement('a');
  link.setAttribute('href', encodedUri);
  link.setAttribute('download', filename);
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
}

export function exportTripDetailsToCsv(trip: TripDetails) {
  if (!trip) return;
  exportTransactionsToCsv(trip.transactions || [], `spedex-trip-${trip.name.toLowerCase().replace(/\s+/g, '-')}.csv`);
}
