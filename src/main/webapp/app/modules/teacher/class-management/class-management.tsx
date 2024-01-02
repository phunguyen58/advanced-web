import './class-management.scss';

import React, { useEffect, useState } from 'react';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import CreateClassModal from '../../../shared/components/create-class-modal/create-class-modal';
import { convertDateTimeToServer } from 'app/shared/util/date-utils';
import { createEntity, getEntities } from 'app/entities/course/course.reducer';
import { Button } from 'primereact/button';
import ClassCard from '../../../shared/components/class-card/class-card';
import { useLocation, useNavigate } from 'react-router-dom';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { getSortState } from 'react-jhipster';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';

export const ClassManagement = () => {
  const dispatch = useAppDispatch();
  const account = useAppSelector(state => state.authentication.account);
  const classes = useAppSelector(state => state.course.entities);
  const loading = useAppSelector(state => state.course.loading);

  const location = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'id'), location.search)
  );

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

  const [visible, setVisible] = useState(false);

  const toggleModal = () => {
    setVisible(!visible);
  };

  const handleCreateClass = _class => {
    // Perform actions to create the class (e.g., make an API call)
    console.log(`Creating class with name: ${JSON.stringify(_class)}`);
    //
    _class.expirationDate = convertDateTimeToServer(_class.expirationDate);
    _class.createdDate = convertDateTimeToServer(_class.createdDate);
    _class.lastModifiedDate = convertDateTimeToServer(_class.lastModifiedDate);

    const entity = {
      ..._class,
    };
    dispatch(createEntity(entity));
  };

  return (
    <div className="d-flex min-vh-100 flex-column aw-class-management-container pt-1">
      <div className="d-flex justify-content-end">
        <Button className="aw-create-class-btn" label="Create class" icon="pi pi-plus" onClick={() => setVisible(true)} />
      </div>
      <CreateClassModal visible={visible} setVisible={toggleModal} onCreateClass={handleCreateClass}></CreateClassModal>
      <div className="d-flex flex-wrap gap-3 p-3">
        {classes.map((clazz, index) => (
          <ClassCard course={clazz}></ClassCard>
        ))}
      </div>
    </div>
  );
};

export default ClassManagement;
