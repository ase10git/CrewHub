import { useEffect, useState, useRef } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Search, Edit, Trash2, Users } from 'lucide-react';
import { Meetup } from '@/api/entities';
export default function MeetupsList() {
  const navigate = useNavigate();
  const [data, setData] = useState([]);
  const [page, setPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(false);
  const [searchInput, setSearchInput] = useState('');
  const [search, setSearch] = useState('');
  const [delTarget, setDelTarget] = useState(null);
  const isComposingRef = useRef(false);
  useEffect(() => {
    if (isComposingRef.current) return;
    const t = setTimeout(() => setSearch(searchInput), 300);
    return () => clearTimeout(t);
  }, [searchInput]);
  const fetchData = async (pageNum) => {
    setLoading(true);
    try {
      const res = await Meetup.paging({ page: pageNum, limit: 10, filter: { search }, sort: '-created_at' });
      setData(res.data?.data || []);
      setTotal(res.data?.total || 0);
      setTotalPages(res.data?.totalPages || 1);
      setPage(pageNum);
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };
  useEffect(() => { fetchData(1); /* eslint-disable-next-line */ }, [search]);
  const handleDelete = async () => {
    if (!delTarget) return;
    try {
      await Meetup.delete(delTarget.id);
      setDelTarget(null);
      fetchData(page);
    } catch (e) { console.error(e); }
  };
  const getPages = () => {
    const pages = [];
    const left = Math.max(2, page - 2);
    const right = Math.min(totalPages - 1, page + 2);
    pages.push(1);
    if (left > 2) pages.push('...');
    for (let i = left; i <= right; i++) pages.push(i);
    if (right < totalPages - 1) pages.push('...');
    if (totalPages > 1) pages.push(totalPages);
    return pages;
  };
  return (
    <div>
      <h2 className="text-2xl font-bold text-slate-800">모임 관리</h2>
      <p className="text-slate-500 mt-1">전체 모임 {total}개</p>
      <div className="relative mt-5 max-w-md">
        <Search className="absolute left-3.5 top-1/2 -translate-y-1/2 w-5 h-5 text-slate-400" />
        <input
          value={searchInput}
          onChange={(e) => setSearchInput(e.target.value)}
          onCompositionStart={() => { isComposingRef.current = true; }}
          onCompositionEnd={(e) => { isComposingRef.current = false; setSearchInput(e.currentTarget.value); }}
          onKeyDown={(e) => { if (e.key === 'Enter' && !e.nativeEvent.isComposing && !isComposingRef.current) setSearch(searchInput); }}
          placeholder="모임 이름 검색"
          className="w-full pl-11 pr-4 py-3 rounded-xl border border-slate-200 focus:border-[#5B9BD5] focus:ring-2 focus:ring-[#5B9BD5]/20 outline-none bg-white"
        />
      </div>
      <div className="mt-5 bg-white rounded-2xl border border-slate-100 shadow-sm overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full text-sm">
            <thead className="bg-slate-50 text-slate-500">
              <tr>
                <th className="text-left px-5 py-3 font-semibold">모임</th>
                <th className="text-left px-5 py-3 font-semibold hidden md:table-cell">카테고리</th>
                <th className="text-left px-5 py-3 font-semibold hidden sm:table-cell">멤버</th>
                <th className="text-left px-5 py-3 font-semibold hidden md:table-cell">모임장</th>
                <th className="text-right px-5 py-3 font-semibold">관리</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-100">
              {loading ? (
                [...Array(5)].map((_, i) => <tr key={i}><td colSpan={5} className="px-5 py-4"><div className="h-8 bg-slate-50 rounded animate-pulse" /></td></tr>)
              ) : data.length === 0 ? (
                <tr><td colSpan={5} className="px-5 py-12 text-center text-slate-400">모임이 없어요.</td></tr>
              ) : (
                data.map((m) => (
                  <tr key={m.id} className="hover:bg-slate-50">
                    <td className="px-5 py-3">
                      <Link to={`/meetups/${m.slug}`} className="font-semibold text-slate-700 hover:text-[#5B9BD5]">{m.name}</Link>
                    </td>
                    <td className="px-5 py-3 hidden md:table-cell"><span className="px-2.5 py-1 rounded-full bg-[#EAF3FB] text-[#34618C] text-xs font-semibold">{m.categoryName}</span></td>
                    <td className="px-5 py-3 hidden sm:table-cell text-slate-500"><span className="flex items-center gap-1"><Users className="w-4 h-4" />{m.memberCount || 0}</span></td>
                    <td className="px-5 py-3 hidden md:table-cell text-slate-500">{m.ownerName || '-'}</td>
                    <td className="px-5 py-3 text-right">
                      <div className="flex items-center justify-end gap-2">
                        <button onClick={() => navigate(`/admin/meetups/${m.id}/edit`)} className="p-2 rounded-lg text-slate-500 hover:bg-slate-100"><Edit className="w-4 h-4" /></button>
                        <button onClick={() => setDelTarget(m)} className="p-2 rounded-lg text-red-500 hover:bg-red-50"><Trash2 className="w-4 h-4" /></button>
                      </div>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>
      {totalPages > 1 && (
        <div className="mt-5 flex items-center justify-center gap-1">
          <button onClick={() => fetchData(page - 1)} disabled={page <= 1 || loading} className="px-3 py-2 rounded-lg text-sm text-slate-600 hover:bg-slate-100 disabled:opacity-40">이전</button>
          {getPages().map((p, i) => p === '...' ? <span key={i} className="px-2 text-slate-400">...</span> : (
            <button key={i} onClick={() => fetchData(p)} disabled={loading} className={`w-9 h-9 rounded-lg text-sm font-semibold ${p === page ? 'bg-[#5B9BD5] text-white' : 'text-slate-600 hover:bg-slate-100'}`}>{p}</button>
          ))}
          <button onClick={() => fetchData(page + 1)} disabled={page >= totalPages || loading} className="px-3 py-2 rounded-lg text-sm text-slate-600 hover:bg-slate-100 disabled:opacity-40">다음</button>
        </div>
      )}
      {delTarget && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40 px-4" onClick={() => setDelTarget(null)}>
          <div className="bg-white rounded-2xl p-6 max-w-sm w-full" onClick={(e) => e.stopPropagation()}>
            <h3 className="text-lg font-bold text-slate-800">모임을 삭제할까요?</h3>
            <p className="text-sm text-slate-500 mt-2">"{delTarget.name}" 모임을 삭제합니다. 복구할 수 없어요.</p>
            <div className="mt-6 flex gap-3">
              <button onClick={() => setDelTarget(null)} className="flex-1 py-3 rounded-xl bg-slate-100 text-slate-600 font-semibold">취소</button>
              <button onClick={handleDelete} className="flex-1 py-3 rounded-xl bg-red-600 text-white font-semibold">삭제</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}