import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Mail, Eye, EyeOff, LogIn } from 'lucide-react';
import { Auth } from '@/api/entities';
import { useAuthStore } from '../../stores/authStore';
const LOGO = 'https://cdn.vibe-x.app/apps/71212311046d33c35ec0f71a/assets/original/logo-0-53405.png';
export default function Login() {
  const navigate = useNavigate();
  const setUser = useAuthStore((s) => s.setUser);
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [showPw, setShowPw] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const handleLogin = async () => {
    setError('');
    if (!email.trim() || !password) { setError('이메일과 비밀번호를 입력해 주세요.'); return; }
    setLoading(true);
    try {
      const res = await Auth.login({ email: email.trim(), password });
      const { token, user } = res.data.data;
      if (user?.role !== 'admin') {
        setError('관리자 계정만 접근할 수 있어요.');
        setLoading(false);
        return;
      }
      localStorage.setItem('access_token', token);
      localStorage.setItem('user', JSON.stringify(user));
      setUser(user);
      navigate('/admin', { replace: true });
    } catch (e) {
      console.error(e);
      setError('이메일 또는 비밀번호가 올바르지 않습니다.');
    } finally {
      setLoading(false);
    }
  };
  return (
    <div className="min-h-screen flex items-center justify-center px-4 bg-slate-900">
      <div className="w-full max-w-sm">
        <div className="text-center mb-7">
          <img src={LOGO} alt="어울림" className="h-10 w-auto object-contain mx-auto" style={{ filter: 'brightness(0) invert(1)' }} />
          <p className="text-slate-400 mt-3">관리자 콘솔 로그인</p>
        </div>
        <div className="bg-white rounded-2xl shadow-2xl p-7" role="form" aria-label="관리자 로그인">
          <div className="space-y-4">
            <div>
              <label className="block text-sm font-semibold text-slate-700 mb-2">이메일</label>
              <div className="relative">
                <Mail className="absolute left-3.5 top-1/2 -translate-y-1/2 w-5 h-5 text-slate-400" />
                <input value={email} onChange={(e) => setEmail(e.target.value)} type="email" placeholder="이메일 주소를 입력해 주세요" className="w-full pl-11 pr-4 py-3 rounded-xl border border-slate-200 focus:border-[#5B9BD5] focus:ring-2 focus:ring-[#5B9BD5]/20 outline-none" />
              </div>
            </div>
            <div>
              <label className="block text-sm font-semibold text-slate-700 mb-2">비밀번호</label>
              <div className="relative">
                <input value={password} onChange={(e) => setPassword(e.target.value)} onKeyDown={(e) => { if (e.key === 'Enter' && !e.nativeEvent.isComposing) handleLogin(); }} type={showPw ? 'text' : 'password'} placeholder="비밀번호를 입력해 주세요" className="w-full pl-4 pr-11 py-3 rounded-xl border border-slate-200 focus:border-[#5B9BD5] focus:ring-2 focus:ring-[#5B9BD5]/20 outline-none" />
                <button type="button" onClick={() => setShowPw((v) => !v)} className="absolute right-3.5 top-1/2 -translate-y-1/2 text-slate-400">{showPw ? <EyeOff className="w-5 h-5" /> : <Eye className="w-5 h-5" />}</button>
              </div>
            </div>
            {error && <div className="p-3 rounded-xl bg-red-50 text-red-600 text-sm">{error}</div>}
            <button onClick={handleLogin} disabled={loading} className="w-full py-3.5 rounded-xl bg-slate-900 text-white font-semibold hover:bg-slate-800 active:scale-95 transition-all disabled:opacity-60 flex items-center justify-center gap-2">
              <LogIn className="w-5 h-5" /> {loading ? '로그인 중...' : '로그인'}
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}