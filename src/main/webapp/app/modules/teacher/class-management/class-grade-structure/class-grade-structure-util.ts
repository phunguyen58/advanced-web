import { ICourse } from 'app/shared/model/course.model';
import { IGradeComposition } from 'app/shared/model/grade-composition.model';
import { cleanEntity } from 'app/shared/util/entity-utils';
import axios from 'axios';

const apiUrl = 'api/grade-compositions';
const courseUrl = 'api/courses';

// Actions

export const getGradeCompositions = async query => {
  const requestUrl = `${apiUrl}?courseId.equals=${query}&isDeleted.equals=false `;
  return axios.get<IGradeComposition[]>(requestUrl);
};

export const getGradeCompositionByCourseId = async (id: string | number) => {
  const requestUrl = `${apiUrl}/course-id/${id}`;
  return axios.get<IGradeComposition>(requestUrl);
};

export const getGradeComposition = async (id: string | number) => {
  const requestUrl = `${apiUrl}/${id}`;
  return axios.get<IGradeComposition>(requestUrl);
};

export const createGradeComposition = async (entity: IGradeComposition) => {
  const result = await axios.post<IGradeComposition>(apiUrl, cleanEntity(entity));
  return result;
};

export const updateGradeComposition = async (entity: IGradeComposition) => {
  const result = await axios.put<IGradeComposition>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
  return result;
};

export const partialUpdateGradeComposition = async (entity: IGradeComposition) => {
  const result = await axios.patch<IGradeComposition>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
  return result;
};

export const deleteGradeComposition = async (id: string | number) => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await axios.delete<IGradeComposition>(requestUrl);

  return result;
};

export const getCourse = async (id: string | number) => {
  const requestUrl = `${courseUrl}/${id}`;
  return axios.get<ICourse>(requestUrl);
};
