import { useEffect, useState, useRef } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Search, Plus } from 'lucide-react';
import { Meetup, Category } from '@/api/entities';
import MeetupCard from '@/components/MeetupCard';
import WaveDivider from '@/components/WaveDivider';
import { useAuthStore } from '../stores/authStore';
export default function Meetups() {
  const navigate = useNavigate();
  const user = useAuthStore((s) => s.user);
  const [categories, setCategories] = useState([]);
  const [activeCat, setActiveCat] = useState('all');
  const [searchInput, setSearchInput] = useState('');
  const [search, setSearch] = useState('');
  const isComposingRef = useRef(false);
  const [data, setData] = useState([]);
  const [page, setPage] = useState(1);
  const [loading, setLoading] = useState(false);
  const [hasMore, setHasMore] = useState(true);
  const loaderRef = useRef(null);
  useEffect(() => {
    Category.paging({ page: 1, limit: 20, sort: 'id' }).then((res) => setCategories(res.data?.data || [])).catch(() => {});
  }, []);
  useEffect(() => {
    if (isComposingRef.current) return;
    const t = setTimeout(() => setSearch(searchInput), 300);
    return () => clearTimeout(t);
  }, [searchInput]);
  const fetchData = async (pageNum, reset = false) => {
    setLoading(true);
    try {
      const filter = { search };
      if (activeCat !== 'all') filter.categoryId = activeCat;
      const res = await Meetup.paging({ page: pageNum, limit: 9, filter, sort: '-created_at' });
      const rows = res.data?.data || [];
      setData((prev) => (reset ? rows : [...prev, ...rows]));
      setHasMore(pageNum < (res.data?.totalPages || 1));
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };
  useEffect(() => {
    if (page === 1) return;
    fetchData(page);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [page]);
  useEffect(() => {
    setData([]);
    setHasMore(true);
    if (page === 1) fetchData(1, true);
    else setPage(1);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [search, activeCat]);
  useEffect(() => {
    if (!loaderRef.current || !hasMore) return;
    const obs = new IntersectionObserver((entries) => {
      if (entries[0].isIntersecting && !loading) setPage((p) => p + 1);
    });
    obs.observe(loaderRef.current);
    return () => obs.disconnect();
  }, [hasMore, loading]);
  const handleCreate = () => {
    if (!user) { navigate('/login'); return; }
    navigate('/meetups/new');
  };
  return (
    <div className="w-full">
      <section className="w-full bg-gradient-to-b from-[#EAF3FB] to-[#F4F9FD] relative overflow-hidden">
        <div className="absolute -top-16 -right-10 w-72 h-72 rounded-full bg-[#A8C8F0]/30 blur-3xl" />
        <div className="relative max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 pt-10 md:pt-14 pb-8">
          <p className="font-hand text-3xl text-[#5B9BD5]">관심사로 만나는</p>
          <h1 className="mt-1 text-3xl md:text-4xl font-bold text-slate-800">모임 둘러보기</h1>
          <p className="mt-3 text-slate-500">마음에 드는 모임을 찾아 가입하거나, 직접 새로운 모임을 만들어 보세요.</p>
          <div className="mt-6 flex flex-col sm:flex-row gap-3">
            <div className="relative flex-1">
              <Search className="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-slate-400" />
              <input
                value={searchInput}
                onChange={(e) => setSearchInput(e.target.value)}
                onCompositionStart={() => { isComposingRef.current = true; }}
                onCompositionEnd={(e) => { isComposingRef.current = false; setSearchInput(e.currentTarget.value); }}
                onKeyDown={(e) => { if (e.key === 'Enter' && !e.nativeEvent.isComposing && !isComposingRef.current) setSearch(searchInput); }}
                placeholder="모임 이름이나 설명으로 검색"
                className="w-full pl-12 pr-4 py-3.5 rounded-2xl border border-slate-200 bg-white focus:border-[#5B9BD5] focus:ring-2 focus:ring-[#5B9BD5]/20 outline-none"
              />
            </div>
            <button onClick={handleCreate} className="px-6 py-3.5 rounded-2xl bg-[#E08A5B] text-white font-semibold hover:bg-[#d07a4b] active:scale-95 transition-all flex items-center justify-center gap-2 whitespace-nowrap">
              <Plus className="w-5 h-5" /> 모임 만들기
            </button>
          </div>
          <div className="mt-5 flex gap-2 overflow-x-auto pb-2 -mx-4 px-4 sm:mx-0 sm:px-0">
            <button onClick={() => setActiveCat('all')} className={`px-4 py-2 rounded-full text-sm font-semibold whitespace-nowrap transition-colors ${activeCat === 'all' ? 'bg-[#5B9BD5] text-white' : 'bg-white text-slate-600 border border-slate-200 hover:border-[#5B9BD5]'}`}>전체</button>
            {categories.map((c) => (
              <button key={c.id} onClick={() => setActiveCat(c.id)} className={`px-4 py-2 rounded-full text-sm font-semibold whitespace-nowrap transition-colors ${String(activeCat) === String(c.id) ? 'bg-[#5B9BD5] text-white' : 'bg-white text-slate-600 border border-slate-200 hover:border-[#5B9BD5]'}`}>
                {c.icon} {c.name}
              </button>
            ))}
          </div>
        </div>
        <WaveDivider color="#A8C8F0" />
      </section>
      <section className="w-full max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10 md:py-12">
        {loading && data.length === 0 ? (
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
            {[...Array(6)].map((_, i) => (
              <div key={i} className="bg-white rounded-3xl overflow-hidden border border-slate-100 animate-pulse">
                <div className="h-44 bg-slate-200" />
                <div className="p-5 space-y-3">
                  <div className="h-5 bg-slate-200 rounded w-3/4" />
                  <div className="h-4 bg-slate-100 rounded w-full" />
                </div>
              </div>
            ))}
          </div>
        ) : data.length === 0 ? (
          <div className="text-center py-20">
            <div className="text-5xl mb-4">🌊</div>
            <h3 className="text-lg font-semibold text-slate-700">조건에 맞는 모임이 없어요</h3>
            <p className="text-slate-400 mt-2">검색어나 카테고리를 바꾸거나, 직접 모임을 만들어 보세요.</p>
          </div>
        ) : (
          <>
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
              {data.map((m) => <MeetupCard key={m.id} meetup={m} />)}
            </div>
            {hasMore && (
              <div ref={loaderRef} className="py-10 flex justify-center">
                <div className="w-7 h-7 border-3 border-slate-200 border-t-[#5B9BD5] rounded-full animate-spin" style={{ borderWidth: '3px' }} />
              </div>
            )}
          </>
        )}
      </section>
    </div>
  );
}