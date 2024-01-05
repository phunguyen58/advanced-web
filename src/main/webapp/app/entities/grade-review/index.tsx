import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import GradeReview from './grade-review';
import GradeReviewDetail from './grade-review-detail';
import GradeReviewUpdate from './grade-review-update';
import GradeReviewDeleteDialog from './grade-review-delete-dialog';

const GradeReviewRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<GradeReview />} />
    <Route path="new" element={<GradeReviewUpdate />} />
    <Route path=":id">
      <Route index element={<GradeReviewDetail />} />
      <Route path="edit" element={<GradeReviewUpdate />} />
      <Route path="delete" element={<GradeReviewDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default GradeReviewRoutes;
