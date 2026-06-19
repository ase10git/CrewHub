import { useEffect, useState, useRef } from 'react';
import { Search } from 'lucide-react';
import { User } from '@/api/entities';
import { format } from 'date-fns';
import { ko } from 'date-fns/locale';
export default function MembersList() {
  const [data, setData] = useState([]);
  const [page, setPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(false);
  const [searchInput, setSearchInput] = useState('');
  const [search, setSearch] = useState('');
  const isComposingRef = useRef(false);
  useEffect(() => {
    if (isComposingRef.current) return;
    const t = setTimeout(() => setSearch(searchInput), 300);
    return () => clearTimeout(t);
  }, [searchInput]);
  const fetchData = async (pageNum) => {
    setLoading(true);
    try {
      const res = await User.paging({ page: pageNum, limit: 10, filter: { search }, sort: '-created_at' });
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
  const toggleActive = async (u) => {
    const current = (u.isActive === true || u.isActive === 'true');
    try {
      await User.update(u.id, { isActive: !current });
      setData((prev) => prev.map((x) => x.id === u.id ? { ...x, isActive: !current } : x));
    } catch (e) {
      console.error(e);
    }
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
      <h2 className="text-2xl font-bold text-slate-800">회원 관리</h2>
      <p className="text-slate-500 mt-1">전체 회원 {total}명</p>
      <div className="relative mt-5 max-w-md">
        <Search className="absolute left-3.5 top-1/2 -translate-y-1/2 w-5 h-5 text-slate-400" />
        <input
          value={searchInput}
          onChange={(e) => setSearchInput(e.target.value)}
          onCompositionStart={() => { isComposingRef.current = true; }}
          onCompositionEnd={(e) => { isComposingRef.current = false; setSearchInput(e.currentTarget.value); }}
          onKeyDown={(e) => { if (e.key === 'Enter' && !e.nativeEvent.isComposing && !isComposingRef.current) setSearch(searchInput); }}
          placeholder="이름·이메일 검색"
          className="w-full pl-11 pr-4 py-3 rounded-xl border border-slate-200 focus:border-[#5B9BD5] focus:ring-2 focus:ring-[#5B9BD5]/20 outline-none bg-white"
        />
      </div>
      <div className="mt-5 bg-white rounded-2xl border border-slate-100 shadow-sm overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full text-sm">
            <thead className="bg-slate-50 text-slate-500">
              <tr>
                <th className="text-left px-5 py-3 font-semibold">회원</th>
                <th className="text-left px-5 py-3 font-semibold hidden md:table-cell">권한</th>
                <th className="text-left px-5 py-3 font-semibold hidden md:table-cell">가입일</th>
                <th className="text-left px-5 py-3 font-semibold">상태</th>
                <th className="text-right px-5 py-3 font-semibold">관리</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-100">
              {loading ? (
                [...Array(5)].map((_, i) => <tr key={i}><td colSpan={5} className="px-5 py-4"><div className="h-8 bg-slate-50 rounded animate-pulse" /></td></tr>)
              ) : data.length === 0 ? (
                <tr><td colSpan={5} className="px-5 py-12 text-center text-slate-400">회원이 없어요.</td></tr>
              ) : (
                data.map((u) => {
                  const active = (u.isActive === true || u.isActive === 'true');
                  return (
                    <tr key={u.id} className="hover:bg-slate-50">
                      <td className="px-5 py-3">
                        <div className="flex items-center gap-3">
                          <img src={u.avatar || `https://api.dicebear.com/7.x/avataaars/svg?seed=${u.email}`} alt={u.name} className="w-9 h-9 rounded-full bg-slate-100" />
                          <div className="min-w-0">
                            <p className="font-semibold text-slate-700 truncate">{u.name}</p>
                            <p className="text-xs text-slate-400 truncate">{u.email}</p>
                          </div>
                        </div>
                      </td>
                      <td className="px-5 py-3 hidden md:table-cell">
                        <span className={`px-2.5 py-1 rounded-full text-xs font-semibold ${u.role === 'admin' ? 'bg-[#EAF3FB] text-[#34618C]' : 'bg-slate-100 text-slate-500'}`}>{u.role === 'admin' ? '관리자' : '회원'}</span>
                      </td>
                      <td className="px-5 py-3 hidden md:table-cell text-slate-500">{u.created_at ? format(new Date(u.created_at), 'yyyy.MM.dd', { locale: ko }) : '-'}</td>
                      <td className="px-5 py-3">
                        <span className={`px-2.5 py-1 rounded-full text-xs font-semibold ${active ? 'bg-green-50 text-green-600' : 'bg-red-50 text-red-500'}`}>{active ? '활성' : '비활성'}</span>
                      </td>
                      <td className="px-5 py-3 text-right">
                        <button onClick={() => toggleActive(u)} disabled={u.role === 'admin'} className={`px-3 py-1.5 rounded-lg text-xs font-semibold transition-colors disabled:opacity-40 ${active ? 'bg-red-50 text-red-600 hover:bg-red-100' : 'bg-green-50 text-green-600 hover:bg-green-100'}`}>
                          {active ? '비활성화' : '활성화'}
                        </button>
                      </td>
                    </tr>
                  );
                })
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
    </div>
  );
}