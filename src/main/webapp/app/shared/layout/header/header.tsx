import './header.scss';

import React, { useState, useEffect, useRef } from 'react';
import { Storage, Translate, translate } from 'react-jhipster';
import LoadingBar from 'react-redux-loading-bar';
import { Collapse, Nav, NavItem, NavLink, Navbar, NavbarToggler } from 'reactstrap';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { joinAClass } from 'app/entities/course/course-router/course.reducer';
import { setLocale } from 'app/shared/reducers/locale';
import { Button } from 'primereact/button';
import { Dialog } from 'primereact/dialog';
import { InputText } from 'primereact/inputtext';
import { Link, useNavigate } from 'react-router-dom';
import AccountMenu from '../menus/account';
import { LocaleMenu } from '../menus';
import { Brand, Home } from './header-components';
import EntitiesMenu from 'app/entities/menu';
import { AUTHORITIES } from 'app/config/constants';
import { listener } from 'app/config/websocket-middleware';
import axios from 'axios';
import { OverlayPanel } from 'primereact/overlaypanel';
import { toast } from 'react-toastify';
import { set } from 'lodash';

export interface IHeaderProps {
  isAuthenticated: boolean;
  isAdmin: boolean;
  ribbonEnv: string;
  isInProduction: boolean;
  isOpenAPIEnabled: boolean;
  currentLocale: string;
}

