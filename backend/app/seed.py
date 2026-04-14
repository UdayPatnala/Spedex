from __future__ import annotations

from datetime import datetime, timedelta

from sqlalchemy import select
from sqlalchemy.orm import Session

from .models import Budget, Reminder, Transaction, User, Vendor
from .security import hash_password

DEMO_EMAIL = "alex@spedex.app"
DEMO_PASSWORD = "spedex123"


def reset_sample_account(session: Session, user: User) -> None:
    session.query(Transaction).filter(Transaction.user_id == user.id).delete()
    session.query(Reminder).filter(Reminder.user_id == user.id).delete()
    session.query(Budget).filter(Budget.user_id == user.id).delete()
    session.query(Vendor).filter(Vendor.user_id == user.id).delete()
    session.flush()


def populate_sample_account(
    session: Session,
    user: User,
    *,
    now: datetime | None = None,
    replace_existing: bool = False,
) -> None:
    existing_vendor = session.scalar(select(Vendor.id).where(Vendor.user_id == user.id).limit(1))
    if existing_vendor and not replace_existing:
        return
    if replace_existing:
        reset_sample_account(session, user)

    now = now or datetime.utcnow()

    vendors = [
        Vendor(
            user_id=user.id,
            name="College Canteen",
            category="Food",
            icon="restaurant",
            accent="rose",
            upi_handle="canteen@okhdfc",
            default_amount=45.0,
            is_quick_pay=True,
        ),
        Vendor(
            user_id=user.id,
            name="Campus Bus",
            category="Commute",
            icon="directions_bus",
            accent="amber",
            upi_handle="campusbus@okaxis",
            default_amount=20.0,
            is_quick_pay=True,
        ),
        Vendor(
            user_id=user.id,
            name="Local Xerox",
            category="Stationery",
            icon="menu_book",
            accent="lavender",
            upi_handle="xerox@oksbi",
            default_amount=10.0,
            is_quick_pay=True,
        ),
        Vendor(
            user_id=user.id,
            name="Tea Stall",
            category="Food",
            icon="coffee",
            accent="mint",
            upi_handle="teaboy@okhdfc",
            default_amount=15.0,
            is_quick_pay=True,
        ),
        Vendor(
            user_id=user.id,
            name="Hostel Mess",
            category="Hostel",
            icon="home_work",
            accent="rose",
            upi_handle="messdues@okaxis",
            default_amount=3500.0,
            is_quick_pay=False,
        ),
        Vendor(
            user_id=user.id,
            name="PG Rent",
            category="Hostel",
            icon="home_work",
            accent="amber",
            upi_handle="landlord@oksbi",
            default_amount=7500.0,
            is_quick_pay=False,
        ),
    ]
    session.add_all(vendors)
    session.flush()

    vendor_by_name = {vendor.name: vendor for vendor in vendors}

    budgets = [
        Budget(user_id=user.id, category="Food & Canteen", icon="restaurant", accent="mint", spent=900.0, limit_amount=2500.0),
        Budget(user_id=user.id, category="Commute", icon="directions_bus", accent="amber", spent=300.0, limit_amount=800.0),
        Budget(user_id=user.id, category="Stationery", icon="menu_book", accent="lavender", spent=150.0, limit_amount=500.0),
        Budget(user_id=user.id, category="Hostel & Room", icon="home_work", accent="rose", spent=7500.0, limit_amount=8000.0),
    ]
    session.add_all(budgets)

    reminders = [
        Reminder(
            user_id=user.id,
            title="PG Rent",
            subtitle="Monthly dues sent to landlord",
            amount=7500.0,
            due_date=now + timedelta(days=3),
            autopay_enabled=False,
            status="scheduled",
        ),
        Reminder(
            user_id=user.id,
            title="Semester Books / Subscriptions",
            subtitle="Library/Resource renewal",
            amount=499.0,
            due_date=now + timedelta(days=9),
            autopay_enabled=True,
            status="scheduled",
        ),
    ]
    session.add_all(reminders)

    def expense(
        name: str,
        amount: float,
        category: str,
        when: datetime,
        *,
        payment_method: str = "UPI",
        account_label: str = "UPI Linked Account",
    ) -> Transaction:
        vendor = vendor_by_name.get(name)
        return Transaction(
            user_id=user.id,
            vendor_id=vendor.id if vendor else None,
            description=name,
            category=category,
            amount=amount,
            direction="expense",
            payment_method=payment_method,
            account_label=account_label,
            status="completed",
            external_reference=f"txn-{name.lower().replace(' ', '-')}-{int(when.timestamp())}",
            occurred_at=when,
        )

    transactions = [
        expense("College Canteen", 45.0, "Food", now.replace(hour=13, minute=20, second=0, microsecond=0)),
        expense("Campus Bus", 20.0, "Commute", now.replace(hour=8, minute=5, second=0, microsecond=0)),
        expense("Tea Stall", 15.0, "Food", now - timedelta(days=1)),
        expense("Local Xerox", 10.0, "Stationery", now - timedelta(days=2)),
        expense("Tea Stall", 20.0, "Food", now - timedelta(days=3)),
        expense("PG Rent", 7500.0, "Hostel", now - timedelta(days=14), payment_method="Bank Transfer"),
        Transaction(
            user_id=user.id,
            description="Monthly Allowance from Home",
            category="Income",
            amount=12000.0,
            direction="income",
            payment_method="Bank Transfer",
            account_label="Savings Account",
            status="completed",
            external_reference=f"txn-allowance-{int((now - timedelta(days=15)).timestamp())}",
            occurred_at=now - timedelta(days=15),
        ),
    ]
    session.add_all(transactions)


