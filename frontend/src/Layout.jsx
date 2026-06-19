import { Outlet, useLocation } from 'react-router-dom';
import { useEffect } from 'react';
import Navbar from '@/components/Navbar';
import Footer from '@/components/Footer';
import { useAuthStore } from './stores/authStore';
import { connectTalkflow } from './lib/talkflow';
export default function Layout({ currentPageName }) {
  const fetchMe = useAuthStore((s) => s.fetchMe);
  const user = useAuthStore((s) => s.user);
  const location = useLocation();
  useEffect(() => { fetchMe(); }, [fetchMe]);
  useEffect(() => {
    if (user?.id) {
      connectTalkflow(user.id, user.name || user.email, user.avatar).catch(() => {});
    }
  }, [user?.id]);
  useEffect(() => {
    window.scrollTo(0, 0);
  }, [location.pathname]);
  return (
    <div className="min-h-screen flex flex-col overflow-x-hidden bg-[#FAFBFD] text-slate-800">
      <Navbar currentPage={currentPageName} />
      <main className="flex-1 flex flex-col">
        <Outlet />
      </main>
      <Footer />
    </div>
  );
}