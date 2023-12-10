import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/categories">
        <Translate contentKey="global.menu.entities.categories" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/product">
        <Translate contentKey="global.menu.entities.product" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/course">
        <Translate contentKey="global.menu.entities.course" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/grade-structure">
        <Translate contentKey="global.menu.entities.gradeStructure" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/grade-composition">
        <Translate contentKey="global.menu.entities.gradeComposition" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/assignment">
        <Translate contentKey="global.menu.entities.assignment" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/assignment-grade">
        <Translate contentKey="global.menu.entities.assignmentGrade" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/course-grade">
        <Translate contentKey="global.menu.entities.courseGrade" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/user-course">
        <Translate contentKey="global.menu.entities.userCourse" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
