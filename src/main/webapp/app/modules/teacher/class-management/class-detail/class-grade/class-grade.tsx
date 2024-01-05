import React, { useEffect, useState } from 'react';
import './class-grade.scss';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { useLocation, useNavigate } from 'react-router-dom';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { getSortState } from 'react-jhipster';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { getEntities } from 'app/entities/assignment/assignment.reducer';
import { IAssignmentGrade } from 'app/shared/model/assignment-grade.model';
import { IAssignment } from 'app/shared/model/assignment.model';

const ClassGrade = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, 999, 'id'), location.search)
  );

  const assignmentList: IAssignment[] = useAppSelector(state => state.assignment.entities);
  const loading = useAppSelector(state => state.assignment.loading);
  const totalItems = useAppSelector(state => state.assignment.totalItems);

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

  return (
    <div className="class-detail-container">
      {assignmentList.map((assignment: IAssignment, key) => (
        <div>
          <span>{assignment.id}</span>
        </div>
      ))}
    </div>
  );
};

export default ClassGrade;
