import React, { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button } from 'primereact/button';
import { Dropdown, DropdownChangeEvent } from 'primereact/dropdown';
import { Formik, Field, Form, useFormik, FieldArray, FieldArrayRenderProps } from 'formik';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { translate, Translate, ValidatedField } from 'react-jhipster';
import { GradeType } from 'app/shared/model/enumerations/grade-type.model';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import * as _ from 'lodash';
import './class-grade-structure.scss';
import { IGradeComposition } from 'app/shared/model/grade-composition.model';
import {
  createGradeComposition,
  deleteGradeComposition,
  getCourse,
  getGradeCompositionByCourseId,
  getGradeCompositions,
  updateGradeComposition,
} from './class-grade-structure-util';
import { ICourse } from 'app/shared/model/course.model';
import * as Yup from 'yup';
import axios from 'axios';

export interface FormData {
  gradeCompositions: IGradeComposition[];
}

const ClassGradeStructure = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const { id } = useParams<'id'>();
  const account = useAppSelector(state => state.authentication.account);
  const isNew = id === undefined;
  const isStudent = account.authorities.includes('ROLE_STUDENT');

  const [gradeCompositions, setGradeCompositions] = useState<IGradeComposition[]>([]);
  const [gradeType, setGradeType] = useState<GradeType>(GradeType.NONE);
  const loading = useAppSelector(state => state.gradeStructure.loading);
  const updating = useAppSelector(state => state.gradeStructure.updating);
  const updateSuccess = useAppSelector(state => state.gradeStructure.updateSuccess);
  const [course, setCourse] = useState<ICourse>();

  useEffect(() => {
    getGradeCompositions(id).then(value => {
      setGradeCompositions(value.data);
      setGradeType(value.data[0].type);
    });
  }, []);

  const handleClose = () => {
    navigate('/grade-structure');
  };

  const validationSchema = Yup.object().shape({
    gradeCompositions: Yup.array().of(
      Yup.object().shape({
        name: Yup.string().required(translate('entity.validation.required')),
        scale: Yup.number().required(translate('entity.validation.required')),
      })
    ),
  });

  useEffect(() => {
    getCourse(id).then(value => setCourse(value.data));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);

    const entity = {
      ...gradeCompositions,
      ...values,
    };
  };

  const defaultValues = () => {
    return { type: 'PERCENTAGE', scale: 0, createdDate: displayDefaultDateTime(), lastModifiedDate: displayDefaultDateTime() };
  };

  return (
    <div className="d-flex aw-class-grade-structure-container flex-column">
      <div className="aw-grade-type d-flex flex-row gap-2 align-items-center">
        <label htmlFor="type">
          <b>{translate('webApp.gradeStructure.type')}</b>
        </label>
        <Dropdown
          name="type"
          options={Object.keys(GradeType).map(type => ({ label: translate(`webApp.GradeType.${type}`), value: type }))}
          value={gradeType}
          onChange={(e: DropdownChangeEvent) => {
            setGradeType(e.value);
          }}
          placeholder={translate('webApp.gradeStructure.type')}
          disabled={isStudent}
        />
      </div>
      <div className="aw-form">
        <Formik
          initialValues={{ gradeCompositions: gradeCompositions } as FormData}
          enableReinitialize
          validationSchema={validationSchema}
          onSubmit={async (data: FormData) => {
            data.gradeCompositions.forEach(value => {
              value.type = gradeType;
            });
            axios.post(`/api/grade-compositions/bulk/${course.id}`, data.gradeCompositions).then(res => {
              setGradeCompositions(res.data);
            });
          }}
        >
          {({ values }) => (
            <Form>
              <FieldArray name="gradeCompositions">
                {({ insert, remove, push }) => (
                  <div className="aw-composition-item">
                    {values.gradeCompositions.length > 0 &&
                      values.gradeCompositions.map((composition, index) => (
                        <div className="d-flex flex-row gap-3 mt-3" key={index}>
                          {!isStudent && (
                            <div className="d-flex flex-column gap-1 justify-content-center">
                              <i
                                className="pi pi-fw pi-chevron-up position"
                                onClick={() => {
                                  if (index > 0) {
                                    insert(index - 1, composition);
                                    remove(index + 1);
                                  }
                                }}
                              />
                              <i
                                className="pi pi-fw pi-chevron-down position"
                                onClick={() => {
                                  if (index < values.gradeCompositions.length - 1) {
                                    insert(index + 2, composition);
                                    remove(index);
                                  }
                                }}
                              />
                            </div>
                          )}
                          <div className="grade-composition-container d-flex flex-row gap-3">
                            <div className="d-flex flex-row gap-2 align-items-center">
                              <label className="mandatory" htmlFor={`gradeCompositions.${index}.name`}>
                                <span className="label">Name</span>
                              </label>
                              <Field name={`gradeCompositions.${index}.name`} type="text" disabled={isStudent} />
                            </div>
                            <div className="d-flex flex-row gap-2  align-items-center">
                              <label className="mandatory" htmlFor={`gradeCompositions.${index}.scale`}>
                                <span className="label">Scale</span>
                              </label>
                              <Field name={`gradeCompositions.${index}.scale`} type="number" disabled={isStudent} />
                            </div>
                            <div className="aw-is-public-checkbox">
                              <Field name={`gradeCompositions.${index}.isPublic`} type="checkbox" disabled={isStudent} />
                              <label htmlFor={`gradeCompositions.${index}.isPublic`}>Publish</label>
                            </div>
                          </div>
                          {!isStudent && (
                            <div className="aw-delete-btn-container justify-content-end">
                              <Button type="button" className="btn btn-action" onClick={() => remove(index)}>
                                <FontAwesomeIcon icon={'trash'} />
                              </Button>
                            </div>
                          )}
                        </div>
                      ))}
                    {!isStudent && (
                      <div className="d-flex justify-content-center">
                        <Button
                          className="btn-add mt-3 mb-3"
                          onClick={() =>
                            push({
                              type: gradeType,
                              // createdDate: displayDefaultDateTime(),
                              // lastModifiedDate: displayDefaultDateTime(),
                              name: `Grade composition ${values.gradeCompositions.length + 1}`,
                              scale: 0,
                              isDeleted: false,
                              isPublic: false,
                              // lastModifiedBy: account.login,
                              // createdBy: account.login,
                            } as IGradeComposition)
                          }
                          type="button"
                        >
                          <FontAwesomeIcon icon={'plus'} />
                        </Button>
                      </div>
                    )}
                  </div>
                )}
              </FieldArray>
              {!isStudent && (
                <Button className="btn btn-success" id="save-entity" type="submit" disabled={updating}>
                  <Translate contentKey="entity.action.save">Save</Translate>
                </Button>
              )}
            </Form>
          )}
        </Formik>
      </div>
    </div>
  );
};

export default ClassGradeStructure;
