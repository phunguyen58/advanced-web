import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import CourseRouterRoutes from './course-router';
import CourseRouter from './course-router';
import ClassManagement from 'app/modules/teacher/class-management/class-management';

const CourseRoutes = () => (
  <div>
    {/* <Route index element={<Course />} /> */}
    <ErrorBoundaryRoutes>
      <Route path=":id">
        <Route path="detail/*" element={<CourseRouter />} />
      </Route>
    </ErrorBoundaryRoutes>
  </div>
);

export default CourseRoutes;
