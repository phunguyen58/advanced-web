import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CourseGrade from './course-grade';
import CourseGradeDetail from './course-grade-detail';
import CourseGradeUpdate from './course-grade-update';
import CourseGradeDeleteDialog from './course-grade-delete-dialog';

const CourseGradeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CourseGrade />} />
    <Route path="new" element={<CourseGradeUpdate />} />
    <Route path=":id">
      <Route index element={<CourseGradeDetail />} />
      <Route path="edit" element={<CourseGradeUpdate />} />
      <Route path="delete" element={<CourseGradeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CourseGradeRoutes;
