import './landing.scss';

import React from 'react';

export const Landing = () => {
  return (
    <div className="landing">
      <div className="title">
        <div className="logo-container">
          <img className="logo" src="/content/images/open-book 1.svg" />
        </div>
        <div className="title-text-container">
          <span className="title-text">Eduvi</span>
        </div>
      </div>
      <div className="introduction">
        <span className="introduction-text">Grow up your skills by online courses with Eduvi</span>
      </div>
    </div>
  );
};

export default Landing;
