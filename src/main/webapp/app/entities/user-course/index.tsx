import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import UserCourse from './user-course';
import UserCourseDetail from './user-course-detail';
import UserCourseUpdate from './user-course-update';
import UserCourseDeleteDialog from './user-course-delete-dialog';

const UserCourseRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<UserCourse />} />
    <Route path="new" element={<UserCourseUpdate />} />
    <Route path=":id">
      <Route index element={<UserCourseDetail />} />
      <Route path="edit" element={<UserCourseUpdate />} />
      <Route path="delete" element={<UserCourseDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default UserCourseRoutes;
