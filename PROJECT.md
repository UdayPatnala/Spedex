# Project: Spedex Trips Ledger Feature

## Architecture
This feature connects a Java Spring Boot backend with a React dashboard frontend. 
1. **Database Layer (H2/JPA)**: Persists `Trip` entities and links them to `Transaction` entities.
2. **Service Layer**: Manages trip lifecycle rules (at most one active trip per user) and computes aggregated trip statistics (total spend, category split, cash vs card split).
3. **Controller Layer**: Exposes REST API endpoints for user authentication and trip tracking.
4. **Client API (React)**: Performs fetch requests with JWT tokens to backend endpoints.
5. **UI Layer**: Displays trip overview, logging forms, tag lists, progress bars, and sidebar history.

## Milestones
| # | Name | Scope | Dependencies | Status |
|---|------|-------|-------------|--------|
| 1 | Initialization | Create PROJECT.md, define API boundaries | None | DONE |
| 2 | E2E Test Suite | Implement opaque-box test suites (Tiers 1-4) | M1 | PLANNED |
| 3 | Backend API | Implement database entities, repositories, services, controllers, and tests | M1 | PLANNED |
| 4 | Frontend UI | Implement React UI components, navigation, charts, forms, and tests | M3 | PLANNED |
| 5 | Integration & Gate | Run E2E tests, execute Forensic Audit, verify constraints | M2, M3, M4 | PLANNED |
| 6 | Adversarial Hardening | Tier 5 white-box coverage hardening | M5 | PLANNED |

## Interface Contracts
### React Frontend ↔ Spring Boot Backend (REST API)

#### `GET /api/trips`
- **Description**: Retrieve all trips for the authenticated user.
- **Headers**: `Authorization: Bearer <token>`
- **Response Status**: `200 OK`
- **Response Body**:
  ```json
  [
    {
      "id": 1,
      "name": "Business Trip to Mumbai",
      "status": "COMPLETED",
      "created_at": "2026-07-10T10:00:00",
      "completed_at": "2026-07-12T18:00:00"
    }
  ]
  ```

#### `POST /api/trips`
- **Description**: Start a new trip. If there is already an active trip, automatically complete it (set status to COMPLETED and set completedAt to now) or return an error. In Spedex, we will automatically complete any existing active trip to keep the user experience seamless, or return an error if completion rules fail. Let's automatically complete it.
- **Headers**: `Authorization: Bearer <token>`
- **Request Body**:
  ```json
  {
    "name": "Trip to Bangalore"
  }
  ```
- **Response Status**: `201 Created` / `200 OK`
- **Response Body**:
  ```json
  {
    "id": 2,
    "name": "Trip to Bangalore",
    "status": "ACTIVE",
    "created_at": "2026-07-13T18:00:00",
    "completed_at": null
  }
  ```

#### `POST /api/trips/{id}/complete`
- **Description**: Mark a specific trip as completed.
- **Headers**: `Authorization: Bearer <token>`
- **Response Status**: `200 OK`
- **Response Body**:
  ```json
  {
    "id": 2,
    "name": "Trip to Bangalore",
    "status": "COMPLETED",
    "created_at": "2026-07-13T18:00:00",
    "completed_at": "2026-07-13T19:30:00"
  }
  ```

#### `GET /api/trips/{id}`
- **Description**: Retrieve details of a specific trip, including all its associated transactions and summarized statistics.
- **Headers**: `Authorization: Bearer <token>`
- **Response Status**: `200 OK`
- **Response Body**:
  ```json
  {
    "id": 2,
    "name": "Trip to Bangalore",
    "status": "ACTIVE",
    "created_at": "2026-07-13T18:00:00",
    "completed_at": null,
    "total_spend": 1250.00,
    "cash_spend": 450.00,
    "card_online_spend": 800.00,
    "category_breakdown": [
      { "category": "Dining", "amount": 450.00, "percentage": 36.0 },
      { "category": "Transport", "amount": 800.00, "percentage": 64.0 }
    ],
    "transactions": [
      {
        "id": 10,
        "description": "Lunch at Udipi",
        "category": "Dining",
        "amount": 450.00,
        "direction": "expense",
        "payment_method": "CASH",
        "account_label": "Primary Account",
        "status": "completed",
        "occurred_at": "2026-07-13T18:15:00"
      },
      {
        "id": 11,
        "description": "Uber Ride",
        "category": "Transport",
        "amount": 800.00,
        "direction": "expense",
        "payment_method": "UPI",
        "account_label": "Primary Account",
        "status": "completed",
        "occurred_at": "2026-07-13T18:30:00"
      }
    ]
  }
  ```

#### `POST /api/trips/{id}/transactions`
- **Description**: Add a manual transaction (e.g. CASH) to a trip.
- **Headers**: `Authorization: Bearer <token>`
- **Request Body**:
  ```json
  {
    "amount": 150.00,
    "description": "Auto fare",
    "category": "Transport"
  }
  ```
- **Response Status**: `201 Created`
- **Response Body**:
  ```json
  {
    "id": 12,
    "description": "Auto fare",
    "category": "Transport",
    "amount": 150.00,
    "direction": "expense",
    "payment_method": "CASH",
    "account_label": "Primary Account",
    "status": "completed",
    "occurred_at": "2026-07-13T18:40:00"
  }
  ```

## Code Layout
- **Backend**:
  - `backend/src/main/java/com/spedex/model/Trip.java` - Trip entity model
  - `backend/src/main/java/com/spedex/model/Transaction.java` - (Modified) to include trip association
  - `backend/src/main/java/com/spedex/repository/TripRepository.java` - Trip CRUD operations
  - `backend/src/main/java/com/spedex/service/TripService.java` - Trip business logic and statistics calculation
  - `backend/src/main/java/com/spedex/controller/TripController.java` - REST endpoints
- **Frontend**:
  - `dashboard_app/src/types.ts` - (Modified) add Trip models and dashboard data structures
  - `dashboard_app/src/api.ts` - (Modified) add client calls to trip endpoints
  - `dashboard_app/src/App.tsx` - (Modified) Sidebar layout and Trip views integration
