import { useEffect, useState } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { ArrowLeft, Users, Globe, Eye, MessageCircle, File, Check, LogIn, Calendar } from 'lucide-react';
import { Meetup, Membership } from '@/api/entities';
import { useAuthStore } from '../stores/authStore';
import { format } from 'date-fns';
import { ko } from 'date-fns/locale';
export default function MeetupDetail() {
  const { slug } = useParams();
  const navigate = useNavigate();
  const user = useAuthStore((s) => s.user);
  const [meetup, setMeetup] = useState(null);
  const [members, setMembers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [notFound, setNotFound] = useState(false);
  const [joining, setJoining] = useState(false);
  const load = async () => {
    setLoading(true);
    try {
      const res = await Meetup.paging({ page: 1, limit: 1, filter: { slug } });
      const m = res.data?.data?.[0];
      if (!m) { setNotFound(true); return; }
      setMeetup(m);
      const memRes = await Membership.paging({ page: 1, limit: 50, filter: { meetupId: m.id } });
      setMembers(memRes.data?.data || []);
    } catch (e) {
      console.error(e);
      setNotFound(true);
    } finally {
      setLoading(false);
    }
  };
  useEffect(() => { load(); /* eslint-disable-next-line */ }, [slug]);
  const isMember = !!user && members.some((m) => String(m.userId) === String(user.id));
  const handleJoin = async () => {
    if (!user) { navigate('/login'); return; }
    setJoining(true);
    try {
      await Membership.create({
        meetupId: meetup.id,
        meetupName: meetup.name,
        userId: user.id,
        userName: user.name,
        userAvatar: user.avatar || '',
        status: 'approved',
        role: 'member',
      });
      await Meetup.update(meetup.id, { memberCount: (meetup.memberCount || 0) + 1 });
      await load();
    } catch (e) {
      console.error(e);
    } finally {
      setJoining(false);
    }
  };
  const handleLeave = async () => {
    const my = members.find((m) => String(m.userId) === String(user.id));
    if (!my) return;
    if (my.role === 'owner') { alert('모임장은 탈퇴할 수 없어요. 모임 관리에서 처리해 주세요.'); return; }
    setJoining(true);
    try {
      await Membership.delete(my.id);
      await Meetup.update(meetup.id, { memberCount: Math.max(0, (meetup.memberCount || 1) - 1) });
      await load();
    } catch (e) {
      console.error(e);
    } finally {
      setJoining(false);
    }
  };
  if (loading) {
    return (
      <div className="max-w-4xl mx-auto px-4 py-10 animate-pulse">
        <div className="h-60 bg-slate-200 rounded-3xl" />
        <div className="h-8 bg-slate-200 rounded w-1/2 mt-6" />
        <div className="h-4 bg-slate-100 rounded w-full mt-4" />
      </div>
    );
  }
  if (notFound) {
    return (
      <div className="max-w-2xl mx-auto px-4 py-24 text-center">
        <div className="text-5xl mb-4">🌊</div>
        <h2 className="text-xl font-bold text-slate-700">모임을 찾을 수 없어요</h2>
        <Link to="/meetups" className="inline-block mt-6 px-6 py-3 rounded-xl bg-[#5B9BD5] text-white font-semibold">모임 목록으로</Link>
      </div>
    );
  }
  return (
    <div className="w-full max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <button onClick={() => navigate(-1)} className="flex items-center gap-1 text-slate-500 hover:text-[#5B9BD5] mb-5">
        <ArrowLeft className="w-4 h-4" /> 뒤로
      </button>
      <div className="rounded-3xl overflow-hidden shadow-lg shadow-blue-100/40 border border-slate-100 bg-white">
        <div className="h-56 md:h-72 bg-gradient-to-br from-[#A8C8F0] to-[#5B9BD5] relative">
          {meetup.coverImage ? (
            <img src={meetup.coverImage} alt={meetup.name} className="w-full h-full object-cover" />
          ) : (
            <div className="w-full h-full flex items-center justify-center text-7xl">🌊</div>
          )}
        </div>
        <div className="p-6 md:p-8">
          <div className="flex flex-wrap items-center gap-2">
            <span className="px-3 py-1 rounded-full bg-[#EAF3FB] text-[#34618C] text-xs font-bold">{meetup.categoryName}</span>
            <span className="px-3 py-1 rounded-full bg-slate-50 text-slate-500 text-xs font-semibold flex items-center gap-1">
              {meetup.isPublic ? <Globe className="w-3 h-3" /> : <Eye className="w-3 h-3" />}
              {meetup.isPublic ? '공개 모임' : '비공개 모임'}
            </span>
          </div>
          <h1 className="mt-4 text-3xl md:text-4xl font-bold text-slate-800">{meetup.name}</h1>
          <div className="mt-3 flex flex-wrap items-center gap-4 text-sm text-slate-500">
            <span className="flex items-center gap-1.5"><Users className="w-4 h-4 text-[#5B9BD5]" />{meetup.memberCount || 0} / {meetup.maxMembers || '∞'}명</span>
            <span>모임장 · {meetup.ownerName}</span>
            {meetup.created_at && (
              <span className="flex items-center gap-1.5"><Calendar className="w-4 h-4" />{format(new Date(meetup.created_at), 'yyyy.MM.dd', { locale: ko })} 개설</span>
            )}
          </div>
          <p className="mt-5 text-slate-600 leading-relaxed whitespace-pre-wrap">{meetup.description}</p>
          <div className="mt-7 flex flex-col sm:flex-row gap-3">
            {!user ? (
              <button onClick={() => navigate('/login')} className="flex-1 py-3.5 rounded-2xl bg-[#5B9BD5] text-white font-semibold hover:bg-[#4a8ac4] active:scale-95 transition-all flex items-center justify-center gap-2">
                <LogIn className="w-5 h-5" /> 로그인하고 가입하기
              </button>
            ) : isMember ? (
              <button onClick={handleLeave} disabled={joining} className="py-3.5 px-6 rounded-2xl bg-white text-slate-500 font-semibold border border-slate-200 hover:bg-slate-50 transition-all flex items-center justify-center gap-2 disabled:opacity-60">
                <Check className="w-5 h-5 text-green-500" /> 참여 중 (탈퇴)
              </button>
            ) : (
              <button onClick={handleJoin} disabled={joining} className="flex-1 py-3.5 rounded-2xl bg-[#5B9BD5] text-white font-semibold hover:bg-[#4a8ac4] active:scale-95 transition-all disabled:opacity-60">
                {joining ? '처리 중...' : '가입하기'}
              </button>
            )}
            <Link to={`/chat/${meetup.id}`} className="py-3.5 px-6 rounded-2xl bg-[#E08A5B] text-white font-semibold hover:bg-[#d07a4b] active:scale-95 transition-all flex items-center justify-center gap-2">
              <MessageCircle className="w-5 h-5" /> 채팅 입장
            </Link>
            <Link to={`/meetups/${meetup.slug}/documents`} className="py-3.5 px-6 rounded-2xl bg-white text-[#34618C] font-semibold border border-[#5B9BD5] hover:bg-[#EAF3FB] active:scale-95 transition-all flex items-center justify-center gap-2">
              <File className="w-5 h-5" /> 문서 보기
            </Link>
          </div>
        </div>
      </div>
      <div className="mt-8 bg-white rounded-3xl shadow-lg shadow-blue-100/40 border border-slate-100 p-6 md:p-8">
        <h2 className="font-hand text-3xl text-[#5B9BD5]">함께하는 멤버</h2>
        {members.length === 0 ? (
          <p className="text-slate-400 mt-3 text-sm">아직 멤버가 없어요. 첫 멤버가 되어보세요!</p>
        ) : (
          <div className="mt-5 grid grid-cols-2 sm:grid-cols-3 gap-3">
            {members.map((m) => (
              <div key={m.id} className="flex items-center gap-3 p-3 rounded-2xl bg-slate-50">
                <img src={m.userAvatar || `https://api.dicebear.com/7.x/avataaars/svg?seed=${m.userName}`} alt={m.userName} className="w-10 h-10 rounded-full bg-white" />
                <div className="min-w-0">
                  <p className="text-sm font-semibold text-slate-700 truncate">{m.userName}</p>
                  <p className="text-xs text-slate-400">{m.role === 'owner' ? '모임장' : '멤버'}</p>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}