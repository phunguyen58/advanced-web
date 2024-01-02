import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Categories from './categories';
import Product from './product';
import Course from './course';
import GradeStructure from './grade-structure';
import GradeComposition from './grade-composition';
import Assignment from './assignment';
import AssignmentGrade from './assignment-grade';
import CourseGrade from './course-grade';
import UserCourse from './user-course';
import InvitationLink from './invitation-link/invitation-link';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="categories/*" element={<Categories />} />
        <Route path="product/*" element={<Product />} />
        <Route path="course/*" element={<Course />} />
        <Route path="grade-structure/*" element={<GradeStructure />} />
        <Route path="grade-composition/*" element={<GradeComposition />} />
        <Route path="assignment/*" element={<Assignment />} />
        <Route path="assignment-grade/*" element={<AssignmentGrade />} />
        <Route path="course-grade/*" element={<CourseGrade />} />
        <Route path="user-course/*" element={<UserCourse />} />
        <Route path="invitation/:invitationCode" element={<InvitationLink />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
