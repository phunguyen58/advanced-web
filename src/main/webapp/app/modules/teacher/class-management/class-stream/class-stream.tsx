import './class-stream.scss';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import React, { useEffect } from 'react';
import { TextFormat, Translate } from 'react-jhipster';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';

import { APP_DATE_FORMAT, AUTHORITIES } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from 'app/entities/course/course-router/course.reducer';
const ClassStream = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const account = useAppSelector(state => state.authentication.account);

  const courseEntity = useAppSelector(state => state.course.entity);
  return (
    <Row className="px-5">
      <Col md="8">
        {/* <h2 data-cy="courseDetailsHeading">
          <Translate contentKey="webApp.course.detail.title">Course</Translate>
        </h2> */}
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
            <span id="createdBy">
              <Translate contentKey="webApp.course.ownerId">Created By</Translate>
            </span>
          </dt>
          <dd>{courseEntity.createdBy}</dd>
        </dl>
        {account.authorities.includes(AUTHORITIES.TEACHER) && (
          <Button tag={Link} to={`edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </span>
          </Button>
        )}
      </Col>
    </Row>
  );
};

export default ClassStream;
