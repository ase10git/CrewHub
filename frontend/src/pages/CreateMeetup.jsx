import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { ArrowLeft, Plus, Image as ImageIcon, Globe, Eye } from 'lucide-react';
import { Meetup, Category, Membership } from '@/api/entities';
import { vibex } from '@/api/vibexClient';
import { useAuthStore } from '../stores/authStore';
export default function CreateMeetup() {
  const navigate = useNavigate();
  const user = useAuthStore((s) => s.user);
  const [categories, setCategories] = useState([]);
  const [form, setForm] = useState({ name: '', description: '', categoryId: '', maxMembers: 20, isPublic: true, coverImage: '' });
  const [uploading, setUploading] = useState(false);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState('');
  useEffect(() => {
    if (!user) { navigate('/login'); return; }
    Category.paging({ page: 1, limit: 20, sort: 'id' }).then((res) => {
      const cats = res.data?.data || [];
      setCategories(cats);
      if (cats[0]) setForm((f) => ({ ...f, categoryId: cats[0].id }));
    }).catch(() => {});
  }, [user, navigate]);
  const handleUpload = async (e) => {
    const file = e.target.files?.[0];
    if (!file) return;
    setUploading(true);
    try {
      const res = await vibex.integrations.Core.UploadFile({ file, folder: 'meetups' });
      const url = res?.data?.file_url;
      if (url) setForm((f) => ({ ...f, coverImage: url }));
    } catch (err) {
      console.error(err);
    } finally {
      setUploading(false);
    }
  };
  const slugify = () => 'm-' + Math.random().toString(36).slice(2, 8) + Date.now().toString(36).slice(-3);
  const handleSubmit = async () => {
    setError('');
    if (!form.name.trim()) { setError('모임 이름을 입력해 주세요.'); return; }
    if (!form.categoryId) { setError('카테고리를 선택해 주세요.'); return; }
    setSaving(true);
    try {
      const cat = categories.find((c) => String(c.id) === String(form.categoryId));
      const slug = slugify();
      const created = await Meetup.create({
        name: form.name.trim(),
        slug,
        description: form.description.trim(),
        categoryId: Number(form.categoryId),
        categoryName: cat?.name || '',
        maxMembers: Number(form.maxMembers) || 20,
        memberCount: 1,
        isPublic: form.isPublic,
        coverImage: form.coverImage || '',
        ownerId: user.id,
        ownerName: user.name,
      });
      const meetup = created.data;
      await Membership.create({
        meetupId: meetup.id,
        meetupName: meetup.name,
        userId: user.id,
        userName: user.name,
        userAvatar: user.avatar || '',
        status: 'approved',
        role: 'owner',
      });
      navigate(`/meetups/${slug}`);
    } catch (err) {
      console.error(err);
      setError('모임 생성에 실패했어요. 다시 시도해 주세요.');
    } finally {
      setSaving(false);
    }
  };
  return (
    <div className="w-full max-w-2xl mx-auto px-4 sm:px-6 lg:px-8 py-10">
      <button onClick={() => navigate(-1)} className="flex items-center gap-1 text-slate-500 hover:text-[#5B9BD5] mb-6">
        <ArrowLeft className="w-4 h-4" /> 뒤로
      </button>
      <div className="bg-white rounded-3xl shadow-lg shadow-blue-100/40 border border-slate-100 p-6 md:p-8">
        <h1 className="font-hand text-4xl text-[#5B9BD5]">새 모임 만들기</h1>
        <p className="text-slate-500 mt-1">함께할 사람들을 위한 모임을 만들어 보세요.</p>
        <div role="form" aria-label="모임 생성" className="mt-7 space-y-5">
          <div>
            <label className="block text-sm font-semibold text-slate-700 mb-2">모임 이름 *</label>
            <input value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} placeholder="예) 토익 만점 도전 스터디" className="w-full px-4 py-3 rounded-xl border border-slate-200 focus:border-[#5B9BD5] focus:ring-2 focus:ring-[#5B9BD5]/20 outline-none" />
          </div>
          <div>
            <label className="block text-sm font-semibold text-slate-700 mb-2">모임 설명</label>
            <textarea value={form.description} onChange={(e) => setForm({ ...form, description: e.target.value })} rows={4} placeholder="어떤 모임인지, 어떤 활동을 하는지 소개해 주세요." className="w-full px-4 py-3 rounded-xl border border-slate-200 focus:border-[#5B9BD5] focus:ring-2 focus:ring-[#5B9BD5]/20 outline-none resize-none" />
          </div>
          <div>
            <label className="block text-sm font-semibold text-slate-700 mb-2">카테고리 *</label>
            <div className="flex flex-wrap gap-2">
              {categories.map((c) => (
                <button key={c.id} type="button" onClick={() => setForm({ ...form, categoryId: c.id })} className={`px-4 py-2 rounded-full text-sm font-semibold transition-colors ${String(form.categoryId) === String(c.id) ? 'bg-[#5B9BD5] text-white' : 'bg-slate-50 text-slate-600 border border-slate-200 hover:border-[#5B9BD5]'}`}>
                  {c.icon} {c.name}
                </button>
              ))}
            </div>
          </div>
          <div>
            <label className="block text-sm font-semibold text-slate-700 mb-2">최대 인원</label>
            <input type="number" min={2} max={500} value={form.maxMembers} onChange={(e) => setForm({ ...form, maxMembers: e.target.value })} className="w-32 px-4 py-3 rounded-xl border border-slate-200 focus:border-[#5B9BD5] focus:ring-2 focus:ring-[#5B9BD5]/20 outline-none" />
          </div>
          <div>
            <label className="block text-sm font-semibold text-slate-700 mb-2">공개 여부</label>
            <div className="flex gap-3">
              <button type="button" onClick={() => setForm({ ...form, isPublic: true })} className={`flex-1 px-4 py-3 rounded-xl border font-semibold flex items-center justify-center gap-2 transition-colors ${form.isPublic ? 'bg-[#EAF3FB] border-[#5B9BD5] text-[#34618C]' : 'border-slate-200 text-slate-500'}`}>
                <Globe className="w-4 h-4" /> 공개
              </button>
              <button type="button" onClick={() => setForm({ ...form, isPublic: false })} className={`flex-1 px-4 py-3 rounded-xl border font-semibold flex items-center justify-center gap-2 transition-colors ${!form.isPublic ? 'bg-[#FBF0E9] border-[#E08A5B] text-[#b56636]' : 'border-slate-200 text-slate-500'}`}>
                <Eye className="w-4 h-4" /> 비공개
              </button>
            </div>
          </div>
          <div>
            <label className="block text-sm font-semibold text-slate-700 mb-2">대표 이미지</label>
            {form.coverImage ? (
              <div className="relative rounded-xl overflow-hidden">
                <img src={form.coverImage} alt="대표 이미지" className="w-full h-44 object-cover" />
                <button type="button" onClick={() => setForm({ ...form, coverImage: '' })} className="absolute top-2 right-2 px-3 py-1 rounded-full bg-black/50 text-white text-xs">변경</button>
              </div>
            ) : (
              <label className="flex flex-col items-center justify-center gap-2 h-32 rounded-xl border-2 border-dashed border-slate-200 cursor-pointer hover:border-[#5B9BD5] transition-colors">
                <ImageIcon className="w-7 h-7 text-slate-400" />
                <span className="text-sm text-slate-400">{uploading ? '업로드 중...' : '이미지를 선택하세요'}</span>
                <input type="file" accept="image/*" className="hidden" onChange={handleUpload} />
              </label>
            )}
          </div>
          {error && <div className="p-3 rounded-xl bg-red-50 text-red-600 text-sm">{error}</div>}
          <button onClick={handleSubmit} disabled={saving} className="w-full py-4 rounded-2xl bg-[#5B9BD5] text-white text-lg font-semibold hover:bg-[#4a8ac4] active:scale-95 transition-all disabled:opacity-60 flex items-center justify-center gap-2">
            <Plus className="w-5 h-5" /> {saving ? '만드는 중...' : '모임 만들기'}
          </button>
        </div>
      </div>
    </div>
  );
}