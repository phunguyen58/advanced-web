import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getSortState, JhiPagination, JhiItemCount, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICourse } from 'app/shared/model/course.model';
import { getEntities, updateEntity } from './course-router/course.reducer';
import './course.scss';
import { InputText } from 'primereact/inputtext';
import { Calendar } from 'primereact/calendar';
import { Dropdown } from 'primereact/dropdown';
import axios from 'axios';
import { set } from 'lodash';

export const Course = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'id'), location.search)
  );

  const courseList = useAppSelector(state => state.course.entities);
  const loading = useAppSelector(state => state.course.loading);
  const totalItems = useAppSelector(state => state.course.totalItems);
  const [statuses, setStatuses] = useState(['All', 'Active', 'Inactive']);
  const [criteria, setCriteria] = useState({});
  const [status, setStatus] = useState('All');
  const [timeoutId, setTimeoutId] = useState(null);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
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

  const toggleActive = course => () => {
    dispatch(
      updateEntity({
        ...course,
        isDeleted: course.isDeleted ? false : true,
      })
    );
  };

  const onChange = e => {
    if (timeoutId) {
      clearTimeout(timeoutId);
    }

    const newTimeoutId = setTimeout(() => {
      // Handle the input change after the delay
      let { name, value } = e.target;
      if (name === 'status.equals') {
        name = 'isDeleted.equals';
        value = value === 'All' ? '' : value === 'Active' ? false : true;
        setStatus(e.value);
      }

      let params = {};

      if (name === 'expirationDate' || name === 'createdDate') {
        value = new Date(value.getTime() + Math.abs(new Date().getTimezoneOffset()) * 60 * 1000).toJSON();
        const tomorrow = new Date(value);
        tomorrow.setDate(tomorrow.getDate() + 1);
        const yesterday = new Date(value);
        yesterday.setDate(yesterday.getDate() - 1);
        setCriteria({
          ...criteria,
          [`${name}.greaterThanOrEqual`]: new Date(yesterday.getTime() + Math.abs(new Date().getTimezoneOffset()) * 60 * 1000).toJSON(),
          [`${name}.lessThanOrEqual`]: new Date(tomorrow.getTime() + Math.abs(new Date().getTimezoneOffset()) * 60 * 1000).toJSON(),
        });
        params = {
          ...criteria,
          [`${name}.greaterThanOrEqual`]: new Date(yesterday.getTime() + Math.abs(new Date().getTimezoneOffset()) * 60 * 1000).toJSON(),
          [`${name}.lessThanOrEqual`]: new Date(tomorrow.getTime() + Math.abs(new Date().getTimezoneOffset()) * 60 * 1000).toJSON(),
        };
      } else {
        setCriteria({ ...criteria, [name]: value });
        params = {
          ...criteria,
          [name]: value,
        };
      }
      dispatch(
        getEntities({
          page: paginationState.activePage - 1,
          size: paginationState.itemsPerPage,
          sort: `${paginationState.sort},${paginationState.order}`,
          query: Object.entries(params)
            .map(([key, val]) => `${encodeURIComponent(key)}=${encodeURIComponent(String(val))}`)
            .join('&'),
        })
      );
    }, 500);

    // Save the new timeout id
    setTimeoutId(newTimeoutId);
  };

  return (
    <div className="course-management-container">
      <div className="m-3">
        <h2 id="course-heading" data-cy="CourseHeading">
          <Translate contentKey="webApp.course.home.classManagement">Courses</Translate>
          {/* <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="webApp.course.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/course/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="webApp.course.home.createLabel">Create new Course</Translate>
          </Link>
        </div> */}
        </h2>
        <div className="table-responsive">
          <Table responsive striped>
            <thead>
              <tr>
                <th className="hand">
                  <div>
                    <span onClick={sort('id')}>{translate('webApp.course.id')}</span> <FontAwesomeIcon icon="sort" />
                    <InputText className="w-100" onChange={e => onChange(e)} name="id.equals" />
                  </div>
                </th>
                <th className="hand">
                  <div>
                    <span onClick={sort('code')}>{translate('webApp.course.code')}</span> <FontAwesomeIcon icon="sort" />
                    <InputText className="w-100" onChange={e => onChange(e)} name="code.contains" />
                  </div>
                </th>
                <th className="hand">
                  <div>
                    <span onClick={sort('name')}>{translate('webApp.course.name')}</span> <FontAwesomeIcon icon="sort" />
                    <InputText className="w-100" onChange={e => onChange(e)} name="name.contains" />
                  </div>
                </th>
                <th className="hand">
                  <div>
                    <span onClick={sort('ownerId')}>{translate('webApp.course.ownerId')}</span> <FontAwesomeIcon icon="sort" />
                    <InputText className="w-100" onChange={e => onChange(e)} name="ownerId.equals" />
                  </div>
                </th>
                {/* <th className="hand" onClick={sort('description')}>
                    <Translate contentKey="webApp.course.description">Description</Translate> <FontAwesomeIcon icon="sort" />
                  </th> */}
                <th className="hand">
                  <div>
                    <span onClick={sort('invitationCode')}>{translate('webApp.course.invitationCode')}</span>{' '}
                    <FontAwesomeIcon icon="sort" />
                    <InputText className="w-100" onChange={e => onChange(e)} name="invitationCode.contains" />
                  </div>
                </th>
                <th className="hand">
                  <div>
                    <span onClick={sort('expirationDate')}>{translate('webApp.course.expirationDate')}</span>{' '}
                    <FontAwesomeIcon icon="sort" />
                    <Calendar className="w-100" onChange={e => onChange(e)} name="expirationDate" dateFormat="dd/mm/yy" />
                  </div>
                </th>

                {/* <th className="hand" onClick={sort('createdBy')}>
                    <Translate contentKey="webApp.course.createdBy">Created By</Translate> <FontAwesomeIcon icon="sort" />
                  </th> */}
                <th className="hand">
                  <div>
                    <span onClick={sort('createdDate')}>{translate('webApp.course.createdDate')}</span> <FontAwesomeIcon icon="sort" />
                    <Calendar className="w-100" onChange={e => onChange(e)} name="createdDate" dateFormat="dd/mm/yy" />
                  </div>
                </th>
                {/* <th className="hand" onClick={sort('lastModifiedBy')}>
                    <Translate contentKey="webApp.course.lastModifiedBy">Last Modified By</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('lastModifiedDate')}>
                    <Translate contentKey="webApp.course.lastModifiedDate">Last Modified Date</Translate> <FontAwesomeIcon icon="sort" />
                  </th> */}
                <th className="hand">
                  <div>
                    <span onClick={sort('isDeleted')}>{translate('webApp.course.status')}</span> <FontAwesomeIcon icon="sort" />
                    <Dropdown options={statuses} className="w-100" onChange={e => onChange(e)} name="status.equals" value={status} />
                  </div>
                </th>
              </tr>
            </thead>
            <tbody>
              {courseList.map((course, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>{course.id}</td>
                  <td>{course.code}</td>
                  <td>{course.name}</td>
                  <td>{course.ownerId}</td>
                  {/* <td>{course.description}</td> */}
                  <td>{course.invitationCode}</td>
                  <td>
                    {course.expirationDate ? <TextFormat type="date" value={course.expirationDate} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  {/* <td>{course.createdBy}</td> */}
                  <td>{course.createdDate ? <TextFormat type="date" value={course.createdDate} format={APP_DATE_FORMAT} /> : null}</td>

                  {/* <td>{course.lastModifiedBy}</td>
                    <td>
                      {course.lastModifiedDate ? <TextFormat type="date" value={course.lastModifiedDate} format={APP_DATE_FORMAT} /> : null}
                    </td> */}
                  {/* <td className="text-end">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`/course/${course.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button
                          tag={Link}
                          to={`/course/${course.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                          color="primary"
                          size="sm"
                          data-cy="entityEditButton"
                        >
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button
                          tag={Link}
                          to={`/course/${course.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                          color="danger"
                          size="sm"
                          data-cy="entityDeleteButton"
                        >
                          <FontAwesomeIcon icon="trash" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.delete">Delete</Translate>
                          </span>
                        </Button>
                      </div>
                    </td> */}
                  <td>
                    <td>
                      {course.isDeleted ? (
                        <Button className="btn-active" color="danger" onClick={toggleActive(course)}>
                          <span>{translate('webApp.course.inactive')}</span>
                        </Button>
                      ) : (
                        <Button className="btn-active" color="success" onClick={toggleActive(course)}>
                          <span>{translate('webApp.course.active')}</span>
                        </Button>
                      )}
                    </td>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        </div>
        {totalItems ? (
          <div className={courseList && courseList.length > 0 ? '' : 'd-none'}>
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
    </div>
  );
};

export default Course;
