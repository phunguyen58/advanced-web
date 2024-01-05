import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import UserManagement, { ClassManagement } from './class-management';
import ClassDetail from './class-detail/class-detail';

const ClassManagementRoutes = () => (
  <div className="class-management-container">
    <ErrorBoundaryRoutes>
      <Route path="detail" element={<ClassDetail />} />
    </ErrorBoundaryRoutes>
  </div>
);

export default ClassManagementRoutes;
