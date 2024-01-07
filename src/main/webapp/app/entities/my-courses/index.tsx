import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import MyCourses from './my-courses';

const MyCoursesRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<MyCourses />} />
  </ErrorBoundaryRoutes>
);

export default MyCoursesRoutes;
