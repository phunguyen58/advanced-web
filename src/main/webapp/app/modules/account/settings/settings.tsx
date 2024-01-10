import React, { useEffect, useState } from 'react';
import { Button, Col, Row } from 'reactstrap';
import { Translate, translate, ValidatedField, ValidatedForm, isEmail } from 'react-jhipster';
import { toast } from 'react-toastify';
import './settings.scss';

import { locales, languages } from 'app/config/translation';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getSession } from 'app/shared/reducers/authentication';
import { saveAccountSettings, reset } from './settings.reducer';
import { AUTHORITIES } from 'app/config/constants';

export const SettingsPage = () => {
  const dispatch = useAppDispatch();
  const account = useAppSelector(state => state.authentication.account);
  const successMessage = useAppSelector(state => state.settings.successMessage);

  useEffect(() => {
    dispatch(getSession());
    return () => {
      dispatch(reset());
    };
  }, []);

  useEffect(() => {
    if (successMessage) {
      toast.success(translate(successMessage));
    }
  }, [successMessage]);

  const handleValidSubmit = values => {
    dispatch(
      saveAccountSettings({
        ...account,
        ...values,
      })
    );
  };

  return (
    <div data-cy="settingPage" className="d-flex container-fluid">
      <Row className="container-fluid">
        <Col md="8">
          {/* <h2 id="settings-title">
            <Translate contentKey="settings.title" interpolate={{ username: account.firstName }}>
              User settings for {account.login}
            </Translate>
          </h2> */}
          <ValidatedForm className="d-flex flex-column" id="settings-form" onSubmit={handleValidSubmit} defaultValues={account}>
            <ValidatedField
              name="login"
              label={translate('settings.form.login')}
              labelClass="aw-label-input"
              id="login"
              data-cy="login"
              disabled={true}
            />
            {account.authorities.includes(AUTHORITIES.STUDENT) && (
              <ValidatedField
                name="studentId"
                label={translate('settings.form.studentId')}
                labelClass="aw-label-input"
                id="studentId"
                data-cy="studentId"
                disabled={true}
              />
            )}
            <ValidatedField
              name="firstName"
              label={translate('settings.form.firstname')}
              labelClass="aw-label-input"
              id="firstName"
              placeholder={translate('settings.form.firstname.placeholder')}
              validate={{
                required: { value: true, message: translate('settings.messages.validate.firstname.required') },
                minLength: { value: 1, message: translate('settings.messages.validate.firstname.minlength') },
                maxLength: { value: 50, message: translate('settings.messages.validate.firstname.maxlength') },
              }}
              data-cy="firstname"
            />
            <ValidatedField
              name="lastName"
              label={translate('settings.form.lastname')}
              labelClass="aw-label-input"
              id="lastName"
              placeholder={translate('settings.form.lastname.placeholder')}
              validate={{
                required: { value: true, message: translate('settings.messages.validate.lastname.required') },
                minLength: { value: 1, message: translate('settings.messages.validate.lastname.minlength') },
                maxLength: { value: 50, message: translate('settings.messages.validate.lastname.maxlength') },
              }}
              data-cy="lastname"
            />
            <ValidatedField
              name="email"
              label={translate('global.form.email.label')}
              labelClass="aw-label-input"
              placeholder={translate('global.form.email.placeholder')}
              type="email"
              validate={{
                required: { value: true, message: translate('global.messages.validate.email.required') },
                minLength: { value: 5, message: translate('global.messages.validate.email.minlength') },
                maxLength: { value: 254, message: translate('global.messages.validate.email.maxlength') },
                validate: v => isEmail(v) || translate('global.messages.validate.email.invalid'),
              }}
              data-cy="email"
            />
            <ValidatedField
              type="select"
              id="langKey"
              name="langKey"
              label={translate('settings.form.language')}
              labelClass="aw-label-input"
              data-cy="langKey"
            >
              {locales.map(locale => (
                <option value={locale} key={locale}>
                  {languages[locale].name}
                </option>
              ))}
            </ValidatedField>

            <div className="aw-button-container">
              <Button className="aw-save-button" type="submit" data-cy="submit">
                <Translate contentKey="settings.form.saveChanges">Save changes</Translate>
              </Button>
            </div>
          </ValidatedForm>
        </Col>
      </Row>
    </div>
  );
};

export default SettingsPage;
