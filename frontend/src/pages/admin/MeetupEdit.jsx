import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { ArrowLeft, Save, Globe, Eye } from 'lucide-react';
import { Meetup, Category } from '@/api/entities';
export default function MeetupEdit() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [categories, setCategories] = useState([]);
  const [form, setForm] = useState(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState('');
  useEffect(() => {
    let mounted = true;
    (async () => {
      setLoading(true);
      try {
        const [cRes, mRes] = await Promise.all([
          Category.paging({ page: 1, limit: 20, sort: 'id' }),
          Meetup.get(id),
        ]);
        if (!mounted) return;
        setCategories(cRes.data?.data || []);
        const m = mRes.data;
        setForm({
          name: m.name || '', description: m.description || '', categoryId: m.categoryId || '',
          maxMembers: m.maxMembers || 20, isPublic: (m.isPublic === true || m.isPublic === 'true'), memberCount: m.memberCount || 0,
        });
      } catch (e) {
        console.error(e);
        setError('모임 정보를 불러오지 못했어요.');
      } finally {
        if (mounted) setLoading(false);
      }
    })();
    return () => { mounted = false; };
  }, [id]);
  const handleSave = async () => {
    setError('');
    if (!form.name.trim()) { setError('모임 이름을 입력해 주세요.'); return; }
    setSaving(true);
    try {
      const cat = categories.find((c) => String(c.id) === String(form.categoryId));
      await Meetup.update(id, {
        name: form.name.trim(),
        description: form.description.trim(),
        categoryId: Number(form.categoryId),
        categoryName: cat?.name || '',
        maxMembers: Number(form.maxMembers) || 20,
        isPublic: form.isPublic,
      });
      navigate('/admin/meetups');
    } catch (e) {
      console.error(e);
      setError('저장에 실패했어요.');
    } finally {
      setSaving(false);
    }
  };
  if (loading || !form) {
    return <div className="max-w-2xl"><div className="h-10 bg-white rounded-xl animate-pulse" /></div>;
  }
  return (
    <div className="max-w-2xl">
      <button onClick={() => navigate('/admin/meetups')} className="flex items-center gap-1 text-slate-500 hover:text-[#5B9BD5] mb-5"><ArrowLeft className="w-4 h-4" /> 목록으로</button>
      <h2 className="text-2xl font-bold text-slate-800">모임 수정</h2>
      <div className="mt-5 bg-white rounded-2xl border border-slate-100 shadow-sm p-6 space-y-5" role="form" aria-label="모임 수정">
        <div>
          <label className="block text-sm font-semibold text-slate-700 mb-2">모임 이름</label>
          <input value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} className="w-full px-4 py-3 rounded-xl border border-slate-200 focus:border-[#5B9BD5] focus:ring-2 focus:ring-[#5B9BD5]/20 outline-none" />
        </div>
        <div>
          <label className="block text-sm font-semibold text-slate-700 mb-2">설명</label>
          <textarea value={form.description} onChange={(e) => setForm({ ...form, description: e.target.value })} rows={4} className="w-full px-4 py-3 rounded-xl border border-slate-200 focus:border-[#5B9BD5] focus:ring-2 focus:ring-[#5B9BD5]/20 outline-none resize-none" />
        </div>
        <div>
          <label className="block text-sm font-semibold text-slate-700 mb-2">카테고리</label>
          <select value={form.categoryId} onChange={(e) => setForm({ ...form, categoryId: e.target.value })} className="w-full px-4 py-3 rounded-xl border border-slate-200 focus:border-[#5B9BD5] outline-none bg-white">
            {categories.map((c) => <option key={c.id} value={c.id}>{c.name}</option>)}
          </select>
        </div>
        <div>
          <label className="block text-sm font-semibold text-slate-700 mb-2">최대 인원</label>
          <input type="number" value={form.maxMembers} onChange={(e) => setForm({ ...form, maxMembers: e.target.value })} className="w-32 px-4 py-3 rounded-xl border border-slate-200 focus:border-[#5B9BD5] outline-none" />
        </div>
        <div>
          <label className="block text-sm font-semibold text-slate-700 mb-2">공개 여부</label>
          <div className="flex gap-3">
            <button type="button" onClick={() => setForm({ ...form, isPublic: true })} className={`flex-1 px-4 py-3 rounded-xl border font-semibold flex items-center justify-center gap-2 ${form.isPublic ? 'bg-[#EAF3FB] border-[#5B9BD5] text-[#34618C]' : 'border-slate-200 text-slate-500'}`}><Globe className="w-4 h-4" /> 공개</button>
            <button type="button" onClick={() => setForm({ ...form, isPublic: false })} className={`flex-1 px-4 py-3 rounded-xl border font-semibold flex items-center justify-center gap-2 ${!form.isPublic ? 'bg-[#FBF0E9] border-[#E08A5B] text-[#b56636]' : 'border-slate-200 text-slate-500'}`}><Eye className="w-4 h-4" /> 비공개</button>
          </div>
        </div>
        {error && <div className="p-3 rounded-xl bg-red-50 text-red-600 text-sm">{error}</div>}
        <button onClick={handleSave} disabled={saving} className="w-full py-3.5 rounded-xl bg-[#5B9BD5] text-white font-semibold hover:bg-[#4a8ac4] active:scale-95 transition-all disabled:opacity-60 flex items-center justify-center gap-2">
          <Save className="w-5 h-5" /> {saving ? '저장 중...' : '저장하기'}
        </button>
      </div>
    </div>
  );
}