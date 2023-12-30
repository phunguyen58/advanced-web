import './home.scss';

import React, { useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { Row, Col, Alert } from 'reactstrap';

import { useAppSelector } from 'app/config/store';
import ClassManagement from '../class-management/class-management';
import { useNavigate } from 'react-router-dom';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import { AUTHORITIES } from 'app/config/constants';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);
  const isAdmin = useAppSelector(state => hasAnyAuthority(state.authentication.account.authorities, [AUTHORITIES.ADMIN]));
  const navigate = useNavigate();

  useEffect(() => {
    if (isAdmin) {
      navigate('/admin/user-management');
    }
  }, []);

  return <ClassManagement></ClassManagement>;
};

export default Home;
