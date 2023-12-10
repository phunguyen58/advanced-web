import dayjs from 'dayjs';
import { IGradeComposition } from 'app/shared/model/grade-composition.model';

export interface IGradeStructure {
  id?: number;
  courseId?: number;
  isDeleted?: boolean | null;
  createdBy?: string;
  createdDate?: string;
  lastModifiedBy?: string;
  lastModifiedDate?: string;
  gradeCompositions?: IGradeComposition | null;
}

export const defaultValue: Readonly<IGradeStructure> = {
  isDeleted: false,
};
