import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './grade-review.reducer';

export const GradeReviewDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const gradeReviewEntity = useAppSelector(state => state.gradeReview.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="gradeReviewDetailsHeading">
          <Translate contentKey="webApp.gradeReview.detail.title">GradeReview</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{gradeReviewEntity.id}</dd>
          <dt>
            <span id="gradeCompositionId">
              <Translate contentKey="webApp.gradeReview.gradeCompositionId">Grade Composition Id</Translate>
            </span>
          </dt>
          <dd>{gradeReviewEntity.gradeCompositionId}</dd>
          <dt>
            <span id="studentId">
              <Translate contentKey="webApp.gradeReview.studentId">Student Id</Translate>
            </span>
          </dt>
          <dd>{gradeReviewEntity.studentId}</dd>
          <dt>
            <span id="courseId">
              <Translate contentKey="webApp.gradeReview.courseId">Course Id</Translate>
            </span>
          </dt>
          <dd>{gradeReviewEntity.courseId}</dd>
          <dt>
            <span id="reviewerId">
              <Translate contentKey="webApp.gradeReview.reviewerId">Reviewer Id</Translate>
            </span>
          </dt>
          <dd>{gradeReviewEntity.reviewerId}</dd>
          <dt>
            <span id="assigmentId">
              <Translate contentKey="webApp.gradeReview.assigmentId">Assigment Id</Translate>
            </span>
          </dt>
          <dd>{gradeReviewEntity.assigmentId}</dd>
          <dt>
            <span id="assimentGradeId">
              <Translate contentKey="webApp.gradeReview.assimentGradeId">Assiment Grade Id</Translate>
            </span>
          </dt>
          <dd>{gradeReviewEntity.assimentGradeId}</dd>
          <dt>
            <span id="currentGrade">
              <Translate contentKey="webApp.gradeReview.currentGrade">Current Grade</Translate>
            </span>
          </dt>
          <dd>{gradeReviewEntity.currentGrade}</dd>
          <dt>
            <span id="expectationGrade">
              <Translate contentKey="webApp.gradeReview.expectationGrade">Expectation Grade</Translate>
            </span>
          </dt>
          <dd>{gradeReviewEntity.expectationGrade}</dd>
          <dt>
            <span id="studentExplanation">
              <Translate contentKey="webApp.gradeReview.studentExplanation">Student Explanation</Translate>
            </span>
          </dt>
          <dd>{gradeReviewEntity.studentExplanation}</dd>
          <dt>
            <span id="teacherComment">
              <Translate contentKey="webApp.gradeReview.teacherComment">Teacher Comment</Translate>
            </span>
          </dt>
          <dd>{gradeReviewEntity.teacherComment}</dd>
          <dt>
            <span id="isFinal">
              <Translate contentKey="webApp.gradeReview.isFinal">Is Final</Translate>
            </span>
          </dt>
          <dd>{gradeReviewEntity.isFinal ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/grade-review" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/grade-review/${gradeReviewEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default GradeReviewDetail;
