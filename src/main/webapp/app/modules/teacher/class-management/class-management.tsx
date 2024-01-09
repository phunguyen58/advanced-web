import './class-management.scss';

import React, { useEffect, useState } from 'react';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import CreateClassModal from '../../../shared/components/create-class-modal/create-class-modal';
import { convertDateTimeToServer } from 'app/shared/util/date-utils';
import { createEntity, getEntities, getMyCourses } from 'app/entities/course/course-router/course.reducer';
import { Button } from 'primereact/button';
import ClassCard from '../../../shared/components/class-card/class-card';
import { useLocation, useNavigate } from 'react-router-dom';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { JhiItemCount, JhiPagination, Translate, getSortState, translate } from 'react-jhipster';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { ICourse } from 'app/shared/model/course.model';
import { AUTHORITIES } from 'app/config/constants';
import CourseUpdate from 'app/entities/course/course-router/course-update';
import { Dialog } from 'primereact/dialog';

export const ClassManagement = () => {
  const dispatch = useAppDispatch();
  const account = useAppSelector(state => state.authentication.account);
  const classes = useAppSelector(state => state.course.entities);
  const loading = useAppSelector(state => state.course.loading);
  const totalItems = useAppSelector(state => state.course.totalItems);
  const location = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'id'), location.search)
  );

  const getAllEntities = () => {
    dispatch(
      getMyCourses({
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

  const handleEventFromChild = data => {
    if (data === false) {
      setVisible(false);
    }
  };

  return (
    <div className="d-flex min-vh-100 flex-column aw-class-management-container p-2">
      <div className="d-flex align-items-center justify-content-between">
        <h2 id="course-heading" data-cy="CourseHeading">
          <Translate contentKey="webApp.course.home.classManagement">Courses</Translate>
        </h2>
        {account.authorities.includes(AUTHORITIES.TEACHER) && (
          <Button
            className="aw-create-class-btn mt-3 mr-4"
            label={translate('webApp.classManagement.createClassButton')}
            icon="pi pi-plus"
            onClick={() => setVisible(true)}
          />
        )}
      </div>
      <Dialog
        className="join-a-course-dialog"
        header={translate('webApp.course.home.createClass')}
        draggable={false}
        resizable={false}
        visible={visible}
        style={{ width: '50vw' }}
        onHide={() => setVisible(false)}
      >
        <CourseUpdate onEventTrigger={() => handleEventFromChild}></CourseUpdate>
      </Dialog>
      <div className="d-flex flex-wrap gap-3 p-2">
        {classes && classes.length > 0 ? (
          <div className="grid">
            {classes.map(course => (
              <div className="col-4">
                <ClassCard course={course} />
              </div>
            ))}
          </div>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="webApp.course.home.notFound">No Courses found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={classes && classes.length > 0 ? '' : 'd-none'}>
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

export default ClassManagement;
