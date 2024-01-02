import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import InvitationLink from './invitation-link';

const InvitationLinkRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route path=":invitationCode">
      <Route index element={<InvitationLink />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default InvitationLinkRoutes;
