import Layout from './Layout.jsx';
import Home from './pages/Home.jsx';
import Meetups from './pages/Meetups.jsx';
import CreateMeetup from './pages/CreateMeetup.jsx';
import MeetupDetail from './pages/MeetupDetail.jsx';
import Documents from './pages/Documents.jsx';
import DocumentDetail from './pages/DocumentDetail.jsx';
import DocumentEditor from './pages/DocumentEditor.jsx';
import Chat from './pages/Chat.jsx';
import MyPage from './pages/MyPage.jsx';
import Login from './pages/Login.jsx';
import Register from './pages/Register.jsx';
import AdminLayout from './pages/admin/AdminLayout.jsx';
import Dashboard from './pages/admin/Dashboard.jsx';
import MembersList from './pages/admin/MembersList.jsx';
import MeetupsList from './pages/admin/MeetupsList.jsx';
import MeetupEdit from './pages/admin/MeetupEdit.jsx';
import DocumentsList from './pages/admin/DocumentsList.jsx';
export const PAGES = {
  Home,
  'meetups': Meetups,
  'meetups/new': CreateMeetup,
  'meetups/:slug': MeetupDetail,
  'meetups/:slug/documents': Documents,
  'chat/:meetupId': Chat,
  'documents/new': DocumentEditor,
  'documents/:id': DocumentDetail,
  'documents/:id/edit': DocumentEditor,
  'mypage': MyPage,
  'login': Login,
  'register': Register,
};
export const ADMINS = {
  Dashboard,
  'members': MembersList,
  'meetups': MeetupsList,
  'meetups/:id/edit': MeetupEdit,
  'documents': DocumentsList,
};
export const PRIVATE_PAGES = {};
export const pagesConfig = {
  privatePages: PRIVATE_PAGES,
  mainPage: 'Home',
  Pages: PAGES,
  Layout: Layout,
  Admins: ADMINS,
  adminMainPage: 'Dashboard',
  AdminLayout: AdminLayout,
};