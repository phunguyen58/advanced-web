import './class-management.scss';

import React, { useState } from 'react';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import CreateClassModal from './create-class-modal/create-class-modal';
import { convertDateTimeToServer } from 'app/shared/util/date-utils';
import { createEntity } from 'app/entities/course/course.reducer';
import { Button } from 'primereact/button';
import ClassCard from './class-card/class-card';

export const ClassManagement = () => {
  //   const account = useAppSelector(state => state.authentication.account);
  const dispatch = useAppDispatch();

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
        <ClassCard></ClassCard>
        <ClassCard></ClassCard>
        <ClassCard></ClassCard>
        <ClassCard></ClassCard>
        <ClassCard></ClassCard>
        <ClassCard></ClassCard>
      </div>
    </div>
  );
};

export default ClassManagement;
