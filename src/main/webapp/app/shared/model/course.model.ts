import dayjs from 'dayjs';
import { IAssignment } from 'app/shared/model/assignment.model';

export interface ICourse {
  id?: number;
  code?: string;
  name?: string;
  invitationCode?: string;
  expirationDate?: string | null;
  gradeStructureId?: number;
  isDeleted?: boolean | null;
  createdBy?: string;
  createdDate?: string;
  lastModifiedBy?: string;
  lastModifiedDate?: string;
  assignments?: IAssignment | null;
}

export const defaultValue: Readonly<ICourse> = {
  isDeleted: false,
};
