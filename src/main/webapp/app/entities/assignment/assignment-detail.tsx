import React, { useEffect } from 'react';
import { Link, useParams, useNavigate } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './assignment.reducer';
import AssignmentGrade from '../assignment-grade/assignment-grade';
import { Divider } from 'primereact/divider';

export const AssignmentDetail = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();

  const { asignmentId } = useParams<'asignmentId'>();

  useEffect(() => {
    dispatch(getEntity(asignmentId));
  }, []);

  const assignmentEntity = useAppSelector(state => state.assignment.entity);
  return (
    <Row className="px-5">
      <Col md="12">
        <h2 data-cy="assignmentDetailsHeading">
          <Translate contentKey="webApp.assignment.detail.title">Assignment</Translate>
        </h2>
        <dl className="jh-entity-details">
          {/* <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt> */}
          {/* <dd>{assignmentEntity.id}</dd> */}
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

        <Divider />

        <AssignmentGrade></AssignmentGrade>

        <Button onClick={() => navigate(-2)} replace color="light" className="btn-outline-dark" data-cy="entityDetailsBackButton">
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AssignmentDetail;
