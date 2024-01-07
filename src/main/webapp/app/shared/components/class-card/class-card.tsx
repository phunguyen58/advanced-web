import React from 'react';
import { Card } from 'primereact/card';
import { Button } from 'primereact/button';
import './class-card.scss';
import { ICourse } from 'app/shared/model/course.model';
import { useNavigate } from 'react-router-dom';
import { translate } from 'react-jhipster';
export interface IClassCardProp {
  course: ICourse;
}

export default function ClassCard(prop: IClassCardProp) {
  const navigate = useNavigate();

  const showDetail = id => {
    navigate(`/course/${id}/detail`);
  };
  const header = (
    <div className="position-relative">
      {prop.course.id % 2 === 0 ? (
        <img className="aw-card-header-image" src="content/images/img_bookclub.jpeg" alt="" />
      ) : (
        <img className="aw-card-header-image" src="content/images/img_learnlanguage.jpeg" alt="" />
      )}
      {/* <img className="aw-card-header-image" src="content/images/img_bookclub.jpeg" alt="" /> */}
      <div className="d-flex flex-column position-absolute aw-content-container">
        <span className="aw-card-title text-truncate">{prop.course.name}</span>
        <span className="aw-card-description text-decoration-underline">{prop.course.createdBy}</span>
      </div>
    </div>
  );
  const footer = (
    <div className="d-flex justify-content-end">
      <Button
        className="aw-detail-btn"
        label={translate('webApp.course.details')}
        icon="pi pi-arrow-up-right"
        onClick={() => showDetail(prop.course.id)}
      />
    </div>
  );

  return <Card footer={footer} header={header} className="aw-card-container"></Card>;
}
