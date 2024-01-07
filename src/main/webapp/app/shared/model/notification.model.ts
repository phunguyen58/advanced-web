export interface INotification {
  id?: number;
  message?: string | null;
  topic?: string | null;
  receivers?: string | null;
  sender?: string | null;
  role?: string | null;
  link?: string | null;
  gradeReviewId?: number | null;
  isRead?: boolean | null;
}

export const defaultValue: Readonly<INotification> = {};
