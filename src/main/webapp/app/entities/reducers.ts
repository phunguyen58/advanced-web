import categories from 'app/entities/categories/categories.reducer';
import product from 'app/entities/product/product.reducer';
import course from 'app/entities/course/course-router/course.reducer';
import gradeStructure from 'app/entities/grade-structure/grade-structure.reducer';
import gradeComposition from 'app/entities/grade-composition/grade-composition.reducer';
import assignment from 'app/entities/assignment/assignment.reducer';
import assignmentGrade from 'app/entities/assignment-grade/assignment-grade.reducer';
import courseGrade from 'app/entities/course-grade/course-grade.reducer';
import userCourse from 'app/entities/user-course/user-course.reducer';
import gradeReview from 'app/entities/grade-review/grade-review.reducer';
import notification from 'app/entities/notification/notification.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  categories,
  product,
  course,
  gradeStructure,
  gradeComposition,
  assignment,
  assignmentGrade,
  courseGrade,
  userCourse,
  gradeReview,
  notification,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
