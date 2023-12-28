import React, { useState } from 'react';
import { Button } from 'primereact/button';
import { Dialog } from 'primereact/dialog';
import { InputText } from 'primereact/inputtext';
import './create-class-modal.scss';

const CreateClassModal = ({ visible, setVisible, onCreateClass }) => {
  const [className, setClassName] = useState('');

  const handleCreateClass = () => {
    // Perform any necessary validation before creating the class
    if (className.trim() === '') {
      // Handle validation error (e.g., show an alert)
      return;
    }

    // Generate random values for other properties
    const expirationDate = generateRandomDate();
    const createdDate = new Date().toISOString();
    const lastModifiedDate = new Date().toISOString();
    const lastModifiedBy = 'admin';
    const gradeStructureId = generateRandomGradeStructureId();
    const isDeleted = false; // or generate randomly if needed
    const createdBy = 'admin'; // or get from authentication if available
    const code = 'code';
    const invitationCode = 'invitationCode';

    // Call the onCreateClass function with the class name and generated properties
    onCreateClass({
      code,
      createdBy,
      createdDate,
      expirationDate,
      gradeStructureId,
      invitationCode,
      isDeleted,
      lastModifiedBy,
      lastModifiedDate,
      name: className,
    });

    // Clear the input and close the modal
    setClassName('');
    setVisible(false);
  };

  // Helper function to generate a random date within a reasonable range
  const generateRandomDate = () => {
    const currentDate = new Date();
    const randomDays = Math.floor(Math.random() * 30); // Adjust the range as needed
    currentDate.setDate(currentDate.getDate() + randomDays);
    return currentDate.toISOString();
  };

  // Helper function to generate a random grade structure ID
  const generateRandomGradeStructureId = () => {
    return Math.floor(Math.random() * 1000); // Adjust the range as needed
  };

  const footerContent = (
    <div className="aw-footer-container">
      <Button label="Cancel" icon="pi pi-times" onClick={() => setVisible(false)} className="aw-cancel-btn p-button-text" />
      <Button
        className="aw-create-btn"
        label="Create"
        icon="pi pi-check"
        onClick={() => {
          setVisible(false);
          handleCreateClass();
        }}
        autoFocus
      />
    </div>
  );

  return (
    <>
      <Dialog
        header="Create a New Class"
        visible={visible}
        style={{ width: '50vw' }}
        onHide={() => setVisible(false)}
        footer={footerContent}
        contentClassName="aw-dialog"
        className="aw-create-class-dialog-container"
      >
        <div className="d-flex w-100">
          <div className="justify-content-center w-100">
            <span className="p-float-label">
              <InputText className="w-100" id="className" value={className} onChange={e => setClassName(e.target.value)} />
              <label htmlFor="className">Class name</label>
            </span>
          </div>
        </div>
      </Dialog>
    </>
  );
};

export default CreateClassModal;
