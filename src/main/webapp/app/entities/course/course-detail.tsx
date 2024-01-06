import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { Button } from 'reactstrap';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import axios from 'axios';
import { Chips } from 'primereact/chips';
import { Dialog } from 'primereact/dialog';
import { MenuItem } from 'primereact/menuitem';
import { TabMenu } from 'primereact/tabmenu';
import { translate } from 'react-jhipster';
import { toast } from 'react-toastify';
import './course-detail.scss';

import { getEntity } from './course.reducer';
import ClassGrade from 'app/modules/teacher/class-management/class-grade/class-grade';
import ClassPeople from 'app/modules/teacher/class-management/class-people/class-people';
import ClassStream from 'app/modules/teacher/class-management/class-stream/class-stream';
import ClassWork from 'app/modules/teacher/class-management/class-work/class-people';

export const CourseDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  const courseEntity = useAppSelector(state => state.course.entity);
  const account = useAppSelector(state => state.authentication.account);

  const [activeIndex, setActiveIndex] = useState(0);
  const [visible, setVisible] = useState(false);
  const [isCopyInvitationCode, setIsCopyInvitationCode] = useState(false);
  const [isCopyInvitationLink, setIsCopyInvitationLink] = useState(false);
  const [invitedMails, setInvitedMails] = useState([]);
  const [disabledSendInvitation, setDisabledSendInvitation] = useState(true);

  const items: MenuItem[] = [
    { label: 'Stream', icon: 'pi pi-fw pi-home', className: 'aw-menu-item' },
    { label: 'ClassWork', icon: 'pi pi-fw pi-calendar', className: 'aw-menu-item' },
    { label: 'People', icon: 'pi pi-fw pi-people', className: 'aw-menu-item' },
    { label: 'Grade', icon: 'pi pi-fw pi-file', className: 'aw-menu-item' },
  ];

  const TAB_INDEX = {
    STREAM: 0,
    CLASS_WORK: 1,
    PEOPLE: 2,
    GRADE: 3,
  };

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const handleCopyInvitationCodeToClipboard = () => {
    navigator.clipboard.writeText(courseEntity.invitationCode);
    setIsCopyInvitationCode(true);
    setTimeout(() => {
      setIsCopyInvitationCode(false);
    }, 2000);
  };

  const handleCopyInvitationLinkToClipboard = () => {
    const baseUrl = window.location.origin;
    navigator.clipboard.writeText(`${baseUrl}/invitation/${courseEntity.invitationCode}`);
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
      axios.post(`/api/courses/send-invitation/${courseEntity.invitationCode}`, invitedMails).then(res => {
        setVisible(false);
        setInvitedMails([]);
        toast.success(translate('webApp.course.inviteSuccess'), {
          position: toast.POSITION.TOP_LEFT,
        });
      });
    }
  };
  return (
    <div className="course-detail-container">
      <div className="d-flex flex-row">
        <TabMenu
          model={items}
          unstyled={false}
          activeIndex={activeIndex}
          onTabChange={e => {
            setActiveIndex(e.index);
          }}
          className="flex-1"
        />
        {hasAnyAuthority(account.authorities, ['ROLE_TEACHER']) && (
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
      {activeIndex === TAB_INDEX.STREAM && <ClassStream></ClassStream>}
      {activeIndex === TAB_INDEX.CLASS_WORK && <ClassWork></ClassWork>}
      {activeIndex === TAB_INDEX.PEOPLE && <ClassPeople></ClassPeople>}
      {activeIndex === TAB_INDEX.GRADE && <ClassGrade></ClassGrade>}
    </div>
  );
};

export default CourseDetail;
