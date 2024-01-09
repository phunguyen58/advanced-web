import React from 'react';

import { Route } from 'react-router-dom';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import UserManagement from './user-management';
import Logs from './logs/logs';
import Health from './health/health';
import Metrics from './metrics/metrics';
import Configuration from './configuration/configuration';
import Docs from './docs/docs';
import Tracker from './tracker/tracker';
import { AdminSidebar } from 'app/shared/components/admin-sidebar/AdminSidebar';
import './index.scss';
import Course from 'app/entities/course/course';

const AdministrationRoutes = () => (
  <div>
    <AdminSidebar />
    <div className="admin-content">
      <ErrorBoundaryRoutes>
        <Route path="user-management/*" element={<UserManagement />} />
        <Route path="tracker" element={<Tracker />} />
        <Route path="health" element={<Health />} />
        <Route path="metrics" element={<Metrics />} />
        <Route path="configuration" element={<Configuration />} />
        <Route path="logs" element={<Logs />} />
        <Route path="docs" element={<Docs />} />
        <Route path="course-management" element={<Course />} />
      </ErrorBoundaryRoutes>
    </div>
  </div>
);

export default AdministrationRoutes;
