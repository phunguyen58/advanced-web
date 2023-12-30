import React from 'react';
import { Card } from 'primereact/card';
import { Button } from 'primereact/button';
import './class-card.scss';
import { ICourse } from 'app/shared/model/course.model';
import { useNavigate } from 'react-router-dom';
export interface IClassCardProp {
  course: ICourse;
}

export default function ClassCard(prop: IClassCardProp) {
  const navigate = useNavigate();

  const showDetail = () => {
    navigate('/teacher/class-management/detail');
  };
  const header = (
    <div className="position-relative">
      {/* <img alt="Card" src="https://primefaces.org/cdn/primereact/images/usercard.png" /> */}
      <img className="aw-card-header-image" src="../../../../content/images/title-area-left-background-holiday.jpg" alt="" />
      <div className="d-flex flex-column position-absolute aw-content-container">
        <span className="aw-card-title">{prop.course.name}</span>
        <span className="aw-card-description">{prop.course.name}</span>
      </div>
    </div>
  );
  const footer = (
    <div className="d-flex justify-content-end">
      <Button className="aw-detail-btn" label="Detail" icon="pi pi-check" onClick={showDetail} />
    </div>
  );

  return <Card footer={footer} header={header} className="aw-card-container"></Card>;
}
