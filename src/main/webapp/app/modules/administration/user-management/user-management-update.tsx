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
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import { AUTHORITIES } from 'app/config/constants';

export const UserManagementUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const [userId, setUserId] = useState(null);
  const [userLogin, setUserLogin] = useState('');
  const [userFirstName, setUserFirstName] = useState('');
  const [userLastName, setUserLastName] = useState('');
  const [userEmail, setUserEmail] = useState('');
  const [userStudentId, setUserStudentId] = useState('');
  const [userAuthorities, setUserAuthorities] = useState([]);
  const [userActivated, setUserActivated] = useState(false);
  const [userLangKey, setUserLangKey] = useState('');

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
    values.id = userId;
    values.login = userLogin;
    values.firstName = userFirstName;
    values.lastName = userLastName;
    values.email = userEmail;
    values.studentId = userStudentId;
    values.langKey = userLangKey;
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
    setUserId(user.id);
    setUserLogin(user.login);
    setUserFirstName(user.firstName);
    setUserLastName(user.lastName);
    setUserEmail(user.email);
    setUserStudentId(user.studentId);
    setUserLangKey(user.langKey);
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
                    value={userId}
                    validate={{ required: true }}
                    onChange={e => {
                      setUserId(e.target.value);
                    }}
                  />
                </Col>
              ) : null}
              <Col md="6">
                <ValidatedField
                  type="text"
                  name="login"
                  label={translate('userManagement.login')}
                  value={user.login}
                  readOnly={Boolean(user.login)}
                  onChange={e => {
                    setUserLogin(e.target.value);
                  }}
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
                  value={userFirstName}
                  onChange={e => {
                    setUserFirstName(e.target.value);
                  }}
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
                  value={userLastName}
                  onChange={e => {
                    setUserLastName(e.target.value);
                  }}
                />
                {/* <FormText>This field cannot be longer than 50 characters.</FormText> */}
              </Col>
              <Col md="6">
                <ValidatedField
                  name="email"
                  label={translate('global.form.email.label')}
                  value={userEmail}
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
                  onChange={e => {
                    setUserEmail(e.target.value);
                  }}
                />
              </Col>
              <Col md="6">
                <ValidatedField
                  type="select"
                  name="langKey"
                  label={translate('userManagement.langKey')}
                  onChange={e => {
                    setUserLangKey(e.target.value);
                  }}
                >
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
              {hasAnyAuthority(userAuthorities, [AUTHORITIES.STUDENT]) ? (
                <Col md="6">
                  <ValidatedField
                    type="text"
                    name="studentId"
                    label={translate('userManagement.studentId')}
                    validate={{
                      maxLength: {
                        value: 10,
                        message: translate('entity.validation.maxlength', { max: 10 }),
                      },
                    }}
                    value={userStudentId}
                    onChange={e => {
                      setUserStudentId(e.target.value);
                    }}
                  />
                </Col>
              ) : null}
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
