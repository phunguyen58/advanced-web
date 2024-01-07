import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './grade-composition.reducer';

export const GradeCompositionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const gradeCompositionEntity = useAppSelector(state => state.gradeComposition.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="gradeCompositionDetailsHeading">
          <Translate contentKey="webApp.gradeComposition.detail.title">GradeComposition</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{gradeCompositionEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="webApp.gradeComposition.name">Name</Translate>
            </span>
          </dt>
          <dd>{gradeCompositionEntity.name}</dd>
          <dt>
            <span id="scale">
              <Translate contentKey="webApp.gradeComposition.scale">Scale</Translate>
            </span>
          </dt>
          <dd>{gradeCompositionEntity.scale}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="webApp.gradeComposition.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{gradeCompositionEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="webApp.gradeComposition.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{gradeCompositionEntity.createdBy}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="webApp.gradeComposition.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {gradeCompositionEntity.createdDate ? (
              <TextFormat value={gradeCompositionEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="webApp.gradeComposition.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{gradeCompositionEntity.lastModifiedBy}</dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="webApp.gradeComposition.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {gradeCompositionEntity.lastModifiedDate ? (
              <TextFormat value={gradeCompositionEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="type">
              <Translate contentKey="webApp.gradeComposition.type">Type</Translate>
            </span>
          </dt>
          <dd>{gradeCompositionEntity.type}</dd>
          <dt>
            <span id="isPublic">
              <Translate contentKey="webApp.gradeComposition.isPublic">Is Public</Translate>
            </span>
          </dt>
          <dd>{gradeCompositionEntity.isPublic ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="webApp.gradeComposition.course">Course</Translate>
          </dt>
          <dd>{gradeCompositionEntity.course ? gradeCompositionEntity.course.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/grade-composition" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/grade-composition/${gradeCompositionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default GradeCompositionDetail;
