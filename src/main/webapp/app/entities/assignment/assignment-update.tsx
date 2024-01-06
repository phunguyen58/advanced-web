import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICourse } from 'app/shared/model/course.model';
import { getEntities as getCourses } from 'app/entities/course/course-router/course.reducer';
import { IGradeComposition } from 'app/shared/model/grade-composition.model';
import { getEntities as getGradeCompositions } from 'app/entities/grade-composition/grade-composition.reducer';
import { IAssignment } from 'app/shared/model/assignment.model';
import { getEntity, updateEntity, createEntity, reset } from './assignment.reducer';

export const AssignmentUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const courses = useAppSelector(state => state.course.entities);
  const gradeCompositions = useAppSelector(state => state.gradeComposition.entities);
  const assignmentEntity = useAppSelector(state => state.assignment.entity);
  const loading = useAppSelector(state => state.assignment.loading);
  const updating = useAppSelector(state => state.assignment.updating);
  const updateSuccess = useAppSelector(state => state.assignment.updateSuccess);

  const handleClose = () => {
    navigate('/assignment' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getCourses({}));
    dispatch(getGradeCompositions({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);

    const entity = {
      ...assignmentEntity,
      ...values,
      course: courses.find(it => it.id.toString() === values.course.toString()),
      gradeComposition: gradeCompositions.find(it => it.id.toString() === values.gradeComposition.toString()),
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
          createdDate: displayDefaultDateTime(),
          lastModifiedDate: displayDefaultDateTime(),
        }
      : {
          ...assignmentEntity,
          createdDate: convertDateTimeFromServer(assignmentEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(assignmentEntity.lastModifiedDate),
          course: assignmentEntity?.course?.id,
          gradeComposition: assignmentEntity?.gradeComposition?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="webApp.assignment.home.createOrEditLabel" data-cy="AssignmentCreateUpdateHeading">
            <Translate contentKey="webApp.assignment.home.createOrEditLabel">Create or edit a Assignment</Translate>
          </h2>
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
                  id="assignment-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('webApp.assignment.name')}
                id="assignment-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('webApp.assignment.description')}
                id="assignment-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label={translate('webApp.assignment.weight')}
                id="assignment-weight"
                name="weight"
                data-cy="weight"
                type="text"
              />
              <ValidatedField
                label={translate('webApp.assignment.isDeleted')}
                id="assignment-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('webApp.assignment.createdBy')}
                id="assignment-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('webApp.assignment.createdDate')}
                id="assignment-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('webApp.assignment.lastModifiedBy')}
                id="assignment-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('webApp.assignment.lastModifiedDate')}
                id="assignment-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="assignment-course"
                name="course"
                data-cy="course"
                label={translate('webApp.assignment.course')}
                type="select"
              >
                <option value="" key="0" />
                {courses
                  ? courses.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="assignment-gradeComposition"
                name="gradeComposition"
                data-cy="gradeComposition"
                label={translate('webApp.assignment.gradeComposition')}
                type="select"
              >
                <option value="" key="0" />
                {gradeCompositions
                  ? gradeCompositions.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/assignment" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default AssignmentUpdate;
