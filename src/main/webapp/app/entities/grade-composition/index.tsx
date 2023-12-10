import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import GradeComposition from './grade-composition';
import GradeCompositionDetail from './grade-composition-detail';
import GradeCompositionUpdate from './grade-composition-update';
import GradeCompositionDeleteDialog from './grade-composition-delete-dialog';

const GradeCompositionRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<GradeComposition />} />
    <Route path="new" element={<GradeCompositionUpdate />} />
    <Route path=":id">
      <Route index element={<GradeCompositionDetail />} />
      <Route path="edit" element={<GradeCompositionUpdate />} />
      <Route path="delete" element={<GradeCompositionDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default GradeCompositionRoutes;
