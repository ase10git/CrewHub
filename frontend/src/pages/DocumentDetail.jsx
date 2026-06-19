import { useEffect, useState } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { ArrowLeft, Edit, Trash2, Calendar, User } from 'lucide-react';
import { Document, Meetup } from '@/api/entities';
import { useAuthStore } from '../stores/authStore';
import { format } from 'date-fns';
import { ko } from 'date-fns/locale';
export default function DocumentDetail() {
  const { id } = useParams();
  const navigate = useNavigate();
  const user = useAuthStore((s) => s.user);
  const [doc, setDoc] = useState(null);
  const [meetup, setMeetup] = useState(null);
  const [loading, setLoading] = useState(true);
  const [notFound, setNotFound] = useState(false);
  const [confirmDel, setConfirmDel] = useState(false);
  const [deleting, setDeleting] = useState(false);
  useEffect(() => {
    let mounted = true;
    (async () => {
      setLoading(true);
      try {
        const res = await Document.get(id);
        const d = res.data;
        if (!mounted) return;
        if (!d) { setNotFound(true); return; }
        setDoc(d);
        if (d.meetupId) {
          try {
            const mRes = await Meetup.get(d.meetupId);
            if (mounted) setMeetup(mRes.data);
          } catch (e) { /* ignore */ }
        }
      } catch (e) {
        console.error(e);
        setNotFound(true);
      } finally {
        if (mounted) setLoading(false);
      }
    })();
    return () => { mounted = false; };
  }, [id]);
  const isAuthor = !!user && doc && String(doc.authorId) === String(user.id);
  const handleDelete = async () => {
    setDeleting(true);
    try {
      await Document.delete(doc.id);
      navigate(meetup ? `/meetups/${meetup.slug}/documents` : '/meetups');
    } catch (e) {
      console.error(e);
      setDeleting(false);
    }
  };
  if (loading) {
    return (
      <div className="max-w-3xl mx-auto px-4 py-10 animate-pulse">
        <div className="h-8 bg-slate-200 rounded w-2/3" />
        <div className="h-4 bg-slate-100 rounded w-1/3 mt-4" />
        <div className="h-40 bg-slate-100 rounded mt-6" />
      </div>
    );
  }
  if (notFound) {
    return (
      <div className="max-w-2xl mx-auto px-4 py-24 text-center">
        <div className="text-5xl mb-4">📄</div>
        <h2 className="text-xl font-bold text-slate-700">문서를 찾을 수 없어요</h2>
        <Link to="/meetups" className="inline-block mt-6 px-6 py-3 rounded-xl bg-[#5B9BD5] text-white font-semibold">모임 목록으로</Link>
      </div>
    );
  }
  return (
    <div className="w-full max-w-3xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <button onClick={() => navigate(-1)} className="flex items-center gap-1 text-slate-500 hover:text-[#5B9BD5] mb-5">
        <ArrowLeft className="w-4 h-4" /> 뒤로
      </button>
      <article className="bg-white rounded-3xl shadow-lg shadow-blue-100/40 border border-slate-100 p-6 md:p-10">
        {meetup && (
          <Link to={`/meetups/${meetup.slug}`} className="inline-block px-3 py-1 rounded-full bg-[#EAF3FB] text-[#34618C] text-xs font-bold hover:bg-[#dceaf7]">{meetup.name}</Link>
        )}
        <h1 className="mt-4 text-3xl md:text-4xl font-bold text-slate-800 leading-tight">{doc.title}</h1>
        <div className="mt-4 flex flex-wrap items-center gap-4 text-sm text-slate-400 pb-5 border-b border-slate-100">
          <span className="flex items-center gap-1.5"><User className="w-4 h-4" />{doc.authorName}</span>
          {doc.created_at && <span className="flex items-center gap-1.5"><Calendar className="w-4 h-4" />{format(new Date(doc.created_at), 'yyyy년 M월 d일', { locale: ko })}</span>}
        </div>
        <div className="mt-6 text-slate-700 leading-loose whitespace-pre-wrap text-[16px]">{doc.content}</div>
        {isAuthor && (
          <div className="mt-8 pt-6 border-t border-slate-100 flex gap-3">
            <Link to={`/documents/${doc.id}/edit`} className="px-5 py-2.5 rounded-xl bg-[#5B9BD5] text-white font-semibold hover:bg-[#4a8ac4] transition-colors flex items-center gap-2">
              <Edit className="w-4 h-4" /> 수정
            </Link>
            <button onClick={() => setConfirmDel(true)} className="px-5 py-2.5 rounded-xl bg-white text-red-600 font-semibold border border-red-200 hover:bg-red-50 transition-colors flex items-center gap-2">
              <Trash2 className="w-4 h-4" /> 삭제
            </button>
          </div>
        )}
      </article>
      {confirmDel && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40 px-4" onClick={() => setConfirmDel(false)}>
          <div className="bg-white rounded-3xl p-6 max-w-sm w-full" onClick={(e) => e.stopPropagation()}>
            <h3 className="text-lg font-bold text-slate-800">문서를 삭제할까요?</h3>
            <p className="text-sm text-slate-500 mt-2">삭제한 문서는 복구할 수 없어요.</p>
            <div className="mt-6 flex gap-3">
              <button onClick={() => setConfirmDel(false)} className="flex-1 py-3 rounded-xl bg-slate-100 text-slate-600 font-semibold">취소</button>
              <button onClick={handleDelete} disabled={deleting} className="flex-1 py-3 rounded-xl bg-red-600 text-white font-semibold disabled:opacity-60">{deleting ? '삭제 중...' : '삭제'}</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}