import json
import re
import base64
import math
from datetime import datetime
from http.server import HTTPServer, BaseHTTPRequestHandler

# In-memory database simulation
USERS = {}  # email -> user details dict
TRIPS = []  # list of trip dicts
TRANSACTIONS = []  # list of transaction dicts
USER_ID_COUNTER = 1
TRIP_ID_COUNTER = 1
TRANSACTION_ID_COUNTER = 1

def generate_token(email):
    # Simple base64 token simulation
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
            return email
        return None

    def do_GET(self):
        from urllib.parse import urlparse
        parsed = urlparse(self.path)
        req_path = parsed.path.rstrip('/')
        if not req_path:
            req_path = '/'

        # 1. Health check
        if req_path == '/api/health':
            self._send_response(200, {"status": "UP", "deployment": "Java/Spring Boot"})
            return

        # Authenticate other GET requests
        user_email = self.get_auth_user()
        if not user_email:
            self._send_response(401, {"error": "Unauthorized", "message": "Missing or invalid token"})
            return

        # 2. Get current user profile
        if req_path == '/api/auth/me':
            user = USERS[user_email]
            self._send_response(200, {
                "id": user["id"],
                "name": user["name"],
                "email": user["email"],
                "plan": user["plan"],
                "avatar_initials": user["avatar_initials"],
                "member_since": user["member_since"],
                "profile_picture_url": user["profile_picture_url"]
            })
            return

        # 3. Get all trips for the authenticated user
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

        # 4. Get specific trip details (with stats and transactions)
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

            # Gather all transactions linked to this trip
            trip_txs = [tx for tx in TRANSACTIONS if tx.get("trip_id") == trip_id]

            # Compute Statistics
            total_spend = 0.0
            cash_spend = 0.0
            card_online_spend = 0.0
            category_totals = {}

            # Filter for successful/completed transactions
            successful_txs = [tx for tx in trip_txs if tx["status"] in ("completed", "success")]

            for tx in successful_txs:
                amount = tx["amount"]
                total_spend += amount
                if tx["payment_method"].upper() == "CASH":
                    cash_spend += amount
                else:
                    card_online_spend += amount

                cat = tx["category"].strip()
                # Case-insensitive grouping while preserving first-seen case
                matched_key = None
                for existing_key in category_totals:
                    if existing_key.lower() == cat.lower():
                        matched_key = existing_key
                        break
                if matched_key:
                    category_totals[matched_key] += amount
                else:
                    category_totals[cat] = amount

            # Build category breakdown
            category_breakdown = []
            for cat, amt in category_totals.items():
                pct = round((amt / total_spend * 100), 2) if total_spend > 0.0 else 0.0
                category_breakdown.append({
                    "category": cat,
                    "amount": round(amt, 2),
                    "percentage": pct
                })
            
            # Sort breakdown descending by amount, and ascending by category for stable sorting
            category_breakdown.sort(key=lambda x: (-x["amount"], x["category"]))

            # Build response in snake_case
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
        global USER_ID_COUNTER, TRIP_ID_COUNTER, TRANSACTION_ID_COUNTER
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

            # Extract initials from name
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
                "profile_picture_url": None
            }
            USERS[email] = user
            USER_ID_COUNTER += 1

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
                    "profile_picture_url": user["profile_picture_url"]
                }
            })
            return

        # 2. Login
        if req_path == '/api/auth/login':
            email = body.get('email')
            password = body.get('password')

            if not email or not password:
                self._send_response(400, {"error": "Bad Request", "message": "Email and password are required"})
                return
            if email not in USERS or USERS[email]["password"] != password:
                self._send_response(401, {"error": "Unauthorized", "message": "Invalid email or password"})
                return

            user = USERS[email]
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
                    "profile_picture_url": user["profile_picture_url"]
                }
            })
            return

        # Authenticate all other POST endpoints
        user_email = self.get_auth_user()
        if not user_email:
            self._send_response(401, {"error": "Unauthorized", "message": "Missing or invalid token"})
            return

        # 3. Start a new trip
        if req_path == '/api/trips':
            name = body.get('name')
            if name is not None and not isinstance(name, str):
                self._send_response(400, {"error": "Bad Request", "message": "Trip name must be a string"})
                return
            if not name or not name.strip():
                self._send_response(400, {"error": "Bad Request", "message": "Trip name is required"})
                return

            now_str = datetime.now().strftime('%Y-%m-%dT%H:%M:%S')
            # Auto-complete any existing active trip for this user
            for t in TRIPS:
                if t["user_email"] == user_email and t["status"] == "ACTIVE":
                    t["status"] = "COMPLETED"
                    t["completed_at"] = now_str

            new_trip = {
                "id": TRIP_ID_COUNTER,
                "name": name.strip(),
                "status": "ACTIVE",
                "created_at": now_str,
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

        # 4. Complete a specific trip
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

            # Complete the trip only if not already completed
            if trip["status"] != "COMPLETED":
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

        # 5. Add manual transaction to a trip
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

            # Reject manual transaction on completed trips
            if trip["status"] == "COMPLETED":
                self._send_response(400, {"error": "Bad Request", "message": "Cannot log transactions on completed trips"})
                return

            amount = body.get('amount')
            description = body.get('description')
            category = body.get('category')

            if amount is None or description is None or category is None:
                self._send_response(400, {"error": "Bad Request", "message": "Missing amount, description, or category"})
                return

            if not isinstance(description, str) or not isinstance(category, str):
                self._send_response(400, {"error": "Bad Request", "message": "Description and category must be strings"})
                return

            if not description.strip() or not category.strip():
                self._send_response(400, {"error": "Bad Request", "message": "Description and category cannot be empty"})
                return

            import math
            try:
                if isinstance(amount, bool):
                    self._send_response(400, {"error": "Bad Request", "message": "Amount cannot be a boolean"})
                    return
                amount_val = float(amount)
                if not math.isfinite(amount_val) or amount_val <= 0.0:
                    self._send_response(400, {"error": "Bad Request", "message": "Amount must be a finite number greater than zero"})
                    return
            except (ValueError, TypeError):
                self._send_response(400, {"error": "Bad Request", "message": "Amount must be a numeric value"})
                return

            new_tx = {
                "id": TRANSACTION_ID_COUNTER,
                "description": description.strip(),
                "category": category.strip(),
                "amount": amount_val,
                "direction": "expense",
                "payment_method": "CASH",
                "account_label": "Primary Account",
                "status": "completed",
                "occurred_at": datetime.now().strftime('%Y-%m-%dT%H:%M:%S'),
                "user_email": user_email,
                "trip_id": trip_id
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

        # 6. Prepare auto-linked payment
        if req_path == '/api/payments/prepare':
            amount = body.get('amount')
            payee_name = body.get('payee_name', 'Unknown Vendor')
            upi_handle = body.get('upi_handle', '')
            category = body.get('category', 'Payments')

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

            import math
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

            # Check active trip
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

        # 7. Complete prepared payment
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
