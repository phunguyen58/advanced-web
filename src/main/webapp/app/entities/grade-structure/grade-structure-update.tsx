import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IGradeComposition } from 'app/shared/model/grade-composition.model';
import { getEntities as getGradeCompositions } from 'app/entities/grade-composition/grade-composition.reducer';
import { IGradeStructure } from 'app/shared/model/grade-structure.model';
import { getEntity, updateEntity, createEntity, reset } from './grade-structure.reducer';

export const GradeStructureUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const gradeCompositions = useAppSelector(state => state.gradeComposition.entities);
  const gradeStructureEntity = useAppSelector(state => state.gradeStructure.entity);
  const loading = useAppSelector(state => state.gradeStructure.loading);
  const updating = useAppSelector(state => state.gradeStructure.updating);
  const updateSuccess = useAppSelector(state => state.gradeStructure.updateSuccess);

  const handleClose = () => {
    navigate('/grade-structure' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

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
      ...gradeStructureEntity,
      ...values,
      gradeCompositions: gradeCompositions.find(it => it.id.toString() === values.gradeCompositions.toString()),
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
          ...gradeStructureEntity,
          createdDate: convertDateTimeFromServer(gradeStructureEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(gradeStructureEntity.lastModifiedDate),
          gradeCompositions: gradeStructureEntity?.gradeCompositions?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="webApp.gradeStructure.home.createOrEditLabel" data-cy="GradeStructureCreateUpdateHeading">
            <Translate contentKey="webApp.gradeStructure.home.createOrEditLabel">Create or edit a GradeStructure</Translate>
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
                  id="grade-structure-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('webApp.gradeStructure.courseId')}
                id="grade-structure-courseId"
                name="courseId"
                data-cy="courseId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('webApp.gradeStructure.isDeleted')}
                id="grade-structure-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('webApp.gradeStructure.createdBy')}
                id="grade-structure-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('webApp.gradeStructure.createdDate')}
                id="grade-structure-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('webApp.gradeStructure.lastModifiedBy')}
                id="grade-structure-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('webApp.gradeStructure.lastModifiedDate')}
                id="grade-structure-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="grade-structure-gradeCompositions"
                name="gradeCompositions"
                data-cy="gradeCompositions"
                label={translate('webApp.gradeStructure.gradeCompositions')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/grade-structure" replace color="info">
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

export default GradeStructureUpdate;
