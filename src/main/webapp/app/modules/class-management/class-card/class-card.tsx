import React from 'react';
import { Card } from 'primereact/card';
import { Button } from 'primereact/button';
import './class-card.scss';

export default function ClassCard() {
  const header = (
    <div className="position-relative">
      {/* <img alt="Card" src="https://primefaces.org/cdn/primereact/images/usercard.png" /> */}
      <img className="aw-card-header-image" src="../../../../content/images/title-area-left-background-holiday.jpg" alt="" />
      <div className="d-flex flex-column position-absolute aw-content-container">
        <span className="aw-card-title">Testing class</span>
        <span className="aw-card-description">co ban</span>
      </div>
    </div>
  );
  const footer = (
    <div className="d-flex justify-content-end">
      <Button className="aw-detail-btn" label="Detail" icon="pi pi-check" />
    </div>
  );

  return <Card footer={footer} header={header} className="aw-card-container"></Card>;
}
