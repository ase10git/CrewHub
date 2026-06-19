import { useState } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { Menu, X, User, LogOut, LogIn } from 'lucide-react';
import { useAuthStore } from '../stores/authStore';
const LOGO = 'https://cdn.vibe-x.app/apps/71212311046d33c35ec0f71a/assets/original/logo-0-53405.png';
export default function Navbar() {
  const [open, setOpen] = useState(false);
  const [menu, setMenu] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();
  const user = useAuthStore((s) => s.user);
  const logout = useAuthStore((s) => s.logout);
  const isActive = (path) => location.pathname === path || (path === '/meetups' && location.pathname.startsWith('/meetups'));
  const handleLogout = () => {
    logout();
    setMenu(false);
    setOpen(false);
    navigate('/');
  };
  return (
    <header className="sticky top-0 z-40 bg-white/90 backdrop-blur-md border-b border-slate-100">
      <nav className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 h-16 flex items-center justify-between">
        <Link to="/" className="flex items-center" onClick={() => setOpen(false)}>
          <img src={LOGO} alt="어울림" className="h-9 w-auto object-contain" />
        </Link>
        <div className="hidden md:flex items-center gap-8">
          <Link to="/" className={`text-[15px] font-medium transition-colors ${isActive('/') ? 'text-[#5B9BD5]' : 'text-slate-600 hover:text-[#5B9BD5]'}`}>홈</Link>
          <Link to="/meetups" className={`text-[15px] font-medium transition-colors ${isActive('/meetups') ? 'text-[#5B9BD5]' : 'text-slate-600 hover:text-[#5B9BD5]'}`}>모임</Link>
          {user ? (
            <div className="relative">
              <button onClick={() => setMenu((v) => !v)} className="flex items-center gap-2 pl-2 pr-3 py-1.5 rounded-full hover:bg-slate-50 transition-colors">
                <img src={user.avatar || `https://api.dicebear.com/7.x/avataaars/svg?seed=${user.email}`} alt={user.name} className="w-8 h-8 rounded-full bg-slate-100" />
                <span className="text-sm font-medium text-slate-700">{user.name}</span>
              </button>
              {menu && (
                <div className="absolute right-0 mt-2 w-44 bg-white rounded-2xl shadow-xl border border-slate-100 py-2">
                  <Link to="/mypage" onClick={() => setMenu(false)} className="flex items-center gap-2 px-4 py-2.5 text-sm text-slate-700 hover:bg-slate-50">
                    <User className="w-4 h-4" /> 마이페이지
                  </Link>
                  <button onClick={handleLogout} className="w-full flex items-center gap-2 px-4 py-2.5 text-sm text-slate-700 hover:bg-slate-50">
                    <LogOut className="w-4 h-4" /> 로그아웃
                  </button>
                </div>
              )}
            </div>
          ) : (
            <div className="flex items-center gap-3">
              <Link to="/login" className="text-[15px] font-medium text-slate-600 hover:text-[#5B9BD5]">로그인</Link>
              <Link to="/register" className="px-5 py-2 rounded-full bg-[#5B9BD5] text-white text-sm font-semibold hover:bg-[#4a8ac4] transition-colors">회원가입</Link>
            </div>
          )}
        </div>
        <button className="md:hidden p-2 text-slate-700" onClick={() => setOpen((v) => !v)} aria-label="메뉴">
          {open ? <X className="w-6 h-6" /> : <Menu className="w-6 h-6" />}
        </button>
      </nav>
      {open && (
        <div className="md:hidden border-t border-slate-100 bg-white px-4 py-4 space-y-1">
          <Link to="/" onClick={() => setOpen(false)} className="block px-3 py-2.5 rounded-xl text-slate-700 hover:bg-slate-50 font-medium">홈</Link>
          <Link to="/meetups" onClick={() => setOpen(false)} className="block px-3 py-2.5 rounded-xl text-slate-700 hover:bg-slate-50 font-medium">모임</Link>
          {user ? (
            <>
              <Link to="/mypage" onClick={() => setOpen(false)} className="block px-3 py-2.5 rounded-xl text-slate-700 hover:bg-slate-50 font-medium">마이페이지</Link>
              <button onClick={handleLogout} className="w-full text-left px-3 py-2.5 rounded-xl text-slate-700 hover:bg-slate-50 font-medium">로그아웃</button>
            </>
          ) : (
            <div className="flex gap-2 pt-2">
              <Link to="/login" onClick={() => setOpen(false)} className="flex-1 text-center px-4 py-2.5 rounded-xl border border-[#5B9BD5] text-[#5B9BD5] font-semibold flex items-center justify-center gap-1"><LogIn className="w-4 h-4" />로그인</Link>
              <Link to="/register" onClick={() => setOpen(false)} className="flex-1 text-center px-4 py-2.5 rounded-xl bg-[#5B9BD5] text-white font-semibold">회원가입</Link>
            </div>
          )}
        </div>
      )}
    </header>
  );
}