import { Link } from 'react-router-dom';
import { Mail, MessageCircle, MapPin } from 'lucide-react';
const LOGO = 'https://cdn.vibe-x.app/apps/71212311046d33c35ec0f71a/assets/original/logo-0-53405.png';
export default function Footer() {
  return (
    <footer className="bg-[#34618C] text-white mt-auto">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10 md:py-14">
        <div className="grid grid-cols-1 md:grid-cols-4 gap-8">
          <div className="md:col-span-2">
            <img src={LOGO} alt="어울림" className="h-9 w-auto object-contain" style={{ filter: 'brightness(0) invert(1)' }} />
            <p className="mt-4 text-sm text-white/70 leading-relaxed max-w-md">
              스터디·취미·언어 등 다양한 모임을 만들고 참여하세요. 실시간 채팅과 문서 아카이빙까지, 함께 어울리는 모든 순간을 한 곳에서.
            </p>
          </div>
          <div className="hidden md:block">
            <h4 className="font-hand text-2xl mb-3">바로가기</h4>
            <ul className="space-y-2 text-sm text-white/70">
              <li><Link to="/" className="hover:text-white">홈</Link></li>
              <li><Link to="/meetups" className="hover:text-white">모임 둘러보기</Link></li>
              <li><Link to="/meetups/new" className="hover:text-white">모임 만들기</Link></li>
              <li><Link to="/mypage" className="hover:text-white">마이페이지</Link></li>
            </ul>
          </div>
          <div className="hidden md:block">
            <h4 className="font-hand text-2xl mb-3">문의</h4>
            <ul className="space-y-2 text-sm text-white/70">
              <li className="flex items-center gap-2"><Mail className="w-4 h-4" /> hello@eoullim.app</li>
              <li className="flex items-center gap-2"><MessageCircle className="w-4 h-4" /> 평일 10:00 ~ 18:00</li>
              <li className="flex items-center gap-2"><MapPin className="w-4 h-4" /> 서울특별시 어울림로 12</li>
            </ul>
          </div>
        </div>
        <div className="md:hidden mt-6 text-center text-sm text-white/70">
          hello@eoullim.app · 평일 10:00~18:00
        </div>
        <div className="mt-8 pt-6 border-t border-white/15 flex flex-col md:flex-row items-center justify-between gap-3">
          <p className="text-xs text-white/60">© 2026 어울림. All rights reserved.</p>
          <Link to="/admin/login" className="text-xs text-white/40 hover:text-white/80 transition-colors">관리자</Link>
        </div>
      </div>
    </footer>
  );
}