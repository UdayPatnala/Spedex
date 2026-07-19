import json
import random
import math
import unittest
import urllib.error
import urllib.request

API_BASE = "http://localhost:8080/api"

def make_request(path, method="GET", headers=None, body=None):
    url = f"{API_BASE}{path}"
    req_headers = {
        "Accept": "application/json"
    }
    if headers:
        req_headers.update(headers)

    data = None
    if body is not None:
        data = json.dumps(body).encode('utf-8')
        req_headers['Content-Type'] = 'application/json'

    req = urllib.request.Request(url, data=data, headers=req_headers, method=method)
    try:
        with urllib.request.urlopen(req) as response:
            status_code = response.status
            res_body = response.read().decode('utf-8')
            return status_code, json.loads(res_body) if res_body else {}
    except urllib.error.HTTPError as e:
        status_code = e.code
        try:
            res_body = e.read().decode('utf-8')
            return status_code, json.loads(res_body) if res_body else {}
        except Exception:
            return status_code, {"error": str(e)}
    except Exception as e:
        return 500, {"error": str(e)}

class SpedexTripsE2ETestCase(unittest.TestCase):
    def setUp(self):
        # Create a unique user for each test to ensure isolation
        self.rand_id = random.randint(10000, 99999)
        self.email = f"user_{self.rand_id}@example.com"
        self.password = "password123"
        self.name = f"Tester {self.rand_id}"

        status, res = make_request("/auth/signup", "POST", body={
            "name": self.name,
            "email": self.email,
            "password": self.password
        })
        self.assertEqual(status, 200, f"Signup failed: {res}")
        self.token = res["access_token"]
        self.headers = {"Authorization": f"Bearer {self.token}"}

    # ==========================================
    # TIER 1: FEATURE COVERAGE (30 cases, 5 per F1-F6)
    # ==========================================

    # --- Feature 1: Trip Lifecycle ---
    def test_tier1_f1_01(self):
        # Start a new trip and verify response format and initial status
        status, trip = make_request("/trips", "POST", headers=self.headers, body={"name": "Business Mumbai"})
        self.assertEqual(status, 201)
        self.assertEqual(trip["name"], "Business Mumbai")
        self.assertEqual(trip["status"], "ACTIVE")
        self.assertIsNotNone(trip.get("created_at"))
        self.assertIsNone(trip.get("completed_at"))

    def test_tier1_f1_02(self):
        # Retrieve all trips and verify the list contains the started trip
        status, _ = make_request("/trips", "POST", headers=self.headers, body={"name": "Vacation Goa"})
        self.assertEqual(status, 201)
        status, trips = make_request("/trips", "GET", headers=self.headers)
        self.assertEqual(status, 200)
        self.assertEqual(len(trips), 1)
        self.assertEqual(trips[0]["name"], "Vacation Goa")

    def test_tier1_f1_03(self):
        # View specific trip details and verify initial stats/empty transactions
        status, trip = make_request("/trips", "POST", headers=self.headers, body={"name": "Short Trip"})
        self.assertEqual(status, 201)
        trip_id = trip["id"]
        status, details = make_request(f"/trips/{trip_id}", "GET", headers=self.headers)
        self.assertEqual(status, 200)
        self.assertEqual(details["id"], trip_id)
        self.assertEqual(details["status"], "ACTIVE")
        self.assertEqual(details["total_spend"], 0.0)
        self.assertEqual(len(details["transactions"]), 0)

    def test_tier1_f1_04(self):
        # Complete a trip and verify status and completed_at is populated
        status, trip = make_request("/trips", "POST", headers=self.headers, body={"name": "To Complete"})
        self.assertEqual(status, 201)
        trip_id = trip["id"]
        status, completed = make_request(f"/trips/{trip_id}/complete", "POST", headers=self.headers)
        self.assertEqual(status, 200)
        self.assertEqual(completed["status"], "COMPLETED")
        self.assertIsNotNone(completed.get("completed_at"))

    def test_tier1_f1_05(self):
        # Verify starting a second trip auto-completes the first active trip
        status, trip1 = make_request("/trips", "POST", headers=self.headers, body={"name": "Trip One"})
        self.assertEqual(status, 201)
        trip1_id = trip1["id"]
        status, trip2 = make_request("/trips", "POST", headers=self.headers, body={"name": "Trip Two"})
        self.assertEqual(status, 201)
        # Check trip 1 details to see if completed
        status, details1 = make_request(f"/trips/{trip1_id}", "GET", headers=self.headers)
        self.assertEqual(status, 200)
        self.assertEqual(details1["status"], "COMPLETED")
        self.assertIsNotNone(details1["completed_at"])

    # --- Feature 2: Auto-linking Transactions ---
    def test_tier1_f2_01(self):
        # Prepare payment with active trip and verify it auto-links
        status, _ = make_request("/trips", "POST", headers=self.headers, body={"name": "Active Goa"})
        self.assertEqual(status, 201)
        status, prep = make_request("/payments/prepare", "POST", headers=self.headers, body={
            "amount": 120.0, "payee_name": "Restaurant", "category": "Food"
        })
        self.assertEqual(status, 200)
        self.assertIsNotNone(prep["transaction"]["trip_id"])
        self.assertEqual(prep["transaction"]["status"], "pending")

    def test_tier1_f2_02(self):
        # Complete prepared payment with success status
        status, _ = make_request("/trips", "POST", headers=self.headers, body={"name": "Active Goa"})
        self.assertEqual(status, 201)
        status, prep = make_request("/payments/prepare", "POST", headers=self.headers, body={
            "amount": 150.0, "payee_name": "Hotel", "category": "Lodging"
        })
        tx_id = prep["transaction"]["id"]
        status, comp = make_request(f"/payments/{tx_id}/complete", "POST", headers=self.headers, body={"status": "success"})
        self.assertEqual(status, 200)
        self.assertEqual(comp["status"], "success")

    def test_tier1_f2_03(self):
        # Verify completed transaction updates stats and is returned in details
        status, trip = make_request("/trips", "POST", headers=self.headers, body={"name": "Active Goa"})
        self.assertEqual(status, 201)
        trip_id = trip["id"]
        status, prep = make_request("/payments/prepare", "POST", headers=self.headers, body={
            "amount": 150.0, "payee_name": "Hotel", "category": "Lodging"
        })
        tx_id = prep["transaction"]["id"]
        make_request(f"/payments/{tx_id}/complete", "POST", headers=self.headers, body={"status": "success"})
        status, details = make_request(f"/trips/{trip_id}", "GET", headers=self.headers)
        self.assertEqual(status, 200)
        self.assertEqual(details["total_spend"], 150.0)
        self.assertEqual(details["card_online_spend"], 150.0)
        self.assertEqual(len(details["transactions"]), 1)
        self.assertEqual(details["transactions"][0]["id"], tx_id)

    def test_tier1_f2_04(self):
        # Verify completing with failed does not count in successful statistics
        status, trip = make_request("/trips", "POST", headers=self.headers, body={"name": "Active Goa"})
        self.assertEqual(status, 201)
        trip_id = trip["id"]
        status, prep = make_request("/payments/prepare", "POST", headers=self.headers, body={
            "amount": 250.0, "payee_name": "Cab", "category": "Transport"
        })
        tx_id = prep["transaction"]["id"]
        status, comp = make_request(f"/payments/{tx_id}/complete", "POST", headers=self.headers, body={"status": "failed"})
        self.assertEqual(status, 200)
        status, details = make_request(f"/trips/{trip_id}", "GET", headers=self.headers)
        self.assertEqual(status, 200)
        self.assertEqual(details["total_spend"], 0.0) # failed does not aggregate

    def test_tier1_f2_05(self):
        # Prepare payment with no active trip -> trip_id is null
        status, prep = make_request("/payments/prepare", "POST", headers=self.headers, body={
            "amount": 99.0, "payee_name": "Shop", "category": "Retail"
        })
        self.assertEqual(status, 200)
        self.assertIsNone(prep["transaction"]["trip_id"])

    # --- Feature 3: Manual CASH Transactions ---
    def test_tier1_f3_01(self):
        # Log manual CASH transaction on active trip
        status, trip = make_request("/trips", "POST", headers=self.headers, body={"name": "My Trip"})
        self.assertEqual(status, 201)
        trip_id = trip["id"]
        status, tx = make_request(f"/trips/{trip_id}/transactions", "POST", headers=self.headers, body={
            "amount": 35.0, "description": "Tea", "category": "Food"
        })
        self.assertEqual(status, 201)
        self.assertEqual(tx["payment_method"], "CASH")
        self.assertEqual(tx["status"], "completed")

    def test_tier1_f3_02(self):
        # Verify manual cash transaction shows up in details list
        status, trip = make_request("/trips", "POST", headers=self.headers, body={"name": "My Trip"})
        trip_id = trip["id"]
        status, tx = make_request(f"/trips/{trip_id}/transactions", "POST", headers=self.headers, body={
            "amount": 35.0, "description": "Tea", "category": "Food"
        })
        tx_id = tx["id"]
        status, details = make_request(f"/trips/{trip_id}", "GET", headers=self.headers)
        self.assertEqual(status, 200)
        self.assertEqual(len(details["transactions"]), 1)
        self.assertEqual(details["transactions"][0]["id"], tx_id)

    def test_tier1_f3_03(self):
        # Verify manual cash updates cash_spend and total_spend statistics
        status, trip = make_request("/trips", "POST", headers=self.headers, body={"name": "My Trip"})
        trip_id = trip["id"]
        make_request(f"/trips/{trip_id}/transactions", "POST", headers=self.headers, body={
            "amount": 40.0, "description": "Auto", "category": "Transport"
        })
        status, details = make_request(f"/trips/{trip_id}", "GET", headers=self.headers)
        self.assertEqual(status, 200)
        self.assertEqual(details["cash_spend"], 40.0)
        self.assertEqual(details["total_spend"], 40.0)

    def test_tier1_f3_04(self):
        # Verify multiple manual cash transactions can be logged successfully
        status, trip = make_request("/trips", "POST", headers=self.headers, body={"name": "My Trip"})
        trip_id = trip["id"]
        make_request(f"/trips/{trip_id}/transactions", "POST", headers=self.headers, body={
            "amount": 10.0, "description": "Water", "category": "Food"
        })
        make_request(f"/trips/{trip_id}/transactions", "POST", headers=self.headers, body={
            "amount": 20.0, "description": "Bus", "category": "Transport"
        })
        status, details = make_request(f"/trips/{trip_id}", "GET", headers=self.headers)
        self.assertEqual(status, 200)
        self.assertEqual(len(details["transactions"]), 2)
        self.assertEqual(details["total_spend"], 30.0)

    def test_tier1_f3_05(self):
        # Verify manual cash transactions cannot be added to another user's trip (F5 isolation helper)
        status, trip = make_request("/trips", "POST", headers=self.headers, body={"name": "User A Trip"})
        trip_id = trip["id"]

        # Register User B
        email_b = f"user_b_{self.rand_id}@example.com"
        status, signup_b = make_request("/auth/signup", "POST", body={
            "name": "User B", "email": email_b, "password": "password123"
        })
        token_b = signup_b["access_token"]
        headers_b = {"Authorization": f"Bearer {token_b}"}

        status, _ = make_request(f"/trips/{trip_id}/transactions", "POST", headers=headers_b, body={
            "amount": 50.0, "description": "Hack", "category": "Food"
        })
        self.assertEqual(status, 403)

    # --- Feature 4: Aggregated Trip Statistics ---
    def test_tier1_f4_01(self):
        # Verify initial stats are all zero
        status, trip = make_request("/trips", "POST", headers=self.headers, body={"name": "Stat Trip"})
        trip_id = trip["id"]
        status, details = make_request(f"/trips/{trip_id}", "GET", headers=self.headers)
        self.assertEqual(status, 200)
        self.assertEqual(details["total_spend"], 0.0)
        self.assertEqual(details["cash_spend"], 0.0)
        self.assertEqual(details["card_online_spend"], 0.0)
        self.assertEqual(len(details["category_breakdown"]), 0)

    def test_tier1_f4_02(self):
        # Log a cash transaction and verify stats update
        status, trip = make_request("/trips", "POST", headers=self.headers, body={"name": "Stat Trip"})
        trip_id = trip["id"]
        make_request(f"/trips/{trip_id}/transactions", "POST", headers=self.headers, body={
            "amount": 100.0, "description": "Lunch", "category": "Food"
        })
        status, details = make_request(f"/trips/{trip_id}", "GET", headers=self.headers)
        self.assertEqual(status, 200)
        self.assertEqual(details["total_spend"], 100.0)
        self.assertEqual(details["cash_spend"], 100.0)
        self.assertEqual(details["card_online_spend"], 0.0)

    def test_tier1_f4_03(self):
        # Log mixed cash and online, verify they aggregate correctly
        status, trip = make_request("/trips", "POST", headers=self.headers, body={"name": "Stat Trip"})
        trip_id = trip["id"]
        make_request(f"/trips/{trip_id}/transactions", "POST", headers=self.headers, body={
            "amount": 100.0, "description": "Lunch", "category": "Food"
        })
        status, prep = make_request("/payments/prepare", "POST", headers=self.headers, body={
            "amount": 200.0, "payee_name": "Cab", "category": "Transport"
        })
        tx_id = prep["transaction"]["id"]
        make_request(f"/payments/{tx_id}/complete", "POST", headers=self.headers, body={"status": "success"})

        status, details = make_request(f"/trips/{trip_id}", "GET", headers=self.headers)
        self.assertEqual(status, 200)
        self.assertEqual(details["total_spend"], 300.0)
        self.assertEqual(details["cash_spend"], 100.0)
        self.assertEqual(details["card_online_spend"], 200.0)

    def test_tier1_f4_04(self):
        # Verify single category percentage calculation is 100.0
        status, trip = make_request("/trips", "POST", headers=self.headers, body={"name": "Stat Trip"})
        trip_id = trip["id"]
        make_request(f"/trips/{trip_id}/transactions", "POST", headers=self.headers, body={
            "amount": 50.0, "description": "Lunch", "category": "Food"
        })
        status, details = make_request(f"/trips/{trip_id}", "GET", headers=self.headers)
        self.assertEqual(status, 200)
        bd = details["category_breakdown"]
        self.assertEqual(len(bd), 1)
        self.assertEqual(bd[0]["category"], "Food")
        self.assertEqual(bd[0]["percentage"], 100.0)

    def test_tier1_f4_05(self):
        # Verify category breakdown sorting order: amount descending, alphabetically secondary
        status, trip = make_request("/trips", "POST", headers=self.headers, body={"name": "Stat Trip"})
        trip_id = trip["id"]
        # Food: 10.0, Transport: 20.0, Entertainment: 10.0
        make_request(f"/trips/{trip_id}/transactions", "POST", headers=self.headers, body={
            "amount": 10.0, "description": "Snack", "category": "Food"
        })
        make_request(f"/trips/{trip_id}/transactions", "POST", headers=self.headers, body={
            "amount": 20.0, "description": "Auto", "category": "Transport"
        })
        make_request(f"/trips/{trip_id}/transactions", "POST", headers=self.headers, body={
            "amount": 10.0, "description": "Movie", "category": "Entertainment"
        })
        status, details = make_request(f"/trips/{trip_id}", "GET", headers=self.headers)
        self.assertEqual(status, 200)
        bd = details["category_breakdown"]
        self.assertEqual(len(bd), 3)
        # Transport first (20.0)
        self.assertEqual(bd[0]["category"], "Transport")
        # Entertainment next (10.0) - alphabetically before Food
        self.assertEqual(bd[1]["category"], "Entertainment")
        # Food last (10.0)
        self.assertEqual(bd[2]["category"], "Food")

    # --- Feature 5: Security Isolation ---
    def test_tier1_f5_01(self):
        # Request /trips without auth header -> 401
        status, _ = make_request("/trips", "GET")
        self.assertEqual(status, 401)

    def test_tier1_f5_02(self):
        # Request with invalid token -> 401
        status, _ = make_request("/trips", "GET", headers={"Authorization": "Bearer badtoken"})
        self.assertEqual(status, 401)

    def test_tier1_f5_03(self):
        # View another user's trip details -> 403 or 404
        status, trip = make_request("/trips", "POST", headers=self.headers, body={"name": "User A Trip"})
        trip_id = trip["id"]

        # User B
        email_b = f"user_b_{self.rand_id}@example.com"
        status, signup_b = make_request("/auth/signup", "POST", body={
            "name": "User B", "email": email_b, "password": "password123"
        })
        token_b = signup_b["access_token"]
        headers_b = {"Authorization": f"Bearer {token_b}"}

        status, _ = make_request(f"/trips/{trip_id}", "GET", headers=headers_b)
        self.assertIn(status, (403, 404))

    def test_tier1_f5_04(self):
        # Add transaction to another user's trip -> 403 or 404
        status, trip = make_request("/trips", "POST", headers=self.headers, body={"name": "User A Trip"})
        trip_id = trip["id"]

        email_b = f"user_b_{self.rand_id}@example.com"
        status, signup_b = make_request("/auth/signup", "POST", body={
            "name": "User B", "email": email_b, "password": "password123"
        })
        token_b = signup_b["access_token"]
        headers_b = {"Authorization": f"Bearer {token_b}"}

        status, _ = make_request(f"/trips/{trip_id}/transactions", "POST", headers=headers_b, body={
            "amount": 10.0, "description": "Unauthorized", "category": "Food"
        })
        self.assertIn(status, (403, 404))

    def test_tier1_f5_05(self):
        # Complete another user's trip -> 403 or 404
        status, trip = make_request("/trips", "POST", headers=self.headers, body={"name": "User A Trip"})
        trip_id = trip["id"]

        email_b = f"user_b_{self.rand_id}@example.com"
        status, signup_b = make_request("/auth/signup", "POST", body={
            "name": "User B", "email": email_b, "password": "password123"
        })
        token_b = signup_b["access_token"]
        headers_b = {"Authorization": f"Bearer {token_b}"}

        status, _ = make_request(f"/trips/{trip_id}/complete", "POST", headers=headers_b)
        self.assertIn(status, (403, 404))

    # --- Feature 6: Custom Tags & UI ---
    def test_tier1_f6_01(self):
        # Log manual transaction with custom category and verify it is preserved
        status, trip = make_request("/trips", "POST", headers=self.headers, body={"name": "Tag Trip"})
        trip_id = trip["id"]
        status, tx = make_request(f"/trips/{trip_id}/transactions", "POST", headers=self.headers, body={
            "amount": 50.0, "description": "Item", "category": "CustomCategory123"
        })
        self.assertEqual(status, 201)
        self.assertEqual(tx["category"], "CustomCategory123")

    def test_tier1_f6_02(self):
        # Verify custom category tag containing emojis is preserved
        status, trip = make_request("/trips", "POST", headers=self.headers, body={"name": "Tag Trip"})
        trip_id = trip["id"]
        status, tx = make_request(f"/trips/{trip_id}/transactions", "POST", headers=self.headers, body={
            "amount": 50.0, "description": "Item", "category": "Snacks 🍕🍩"
        })
        self.assertEqual(status, 201)
        self.assertEqual(tx["category"], "Snacks 🍕🍩")

    def test_tier1_f6_03(self):
        # Verify custom tag is shown in category_breakdown
        status, trip = make_request("/trips", "POST", headers=self.headers, body={"name": "Tag Trip"})
        trip_id = trip["id"]
        make_request(f"/trips/{trip_id}/transactions", "POST", headers=self.headers, body={
            "amount": 80.0, "description": "Item", "category": "Diving 🤿"
        })
        status, details = make_request(f"/trips/{trip_id}", "GET", headers=self.headers)
        self.assertEqual(status, 200)
        self.assertEqual(details["category_breakdown"][0]["category"], "Diving 🤿")

    def test_tier1_f6_04(self):
        # Verify prepare payment works with custom category tag
        status, _ = make_request("/trips", "POST", headers=self.headers, body={"name": "Tag Trip"})
        status, prep = make_request("/payments/prepare", "POST", headers=self.headers, body={
            "amount": 100.0, "payee_name": "Vendor", "category": "CustomOnlineTag"
        })
        self.assertEqual(status, 200)
        self.assertEqual(prep["transaction"]["category"], "CustomOnlineTag")

    def test_tier1_f6_05(self):
        # Complete online payment with custom category, check details breakdown
        status, trip = make_request("/trips", "POST", headers=self.headers, body={"name": "Tag Trip"})
        trip_id = trip["id"]
        status, prep = make_request("/payments/prepare", "POST", headers=self.headers, body={
            "amount": 100.0, "payee_name": "Vendor", "category": "CustomOnlineTag"
        })
        tx_id = prep["transaction"]["id"]
        make_request(f"/payments/{tx_id}/complete", "POST", headers=self.headers, body={"status": "success"})
        status, details = make_request(f"/trips/{trip_id}", "GET", headers=self.headers)
        self.assertEqual(status, 200)
        self.assertEqual(details["category_breakdown"][0]["category"], "CustomOnlineTag")

    # ==========================================
    # TIER 2: BOUNDARY & CORNER CASES (30 cases, 5 per F1-F6)
    # ==========================================

    # --- Feature 1 Boundaries ---
    def test_tier2_f1_01(self):
        # Empty trip name -> 400
        status, _ = make_request("/trips", "POST", headers=self.headers, body={"name": ""})
        self.assertEqual(status, 400)

    def test_tier2_f1_02(self):
        # Whitespace-only trip name -> 400
        status, _ = make_request("/trips", "POST", headers=self.headers, body={"name": "   "})
        self.assertEqual(status, 400)

    def test_tier2_f1_03(self):
        # Trip name not a string (type confusion) -> 400
        status, _ = make_request("/trips", "POST", headers=self.headers, body={"name": 123})
        self.assertEqual(status, 400)

    def test_tier2_f1_04(self):
        # Complete already completed trip -> 200, completed_at unchanged (idempotent)
        status, trip = make_request("/trips", "POST", headers=self.headers, body={"name": "Idempotency"})
        trip_id = trip["id"]
        status, comp1 = make_request(f"/trips/{trip_id}/complete", "POST", headers=self.headers)
        self.assertEqual(status, 200)
        t1 = comp1["completed_at"]
        status, comp2 = make_request(f"/trips/{trip_id}/complete", "POST", headers=self.headers)
        self.assertEqual(status, 200)
        self.assertEqual(comp2["completed_at"], t1)

    def test_tier2_f1_05(self):
        # View details for non-existent trip ID -> 404
        status, _ = make_request("/trips/999999", "GET", headers=self.headers)
        self.assertEqual(status, 404)

    # --- Feature 2 Boundaries ---
    def test_tier2_f2_01(self):
        # Prepare payment with type confusion on payload fields -> 400
        status, _ = make_request("/payments/prepare", "POST", headers=self.headers, body={
            "amount": 100.0, "payee_name": 12345, "category": "Food"
        })
        self.assertEqual(status, 400)

    def test_tier2_f2_02(self):
        # Prepare payment with negative amount -> 400
        status, _ = make_request("/payments/prepare", "POST", headers=self.headers, body={
            "amount": -50.0, "payee_name": "Vendor", "category": "Food"
        })
        self.assertEqual(status, 400)

    def test_tier2_f2_03(self):
        # Prepare payment with zero amount -> 400
        status, _ = make_request("/payments/prepare", "POST", headers=self.headers, body={
            "amount": 0.0, "payee_name": "Vendor", "category": "Food"
        })
        self.assertEqual(status, 400)

    def test_tier2_f2_04(self):
        # Prepare payment with infinite or NaN amount -> 400
        status, _ = make_request("/payments/prepare", "POST", headers=self.headers, body={
            "amount": float('inf'), "payee_name": "Vendor", "category": "Food"
        })
        self.assertEqual(status, 400)

    def test_tier2_f2_05(self):
        # Complete non-existent payment ID -> 404
        status, _ = make_request("/payments/999999/complete", "POST", headers=self.headers, body={"status": "success"})
        self.assertEqual(status, 404)

    # --- Feature 3 Boundaries ---
    def test_tier2_f3_01(self):
        # Log manual CASH transaction on completed trip -> 400
        status, trip = make_request("/trips", "POST", headers=self.headers, body={"name": "Completed Trip"})
        trip_id = trip["id"]
        make_request(f"/trips/{trip_id}/complete", "POST", headers=self.headers)

        status, _ = make_request(f"/trips/{trip_id}/transactions", "POST", headers=self.headers, body={
            "amount": 10.0, "description": "Tea", "category": "Food"
        })
        self.assertEqual(status, 400)

    def test_tier2_f3_02(self):
        # Log cash with negative amount -> 400
        status, trip = make_request("/trips", "POST", headers=self.headers, body={"name": "Boundary"})
        trip_id = trip["id"]
        status, _ = make_request(f"/trips/{trip_id}/transactions", "POST", headers=self.headers, body={
            "amount": -5.0, "description": "Tea", "category": "Food"
        })
        self.assertEqual(status, 400)

    def test_tier2_f3_03(self):
        # Log cash with zero amount -> 400
        status, trip = make_request("/trips", "POST", headers=self.headers, body={"name": "Boundary"})
        trip_id = trip["id"]
        status, _ = make_request(f"/trips/{trip_id}/transactions", "POST", headers=self.headers, body={
            "amount": 0.0, "description": "Tea", "category": "Food"
        })
        self.assertEqual(status, 400)

    def test_tier2_f3_04(self):
        # Log cash with float pollution (NaN / Inf) -> 400
        status, trip = make_request("/trips", "POST", headers=self.headers, body={"name": "Boundary"})
        trip_id = trip["id"]
        status, _ = make_request(f"/trips/{trip_id}/transactions", "POST", headers=self.headers, body={
            "amount": float('nan'), "description": "Tea", "category": "Food"
        })
        self.assertEqual(status, 400)

    def test_tier2_f3_05(self):
        # Log cash with missing fields or type confusion (e.g. description is list) -> 400
        status, trip = make_request("/trips", "POST", headers=self.headers, body={"name": "Boundary"})
        trip_id = trip["id"]
        status, _ = make_request(f"/trips/{trip_id}/transactions", "POST", headers=self.headers, body={
            "amount": 10.0, "description": ["Not a string"], "category": "Food"
        })
        self.assertEqual(status, 400)

    # --- Feature 4 Boundaries ---
    def test_tier2_f4_01(self):
        # Group categories case-insensitively. Verify "dining", "Dining", and "DINING" are grouped into one breakdown entry.
        status, trip = make_request("/trips", "POST", headers=self.headers, body={"name": "Case Insensitive"})
        trip_id = trip["id"]
        make_request(f"/trips/{trip_id}/transactions", "POST", headers=self.headers, body={
            "amount": 10.0, "description": "d1", "category": "dining"
        })
        make_request(f"/trips/{trip_id}/transactions", "POST", headers=self.headers, body={
            "amount": 20.0, "description": "d2", "category": "Dining"
        })
        make_request(f"/trips/{trip_id}/transactions", "POST", headers=self.headers, body={
            "amount": 30.0, "description": "d3", "category": "DINING"
        })
        status, details = make_request(f"/trips/{trip_id}", "GET", headers=self.headers)
        self.assertEqual(status, 200)
        bd = details["category_breakdown"]
        self.assertEqual(len(bd), 1)
        self.assertEqual(bd[0]["category"].lower(), "dining")
        self.assertEqual(bd[0]["amount"], 60.0)

    def test_tier2_f4_02(self):
        # Validate precision rounding: 10.03 + 20.07 + 30.01 = 60.11
        status, trip = make_request("/trips", "POST", headers=self.headers, body={"name": "Precision"})
        trip_id = trip["id"]
        make_request(f"/trips/{trip_id}/transactions", "POST", headers=self.headers, body={
            "amount": 10.03, "description": "p1", "category": "C1"
        })
        make_request(f"/trips/{trip_id}/transactions", "POST", headers=self.headers, body={
            "amount": 20.07, "description": "p2", "category": "C2"
        })
        make_request(f"/trips/{trip_id}/transactions", "POST", headers=self.headers, body={
            "amount": 30.01, "description": "p3", "category": "C1"
        })
        status, details = make_request(f"/trips/{trip_id}", "GET", headers=self.headers)
        self.assertEqual(status, 200)
        self.assertEqual(details["total_spend"], 60.11)

    def test_tier2_f4_03(self):
        # Trip with no transactions -> empty breakdown and 0.0 stats
        status, trip = make_request("/trips", "POST", headers=self.headers, body={"name": "Empty"})
        trip_id = trip["id"]
        status, details = make_request(f"/trips/{trip_id}", "GET", headers=self.headers)
        self.assertEqual(status, 200)
        self.assertEqual(details["total_spend"], 0.0)
        self.assertEqual(len(details["category_breakdown"]), 0)

    def test_tier2_f4_04(self):
        # Trip with only failed transactions -> empty breakdown and 0.0 stats
        status, trip = make_request("/trips", "POST", headers=self.headers, body={"name": "Failed Only"})
        trip_id = trip["id"]
        status, prep = make_request("/payments/prepare", "POST", headers=self.headers, body={
            "amount": 100.0, "payee_name": "Vendor", "category": "Food"
        })
        tx_id = prep["transaction"]["id"]
        make_request(f"/payments/{tx_id}/complete", "POST", headers=self.headers, body={"status": "failed"})
        status, details = make_request(f"/trips/{trip_id}", "GET", headers=self.headers)
        self.assertEqual(status, 200)
        self.assertEqual(details["total_spend"], 0.0)
        self.assertEqual(len(details["category_breakdown"]), 0)

    def test_tier2_f4_05(self):
        # Trip with a huge transaction amount -> verify it calculates without crashing
        status, trip = make_request("/trips", "POST", headers=self.headers, body={"name": "Huge"})
        trip_id = trip["id"]
        make_request(f"/trips/{trip_id}/transactions", "POST", headers=self.headers, body={
            "amount": 12345678.90, "description": "Large", "category": "Invest"
        })
        status, details = make_request(f"/trips/{trip_id}", "GET", headers=self.headers)
        self.assertEqual(status, 200)
        self.assertEqual(details["total_spend"], 12345678.90)

    # --- Feature 5 Boundaries ---
    def test_tier2_f5_01(self):
        # Authorization header with split length < 2 (e.g. "Bearer") -> 401 (no crash)
        status, _ = make_request("/trips", "GET", headers={"Authorization": "Bearer"})
        self.assertEqual(status, 401)

    def test_tier2_f5_02(self):
        # Authorization header with invalid encoding/signature -> 401
        status, _ = make_request("/trips", "GET", headers={"Authorization": "Bearer @#!@#"})
        self.assertEqual(status, 401)

    def test_tier2_f5_03(self):
        # Path Parsing: trailing slash on GET /trips/ -> should work
        status, _ = make_request("/trips", "POST", headers=self.headers, body={"name": "Slash Test"})
        self.assertEqual(status, 201)
        status, trips = make_request("/trips/", "GET", headers=self.headers)
        self.assertEqual(status, 200)
        self.assertEqual(len(trips), 1)

    def test_tier2_f5_04(self):
        # Path Parsing: query parameters on GET /api/trips?active=true -> should work
        status, trips = make_request("/trips?active=true", "GET", headers=self.headers)
        self.assertEqual(status, 200)

    def test_tier2_f5_05(self):
        # Path Parsing: trailing slash and ID on specific trip details endpoint GET /api/trips/{id}/
        status, trip = make_request("/trips", "POST", headers=self.headers, body={"name": "Slash ID"})
        trip_id = trip["id"]
        status, details = make_request(f"/trips/{trip_id}/", "GET", headers=self.headers)
        self.assertEqual(status, 200)
        self.assertEqual(details["name"], "Slash ID")

    # --- Feature 6 Boundaries ---
    def test_tier2_f6_01(self):
        # Custom category name that is very long
        status, trip = make_request("/trips", "POST", headers=self.headers, body={"name": "Long Tag"})
        trip_id = trip["id"]
        long_cat = "A" * 500
        status, tx = make_request(f"/trips/{trip_id}/transactions", "POST", headers=self.headers, body={
            "amount": 10.0, "description": "Item", "category": long_cat
        })
        self.assertEqual(status, 201)
        self.assertEqual(tx["category"], long_cat)

    def test_tier2_f6_02(self):
        # Custom category name with special character escaping checks
        status, trip = make_request("/trips", "POST", headers=self.headers, body={"name": "Escaping"})
        trip_id = trip["id"]
        special_cat = "Tag\\'\"\\\\;!@#$"
        status, tx = make_request(f"/trips/{trip_id}/transactions", "POST", headers=self.headers, body={
            "amount": 10.0, "description": "Item", "category": special_cat
        })
        self.assertEqual(status, 201)
        self.assertEqual(tx["category"], special_cat)

    def test_tier2_f6_03(self):
        # Leading and trailing whitespace in category tags is stripped before grouping
        status, trip = make_request("/trips", "POST", headers=self.headers, body={"name": "Whitespace Strip"})
        trip_id = trip["id"]
        make_request(f"/trips/{trip_id}/transactions", "POST", headers=self.headers, body={
            "amount": 10.0, "description": "Item", "category": "  Dining  "
        })
        status, details = make_request(f"/trips/{trip_id}", "GET", headers=self.headers)
        self.assertEqual(status, 200)
        self.assertEqual(details["category_breakdown"][0]["category"], "Dining")

    def test_tier2_f6_04(self):
        # Prepare payment with empty category tag -> 400
        status, _ = make_request("/payments/prepare", "POST", headers=self.headers, body={
            "amount": 100.0, "payee_name": "Vendor", "category": "   "
        })
        self.assertEqual(status, 400)

    def test_tier2_f6_05(self):
        # Multiple transactions with different category casing preserve first seen casing in breakdown
        status, trip = make_request("/trips", "POST", headers=self.headers, body={"name": "Casing Preservation"})
        trip_id = trip["id"]
        make_request(f"/trips/{trip_id}/transactions", "POST", headers=self.headers, body={
            "amount": 10.0, "description": "d1", "category": "dining"
        })
        make_request(f"/trips/{trip_id}/transactions", "POST", headers=self.headers, body={
            "amount": 20.0, "description": "d2", "category": "Dining"
        })
        status, details = make_request(f"/trips/{trip_id}", "GET", headers=self.headers)
        self.assertEqual(status, 200)
        # Should be grouped under "dining" since it was first seen
        self.assertEqual(details["category_breakdown"][0]["category"], "dining")

    # ==========================================
    # TIER 3: CROSS-FEATURE COMBINATIONS (6 cases)
    # ==========================================

    def test_tier3_01(self):
        # Active trip auto-linking combination. Start Trip A, prepare payment A, start Trip B (auto-completes Trip A), prepare payment B. Complete payment A and B. Verify correct isolation.
        status, trip_a = make_request("/trips", "POST", headers=self.headers, body={"name": "Trip A"})
        trip_a_id = trip_a["id"]
        status, prep_a = make_request("/payments/prepare", "POST", headers=self.headers, body={
            "amount": 100.0, "payee_name": "Vendor A", "category": "Food"
        })
        tx_a_id = prep_a["transaction"]["id"]

        status, trip_b = make_request("/trips", "POST", headers=self.headers, body={"name": "Trip B"})
        trip_b_id = trip_b["id"]
        status, prep_b = make_request("/payments/prepare", "POST", headers=self.headers, body={
            "amount": 200.0, "payee_name": "Vendor B", "category": "Cab"
        })
        tx_b_id = prep_b["transaction"]["id"]

        make_request(f"/payments/{tx_a_id}/complete", "POST", headers=self.headers, body={"status": "success"})
        make_request(f"/payments/{tx_b_id}/complete", "POST", headers=self.headers, body={"status": "success"})

        status, det_a = make_request(f"/trips/{trip_a_id}", "GET", headers=self.headers)
        status, det_b = make_request(f"/trips/{trip_b_id}", "GET", headers=self.headers)

        self.assertEqual(det_a["total_spend"], 100.0)
        self.assertEqual(det_b["total_spend"], 200.0)

    def test_tier3_02(self):
        # Active trip manual cash combination. Start Trip A, log cash A, start Trip B (completes Trip A), log cash B. Check stats.
        status, trip_a = make_request("/trips", "POST", headers=self.headers, body={"name": "Trip A"})
        trip_a_id = trip_a["id"]
        make_request(f"/trips/{trip_a_id}/transactions", "POST", headers=self.headers, body={
            "amount": 30.0, "description": "Tea", "category": "Food"
        })

        status, trip_b = make_request("/trips", "POST", headers=self.headers, body={"name": "Trip B"})
        trip_b_id = trip_b["id"]
        make_request(f"/trips/{trip_b_id}/transactions", "POST", headers=self.headers, body={
            "amount": 70.0, "description": "Lunch", "category": "Food"
        })

        status, det_a = make_request(f"/trips/{trip_a_id}", "GET", headers=self.headers)
        status, det_b = make_request(f"/trips/{trip_b_id}", "GET", headers=self.headers)

        self.assertEqual(det_a["total_spend"], 30.0)
        self.assertEqual(det_a["status"], "COMPLETED")
        self.assertEqual(det_b["total_spend"], 70.0)
        self.assertEqual(det_b["status"], "ACTIVE")

    def test_tier3_03(self):
        # Mixed payment types. Start Trip, log cash, prepare & complete online payment. Check totals match.
        status, trip = make_request("/trips", "POST", headers=self.headers, body={"name": "Mixed"})
        trip_id = trip["id"]
        make_request(f"/trips/{trip_id}/transactions", "POST", headers=self.headers, body={
            "amount": 50.0, "description": "Cash Item", "category": "Tag"
        })
        status, prep = make_request("/payments/prepare", "POST", headers=self.headers, body={
            "amount": 150.0, "payee_name": "Online Vendor", "category": "Tag"
        })
        tx_id = prep["transaction"]["id"]
        make_request(f"/payments/{tx_id}/complete", "POST", headers=self.headers, body={"status": "success"})

        status, details = make_request(f"/trips/{trip_id}", "GET", headers=self.headers)
        self.assertEqual(status, 200)
        self.assertEqual(details["cash_spend"], 50.0)
        self.assertEqual(details["card_online_spend"], 150.0)
        self.assertEqual(details["total_spend"], 200.0)

    def test_tier3_04(self):
        # Trip completion freezing. Start Trip, complete it. Verify trying to prepare payment does not link to it, and trying to log cash fails.
        status, trip = make_request("/trips", "POST", headers=self.headers, body={"name": "Freezing"})
        trip_id = trip["id"]
        make_request(f"/trips/{trip_id}/complete", "POST", headers=self.headers)

        # 1. Prepare payment -> trip_id is null
        status, prep = make_request("/payments/prepare", "POST", headers=self.headers, body={
            "amount": 100.0, "payee_name": "Vendor", "category": "Food"
        })
        self.assertEqual(status, 200)
        self.assertIsNone(prep["transaction"]["trip_id"])

        # 2. Log cash -> returns 400
        status, _ = make_request(f"/trips/{trip_id}/transactions", "POST", headers=self.headers, body={
            "amount": 50.0, "description": "Cash", "category": "Food"
        })
        self.assertEqual(status, 400)

    def test_tier3_05(self):
        # Multiple users parallel transactions. User A and User B both have active trips, both prepare transactions. Verify no cross-linking or leaks.
        status, trip_a = make_request("/trips", "POST", headers=self.headers, body={"name": "User A Trip"})
        trip_a_id = trip_a["id"]

        # Register User B
        email_b = f"user_b_{self.rand_id}@example.com"
        status, signup_b = make_request("/auth/signup", "POST", body={
            "name": "User B", "email": email_b, "password": "password123"
        })
        token_b = signup_b["access_token"]
        headers_b = {"Authorization": f"Bearer {token_b}"}

        status, trip_b = make_request("/trips", "POST", headers=headers_b, body={"name": "User B Trip"})
        trip_b_id = trip_b["id"]

        # User A prepares payment
        status, prep_a = make_request("/payments/prepare", "POST", headers=self.headers, body={
            "amount": 10.0, "payee_name": "A Vendor"
        })
        # User B prepares payment
        status, prep_b = make_request("/payments/prepare", "POST", headers=headers_b, body={
            "amount": 20.0, "payee_name": "B Vendor"
        })

        self.assertEqual(prep_a["transaction"]["trip_id"], trip_a_id)
        self.assertEqual(prep_b["transaction"]["trip_id"], trip_b_id)

    def test_tier3_06(self):
        # Health check path parsing with trailing slashes and query parameters.
        status, health1 = make_request("/health/", "GET")
        self.assertEqual(status, 200)
        self.assertEqual(health1["status"], "UP")

        status, health2 = make_request("/health?v=1", "GET")
        self.assertEqual(status, 200)
        self.assertEqual(health2["status"], "UP")

    # ==========================================
    # TIER 4: REAL-WORLD APPLICATION SCENARIOS (5 cases)
    # ==========================================

    def test_tier4_01(self):
        # Goa getaway complete workflow
        status, trip = make_request("/trips", "POST", headers=self.headers, body={"name": "Weekend Goa"})
        self.assertEqual(status, 201)
        trip_id = trip["id"]

        # Online hotel booking
        status, prep_h = make_request("/payments/prepare", "POST", headers=self.headers, body={
            "amount": 5000.0, "payee_name": "Taj Hotels", "category": "Lodging"
        })
        tx_h_id = prep_h["transaction"]["id"]
        make_request(f"/payments/{tx_h_id}/complete", "POST", headers=self.headers, body={"status": "success"})

        # Street food cash
        make_request(f"/trips/{trip_id}/transactions", "POST", headers=self.headers, body={
            "amount": 350.0, "description": "Street Food", "category": "Dining"
        })

        # Taxi cash
        make_request(f"/trips/{trip_id}/transactions", "POST", headers=self.headers, body={
            "amount": 600.0, "description": "Cab ride", "category": "Transport"
        })

        # Dinner online
        status, prep_d = make_request("/payments/prepare", "POST", headers=self.headers, body={
            "amount": 1500.0, "payee_name": "Dinner Buffet", "category": "Dining"
        })
        tx_d_id = prep_d["transaction"]["id"]
        make_request(f"/payments/{tx_d_id}/complete", "POST", headers=self.headers, body={"status": "success"})

        # Souvenirs cash
        make_request(f"/trips/{trip_id}/transactions", "POST", headers=self.headers, body={
            "amount": 800.0, "description": "Fridge Magnets", "category": "Souvenirs"
        })

        # Complete trip
        make_request(f"/trips/{trip_id}/complete", "POST", headers=self.headers)

        # Retrieve report
        status, report = make_request(f"/trips/{trip_id}", "GET", headers=self.headers)
        self.assertEqual(status, 200)
        self.assertEqual(report["status"], "COMPLETED")
        self.assertEqual(report["total_spend"], 8250.0)
        self.assertEqual(report["cash_spend"], 1750.0)
        self.assertEqual(report["card_online_spend"], 6500.0)

        # Check breakdown
        bd = report["category_breakdown"]
        self.assertEqual(len(bd), 4)
        self.assertEqual(bd[0]["category"], "Lodging")
        self.assertEqual(bd[0]["amount"], 5000.0)
        self.assertEqual(bd[0]["percentage"], 60.61)

    def test_tier4_02(self):
        # Delhi to Gurgaon double agent workflow
        status, delhi = make_request("/trips", "POST", headers=self.headers, body={"name": "Delhi Visit"})
        delhi_id = delhi["id"]

        make_request(f"/trips/{delhi_id}/transactions", "POST", headers=self.headers, body={
            "amount": 500.0, "description": "Cafe", "category": "Dining"
        })

        # Auto-completes Delhi
        status, gurgaon = make_request("/trips", "POST", headers=self.headers, body={"name": "Gurgaon Conference"})
        gurgaon_id = gurgaon["id"]

        make_request(f"/trips/{gurgaon_id}/transactions", "POST", headers=self.headers, body={
            "amount": 1000.0, "description": "Dinner", "category": "Dining"
        })

        status, details_delhi = make_request(f"/trips/{delhi_id}", "GET", headers=self.headers)
        self.assertEqual(details_delhi["status"], "COMPLETED")
        self.assertEqual(details_delhi["total_spend"], 500.0)

        status, details_gurgaon = make_request(f"/trips/{gurgaon_id}", "GET", headers=self.headers)
        self.assertEqual(details_gurgaon["status"], "ACTIVE")
        self.assertEqual(details_gurgaon["total_spend"], 1000.0)

    def test_tier4_03(self):
        # Multi-city business tour. Start Mumbai, log online expenses, complete. Start Chennai, log cash expenses, complete. Check history list.
        status, mum = make_request("/trips", "POST", headers=self.headers, body={"name": "Mumbai Tour"})
        mum_id = mum["id"]
        status, prep1 = make_request("/payments/prepare", "POST", headers=self.headers, body={
            "amount": 8000.0, "payee_name": "Indigo", "category": "Flight"
        })
        make_request(f"/payments/{prep1['transaction']['id']}/complete", "POST", headers=self.headers, body={"status": "success"})
        make_request(f"/trips/{mum_id}/complete", "POST", headers=self.headers)

        status, che = make_request("/trips", "POST", headers=self.headers, body={"name": "Chennai Tour"})
        che_id = che["id"]
        make_request(f"/trips/{che_id}/transactions", "POST", headers=self.headers, body={
            "amount": 200.0, "description": "Auto ride", "category": "Transport"
        })
        make_request(f"/trips/{che_id}/complete", "POST", headers=self.headers)

        status, trips = make_request("/trips", "GET", headers=self.headers)
        self.assertEqual(len(trips), 2)

    def test_tier4_04(self):
        # Failed transaction recovery flow
        status, trip = make_request("/trips", "POST", headers=self.headers, body={"name": "Recovery Trip"})
        trip_id = trip["id"]

        # Prepare online payment of 500.0
        status, prep = make_request("/payments/prepare", "POST", headers=self.headers, body={
            "amount": 500.0, "payee_name": "Uber", "category": "Transport"
        })
        tx_id = prep["transaction"]["id"]

        # Fails
        make_request(f"/payments/{tx_id}/complete", "POST", headers=self.headers, body={"status": "failed"})

        # Recover by logging cash transaction of 450.0 instead
        make_request(f"/trips/{trip_id}/transactions", "POST", headers=self.headers, body={
            "amount": 450.0, "description": "Local Taxi Cash", "category": "Transport"
        })

        status, details = make_request(f"/trips/{trip_id}", "GET", headers=self.headers)
        self.assertEqual(details["total_spend"], 450.0)
        self.assertEqual(details["cash_spend"], 450.0)
        self.assertEqual(details["card_online_spend"], 0.0)

    def test_tier4_05(self):
        # Complete expense report flow with 5 mixed transactions
        status, trip = make_request("/trips", "POST", headers=self.headers, body={"name": "Comprehensive"})
        trip_id = trip["id"]

        # 1. Cash Food: 10.0
        make_request(f"/trips/{trip_id}/transactions", "POST", headers=self.headers, body={
            "amount": 10.0, "description": "a", "category": "Food"
        })
        # 2. Cash Transport: 20.0
        make_request(f"/trips/{trip_id}/transactions", "POST", headers=self.headers, body={
            "amount": 20.0, "description": "b", "category": "Transport"
        })
        # 3. Online Food: 30.0 (Success)
        status, prep1 = make_request("/payments/prepare", "POST", headers=self.headers, body={
            "amount": 30.0, "payee_name": "c", "category": "Food"
        })
        make_request(f"/payments/{prep1['transaction']['id']}/complete", "POST", headers=self.headers, body={"status": "success"})
        # 4. Online Transport: 40.0 (Success)
        status, prep2 = make_request("/payments/prepare", "POST", headers=self.headers, body={
            "amount": 40.0, "payee_name": "d", "category": "Transport"
        })
        make_request(f"/payments/{prep2['transaction']['id']}/complete", "POST", headers=self.headers, body={"status": "success"})
        # 5. Online Lodging: 50.0 (Pending/uncompleted - should not count in stats)
        make_request("/payments/prepare", "POST", headers=self.headers, body={
            "amount": 50.0, "payee_name": "e", "category": "Lodging"
        })

        make_request(f"/trips/{trip_id}/complete", "POST", headers=self.headers)

        status, details = make_request(f"/trips/{trip_id}", "GET", headers=self.headers)
        self.assertEqual(details["total_spend"], 100.0) # 10 + 20 + 30 + 40
        self.assertEqual(details["cash_spend"], 30.0)
        self.assertEqual(details["card_online_spend"], 70.0)

        # Breakdown should have:
        # Transport: 60.0 (60.0%)
        # Food: 40.0 (40.0%)
        bd = details["category_breakdown"]
        self.assertEqual(len(bd), 2)
        self.assertEqual(bd[0]["category"], "Transport")
        self.assertEqual(bd[0]["percentage"], 60.0)
        self.assertEqual(bd[1]["category"], "Food")
        self.assertEqual(bd[1]["percentage"], 40.0)

if __name__ == "__main__":
    unittest.main()
