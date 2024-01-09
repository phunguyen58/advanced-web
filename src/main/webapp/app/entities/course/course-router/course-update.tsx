import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import {
  convertDateTimeFromServer,
  convertDateTimeToServer,
  displayDefaultDateTime,
  displayDefaultDateTimeCreateClass,
} from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICourse } from 'app/shared/model/course.model';
import { getEntity, updateEntity, createEntity, reset } from './course.reducer';

export const CourseUpdate = ({ onEventTrigger }) => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const courseEntity = useAppSelector(state => state.course.entity);
  const loading = useAppSelector(state => state.course.loading);
  const updating = useAppSelector(state => state.course.updating);
  const updateSuccess = useAppSelector(state => state.course.updateSuccess);

  const handleClose = () => {
    emitEvent();
  };

  const emitEvent = () => {
    // Call the function passed as a prop to emit the event
    onEventTrigger(false);
  };

  useEffect(() => {
    if (isNew) {
      // dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.expirationDate = convertDateTimeToServer(values.expirationDate);
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);

    const entity = {
      ...courseEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          expirationDate: displayDefaultDateTimeCreateClass(),
          createdDate: displayDefaultDateTime(),
          lastModifiedDate: displayDefaultDateTime(),
        }
      : {
          ...courseEntity,
          expirationDate: convertDateTimeFromServer(courseEntity.expirationDate),
          createdDate: convertDateTimeFromServer(courseEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(courseEntity.lastModifiedDate),
        };

  const handleBackButtonClick = () => {
    navigate(`/course/${id}/detail/stream`);
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          {!isNew && (
            <h2 id="webApp.course.home.createOrEditLabel" data-cy="CourseCreateUpdateHeading">
              <Translate contentKey="webApp.course.home.createOrEditLabel">Create or edit a Course</Translate>
            </h2>
          )}
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="course-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('webApp.course.code')}
                id="course-code"
                name="code"
                data-cy="code"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('webApp.course.name')}
                id="course-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              {/* <ValidatedField
                label={translate('webApp.course.ownerId')}
                id="course-ownerId"
                name="ownerId"
                data-cy="ownerId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              /> */}
              <ValidatedField
                label={translate('webApp.course.description')}
                id="course-description"
                name="description"
                data-cy="description"
                type="text"
              />
              {/* <ValidatedField
                label={translate('webApp.course.invitationCode')}
                id="course-invitationCode"
                name="invitationCode"
                data-cy="invitationCode"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              /> */}
              <ValidatedField
                label={translate('webApp.course.expirationDate')}
                id="course-expirationDate"
                name="expirationDate"
                data-cy="expirationDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              {/* <ValidatedField
                label={translate('webApp.course.isDeleted')}
                id="course-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('webApp.course.createdBy')}
                id="course-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('webApp.course.createdDate')}
                id="course-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('webApp.course.lastModifiedBy')}
                id="course-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('webApp.course.lastModifiedDate')}
                id="course-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              /> */}
              {!isNew && (
                <Button onClick={() => handleBackButtonClick()} id="cancel-save" data-cy="entityCreateCancelButton" color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">
                    <Translate contentKey="entity.action.back">Back</Translate>
                  </span>
                </Button>
              )}
              &nbsp;
              {courseEntity.isDeleted !== true && (
                <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp;
                  <Translate contentKey="entity.action.save">Save</Translate>
                </Button>
              )}
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default CourseUpdate;
