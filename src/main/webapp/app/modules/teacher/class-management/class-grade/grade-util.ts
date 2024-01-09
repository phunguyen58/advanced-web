import { createAsyncThunk } from '@reduxjs/toolkit';
import { IAssignmentGrade } from 'app/shared/model/assignment-grade.model';
import { IAssignment } from 'app/shared/model/assignment.model';
import { ICourse } from 'app/shared/model/course.model';
import { IGradeBoard } from 'app/shared/model/grade-board.model';
import { IUserCourse } from 'app/shared/model/user-course.model';
import { IUser } from 'app/shared/model/user.model';
import { IQueryParams, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import axios from 'axios';

const assignmentGradeUrl = 'api/assignment-grades';
const userCourseUrl = 'api/user-courses';
const userUrl = 'api/admin/users';
const courseUrl = 'api/courses';
const assignmentUrl = 'api/assignments';

// Actions

export const getAssimentGrades = studentId => {
  const requestUrl = `${assignmentGradeUrl}?page=${0}&size=${999}&studentId.equals=${studentId}`;
  return axios.get<IAssignmentGrade[]>(requestUrl);
};

export const getGradeBoards = courseId => {
  const requestUrl = `${assignmentGradeUrl}/grade-board/course-id/${courseId}`;
  return axios.get<IGradeBoard[]>(requestUrl);
};
export const getUserById = id => {
  const requestUrl = `${userUrl}/user-id/${id}`;
  return axios.get<IUser>(requestUrl);
};

export const getUserCourses = createAsyncThunk('userCourse/fetch_entity_list', async ({ page, size, sort, query }: IQueryParams) => {
  const requestUrl = `${userCourseUrl}${
    sort ? `?page=${page}&size=${size}&sort=${sort}&courseId.equals=${query}&` : '?'
  }cacheBuster=${new Date().getTime()}`;
  return axios.get<IUserCourse[]>(requestUrl);
});

export const getCourse = createAsyncThunk(
  'course/fetch_entity',
  async (id: string | number) => {
    const requestUrl = `${courseUrl}/${id}`;
    return axios.get<ICourse>(requestUrl);
  },
  { serializeError: serializeAxiosError }
);

export const getAssignmentsByCourseId = courseId => {
  const requestUrl = `${assignmentUrl}/course-id/${courseId}`;
  return axios.get<IAssignment[]>(requestUrl);
};
