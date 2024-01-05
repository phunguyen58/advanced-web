import React, { useContext, useState } from 'react';
import './AdminSidebar.scss';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { MenuContext } from './AdminSidebarContext';
import { useNavigate } from 'react-router-dom';

export const AdminSidebar = () => {
  const { activeMenu, setActiveMenu } = useContext(MenuContext);
  const [curActiveMenu, setCurActiveMenu] = useState(activeMenu);
  const navigate = useNavigate();

  const handleMenuItemClick = (menuName, path) => {
    setActiveMenu(menuName);
    setCurActiveMenu(menuName);
    navigate(path);
  };

  return (
    <div className="admin-sidebar-container">
      <div className="admin-menu-header">Admin Menu</div>
      <div
        className={`admin-menu-item ${curActiveMenu === 'user' ? 'active' : ''}`}
        onClick={() => handleMenuItemClick('user', '/admin/user-management')}
      >
        <FontAwesomeIcon icon="user" className="me-3" />
        <span>User management</span>
      </div>

      <div
        className={`admin-menu-item ${curActiveMenu === 'class' ? 'active' : ''}`}
        onClick={() => handleMenuItemClick('class', '/class')}
      >
        <FontAwesomeIcon icon="book" className="me-3" />
        <span>Class management</span>
      </div>
    </div>
  );
};
