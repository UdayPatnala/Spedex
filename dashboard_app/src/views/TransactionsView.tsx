import { useEffect, useState } from "react";
import type { Category, PaginatedTransactions, Transaction, TransactionCreate, TransactionUpdate, Vendor } from "../types";
import { formatCurrency, formatDate, formatTime, categoryEmoji, categoryLabel } from "../utils/formatters";
import { getTransactions, createTransaction, updateTransaction, deleteTransaction, getCategories, getPaymentReferences, downloadTransactionsCsv } from "../api";
import { AddTransactionModal } from "../components/transactions/AddTransactionModal";
import { EditTransactionModal } from "../components/transactions/EditTransactionModal";
import { DeleteConfirmModal } from "../components/transactions/DeleteConfirmModal";

export function TransactionsView() {
  const [data, setData] = useState<PaginatedTransactions | null>(null);
  const [loading, setLoading] = useState(true);
  const [categories, setCategories] = useState<Category[]>([]);
  const [vendors, setVendors] = useState<Vendor[]>([]);

  // Filters
  const [search, setSearch] = useState("");
  const [category, setCategory] = useState("ALL");
  const [direction, setDirection] = useState("ALL");
  const [page, setPage] = useState(0);

  // Modals
  const [showAddModal, setShowAddModal] = useState(false);
  const [editingTransaction, setEditingTransaction] = useState<Transaction | null>(null);
  const [deletingTransaction, setDeletingTransaction] = useState<Transaction | null>(null);
  const [exporting, setExporting] = useState(false);

  useEffect(() => {
    // Load categories and payment references once
    Promise.all([
      getCategories().catch(() => []),
      getPaymentReferences().catch(() => []),
    ]).then(([cats, vends]) => {
      setCategories(cats);
      setVendors(vends);
    });
  }, []);

  async function loadData() {
    setLoading(true);
    try {
      const res = await getTransactions({
        page,
        size: 15,
        search: search.trim() || undefined,
        category: category !== "ALL" ? category : undefined,
        direction: direction !== "ALL" ? direction : undefined,
      });
      setData(res);
    } catch (e) {
      console.error("Failed to load transactions", e);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    void loadData();
  }, [page, category, direction]);

  function handleSearchSubmit(e: React.FormEvent) {
    e.preventDefault();
    setPage(0);
    void loadData();
  }

  async function handleCreate(payload: TransactionCreate) {
    await createTransaction(payload);
    void loadData();
  }

  async function handleUpdate(id: number, payload: TransactionUpdate) {
    await updateTransaction(id, payload);
    void loadData();
  }

  async function handleDelete(id: number) {
    await deleteTransaction(id);
    void loadData();
  }

  async function handleExportCsv() {
    setExporting(true);
    try {
      await downloadTransactionsCsv();
    } catch (err: any) {
      alert(err?.message || "Failed to export CSV");
    } finally {
      setExporting(false);
    }
  }

  // Summary statistics calculated from the current result set
  const transactions = data?.content || [];
  const totalAmount = transactions.reduce((acc, t) => acc + (t.direction === "expense" ? t.amount : -t.amount), 0);
  const count = data?.total_elements || 0;
  const avgAmount = count > 0 && transactions.length > 0
    ? (transactions.reduce((acc, t) => acc + t.amount, 0) / transactions.length)
    : 0;

  return (
    <div className="transactions-view" style={{ display: "flex", flexDirection: "column", gap: 20 }}>
      {/* Header bar */}
      <section className="card" style={{ display: "flex", justifyContent: "space-between", alignItems: "center", flexWrap: "wrap", gap: 16 }}>
        <div>
          <p className="eyebrow">Personal Ledger</p>
          <h2 className="page-title section-title" style={{ margin: 0 }}>Transaction History</h2>
          <p className="subtle" style={{ margin: "4px 0 0" }}>
            Search, filter, categorize, and audit all recorded financial entries.
          </p>
        </div>

        <div style={{ display: "flex", gap: 10, flexWrap: "wrap" }}>
          <button
            type="button"
            className="action-pill"
            onClick={handleExportCsv}
            disabled={exporting}
            style={{ display: "flex", alignItems: "center", gap: 6, cursor: "pointer", background: "var(--surface)", border: "1px solid var(--border-soft)", borderRadius: 10, padding: "8px 14px" }}
          >
            <span className="material-symbols-outlined" style={{ fontSize: 18 }}>file_download</span>
            {exporting ? "Exporting..." : "Export CSV"}
          </button>
          <button
            type="button"
            className="auth-submit"
            onClick={() => setShowAddModal(true)}
            style={{ display: "flex", alignItems: "center", gap: 6, margin: 0, padding: "8px 18px" }}
          >
            <span className="material-symbols-outlined" style={{ fontSize: 18 }}>add</span>
            Record Expense
          </button>
        </div>
      </section>

      {/* KPI Metrics Strip */}
      <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fit, minmax(200px, 1fr))", gap: 16 }}>
        <div className="card" style={{ padding: 18 }}>
          <span className="metric-label">Filtered Net Spending</span>
          <h3 className="headline" style={{ margin: "6px 0 2px" }}>{formatCurrency(Math.max(0, totalAmount))}</h3>
          <span className="subtle" style={{ fontSize: 12 }}>Across {transactions.length} displayed entries</span>
        </div>
        <div className="card" style={{ padding: 18 }}>
          <span className="metric-label">Total Transactions</span>
          <h3 className="headline" style={{ margin: "6px 0 2px" }}>{count}</h3>
          <span className="subtle" style={{ fontSize: 12 }}>Recorded in personal database</span>
        </div>
        <div className="card" style={{ padding: 18 }}>
          <span className="metric-label">Average Transaction</span>
          <h3 className="headline" style={{ margin: "6px 0 2px" }}>{formatCurrency(avgAmount)}</h3>
          <span className="subtle" style={{ fontSize: 12 }}>Per current page items</span>
        </div>
      </div>

      {/* Filter and Search Bar */}
      <section className="card" style={{ padding: 16 }}>
        <form onSubmit={handleSearchSubmit} style={{ display: "flex", gap: 12, flexWrap: "wrap", alignItems: "center" }}>
          <div style={{ flex: "1 1 240px", position: "relative" }}>
            <input
              type="text"
              placeholder="Search description, payee, or note..."
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              style={{ width: "100%", padding: "10px 14px 10px 36px", borderRadius: 10, border: "1px solid var(--border-soft)", background: "var(--surface)" }}
            />
            <span className="material-symbols-outlined" style={{ position: "absolute", left: 10, top: 11, fontSize: 18, color: "var(--text-subtle)" }}>search</span>
          </div>

          <div style={{ display: "flex", gap: 8, flexWrap: "wrap", alignItems: "center" }}>
            <select
              value={category}
              onChange={(e) => { setCategory(e.target.value); setPage(0); }}
              style={{ padding: "10px 14px", borderRadius: 10, border: "1px solid var(--border-soft)", background: "var(--surface)" }}
            >
              <option value="ALL">All Categories</option>
              {categories.map((c) => (
                <option key={c.id} value={c.name}>{c.name}</option>
              ))}
            </select>

            <select
              value={direction}
              onChange={(e) => { setDirection(e.target.value); setPage(0); }}
              style={{ padding: "10px 14px", borderRadius: 10, border: "1px solid var(--border-soft)", background: "var(--surface)" }}
            >
              <option value="ALL">All Types</option>
              <option value="expense">Expenses Only</option>
              <option value="income">Income Only</option>
            </select>

            <button type="submit" className="action-pill" style={{ padding: "10px 16px", cursor: "pointer", background: "var(--primary)", color: "white", border: "none", borderRadius: 10 }}>
              Search
            </button>
            {(search || category !== "ALL" || direction !== "ALL") && (
              <button
                type="button"
                onClick={() => { setSearch(""); setCategory("ALL"); setDirection("ALL"); setPage(0); }}
                style={{ padding: "10px 14px", cursor: "pointer", background: "transparent", border: "1px solid var(--border-soft)", borderRadius: 10, color: "var(--text-subtle)" }}
              >
                Reset
              </button>
            )}
          </div>
        </form>
      </section>

      {/* Transactions List */}
      <section className="card" style={{ padding: 0, overflow: "hidden" }}>
        {loading ? (
          <div style={{ textAlign: "center", padding: "3rem", color: "var(--text-subtle)" }}>
            <p>Loading transactions from database...</p>
          </div>
        ) : transactions.length === 0 ? (
          <div style={{ textAlign: "center", padding: "3.5rem 1rem", color: "var(--text-subtle)" }}>
            <span className="material-symbols-outlined" style={{ fontSize: 54, opacity: 0.4, marginBottom: 12 }}>receipt_long</span>
            <h4 style={{ margin: "0 0 6px" }}>No transactions found</h4>
            <p className="subtle" style={{ maxWidth: 360, margin: "0 auto 18px" }}>
              {search || category !== "ALL" || direction !== "ALL"
                ? "No entries match your search or filter criteria. Try adjusting your filters."
                : "You have not recorded any expenses yet. Tap 'Record Expense' to add your first transaction."}
            </p>
            <button
              type="button"
              className="auth-submit"
              onClick={() => setShowAddModal(true)}
              style={{ padding: "8px 20px" }}
            >
              Record First Expense
            </button>
          </div>
        ) : (
          <div className="transactions-table-wrapper" style={{ overflowX: "auto" }}>
            <table style={{ width: "100%", borderCollapse: "collapse", textAlign: "left" }}>
              <thead>
                <tr style={{ background: "rgba(0,0,0,0.02)", borderBottom: "1px solid var(--border-soft)", fontSize: 12, color: "var(--text-subtle)", textTransform: "uppercase" }}>
                  <th style={{ padding: "12px 18px" }}>Transaction</th>
                  <th style={{ padding: "12px 14px" }}>Category</th>
                  <th style={{ padding: "12px 14px" }}>Date & Time</th>
                  <th style={{ padding: "12px 14px" }}>Method</th>
                  <th style={{ padding: "12px 18px", textAlign: "right" }}>Amount</th>
                  <th style={{ padding: "12px 18px", textAlign: "center" }}>Actions</th>
                </tr>
              </thead>
              <tbody>
                {transactions.map((t) => (
                  <tr key={t.id} style={{ borderBottom: "1px solid var(--border-soft)", transition: "background 0.15s" }}>
                    <td style={{ padding: "14px 18px" }}>
                      <div style={{ display: "flex", alignItems: "center", gap: 12 }}>
                        <div className={`icon-badge ${t.direction === "income" ? "accent-mint" : "accent-rose"}`} style={{ width: 36, height: 36, borderRadius: 10, display: "flex", alignItems: "center", justifyContent: "center" }}>
                          <span className="emoji-glyph" style={{ fontSize: 18 }}>
                            {t.direction === "income" ? categoryEmoji("Income") : categoryEmoji(t.category)}
                          </span>
                        </div>
                        <div>
                          <strong style={{ display: "block", color: "var(--text)" }}>{t.description}</strong>
                          {t.vendor_name && (
                            <span className="subtle" style={{ fontSize: 12 }}>
                              Payee: {t.vendor_name}
                            </span>
                          )}
                        </div>
                      </div>
                    </td>
                    <td style={{ padding: "14px 14px" }}>
                      <span className="status-chip soft" style={{ fontSize: 12 }}>
                        {categoryLabel(t.category)}
                      </span>
                    </td>
                    <td style={{ padding: "14px 14px", fontSize: 13, color: "var(--text-subtle)" }}>
                      {formatDate(t.occurred_at)} <span style={{ opacity: 0.7 }}>{formatTime(t.occurred_at)}</span>
                    </td>
                    <td style={{ padding: "14px 14px", fontSize: 13 }}>
                      <span style={{ padding: "4px 8px", background: "var(--surface)", border: "1px solid var(--border-soft)", borderRadius: 6, fontSize: 11, fontWeight: 600 }}>
                        {t.payment_method || "UPI"}
                      </span>
                    </td>
                    <td style={{ padding: "14px 18px", textAlign: "right" }}>
                      <strong style={{ fontSize: 15, color: t.direction === "income" ? "#28a745" : "var(--primary)" }}>
                        {t.direction === "income" ? "+" : "-"} {formatCurrency(t.amount)}
                      </strong>
                    </td>
                    <td style={{ padding: "14px 18px", textAlign: "center" }}>
                      <div style={{ display: "inline-flex", gap: 6 }}>
                        <button
                          type="button"
                          title="Edit transaction"
                          onClick={() => setEditingTransaction(t)}
                          style={{ background: "transparent", border: "1px solid var(--border-soft)", borderRadius: 8, padding: "6px 8px", cursor: "pointer", color: "var(--text)" }}
                        >
                          <span className="material-symbols-outlined" style={{ fontSize: 16 }}>edit</span>
                        </button>
                        <button
                          type="button"
                          title="Delete transaction"
                          onClick={() => setDeletingTransaction(t)}
                          style={{ background: "transparent", border: "1px solid rgba(220,53,69,0.3)", borderRadius: 8, padding: "6px 8px", cursor: "pointer", color: "#dc3545" }}
                        >
                          <span className="material-symbols-outlined" style={{ fontSize: 16 }}>delete</span>
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}

        {/* Pagination Bar */}
        {data && data.total_pages > 1 && (
          <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", padding: "14px 18px", borderTop: "1px solid var(--border-soft)" }}>
            <span className="subtle" style={{ fontSize: 13 }}>
              Page {data.page_number + 1} of {data.total_pages} ({data.total_elements} entries)
            </span>
            <div style={{ display: "flex", gap: 8 }}>
              <button
                type="button"
                disabled={data.page_number === 0}
                onClick={() => setPage((p) => Math.max(0, p - 1))}
                style={{ padding: "6px 14px", borderRadius: 8, border: "1px solid var(--border-soft)", background: "var(--surface)", cursor: data.page_number === 0 ? "not-allowed" : "pointer", opacity: data.page_number === 0 ? 0.5 : 1 }}
              >
                Previous
              </button>
              <button
                type="button"
                disabled={data.page_number + 1 >= data.total_pages}
                onClick={() => setPage((p) => p + 1)}
                style={{ padding: "6px 14px", borderRadius: 8, border: "1px solid var(--border-soft)", background: "var(--surface)", cursor: data.page_number + 1 >= data.total_pages ? "not-allowed" : "pointer", opacity: data.page_number + 1 >= data.total_pages ? 0.5 : 1 }}
              >
                Next
              </button>
            </div>
          </div>
        )}
      </section>

      {/* Modals */}
      {showAddModal && (
        <AddTransactionModal
          onClose={() => setShowAddModal(false)}
          onSave={handleCreate}
          categories={categories}
          vendors={vendors}
        />
      )}

      {editingTransaction && (
        <EditTransactionModal
          transaction={editingTransaction}
          onClose={() => setEditingTransaction(null)}
          onSave={handleUpdate}
          categories={categories}
          vendors={vendors}
        />
      )}

      {deletingTransaction && (
        <DeleteConfirmModal
          transaction={deletingTransaction}
          onClose={() => setDeletingTransaction(null)}
          onConfirm={handleDelete}
        />
      )}
    </div>
  );
}
