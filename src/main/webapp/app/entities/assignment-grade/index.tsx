import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import AssignmentGrade from './assignment-grade';
import AssignmentGradeDetail from './assignment-grade-detail';
import AssignmentGradeUpdate from './assignment-grade-update';
import AssignmentGradeDeleteDialog from './assignment-grade-delete-dialog';

const AssignmentGradeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<AssignmentGrade />} />
    <Route path="new" element={<AssignmentGradeUpdate />} />
    <Route path=":id">
      <Route index element={<AssignmentGradeDetail />} />
      <Route path="edit" element={<AssignmentGradeUpdate />} />
      <Route path="delete" element={<AssignmentGradeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AssignmentGradeRoutes;
