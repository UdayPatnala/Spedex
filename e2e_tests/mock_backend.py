import json
import re
import base64
import math
import uuid
from datetime import datetime
from urllib.parse import urlparse, parse_qs
from http.server import HTTPServer, BaseHTTPRequestHandler

# In-memory database simulation
USERS = {}  # email -> user details dict
TRIPS = []  # list of trip dicts
TRANSACTIONS = []  # list of transaction dicts
CONSENT_RECORDS = []  # list of consent record dicts
GRIEVANCES = []  # list of grievance dicts
GUARDIAN_TOKENS = {}  # token -> email

USER_ID_COUNTER = 1
TRIP_ID_COUNTER = 1
TRANSACTION_ID_COUNTER = 1
GRIEVANCE_ID_COUNTER = 1

LEGAL_DOCS = {
    "terms": {
        "docType": "terms",
        "title": "Terms of Service",
        "content": "# SpeDex Terms of Service\n\nNon-custodial student finance platform...",
        "lastUpdated": "2026-03-01T00:00:00"
    },
    "privacy": {
        "docType": "privacy",
        "title": "Privacy Policy",
        "content": "# SpeDex Privacy Policy\n\nCompliant with DPDP Act 2023 & Rules 2025...",
        "lastUpdated": "2026-03-01T00:00:00"
    },
    "consent": {
        "docType": "consent",
        "title": "Consent Notice",
        "content": "# SpeDex DPDP Consent Notice...",
        "lastUpdated": "2026-03-01T00:00:00"
    },
    "cookies": {
        "docType": "cookies",
        "title": "Cookie Policy",
        "content": "# SpeDex Cookie Policy...",
        "lastUpdated": "2026-03-01T00:00:00"
    },
    "child-privacy": {
        "docType": "child-privacy",
        "title": "Child & Minor Privacy Policy",
        "content": "# SpeDex Child & Minor Privacy Policy...",
        "lastUpdated": "2026-03-01T00:00:00"
    },
    "data-retention": {
        "docType": "data-retention",
        "title": "Data Retention Policy",
        "content": "# SpeDex Data Retention Policy...",
        "lastUpdated": "2026-03-01T00:00:00"
    },
    "grievance": {
        "docType": "grievance",
        "title": "Grievance Redressal Mechanism",
        "content": "# SpeDex Grievance Redressal Mechanism...",
        "lastUpdated": "2026-03-01T00:00:00"
    },
    "third-parties": {
        "docType": "third-parties",
        "title": "Third-Party Subprocessors Register",
        "content": "# SpeDex Third-Party Subprocessors Register...",
        "lastUpdated": "2026-03-01T00:00:00"
    }
}

def generate_token(email):
    return base64.b64encode(email.encode('utf-8')).decode('utf-8')

def extract_email(token):
    try:
        return base64.b64decode(token.encode('utf-8')).decode('utf-8')
    except Exception:
        return None

