import React from 'react';

import { UncontrolledDropdown, DropdownToggle, DropdownMenu } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

export const NavDropdown = props => (
  <UncontrolledDropdown nav inNavbar id={props.id} data-cy={props['data-cy']}>
    <DropdownToggle nav caret className="d-flex align-items-center">
      {props.icon && <FontAwesomeIcon icon={props.icon} />}
      {props.img && <img className="aw-lang" style={{ width: '30px', height: '20px' }} src={props.img} />}
      <span>{props.name}</span>
    </DropdownToggle>
    <DropdownMenu end style={props.style}>
      {props.children}
    </DropdownMenu>
  </UncontrolledDropdown>
);
