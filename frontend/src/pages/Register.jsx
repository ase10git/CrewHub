import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { Mail, Eye, EyeOff, User, Check } from 'lucide-react';
import { Auth } from '@/api/entities';
import { useAuthStore } from '../stores/authStore';
import { connectTalkflow } from '../lib/talkflow';
const LOGO = 'https://cdn.vibe-x.app/apps/71212311046d33c35ec0f71a/assets/original/logo-0-53405.png';
const validatePassword = (pw) => {
  if (pw.length < 8) return 'min_length';
  if (!/[0-9]/.test(pw)) return 'no_digit';
  if (!/[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/.test(pw)) return 'no_special';
  return null;
};
export default function Register() {
  const navigate = useNavigate();
  const setUser = useAuthStore((s) => s.setUser);
  const [form, setForm] = useState({ name: '', email: '', password: '', passwordConfirm: '' });
  const [touched, setTouched] = useState({});
  const [showPw, setShowPw] = useState(false);
  const [loading, setLoading] = useState(false);
  const [serverError, setServerError] = useState('');
  const errors = {
    name: form.name.trim().length < 2 ? '이름은 2자 이상 입력해 주세요.' : '',
    email: !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.email) ? '올바른 이메일 형식이 아니에요.' : '',
    password: form.password && validatePassword(form.password) ? '비밀번호는 8자 이상이며 숫자와 특수문자를 포함해야 합니다.' : '',
    passwordConfirm: form.passwordConfirm && form.password !== form.passwordConfirm ? '비밀번호가 일치하지 않습니다.' : '',
  };
  const isValid = form.name.trim().length >= 2 && /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.email) && !validatePassword(form.password) && form.password === form.passwordConfirm;
  const handleRegister = async () => {
    setTouched({ name: true, email: true, password: true, passwordConfirm: true });
    setServerError('');
    if (!isValid) return;
    setLoading(true);
    try {
      const res = await Auth.register({ email: form.email.trim(), password: form.password, name: form.name.trim() });
      const data = res.data.data || res.data;
      const token = data.token;
      const user = data.user;
      if (token) localStorage.setItem('access_token', token);
      if (user) { localStorage.setItem('user', JSON.stringify(user)); setUser(user); }
      try { if (user) await connectTalkflow(user.id, user.name, user.avatar); } catch (e) { /* ignore */ }
      navigate('/');
    } catch (e) {
      console.error(e);
      setServerError('회원가입에 실패했어요. 이미 가입된 이메일일 수 있어요.');
    } finally {
      setLoading(false);
    }
  };
  return (
    <div className="w-full min-h-[calc(100vh-160px)] flex items-center justify-center px-4 py-12 bg-gradient-to-b from-[#EAF3FB] to-white">
      <div className="w-full max-w-md">
        <div className="text-center mb-7">
          <Link to="/"><img src={LOGO} alt="어울림" className="h-11 w-auto object-contain mx-auto" /></Link>
          <p className="font-hand text-3xl text-[#5B9BD5] mt-3">어울림에 오신 걸 환영해요</p>
        </div>
        <div className="bg-white rounded-3xl shadow-lg shadow-blue-100/40 border border-slate-100 p-7 md:p-8">
          <h1 className="text-2xl font-bold text-slate-800">회원가입</h1>
          <div role="form" aria-label="회원가입" className="mt-6 space-y-4">
            <div>
              <label className="block text-sm font-semibold text-slate-700 mb-2">닉네임</label>
              <div className="relative">
                <User className="absolute left-3.5 top-1/2 -translate-y-1/2 w-5 h-5 text-slate-400" />
                <input value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} onBlur={() => setTouched({ ...touched, name: true })} placeholder="사용할 닉네임" className={`w-full pl-11 pr-4 py-3 rounded-xl border outline-none focus:ring-2 focus:ring-[#5B9BD5]/20 ${touched.name && errors.name ? 'border-red-500' : 'border-slate-200 focus:border-[#5B9BD5]'}`} />
              </div>
              {touched.name && errors.name && <p className="text-xs text-red-500 mt-1.5">{errors.name}</p>}
            </div>
            <div>
              <label className="block text-sm font-semibold text-slate-700 mb-2">이메일</label>
              <div className="relative">
                <Mail className="absolute left-3.5 top-1/2 -translate-y-1/2 w-5 h-5 text-slate-400" />
                <input value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} onBlur={() => setTouched({ ...touched, email: true })} type="email" placeholder="이메일 주소" className={`w-full pl-11 pr-4 py-3 rounded-xl border outline-none focus:ring-2 focus:ring-[#5B9BD5]/20 ${touched.email && errors.email ? 'border-red-500' : 'border-slate-200 focus:border-[#5B9BD5]'}`} />
              </div>
              {touched.email && errors.email && <p className="text-xs text-red-500 mt-1.5">{errors.email}</p>}
            </div>
            <div>
              <label className="block text-sm font-semibold text-slate-700 mb-2">비밀번호</label>
              <div className="relative">
                <input value={form.password} onChange={(e) => setForm({ ...form, password: e.target.value })} onBlur={() => setTouched({ ...touched, password: true })} type={showPw ? 'text' : 'password'} placeholder="8자 이상, 숫자/특수문자 포함" className={`w-full pl-4 pr-11 py-3 rounded-xl border outline-none focus:ring-2 focus:ring-[#5B9BD5]/20 ${touched.password && errors.password ? 'border-red-500' : 'border-slate-200 focus:border-[#5B9BD5]'}`} />
                <button type="button" onClick={() => setShowPw((v) => !v)} className="absolute right-3.5 top-1/2 -translate-y-1/2 text-slate-400">{showPw ? <EyeOff className="w-5 h-5" /> : <Eye className="w-5 h-5" />}</button>
              </div>
              {touched.password && errors.password && <p className="text-xs text-red-500 mt-1.5">{errors.password}</p>}
            </div>
            <div>
              <label className="block text-sm font-semibold text-slate-700 mb-2">비밀번호 확인</label>
              <input value={form.passwordConfirm} onChange={(e) => setForm({ ...form, passwordConfirm: e.target.value })} onBlur={() => setTouched({ ...touched, passwordConfirm: true })} onKeyDown={(e) => { if (e.key === 'Enter' && !e.nativeEvent.isComposing) handleRegister(); }} type={showPw ? 'text' : 'password'} placeholder="비밀번호를 다시 입력해 주세요" className={`w-full px-4 py-3 rounded-xl border outline-none focus:ring-2 focus:ring-[#5B9BD5]/20 ${touched.passwordConfirm && errors.passwordConfirm ? 'border-red-500' : 'border-slate-200 focus:border-[#5B9BD5]'}`} />
              {touched.passwordConfirm && errors.passwordConfirm && <p className="text-xs text-red-500 mt-1.5">{errors.passwordConfirm}</p>}
            </div>
            {serverError && <div className="p-3 rounded-xl bg-red-50 text-red-600 text-sm">{serverError}</div>}
            <button onClick={handleRegister} disabled={!isValid || loading} className="w-full py-3.5 rounded-2xl bg-[#5B9BD5] text-white font-semibold hover:bg-[#4a8ac4] active:scale-95 transition-all disabled:opacity-50 flex items-center justify-center gap-2">
              <Check className="w-5 h-5" /> {loading ? '가입 중...' : '회원가입'}
            </button>
          </div>
          <p className="text-center text-sm text-slate-500 mt-6">
            이미 회원이신가요? <Link to="/login" className="text-[#5B9BD5] font-semibold hover:underline">로그인</Link>
          </p>
        </div>
      </div>
    </div>
  );
}