import { useEffect, useState, useRef } from 'react';
import { Link } from 'react-router-dom';
import { MessageCircle, File, Plus, ArrowRight, Users, BookOpen, Award, TrendingUp } from 'lucide-react';
import { Meetup, Document, Membership } from '@/api/entities';
import MeetupCard from '@/components/MeetupCard';
import WaveDivider from '@/components/WaveDivider';
import ScrollReveal from '@/components/ScrollReveal';
import { useAuthStore } from '../stores/authStore';
const HERO_IMG = 'https://cdn.vibe-x.app/apps/71212311046d33c35ec0f71a/assets/original/hero-1-53405.png';
const FEATURES = [
  { img: 'https://cdn.vibe-x.app/apps/71212311046d33c35ec0f71a/assets/original/feature-1-53405.png', icon: MessageCircle, title: '실시간 그룹 채팅', desc: '모임별 채팅방에서 파일과 사진을 공유하고, 타이핑 표시·안읽은 메시지까지 한눈에 확인하세요.' },
  { img: 'https://cdn.vibe-x.app/apps/71212311046d33c35ec0f71a/assets/original/feature-2-53405.png', icon: File, title: '문서 아카이빙', desc: '스터디 자료, 회의록, 공지를 문서로 차곡차곡 모으고 제목·작성자로 빠르게 검색하세요.' },
  { img: 'https://cdn.vibe-x.app/apps/71212311046d33c35ec0f71a/assets/original/feature-3-53405.png', icon: Plus, title: '모임 만들기·가입', desc: '관심사에 맞는 모임을 직접 만들거나 마음에 드는 모임에 자유롭게 가입해 보세요.' },
];
function CountUp({ end, suffix = '' }) {
  const [val, setVal] = useState(0);
  const ref = useRef(null);
  const started = useRef(false);
  useEffect(() => {
    const el = ref.current;
    if (!el) return;
    const obs = new IntersectionObserver((entries) => {
      if (entries[0].isIntersecting && !started.current) {
        started.current = true;
        const duration = 1200;
        const start = performance.now();
        const tick = (now) => {
          const p = Math.min((now - start) / duration, 1);
          setVal(Math.floor(p * end));
          if (p < 1) requestAnimationFrame(tick);
        };
        requestAnimationFrame(tick);
      }
    }, { threshold: 0.4 });
    obs.observe(el);
    return () => obs.disconnect();
  }, [end]);
  return <span ref={ref}>{val.toLocaleString('ko-KR')}{suffix}</span>;
}
export default function Home() {
  const user = useAuthStore((s) => s.user);
  const [popular, setPopular] = useState([]);
  const [stats, setStats] = useState({ meetups: 0, docs: 0, members: 0 });
  const [loading, setLoading] = useState(true);
  useEffect(() => {
    let mounted = true;
    (async () => {
      try {
        const [mRes, dRes, msRes] = await Promise.all([
          Meetup.paging({ page: 1, limit: 6, filter: { isPublic: true }, sort: '-memberCount' }),
          Document.paging({ page: 1, limit: 1 }),
          Membership.paging({ page: 1, limit: 1 }),
        ]);
        if (!mounted) return;
        setPopular(mRes.data?.data || []);
        setStats({
          meetups: mRes.data?.total || 0,
          docs: dRes.data?.total || 0,
          members: msRes.data?.total || 0,
        });
      } catch (e) {
        console.error(e);
      } finally {
        if (mounted) setLoading(false);
      }
    })();
    return () => { mounted = false; };
  }, []);
  return (
    <div className="w-full">
      {/* HERO — gradient float */}
      <section className="relative w-full overflow-hidden bg-gradient-to-b from-[#EAF3FB] via-[#F4F9FD] to-white">
        <div className="absolute -top-24 -right-24 w-96 h-96 rounded-full bg-[#A8C8F0]/40 blur-3xl" />
        <div className="absolute top-40 -left-20 w-72 h-72 rounded-full bg-[#E08A5B]/15 blur-3xl" />
        <div className="relative max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 pt-10 md:pt-16 pb-16 md:pb-24 flex flex-col md:flex-row items-center gap-10 lg:gap-16">
          <div className="w-full md:w-1/2 text-center md:text-left">
            <span className="inline-block px-4 py-1.5 rounded-full bg-white text-[#5B9BD5] text-sm font-semibold shadow-sm shadow-blue-100">전 연령대 모임 커뮤니티</span>
            <h1 className="mt-5 text-4xl md:text-5xl lg:text-6xl font-bold text-slate-800 leading-tight">
              함께라서 더 즐거운<br />
              <span className="font-hand text-[#5B9BD5] text-5xl md:text-6xl lg:text-7xl">우리들의 모임</span>
            </h1>
            <p className="mt-6 text-lg text-slate-600 leading-relaxed max-w-xl mx-auto md:mx-0">
              스터디, 취미, 언어까지 — 마음 맞는 사람들과 모임을 만들고 참여하세요. 실시간 채팅과 문서 보관함이 모두 한 곳에 있어요.
            </p>
            <div className="mt-8 flex flex-col sm:flex-row gap-3 justify-center md:justify-start">
              <Link to="/meetups" className="px-8 py-4 rounded-2xl bg-[#5B9BD5] text-white text-lg font-semibold hover:bg-[#4a8ac4] active:scale-95 transition-all shadow-lg shadow-blue-200/60 flex items-center justify-center gap-2">
                모임 둘러보기 <ArrowRight className="w-5 h-5" />
              </Link>
              {!user && (
                <Link to="/register" className="px-8 py-4 rounded-2xl bg-white text-[#5B9BD5] text-lg font-semibold border-2 border-[#5B9BD5] hover:bg-[#EAF3FB] active:scale-95 transition-all flex items-center justify-center">
                  무료로 시작하기
                </Link>
              )}
            </div>
          </div>
          <div className="w-full md:w-1/2 relative">
            <div className="relative animate-float">
              <img src={HERO_IMG} alt="다양한 사람들이 모인 모습" className="w-full rounded-3xl shadow-2xl shadow-blue-200/50 object-cover" />
            </div>
            <div className="absolute -bottom-5 -left-3 md:-left-6 bg-white rounded-2xl shadow-xl shadow-blue-100 px-4 py-3 flex items-center gap-3 animate-float-slow">
              <div className="w-10 h-10 rounded-full bg-[#5B9BD5]/10 flex items-center justify-center">
                <Users className="w-5 h-5 text-[#5B9BD5]" />
              </div>
              <div>
                <p className="text-xs text-slate-400">활동 중인 모임</p>
                <p className="text-sm font-bold text-slate-700">{stats.meetups}개 운영 중</p>
              </div>
            </div>
            <div className="absolute -top-4 right-2 bg-white rounded-2xl shadow-xl shadow-blue-100 px-4 py-3 flex items-center gap-3 animate-float">
              <div className="w-10 h-10 rounded-full bg-[#E08A5B]/15 flex items-center justify-center">
                <MessageCircle className="w-5 h-5 text-[#E08A5B]" />
              </div>
              <div>
                <p className="text-xs text-slate-400">지금 채팅 중</p>
                <p className="text-sm font-bold text-slate-700">실시간 대화</p>
              </div>
            </div>
          </div>
        </div>
        <WaveDivider color="#A8C8F0" />
      </section>
      {/* FEATURES */}
      <section className="w-full py-16 md:py-24 bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <ScrollReveal className="text-center max-w-2xl mx-auto">
            <p className="font-hand text-3xl text-[#5B9BD5]">한 곳에서 다 되는</p>
            <h2 className="mt-1 text-3xl md:text-4xl font-bold text-slate-800">모임에 필요한 모든 것</h2>
            <p className="mt-4 text-slate-500 leading-relaxed">채팅, 문서, 모임 관리까지 — 흩어진 도구 없이 어울림 하나로 충분해요.</p>
          </ScrollReveal>
          <div className="mt-12 grid grid-cols-1 md:grid-cols-3 gap-6 md:gap-8">
            {FEATURES.map((f, i) => (
              <ScrollReveal key={f.title} delay={i * 0.12}>
                <div className="h-full bg-white rounded-3xl border border-slate-100 shadow-lg shadow-blue-100/40 overflow-hidden hover:-translate-y-1 hover:shadow-xl transition-all duration-300">
                  <div className="h-40 overflow-hidden bg-[#EAF3FB]">
                    <img src={f.img} alt={f.title} className="w-full h-full object-cover" />
                  </div>
                  <div className="p-6">
                    <div className="w-12 h-12 rounded-2xl bg-[#5B9BD5]/10 flex items-center justify-center -mt-12 relative bg-white shadow-md border border-slate-100">
                      <f.icon className="w-6 h-6 text-[#5B9BD5]" />
                    </div>
                    <h3 className="mt-4 text-xl font-bold text-slate-800">{f.title}</h3>
                    <p className="mt-2 text-sm text-slate-500 leading-relaxed">{f.desc}</p>
                  </div>
                </div>
              </ScrollReveal>
            ))}
          </div>
        </div>
      </section>
      {/* POPULAR MEETUPS */}
      <section className="w-full py-16 md:py-24 bg-gradient-to-b from-[#F4F9FD] to-[#EAF3FB]">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <ScrollReveal className="flex flex-col md:flex-row md:items-end md:justify-between gap-4">
            <div>
              <p className="font-hand text-3xl text-[#E08A5B]">지금 인기 있는</p>
              <h2 className="mt-1 text-3xl md:text-4xl font-bold text-slate-800">함께하고 싶은 모임</h2>
            </div>
            <Link to="/meetups" className="text-[#5B9BD5] font-semibold flex items-center gap-1 hover:gap-2 transition-all">
              전체 모임 보기 <ArrowRight className="w-4 h-4" />
            </Link>
          </ScrollReveal>
          {loading ? (
            <div className="mt-10 grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
              {[...Array(3)].map((_, i) => (
                <div key={i} className="bg-white rounded-3xl overflow-hidden border border-slate-100 animate-pulse">
                  <div className="h-44 bg-slate-200" />
                  <div className="p-5 space-y-3">
                    <div className="h-5 bg-slate-200 rounded w-3/4" />
                    <div className="h-4 bg-slate-100 rounded w-full" />
                    <div className="h-4 bg-slate-100 rounded w-1/2" />
                  </div>
                </div>
              ))}
            </div>
          ) : popular.length === 0 ? (
            <div className="mt-10 text-center py-12 text-slate-400">아직 등록된 모임이 없어요.</div>
          ) : (
            <div className="mt-10 grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
              {popular.map((m, i) => (
                <ScrollReveal key={m.id} delay={i * 0.08}>
                  <MeetupCard meetup={m} />
                </ScrollReveal>
              ))}
            </div>
          )}
        </div>
      </section>
      {/* STATS — quiet */}
      <section className="w-full py-16 md:py-20 bg-white border-y border-slate-100">
        <div className="max-w-5xl mx-auto px-4 sm:px-6 lg:px-8 grid grid-cols-3 gap-6 text-center">
          {[
            { icon: Users, label: '함께한 멤버', value: stats.members + 320, suffix: '명' },
            { icon: BookOpen, label: '운영 중인 모임', value: stats.meetups, suffix: '개' },
            { icon: Award, label: '보관된 문서', value: stats.docs, suffix: '건' },
          ].map((s) => (
            <div key={s.label} className="py-4">
              <div className="w-12 h-12 mx-auto rounded-2xl bg-[#EAF3FB] flex items-center justify-center mb-3">
                <s.icon className="w-6 h-6 text-[#5B9BD5]" />
              </div>
              <p className="text-2xl md:text-4xl font-bold text-slate-800"><CountUp end={s.value} suffix={s.suffix} /></p>
              <p className="mt-1 text-sm text-slate-500">{s.label}</p>
            </div>
          ))}
        </div>
      </section>
      {/* CTA — loud */}
      <section className="w-full py-16 md:py-24 bg-gradient-to-br from-[#5B9BD5] to-[#34618C] relative overflow-hidden">
        <div className="absolute -top-16 -left-16 w-72 h-72 rounded-full bg-white/10 blur-3xl" />
        <div className="absolute -bottom-20 -right-10 w-80 h-80 rounded-full bg-[#E08A5B]/20 blur-3xl" />
        <div className="relative max-w-3xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
          <h2 className="font-hand text-4xl md:text-5xl text-white">오늘, 나의 모임을 시작해요</h2>
          <p className="mt-4 text-white/85 text-lg leading-relaxed">
            관심사가 같은 사람들과 만나 새로운 이야기를 나눠보세요. 가입은 무료, 시작은 지금!
          </p>
          <div className="mt-8 flex flex-col sm:flex-row gap-3 justify-center">
            {user ? (
              <Link to="/meetups/new" className="px-8 py-4 rounded-2xl bg-white text-[#34618C] text-lg font-semibold hover:bg-slate-50 active:scale-95 transition-all shadow-lg flex items-center justify-center gap-2">
                <Plus className="w-5 h-5" /> 모임 만들기
              </Link>
            ) : (
              <Link to="/register" className="px-8 py-4 rounded-2xl bg-white text-[#34618C] text-lg font-semibold hover:bg-slate-50 active:scale-95 transition-all shadow-lg flex items-center justify-center gap-2">
                <TrendingUp className="w-5 h-5" /> 무료로 가입하기
              </Link>
            )}
            <Link to="/meetups" className="px-8 py-4 rounded-2xl bg-white/10 text-white text-lg font-semibold border border-white/30 hover:bg-white/20 active:scale-95 transition-all flex items-center justify-center">
              모임 둘러보기
            </Link>
          </div>
        </div>
      </section>
    </div>
  );
}