import TalkFlowClientModule from '@vibexnpm/talkflow';
const TalkFlowClient = typeof TalkFlowClientModule === 'function'
  ? TalkFlowClientModule
  : (TalkFlowClientModule.default || TalkFlowClientModule);
export const talkflow = new TalkFlowClient({
  apiKey: 'ck-6f82c7da10b18e64cc94bb3c3cdf768d45458a1e1ebd1ddbca1ce269142dc029',
  projectId: '71212311046d33c35ec0f71a',
  env: 'production',
});
export async function connectTalkflow(userId, nickname, profileImageUrl) {
  try {
    if (typeof talkflow.getUserId === 'function' && talkflow.getUserId()) {
      return talkflow.getUserId();
    }
  } catch (e) { /* not connected yet */ }
  const res = await fetch('https://app.vibe-x.app/v1/projects/71212311046d33c35ec0f71a/users/auth', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ userId: String(userId), nickname: nickname || ('user' + userId) }),
  });
  if (!res.ok) throw new Error('TalkFlow auth failed: ' + res.status);
  const data = await res.json();
  const payload = data.data?.data?.accessToken ? data.data.data : (data.data?.accessToken ? data.data : data);
  const jwtToken = payload.accessToken;
  const talkflowUserId = payload.user.userId;
  await talkflow.connect(jwtToken, { enablePush: false });
  try {
    const updatePayload = {};
    if (nickname) updatePayload.nickname = nickname;
    if (profileImageUrl) updatePayload.profileImageUrl = profileImageUrl;
    if (Object.keys(updatePayload).length > 0) {
      await talkflow.updateMyInfo(updatePayload);
    }
  } catch (e) {
    console.warn('TalkFlow updateMyInfo failed:', e);
  }
  return talkflowUserId;
}
export async function disconnectTalkflow() {
  try { await talkflow.logout(); } catch (e) { /* ignore */ }
}
export function safeTsZ(ts) {
  if (ts == null) return new Date().toISOString();
  if (typeof ts === 'number') return new Date(ts).toISOString();
  if (ts instanceof Date) return ts.toISOString();
  if (typeof ts === 'string') {
    if (!ts.endsWith('Z') && !ts.includes('+')) return ts + 'Z';
    return ts;
  }
  return new Date().toISOString();
}