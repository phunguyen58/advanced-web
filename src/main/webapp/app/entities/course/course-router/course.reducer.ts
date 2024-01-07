import { createAsyncThunk, isFulfilled, isPending } from '@reduxjs/toolkit';
import axios from 'axios';

import { ICourse, defaultValue } from 'app/shared/model/course.model';
import { EntityState, IQueryParams, createEntitySlice, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { cleanEntity } from 'app/shared/util/entity-utils';

const initialState: EntityState<ICourse> = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

const apiUrl = 'api/courses';

// Actions

export const getEntities = createAsyncThunk('course/fetch_entity_list', async ({ page, size, sort, query }: IQueryParams) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}&${query}&` : '?'}cacheBuster=${new Date().getTime()}`;
  return axios.get<ICourse[]>(requestUrl);
});

export const getMyCourses = createAsyncThunk('course/fetch_my_courses', async ({ page, size, sort }: IQueryParams) => {
  const requestUrl = `${apiUrl}/my-courses${sort ? `?page=${page}&size=${size}&sort=${sort}&` : '?'}cacheBuster=${new Date().getTime()}`;
  return axios.get<ICourse[]>(requestUrl);
});

export const getEntity = createAsyncThunk(
  'course/fetch_entity',
  async (id: string | number) => {
    const requestUrl = `${apiUrl}/${id}`;
    return axios.get<ICourse>(requestUrl);
  },
  { serializeError: serializeAxiosError }
);

export const createEntity = createAsyncThunk(
  'course/create_entity',
  async (entity: ICourse, thunkAPI) => {
    const result = await axios.post<ICourse>(apiUrl, cleanEntity(entity));
    thunkAPI.dispatch(getMyCourses({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const joinAClass = createAsyncThunk(
  'course/join_a_class',
  async (invitationCode: string, thunkAPI) => {
    const result = await axios.post<ICourse>(`${apiUrl}/invitation/${invitationCode}`);
    thunkAPI.dispatch(getMyCourses({}));
    return result; // Assuming result contains the course data
  },
  { serializeError: serializeAxiosError }
);

export const updateEntity = createAsyncThunk(
  'course/update_entity',
  async (entity: ICourse, thunkAPI) => {
    const result = await axios.put<ICourse>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const partialUpdateEntity = createAsyncThunk(
  'course/partial_update_entity',
  async (entity: ICourse, thunkAPI) => {
    const result = await axios.patch<ICourse>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const deleteEntity = createAsyncThunk(
  'course/delete_entity',
  async (id: string | number, thunkAPI) => {
    const requestUrl = `${apiUrl}/${id}`;
    const result = await axios.delete<ICourse>(requestUrl);
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

// slice

export const CourseSlice = createEntitySlice({
  name: 'course',
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
      .addMatcher(isFulfilled(getEntities, getMyCourses), (state, action) => {
        const { data, headers } = action.payload;

        return {
          ...state,
          loading: false,
          entities: data,
          totalItems: parseInt(headers['x-total-count'], 10),
        };
      })
      .addMatcher(isFulfilled(createEntity, joinAClass, updateEntity, partialUpdateEntity), (state, action) => {
        state.updating = false;
        state.loading = false;
        state.updateSuccess = true;
        state.entity = action.payload.data;
      })
      .addMatcher(isPending(getEntities, getEntity, getMyCourses), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
      })
      .addMatcher(isPending(createEntity, joinAClass, updateEntity, partialUpdateEntity, deleteEntity), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.updating = true;
      });
  },
});

export const { reset } = CourseSlice.actions;

// Reducer
export default CourseSlice.reducer;
