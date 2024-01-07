import axios from 'axios';
import { createAsyncThunk, isFulfilled, isPending, isRejected } from '@reduxjs/toolkit';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { IQueryParams, createEntitySlice, EntityState, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { IAssignmentGrade, defaultValue } from 'app/shared/model/assignment-grade.model';
import thunk from 'redux-thunk';

const initialState: EntityState<IAssignmentGrade> = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

const apiUrl = 'api/assignment-grades';

// Actions

export const getEntities = createAsyncThunk('assignmentGrade/fetch_entity_list', async ({ page, size, sort }: IQueryParams) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}&` : '?'}cacheBuster=${new Date().getTime()}`;
  return axios.get<IAssignmentGrade[]>(requestUrl);
});

export const getEntitiesByAssignmentId = createAsyncThunk(
  'assignmentGrade/fetch_entity_list_by_assignment',
  async ({ query, page, size, sort }: IQueryParams) => {
    const requestUrl = `${apiUrl}${sort ? `?${query}&page=${page}&size=${size}&sort=${sort}&` : '?'}cacheBuster=${new Date().getTime()}`;
    return axios.get<IAssignmentGrade[]>(requestUrl);
  }
);

export const getEntity = createAsyncThunk(
  'assignmentGrade/fetch_entity',
  async (id: string | number) => {
    const requestUrl = `${apiUrl}/${id}`;
    return axios.get<IAssignmentGrade>(requestUrl);
  },
  { serializeError: serializeAxiosError }
);

export const createEntity = createAsyncThunk(
  'assignmentGrade/create_entity',
  async (entity: IAssignmentGrade, thunkAPI) => {
    const result = await axios.post<IAssignmentGrade>(apiUrl, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const createAssignmentGradeList = createAsyncThunk(
  'assignmentGrade/create_assignment_grade_list',
  async ({ courseId, assignmentId }: { courseId: number; assignmentId: number }, thunkAPI) => {
    const result = await axios.post(`${apiUrl}/course/${courseId}/create-assigment-grade-list/${assignmentId}`);
    // Dispatch other actions here if needed
    // thunkAPI.dispatch(result.data);
    // Return the result of the API call (if needed)
    return result.data;
  },
  { serializeError: serializeAxiosError }
);

export const updateEntity = createAsyncThunk(
  'assignmentGrade/update_entity',
  async (entity: IAssignmentGrade, thunkAPI) => {
    const result = await axios.put<IAssignmentGrade>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const partialUpdateEntity = createAsyncThunk(
  'assignmentGrade/partial_update_entity',
  async (entity: IAssignmentGrade, thunkAPI) => {
    const result = await axios.patch<IAssignmentGrade>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const deleteEntity = createAsyncThunk(
  'assignmentGrade/delete_entity',
  async (id: string | number, thunkAPI) => {
    const requestUrl = `${apiUrl}/${id}`;
    const result = await axios.delete<IAssignmentGrade>(requestUrl);
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

// slice

export const AssignmentGradeSlice = createEntitySlice({
  name: 'assignmentGrade',
  initialState,
  extraReducers(builder) {
    builder
      .addCase(getEntity.fulfilled, (state, action) => {
        state.loading = false;
        state.entity = action.payload.data;
      })
      .addCase(deleteEntity.fulfilled, state => {
        state.updating = false;
        state.updateSuccess = true;
        state.entity = {};
      })
      .addMatcher(isFulfilled(getEntities, getEntitiesByAssignmentId), (state, action) => {
        const { data, headers } = action.payload;

        return {
          ...state,
          loading: false,
          entities: data,
          totalItems: parseInt(headers['x-total-count'], 10),
        };
      })
      .addMatcher(isFulfilled(createEntity, updateEntity, partialUpdateEntity), (state, action) => {
        state.updating = false;
        state.loading = false;
        state.updateSuccess = true;
        state.entity = action.payload.data;
      })
      .addMatcher(isPending(getEntities, getEntitiesByAssignmentId, getEntity), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
      })
      .addMatcher(isPending(createEntity, updateEntity, partialUpdateEntity, deleteEntity), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.updating = true;
      })
      .addMatcher(isFulfilled(createAssignmentGradeList), (state, action) => {
        state.updating = false;
        state.loading = false;
        state.updateSuccess = true;
        state.entities = action.payload; // Assuming the payload is the response data
      })
      .addMatcher(isPending(createAssignmentGradeList), state => {
        state.loading = true;
        state.errorMessage = null;
        state.updateSuccess = false;
      })
      .addMatcher(isRejected(createAssignmentGradeList), (state, action) => {
        state.errorMessage = action.error.message;
        state.updating = false;
        state.loading = false;
        state.updateSuccess = false;
      });
  },
});

export const { reset } = AssignmentGradeSlice.actions;

// Reducer
export default AssignmentGradeSlice.reducer;
