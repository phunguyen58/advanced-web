import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './notification.reducer';

export const NotificationDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const notificationEntity = useAppSelector(state => state.notification.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="notificationDetailsHeading">
          <Translate contentKey="webApp.notification.detail.title">Notification</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{notificationEntity.id}</dd>
          <dt>
            <span id="message">
              <Translate contentKey="webApp.notification.message">Message</Translate>
            </span>
          </dt>
          <dd>{notificationEntity.message}</dd>
          <dt>
            <span id="topic">
              <Translate contentKey="webApp.notification.topic">Topic</Translate>
            </span>
          </dt>
          <dd>{notificationEntity.topic}</dd>
          <dt>
            <span id="receivers">
              <Translate contentKey="webApp.notification.receivers">Receivers</Translate>
            </span>
          </dt>
          <dd>{notificationEntity.receivers}</dd>
          <dt>
            <span id="sender">
              <Translate contentKey="webApp.notification.sender">Sender</Translate>
            </span>
          </dt>
          <dd>{notificationEntity.sender}</dd>
          <dt>
            <span id="role">
              <Translate contentKey="webApp.notification.role">Role</Translate>
            </span>
          </dt>
          <dd>{notificationEntity.role}</dd>
          <dt>
            <span id="link">
              <Translate contentKey="webApp.notification.link">Link</Translate>
            </span>
          </dt>
          <dd>{notificationEntity.link}</dd>
          <dt>
            <span id="gradeReviewId">
              <Translate contentKey="webApp.notification.gradeReviewId">Grade Review Id</Translate>
            </span>
          </dt>
          <dd>{notificationEntity.gradeReviewId}</dd>
        </dl>
        <Button tag={Link} to="/notification" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/notification/${notificationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default NotificationDetail;
