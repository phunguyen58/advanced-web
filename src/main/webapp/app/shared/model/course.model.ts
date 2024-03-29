import dayjs from 'dayjs';
import { IAssignment } from 'app/shared/model/assignment.model';
import { IGradeComposition } from 'app/shared/model/grade-composition.model';

export interface ICourse {
  id?: number;
  code?: string;
  name?: string;
  ownerId?: number;
  description?: string | null;
  invitationCode?: string;
  expirationDate?: Date | null;
  isDeleted?: boolean | null;
  createdBy?: string;
  createdDate?: Date | null;
  lastModifiedBy?: string;
  lastModifiedDate?: Date | null;
  assignments?: IAssignment[] | null;
  gradeCompositions?: IGradeComposition[] | null;
}

export const defaultValue: Readonly<ICourse> = {
  isDeleted: false,
};
