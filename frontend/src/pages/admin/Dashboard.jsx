import { useEffect, useState } from 'react';
import { Users, MessageCircle, File, TrendingUp } from 'lucide-react';
import { User, Meetup, Document, Category } from '@/api/entities';
import { BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer, CartesianGrid } from 'recharts';
import { format } from 'date-fns';
import { ko } from 'date-fns/locale';
export default function Dashboard() {
  const [stats, setStats] = useState({ users: 0, meetups: 0, docs: 0 });
  const [recent, setRecent] = useState([]);
  const [chartData, setChartData] = useState([]);
  const [loading, setLoading] = useState(true);
  useEffect(() => {
    let mounted = true;
    (async () => {
      setLoading(true);
      try {
        const [uRes, mRes, dRes, cRes] = await Promise.all([
          User.paging({ page: 1, limit: 10, sort: '-created_at' }),
          Meetup.paging({ page: 1, limit: 100 }),
          Document.paging({ page: 1, limit: 1 }),
          Category.paging({ page: 1, limit: 20, sort: 'id' }),
        ]);
        if (!mounted) return;
        setStats({ users: uRes.data?.total || 0, meetups: mRes.data?.total || 0, docs: dRes.data?.total || 0 });
        setRecent(uRes.data?.data || []);
        const cats = cRes.data?.data || [];
        const meetups = mRes.data?.data || [];
        const data = cats.map((c) => ({
          name: c.name,
          모임수: meetups.filter((m) => String(m.categoryId) === String(c.id)).length,
        }));
        setChartData(data);
      } catch (e) {
        console.error(e);
      } finally {
        if (mounted) setLoading(false);
      }
    })();
    return () => { mounted = false; };
  }, []);
  const cards = [
    { label: '전체 회원', value: stats.users, icon: Users, color: '#5B9BD5', bg: '#EAF3FB' },
    { label: '운영 중인 모임', value: stats.meetups, icon: MessageCircle, color: '#E08A5B', bg: '#FBF0E9' },
    { label: '보관된 문서', value: stats.docs, icon: File, color: '#34618C', bg: '#E5EDF5' },
  ];
  return (
    <div>
      <h2 className="text-2xl font-bold text-slate-800">대시보드</h2>
      <p className="text-slate-500 mt-1">어울림 커뮤니티 현황을 한눈에 확인하세요.</p>
      <div className="mt-6 grid grid-cols-1 sm:grid-cols-3 gap-4">
        {cards.map((c) => (
          <div key={c.label} className="bg-white rounded-2xl border border-slate-100 shadow-sm p-6 flex items-center gap-4">
            <div className="w-14 h-14 rounded-2xl flex items-center justify-center" style={{ background: c.bg }}>
              <c.icon className="w-7 h-7" style={{ color: c.color }} />
            </div>
            <div>
              <p className="text-sm text-slate-400">{c.label}</p>
              <p className="text-3xl font-bold text-slate-800">{loading ? '...' : c.value.toLocaleString('ko-KR')}</p>
            </div>
          </div>
        ))}
      </div>
      <div className="mt-6 grid grid-cols-1 lg:grid-cols-2 gap-6">
        <div className="bg-white rounded-2xl border border-slate-100 shadow-sm p-6">
          <h3 className="font-bold text-slate-800 flex items-center gap-2"><TrendingUp className="w-5 h-5 text-[#5B9BD5]" /> 카테고리별 모임 수</h3>
          <div className="mt-4 h-64">
            <ResponsiveContainer width="100%" height="100%">
              <BarChart data={chartData}>
                <CartesianGrid strokeDasharray="3 3" stroke="#f1f5f9" />
                <XAxis dataKey="name" tick={{ fontSize: 12, fill: '#94a3b8' }} />
                <YAxis allowDecimals={false} tick={{ fontSize: 12, fill: '#94a3b8' }} />
                <Tooltip />
                <Bar dataKey="모임수" fill="#5B9BD5" radius={[6, 6, 0, 0]} />
              </BarChart>
            </ResponsiveContainer>
          </div>
        </div>
        <div className="bg-white rounded-2xl border border-slate-100 shadow-sm p-6">
          <h3 className="font-bold text-slate-800 flex items-center gap-2"><Users className="w-5 h-5 text-[#5B9BD5]" /> 최근 가입자</h3>
          <div className="mt-4 space-y-3">
            {loading ? (
              [...Array(4)].map((_, i) => <div key={i} className="h-12 bg-slate-50 rounded-xl animate-pulse" />)
            ) : recent.length === 0 ? (
              <p className="text-sm text-slate-400 py-6 text-center">가입자가 없어요.</p>
            ) : (
              recent.slice(0, 6).map((u) => (
                <div key={u.id} className="flex items-center gap-3 p-2 rounded-xl hover:bg-slate-50">
                  <img src={u.avatar || `https://api.dicebear.com/7.x/avataaars/svg?seed=${u.email}`} alt={u.name} className="w-9 h-9 rounded-full bg-slate-100" />
                  <div className="min-w-0 flex-1">
                    <p className="text-sm font-semibold text-slate-700 truncate">{u.name}</p>
                    <p className="text-xs text-slate-400 truncate">{u.email}</p>
                  </div>
                  {u.created_at && <span className="text-xs text-slate-400">{format(new Date(u.created_at), 'M.d', { locale: ko })}</span>}
                </div>
              ))
            )}
          </div>
        </div>
      </div>
    </div>
  );
}