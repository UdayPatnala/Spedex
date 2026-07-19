#!/usr/bin/env python3
import subprocess
import sys
import time
import urllib.request
import urllib.error

def wait_for_backend(url, timeout=5.0):
    print("Waiting for mock backend to start...")
    start_time = time.time()
    while time.time() - start_time < timeout:
        try:
            with urllib.request.urlopen(url) as res:
                if res.status == 200:
                    print("Mock backend is ready.")
                    return True
        except (urllib.error.URLError, urllib.error.HTTPError):
            pass
        time.sleep(0.2)
    return False

def main():
    # Start mock_backend.py on port 8080 in a background subprocess
    print("Starting mock backend on port 8080...")
    backend_proc = subprocess.Popen(
        [sys.executable, "e2e_tests/mock_backend.py", "8080"],
        stdout=subprocess.DEVNULL,
        stderr=subprocess.DEVNULL
    )

    # Wait for the backend health check to succeed
    health_url = "http://localhost:8080/api/health"
    if not wait_for_backend(health_url):
        print("Error: Mock backend failed to start in time.")
        backend_proc.terminate()
        sys.exit(1)

    # Run test_trips_e2e.py using sys.executable
    print("Running E2E test suite...")
    test_result = subprocess.run([sys.executable, "e2e_tests/test_trips_e2e.py"])

    # Gracefully terminate the mock backend subprocess
    print("Stopping mock backend...")
    backend_proc.terminate()
    try:
        backend_proc.wait(timeout=3.0)
    except subprocess.TimeoutExpired:
        print("Mock backend did not terminate gracefully, force killing...")
        backend_proc.kill()

    # Exit with the return code of the test suite
    print(f"E2E test suite finished with exit code: {test_result.returncode}")
    sys.exit(test_result.returncode)

if __name__ == '__main__':
    main()
