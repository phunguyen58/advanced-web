import './home.scss';

import React from 'react';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { Row, Col, Alert } from 'reactstrap';

import { useAppSelector } from 'app/config/store';
import ClassManagement from '../class-management/class-management';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);

  return <ClassManagement></ClassManagement>;
};

export default Home;
