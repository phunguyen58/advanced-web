import dayjs from 'dayjs';

export interface ICourseGrade {
  id?: number;
  gradeCompositionId?: number | null;
  studentId?: string;
  isMarked?: boolean | null;
  isDeleted?: boolean | null;
  createdBy?: string;
  createdDate?: string;
  lastModifiedBy?: string;
  lastModifiedDate?: string;
}

export const defaultValue: Readonly<ICourseGrade> = {
  isMarked: false,
  isDeleted: false,
};
