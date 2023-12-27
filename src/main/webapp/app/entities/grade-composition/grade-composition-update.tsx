import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IGradeStructure } from 'app/shared/model/grade-structure.model';
import { getEntities as getGradeStructures } from 'app/entities/grade-structure/grade-structure.reducer';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IGradeComposition } from 'app/shared/model/grade-composition.model';
import { getEntity, updateEntity, createEntity, reset } from './grade-composition.reducer';

export const GradeCompositionUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const gradeCompositionEntity = useAppSelector(state => state.gradeComposition.entity);
  const loading = useAppSelector(state => state.gradeComposition.loading);
  const updating = useAppSelector(state => state.gradeComposition.updating);
  const updateSuccess = useAppSelector(state => state.gradeComposition.updateSuccess);

  const handleClose = () => {
    navigate('/grade-composition' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getGradeStructures({}));
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
      ...gradeCompositionEntity,
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
          type: 'PERCENTAGE',
          ...gradeCompositionEntity,
          createdDate: convertDateTimeFromServer(gradeCompositionEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(gradeCompositionEntity.lastModifiedDate),
          gradeStructureId: gradeCompositionEntity?.gradeStructure?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="webApp.gradeComposition.home.createOrEditLabel" data-cy="GradeCompositionCreateUpdateHeading">
            <Translate contentKey="webApp.gradeComposition.home.createOrEditLabel">Create or edit a GradeComposition</Translate>
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
                  id="grade-composition-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('webApp.gradeComposition.name')}
                id="grade-composition-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('webApp.gradeComposition.scale')}
                id="grade-composition-scale"
                name="scale"
                data-cy="scale"
                type="text"
              />
              <ValidatedField
                label={translate('webApp.gradeComposition.isDeleted')}
                id="grade-composition-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('webApp.gradeComposition.createdBy')}
                id="grade-composition-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('webApp.gradeComposition.createdDate')}
                id="grade-composition-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('webApp.gradeComposition.lastModifiedBy')}
                id="grade-composition-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('webApp.gradeComposition.lastModifiedDate')}
                id="grade-composition-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('webApp.gradeComposition.type')}
                id="grade-composition-type"
                name="type"
                data-cy="type"
                type="select"
              >
                <option value="PERCENTAGE">{translate('webApp.GradeType.PERCENTAGE')}</option>
                <option value="POINT">{translate('webApp.GradeType.POINT')}</option>
                <option value="NONE">{translate('webApp.GradeType.NONE')}</option>
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/grade-composition" replace color="info">
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

export default GradeCompositionUpdate;
