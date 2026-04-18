import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest';
import { getCurrentUser, setAuthToken } from './api';

describe('API request function', () => {
  let originalFetch: typeof global.fetch;

  beforeEach(() => {
    originalFetch = global.fetch;
    global.fetch = vi.fn().mockResolvedValue({
      ok: true,
      json: async () => ({ id: 1, name: 'Test User' })
    });
    setAuthToken(null);
  });

  afterEach(() => {
    global.fetch = originalFetch;
    vi.restoreAllMocks();
  });

  it('should ignore localStorage exceptions and proceed without token', async () => {
    // Mock window to have localStorage that throws an error
    const originalLocalStorage = window.localStorage;
    Object.defineProperty(window, 'localStorage', {
      value: {
        getItem: vi.fn().mockImplementation(() => {
          throw new Error('localStorage is disabled or restricted');
        })
      },
      writable: true
    });

    try {
      const user = await getCurrentUser();
      expect(user).toEqual({ id: 1, name: 'Test User' });
      expect(global.fetch).toHaveBeenCalledOnce();

      const fetchArgs = (global.fetch as any).mock.calls[0];
      expect(fetchArgs[1].headers).not.toHaveProperty('Authorization');
    } finally {
      Object.defineProperty(window, 'localStorage', {
        value: originalLocalStorage,
        writable: true
      });
    }
  });

  it('should ignore JSON.parse exceptions and proceed without token', async () => {
    const originalLocalStorage = window.localStorage;
    Object.defineProperty(window, 'localStorage', {
      value: {
        getItem: vi.fn().mockReturnValue('invalid json')
      },
      writable: true
    });

    try {
      const user = await getCurrentUser();
      expect(user).toEqual({ id: 1, name: 'Test User' });
      expect(global.fetch).toHaveBeenCalledOnce();

      const fetchArgs = (global.fetch as any).mock.calls[0];
      expect(fetchArgs[1].headers).not.toHaveProperty('Authorization');
    } finally {
      Object.defineProperty(window, 'localStorage', {
        value: originalLocalStorage,
        writable: true
      });
    }
  });
});
