import './header.scss';

import React, { useState } from 'react';
import { Translate, Storage, translate } from 'react-jhipster';
import { Navbar, Nav, NavbarToggler, Collapse, NavItem, NavLink } from 'reactstrap';
import LoadingBar from 'react-redux-loading-bar';

import { Home, Brand } from './header-components';
import { AdminMenu, EntitiesMenu, AccountMenu, LocaleMenu } from '../menus';
import { useAppDispatch } from 'app/config/store';
import { setLocale } from 'app/shared/reducers/locale';
import { Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faHeart } from '@fortawesome/free-solid-svg-icons/faHeart';
import { Button } from 'primereact/button';
import { Dialog } from 'primereact/dialog';
import { InputText } from 'primereact/inputtext';
import { joinAClass } from 'app/entities/course/course.reducer';

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
    dispatch(joinAClass(codeToJoinClass));
  };
  /* jhipster-needle-add-element-to-menu - JHipster will add new menu items here */

  return (
    <div id="app-header">
      <Dialog
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
              className="w-100"
              value={codeToJoinClass}
              onChange={e => setCodeToJoinClass(e.target.value)}
              onKeyDown={handleKeyDown}
            />
            <Button
              disabled={!codeToJoinClass}
              className="w-50"
              label={translate('webApp.course.join')}
              icon="pi pi-external-link"
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
                label={translate('webApp.course.joinacourse')}
                icon="pi pi-external-link"
                onClick={() => toggleJoinClassPanel(true)}
              />
            )}
            <Home />
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
