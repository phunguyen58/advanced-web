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
  const TAB_INDEX = ['stream', 'class-work', 'people', 'grade', 'grade-structure', 'grade-review'];
  const TAB_NAME = {
    stream: 0,
    'class-work': 1,
    people: 2,
    grade: 3,
    'grade-structure': 4,
    'grade-review': 5,
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
    { label: translate('webApp.course.stream'), icon: 'pi pi-fw pi-home', className: 'aw-menu-item' },
    { label: translate('webApp.assignment.detail.title'), icon: 'pi pi-fw pi-calendar', className: 'aw-menu-item' },
    { label: translate('webApp.course.members'), icon: 'pi pi-fw pi-users', className: 'aw-menu-item' },
    { label: translate('webApp.course.gradeBoard'), icon: 'pi pi-fw pi-table', className: 'aw-menu-item' },
    { label: translate('global.menu.entities.gradeStructure'), icon: 'pi pi-fw pi-file', className: 'aw-menu-item' },
    { label: translate('webApp.gradeReview.detail.title'), icon: 'pi pi-fw pi-pencil', className: 'aw-menu-item' },
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
      {!courseEntity.isDeleted && (
        <>
          <Dialog
            header={translate('webApp.course.invitePeople')}
            visible={visible}
            style={{ width: '50vw' }}
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
          <div
            className="w-100 p-2"
            style={{
              padding: '2px',
              borderRadius: '10px',
              background:
                'linear-gradient(90deg, rgba(145,226,237,0.2),rgba(251, 199, 145, 0.2)), linear-gradient(180deg, rgba(253, 228, 165, 0.2), rgba(172, 180, 223, 0.2))',
            }}
          >
            <h3 className="ms-4 text-black fs-10">{courseEntity.name}</h3>
            {hasAnyAuthority(account.authorities, ['ROLE_TEACHER']) && account.id === curCourseOwnerId && (
              <Button className="d-flex flex-row align-items-center btn-invite btn btn-dark" onClick={() => setVisible(true)}>
                {translate('webApp.course.invite')}
              </Button>
            )}
          </div>
          <TabMenu
            model={items}
            unstyled={false}
            activeIndex={activeIndex}
            onTabChange={e => {
              handleMenuItemClick(TAB_INDEX[e.index], `/course/${id}/detail/${TAB_INDEX[e.index]}`);
              setActiveIndex(e.index);
            }}
          />
        </>
      )}
      {courseEntity.isDeleted && (
        <div className="w-100 p-2">
          <h3 className="ms-4 text-black fs-10">
            {courseEntity.name} {translate('webApp.course.isDeleted')}
          </h3>
        </div>
      )}
    </div>
  );
};
