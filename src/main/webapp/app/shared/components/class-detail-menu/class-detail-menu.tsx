import { useAppSelector } from 'app/config/store';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import axios from 'axios';
import { Chips } from 'primereact/chips';
import { Dialog } from 'primereact/dialog';
import { MenuItem } from 'primereact/menuitem';
import { TabMenu } from 'primereact/tabmenu';
import React, { useContext, useEffect, useState } from 'react';
import { translate } from 'react-jhipster';
import { useLocation, useNavigate, useParams, useSearchParams } from 'react-router-dom';
import { toast } from 'react-toastify';
import { Button } from 'reactstrap';
import { CourseOwnerIdContext, InvitationCodeContext, MenuContext } from './class-detail-context';
import './class-detail-menu.scss';
import { set } from 'lodash';

export const ClassDetailMenu = () => {
  const { activeMenu, setActiveMenu } = useContext(MenuContext);
  const { invitationCode, setInvitationCode } = useContext(InvitationCodeContext);
  const { courseOwnerId, setCourseOwnerId } = useContext(CourseOwnerIdContext);
  const [curActiveMenu, setCurActiveMenu] = useState(activeMenu);
  const [curInvitationCode, setCurInvitationCode] = useState(invitationCode);
  const [curCourseOwnerId, setCurCourseOwnerId] = useState(courseOwnerId);
  const [isCopyInvitationCode, setIsCopyInvitationCode] = useState(false);
  const [isCopyInvitationLink, setIsCopyInvitationLink] = useState(false);
  const [invitedMails, setInvitedMails] = useState([]);
  const [disabledSendInvitation, setDisabledSendInvitation] = useState(true);
  const [visible, setVisible] = useState(false);

  const navigate = useNavigate();
  const location = useLocation();
  const TAB_INDEX = ['stream', 'class-work', 'people', 'grade'];
  const TAB_NAME = {
    stream: 0,
    'class-work': 1,
    people: 2,
    grade: 3,
  };

  useEffect(() => {
    const path = location.pathname.split('/')[4];
    if (path) {
      setActiveIndex(TAB_NAME[path]);
    }
  });

  const { id } = useParams<'id'>();
  const [searchParams] = useSearchParams();
  const account = useAppSelector(state => state.authentication.account);
  const courseEntity = useAppSelector(state => state.course.entity);

  useEffect(() => {
    if (!curInvitationCode) {
      axios.get(`/api/courses/${id}`).then(res => {
        setCurInvitationCode(res.data.invitationCode);
        setInvitationCode(res.data.invitationCode);
        setCurCourseOwnerId(res.data.ownerId);
        setCourseOwnerId(res.data.ownerId);
      });
    }
  }, []);

  const [activeIndex, setActiveIndex] = useState(TAB_NAME[curActiveMenu]);
  const items: MenuItem[] = [
    { label: 'Grade structure', icon: 'pi pi-fw pi-home', className: 'aw-menu-item' },
    { label: 'Assginments', icon: 'pi pi-fw pi-calendar', className: 'aw-menu-item' },
    { label: 'Members', icon: 'pi pi-fw pi-users', className: 'aw-menu-item' },
    { label: 'Grade board', icon: 'pi pi-fw pi-folder', className: 'aw-menu-item' },
  ];

  const handleMenuItemClick = (menuName, path) => {
    setActiveMenu(menuName);
    setCurActiveMenu(menuName);
    navigate(path);
  };

  const handleCopyInvitationCodeToClipboard = () => {
    navigator.clipboard.writeText(curInvitationCode);
    setIsCopyInvitationCode(true);
    setTimeout(() => {
      setIsCopyInvitationCode(false);
    }, 2000);
  };

  const handleCopyInvitationLinkToClipboard = () => {
    const baseUrl = window.location.origin;
    navigator.clipboard.writeText(`${baseUrl}/invitation/${curInvitationCode}`);
    setIsCopyInvitationLink(true);
    setTimeout(() => {
      setIsCopyInvitationLink(false);
    }, 2000);
  };

  const handleSetInvitedMails = mails => {
    const lastMail = mails[mails.length - 1];
    const emailRegex = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Z|a-z]{2,}$/;
    if (mails.length > 5 || !emailRegex.test(lastMail)) {
      mails.pop();
    }
    setInvitedMails(mails);

    if (invitedMails.length > 0) {
      setDisabledSendInvitation(true);
    } else {
      setDisabledSendInvitation(false);
    }
  };

  const handleSendInvitationByEmail = () => {
    if (invitedMails.length > 0) {
      axios.post(`/api/courses/send-invitation/${curInvitationCode}`, invitedMails).then(res => {
        setVisible(false);
        setInvitedMails([]);
        toast.success(translate('webApp.course.inviteSuccess'), {
          position: toast.POSITION.TOP_LEFT,
        });
      });
    }
  };

  return (
    <div className="aw-class-detail-menu-container">
      <TabMenu
        model={items}
        unstyled={false}
        activeIndex={activeIndex}
        onTabChange={e => {
          handleMenuItemClick(TAB_INDEX[e.index], `/course/${id}/detail/${TAB_INDEX[e.index]}`);
          setActiveIndex(e.index);
        }}
      />
      {hasAnyAuthority(account.authorities, ['ROLE_TEACHER']) && account.id === curCourseOwnerId && (
        <>
          <Button className="d-flex flex-row align-items-center btn-invite btn btn-dark" onClick={() => setVisible(true)}>
            {translate('webApp.course.invite')}
          </Button>
          <Dialog
            header={translate('webApp.course.invitePeople')}
            visible={visible}
            style={{ width: '25vw' }}
            onHide={() => setVisible(false)}
          >
            <div className="w-100">
              <Chips
                className="w-100"
                value={invitedMails}
                onChange={e => handleSetInvitedMails(e.value)}
                placeholder={translate('webApp.course.addEmails')}
              ></Chips>
            </div>
            <div className="d-flex flex-row gap-3 justify-content-between">
              <div className="d-flex flex-row gap-3">
                <button className="btn btn-outline-dark" onClick={handleCopyInvitationCodeToClipboard}>
                  {!isCopyInvitationCode && translate('webApp.course.copyCode')}
                  {isCopyInvitationCode && <span>{translate('webApp.course.copied')}</span>}
                </button>
                <button className="btn btn-outline-dark" onClick={handleCopyInvitationLinkToClipboard}>
                  {!isCopyInvitationLink && translate('webApp.course.copyLink')}
                  {isCopyInvitationLink && <span>{translate('webApp.course.copied')}</span>}
                </button>
              </div>
              <button className="btn btn-primary" onClick={handleSendInvitationByEmail} disabled={disabledSendInvitation}>
                {translate('webApp.course.sendInvitation')}
              </button>
            </div>
          </Dialog>
        </>
      )}
    </div>
  );
};