const Header = (props: IHeaderProps) => {
  const op = useRef(null);
  const [menuOpen, setMenuOpen] = useState(false);
  const [notifications, setNotifications] = useState([]);
  const [visibleNotificationDialog, setVisibleNotificationDialog] = useState(false);

  const dispatch = useAppDispatch();
  const navigate = useNavigate();

  const handleLocaleChange = event => {
    const langKey = event.target.value;
    Storage.session.set('locale', langKey);
    dispatch(setLocale(langKey));
  };

  const account = useAppSelector(state => state.authentication.account);

  const renderDevRibbon = () =>
    props.isInProduction === false ? (
      <div className="ribbon dev">
        <a href="">
          <Translate contentKey={`global.ribbon.${props.ribbonEnv}`} />
        </a>
      </div>
    ) : null;

  const toggleMenu = () => setMenuOpen(!menuOpen);

  const [isDisplayedJoinClass, toggleJoinClassPanel] = useState(false);

  const [codeToJoinClass, setCodeToJoinClass] = useState('');

  const handleKeyDown = (event: React.KeyboardEvent<HTMLInputElement>) => {
    if (event.key === 'Enter') {
      event.preventDefault(); // Prevent default behavior (e.g., form submission)
      handleCodeEntry();
    }
  };

  const handleCodeEntry = () => {
    dispatch(joinAClass(codeToJoinClass)).finally(() => {
      toggleJoinClassPanel(false);
      setCodeToJoinClass('');
    });
  };

  const redirectToMyCourses = () => {
    window.location.href = '/my-classes';
  };

  const redirectoClassManagement = () => {
    window.location.href = '/class';
  };

  const redirectToAdmin = () => {
    window.location.href = '/admin/user-management';
  };
  /* jhipster-needle-add-element-to-menu - JHipster will add new menu items here */

  useEffect(() => {
    const fetchNotifications = (prevNotiLen, isSocketCall) => {
      axios.get(`/api/notifications?receivers.equals=${account.login}&isRead.equals=false&sort=id,desc`).then(res => {
        if (res.data.length > prevNotiLen && isSocketCall) {
          toast.info(translate(`pushNotification.${res.data[0].message}`), { position: toast.POSITION.BOTTOM_RIGHT });
        }
        setNotifications(res.data);
      });
    };

    const handleMessage = (message, prevNotiLen) => {
      if (message?.type === 'notification') {
        setTimeout(() => {
          fetchNotifications(prevNotiLen, true);
        }, 2000);
      }
    };

    listener?.subscribe((message: any) => {
      handleMessage(message, notifications.length);
    });
    fetchNotifications(0, false);
  }, [listener]);

  const toggleNotificationDialog = () => {
    setVisibleNotificationDialog(!visibleNotificationDialog);
  };

  const clickNotification = notification => {
    navigate(notification.link);
    setVisibleNotificationDialog(false);
    axios.put(`/api/notifications/${notification.id}`, { id: notification.id, isRead: true }).then(res => {
      setNotifications(notifications.filter(n => n.id !== notification.id));
    });
  };

  return (
    <div id="app-header">
      <Dialog
        className="join-a-course-dialog"
        header={translate('webApp.course.joinacourse')}
        draggable={false}
        resizable={false}
        visible={isDisplayedJoinClass}
        style={{ width: '30vw' }}
        onHide={() => toggleJoinClassPanel(false)}
      >
        <div className="d-flex flex-column">
          <span className="align-self-center">
            <Translate contentKey="webApp.course.enterinvitationcode" />
          </span>
          <p className="d-flex flex-column m-0 gap-2 align-items-center">
            <InputText
              className="aw-input w-100"
              value={codeToJoinClass}
              onChange={e => setCodeToJoinClass(e.target.value)}
              onKeyDown={handleKeyDown}
            />
            <Button
              disabled={!codeToJoinClass}
              className="aw-btn-join w-50"
              label={translate('webApp.course.join')}
              icon="pi pi-plus"
              onClick={handleCodeEntry}
            />
          </p>
        </div>
      </Dialog>
      {/* {renderDevRibbon()} */}
      <LoadingBar className="loading-bar" />
      <Navbar data-cy="navbar" dark expand="md" fixed="top" className="jh-navbar">
        <Brand />
        <NavbarToggler style={{ background: '#000' }} aria-label="Menu" onClick={toggleMenu} />
        <Collapse isOpen={menuOpen} navbar>
          <Nav id="header-tabs" className="ms-auto" navbar>
            {props.isAuthenticated && account.authorities.includes(AUTHORITIES.ADMIN) && (
              <Button
                className="aw-btn-header mr-2"
                label={translate('global.menu.admin.main')}
                icon="pi pi-user"
                onClick={() => redirectToAdmin()}
              />
            )}
            {props.isAuthenticated && (
              <Button
                className="aw-btn-header mr-2"
                label={translate('webApp.course.joinacourse')}
                icon="pi pi-plus"
                onClick={() => toggleJoinClassPanel(true)}
              />
            )}
            {props.isAuthenticated && (
              <Button
                className="aw-btn-header mr-2"
                label={translate('webApp.course.home.myCourses')}
                icon="pi pi-bookmark"
                onClick={redirectToMyCourses}
              />
            )}
            {props.isAuthenticated && account.authorities.includes(AUTHORITIES.TEACHER) && (
              <Button
                className="aw-btn-header"
                label={translate('webApp.course.home.classManagement')}
                icon="pi pi-briefcase"
                onClick={redirectoClassManagement}
              />
            )}
            {/* {props.isAuthenticated && <EntitiesMenu />} */}
            {!props.isAuthenticated && (
              <NavItem active={true}>
                <NavLink tag={Link} to="/landing" className="d-flex align-items-center">
                  <FontAwesomeIcon icon="heart" /> <span>Landing Page</span>
                </NavLink>
              </NavItem>
            )}
            <LocaleMenu currentLocale={props.currentLocale} onClick={handleLocaleChange} />
            {props.isAuthenticated &&
              (account.authorities.includes(AUTHORITIES.TEACHER) || account.authorities.includes(AUTHORITIES.STUDENT)) && (
                // notification button
                <div className="btn-notification-container">
                  <Button className="btn-notification" onClick={() => toggleNotificationDialog()}>
                    <FontAwesomeIcon icon="bell" />
                  </Button>
                  {notifications.length > 0 && (
                    <>
                      <span className="notification-number">{notifications.length}</span>
                      {visibleNotificationDialog && (
                        <div className="notification-dialog">
                          <div className="d-flex flex-column gap-1">
                            {notifications.map((notification, index) => (
                              <div key={index} className="notification-item" onClick={() => clickNotification(notification)}>
                                <span>{translate(`pushNotification.${notification.message}`)}</span>
                                {!notification.isRead && (
                                  <div className="dot-container">
                                    <span className="dot"></span>
                                  </div>
                                )}
                              </div>
                            ))}
                          </div>
                        </div>
                      )}
                    </>
                  )}
                </div>
              )}
            <AccountMenu isAuthenticated={props.isAuthenticated} />
          </Nav>
        </Collapse>
      </Navbar>
    </div>
  );
};

export default Header;
