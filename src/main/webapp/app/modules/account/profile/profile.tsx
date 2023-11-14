import React, { useEffect, useState } from 'react';
import { Button, Col, Row } from 'reactstrap';
import { Translate, translate, ValidatedField, ValidatedForm, isEmail } from 'react-jhipster';
import { toast } from 'react-toastify';
import './profile.scss';

import { locales, languages } from 'app/config/translation';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getSession } from 'app/shared/reducers/authentication';
import SettingsPage from '../settings/settings';
import PasswordPage from '../password/password';

export const ProfilePage = () => {
  const account = useAppSelector(state => state.authentication.account);
  const [selectedItem, setSelectedItem] = useState(0);

  return (
    <div className="container-lg mt-4">
      <div className="aw-account-info d-flex flex-row gap-1 mb-3 ps-3">
        <img src="../../../../content/images/default_profile.svg" alt="" />
        <div className="d-flex flex-column gap-1 ms-2">
          <span className="aw-account-text">{account.firstName}</span>
          <span className="aw-account-sub-text">Update your username and manage your account</span>
        </div>
      </div>
      <div className="d-flex container-lg flex-row">
        <div className="d-flex col-4">
          <div className="aw-list container-fluid d-flex flex-column">
            <div className={'aw-menu-item-text aw-item ' + (selectedItem === 0 ? 'aw-bold' : '')} onClick={() => setSelectedItem(0)}>
              Gereral
            </div>
            <div className={'aw-menu-item-text aw-item ' + (selectedItem === 1 ? 'aw-bold' : '')} onClick={() => setSelectedItem(1)}>
              Password
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
