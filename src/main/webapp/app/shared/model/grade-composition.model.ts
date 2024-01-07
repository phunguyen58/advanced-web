import dayjs from 'dayjs';
import { IAssignment } from 'app/shared/model/assignment.model';
import { ICourse } from 'app/shared/model/course.model';
import { GradeType } from 'app/shared/model/enumerations/grade-type.model';

export interface IGradeComposition {
  id?: number;
  name?: string;
  scale?: number | null;
  isDeleted?: boolean | null;
  createdBy?: string;
  createdDate?: any;
  lastModifiedBy?: string;
  lastModifiedDate?: any;
  type?: GradeType;
  isPublic?: boolean | null;
  assignments?: IAssignment[] | null;
  course?: ICourse | null;
  position?: number | null;
}

export const defaultValue: Readonly<IGradeComposition> = {
  isDeleted: false,
  isPublic: false,
};
