import dayjs from 'dayjs';
import { IAssignment } from 'app/shared/model/assignment.model';

export interface IAssignmentGrade {
  id?: number;
  studentId?: string;
  grade?: number;
  isDeleted?: boolean | null;
  createdBy?: string;
  createdDate?: string;
  lastModifiedBy?: string;
  lastModifiedDate?: string;
  assignments?: IAssignment[] | null;
}

export const defaultValue: Readonly<IAssignmentGrade> = {
  isDeleted: false,
};
