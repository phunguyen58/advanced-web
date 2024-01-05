import React, { useEffect, useRef, useState } from 'react';
import { Button } from 'primereact/button';
import { Dialog } from 'primereact/dialog';
import { InputText } from 'primereact/inputtext';
import './create-class-modal.scss';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { useNavigate } from 'react-router-dom';
import { ICourse } from 'app/shared/model/course.model';

import { translate } from 'react-jhipster';
import { useFormik } from 'formik';
import { initCourse } from 'app/entities/course/courseUtil';
import { classNames } from 'primereact/utils';

import * as _ from 'lodash';

export interface ICreateClassModalProp {
  visible: boolean;
  setVisible: any;
  onCreateClass: (clazz: ICourse) => void;
}
const CreateClassModal = (prop: ICreateClassModalProp) => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();

  const account = useAppSelector(state => state.authentication.account);
  const assignments = useAppSelector(state => state.assignment.entities);
  const courseEntity = useAppSelector(state => state.course.entity);
  const loading = useAppSelector(state => state.course.loading);
  //TODO: handle loading and updating
  const updating = useAppSelector(state => state.course.updating);
  const updateSuccess = useAppSelector(state => state.course.updateSuccess);

  const formik = useFormik({
    initialValues: initCourse(),
    validate: (data: ICourse) => {
      let errors: any = {};

      if (_.isEmpty(data.name)) {
        errors.name = 'Class name is required.';
      }

      if (_.isEmpty(data.code)) {
        errors.code = 'code is required.';
      }

      return errors;
    },
    onSubmit: (data: ICourse) => {
      prop.onCreateClass({
        ownerId: account.id,
        createdBy: account.id,
        lastModifiedBy: account.id,
        ...data,
      });
      formik.resetForm();
    },
  });

  const isFormFieldInvalid = name => !!(formik.touched?.[name] && formik.errors[name]);
  const getFormErrorMessage = name => {
    return isFormFieldInvalid(name) ? <small className="p-error">{formik.errors[name]}</small> : null;
  };

  const footerContent = (
    <div className="aw-footer-container">
      <Button
        label={translate('webApp.classManagement.action.cancel')}
        icon="pi pi-times"
        onClick={() => prop.setVisible(false)}
        className="aw-cancel-btn p-button-text"
      />
      <Button
        className="aw-create-btn"
        label={translate('webApp.classManagement.action.create')}
        icon="pi pi-check"
        onClick={() => {
          prop.setVisible(false);
          formik.submitForm().then(value => console.warn(value));
        }}
        autoFocus
      />
    </div>
  );

  return (
    <div className="aw-create-class-modal-container">
      <Dialog
        header={translate('webApp.classManagement.createNewClass')}
        visible={prop.visible}
        style={{ width: '50vw' }}
        onHide={() => prop.setVisible(false)}
        footer={footerContent}
        contentClassName="aw-dialog"
        className="aw-create-class-dialog-container"
      >
        <div className="d-flex w-100">
          <form onSubmit={formik.handleSubmit} className="justify-content-center w-100 aw-dialog-content">
            <span className="p-float-label">
              <InputText
                className={classNames({ 'p-invalid': isFormFieldInvalid('name'), 'w-100': true })}
                id="name"
                value={formik.values.name}
                onChange={e => formik.setFieldValue('name', e.target.value)}
                onBlur={formik.handleBlur}
              />
              <label htmlFor="name">{translate('webApp.classManagement.className')}</label>
            </span>
            <span className="p-float-label">
              <InputText
                className={classNames({ 'p-invalid': isFormFieldInvalid('code'), 'w-100': true })}
                id="code"
                value={formik.values.code}
                onChange={e => formik.setFieldValue('code', e.target.value)}
                onBlur={formik.handleBlur}
              />
              <label htmlFor="code">{translate('webApp.classManagement.code')}</label>
            </span>
            <span className="p-float-label">
              <InputText
                className={classNames({ 'p-invalid': isFormFieldInvalid('description'), 'w-100': true })}
                id="description"
                value={formik.values.description}
                onChange={e => formik.setFieldValue('description', e.target.value)}
                onBlur={formik.handleBlur}
              />
              <label htmlFor="description">{translate('webApp.classManagement.description')}</label>
            </span>
          </form>
        </div>
      </Dialog>
    </div>
  );
};

export default CreateClassModal;
