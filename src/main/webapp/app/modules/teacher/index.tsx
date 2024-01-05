import React from 'react';

import { Route } from 'react-router-dom';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import ClassManagement from './class-management';

const TeacherRoutes = () => (
  <div>
    <div className="teacher-content">
      <ErrorBoundaryRoutes>
        <Route path="/*" element={<ClassManagement />} />
      </ErrorBoundaryRoutes>
    </div>
  </div>
);

export default TeacherRoutes;
