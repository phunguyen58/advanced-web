import React, { useEffect, useState } from 'react';
import { Translate, ValidatedField, translate } from 'react-jhipster';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, FormText, Label, Row } from 'reactstrap';

import { AUTHORITIES } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { languages, locales } from 'app/config/translation';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import { Field, Form, Formik } from 'formik';
import { MultiSelect } from 'primereact/multiselect';
import { Skeleton } from 'primereact/skeleton';
import * as Yup from 'yup';
import { createUser, getRoles, getUser, reset, updateUser } from './user-management.reducer';

export const UserManagementUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const [userAuthorities, setUserAuthorities] = useState([]);
  const [userActivated, setUserActivated] = useState(false);
  const [userLangKey, setUserLangKey] = useState('en');

  const { login } = useParams<'login'>();
  const isNew = login === undefined;

  const validationSchema = Yup.object().shape({
    login: Yup.string()
      .required(translate('register.messages.validate.login.required'))
      .matches(
        /^[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$|^[_.@A-Za-z0-9-]+$/,
        translate('register.messages.validate.login.pattern')
      )
      .min(1, translate('register.messages.validate.login.minlength'))
      .max(50, translate('register.messages.validate.login.maxlength')),
    firstName: Yup.string()
      .required(translate('entity.validation.required'))
      .max(50, translate('entity.validation.maxlength', { max: 50 })),
    lastName: Yup.string()
      .required(translate('entity.validation.required'))
      .max(50, translate('entity.validation.maxlength', { max: 50 })),
    email: Yup.string()
      .required(translate('global.messages.validate.email.required'))
      .min(5, translate('global.messages.validate.email.minlength'))
      .max(254, translate('global.messages.validate.email.maxlength'))
      .matches(/^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/, translate('global.messages.validate.email.invalid')),
    studentId: Yup.string().max(10, translate('entity.validation.maxlength', { max: 10 })),
  });

  const initialValues = {
    id: null,
    login: '',
    firstName: '',
    lastName: '',
    email: '',
    studentId: '',
    authorities: [AUTHORITIES.USER],
    activated: false,
    langKey: 'en',
  };

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
    values.langKey = userLangKey ? userLangKey : 'en';

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
    setUserLangKey(user.langKey);
  }, [user.authorities, user.activated, user.langKey]);

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
          <div>
            <Formik initialValues={isNew ? initialValues : user} validationSchema={validationSchema} onSubmit={saveUser}>
              {({ touched, errors, values, handleChange, handleSubmit }) => (
                <Form>
                  <Row>
                    {user.id ? (
                      <Col md="6">
                        <Label>{translate('global.field.id')}</Label>

                        <Field className="field" type="text" name="id" required readOnly value={user.id} validate={{ required: true }} />
                      </Col>
                    ) : null}
                    <Col md="6" className="mb-3">
                      <Label>{translate('userManagement.login')}</Label>
                      <Field
                        className="field"
                        type="text"
                        name="login"
                        value={values.login}
                        readOnly={Boolean(user.login)}
                        onChange={handleChange('login')}
                      />
                      {touched.login && errors.login && (
                        <FormText color="danger">
                          <span className="error-message">{errors.login as string}</span>
                        </FormText>
                      )}
                    </Col>
                    <Col md="6" className="mb-3">
                      <Label>{translate('userManagement.firstName')}</Label>

                      <Field className="field" type="text" name="firstName" value={values.firstName} onChange={handleChange('firstName')} />
                      {touched.firstName && errors.firstName && (
                        <FormText color="danger">
                          <span className="error-message">{errors.firstName as string}</span>
                        </FormText>
                      )}
                    </Col>
                    <Col md="6" className="mb-3">
                      <Label>{translate('userManagement.lastName')}</Label>

                      <Field className="field" type="text" name="lastName" value={values.lastName} onChange={handleChange('lastName')} />
                      {touched.lastName && errors.lastName && (
                        <FormText color="danger">
                          <span className="error-message">{errors.lastName as string}</span>
                        </FormText>
                      )}
                    </Col>
                    <Col md="6" className="mb-3">
                      <Label>{translate('global.form.email.label')}</Label>
                      <Field
                        className="field"
                        name="email"
                        value={values.email}
                        placeholder={translate('global.form.email.placeholder')}
                        type="email"
                        onChange={handleChange('email')}
                      />
                      {touched.email && errors.email && (
                        <FormText color="danger">
                          <span className="error-message">{errors.email as string}</span>
                        </FormText>
                      )}
                    </Col>
                    <Col md="6" className="mb-3">
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
                    <Col md="6" className="mb-3">
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
                    </Col>
                    {hasAnyAuthority(userAuthorities, [AUTHORITIES.STUDENT]) ? (
                      <Col md="6" className="mb-3">
                        <Label>{translate('userManagement.studentId')}</Label>
                        <Field
                          className="field"
                          type="text"
                          name="studentId"
                          value={values.studentId}
                          onChange={handleChange('studentId')}
                        />
                        {touched.studentId && errors.studentId && (
                          <FormText color="danger">
                            <span className="error-message">{errors.studentId as string}</span>
                          </FormText>
                        )}
                      </Col>
                    ) : null}
                    {user.id ? (
                      <Col md="6" className="mb-3">
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
                </Form>
              )}
            </Formik>
          </div>
        )}
      </div>
    </div>
  );
};

export default UserManagementUpdate;
