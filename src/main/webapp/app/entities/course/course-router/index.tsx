import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ClassStream from 'app/modules/teacher/class-management/class-stream/class-stream';
import ClassWork from 'app/modules/teacher/class-management/class-work/class-people';
import ClassPeople from 'app/modules/teacher/class-management/class-people/class-people';
import ClassGrade from 'app/modules/teacher/class-management/class-grade/class-grade';
import { ClassDetailMenu } from 'app/shared/components/class-detail-menu/class-detail-menu';
import CourseUpdate from './course-update';
import CourseDeleteDialog from './course-delete-dialog';
import GradeStructure from 'app/entities/grade-structure/grade-structure';
import ClassGradeStructure from 'app/modules/teacher/class-management/class-grade-structure/class-grade-structure';

const CourseRouterRoutes = () => (
  <div>
    {/* <Route index element={<Course />} /> */}

    <ClassDetailMenu></ClassDetailMenu>
    <ErrorBoundaryRoutes>
      <Route path="" element={<ClassStream />} />
      {/* <Route index element={<CourseDetail />} /> */}
      <Route path="stream" element={<ClassStream />} />
      <Route path="class-work" element={<ClassWork />} />
      <Route path="people" element={<ClassPeople />} />
      <Route path="grade" element={<ClassGrade />} />
      <Route path="new" element={<CourseUpdate />} />
      <Route path="edit" element={<CourseUpdate />} />
      <Route path="delete" element={<CourseDeleteDialog />} />
      <Route path="grade-structure" element={<ClassGradeStructure />} />
    </ErrorBoundaryRoutes>
  </div>
);

export default CourseRouterRoutes;
