import React, { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button } from 'primereact/button';
import { Dropdown, DropdownChangeEvent } from 'primereact/dropdown';
import { Formik, Field, Form, useFormik, FieldArray, FieldArrayRenderProps } from 'formik';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { translate, Translate } from 'react-jhipster';
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

export interface FormData {
  gradeCompositions: IGradeComposition[];
}

const ClassGradeStructure = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const { id } = useParams<'id'>();
  const account = useAppSelector(state => state.authentication.account);
  const isNew = id === undefined;

  const [gradeCompositions, setGradeCompositions] = useState<IGradeComposition[]>([]);
  const [gradeType, setGradeType] = useState<GradeType>(GradeType.NONE);
  const loading = useAppSelector(state => state.gradeStructure.loading);
  const updating = useAppSelector(state => state.gradeStructure.updating);
  const updateSuccess = useAppSelector(state => state.gradeStructure.updateSuccess);
  const [course, setCourse] = useState<ICourse>();

  useEffect(() => {
    getGradeCompositions(1).then(value => setGradeCompositions(value.data));
  }, []);

  const handleClose = () => {
    navigate('/grade-structure');
  };

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
      <div className="aw-grade-type d-flex flex-column">
        <label htmlFor="type">
          <Translate contentKey="webApp.gradeStructure.type">Type</Translate>
        </label>
        <Dropdown
          name="type"
          options={Object.keys(GradeType).map(type => ({ label: translate(`webApp.GradeType.${type}`), value: type }))}
          value={gradeType}
          onChange={(e: DropdownChangeEvent) => {
            setGradeType(e.value);
          }}
          placeholder={translate('webApp.gradeStructure.type')}
        />
      </div>
      <div className="aw-form">
        <Formik
          initialValues={{ gradeCompositions: gradeCompositions } as FormData}
          enableReinitialize
          onSubmit={async (data: FormData) => {
            // await new Promise(r => setTimeout(r, 500));
            gradeCompositions.map(value => {
              return { ...value, type: gradeType };
            });
            const gradeIds = gradeCompositions.map(value => value.id).filter(id => id !== null);
            data = {
              gradeCompositions: data.gradeCompositions.map(value => {
                return { ...value, type: gradeType };
              }),
            };

            console.log('gradeCompositions', data);
            const newGradeComposition = data.gradeCompositions.filter(composition => !composition.id);
            const oldGradeComposition = data.gradeCompositions.filter(composition => composition.id && gradeIds.includes(composition.id));
            const deletedGradeComposition = gradeCompositions.filter(
              composition => data.gradeCompositions.findIndex(value => value.id === composition.id) === -1
            );

            newGradeComposition
              .map(value => {
                return {
                  ...value,
                  createdDate: convertDateTimeToServer(value.createdDate),
                  lastModifiedDate: convertDateTimeToServer(value.lastModifiedDate),
                  course: course,
                };
              })
              .forEach(value => {
                try {
                  createGradeComposition(value);
                } catch (error) {
                  console.error(error);
                }
              });
            oldGradeComposition.forEach(value => {
              try {
                updateGradeComposition(value);
              } catch (error) {
                console.error(error);
              }
            });
            deletedGradeComposition.forEach(value => {
              try {
                updateGradeComposition({ ...value, isDeleted: true });
              } catch (error) {
                console.error(error);
              }
            });

            console.log('gradeConewGradeCompositionmpositions', newGradeComposition);
            console.log('oldGradeComposition', oldGradeComposition);
            console.log('deletedGradeComposition', deletedGradeComposition);
            // alert(JSON.stringify(data, null, 2));
          }}
        >
          {({ values }) => (
            <Form>
              <FieldArray name="gradeCompositions">
                {({ insert, remove, push }) => (
                  <div className="aw-composition-item">
                    {values.gradeCompositions.length > 0 &&
                      values.gradeCompositions.map((composition, index) => (
                        <div className="row" key={index}>
                          <div className="col">
                            <label htmlFor={`gradeCompositions.${index}.name`}>Name</label>
                            <Field name={`gradeCompositions.${index}.name`} type="text" />
                          </div>
                          <div className="col">
                            <label htmlFor={`gradeCompositions.${index}.scale`}>Scale</label>
                            <Field name={`gradeCompositions.${index}.scale`} type="number" />
                          </div>
                          <div className="col aw-is-public-checkbox">
                            <label htmlFor={`gradeCompositions.${index}.isPublic`}>isPublic</label>
                            <Field name={`gradeCompositions.${index}.isPublic`} type="checkbox" />
                          </div>
                          <div className="col aw-delete-btn-container">
                            <button type="button" className="secondary aw-delete-btn" onClick={() => remove(index)}>
                              X
                            </button>
                          </div>
                        </div>
                      ))}
                    <div
                      className="aw-add-btn"
                      onClick={() =>
                        push({
                          type: gradeType,
                          createdDate: displayDefaultDateTime(),
                          lastModifiedDate: displayDefaultDateTime(),
                          scale: 10,
                          isDeleted: false,
                          createdBy: account.login,
                          lastModifiedBy: account.login,
                          isPublic: false,
                        } as IGradeComposition)
                      }
                    >
                      Add composition
                    </div>
                  </div>
                )}
              </FieldArray>
              <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </Form>
          )}
        </Formik>
      </div>
    </div>
  );
};

export default ClassGradeStructure;
