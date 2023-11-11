import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './product.reducer';

export const ProductDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const productEntity = useAppSelector(state => state.product.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="productDetailsHeading">
          <Translate contentKey="webApp.product.detail.title">Product</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{productEntity.id}</dd>
          <dt>
            <span id="productName">
              <Translate contentKey="webApp.product.productName">Product Name</Translate>
            </span>
          </dt>
          <dd>{productEntity.productName}</dd>
          <dt>
            <span id="productPrice">
              <Translate contentKey="webApp.product.productPrice">Product Price</Translate>
            </span>
          </dt>
          <dd>{productEntity.productPrice}</dd>
          <dt>
            <span id="productPriceSale">
              <Translate contentKey="webApp.product.productPriceSale">Product Price Sale</Translate>
            </span>
          </dt>
          <dd>{productEntity.productPriceSale}</dd>
          <dt>
            <span id="productDescription">
              <Translate contentKey="webApp.product.productDescription">Product Description</Translate>
            </span>
          </dt>
          <dd>{productEntity.productDescription}</dd>
          <dt>
            <span id="productShortDescription">
              <Translate contentKey="webApp.product.productShortDescription">Product Short Description</Translate>
            </span>
          </dt>
          <dd>{productEntity.productShortDescription}</dd>
          <dt>
            <span id="productQuantity">
              <Translate contentKey="webApp.product.productQuantity">Product Quantity</Translate>
            </span>
          </dt>
          <dd>{productEntity.productQuantity}</dd>
          <dt>
            <span id="productCode">
              <Translate contentKey="webApp.product.productCode">Product Code</Translate>
            </span>
          </dt>
          <dd>{productEntity.productCode}</dd>
          <dt>
            <span id="productPointRating">
              <Translate contentKey="webApp.product.productPointRating">Product Point Rating</Translate>
            </span>
          </dt>
          <dd>{productEntity.productPointRating}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="webApp.product.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{productEntity.createdBy}</dd>
          <dt>
            <span id="createdTime">
              <Translate contentKey="webApp.product.createdTime">Created Time</Translate>
            </span>
          </dt>
          <dd>
            {productEntity.createdTime ? <TextFormat value={productEntity.createdTime} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="webApp.product.categories">Categories</Translate>
          </dt>
          <dd>{productEntity.categories ? productEntity.categories.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/product" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/product/${productEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProductDetail;
