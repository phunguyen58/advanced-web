import React, { useEffect } from 'react';
import { Link, useParams, useNavigate } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './assignment.reducer';
import AssignmentGrade from '../assignment-grade/assignment-grade';

export const AssignmentDetail = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();

  const { asignmentId } = useParams<'asignmentId'>();

  useEffect(() => {
    dispatch(getEntity(asignmentId));
  }, []);

  const assignmentEntity = useAppSelector(state => state.assignment.entity);
  return (
    <Row className="px-4">
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
            <span id="description">
              <Translate contentKey="webApp.assignment.description">Description</Translate>
            </span>
          </dt>
          <dd>{assignmentEntity.description}</dd>
        </dl>
        <Button onClick={() => navigate(-2)} replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
      </Col>
      <AssignmentGrade></AssignmentGrade>
    </Row>
  );
};

export default AssignmentDetail;
