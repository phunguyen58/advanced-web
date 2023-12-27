import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './grade-structure.reducer';

export const GradeStructureDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const gradeStructureEntity = useAppSelector(state => state.gradeStructure.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="gradeStructureDetailsHeading">
          <Translate contentKey="webApp.gradeStructure.detail.title">GradeStructure</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{gradeStructureEntity.id}</dd>
          <dt>
            <span id="courseId">
              <Translate contentKey="webApp.gradeStructure.courseId">Course Id</Translate>
            </span>
          </dt>
          <dd>{gradeStructureEntity.courseId}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="webApp.gradeStructure.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{gradeStructureEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="webApp.gradeStructure.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{gradeStructureEntity.createdBy}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="webApp.gradeStructure.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {gradeStructureEntity.createdDate ? (
              <TextFormat value={gradeStructureEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="webApp.gradeStructure.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{gradeStructureEntity.lastModifiedBy}</dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="webApp.gradeStructure.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {gradeStructureEntity.lastModifiedDate ? (
              <TextFormat value={gradeStructureEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="type">
              <Translate contentKey="webApp.gradeStructure.type">Type</Translate>
            </span>
          </dt>
          <dd>{gradeStructureEntity.type}</dd>
        </dl>
        <Button tag={Link} to="/grade-structure" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/grade-structure/${gradeStructureEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default GradeStructureDetail;
