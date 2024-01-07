import { IAssignmentGrade } from './assignment-grade.model';
import { IAssignment } from './assignment.model';
import { ICourse } from './course.model';
import { GradeType } from './enumerations/grade-type.model';
import { IUser } from './user.model';

export interface IGradeBoard {
  user?: IUser | null;
  studentId?: string | null;
  userAssignmentGradesInCourse?: IAssignmentGrade[] | null;
  assignmentsInCourse?: IAssignment[] | null;
  finalGrade?: number | null;
  gradeType?: GradeType | null;
  course?: ICourse | null;
}
