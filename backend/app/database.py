from __future__ import annotations

from collections.abc import Generator

from sqlalchemy import create_engine
from sqlalchemy.orm import Session, declarative_base, sessionmaker

from .config import settings


def _normalise_db_url(url: str) -> str:
    """Ensure the URL uses the psycopg2 driver so SQLAlchemy can find it.

    Vercel's Python environment ships psycopg2-binary. Normalise any of:
      postgresql://...
      postgresql+psycopg://...    (psycopg v3)
      postgres://...              (common shorthand)
    to:
      postgresql+psycopg2://...
    """
    for prefix in (
        "postgresql+psycopg://",
        "postgres+psycopg://",
        "postgresql+psycopg2://",
        "postgres+psycopg2://",
        "postgresql://",
        "postgres://",
    ):
        if url.startswith(prefix):
            rest = url[len(prefix):]
            return f"postgresql+psycopg2://{rest}"
    return url


_db_url = _normalise_db_url(settings.database_url)
connect_args = {"check_same_thread": False} if _db_url.startswith("sqlite") else {}

engine = create_engine(_db_url, connect_args=connect_args, future=True)
SessionLocal = sessionmaker(bind=engine, autoflush=False, autocommit=False, future=True)
Base = declarative_base()


def get_db() -> Generator[Session, None, None]:
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()
