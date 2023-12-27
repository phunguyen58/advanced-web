import './home.scss';

import React, { useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { Row, Col, Alert } from 'reactstrap';

import { useAppSelector } from 'app/config/store';
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

  return (
    <div className="home">
      <div className="title">
        <div className="logo-container">
          <img className="logo" src="/content/images/open-book 1.svg" />
        </div>
        <div className="title-text-container">
          <span className="title-text">Eduvi</span>
        </div>
      </div>
      <div className="introduction">
        <span className="introduction-text">Welcome to Eduvi Online Learning Platform</span>
      </div>
    </div>
  );
};

export default Home;
