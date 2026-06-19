import { create } from 'zustand';
import { vibex } from '@/api/vibexClient';
export const useAuthStore = create((set) => ({
  user: (() => { try { return JSON.parse(localStorage.getItem('user') || 'null'); } catch { return null; } })(),
  initialized: false,
  setUser: (user) => {
    if (user) localStorage.setItem('user', JSON.stringify(user));
    set({ user });
  },
  fetchMe: async () => {
    const token = localStorage.getItem('access_token');
    if (!token) {
      set({ user: null, initialized: true });
      return null;
    }
    try {
      const res = await vibex.auth.me();
      const me = res.data;
      localStorage.setItem('user', JSON.stringify(me));
      set({ user: me, initialized: true });
      return me;
    } catch (e) {
      localStorage.removeItem('access_token');
      localStorage.removeItem('user');
      set({ user: null, initialized: true });
      return null;
    }
  },
  logout: () => {
    localStorage.removeItem('access_token');
    localStorage.removeItem('refresh_token');
    localStorage.removeItem('user');
    set({ user: null });
  },
}));