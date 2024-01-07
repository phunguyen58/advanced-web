import React, { useState, useEffect, useRef } from 'react';
import { Link, useLocation, useNavigate, useParams } from 'react-router-dom';
import { Button, Table, Badge } from 'reactstrap';
import { Translate, TextFormat, JhiPagination, JhiItemCount, getSortState, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, AUTHORITIES } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { getUsersAsAdmin, getUsersByCourseId, updateUser } from './user-management.reducer';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import './index.scss';
import axios from 'axios';
import { toast } from 'react-toastify';

export const UserManagementClass = () => {
  const excelFileInputRef = useRef(null);

  const dispatch = useAppDispatch();
  const account = useAppSelector(state => state.authentication.account);
  const users = useAppSelector(state => state.userManagement.users);
  const totalItems = useAppSelector(state => state.userManagement.totalItems);
  const loading = useAppSelector(state => state.userManagement.loading);

  const location = useLocation();
  const navigate = useNavigate();

  const [pagination, setPagination] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'id'), location.search)
  );

  const { id } = useParams<'id'>();

  const getUsersFromProps = () => {
    dispatch(
      getUsersByCourseId({
        queryParams: { page: pagination.activePage - 1, size: pagination.itemsPerPage, sort: `${pagination.sort},${pagination.order}` },
        courseId: Number(id),
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

  return (
    <div className="m-3">
      <h2 id="user-management-page-heading" data-cy="userManagementPageHeading">
        <div className="d-flex justify-content-end">
          <Button className="me-2 btn-action" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />
          </Button>
          {/* <label htmlFor="fileInput" className="custom-file-upload">
            {translate('userManagement.importExcel')}
          </label> */}
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
            <th>
              <Translate contentKey="userManagement.profiles">Profiles</Translate>
            </th>
            <th className="hand" onClick={sort('studentId')}>
              <div className="d-flex gap-2 align-items-center">
                <Translate contentKey="userManagement.studentId">Student ID</Translate>
                <FontAwesomeIcon icon="sort" />
              </div>
            </th>
          </tr>
        </thead>
        <tbody>
          {users.map((user, i) => (
            <tr id={user.login} key={`user-${i}`}>
              <td>{user.id}</td>
              <td>{user.login}</td>
              <td>{user.email}</td>
              <td>
                {user.authorities
                  ? user.authorities
                      .filter(f => f !== AUTHORITIES.USER)
                      .map((authority, j) => (
                        <div key={`user-auth-${i}-${j}`}>
                          <Badge color="info">{translate(`userManagement.userRole.${authority}`)}</Badge>
                        </div>
                      ))
                  : null}
              </td>
              <td>{user.studentId}</td>
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

export default UserManagementClass;
