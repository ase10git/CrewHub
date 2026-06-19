import { Link } from 'react-router-dom';
import { Users, Globe, Eye } from 'lucide-react';
export default function MeetupCard({ meetup }) {
  return (
    <Link
      to={`/meetups/${meetup.slug || meetup.id}`}
      className="group block bg-white rounded-3xl shadow-lg shadow-blue-100/40 border border-slate-100 overflow-hidden hover:-translate-y-1.5 hover:shadow-xl hover:shadow-blue-200/50 transition-all duration-300"
    >
      <div className="relative h-44 overflow-hidden bg-gradient-to-br from-[#A8C8F0] to-[#5B9BD5]">
        {meetup.coverImage ? (
          <img src={meetup.coverImage} alt={meetup.name} className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-500" />
        ) : (
          <div className="w-full h-full flex items-center justify-center text-6xl opacity-90 select-none">🌊</div>
        )}
        <span className="absolute top-3 left-3 px-3 py-1 rounded-full bg-white/90 backdrop-blur text-[#34618C] text-xs font-bold">
          {meetup.categoryName || '모임'}
        </span>
      </div>
      <div className="p-5">
        <h3 className="font-bold text-lg text-slate-800 truncate">{meetup.name}</h3>
        <p className="text-sm text-slate-500 mt-1.5 leading-relaxed line-clamp-2 h-10">{meetup.description}</p>
        <div className="flex items-center gap-4 mt-4 text-sm text-slate-500">
          <span className="flex items-center gap-1"><Users className="w-4 h-4 text-[#5B9BD5]" />{meetup.memberCount || 0}명</span>
          <span className="flex items-center gap-1">
            {meetup.isPublic ? <Globe className="w-4 h-4 text-[#5B9BD5]" /> : <Eye className="w-4 h-4 text-[#E08A5B]" />}
            {meetup.isPublic ? '공개' : '비공개'}
          </span>
        </div>
      </div>
    </Link>
  );
}