import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './assignment.reducer';

export const AssignmentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const assignmentEntity = useAppSelector(state => state.assignment.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="assignmentDetailsHeading">
          <Translate contentKey="webApp.assignment.detail.title">Assignment</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{assignmentEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="webApp.assignment.name">Name</Translate>
            </span>
          </dt>
          <dd>{assignmentEntity.name}</dd>
          <dt>
            <span id="weight">
              <Translate contentKey="webApp.assignment.weight">Weight</Translate>
            </span>
          </dt>
          <dd>{assignmentEntity.weight}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="webApp.assignment.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{assignmentEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="webApp.assignment.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{assignmentEntity.createdBy}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="webApp.assignment.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {assignmentEntity.createdDate ? <TextFormat value={assignmentEntity.createdDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="webApp.assignment.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{assignmentEntity.lastModifiedBy}</dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="webApp.assignment.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {assignmentEntity.lastModifiedDate ? (
              <TextFormat value={assignmentEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="webApp.assignment.assignmentGrades">Assignment Grades</Translate>
          </dt>
          <dd>{assignmentEntity.assignmentGrades ? assignmentEntity.assignmentGrades.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/assignment" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/assignment/${assignmentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AssignmentDetail;
