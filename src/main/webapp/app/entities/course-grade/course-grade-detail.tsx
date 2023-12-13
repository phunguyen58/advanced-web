import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './course-grade.reducer';

export const CourseGradeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const courseGradeEntity = useAppSelector(state => state.courseGrade.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="courseGradeDetailsHeading">
          <Translate contentKey="webApp.courseGrade.detail.title">CourseGrade</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{courseGradeEntity.id}</dd>
          <dt>
            <span id="gradeCompositionId">
              <Translate contentKey="webApp.courseGrade.gradeCompositionId">Grade Composition Id</Translate>
            </span>
          </dt>
          <dd>{courseGradeEntity.gradeCompositionId}</dd>
          <dt>
            <span id="studentId">
              <Translate contentKey="webApp.courseGrade.studentId">Student Id</Translate>
            </span>
          </dt>
          <dd>{courseGradeEntity.studentId}</dd>
          <dt>
            <span id="isMarked">
              <Translate contentKey="webApp.courseGrade.isMarked">Is Marked</Translate>
            </span>
          </dt>
          <dd>{courseGradeEntity.isMarked ? 'true' : 'false'}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="webApp.courseGrade.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{courseGradeEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="webApp.courseGrade.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{courseGradeEntity.createdBy}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="webApp.courseGrade.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {courseGradeEntity.createdDate ? (
              <TextFormat value={courseGradeEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="webApp.courseGrade.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{courseGradeEntity.lastModifiedBy}</dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="webApp.courseGrade.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {courseGradeEntity.lastModifiedDate ? (
              <TextFormat value={courseGradeEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/course-grade" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/course-grade/${courseGradeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CourseGradeDetail;
