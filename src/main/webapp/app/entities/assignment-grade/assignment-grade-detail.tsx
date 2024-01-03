import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './assignment-grade.reducer';

export const AssignmentGradeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const assignmentGradeEntity = useAppSelector(state => state.assignmentGrade.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="assignmentGradeDetailsHeading">
          <Translate contentKey="webApp.assignmentGrade.detail.title">AssignmentGrade</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{assignmentGradeEntity.id}</dd>
          <dt>
            <span id="studentId">
              <Translate contentKey="webApp.assignmentGrade.studentId">Student Id</Translate>
            </span>
          </dt>
          <dd>{assignmentGradeEntity.studentId}</dd>
          <dt>
            <span id="grade">
              <Translate contentKey="webApp.assignmentGrade.grade">Grade</Translate>
            </span>
          </dt>
          <dd>{assignmentGradeEntity.grade}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="webApp.assignmentGrade.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{assignmentGradeEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="webApp.assignmentGrade.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{assignmentGradeEntity.createdBy}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="webApp.assignmentGrade.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {assignmentGradeEntity.createdDate ? (
              <TextFormat value={assignmentGradeEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="webApp.assignmentGrade.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{assignmentGradeEntity.lastModifiedBy}</dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="webApp.assignmentGrade.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {assignmentGradeEntity.lastModifiedDate ? (
              <TextFormat value={assignmentGradeEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="webApp.assignmentGrade.assignment">Assignment</Translate>
          </dt>
          <dd>{assignmentGradeEntity.assignment ? assignmentGradeEntity.assignment.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/assignment-grade" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/assignment-grade/${assignmentGradeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AssignmentGradeDetail;
