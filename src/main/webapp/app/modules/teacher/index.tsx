import React from 'react';

import { Route } from 'react-router-dom';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import ClassManagement from './class-management/class-management';
import ClassDetail from './class-management/class-detail/class-detail';

const TeacherRoutes = () => (
  <div>
    <div className="teacher-content">
      <ErrorBoundaryRoutes>
        <Route path="detail" element={<ClassDetail />} />
        <Route index element={<ClassManagement />} />
      </ErrorBoundaryRoutes>
    </div>
  </div>
);

export default TeacherRoutes;
