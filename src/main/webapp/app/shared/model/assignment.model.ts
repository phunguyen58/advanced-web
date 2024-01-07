import dayjs from 'dayjs';
import { IAssignmentGrade } from 'app/shared/model/assignment-grade.model';
import { ICourse } from 'app/shared/model/course.model';
import { IGradeComposition } from 'app/shared/model/grade-composition.model';

export interface IAssignment {
  id?: number;
  name?: string;
  description?: string | null;
  weight?: number | null;
  isDeleted?: boolean | null;
  createdBy?: string;
  createdDate?: string;
  lastModifiedBy?: string;
  lastModifiedDate?: string;
  assignmentGrades?: IAssignmentGrade[] | null;
  course?: ICourse | null;
  gradeComposition?: IGradeComposition | null;
}

export const defaultValue: Readonly<IAssignment> = {
  isDeleted: false,
};
