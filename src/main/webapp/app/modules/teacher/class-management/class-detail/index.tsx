import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import { ClassDetailMenu } from 'app/shared/components/class-detail-menu/class-detail-menu';
import ClassStream from './class-stream/class-stream';
import ClassWork from './class-work/class-people';
import ClassPeople from './class-people/class-people';
import ClassGrade from './class-grade/class-grade';
import ClassDetail from './class-detail';

const ClassDetailRoutes = () => (
  <div className="d-flex mt-5">
    <ClassDetailMenu></ClassDetailMenu>
    <ErrorBoundaryRoutes>
      <Route index element={<ClassDetail />} />
      <Route path="stream" element={<ClassStream />} />
      <Route path="class-work" element={<ClassWork />} />
      <Route path="people" element={<ClassPeople />} />
      <Route path="grade" element={<ClassGrade />} />
    </ErrorBoundaryRoutes>
  </div>
);

export default ClassDetailRoutes;
