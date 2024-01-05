import React, { useState } from 'react';
import './class-detail.scss';
import { TabMenu } from 'primereact/tabmenu';
import { MenuItem } from 'primereact/menuitem';
import ClassStream from '../class-stream/class-stream';
import ClassWork from '../class-work/class-people';
import ClassPeople from '../class-people/class-people';
import ClassGrade from '../class-grade/class-grade';

const ClassDetail = () => {
  const [activeIndex, setActiveIndex] = useState(0);
  const items: MenuItem[] = [
    { label: 'Stream', icon: 'pi pi-fw pi-home', className: 'aw-menu-item' },
    { label: 'ClassWork', icon: 'pi pi-fw pi-calendar', className: 'aw-menu-item' },
    { label: 'People', icon: 'pi pi-fw pi-people', className: 'aw-menu-item' },
    { label: 'Grade', icon: 'pi pi-fw pi-file', className: 'aw-menu-item' },
  ];

  const TAB_INDEX = {
    STREAM: 0,
    CLASS_WORK: 1,
    PEOPLE: 2,
    GRADE: 3,
  };

  return (
    <div className="class-detail-container">
      <TabMenu
        model={items}
        unstyled={false}
        activeIndex={activeIndex}
        onTabChange={e => {
          setActiveIndex(e.index);
        }}
      />
      {activeIndex === TAB_INDEX.STREAM && <ClassStream></ClassStream>}
      {activeIndex === TAB_INDEX.CLASS_WORK && <ClassWork></ClassWork>}
      {activeIndex === TAB_INDEX.PEOPLE && <ClassPeople></ClassPeople>}
      {activeIndex === TAB_INDEX.GRADE && <ClassGrade></ClassGrade>}
    </div>
  );
};

export default ClassDetail;
