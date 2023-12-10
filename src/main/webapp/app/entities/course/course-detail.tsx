import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './course.reducer';

export const CourseDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const courseEntity = useAppSelector(state => state.course.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="courseDetailsHeading">
          <Translate contentKey="webApp.course.detail.title">Course</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{courseEntity.id}</dd>
          <dt>
            <span id="code">
              <Translate contentKey="webApp.course.code">Code</Translate>
            </span>
          </dt>
          <dd>{courseEntity.code}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="webApp.course.name">Name</Translate>
            </span>
          </dt>
          <dd>{courseEntity.name}</dd>
          <dt>
            <span id="invitationCode">
              <Translate contentKey="webApp.course.invitationCode">Invitation Code</Translate>
            </span>
          </dt>
          <dd>{courseEntity.invitationCode}</dd>
          <dt>
            <span id="expirationDate">
              <Translate contentKey="webApp.course.expirationDate">Expiration Date</Translate>
            </span>
          </dt>
          <dd>
            {courseEntity.expirationDate ? <TextFormat value={courseEntity.expirationDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="gradeStructureId">
              <Translate contentKey="webApp.course.gradeStructureId">Grade Structure Id</Translate>
            </span>
          </dt>
          <dd>{courseEntity.gradeStructureId}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="webApp.course.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{courseEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="webApp.course.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{courseEntity.createdBy}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="webApp.course.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>{courseEntity.createdDate ? <TextFormat value={courseEntity.createdDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="webApp.course.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{courseEntity.lastModifiedBy}</dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="webApp.course.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {courseEntity.lastModifiedDate ? (
              <TextFormat value={courseEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="webApp.course.assignments">Assignments</Translate>
          </dt>
          <dd>{courseEntity.assignments ? courseEntity.assignments.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/course" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/course/${courseEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CourseDetail;