class MockBackendHandler(BaseHTTPRequestHandler):
    def _send_response(self, status_code, body):
        self.send_response(status_code)
        self.send_header('Content-Type', 'application/json')
        self.send_header('Access-Control-Allow-Origin', '*')
        self.send_header('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE, OPTIONS')
        self.send_header('Access-Control-Allow-Headers', 'Authorization, Content-Type')
        self.end_headers()
        self.wfile.write(json.dumps(body).encode('utf-8'))

    def do_OPTIONS(self):
        self.send_response(204)
        self.send_header('Access-Control-Allow-Origin', '*')
        self.send_header('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE, OPTIONS')
        self.send_header('Access-Control-Allow-Headers', 'Authorization, Content-Type')
        self.end_headers()

    def get_auth_user(self):
        auth_header = self.headers.get('Authorization')
        if not auth_header or not auth_header.startswith('Bearer '):
            return None
        parts = auth_header.split(' ')
        if len(parts) < 2:
            return None
        token = parts[1]
        email = extract_email(token)
        if email and email in USERS:
            user = USERS[email]
            if user.get("is_erased", False):
                return None
            return email
        return None

    def do_GET(self):
        parsed = urlparse(self.path)
        req_path = parsed.path.rstrip('/')
        if not req_path:
            req_path = '/'

        # 1. Health check
        if req_path == '/api/health':
            self._send_response(200, {"status": "UP", "deployment": "Java/Spring Boot"})
            return

        # 2. Public Legal Documents
        legal_match = re.match(r'^/api/privacy/legal/([a-zA-Z0-9_\-]+)$', req_path)
        if legal_match:
            doc_type = legal_match.group(1).lower()
            if doc_type in LEGAL_DOCS:
                self._send_response(200, LEGAL_DOCS[doc_type])
            else:
                self._send_response(404, {"error": "Not Found", "message": f"Document '{doc_type}' not found"})
            return

        # 3. Public Guardian Verification via GET /api/privacy/guardian-consent/verify?token=...
        if req_path == '/api/privacy/guardian-consent/verify':
            qs = parse_qs(parsed.query)
            token_val = qs.get("token", [None])[0]
            if not token_val or token_val not in GUARDIAN_TOKENS:
                self._send_response(400, {"error": "Bad Request", "message": "Invalid or expired guardian verification token"})
                return
            target_email = GUARDIAN_TOKENS[token_val]
            if target_email in USERS:
                USERS[target_email]["guardian_consent_status"] = "VERIFIED"
                CONSENT_RECORDS.append({
                    "id": len(CONSENT_RECORDS) + 1,
                    "user_email": target_email,
                    "consent_type": "GUARDIAN_VERIFICATION",
                    "action": "GRANTED",
                    "timestamp": datetime.now().strftime('%Y-%m-%dT%H:%M:%S'),
                    "policy_version": "v1.0-2026",
                    "details": "Guardian consent verified via token link"
                })
            self._send_response(200, {"success": True, "message": "Guardian consent verified successfully"})
            return

        # Authenticate other GET requests
        user_email = self.get_auth_user()
        if not user_email:
            self._send_response(401, {"error": "Unauthorized", "message": "Missing or invalid token"})
            return

        user = USERS[user_email]

        # 4. Get current user profile
        if req_path == '/api/auth/me':
            self._send_response(200, {
                "id": user["id"],
                "name": user["name"],
                "email": user["email"],
                "plan": user["plan"],
                "avatar_initials": user["avatar_initials"],
                "member_since": user["member_since"],
                "profile_picture_url": user["profile_picture_url"],
                "is_minor": user.get("is_minor", False),
                "age": user.get("age", 18),
                "guardian_email": user.get("guardian_email"),
                "guardian_name": user.get("guardian_name"),
                "guardian_consent_status": user.get("guardian_consent_status", "NOT_REQUIRED"),
                "analytics_consent": user.get("analytics_consent", False),
                "marketing_consent": user.get("marketing_consent", False)
            })
            return

        # 5. Get Privacy Settings
        if req_path == '/api/privacy/settings':
            self._send_response(200, {
                "analyticsConsent": user.get("analytics_consent", False),
                "marketingConsent": user.get("marketing_consent", False),
                "isMinor": user.get("is_minor", False),
                "age": user.get("age", 18),
                "guardianEmail": user.get("guardian_email"),
                "guardianName": user.get("guardian_name"),
                "guardianConsentStatus": user.get("guardian_consent_status", "NOT_REQUIRED"),
                "dataRetentionDays": 180,
                "canExport": True,
                "canRequestErasure": True
            })
            return

        # 6. Export Personal Data (Section 11 DPDP Act)
        if req_path == '/api/privacy/export':
            user_trips = [t for t in TRIPS if t["user_email"] == user_email]
            user_consents = [c for c in CONSENT_RECORDS if c["user_email"] == user_email]
            user_grievances = [g for g in GRIEVANCES if g["user_email"] == user_email]
            self._send_response(200, {
                "exportTimestamp": datetime.now().strftime('%Y-%m-%dT%H:%M:%S'),
                "userData": {
                    "id": user["id"],
                    "name": user["name"],
                    "email": user["email"],
                    "memberSince": user["member_since"]
                },
                "privacySettings": {
                    "analyticsConsent": user.get("analytics_consent", False),
                    "marketingConsent": user.get("marketing_consent", False),
                    "isMinor": user.get("is_minor", False),
                    "guardianConsentStatus": user.get("guardian_consent_status", "NOT_REQUIRED")
                },
                "trips": user_trips,
                "consentHistory": user_consents,
                "grievanceHistory": user_grievances,
                "complianceNotice": "Exported in accordance with DPDP Act 2023 Section 11 (Right to Access)."
            })
            return

        # 7. Get Grievances
        if req_path == '/api/privacy/grievance':
            user_grievances = [g for g in GRIEVANCES if g["user_email"] == user_email]
            res_grievances = []
            for g in user_grievances:
                res_grievances.append({
                    "ticketId": g["ticket_id"],
                    "category": g["category"],
                    "description": g["description"],
                    "status": g["status"],
                    "createdAt": g["created_at"],
                    "responseMessage": g.get("response_message"),
                    "redressedAt": g.get("redressed_at")
                })
            self._send_response(200, res_grievances)
            return

        # 8. Get all trips for the authenticated user
        if req_path == '/api/trips':
            user_trips = [t for t in TRIPS if t["user_email"] == user_email]
            response_trips = []
            for t in user_trips:
                response_trips.append({
                    "id": t["id"],
                    "name": t["name"],
                    "status": t["status"],
                    "created_at": t["created_at"],
                    "completed_at": t["completed_at"]
                })
            self._send_response(200, response_trips)
            return

        # 9. Get specific trip details (with stats and transactions)
        trip_match = re.match(r'^/api/trips/(\d+)$', req_path)
        if trip_match:
            trip_id = int(trip_match.group(1))
            trip = next((t for t in TRIPS if t["id"] == trip_id), None)
            if not trip:
                self._send_response(404, {"error": "Not Found", "message": f"Trip {trip_id} not found"})
                return
            if trip["user_email"] != user_email:
                self._send_response(403, {"error": "Forbidden", "message": "You do not own this trip"})
                return

            trip_txs = [tx for tx in TRANSACTIONS if tx.get("trip_id") == trip_id]

            total_spend = 0.0
            cash_spend = 0.0
            card_online_spend = 0.0
            category_totals = {}

            successful_txs = [tx for tx in trip_txs if tx["status"] in ("completed", "success")]

            for tx in successful_txs:
                amount = tx["amount"]
                total_spend += amount
                if tx["payment_method"].upper() == "CASH":
                    cash_spend += amount
                else:
                    card_online_spend += amount

                cat = tx["category"].strip()
                matched_key = None
                for existing_key in category_totals:
                    if existing_key.lower() == cat.lower():
                        matched_key = existing_key
                        break
                if matched_key:
                    category_totals[matched_key] += amount
                else:
                    category_totals[cat] = amount

            category_breakdown = []
            for cat, amt in category_totals.items():
                pct = round((amt / total_spend * 100), 2) if total_spend > 0.0 else 0.0
                category_breakdown.append({
                    "category": cat,
                    "amount": round(amt, 2),
                    "percentage": pct
                })
            
            category_breakdown.sort(key=lambda x: (-x["amount"], x["category"]))

            response = {
                "id": trip["id"],
                "name": trip["name"],
                "status": trip["status"],
                "created_at": trip["created_at"],
                "completed_at": trip["completed_at"],
                "total_spend": round(total_spend, 2),
                "cash_spend": round(cash_spend, 2),
                "card_online_spend": round(card_online_spend, 2),
                "category_breakdown": category_breakdown,
                "transactions": [
                    {
                        "id": tx["id"],
                        "description": tx["description"],
                        "category": tx["category"],
                        "amount": tx["amount"],
                        "direction": tx["direction"],
                        "payment_method": tx["payment_method"],
                        "account_label": tx["account_label"],
                        "status": tx["status"],
                        "external_reference": tx.get("external_reference"),
                        "occurred_at": tx["occurred_at"],
                        "vendor_name": tx.get("vendor_name"),
                        "trip_id": tx["trip_id"]
                    } for tx in trip_txs
                ]
            }
            self._send_response(200, response)
            return

        self._send_response(404, {"error": "Not Found", "message": f"Endpoint GET {self.path} not found"})

    def do_POST(self):
        global USER_ID_COUNTER, TRIP_ID_COUNTER, TRANSACTION_ID_COUNTER, GRIEVANCE_ID_COUNTER
        content_length = int(self.headers.get('Content-Length', 0))
        post_data = self.rfile.read(content_length)

        try:
            body = json.loads(post_data.decode('utf-8')) if post_data else {}
        except Exception:
            self._send_response(400, {"error": "Bad Request", "message": "Invalid JSON body"})
            return

        from urllib.parse import urlparse
        parsed = urlparse(self.path)
        req_path = parsed.path.rstrip('/')
        if not req_path:
            req_path = '/'

        # 1. Signup
        if req_path == '/api/auth/signup':
            name = body.get('name')
            email = body.get('email')
            password = body.get('password')

            if name is not None and not isinstance(name, str):
                self._send_response(400, {"error": "Bad Request", "message": "Name must be a string"})
                return
            if email is not None and not isinstance(email, str):
                self._send_response(400, {"error": "Bad Request", "message": "Email must be a string"})
                return
            if password is not None and not isinstance(password, str):
                self._send_response(400, {"error": "Bad Request", "message": "Password must be a string"})
                return

            if not name or not name.strip() or not email or not email.strip() or not password:
                self._send_response(400, {"error": "Bad Request", "message": "Missing required fields"})
                return
            if len(password) < 6:
                self._send_response(400, {"error": "Bad Request", "message": "Password must be at least 6 characters long"})
                return
            if email in USERS:
                self._send_response(400, {"error": "Conflict", "message": "Email already registered"})
                return

            is_minor = bool(body.get('isMinor', False) or body.get('is_minor', False))
            age = int(body.get('age', 18))
            guardian_email = body.get('guardianEmail') or body.get('guardian_email')
            guardian_name = body.get('guardianName') or body.get('guardian_name')

            if is_minor:
                guardian_consent_status = "PENDING"
                analytics_consent = False
                marketing_consent = False
            else:
                guardian_consent_status = "NOT_REQUIRED"
                analytics_consent = bool(body.get('analyticsConsent', False) or body.get('analytics_consent', False))
                marketing_consent = bool(body.get('marketingConsent', False) or body.get('marketing_consent', False))

            parts = name.split()
            initials = "".join([p[0].upper() for p in parts if p])[:2] if parts else "U"

            user = {
                "id": USER_ID_COUNTER,
                "name": name,
                "email": email,
                "password": password,
                "plan": "FREE",
                "avatar_initials": initials,
                "member_since": datetime.now().strftime('%Y-%m-%dT%H:%M:%S'),
                "profile_picture_url": None,
                "is_minor": is_minor,
                "age": age,
                "guardian_email": guardian_email,
                "guardian_name": guardian_name,
                "guardian_consent_status": guardian_consent_status,
                "analytics_consent": analytics_consent,
                "marketing_consent": marketing_consent,
                "is_erased": False
            }
            USERS[email] = user
            USER_ID_COUNTER += 1

            CONSENT_RECORDS.append({
                "id": len(CONSENT_RECORDS) + 1,
                "user_email": email,
                "consent_type": "TERMS_AND_PRIVACY",
                "action": "GRANTED",
                "timestamp": datetime.now().strftime('%Y-%m-%dT%H:%M:%S'),
                "policy_version": "v1.0-2026",
                "details": "Accepted upon registration"
            })

            token = generate_token(email)
            self._send_response(200, {
                "access_token": token,
                "token_type": "bearer",
                "user": {
                    "id": user["id"],
                    "name": user["name"],
                    "email": user["email"],
                    "plan": user["plan"],
                    "avatar_initials": user["avatar_initials"],
                    "member_since": user["member_since"],
                    "is_minor": user["is_minor"],
                    "age": user["age"],
                    "guardian_email": user["guardian_email"],
                    "guardian_name": user["guardian_name"],
                    "guardian_consent_status": user["guardian_consent_status"],
                    "analytics_consent": user["analytics_consent"],
                    "marketing_consent": user["marketing_consent"]
                }
            })
            return

        # Authenticate all remaining POST requests
        user_email = self.get_auth_user()
        if not user_email:
            self._send_response(401, {"error": "Unauthorized", "message": "Missing or invalid token"})
            return

        user = USERS[user_email]

        # 2. Update Privacy Consents
        if req_path == '/api/privacy/consent':
            if user.get("is_minor", False):
                user["analytics_consent"] = False
                user["marketing_consent"] = False
            else:
                if 'analyticsConsent' in body:
                    user['analytics_consent'] = bool(body['analyticsConsent'])
                if 'marketingConsent' in body:
                    user['marketing_consent'] = bool(body['marketingConsent'])

            CONSENT_RECORDS.append({
                "id": len(CONSENT_RECORDS) + 1,
                "user_email": user_email,
                "consent_type": "PREFERENCES_UPDATE",
                "action": "UPDATED",
                "timestamp": datetime.now().strftime('%Y-%m-%dT%H:%M:%S'),
                "policy_version": "v1.0-2026",
                "details": f"Analytics: {user['analytics_consent']}, Marketing: {user['marketing_consent']}"
            })

            self._send_response(200, {
                "analyticsConsent": user["analytics_consent"],
                "marketingConsent": user["marketing_consent"],
                "isMinor": user["is_minor"],
                "age": user["age"],
                "guardianEmail": user.get("guardian_email"),
                "guardianName": user.get("guardian_name"),
                "guardianConsentStatus": user.get("guardian_consent_status", "NOT_REQUIRED"),
                "dataRetentionDays": 180,
                "canExport": True,
                "canRequestErasure": True
            })
            return

        # 3. Request Guardian Consent
        if req_path == '/api/privacy/guardian-consent/request':
            g_name = body.get('guardianName')
            g_email = body.get('guardianEmail')
            if not g_name or not g_email:
                self._send_response(400, {"error": "Bad Request", "message": "Guardian name and email are required"})
                return
            user["guardian_name"] = g_name
            user["guardian_email"] = g_email
            user["guardian_consent_status"] = "PENDING"

            sim_token = str(uuid.uuid4())
            GUARDIAN_TOKENS[sim_token] = user_email

            self._send_response(200, {
                "success": True,
                "message": "Guardian verification request sent",
                "guardianEmail": g_email,
                "verificationToken": sim_token
            })
            return

        # 4. Submit Privacy Grievance
        if req_path == '/api/privacy/grievance':
            cat = body.get('category')
            desc = body.get('description')
            if not cat or not desc:
                self._send_response(400, {"error": "Bad Request", "message": "Category and description are required"})
                return
            ticket_id = f"GRV-{datetime.now().strftime('%Y%m%d')}-{random_hex()}"
            grievance = {
                "id": GRIEVANCE_ID_COUNTER,
                "ticket_id": ticket_id,
                "user_email": user_email,
                "category": cat,
                "description": desc,
                "status": "OPEN",
                "created_at": datetime.now().strftime('%Y-%m-%dT%H:%M:%S'),
                "response_message": "Under review by Data Protection Officer",
                "redressed_at": None
            }
            GRIEVANCES.append(grievance)
            GRIEVANCE_ID_COUNTER += 1

            self._send_response(200, {
                "ticketId": grievance["ticket_id"],
                "category": grievance["category"],
                "description": grievance["description"],
                "status": grievance["status"],
                "createdAt": grievance["created_at"],
                "responseMessage": grievance["response_message"],
                "redressedAt": grievance["redressed_at"]
            })
            return

        # 5. Right to Erasure / Account Deletion
        if req_path == '/api/privacy/erase':
            confirm = body.get('confirmationText', '')
            if confirm != "DELETE MY DATA":
                self._send_response(400, {"error": "Bad Request", "message": "Confirmation text must be 'DELETE MY DATA'"})
                return

            user["is_erased"] = True
            user["erased_at"] = datetime.now().strftime('%Y-%m-%dT%H:%M:%S')
            user["name"] = "Erased User"
            user["avatar_initials"] = "XX"
            user["profile_picture_url"] = None

            CONSENT_RECORDS.append({
                "id": len(CONSENT_RECORDS) + 1,
                "user_email": user_email,
                "consent_type": "RIGHT_TO_ERASURE",
                "action": "WITHDRAWN",
                "timestamp": datetime.now().strftime('%Y-%m-%dT%H:%M:%S'),
                "policy_version": "v1.0-2026",
                "details": "Account erased and anonymized per DPDP Act Section 12"
            })

            self._send_response(200, {
                "success": True,
                "message": "Personal data erased and account anonymized in accordance with DPDP Act Section 12"
            })
            return

        # 6. Start new trip
        if req_path == '/api/trips':
            trip_name = body.get('name')
            if trip_name is not None and not isinstance(trip_name, str):
                self._send_response(400, {"error": "Bad Request", "message": "Trip name must be a string"})
                return

            if not trip_name or not trip_name.strip():
                self._send_response(400, {"error": "Bad Request", "message": "Trip name is required"})
                return

            for t in TRIPS:
                if t["user_email"] == user_email and t["status"] == "ACTIVE":
                    t["status"] = "COMPLETED"
                    t["completed_at"] = datetime.now().strftime('%Y-%m-%dT%H:%M:%S')

            new_trip = {
                "id": TRIP_ID_COUNTER,
                "name": trip_name.strip(),
                "status": "ACTIVE",
                "created_at": datetime.now().strftime('%Y-%m-%dT%H:%M:%S'),
                "completed_at": None,
                "user_email": user_email
            }
            TRIPS.append(new_trip)
            TRIP_ID_COUNTER += 1

            self._send_response(201, {
                "id": new_trip["id"],
                "name": new_trip["name"],
                "status": new_trip["status"],
                "created_at": new_trip["created_at"],
                "completed_at": new_trip["completed_at"]
            })
            return

        # 7. Complete trip
        complete_match = re.match(r'^/api/trips/(\d+)/complete$', req_path)
        if complete_match:
            trip_id = int(complete_match.group(1))
            trip = next((t for t in TRIPS if t["id"] == trip_id), None)
            if not trip:
                self._send_response(404, {"error": "Not Found", "message": f"Trip {trip_id} not found"})
                return
            if trip["user_email"] != user_email:
                self._send_response(403, {"error": "Forbidden", "message": "You do not own this trip"})
                return
            if trip["status"] == "COMPLETED":
                self._send_response(200, {
                    "id": trip["id"],
                    "name": trip["name"],
                    "status": trip["status"],
                    "created_at": trip["created_at"],
                    "completed_at": trip["completed_at"]
                })
                return

            trip["status"] = "COMPLETED"
            trip["completed_at"] = datetime.now().strftime('%Y-%m-%dT%H:%M:%S')

            self._send_response(200, {
                "id": trip["id"],
                "name": trip["name"],
                "status": trip["status"],
                "created_at": trip["created_at"],
                "completed_at": trip["completed_at"]
            })
            return

        # 8. Add manual transaction to trip
        tx_match = re.match(r'^/api/trips/(\d+)/transactions$', req_path)
        if tx_match:
            trip_id = int(tx_match.group(1))
            trip = next((t for t in TRIPS if t["id"] == trip_id), None)
            if not trip:
                self._send_response(404, {"error": "Not Found", "message": f"Trip {trip_id} not found"})
                return
            if trip["user_email"] != user_email:
                self._send_response(403, {"error": "Forbidden", "message": "You do not own this trip"})
                return
            if trip["status"] == "COMPLETED":
                self._send_response(400, {"error": "Bad Request", "message": "Cannot add transaction to a completed trip"})
                return

            amount = body.get('amount')
            description = body.get('description', '')
            category = body.get('category', 'General')
            payment_method = body.get('payment_method', 'CASH')
            vendor_name = body.get('vendor_name')

            if amount is None:
                self._send_response(400, {"error": "Bad Request", "message": "Amount is required"})
                return

            try:
                if isinstance(amount, bool):
                    self._send_response(400, {"error": "Bad Request", "message": "Amount cannot be a boolean"})
                    return
                amount_val = float(amount)
                if not math.isfinite(amount_val) or amount_val <= 0.0:
                    self._send_response(400, {"error": "Bad Request", "message": "Amount must be a finite number greater than zero"})
                    return
            except (ValueError, TypeError):
                self._send_response(400, {"error": "Bad Request", "message": "Amount must be numeric"})
                return

            if description is not None and not isinstance(description, str):
                self._send_response(400, {"error": "Bad Request", "message": "Description must be a string"})
                return
            if category is not None and not isinstance(category, str):
                self._send_response(400, {"error": "Bad Request", "message": "Category must be a string"})
                return
            if payment_method is not None and not isinstance(payment_method, str):
                self._send_response(400, {"error": "Bad Request", "message": "Payment method must be a string"})
                return

            if not category.strip():
                category = "General"

            new_tx = {
                "id": TRANSACTION_ID_COUNTER,
                "description": description if description else f"{category} Expense",
                "category": category.strip(),
                "amount": amount_val,
                "direction": "expense",
                "payment_method": payment_method.upper() if payment_method else "CASH",
                "account_label": "Cash" if (payment_method and payment_method.upper() == "CASH") else "Manual Entry",
                "status": "completed",
                "occurred_at": datetime.now().strftime('%Y-%m-%dT%H:%M:%S'),
                "user_email": user_email,
                "trip_id": trip_id,
                "vendor_name": vendor_name,
                "external_reference": None
            }
            TRANSACTIONS.append(new_tx)
            TRANSACTION_ID_COUNTER += 1

            self._send_response(201, {
                "id": new_tx["id"],
                "description": new_tx["description"],
                "category": new_tx["category"],
                "amount": new_tx["amount"],
                "direction": new_tx["direction"],
                "payment_method": new_tx["payment_method"],
                "account_label": new_tx["account_label"],
                "status": new_tx["status"],
                "occurred_at": new_tx["occurred_at"],
                "trip_id": new_tx["trip_id"]
            })
            return

        # 9. Prepare payment
        if req_path == '/api/payments/prepare':
            amount = body.get('amount')
            payee_name = body.get('payee_name', 'Merchant')
            upi_handle = body.get('upi_handle', 'merchant@upi')
            category = body.get('category', 'Shopping')

            if payee_name is not None:
                if not isinstance(payee_name, str):
                    self._send_response(400, {"error": "Bad Request", "message": "Payee name must be a string"})
                    return
                if not payee_name.strip():
                    self._send_response(400, {"error": "Bad Request", "message": "Payee name cannot be empty"})
                    return
            if upi_handle is not None and not isinstance(upi_handle, str):
                self._send_response(400, {"error": "Bad Request", "message": "UPI handle must be a string"})
                return
            if category is not None:
                if not isinstance(category, str):
                    self._send_response(400, {"error": "Bad Request", "message": "Category must be a string"})
                    return
                if not category.strip():
                    self._send_response(400, {"error": "Bad Request", "message": "Category cannot be empty"})
                    return

            if amount is None:
                self._send_response(400, {"error": "Bad Request", "message": "Amount is required"})
                return

            try:
                if isinstance(amount, bool):
                    self._send_response(400, {"error": "Bad Request", "message": "Amount cannot be a boolean"})
                    return
                amount_val = float(amount)
                if not math.isfinite(amount_val) or amount_val <= 0.0:
                    self._send_response(400, {"error": "Bad Request", "message": "Amount must be a finite number greater than zero"})
                    return
            except (ValueError, TypeError):
                self._send_response(400, {"error": "Bad Request", "message": "Amount must be numeric"})
                return

            active_trip = next((t for t in TRIPS if t["user_email"] == user_email and t["status"] == "ACTIVE"), None)

            new_tx = {
                "id": TRANSACTION_ID_COUNTER,
                "description": f"Payment to {payee_name}",
                "category": category,
                "amount": amount_val,
                "direction": "expense",
                "payment_method": "upi",
                "account_label": "Primary UPI",
                "status": "pending",
                "occurred_at": datetime.now().strftime('%Y-%m-%dT%H:%M:%S'),
                "user_email": user_email,
                "trip_id": active_trip["id"] if active_trip else None,
                "vendor_name": payee_name,
                "external_reference": None
            }
            TRANSACTIONS.append(new_tx)
            TRANSACTION_ID_COUNTER += 1

            self._send_response(200, {
                "transaction": {
                    "id": new_tx["id"],
                    "description": new_tx["description"],
                    "category": new_tx["category"],
                    "amount": new_tx["amount"],
                    "direction": new_tx["direction"],
                    "payment_method": new_tx["payment_method"],
                    "account_label": new_tx["account_label"],
                    "status": new_tx["status"],
                    "occurred_at": new_tx["occurred_at"],
                    "trip_id": new_tx["trip_id"]
                },
                "upi_url": f"upi://pay?pa={upi_handle}&pn={payee_name}&am={amount_val}&cu=INR",
                "redirect_message": "Redirecting to UPI app..."
            })
            return

        # 10. Complete prepared payment
        complete_tx_match = re.match(r'^/api/payments/(\d+)/complete$', req_path)
        if complete_tx_match:
            tx_id = int(complete_tx_match.group(1))
            tx = next((t for t in TRANSACTIONS if t["id"] == tx_id), None)
            if not tx:
                self._send_response(404, {"error": "Not Found", "message": f"Transaction {tx_id} not found"})
                return
            if tx["user_email"] != user_email:
                self._send_response(403, {"error": "Forbidden", "message": "You do not own this transaction"})
                return

            req_status = body.get('status', 'failed')
            norm_status = "success" if str(req_status).lower() in ("completed", "success") else "failed"
            tx["status"] = norm_status

            self._send_response(200, {
                "transaction_id": tx_id,
                "status": norm_status,
                "message": "Payment state updated"
            })
            return

        self._send_response(404, {"error": "Not Found", "message": f"Endpoint POST {self.path} not found"})

def random_hex():
    import random
    return f"{random.randint(0x1000, 0xFFFF):04X}"

def run(port=8080):
    server_address = ('', port)
    httpd = HTTPServer(server_address, MockBackendHandler)
    print(f"Mock Spedex Backend running on port {port}...")
    try:
        httpd.serve_forever()
    except KeyboardInterrupt:
        print("\nStopping Mock Backend...")
        httpd.server_close()

if __name__ == '__main__':
    import sys
    port = 8080
    if len(sys.argv) > 1:
        try:
            port = int(sys.argv[1])
        except ValueError:
            pass
    run(port)
