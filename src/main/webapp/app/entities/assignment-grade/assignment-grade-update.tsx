import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IAssignment } from 'app/shared/model/assignment.model';
import { getEntities as getAssignments } from 'app/entities/assignment/assignment.reducer';
import { IAssignmentGrade } from 'app/shared/model/assignment-grade.model';
import { getEntity, updateEntity, createEntity, reset } from './assignment-grade.reducer';

export const AssignmentGradeUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const assignments = useAppSelector(state => state.assignment.entities);
  const assignmentGradeEntity = useAppSelector(state => state.assignmentGrade.entity);
  const loading = useAppSelector(state => state.assignmentGrade.loading);
  const updating = useAppSelector(state => state.assignmentGrade.updating);
  const updateSuccess = useAppSelector(state => state.assignmentGrade.updateSuccess);

  const handleClose = () => {
    navigate('/assignment-grade' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getAssignments({}));
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
      ...assignmentGradeEntity,
      ...values,
      assignment: assignments.find(it => it.id.toString() === values.assignment.toString()),
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
          ...assignmentGradeEntity,
          createdDate: convertDateTimeFromServer(assignmentGradeEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(assignmentGradeEntity.lastModifiedDate),
          assignment: assignmentGradeEntity?.assignment?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="webApp.assignmentGrade.home.createOrEditLabel" data-cy="AssignmentGradeCreateUpdateHeading">
            <Translate contentKey="webApp.assignmentGrade.home.createOrEditLabel">Create or edit a AssignmentGrade</Translate>
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
                  id="assignment-grade-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('webApp.assignmentGrade.studentId')}
                id="assignment-grade-studentId"
                name="studentId"
                data-cy="studentId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('webApp.assignmentGrade.grade')}
                id="assignment-grade-grade"
                name="grade"
                data-cy="grade"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('webApp.assignmentGrade.isDeleted')}
                id="assignment-grade-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('webApp.assignmentGrade.createdBy')}
                id="assignment-grade-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('webApp.assignmentGrade.createdDate')}
                id="assignment-grade-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('webApp.assignmentGrade.lastModifiedBy')}
                id="assignment-grade-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('webApp.assignmentGrade.lastModifiedDate')}
                id="assignment-grade-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="assignment-grade-assignment"
                name="assignment"
                data-cy="assignment"
                label={translate('webApp.assignmentGrade.assignment')}
                type="select"
              >
                <option value="" key="0" />
                {assignments
                  ? assignments.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/assignment-grade" replace color="info">
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

export default AssignmentGradeUpdate;
