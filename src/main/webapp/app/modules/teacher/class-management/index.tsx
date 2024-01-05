import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import ClassManagement from './class-management';
import ClassDetail from './class-detail';

const ClassManagementRoutes = () => (
  <div className="class-management-container">
    <ErrorBoundaryRoutes>
      <Route index element={<ClassManagement />} />
      <Route path=":id">
        <Route path="detail/*" element={<ClassDetail />} />
      </Route>
    </ErrorBoundaryRoutes>
  </div>
);

export default ClassManagementRoutes;
