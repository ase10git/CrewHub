import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { Mail, Eye, EyeOff, LogIn } from 'lucide-react';
import { Auth } from '@/api/entities';
import { useAuthStore } from '../stores/authStore';
import { connectTalkflow } from '../lib/talkflow';
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
      localStorage.setItem('access_token', token);
      localStorage.setItem('user', JSON.stringify(user));
      setUser(user);
      try { await connectTalkflow(user.id, user.name || user.email, user.avatar); } catch (e) { /* ignore */ }
      navigate('/');
    } catch (e) {
      console.error(e);
      setError('이메일 또는 비밀번호가 올바르지 않습니다.');
    } finally {
      setLoading(false);
    }
  };
  return (
    <div className="w-full min-h-[calc(100vh-160px)] flex items-center justify-center px-4 py-12 bg-gradient-to-b from-[#EAF3FB] to-white">
      <div className="w-full max-w-md">
        <div className="text-center mb-7">
          <Link to="/"><img src={LOGO} alt="어울림" className="h-11 w-auto object-contain mx-auto" /></Link>
          <p className="font-hand text-3xl text-[#5B9BD5] mt-3">다시 만나서 반가워요</p>
        </div>
        <div className="bg-white rounded-3xl shadow-lg shadow-blue-100/40 border border-slate-100 p-7 md:p-8">
          <h1 className="text-2xl font-bold text-slate-800">로그인</h1>
          <div role="form" aria-label="로그인" className="mt-6 space-y-4">
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
            <button onClick={handleLogin} disabled={loading} className="w-full py-3.5 rounded-2xl bg-[#5B9BD5] text-white font-semibold hover:bg-[#4a8ac4] active:scale-95 transition-all disabled:opacity-60 flex items-center justify-center gap-2">
              <LogIn className="w-5 h-5" /> {loading ? '로그인 중...' : '로그인'}
            </button>
          </div>
          <p className="text-center text-sm text-slate-500 mt-6">
            아직 회원이 아니신가요? <Link to="/register" className="text-[#5B9BD5] font-semibold hover:underline">회원가입</Link>
          </p>
        </div>
      </div>
    </div>
  );
}