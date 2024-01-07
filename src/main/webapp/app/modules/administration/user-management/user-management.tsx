import React, { useState, useEffect, useRef } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table, Badge } from 'reactstrap';
import { Translate, TextFormat, JhiPagination, JhiItemCount, getSortState, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { getUsersAsAdmin, updateUser } from './user-management.reducer';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import './index.scss';
import axios from 'axios';
import { toast } from 'react-toastify';
import { listener, sendNotificationStudent } from 'app/config/websocket-middleware';

export const UserManagement = () => {
  const excelFileInputRef = useRef(null);

  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const [pagination, setPagination] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'id'), location.search)
  );

  const getUsersFromProps = () => {
    dispatch(
      getUsersAsAdmin({
        page: pagination.activePage - 1,
        size: pagination.itemsPerPage,
        sort: `${pagination.sort},${pagination.order}`,
      })
    );
    const endURL = `?page=${pagination.activePage}&sort=${pagination.sort},${pagination.order}`;
    if (location.search !== endURL) {
      navigate(`${location.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    getUsersFromProps();
  }, [pagination.activePage, pagination.order, pagination.sort]);

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const page = params.get('page');
    const sortParam = params.get(SORT);
    if (page && sortParam) {
      const sortSplit = sortParam.split(',');
      setPagination({
        ...pagination,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }

    const handleMessage = message => {
      console.log('Hello: ', message);
    };

    listener.subscribe((message: any) => {
      handleMessage(message);
    });
  }, [location.search]);

  const sort = p => () =>
    setPagination({
      ...pagination,
      order: pagination.order === ASC ? DESC : ASC,
      sort: p,
    });

  const handlePagination = currentPage =>
    setPagination({
      ...pagination,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    getUsersFromProps();
  };

  const toggleActive = user => () => {
    dispatch(
      updateUser({
        ...user,
        activated: !user.activated,
      })
    );
  };

  const onFileChange = event => {
    const file = event.target.files[0];

    if (file) {
      const formData = new FormData();
      formData.append('file', file);

      // Replace 'YOUR_BACKEND_API_ENDPOINT' with the actual API endpoint to handle file uploads
      axios
        .post(`/api/upload/student-ids-mapping`, formData, {
          headers: {
            'Content-Type': 'multipart/form-data',
          },
        })
        .then(response => {
          // Handle successful response from the backend
          getUsersFromProps();
          excelFileInputRef.current.value = '';
          toast.success(translate('userManagement.updateStudentIds'), {
            position: toast.POSITION.TOP_LEFT,
          });
        })
        .catch(error => {
          console.error('Error uploading file:', error);
          // Handle error
        });
    }
  };

  const handleSendNotificationStudent = () => {
    sendNotificationStudent('Hello', 'admin', 'student');
  };

  const account = useAppSelector(state => state.authentication.account);
  const users = useAppSelector(state => state.userManagement.users);
  const totalItems = useAppSelector(state => state.userManagement.totalItems);
  const loading = useAppSelector(state => state.userManagement.loading);

  return (
    <div className="m-3">
      <h2 id="user-management-page-heading" data-cy="userManagementPageHeading">
        <Translate contentKey="userManagement.home.title">Users</Translate>
        <div className="d-flex justify-content-end">
          {/* <Button className="me-2 btn-action" onClick={handleSendNotificationStudent} disabled={loading}>
            <FontAwesomeIcon icon="bell" spin={loading} />
          </Button> */}
          <Button className="me-2 btn-action" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />
          </Button>
          <label htmlFor="fileInput" className="custom-file-upload">
            {translate('userManagement.importExcel')}
          </label>
          <input id="fileInput" type="file" onChange={onFileChange} style={{ display: 'none' }} ref={excelFileInputRef} />
          <Link to="new" className="btn btn-success">
            <Translate contentKey="userManagement.home.createLabel">Create a new user</Translate>
          </Link>
        </div>
      </h2>
      <Table responsive striped>
        <thead>
          <tr>
            <th className="hand d-flex gap-2" onClick={sort('id')}>
              <div className="d-flex gap-2 align-items-center">
                <Translate contentKey="global.field.id">ID</Translate>
                <FontAwesomeIcon icon="sort" />
              </div>
            </th>
            <th className="hand" onClick={sort('login')}>
              <div className="d-flex gap-2 align-items-center">
                <Translate contentKey="userManagement.login">Login</Translate>
                <FontAwesomeIcon icon="sort" />
              </div>
            </th>
            <th className="hand" onClick={sort('email')}>
              <div className="d-flex gap-2 align-items-center">
                <Translate contentKey="userManagement.email">Email</Translate>
                <FontAwesomeIcon icon="sort" />
              </div>
            </th>
            <th className="hand" onClick={sort('activated')}>
              <div className="d-flex gap-2 align-items-center">
                <Translate contentKey="userManagement.activated">Activated</Translate>
                <FontAwesomeIcon icon="sort" />
              </div>
            </th>
            {/* <th className="hand" onClick={sort('langKey')}>
              <Translate contentKey="userManagement.langKey">Lang Key</Translate>
              <FontAwesomeIcon icon="sort" />
            </th> */}
            <th>
              <Translate contentKey="userManagement.profiles">Profiles</Translate>
            </th>
            <th className="hand" onClick={sort('studentId')}>
              <div className="d-flex gap-2 align-items-center">
                <Translate contentKey="userManagement.studentId">Student ID</Translate>
                <FontAwesomeIcon icon="sort" />
              </div>
            </th>
            {/* <th className="hand" onClick={sort('createdDate')}>
              <Translate contentKey="userManagement.createdDate">Created Date</Translate>
              <FontAwesomeIcon icon="sort" />
            </th>
            <th className="hand" onClick={sort('lastModifiedBy')}>
              <Translate contentKey="userManagement.lastModifiedBy">Last Modified By</Translate>
              <FontAwesomeIcon icon="sort" />
            </th>
            <th id="modified-date-sort" className="hand" onClick={sort('lastModifiedDate')}>
              <Translate contentKey="userManagement.lastModifiedDate">Last Modified Date</Translate>
              <FontAwesomeIcon icon="sort" />
            </th> */}
            <th />
          </tr>
        </thead>
        <tbody>
          {users.map((user, i) => (
            <tr id={user.login} key={`user-${i}`}>
              <td>
                {/* <Button tag={Link} to={user.login} color="link" size="sm"> */}
                {user.id}
                {/* </Button> */}
              </td>
              <td>{user.login}</td>
              <td>{user.email}</td>
              <td>
                {user.activated ? (
                  <Button color="success" onClick={toggleActive(user)}>
                    <span>{translate('userManagement.activated')}</span>
                  </Button>
                ) : (
                  <Button color="danger" onClick={toggleActive(user)}>
                    <span>{translate('userManagement.deactivated')}</span>
                  </Button>
                )}
              </td>
              {/* <td>{user.langKey}</td> */}
              <td>
                {user.authorities
                  ? user.authorities.map((authority, j) => (
                      <div key={`user-auth-${i}-${j}`}>
                        <Badge color="info">{authority}</Badge>
                      </div>
                    ))
                  : null}
              </td>
              <td>{user.studentId}</td>
              {/* <td>
                {user.createdDate ? <TextFormat value={user.createdDate} type="date" format={APP_DATE_FORMAT} blankOnInvalid /> : null}
              </td>
              <td>{user.lastModifiedBy}</td>
              <td>
                {user.lastModifiedDate ? (
                  <TextFormat value={user.lastModifiedDate} type="date" format={APP_DATE_FORMAT} blankOnInvalid />
                ) : null}
              </td> */}
              <td className="text-end">
                <div className="d-flex gap-1 justify-content-end">
                  {/* <Button tag={Link} to={user.login} color="info" size="sm">
                    <FontAwesomeIcon icon="eye" />{' '}
                    <span className="d-none d-md-inline">
                      <Translate contentKey="entity.action.view">View</Translate>
                    </span>
                  </Button> */}
                  <Button tag={Link} to={`${user.login}/edit`} className="btn-action" size="sm">
                    <FontAwesomeIcon icon="pencil-alt" />{' '}
                    {/* <span className="d-none d-md-inline">
                      <Translate contentKey="entity.action.edit">Edit</Translate>
                    </span> */}
                  </Button>
                  <Button tag={Link} to={`${user.login}/delete`} className="btn-action" size="sm" disabled={account.login === user.login}>
                    <FontAwesomeIcon icon="trash" />{' '}
                    {/* <span className="d-none d-md-inline">
                      <Translate contentKey="entity.action.delete">Delete</Translate>
                    </span> */}
                  </Button>
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>
      {totalItems ? (
        <div className={users?.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-end d-flex">
            <JhiItemCount page={pagination.activePage} total={totalItems} itemsPerPage={pagination.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-end d-flex">
            <JhiPagination
              activePage={pagination.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={pagination.itemsPerPage}
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

export default UserManagement;
