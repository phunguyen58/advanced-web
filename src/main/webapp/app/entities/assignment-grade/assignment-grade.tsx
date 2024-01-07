import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import React, { useEffect, useRef, useState } from 'react';
import { JhiItemCount, JhiPagination, TextFormat, Translate, getSortState, translate } from 'react-jhipster';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';

import { APP_DATE_FORMAT, AUTHORITIES } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';

import axios from 'axios';
import { Dialog } from 'primereact/dialog';
import { toast } from 'react-toastify';
import AssignmentGradeUpdate from './assignment-grade-update';
import { createAssignmentGradeList, getEntitiesByAssignmentId } from './assignment-grade.reducer';

export const AssignmentGrade = () => {
  const dispatch = useAppDispatch();
  const excelFileInputRef = useRef(null);

  const location = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'id'), location.search)
  );

  const account = useAppSelector(state => state.authentication.account);
  const assignmentGradeList = useAppSelector(state => state.assignmentGrade.entities);
  const loading = useAppSelector(state => state.assignmentGrade.loading);
  const totalItems = useAppSelector(state => state.assignmentGrade.totalItems);

  const pathParts = location.pathname.split('/'); // Split the path by '/'
  const courseIdIndex = pathParts.indexOf('course') + 1; // Get the index of 'course' and add 1
  const assignmentIdIndex = pathParts.indexOf('assignment') + 1; // Get the index of 'assignment' and add 1

  const courseId = Number(pathParts[courseIdIndex]);
  const assignmentId = Number(pathParts[assignmentIdIndex]);

  const getAllEntities = () => {
    // Construct the base query
    let baseQuery = `assignmentId.equals=${assignmentId}`;

    // Add conditions based on user role
    if (account.authorities.includes(AUTHORITIES.STUDENT)) {
      baseQuery += `&studentId.equals=${account.studentId}`;
    }

    // Dispatch action with the constructed query
    dispatch(
      getEntitiesByAssignmentId({
        query: baseQuery,
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      })
    );
  };

  const createAssignmentGradeListHandle = () => {
    if (assignmentGradeList.length === 0) {
      dispatch(createAssignmentGradeList({ courseId, assignmentId }));
    }
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
  const [assignmentGradeId, setAssignmentGradeId] = useState(0); // Using useState hook to maintain value
  const [isDisplayAssignmentGradeUpdate, setIsDisplayAssignmentGradeUpdate] = useState(false);
  const handleToShowAssignmentGradeUpdate = (assignmentGradeIdParam: number) => {
    setIsDisplayAssignmentGradeUpdate(true);
    setAssignmentGradeId(assignmentGradeIdParam);
  };

  const handleToShowGradeReview = assignmentGrade => {
    if (assignmentGrade.gradeReviewId) {
      window.location.href = `course/${courseId}/detail/grade-review/${assignmentGrade.gradeReviewId}/edit`;
    } else {
      window.location.href = `course/${courseId}/detail/grade-review/new?assignmentGradeId=${assignmentGrade.id}`;
    }
  };

  const onFileChange = event => {
    const file = event.target.files[0];

    if (file) {
      const formData = new FormData();
      formData.append('file', file);

      // Replace 'YOUR_BACKEND_API_ENDPOINT' with the actual API endpoint to handle file uploads
      axios
        .post(`/api/upload/assignment-grades/${assignmentId}`, formData, {
          headers: {
            'Content-Type': 'multipart/form-data',
          },
        })
        .then(response => {
          // Handle successful response from the backend
          getAllEntities();
          excelFileInputRef.current.value = '';
          toast.success(translate('webApp.assignment.assignmentGradeUpdated'), {
            position: toast.POSITION.TOP_LEFT,
          });
        })
        .catch(error => {
          console.error('Error uploading file:', error);
          // Handle error
        });
    }
  };

  return (
    <div>
      {isDisplayAssignmentGradeUpdate && isDisplayAssignmentGradeUpdate === true && (
        <Dialog
          className="join-a-course-dialog"
          header={translate('webApp.assignmentGrade.home.title')}
          draggable={false}
          resizable={false}
          visible={isDisplayAssignmentGradeUpdate}
          style={{ width: '80vw' }}
          onHide={() => setIsDisplayAssignmentGradeUpdate(false)}
        >
          <AssignmentGradeUpdate assignmentGradeId={assignmentGradeId}></AssignmentGradeUpdate>
        </Dialog>
      )}
      <h2 id="assignment-grade-heading" data-cy="AssignmentGradeHeading">
        <Translate contentKey="webApp.assignmentGrade.home.title">Assignment Grades</Translate>
        <div className="d-flex justify-content-end">
          {account.authorities.includes(AUTHORITIES.TEACHER) && (
            <>
              <label htmlFor="fileInput" className="custom-file-upload">
                {translate('userManagement.importExcel')}
              </label>
              <input id="fileInput" type="file" onChange={onFileChange} style={{ display: 'none' }} ref={excelFileInputRef} />
            </>
          )}
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="webApp.assignmentGrade.home.refreshListLabel">Refresh List</Translate>
          </Button>
          {assignmentGradeList.length === 0 && account.authorities.includes(AUTHORITIES.TEACHER) && (
            <Button
              onClick={createAssignmentGradeListHandle}
              className="btn btn-primary jh-create-entity"
              id="jh-create-entity"
              data-cy="entityCreateButton"
            >
              <FontAwesomeIcon icon="plus" />
              &nbsp;
              <Translate contentKey="webApp.assignmentGrade.home.createList">Create new Assignment Grade List</Translate>
            </Button>
          )}
        </div>
      </h2>
      <div className="table-responsive">
        {assignmentGradeList && assignmentGradeList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="webApp.assignmentGrade.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('studentId')}>
                  <Translate contentKey="webApp.assignmentGrade.studentId">Student Id</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('grade')}>
                  <Translate contentKey="webApp.assignmentGrade.grade">Grade</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('lastModifiedBy')}>
                  <Translate contentKey="webApp.assignmentGrade.lastModifiedBy">Last Modified By</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('lastModifiedDate')}>
                  <Translate contentKey="webApp.assignmentGrade.lastModifiedDate">Last Modified Date</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {assignmentGradeList.map((assignmentGrade, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`assignment-grade/${assignmentGrade.id}`} color="link" size="sm">
                      {assignmentGrade.id}
                    </Button>
                  </td>
                  <td>{assignmentGrade.studentId}</td>
                  <td>{assignmentGrade.grade}</td>
                  <td>{assignmentGrade.lastModifiedBy}</td>
                  <td>
                    {assignmentGrade.lastModifiedDate ? (
                      <TextFormat type="date" value={assignmentGrade.lastModifiedDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      {/* <Button tag={Link} to={`assignment-grade/${assignmentGrade.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button> */}
                      {account && (
                        <Button
                          onClick={() => handleToShowGradeReview(assignmentGrade)}
                          color="primary"
                          size="sm"
                          data-cy="entityEditButton"
                        >
                          <span className="d-none d-md-inline">
                            <Translate contentKey="webApp.gradeReview.detail.title">Grade Review</Translate>
                          </span>
                        </Button>
                      )}
                      {account && account.authorities.includes('ROLE_TEACHER') && (
                        <Button
                          onClick={() => handleToShowAssignmentGradeUpdate(assignmentGrade.id)}
                          color="primary"
                          size="sm"
                          data-cy="entityEditButton"
                        >
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                      )}

                      {/* <Button
                        tag={Link}
                        to={`/assignment-grade/${assignmentGrade.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="webApp.assignmentGrade.home.notFound">No Assignment Grades found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={assignmentGradeList && assignmentGradeList.length > 0 ? '' : 'd-none'}>
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

export default AssignmentGrade;
