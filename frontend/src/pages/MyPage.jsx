import { useEffect, useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { Users, File, User, Mail, MessageCircle, Calendar } from 'lucide-react';
import { Membership, Document } from '@/api/entities';
import { useAuthStore } from '../stores/authStore';
import MeetupCard from '@/components/MeetupCard';
import { Meetup } from '@/api/entities';
import { format } from 'date-fns';
import { ko } from 'date-fns/locale';
export default function MyPage() {
  const navigate = useNavigate();
  const user = useAuthStore((s) => s.user);
  const initialized = useAuthStore((s) => s.initialized);
  const [tab, setTab] = useState('meetups');
  const [myMeetups, setMyMeetups] = useState([]);
  const [myDocs, setMyDocs] = useState([]);
  const [loading, setLoading] = useState(true);
  useEffect(() => {
    if (initialized && !user) { navigate('/login'); }
  }, [initialized, user, navigate]);
  useEffect(() => {
    if (!user) return;
    let mounted = true;
    (async () => {
      setLoading(true);
      try {
        const memRes = await Membership.paging({ page: 1, limit: 50, filter: { userId: user.id } });
        const memberships = memRes.data?.data || [];
        const meetups = [];
        for (const mem of memberships) {
          try {
            const mRes = await Meetup.get(mem.meetupId);
            if (mRes.data) meetups.push(mRes.data);
          } catch (e) { /* ignore */ }
        }
        const docRes = await Document.paging({ page: 1, limit: 50, filter: { authorId: user.id }, sort: '-created_at' });
        if (!mounted) return;
        setMyMeetups(meetups);
        setMyDocs(docRes.data?.data || []);
      } catch (e) {
        console.error(e);
      } finally {
        if (mounted) setLoading(false);
      }
    })();
    return () => { mounted = false; };
  }, [user]);
  if (!user) {
    return <div className="max-w-md mx-auto px-4 py-24 text-center"><div className="w-8 h-8 border-4 border-slate-200 border-t-[#5B9BD5] rounded-full animate-spin mx-auto" /></div>;
  }
  return (
    <div className="w-full max-w-5xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      {/* profile header */}
      <div className="bg-gradient-to-br from-[#5B9BD5] to-[#34618C] rounded-3xl p-6 md:p-8 text-white relative overflow-hidden">
        <div className="absolute -top-10 -right-10 w-48 h-48 rounded-full bg-white/10 blur-2xl" />
        <div className="relative flex items-center gap-5">
          <img src={user.avatar || `https://api.dicebear.com/7.x/avataaars/svg?seed=${user.email}`} alt={user.name} className="w-20 h-20 rounded-full bg-white/20 border-4 border-white/30" />
          <div className="min-w-0">
            <h1 className="text-2xl md:text-3xl font-bold">{user.name}</h1>
            <p className="text-white/80 text-sm flex items-center gap-1.5 mt-1"><Mail className="w-4 h-4" />{user.email}</p>
            {user.bio && <p className="text-white/70 text-sm mt-1.5">{user.bio}</p>}
          </div>
        </div>
        <div className="relative mt-6 flex gap-6">
          <div><p className="text-2xl font-bold">{myMeetups.length}</p><p className="text-white/70 text-sm">참여 모임</p></div>
          <div><p className="text-2xl font-bold">{myDocs.length}</p><p className="text-white/70 text-sm">작성 문서</p></div>
        </div>
      </div>
      {/* tabs */}
      <div className="mt-6 flex gap-2 border-b border-slate-100">
        {[
          { key: 'meetups', label: '참여 중인 모임', icon: Users },
          { key: 'docs', label: '내 문서', icon: File },
          { key: 'profile', label: '프로필', icon: User },
        ].map((t) => (
          <button key={t.key} onClick={() => setTab(t.key)} className={`px-4 py-3 font-semibold text-sm flex items-center gap-1.5 border-b-2 -mb-px transition-colors ${tab === t.key ? 'border-[#5B9BD5] text-[#5B9BD5]' : 'border-transparent text-slate-400 hover:text-slate-600'}`}>
            <t.icon className="w-4 h-4" /> {t.label}
          </button>
        ))}
      </div>
      <div className="mt-6">
        {loading ? (
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-5">
            {[...Array(2)].map((_, i) => <div key={i} className="h-40 bg-white rounded-3xl border border-slate-100 animate-pulse" />)}
          </div>
        ) : tab === 'meetups' ? (
          myMeetups.length === 0 ? (
            <div className="text-center py-16">
              <Users className="w-12 h-12 text-slate-200 mx-auto mb-3" />
              <p className="text-slate-400">아직 참여 중인 모임이 없어요.</p>
              <Link to="/meetups" className="inline-block mt-5 px-6 py-3 rounded-xl bg-[#5B9BD5] text-white font-semibold">모임 둘러보기</Link>
            </div>
          ) : (
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-5">
              {myMeetups.map((m) => <MeetupCard key={m.id} meetup={m} />)}
            </div>
          )
        ) : tab === 'docs' ? (
          myDocs.length === 0 ? (
            <div className="text-center py-16">
              <File className="w-12 h-12 text-slate-200 mx-auto mb-3" />
              <p className="text-slate-400">아직 작성한 문서가 없어요.</p>
            </div>
          ) : (
            <div className="space-y-3">
              {myDocs.map((d) => (
                <Link key={d.id} to={`/documents/${d.id}`} className="block bg-white rounded-2xl border border-slate-100 shadow-sm hover:shadow-md p-5 transition-all">
                  <div className="flex items-center justify-between gap-3">
                    <h3 className="text-lg font-bold text-slate-800 truncate">{d.title}</h3>
                    {d.meetupName && <span className="px-2.5 py-1 rounded-full bg-[#EAF3FB] text-[#34618C] text-xs font-semibold whitespace-nowrap">{d.meetupName}</span>}
                  </div>
                  <p className="text-sm text-slate-500 mt-1.5 line-clamp-2">{d.content}</p>
                  {d.created_at && <p className="text-xs text-slate-400 mt-2 flex items-center gap-1"><Calendar className="w-3.5 h-3.5" />{format(new Date(d.created_at), 'yyyy.MM.dd', { locale: ko })}</p>}
                </Link>
              ))}
            </div>
          )
        ) : (
          <div className="bg-white rounded-3xl border border-slate-100 shadow-sm p-6 md:p-8 max-w-lg">
            <h2 className="font-hand text-3xl text-[#5B9BD5] mb-5">내 프로필</h2>
            <div className="space-y-4">
              <div>
                <p className="text-xs text-slate-400">닉네임</p>
                <p className="text-slate-800 font-semibold">{user.name}</p>
              </div>
              <div>
                <p className="text-xs text-slate-400">이메일</p>
                <p className="text-slate-800 font-semibold">{user.email}</p>
              </div>
              {user.bio && (
                <div>
                  <p className="text-xs text-slate-400">소개</p>
                  <p className="text-slate-700">{user.bio}</p>
                </div>
              )}
            </div>
          </div>
        )}
      </div>
    </div>
  );
}