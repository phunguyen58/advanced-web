import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IGradeReview } from 'app/shared/model/grade-review.model';
import { getEntity, updateEntity, createEntity, reset } from './grade-review.reducer';

export const GradeReviewUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const gradeReviewEntity = useAppSelector(state => state.gradeReview.entity);
  const loading = useAppSelector(state => state.gradeReview.loading);
  const updating = useAppSelector(state => state.gradeReview.updating);
  const updateSuccess = useAppSelector(state => state.gradeReview.updateSuccess);

  const handleClose = () => {
    navigate('/grade-review' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
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
    const entity = {
      ...gradeReviewEntity,
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
      ? {}
      : {
          ...gradeReviewEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="webApp.gradeReview.home.createOrEditLabel" data-cy="GradeReviewCreateUpdateHeading">
            <Translate contentKey="webApp.gradeReview.home.createOrEditLabel">Create or edit a GradeReview</Translate>
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
                  id="grade-review-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('webApp.gradeReview.gradeCompositionId')}
                id="grade-review-gradeCompositionId"
                name="gradeCompositionId"
                data-cy="gradeCompositionId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('webApp.gradeReview.studentId')}
                id="grade-review-studentId"
                name="studentId"
                data-cy="studentId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('webApp.gradeReview.courseId')}
                id="grade-review-courseId"
                name="courseId"
                data-cy="courseId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('webApp.gradeReview.reviewerId')}
                id="grade-review-reviewerId"
                name="reviewerId"
                data-cy="reviewerId"
                type="text"
              />
              <ValidatedField
                label={translate('webApp.gradeReview.assigmentId')}
                id="grade-review-assigmentId"
                name="assigmentId"
                data-cy="assigmentId"
                type="text"
              />
              <ValidatedField
                label={translate('webApp.gradeReview.assimentGradeId')}
                id="grade-review-assimentGradeId"
                name="assimentGradeId"
                data-cy="assimentGradeId"
                type="text"
              />
              <ValidatedField
                label={translate('webApp.gradeReview.currentGrade')}
                id="grade-review-currentGrade"
                name="currentGrade"
                data-cy="currentGrade"
                type="text"
              />
              <ValidatedField
                label={translate('webApp.gradeReview.expectationGrade')}
                id="grade-review-expectationGrade"
                name="expectationGrade"
                data-cy="expectationGrade"
                type="text"
              />
              <ValidatedField
                label={translate('webApp.gradeReview.studentExplanation')}
                id="grade-review-studentExplanation"
                name="studentExplanation"
                data-cy="studentExplanation"
                type="text"
              />
              <ValidatedField
                label={translate('webApp.gradeReview.teacherComment')}
                id="grade-review-teacherComment"
                name="teacherComment"
                data-cy="teacherComment"
                type="text"
              />
              <ValidatedField
                label={translate('webApp.gradeReview.isFinal')}
                id="grade-review-isFinal"
                name="isFinal"
                data-cy="isFinal"
                check
                type="checkbox"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/grade-review" replace color="info">
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

export default GradeReviewUpdate;
