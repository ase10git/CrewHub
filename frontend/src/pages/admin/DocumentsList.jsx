import { useEffect, useState, useRef } from 'react';
import { Search, Trash2, Eye, X } from 'lucide-react';
import { Document } from '@/api/entities';
import { format } from 'date-fns';
import { ko } from 'date-fns/locale';
export default function DocumentsList() {
  const [data, setData] = useState([]);
  const [page, setPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(false);
  const [searchInput, setSearchInput] = useState('');
  const [search, setSearch] = useState('');
  const [delTarget, setDelTarget] = useState(null);
  const [preview, setPreview] = useState(null);
  const isComposingRef = useRef(false);
  useEffect(() => {
    if (isComposingRef.current) return;
    const t = setTimeout(() => setSearch(searchInput), 300);
    return () => clearTimeout(t);
  }, [searchInput]);
  const fetchData = async (pageNum) => {
    setLoading(true);
    try {
      const res = await Document.paging({ page: pageNum, limit: 10, filter: { search }, sort: '-created_at' });
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
      await Document.delete(delTarget.id);
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
      <h2 className="text-2xl font-bold text-slate-800">문서 관리</h2>
      <p className="text-slate-500 mt-1">전체 문서 {total}건</p>
      <div className="relative mt-5 max-w-md">
        <Search className="absolute left-3.5 top-1/2 -translate-y-1/2 w-5 h-5 text-slate-400" />
        <input
          value={searchInput}
          onChange={(e) => setSearchInput(e.target.value)}
          onCompositionStart={() => { isComposingRef.current = true; }}
          onCompositionEnd={(e) => { isComposingRef.current = false; setSearchInput(e.currentTarget.value); }}
          onKeyDown={(e) => { if (e.key === 'Enter' && !e.nativeEvent.isComposing && !isComposingRef.current) setSearch(searchInput); }}
          placeholder="제목·작성자 검색"
          className="w-full pl-11 pr-4 py-3 rounded-xl border border-slate-200 focus:border-[#5B9BD5] focus:ring-2 focus:ring-[#5B9BD5]/20 outline-none bg-white"
        />
      </div>
      <div className="mt-5 bg-white rounded-2xl border border-slate-100 shadow-sm overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full text-sm">
            <thead className="bg-slate-50 text-slate-500">
              <tr>
                <th className="text-left px-5 py-3 font-semibold">제목</th>
                <th className="text-left px-5 py-3 font-semibold hidden md:table-cell">모임</th>
                <th className="text-left px-5 py-3 font-semibold hidden sm:table-cell">작성자</th>
                <th className="text-left px-5 py-3 font-semibold hidden md:table-cell">작성일</th>
                <th className="text-right px-5 py-3 font-semibold">관리</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-100">
              {loading ? (
                [...Array(5)].map((_, i) => <tr key={i}><td colSpan={5} className="px-5 py-4"><div className="h-8 bg-slate-50 rounded animate-pulse" /></td></tr>)
              ) : data.length === 0 ? (
                <tr><td colSpan={5} className="px-5 py-12 text-center text-slate-400">문서가 없어요.</td></tr>
              ) : (
                data.map((d) => (
                  <tr key={d.id} className="hover:bg-slate-50">
                    <td className="px-5 py-3"><button onClick={() => setPreview(d)} className="font-semibold text-slate-700 hover:text-[#5B9BD5] text-left">{d.title}</button></td>
                    <td className="px-5 py-3 hidden md:table-cell text-slate-500">{d.meetupName || '-'}</td>
                    <td className="px-5 py-3 hidden sm:table-cell text-slate-500">{d.authorName}</td>
                    <td className="px-5 py-3 hidden md:table-cell text-slate-500">{d.created_at ? format(new Date(d.created_at), 'yyyy.MM.dd', { locale: ko }) : '-'}</td>
                    <td className="px-5 py-3 text-right">
                      <div className="flex items-center justify-end gap-2">
                        <button onClick={() => setPreview(d)} className="p-2 rounded-lg text-slate-500 hover:bg-slate-100"><Eye className="w-4 h-4" /></button>
                        <button onClick={() => setDelTarget(d)} className="p-2 rounded-lg text-red-500 hover:bg-red-50"><Trash2 className="w-4 h-4" /></button>
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
      {preview && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40 px-4" onClick={() => setPreview(null)}>
          <div className="bg-white rounded-2xl p-6 max-w-lg w-full max-h-[80vh] overflow-y-auto" onClick={(e) => e.stopPropagation()}>
            <div className="flex items-start justify-between gap-3">
              <h3 className="text-xl font-bold text-slate-800">{preview.title}</h3>
              <button onClick={() => setPreview(null)}><X className="w-5 h-5 text-slate-400" /></button>
            </div>
            <p className="text-xs text-slate-400 mt-1">{preview.authorName} · {preview.meetupName}</p>
            <div className="mt-4 text-slate-700 leading-relaxed whitespace-pre-wrap text-sm">{preview.content}</div>
          </div>
        </div>
      )}
      {delTarget && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40 px-4" onClick={() => setDelTarget(null)}>
          <div className="bg-white rounded-2xl p-6 max-w-sm w-full" onClick={(e) => e.stopPropagation()}>
            <h3 className="text-lg font-bold text-slate-800">문서를 삭제할까요?</h3>
            <p className="text-sm text-slate-500 mt-2">"{delTarget.title}" 문서를 삭제합니다. 복구할 수 없어요.</p>
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