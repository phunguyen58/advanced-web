import dayjs from 'dayjs';
import { GradeType } from 'app/shared/model/enumerations/grade-type.model';

export interface IGradeStructure {
  id?: number;
  courseId?: number;
  isDeleted?: boolean | null;
  createdBy?: string;
  createdDate?: string;
  lastModifiedBy?: string;
  lastModifiedDate?: string;
  type?: GradeType;
}

export const defaultValue: Readonly<IGradeStructure> = {
  isDeleted: false,
};
