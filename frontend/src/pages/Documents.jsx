import { useEffect, useState, useRef } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { ArrowLeft, Search, Plus, File, Calendar, User } from 'lucide-react';
import { Meetup, Document, Membership } from '@/api/entities';
import { useAuthStore } from '../stores/authStore';
import { format } from 'date-fns';
import { ko } from 'date-fns/locale';
export default function Documents() {
  const { slug } = useParams();
  const navigate = useNavigate();
  const user = useAuthStore((s) => s.user);
  const [meetup, setMeetup] = useState(null);
  const [isMember, setIsMember] = useState(false);
  const [docs, setDocs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchInput, setSearchInput] = useState('');
  const [search, setSearch] = useState('');
  const isComposingRef = useRef(false);
  useEffect(() => {
    if (isComposingRef.current) return;
    const t = setTimeout(() => setSearch(searchInput), 300);
    return () => clearTimeout(t);
  }, [searchInput]);
  useEffect(() => {
    let mounted = true;
    (async () => {
      try {
        const res = await Meetup.paging({ page: 1, limit: 1, filter: { slug } });
        const m = res.data?.data?.[0];
        if (!mounted) return;
        if (!m) { setMeetup(null); setLoading(false); return; }
        setMeetup(m);
        if (user) {
          const mem = await Membership.paging({ page: 1, limit: 5, filter: { meetupId: m.id, userId: user.id } });
          setIsMember((mem.data?.data || []).length > 0);
        }
      } catch (e) { console.error(e); }
    })();
    return () => { mounted = false; };
    // eslint-disable-next-line
  }, [slug, user]);
  useEffect(() => {
    if (!meetup) return;
    let mounted = true;
    (async () => {
      setLoading(true);
      try {
        const res = await Document.paging({ page: 1, limit: 50, filter: { meetupId: meetup.id, search }, sort: '-created_at' });
        if (mounted) setDocs(res.data?.data || []);
      } catch (e) { console.error(e); }
      finally { if (mounted) setLoading(false); }
    })();
    return () => { mounted = false; };
  }, [meetup, search]);
  if (!loading && !meetup) {
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
      <button onClick={() => navigate(meetup ? `/meetups/${meetup.slug}` : '/meetups')} className="flex items-center gap-1 text-slate-500 hover:text-[#5B9BD5] mb-5">
        <ArrowLeft className="w-4 h-4" /> 모임으로
      </button>
      <div className="flex flex-col sm:flex-row sm:items-end sm:justify-between gap-3">
        <div>
          <p className="font-hand text-3xl text-[#5B9BD5]">{meetup?.name}</p>
          <h1 className="mt-1 text-2xl md:text-3xl font-bold text-slate-800">문서 보관함</h1>
        </div>
        {isMember && (
          <Link to={`/documents/new?meetupId=${meetup.id}`} className="px-5 py-3 rounded-2xl bg-[#E08A5B] text-white font-semibold hover:bg-[#d07a4b] active:scale-95 transition-all flex items-center justify-center gap-2 whitespace-nowrap">
            <Plus className="w-5 h-5" /> 새 문서 작성
          </Link>
        )}
      </div>
      <div className="relative mt-6">
        <Search className="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-slate-400" />
        <input
          value={searchInput}
          onChange={(e) => setSearchInput(e.target.value)}
          onCompositionStart={() => { isComposingRef.current = true; }}
          onCompositionEnd={(e) => { isComposingRef.current = false; setSearchInput(e.currentTarget.value); }}
          onKeyDown={(e) => { if (e.key === 'Enter' && !e.nativeEvent.isComposing && !isComposingRef.current) setSearch(searchInput); }}
          placeholder="제목·작성자로 검색"
          className="w-full pl-12 pr-4 py-3.5 rounded-2xl border border-slate-200 bg-white focus:border-[#5B9BD5] focus:ring-2 focus:ring-[#5B9BD5]/20 outline-none"
        />
      </div>
      {!isMember && user && (
        <p className="mt-4 text-sm text-[#b56636] bg-[#FBF0E9] rounded-xl px-4 py-3">모임에 가입하면 문서를 작성할 수 있어요.</p>
      )}
      <div className="mt-6 space-y-3">
        {loading ? (
          [...Array(4)].map((_, i) => (
            <div key={i} className="bg-white rounded-2xl border border-slate-100 p-5 animate-pulse">
              <div className="h-5 bg-slate-200 rounded w-1/2" />
              <div className="h-4 bg-slate-100 rounded w-1/3 mt-3" />
            </div>
          ))
        ) : docs.length === 0 ? (
          <div className="text-center py-16">
            <File className="w-12 h-12 text-slate-300 mx-auto mb-3" />
            <h3 className="text-lg font-semibold text-slate-700">아직 문서가 없어요</h3>
            <p className="text-slate-400 mt-2">{isMember ? '첫 문서를 작성해 보세요!' : '모임 멤버가 문서를 작성하면 여기에 표시돼요.'}</p>
          </div>
        ) : (
          docs.map((d) => (
            <Link key={d.id} to={`/documents/${d.id}`} className="block bg-white rounded-2xl border border-slate-100 shadow-sm hover:shadow-md hover:border-[#5B9BD5]/40 p-5 transition-all">
              <h3 className="text-lg font-bold text-slate-800">{d.title}</h3>
              <p className="text-sm text-slate-500 mt-1.5 line-clamp-2">{d.content}</p>
              <div className="flex items-center gap-4 mt-3 text-xs text-slate-400">
                <span className="flex items-center gap-1"><User className="w-3.5 h-3.5" />{d.authorName}</span>
                {d.created_at && <span className="flex items-center gap-1"><Calendar className="w-3.5 h-3.5" />{format(new Date(d.created_at), 'yyyy.MM.dd', { locale: ko })}</span>}
              </div>
            </Link>
          ))
        )}
      </div>
    </div>
  );
}