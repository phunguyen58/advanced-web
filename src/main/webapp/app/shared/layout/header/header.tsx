import './header.scss';

import React, { useState } from 'react';
import { Storage, Translate, translate } from 'react-jhipster';
import LoadingBar from 'react-redux-loading-bar';
import { Collapse, Nav, NavItem, NavLink, Navbar, NavbarToggler } from 'reactstrap';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch } from 'app/config/store';
import { joinAClass } from 'app/entities/course/course.reducer';
import { setLocale } from 'app/shared/reducers/locale';
import { Button } from 'primereact/button';
import { Dialog } from 'primereact/dialog';
import { InputText } from 'primereact/inputtext';
import { Link } from 'react-router-dom';
import AccountMenu from '../menus/account';
import { LocaleMenu } from '../menus';
import { Brand, Home } from './header-components';
import EntitiesMenu from 'app/entities/menu';

export interface IHeaderProps {
  isAuthenticated: boolean;
  isAdmin: boolean;
  ribbonEnv: string;
  isInProduction: boolean;
  isOpenAPIEnabled: boolean;
  currentLocale: string;
}

const Header = (props: IHeaderProps) => {
  const [menuOpen, setMenuOpen] = useState(false);

  const dispatch = useAppDispatch();

  const handleLocaleChange = event => {
    const langKey = event.target.value;
    Storage.session.set('locale', langKey);
    dispatch(setLocale(langKey));
  };

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
    window.location.href = '/my-courses';
  };

  const redirectoClassManagement = () => {
    window.location.href = '/class';
  };
  /* jhipster-needle-add-element-to-menu - JHipster will add new menu items here */

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
            {props.isAuthenticated && (
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
            <AccountMenu isAuthenticated={props.isAuthenticated} />
          </Nav>
        </Collapse>
      </Navbar>
    </div>
  );
};

export default Header;
