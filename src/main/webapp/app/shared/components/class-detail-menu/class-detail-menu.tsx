import React, { useContext, useState } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { MenuContext } from './class-detail-context';
import { useNavigate } from 'react-router-dom';
import { TabMenu } from 'primereact/tabmenu';
import { MenuItem } from 'primereact/menuitem';

export const ClassDetailMenu = () => {
  const { activeMenu, setActiveMenu } = useContext(MenuContext);
  const [curActiveMenu, setCurActiveMenu] = useState(activeMenu);
  const navigate = useNavigate();
  const TAB_INDEX = ['stream', 'class-work', 'people', 'grade'];
  const TAB_NAME = {
    stream: 0,
    'class-work': 1,
    people: 2,
    grade: 3,
  };

  const [activeIndex, setActiveIndex] = useState(TAB_NAME[curActiveMenu]);
  const items: MenuItem[] = [
    { label: 'Stream', icon: 'pi pi-fw pi-home', className: 'aw-menu-item' },
    { label: 'ClassWork', icon: 'pi pi-fw pi-calendar', className: 'aw-menu-item' },
    { label: 'People', icon: 'pi pi-fw pi-people', className: 'aw-menu-item' },
    { label: 'Grade', icon: 'pi pi-fw pi-file', className: 'aw-menu-item' },
  ];

  const handleMenuItemClick = (menuName, path) => {
    setActiveMenu(menuName);
    setCurActiveMenu(menuName);
    navigate(path);
  };

  return (
    <div className="aw-class-detail-container">
      <TabMenu
        model={items}
        unstyled={false}
        activeIndex={activeIndex}
        onTabChange={e => {
          handleMenuItemClick(TAB_INDEX[e.index], '/class/detail/' + TAB_INDEX[e.index]);
          setActiveIndex(e.index);
        }}
      />
      aaaaaaaaaaaaaaaaa
    </div>
  );
};
