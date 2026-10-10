# ADR 005: Non-Custodial Payment Reference Model

## Status
Accepted

## Context
SpeDex users frequently pay the same local merchants (campus canteen, photocopy shop, laundry) but lack a structured directory to store payment identifiers (VPAs, payment links, static QRs). However, SpeDex is not a bank or payment processor.

## Decision
Implement a non-custodial Payment Reference directory:
- Support vendor names, UPI VPAs, payment URLs, QR code references, and notes.
- Strict scheme validation: only `upi://`, `http://`, and `https://` schemes are accepted.
- Zero credential storage: SpeDex strictly prohibits storing UPI MPINs, card PINs, CVVs, passwords, or OTPs.
- Clear product boundary: SpeDex stores references for user convenience and does not process, clear, or hold financial funds.

## Consequences
- Keeps the system compliant with financial regulations and DPDP Act 2023.
- Prevents security liability while delivering high utility to everyday users.
