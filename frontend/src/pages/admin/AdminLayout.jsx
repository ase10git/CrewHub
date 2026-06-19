import { useEffect, useState } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { Home, Users, MessageCircle, File, LogOut, ChevronLeft, Menu } from 'lucide-react';
import { useAuthStore } from '../../stores/authStore';
const LOGO = 'https://cdn.vibe-x.app/apps/71212311046d33c35ec0f71a/assets/original/logo-0-53405.png';
const NAV = [
  { to: '/admin', label: '대시보드', icon: Home, exact: true },
  { to: '/admin/members', label: '회원 관리', icon: Users },
  { to: '/admin/meetups', label: '모임 관리', icon: MessageCircle },
  { to: '/admin/documents', label: '문서 관리', icon: File },
];
export default function AdminLayout({ children }) {
  const navigate = useNavigate();
  const location = useLocation();
  const logout = useAuthStore((s) => s.logout);
  const [isAuth, setIsAuth] = useState(false);
  const [loading, setLoading] = useState(true);
  const [open, setOpen] = useState(false);
  const [admin, setAdmin] = useState(null);
  useEffect(() => {
    const token = localStorage.getItem('access_token');
    if (!token) { navigate('/admin/login', { replace: true }); return; }
    try { setAdmin(JSON.parse(localStorage.getItem('user') || 'null')); } catch (e) { /* ignore */ }
    setIsAuth(true);
    setLoading(false);
  }, [navigate]);
  const handleLogout = () => { logout(); navigate('/admin/login', { replace: true }); };
  const isActive = (item) => item.exact ? location.pathname === '/admin' : location.pathname.startsWith(item.to);
  if (loading || !isAuth) {
    return <div className="fixed inset-0 flex items-center justify-center"><div className="w-8 h-8 border-4 border-slate-200 border-t-slate-800 rounded-full animate-spin" /></div>;
  }
  return (
    <div className="min-h-screen flex bg-slate-50">
      {/* sidebar */}
      <aside className={`fixed lg:static inset-y-0 left-0 z-40 w-64 bg-white border-r border-slate-200 flex flex-col transition-transform ${open ? 'translate-x-0' : '-translate-x-full lg:translate-x-0'}`}>
        <div className="h-16 flex items-center justify-between px-5 border-b border-slate-100">
          <img src={LOGO} alt="어울림" className="h-8 w-auto object-contain" />
          <button className="lg:hidden text-slate-400" onClick={() => setOpen(false)}><ChevronLeft className="w-5 h-5" /></button>
        </div>
        <nav className="flex-1 p-3 space-y-1">
          {NAV.map((item) => (
            <Link key={item.to} to={item.to} onClick={() => setOpen(false)} className={`flex items-center gap-3 px-4 py-3 rounded-xl text-sm font-medium transition-colors ${isActive(item) ? 'bg-[#5B9BD5] text-white' : 'text-slate-600 hover:bg-slate-50'}`}>
              <item.icon className="w-5 h-5" /> {item.label}
            </Link>
          ))}
        </nav>
        <div className="p-3 border-t border-slate-100">
          <div className="flex items-center gap-3 px-2 py-2 mb-2">
            <img src={admin?.avatar || `https://api.dicebear.com/7.x/avataaars/svg?seed=admin`} alt="admin" className="w-9 h-9 rounded-full bg-slate-100" />
            <div className="min-w-0">
              <p className="text-sm font-semibold text-slate-700 truncate">{admin?.name || '관리자'}</p>
              <p className="text-xs text-slate-400 truncate">{admin?.email}</p>
            </div>
          </div>
          <button onClick={handleLogout} className="w-full flex items-center gap-2 px-4 py-2.5 rounded-xl text-sm text-slate-600 hover:bg-slate-50"><LogOut className="w-4 h-4" /> 로그아웃</button>
        </div>
      </aside>
      {open && <div className="fixed inset-0 z-30 bg-black/30 lg:hidden" onClick={() => setOpen(false)} />}
      {/* main */}
      <div className="flex-1 min-w-0 flex flex-col">
        <header className="h-16 bg-white border-b border-slate-200 flex items-center justify-between px-4 lg:px-8 sticky top-0 z-20">
          <button className="lg:hidden text-slate-600" onClick={() => setOpen(true)}><Menu className="w-6 h-6" /></button>
          <h1 className="text-lg font-bold text-slate-800">관리자 콘솔</h1>
          <Link to="/" className="text-sm text-[#5B9BD5] font-medium hover:underline">사이트 보기</Link>
        </header>
        <main className="flex-1 p-4 lg:p-8">{children}</main>
      </div>
    </div>
  );
}