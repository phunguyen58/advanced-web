import React, { useContext, useEffect, useState } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useLocation, useNavigate, useParams, useSearchParams } from 'react-router-dom';
import { TabMenu } from 'primereact/tabmenu';
import { MenuItem } from 'primereact/menuitem';
import './class-detail-menu.scss';
import { MenuContext } from './class-detail-context';
import { set } from 'lodash';
import { dispatch } from 'rxjs/internal/observable/pairs';

export const ClassDetailMenu = () => {
  const { activeMenu, setActiveMenu } = useContext(MenuContext);
  const [curActiveMenu, setCurActiveMenu] = useState(activeMenu);
  const navigate = useNavigate();
  const location = useLocation();
  const TAB_INDEX = ['stream', 'class-work', 'people', 'grade'];
  const TAB_NAME = {
    stream: 0,
    'class-work': 1,
    people: 2,
    grade: 3,
  };

  useEffect(() => {
    const path = location.pathname.split('/')[4];
    if (path) {
      setActiveIndex(TAB_NAME[path]);
    }
  });

  const { id } = useParams<'id'>();
  const [searchParams] = useSearchParams();

  const [activeIndex, setActiveIndex] = useState(TAB_NAME[curActiveMenu]);
  const items: MenuItem[] = [
    { label: 'Grade structure', icon: 'pi pi-fw pi-home', className: 'aw-menu-item' },
    { label: 'Assginments', icon: 'pi pi-fw pi-calendar', className: 'aw-menu-item' },
    { label: 'Members', icon: 'pi pi-fw pi-users', className: 'aw-menu-item' },
    { label: 'Grade board', icon: 'pi pi-fw pi-folder', className: 'aw-menu-item' },
  ];

  const handleMenuItemClick = (menuName, path) => {
    setActiveMenu(menuName);
    setCurActiveMenu(menuName);
    navigate(path);
  };

  return (
    <div className="aw-class-detail-menu-container">
      <TabMenu
        model={items}
        unstyled={false}
        activeIndex={activeIndex}
        onTabChange={e => {
          handleMenuItemClick(TAB_INDEX[e.index], `/course/${id}/detail/${TAB_INDEX[e.index]}`);
          setActiveIndex(e.index);
        }}
      />
    </div>
  );
};
