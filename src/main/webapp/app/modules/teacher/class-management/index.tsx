import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import UserManagement, { ClassManagement } from './class-management';

const ClassManagementRoutes = () => (
  <div className="class-management-container">
    <ErrorBoundaryRoutes></ErrorBoundaryRoutes>
  </div>
);

export default ClassManagementRoutes;
