import dayjs from 'dayjs';
import { IGradeStructure } from 'app/shared/model/grade-structure.model';

export interface IGradeComposition {
  id?: number;
  name?: string;
  minGradeScale?: number;
  maxGradeScale?: number;
  position?: number;
  isDeleted?: boolean | null;
  createdBy?: string;
  createdDate?: string;
  lastModifiedBy?: string;
  lastModifiedDate?: string;
  gradeStructures?: IGradeStructure[] | null;
}

export const defaultValue: Readonly<IGradeComposition> = {
  isDeleted: false,
};
