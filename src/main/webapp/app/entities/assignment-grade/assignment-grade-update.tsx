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

export const AssignmentGradeUpdate = ({ assignmentGradeId }: { assignmentGradeId: number }) => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const id = String(assignmentGradeId);

  const isNew = id === undefined;

  const assignments = useAppSelector(state => state.assignment.entities);
  const assignmentGradeEntity = useAppSelector(state => state.assignmentGrade.entity);
  const loading = useAppSelector(state => state.assignmentGrade.loading);
  const updating = useAppSelector(state => state.assignmentGrade.updating);
  const updateSuccess = useAppSelector(state => state.assignmentGrade.updateSuccess);

  const handleClose = () => {
    // navigate(-1);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    // dispatch(getAssignments({}));
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
                readOnly
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
