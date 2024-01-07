import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import React, { useEffect, useState } from 'react';
import { JhiItemCount, JhiPagination, Translate, getSortState } from 'react-jhipster';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';

import { getEntities } from './grade-review.reducer';

export const GradeReview = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'id'), location.search)
  );

  const gradeReviewList = useAppSelector(state => state.gradeReview.entities);
  const loading = useAppSelector(state => state.gradeReview.loading);
  const totalItems = useAppSelector(state => state.gradeReview.totalItems);

  const getAllEntities = () => {
    const courseId = location.pathname.split('/')[2];
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
        query: `courseId.equals=${courseId}`,
      })
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (location.search !== endURL) {
      navigate(`${location.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [location.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  return (
    <div className="m-3">
      <h2 id="grade-review-heading" data-cy="GradeReviewHeading">
        {/* <Translate contentKey="webApp.gradeReview.home.title">Grade Reviews</Translate> */}
        <div className="d-flex justify-content-end">
          {/* <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="webApp.gradeReview.home.refreshListLabel">Refresh List</Translate>
          </Button> */}
          <Button className="me-2 btn-action" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />
          </Button>
          {/* <Link to="/grade-review/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="webApp.gradeReview.home.createLabel">Create new Grade Review</Translate>
          </Link> */}
        </div>
      </h2>
      <div className="table-responsive">
        <Table responsive striped>
          <thead>
            <tr>
              {/* <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="webApp.gradeReview.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                </th> */}
              {/* <th className="hand" onClick={sort('gradeCompositionId')}>
                  <Translate contentKey="webApp.gradeReview.gradeCompositionId">Grade Composition Id</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th> */}
              <th className="hand" onClick={sort('studentId')}>
                <Translate contentKey="webApp.gradeReview.studentId">Student Id</Translate> <FontAwesomeIcon icon="sort" />
              </th>
              {/* <th className="hand" onClick={sort('courseId')}>
                  <Translate contentKey="webApp.gradeReview.courseId">Course Id</Translate> <FontAwesomeIcon icon="sort" />
                </th> */}
              {/* <th className="hand" onClick={sort('reviewerId')}>
                  <Translate contentKey="webApp.gradeReview.reviewerId">Reviewer Id</Translate> <FontAwesomeIcon icon="sort" />
                </th> */}
              <th className="hand" onClick={sort('assigmentId')}>
                <Translate contentKey="webApp.gradeReview.assigmentId">Assigment Id</Translate> <FontAwesomeIcon icon="sort" />
              </th>
              {/* <th className="hand" onClick={sort('assimentGradeId')}>
                  <Translate contentKey="webApp.gradeReview.assimentGradeId">Assiment Grade Id</Translate> <FontAwesomeIcon icon="sort" />
                </th> */}
              <th className="hand" onClick={sort('currentGrade')}>
                <Translate contentKey="webApp.gradeReview.currentGrade">Current Grade</Translate> <FontAwesomeIcon icon="sort" />
              </th>
              <th className="hand" onClick={sort('expectationGrade')}>
                <Translate contentKey="webApp.gradeReview.expectationGrade">Expectation Grade</Translate> <FontAwesomeIcon icon="sort" />
              </th>
              {/* <th className="hand" onClick={sort('studentExplanation')}>
                  <Translate contentKey="webApp.gradeReview.studentExplanation">Student Explanation</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th> */}
              {/* <th className="hand" onClick={sort('teacherComment')}>
                  <Translate contentKey="webApp.gradeReview.teacherComment">Teacher Comment</Translate> <FontAwesomeIcon icon="sort" />
                </th> */}
              {/* <th className="hand" onClick={sort('isFinal')}>
                  <Translate contentKey="webApp.gradeReview.isFinal">Is Final</Translate> <FontAwesomeIcon icon="sort" />
                </th> */}
              <th />
            </tr>
          </thead>
          <tbody>
            {gradeReviewList.map((gradeReview, i) => (
              <tr key={`entity-${i}`} data-cy="entityTable">
                {/* <td>
                    <Button tag={Link} to={`/grade-review/${gradeReview.id}`} color="link" size="sm">
                      {gradeReview.id}
                    </Button>
                  </td> */}
                {/* <td>{gradeReview.gradeCompositionId}</td> */}
                <td>{gradeReview.studentId}</td>
                {/* <td>{gradeReview.courseId}</td> */}
                {/* <td>{gradeReview.reviewerId}</td> */}
                <td>{gradeReview.assigmentId}</td>
                {/* <td>{gradeReview.assimentGradeId}</td> */}
                <td>{gradeReview.currentGrade}</td>
                <td>{gradeReview.expectationGrade}</td>
                {/* <td>{gradeReview.studentExplanation}</td> */}
                {/* <td>{gradeReview.teacherComment}</td> */}
                {/* <td>{gradeReview.isFinal ? 'true' : 'false'}</td> */}
                <td className="text-end">
                  <div className="btn-group flex-btn-group-container">
                    {/* <Button tag={Link} to={`/grade-review/${gradeReview.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button> */}
                    <Button
                      tag={Link}
                      to={`/grade-review/${gradeReview.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                      size="sm"
                      data-cy="entityEditButton"
                      className="btn-action"
                    >
                      <FontAwesomeIcon icon="pencil-alt" />{' '}
                      {/* <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span> */}
                    </Button>
                    {/* <Button
                        tag={Link}
                        to={`/grade-review/${gradeReview.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button> */}
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </Table>
      </div>
      {totalItems ? (
        <div className={gradeReviewList && gradeReviewList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default GradeReview;
