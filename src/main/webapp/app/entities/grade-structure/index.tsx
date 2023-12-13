import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import GradeStructure from './grade-structure';
import GradeStructureDetail from './grade-structure-detail';
import GradeStructureUpdate from './grade-structure-update';
import GradeStructureDeleteDialog from './grade-structure-delete-dialog';

const GradeStructureRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<GradeStructure />} />
    <Route path="new" element={<GradeStructureUpdate />} />
    <Route path=":id">
      <Route index element={<GradeStructureDetail />} />
      <Route path="edit" element={<GradeStructureUpdate />} />
      <Route path="delete" element={<GradeStructureDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default GradeStructureRoutes;
