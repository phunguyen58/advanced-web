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
import AssignmentDetail from 'app/entities/assignment/assignment-detail';
import AssignmentGradeDetail from 'app/entities/assignment-grade/assignment-grade-detail';
import AssignmentUpdate from 'app/entities/assignment/assignment-update';
import AssignmentDeleteDialog from 'app/entities/assignment/assignment-delete-dialog';
import Course from '..';
import GradeReview from 'app/entities/grade-review';

const CourseRouterRoutes = () => {
  const handleCourseUpdate = () => {};
  return (
    <div>
      {/* <Route index element={<Course />} /> */}

      <ClassDetailMenu></ClassDetailMenu>
      <ErrorBoundaryRoutes>
        <Route path="" element={<ClassStream />} />
        {/* <Route index element={<CourseDetail />} /> */}
        <Route path="stream" element={<ClassStream />} />
        <Route path="edit" element={<CourseUpdate onEventTrigger={handleCourseUpdate} />} />
        <Route path="stream/edit" element={<CourseUpdate onEventTrigger={handleCourseUpdate} />} />
        <Route path="class-work" element={<ClassWork />} />
        <Route path="class-work/assignment/:asignmentId" element={<AssignmentDetail />} />
        <Route path="class-work/assignment/new" element={<AssignmentUpdate />} />
        <Route path="class-work/assignment/:asignmentId/edit" element={<AssignmentUpdate />} />
        <Route path="class-work/assignment/:asignmentId/delete" element={<AssignmentDeleteDialog />} />
        <Route path="people" element={<ClassPeople />} />
        {/* <Route path="people/user-management-class" element={<ClassPeople />} /> */}
        <Route path="grade" element={<ClassGrade />} />
        {/* <Route path="new" element={<CourseUpdate />} /> */}
        {/* <Route path="edit" element={<CourseUpdate />} /> */}
        <Route path="delete" element={<CourseDeleteDialog />} />
        <Route path="grade-structure" element={<ClassGradeStructure />} />
        <Route path="list" element={<Course />} />
        <Route path="grade-review/*" element={<GradeReview />} />
      </ErrorBoundaryRoutes>
    </div>
  );
};

export default CourseRouterRoutes;
