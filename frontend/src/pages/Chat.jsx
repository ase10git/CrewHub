import { useEffect, useRef, useState } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { ArrowLeft, Send, Users, X, Search, MessageCircle, LogIn } from 'lucide-react';
import { Meetup } from '@/api/entities';
import { talkflow, connectTalkflow, safeTsZ } from '../lib/talkflow';
import { useAuthStore } from '../stores/authStore';
import { format } from 'date-fns';
const ATTACH_OPTIONS = [
  { key: 'media', label: '📷 사진 / 동영상', accept: 'image/*,video/*' },
  { key: 'document', label: '📎 문서', accept: '.pdf,.doc,.docx,.xls,.xlsx,.ppt,.pptx,.txt,.zip,.csv' },
  { key: 'audio', label: '🎵 오디오', accept: 'audio/*' },
];
export default function Chat() {
  const { meetupId } = useParams();
  const navigate = useNavigate();
  const user = useAuthStore((s) => s.user);
  const fetchMe = useAuthStore((s) => s.fetchMe);
  const [authStatus, setAuthStatus] = useState('loading');
  const [meetup, setMeetup] = useState(null);
  const [status, setStatus] = useState('init');
  const [errorMsg, setErrorMsg] = useState('');
  const [messages, setMessages] = useState([]);
  const [input, setInput] = useState('');
  const [isSending, setIsSending] = useState(false);
  const [pendingFile, setPendingFile] = useState(null);
  const [showAttachMenu, setShowAttachMenu] = useState(false);
  const [typingUsers, setTypingUsers] = useState(new Set());
  const [showInvite, setShowInvite] = useState(false);
  const [inviteSearchInput, setInviteSearchInput] = useState('');
  const [searchResults, setSearchResults] = useState([]);
  const [invited, setInvited] = useState(new Set());
  const [notice, setNotice] = useState('');
  const seenMessageIdsRef = useRef(new Set());
  const chatMessageHandlerRef = useRef(null);
  const typingHandlerRef = useRef(null);
  const activeRoomIdRef = useRef(null);
  const fileInputRef = useRef(null);
  const isComposingRef = useRef(false);
  const messagesEndRef = useRef(null);
  useEffect(() => { messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' }); }, [messages]);
  useEffect(() => {
    let mounted = true;
    const createRoom = async (m) => {
      const res = await talkflow.createGroupRoom({ roomName: m.name, invitedUserIds: [], anyoneCanInvite: true });
      const newRoomId = res?.data?.id || res?.data?.room?.id || res?.data?.roomId || res?.id || res?.roomId;
      try { if (newRoomId) await Meetup.update(m.id, { roomId: newRoomId }); } catch (e) { /* ignore */ }
      return newRoomId;
    };
    const enterRoomById = async (roomId) => {
      activeRoomIdRef.current = roomId;
      const detail = await talkflow.enterRoom(roomId);
      const msgs = [...(detail?.data?.messages || [])].reverse();
      setMessages(msgs);
      seenMessageIdsRef.current = new Set(msgs.map((mm) => mm.messageId).filter(Boolean));
      if (chatMessageHandlerRef.current && typeof talkflow.off === 'function') talkflow.off('chatMessage', chatMessageHandlerRef.current);
      const onChatMessage = ({ roomId: rid, message }) => {
        if (rid !== activeRoomIdRef.current) return;
        if (!message?.messageId) return;
        if (seenMessageIdsRef.current.has(message.messageId)) return;
        seenMessageIdsRef.current.add(message.messageId);
        message.sentAt = safeTsZ(message.sentAt);
        setMessages((prev) => {
          const optIdx = prev.findIndex((mm) => mm._isOptimistic && mm.userId === message.userId);
          if (optIdx >= 0) { const next = [...prev]; next[optIdx] = message; return next; }
          return [...prev, message];
        });
      };
      chatMessageHandlerRef.current = onChatMessage;
      talkflow.on('chatMessage', onChatMessage);
      if (typingHandlerRef.current && typeof talkflow.off === 'function') talkflow.off('typing', typingHandlerRef.current);
      const onTyping = ({ userId: uid, userName, typing }) => {
        if (uid === talkflow.getUserId()) return;
        setTypingUsers((prev) => {
          const next = new Set(prev);
          if (typing) next.add(userName || uid); else next.delete(userName || uid);
          return next;
        });
      };
      typingHandlerRef.current = onTyping;
      talkflow.on('typing', onTyping);
    };
    (async () => {
      let me = user;
      if (!me) me = await fetchMe();
      if (!me) { if (mounted) setAuthStatus('unauthenticated'); return; }
      if (mounted) setAuthStatus('authenticated');
      try {
        await connectTalkflow(me.id, me.name || me.email, me.avatar);
        const mres = await Meetup.get(meetupId);
        const m = mres.data;
        if (mounted) setMeetup(m);
        let roomId = m.roomId;
        if (roomId) {
          try { await enterRoomById(roomId); }
          catch (e) { roomId = await createRoom(m); await enterRoomById(roomId); }
        } else {
          roomId = await createRoom(m);
          await enterRoomById(roomId);
        }
        if (mounted) setStatus('ready');
      } catch (e) {
        console.error(e);
        if (mounted) { setStatus('error'); setErrorMsg(e.message || '채팅방을 불러오지 못했습니다.'); }
      }
    })();
    return () => {
      mounted = false;
      if (chatMessageHandlerRef.current && typeof talkflow.off === 'function') talkflow.off('chatMessage', chatMessageHandlerRef.current);
      if (typingHandlerRef.current && typeof talkflow.off === 'function') talkflow.off('typing', typingHandlerRef.current);
    };
    // eslint-disable-next-line
  }, [meetupId]);
  useEffect(() => {
    if (authStatus === 'unauthenticated') {
      const t = setTimeout(() => navigate('/login', { replace: true }), 1200);
      return () => clearTimeout(t);
    }
  }, [authStatus, navigate]);
  // invite search (IME-safe)
  useEffect(() => {
    const t = setTimeout(async () => {
      if (isComposingRef.current) return;
      if (!inviteSearchInput.trim()) { setSearchResults([]); return; }
      try {
        const res = await talkflow.searchUsers({ keyword: inviteSearchInput, limit: 20 });
        setSearchResults(res?.data || []);
      } catch (e) { setSearchResults([]); }
    }, 300);
    return () => clearTimeout(t);
  }, [inviteSearchInput]);
  const handleTyping = () => { if (activeRoomIdRef.current) { try { talkflow.startTyping(activeRoomIdRef.current); } catch (e) { /* ignore */ } } };
  const handleAttachOption = (opt) => {
    setShowAttachMenu(false);
    if (fileInputRef.current) { fileInputRef.current.accept = opt.accept; fileInputRef.current.click(); }
  };
  const handleFileChange = (e) => {
    const file = e.target.files?.[0];
    if (file) setPendingFile(file);
    e.target.value = '';
  };
  const handleSend = async () => {
    const text = input.trim();
    if ((!text && !pendingFile) || isSending || !activeRoomIdRef.current) return;
    setIsSending(true);
    try {
      if (pendingFile) {
        await talkflow.sendFileMessage(activeRoomIdRef.current, pendingFile);
        setPendingFile(null);
      }
      if (text) {
        const tmpId = 'tmp_' + Date.now();
        setMessages((prev) => [...prev, {
          messageId: tmpId, userId: talkflow.getUserId(), content: text,
          chatMessageType: 'TEXT', messageType: 'TEXT',
          senderName: user?.name || '나', sentAt: new Date().toISOString(), _isOptimistic: true,
        }]);
        setInput('');
        const res = await talkflow.sendMessage(activeRoomIdRef.current, { message: text });
        const sent = res?.data?.messages?.[0] || res?.messages?.[0];
        if (sent?.messageId) {
          seenMessageIdsRef.current.add(sent.messageId);
          setMessages((prev) => prev.map((mm) => mm.messageId === tmpId ? { ...sent } : mm));
        }
      }
    } catch (e) {
      console.error(e);
      setNotice('메시지 전송에 실패했어요.');
    } finally {
      setIsSending(false);
    }
  };
  const inviteUser = async (u) => {
    if (!activeRoomIdRef.current) return;
    try {
      await talkflow.inviteToGroupRoom(activeRoomIdRef.current, [u.id]);
      setInvited((prev) => new Set(prev).add(u.id));
      setNotice(`${u.nickname || '사용자'}님을 초대했어요.`);
    } catch (e) {
      console.error(e);
      setNotice('초대에 실패했어요.');
    }
  };
  const renderMessage = (msg) => {
    if (msg.fileMetaDataList?.length > 0) {
      return (
        <div className="flex flex-col gap-2">
          {msg.fileMetaDataList.map((fileData, idx) => {
            const fileUrl = fileData.fileUrl;
            const fileName = fileData.fileName || 'file';
            switch (msg.chatMessageType) {
              case 'IMAGE':
                return <img key={idx} src={fileUrl} alt={fileName} className="max-w-[220px] rounded-xl" loading="lazy" />;
              case 'VIDEO':
                return <video key={idx} src={fileUrl} controls className="max-w-[260px] rounded-xl" />;
              case 'AUDIO':
                return <audio key={idx} src={fileUrl} controls className="w-full" />;
              default:
                return (
                  <a key={idx} href={fileUrl} target="_blank" rel="noopener noreferrer" className="flex items-center gap-2 px-3 py-2 bg-white/60 rounded-lg text-sm text-slate-700 break-all">
                    📄 {fileName}
                  </a>
                );
            }
          })}
        </div>
      );
    }
    return <span className="break-words whitespace-pre-wrap">{msg.content}</span>;
  };
  if (authStatus === 'loading' || authStatus === 'unauthenticated') {
    return (
      <div className="max-w-md mx-auto px-4 py-24 text-center">
        {authStatus === 'loading' ? (
          <div className="w-8 h-8 border-4 border-slate-200 border-t-[#5B9BD5] rounded-full animate-spin mx-auto" />
        ) : (
          <>
            <MessageCircle className="w-12 h-12 text-slate-300 mx-auto mb-3" />
            <h2 className="text-lg font-bold text-slate-700">로그인이 필요해요</h2>
            <p className="text-slate-400 mt-2 text-sm">채팅에 참여하려면 로그인해 주세요.</p>
            <Link to="/login" className="inline-flex items-center gap-2 mt-5 px-6 py-3 rounded-xl bg-[#5B9BD5] text-white font-semibold">
              <LogIn className="w-4 h-4" /> 로그인하기
            </Link>
          </>
        )}
      </div>
    );
  }
  const myTfId = talkflow.getUserId();
  return (
    <div className="w-full max-w-3xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
      <div className="bg-white rounded-3xl shadow-lg shadow-blue-100/40 border border-slate-100 overflow-hidden flex flex-col" style={{ height: 'calc(100vh - 160px)', minHeight: '480px' }}>
        {/* header */}
        <div className="flex items-center justify-between px-4 py-3 border-b border-slate-100 bg-[#EAF3FB]">
          <div className="flex items-center gap-3 min-w-0">
            <button onClick={() => navigate(meetup ? `/meetups/${meetup.slug}` : '/meetups')} className="p-1.5 text-slate-500 hover:text-[#5B9BD5]"><ArrowLeft className="w-5 h-5" /></button>
            <div className="min-w-0">
              <h2 className="font-bold text-slate-800 truncate">{meetup?.name || '채팅'}</h2>
              <p className="text-xs text-slate-400">실시간 그룹 채팅</p>
            </div>
          </div>
          <button onClick={() => setShowInvite(true)} className="px-3 py-2 rounded-xl bg-white text-[#34618C] text-sm font-semibold border border-[#5B9BD5]/40 hover:bg-[#dceaf7] flex items-center gap-1.5">
            <Users className="w-4 h-4" /> 멤버 초대
          </button>
        </div>
        {/* messages */}
        <div className="flex-1 overflow-y-auto px-4 py-4 bg-[#FAFBFD]">
          {status === 'init' && (
            <div className="h-full flex items-center justify-center">
              <div className="w-7 h-7 border-4 border-slate-200 border-t-[#5B9BD5] rounded-full animate-spin" />
            </div>
          )}
          {status === 'error' && (
            <div className="h-full flex flex-col items-center justify-center text-center">
              <p className="text-slate-500">{errorMsg}</p>
              <button onClick={() => window.location.reload()} className="mt-4 px-5 py-2.5 rounded-xl bg-[#5B9BD5] text-white font-semibold">다시 시도</button>
            </div>
          )}
          {status === 'ready' && messages.length === 0 && (
            <div className="h-full flex flex-col items-center justify-center text-center">
              <MessageCircle className="w-12 h-12 text-slate-200 mb-3" />
              <p className="text-slate-400">아직 메시지가 없어요. 첫 인사를 건네보세요! 👋</p>
            </div>
          )}
          {status === 'ready' && messages.map((msg) => {
            const mine = String(msg.userId) === String(myTfId);
            return (
              <div key={msg.messageId} className={`flex mb-3 ${mine ? 'justify-end' : 'justify-start'}`}>
                <div className={`max-w-[78%] ${mine ? 'items-end' : 'items-start'} flex flex-col`}>
                  {!mine && <span className="text-xs text-slate-400 mb-1 px-1">{msg.senderName || '멤버'}</span>}
                  <div className={`px-3.5 py-2.5 rounded-2xl text-sm leading-relaxed ${mine ? 'bg-[#5B9BD5] text-white rounded-br-sm' : 'bg-white border border-slate-100 text-slate-700 rounded-bl-sm shadow-sm'}`}>
                    {renderMessage(msg)}
                  </div>
                  <span className="text-[10px] text-slate-300 mt-1 px-1">{(() => { try { return format(new Date(safeTsZ(msg.sentAt)), 'HH:mm'); } catch { return ''; } })()}</span>
                </div>
              </div>
            );
          })}
          <div ref={messagesEndRef} />
        </div>
        {/* typing */}
        {typingUsers.size > 0 && (
          <div className="px-4 py-1 text-xs text-[#5B9BD5] bg-[#FAFBFD]">{[...typingUsers].join(', ')}님이 입력 중...</div>
        )}
        {notice && (
          <div className="px-4 py-2 text-xs text-[#34618C] bg-[#EAF3FB] flex items-center justify-between">
            <span>{notice}</span>
            <button onClick={() => setNotice('')}><X className="w-3.5 h-3.5" /></button>
          </div>
        )}
        {/* pending file */}
        {pendingFile && (
          <div className="px-4 py-2 bg-[#FBF0E9] flex items-center justify-between text-sm text-[#b56636]">
            <span className="truncate">📎 {pendingFile.name}</span>
            <button onClick={() => setPendingFile(null)}><X className="w-4 h-4" /></button>
          </div>
        )}
        {/* input */}
        <div className="px-3 py-3 border-t border-slate-100 bg-white flex items-end gap-2">
          <div className="relative">
            <button onClick={() => setShowAttachMenu((v) => !v)} disabled={status !== 'ready'} className="w-10 h-10 rounded-full bg-slate-50 text-slate-500 hover:bg-slate-100 flex items-center justify-center disabled:opacity-50" title="파일 첨부">＋</button>
            {showAttachMenu && (
              <div className="absolute bottom-full left-0 mb-2 bg-white rounded-2xl shadow-xl border border-slate-100 py-2 min-w-[180px] z-10">
                {ATTACH_OPTIONS.map((opt) => (
                  <button key={opt.key} onClick={() => handleAttachOption(opt)} className="block w-full text-left px-4 py-2.5 text-sm text-slate-700 hover:bg-slate-50">{opt.label}</button>
                ))}
              </div>
            )}
            <input type="file" ref={fileInputRef} className="hidden" onChange={handleFileChange} />
          </div>
          <textarea
            value={input}
            onChange={(e) => { setInput(e.target.value); handleTyping(); }}
            onKeyDown={(e) => { if (e.key === 'Enter' && !e.shiftKey && !e.nativeEvent.isComposing) { e.preventDefault(); handleSend(); } }}
            disabled={status !== 'ready'}
            rows={1}
            placeholder="메시지를 입력하세요"
            className="flex-1 px-4 py-2.5 rounded-2xl border border-slate-200 focus:border-[#5B9BD5] focus:ring-2 focus:ring-[#5B9BD5]/20 outline-none resize-none max-h-28 disabled:bg-slate-50"
          />
          <button onClick={handleSend} disabled={isSending || status !== 'ready' || (!input.trim() && !pendingFile)} className="w-10 h-10 rounded-full bg-[#5B9BD5] text-white flex items-center justify-center hover:bg-[#4a8ac4] active:scale-95 transition-all disabled:opacity-50">
            <Send className="w-5 h-5" />
          </button>
        </div>
      </div>
      {/* invite modal */}
      {showInvite && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40 px-4" onClick={() => setShowInvite(false)}>
          <div className="bg-white rounded-3xl p-6 max-w-md w-full max-h-[80vh] flex flex-col" onClick={(e) => e.stopPropagation()}>
            <div className="flex items-center justify-between mb-4">
              <h3 className="text-lg font-bold text-slate-800">멤버 초대</h3>
              <button onClick={() => setShowInvite(false)}><X className="w-5 h-5 text-slate-400" /></button>
            </div>
            <div className="relative">
              <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-400" />
              <input
                value={inviteSearchInput}
                onChange={(e) => setInviteSearchInput(e.target.value)}
                onCompositionStart={() => { isComposingRef.current = true; }}
                onCompositionEnd={(e) => { isComposingRef.current = false; setInviteSearchInput(e.currentTarget.value); }}
                placeholder="닉네임으로 검색"
                className="w-full pl-9 pr-4 py-2.5 rounded-xl border border-slate-200 focus:border-[#5B9BD5] outline-none"
              />
            </div>
            <div className="mt-4 overflow-y-auto flex-1 space-y-2">
              {searchResults.length === 0 ? (
                <p className="text-sm text-slate-400 text-center py-6">닉네임을 검색해 멤버를 초대하세요.</p>
              ) : (
                searchResults.map((u) => (
                  <div key={u.id} className="flex items-center justify-between p-3 rounded-xl bg-slate-50">
                    <div className="flex items-center gap-3">
                      <img src={u.profileUrl || `https://api.dicebear.com/7.x/avataaars/svg?seed=${u.id}`} alt={u.nickname} className="w-9 h-9 rounded-full bg-white" />
                      <span className="text-sm font-medium text-slate-700">{u.nickname || '사용자'}</span>
                    </div>
                    <button onClick={() => inviteUser(u)} disabled={invited.has(u.id)} className="px-3 py-1.5 rounded-lg bg-[#5B9BD5] text-white text-sm font-semibold disabled:bg-slate-300">
                      {invited.has(u.id) ? '초대됨' : '초대'}
                    </button>
                  </div>
                ))
              )}
            </div>
          </div>
        </div>
      )}
    </div>
  );
}