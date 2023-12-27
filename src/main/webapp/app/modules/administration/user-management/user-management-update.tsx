import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText, Label } from 'reactstrap';
import { Translate, translate, ValidatedField, ValidatedForm, isEmail } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { locales, languages } from 'app/config/translation';
import { getUser, getRoles, updateUser, createUser, reset } from './user-management.reducer';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { MultiSelect } from 'primereact/multiselect';
import { Checkbox } from 'primereact/checkbox';
import { Dropdown } from 'primereact/dropdown';
import { Skeleton } from 'primereact/skeleton';

export const UserManagementUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const [userAuthorities, setUserAuthorities] = useState([]);
  const [userActivated, setUserActivated] = useState(false);

  const { login } = useParams<'login'>();
  const isNew = login === undefined;

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getUser(login));
    }
    dispatch(getRoles());
    return () => {
      dispatch(reset());
    };
  }, [login]);

  const handleClose = () => {
    navigate('/admin/user-management');
  };

  const saveUser = values => {
    values.authorities = userAuthorities;
    values.activated = userActivated;
    if (isNew) {
      dispatch(createUser(values));
    } else {
      dispatch(updateUser(values));
    }
    handleClose();
  };

  const isInvalid = false;
  const user = useAppSelector(state => state.userManagement.user);
  const loading = useAppSelector(state => state.userManagement.loading);
  const updating = useAppSelector(state => state.userManagement.updating);
  const authorities = useAppSelector(state => state.userManagement.authorities);

  useEffect(() => {
    setUserAuthorities(user.authorities);
    setUserActivated(user.activated);
  }, [user.authorities]);

  return (
    <div className="p-2">
      <Row className="justify-content-center">
        <Col md="12">
          <h1>
            {isNew ? (
              <Translate contentKey="userManagement.home.createLabel">Create a User</Translate>
            ) : (
              <Translate contentKey="userManagement.home.editLabel">Edit a User</Translate>
            )}
          </h1>
        </Col>
      </Row>

      <div>
        {loading ? (
          <div>
            <Skeleton className="mb-2"></Skeleton>
            <Skeleton width="10rem" className="mb-2"></Skeleton>
            <Skeleton width="5rem" className="mb-2"></Skeleton>
            <Skeleton height="2rem" className="mb-2"></Skeleton>
            <Skeleton width="10rem" height="4rem"></Skeleton>
          </div>
        ) : (
          <ValidatedForm onSubmit={saveUser} defaultValues={user}>
            <Row>
              {user.id ? (
                <Col md="6">
                  <ValidatedField
                    type="text"
                    name="id"
                    required
                    readOnly
                    label={translate('global.field.id')}
                    value={user.id}
                    validate={{ required: true }}
                  />
                </Col>
              ) : null}
              <Col md="6">
                <ValidatedField
                  type="text"
                  name="login"
                  label={translate('userManagement.login')}
                  value={user.login}
                  validate={{
                    required: {
                      value: true,
                      message: translate('register.messages.validate.login.required'),
                    },
                    pattern: {
                      value: /^[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$|^[_.@A-Za-z0-9-]+$/,
                      message: translate('register.messages.validate.login.pattern'),
                    },
                    minLength: {
                      value: 1,
                      message: translate('register.messages.validate.login.minlength'),
                    },
                    maxLength: {
                      value: 50,
                      message: translate('register.messages.validate.login.maxlength'),
                    },
                  }}
                />
              </Col>
              <Col md="6">
                <ValidatedField
                  type="text"
                  name="firstName"
                  label={translate('userManagement.firstName')}
                  validate={{
                    maxLength: {
                      value: 50,
                      message: translate('entity.validation.maxlength', { max: 50 }),
                    },
                  }}
                  value={user.firstName}
                />
              </Col>
              <Col md="6">
                <ValidatedField
                  type="text"
                  name="lastName"
                  label={translate('userManagement.lastName')}
                  validate={{
                    maxLength: {
                      value: 50,
                      message: translate('entity.validation.maxlength', { max: 50 }),
                    },
                  }}
                  value={user.lastName}
                />
                {/* <FormText>This field cannot be longer than 50 characters.</FormText> */}
              </Col>
              <Col md="6">
                <ValidatedField
                  name="email"
                  label={translate('global.form.email.label')}
                  value={user.email}
                  placeholder={translate('global.form.email.placeholder')}
                  type="email"
                  validate={{
                    required: {
                      value: true,
                      message: translate('global.messages.validate.email.required'),
                    },
                    minLength: {
                      value: 5,
                      message: translate('global.messages.validate.email.minlength'),
                    },
                    maxLength: {
                      value: 254,
                      message: translate('global.messages.validate.email.maxlength'),
                    },
                    validate: v => isEmail(v) || translate('global.messages.validate.email.invalid'),
                  }}
                />
              </Col>
              <Col md="6">
                <ValidatedField type="select" name="langKey" label={translate('userManagement.langKey')}>
                  {locales.map(locale => (
                    <option value={locale} key={locale}>
                      {languages[locale].name}
                    </option>
                  ))}
                </ValidatedField>
              </Col>
              <Col md="6">
                <div className="d-flex flex-column gap-2">
                  <label>{translate('userManagement.profiles')}</label>
                  <MultiSelect
                    name="authorities"
                    options={authorities}
                    value={userAuthorities}
                    onChange={e => {
                      setUserAuthorities(e.value);
                    }}
                  />
                </div>
                {/* <ValidatedField type="select" name="authorities" multiple label={translate('userManagement.profiles')}>
                  {authorities.map(role => (
                    <option value={role} key={role}>
                      {role}
                    </option>
                  ))}
                </ValidatedField> */}
              </Col>
              {user.id ? (
                <Col md="6">
                  <Label>{translate('userManagement.activated')}</Label>
                  <ValidatedField
                    type="checkbox"
                    name="activated"
                    checked={userActivated}
                    value={userActivated}
                    onChange={e => setUserActivated(e.target.checked)}
                  />
                </Col>
              ) : null}
            </Row>
            <div className="d-flex justify-content-end">
              <Button tag={Link} to="/admin/user-management" replace color="outline-dark">
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="success" type="submit" disabled={isInvalid || updating}>
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </div>
          </ValidatedForm>
        )}
      </div>
    </div>
  );
};

export default UserManagementUpdate;
