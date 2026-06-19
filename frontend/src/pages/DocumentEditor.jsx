import { useEffect, useState } from 'react';
import { useParams, useSearchParams, useNavigate } from 'react-router-dom';
import { ArrowLeft, Save } from 'lucide-react';
import { Document, Meetup } from '@/api/entities';
import { useAuthStore } from '../stores/authStore';
export default function DocumentEditor() {
  const { id } = useParams();
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const user = useAuthStore((s) => s.user);
  const isEdit = !!id;
  const meetupIdParam = searchParams.get('meetupId');
  const [meetup, setMeetup] = useState(null);
  const [form, setForm] = useState({ title: '', content: '' });
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState('');
  useEffect(() => {
    if (!user) { navigate('/login'); return; }
    let mounted = true;
    (async () => {
      setLoading(true);
      try {
        if (isEdit) {
          const res = await Document.get(id);
          const d = res.data;
          if (!mounted) return;
          if (!d) { setError('문서를 찾을 수 없어요.'); return; }
          if (String(d.authorId) !== String(user.id)) {
            navigate(`/documents/${id}`);
            return;
          }
          setForm({ title: d.title || '', content: d.content || '' });
          if (d.meetupId) {
            try { const m = await Meetup.get(d.meetupId); if (mounted) setMeetup(m.data); } catch (e) { /* ignore */ }
          }
        } else if (meetupIdParam) {
          const m = await Meetup.get(meetupIdParam);
          if (mounted) setMeetup(m.data);
        }
      } catch (e) {
        console.error(e);
      } finally {
        if (mounted) setLoading(false);
      }
    })();
    return () => { mounted = false; };
    // eslint-disable-next-line
  }, [id]);
  const handleSubmit = async () => {
    setError('');
    if (!form.title.trim()) { setError('제목을 입력해 주세요.'); return; }
    if (!form.content.trim()) { setError('내용을 입력해 주세요.'); return; }
    setSaving(true);
    try {
      if (isEdit) {
        await Document.update(id, { title: form.title.trim(), content: form.content.trim() });
        navigate(`/documents/${id}`);
      } else {
        const created = await Document.create({
          title: form.title.trim(),
          content: form.content.trim(),
          meetupId: meetup ? Number(meetup.id) : (meetupIdParam ? Number(meetupIdParam) : null),
          meetupName: meetup?.name || '',
          authorId: user.id,
          authorName: user.name,
        });
        navigate(`/documents/${created.data.id}`);
      }
    } catch (e) {
      console.error(e);
      setError('저장에 실패했어요. 다시 시도해 주세요.');
    } finally {
      setSaving(false);
    }
  };
  if (loading) {
    return <div className="max-w-3xl mx-auto px-4 py-10"><div className="h-10 bg-slate-100 rounded animate-pulse" /></div>;
  }
  return (
    <div className="w-full max-w-3xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <button onClick={() => navigate(-1)} className="flex items-center gap-1 text-slate-500 hover:text-[#5B9BD5] mb-5">
        <ArrowLeft className="w-4 h-4" /> 뒤로
      </button>
      <div className="bg-white rounded-3xl shadow-lg shadow-blue-100/40 border border-slate-100 p-6 md:p-8">
        <h1 className="font-hand text-4xl text-[#5B9BD5]">{isEdit ? '문서 수정' : '새 문서 작성'}</h1>
        {meetup && <p className="text-slate-500 mt-1">{meetup.name}</p>}
        <div role="form" aria-label="문서 작성" className="mt-6 space-y-5">
          <div>
            <label className="block text-sm font-semibold text-slate-700 mb-2">제목</label>
            <input
              value={form.title}
              onChange={(e) => setForm({ ...form, title: e.target.value })}
              onKeyDown={(e) => { if (e.key === 'Enter' && !e.nativeEvent.isComposing) document.getElementById('doc-body')?.focus(); }}
              placeholder="문서 제목을 입력하세요"
              className="w-full px-4 py-3 rounded-xl border border-slate-200 focus:border-[#5B9BD5] focus:ring-2 focus:ring-[#5B9BD5]/20 outline-none text-lg font-semibold"
            />
          </div>
          <div>
            <label className="block text-sm font-semibold text-slate-700 mb-2">내용</label>
            <textarea
              id="doc-body"
              value={form.content}
              onChange={(e) => setForm({ ...form, content: e.target.value })}
              rows={14}
              placeholder="내용을 자유롭게 작성하세요. 줄바꿈도 그대로 저장됩니다."
              className="w-full px-4 py-3 rounded-xl border border-slate-200 focus:border-[#5B9BD5] focus:ring-2 focus:ring-[#5B9BD5]/20 outline-none leading-relaxed resize-y"
            />
          </div>
          {error && <div className="p-3 rounded-xl bg-red-50 text-red-600 text-sm">{error}</div>}
          <div className="flex gap-3">
            <button onClick={() => navigate(-1)} className="px-6 py-3.5 rounded-2xl bg-slate-100 text-slate-600 font-semibold">취소</button>
            <button onClick={handleSubmit} disabled={saving} className="flex-1 py-3.5 rounded-2xl bg-[#5B9BD5] text-white font-semibold hover:bg-[#4a8ac4] active:scale-95 transition-all disabled:opacity-60 flex items-center justify-center gap-2">
              <Save className="w-5 h-5" /> {saving ? '저장 중...' : '저장하기'}
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}