import './home.scss';

import React from 'react';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { Row, Col, Alert } from 'reactstrap';

import { useAppSelector } from 'app/config/store';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);

  return (
    <div className="home">
      <div className="title">
        <div className="logo-container">
          <img className="logo" src="/content/images/open-book 1.svg" />
        </div>
        <div className="title-text-container">
          <span className="title-text">Eduvi - Education Online</span>
        </div>
      </div>
      <div className="introduction">
        <span className="introduction-text">Welcome to Eduvi Online Learning Platform</span>
      </div>
    </div>
  );
};

export default Home;
