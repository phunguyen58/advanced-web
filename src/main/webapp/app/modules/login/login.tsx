import React, { useEffect, useState } from 'react';
import { Navigate, useLocation, useNavigate } from 'react-router-dom';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { login, socialLogin } from 'app/shared/reducers/authentication';
import LoginModal from './login-modal';

export const Login = () => {
  const dispatch = useAppDispatch();
  const isAuthenticated = useAppSelector(state => state.authentication.isAuthenticated);
  const loginError = useAppSelector(state => state.authentication.loginError);
  const showModalLogin = useAppSelector(state => state.authentication.showModalLogin);
  const [showModal, setShowModal] = useState(showModalLogin);
  const navigate = useNavigate();
  const location = useLocation();

  useEffect(() => {
    setShowModal(true);
  }, []);

  const handleLogin = (username, password, rememberMe = false) => dispatch(login(username, password, rememberMe));

  const handleSocialLogin = (jwt, userId) => dispatch(socialLogin(jwt, userId));

  const handleClose = () => {
    setShowModal(false);
    navigate('/');
  };

  const { from } = (location.state as any) || { from: { pathname: '/', search: location.search } };
  if (isAuthenticated) {
    return <Navigate to={from} replace />;
  }
  return (
    <LoginModal
      showModal={showModal}
      handleLogin={handleLogin}
      handleClose={handleClose}
      loginError={loginError}
      handleSocialLogin={handleSocialLogin}
    />
  );
};

export default Login;
