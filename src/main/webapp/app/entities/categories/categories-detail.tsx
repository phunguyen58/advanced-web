import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './categories.reducer';

export const CategoriesDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const categoriesEntity = useAppSelector(state => state.categories.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="categoriesDetailsHeading">
          <Translate contentKey="webApp.categories.detail.title">Categories</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{categoriesEntity.id}</dd>
          <dt>
            <span id="categoryName">
              <Translate contentKey="webApp.categories.categoryName">Category Name</Translate>
            </span>
          </dt>
          <dd>{categoriesEntity.categoryName}</dd>
          <dt>
            <span id="categoryDescription">
              <Translate contentKey="webApp.categories.categoryDescription">Category Description</Translate>
            </span>
          </dt>
          <dd>{categoriesEntity.categoryDescription}</dd>
          <dt>
            <span id="categoryUrl">
              <Translate contentKey="webApp.categories.categoryUrl">Category Url</Translate>
            </span>
          </dt>
          <dd>{categoriesEntity.categoryUrl}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="webApp.categories.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{categoriesEntity.createdBy}</dd>
          <dt>
            <span id="createdTime">
              <Translate contentKey="webApp.categories.createdTime">Created Time</Translate>
            </span>
          </dt>
          <dd>
            {categoriesEntity.createdTime ? <TextFormat value={categoriesEntity.createdTime} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
        </dl>
        <Button tag={Link} to="/categories" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/categories/${categoriesEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CategoriesDetail;
