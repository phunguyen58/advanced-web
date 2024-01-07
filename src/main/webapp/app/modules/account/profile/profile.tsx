import React, { useState } from 'react';
import { Translate } from 'react-jhipster';
import './profile.scss';

import { useAppSelector } from 'app/config/store';
import PasswordPage from '../password/password';
import SettingsPage from '../settings/settings';

export const ProfilePage = () => {
  const account = useAppSelector(state => state.authentication.account);
  const [selectedItem, setSelectedItem] = useState(0);

  return (
    <div className="container-lg mt-4">
      <div className="aw-account-info d-flex flex-row gap-1 mb-3 ps-3">
        <i className="pi pi-user align-self-center"></i>
        <div className="d-flex flex-column gap-1 ms-2">
          <span className="aw-account-text">
            {account.firstName} {account.lastName}
          </span>
          <span className="aw-account-sub-text">
            <Translate contentKey="settings.description">Update your username and manage your account</Translate>
          </span>
        </div>
      </div>
      <div className="d-flex container-lg flex-row p-0">
        <div className="d-flex col-4">
          <div className="aw-list container-fluid d-flex flex-column p-0">
            <div className={'aw-menu-item-text aw-item ' + (selectedItem === 0 ? 'aw-bold' : '')} onClick={() => setSelectedItem(0)}>
              <Translate contentKey="settings.general">Gereral</Translate>
            </div>
            <div className={'aw-menu-item-text aw-item ' + (selectedItem === 1 ? 'aw-bold' : '')} onClick={() => setSelectedItem(1)}>
              <Translate contentKey="settings.security">Password</Translate>
            </div>
            <div className="aw-divider aw-item" />
          </div>
        </div>
        <div className="d-flex col-8">
          {selectedItem === 0 && <SettingsPage />}
          {selectedItem === 1 && <PasswordPage />}
        </div>
      </div>
    </div>
  );
};

export default ProfilePage;
