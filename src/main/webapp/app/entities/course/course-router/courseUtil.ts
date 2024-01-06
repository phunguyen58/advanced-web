import { ICourse } from 'app/shared/model/course.model';
import { defaultDateTime } from 'app/shared/util/date-utils';
import { v4 as uuidv4 } from 'uuid';

export const initCourse: () => ICourse = () => {
  const currentDate = new Date();
  const oneYearFromNow = new Date();
  oneYearFromNow.setFullYear(currentDate.getFullYear() + 1);

  return {
    code: '',
    name: '',
    ownerId: 0, // Provide a default ownerId
    description: null,
    invitationCode: uuidv4(), //TODO: add real invitation code
    expirationDate: null,
    isDeleted: false,
    createdBy: '',
    createdDate: currentDate,
    lastModifiedBy: '',
    lastModifiedDate: currentDate,
    assignments: null,
    gradeCompositions: null,
  };
};
