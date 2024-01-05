export interface IGradeReview {
  id?: number;
  gradeCompositionId?: number;
  studentId?: string;
  courseId?: number;
  reviewerId?: number | null;
  assigmentId?: number | null;
  assimentGradeId?: number | null;
  currentGrade?: number | null;
  expectationGrade?: number | null;
  studentExplanation?: string | null;
  teacherComment?: string | null;
  isFinal?: boolean | null;
}

export const defaultValue: Readonly<IGradeReview> = {
  isFinal: false,
};
