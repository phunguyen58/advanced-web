import { createAsyncThunk } from '@reduxjs/toolkit';
import { IAssignmentGrade } from 'app/shared/model/assignment-grade.model';
import { IQueryParams } from 'app/shared/reducers/reducer.utils';
import axios from 'axios';

const assignmentGradeUrl = 'api/assignment-grades';

// Actions

export const getAssimentGrades = createAsyncThunk('assignmentGrade/fetch_entity_list', async ({ page, size, sort }: IQueryParams) => {
  const requestUrl = `${assignmentGradeUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}&` : '?'}cacheBuster=${new Date().getTime()}`;
  return axios.get<IAssignmentGrade[]>(requestUrl);
});
