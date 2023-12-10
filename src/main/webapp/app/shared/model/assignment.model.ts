import dayjs from 'dayjs';
import { ICourse } from 'app/shared/model/course.model';
import { IAssignmentGrade } from 'app/shared/model/assignment-grade.model';

export interface IAssignment {
  id?: number;
  name?: string;
  weight?: number | null;
  isDeleted?: boolean | null;
  createdBy?: string;
  createdDate?: string;
  lastModifiedBy?: string;
  lastModifiedDate?: string;
  courses?: ICourse[] | null;
  assignmentGrades?: IAssignmentGrade | null;
}

export const defaultValue: Readonly<IAssignment> = {
  isDeleted: false,
};
