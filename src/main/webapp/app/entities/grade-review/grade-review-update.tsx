import React, { useEffect, useState } from 'react';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { Link, useLocation, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { AUTHORITIES } from 'app/config/constants';
import { createEntity, getEntity, reset, updateEntity } from './grade-review.reducer';
import './grade-review.scss';
import { set } from 'lodash';
import { IAssignment } from 'app/shared/model/assignment.model';
import axios from 'axios';
import {
  sendNotificationCreateGradeReview,
  sendNotificationStudentCommentOnGradeReview,
  sendNotificationSubmitGradeReview,
  sendNotificationTeacher,
  sendNotificationTeacherCommentOnGradeReview,
} from 'app/config/websocket-middleware';
import { InputTextarea } from 'primereact/inputtextarea';
import { is } from 'immer/dist/internal';

export const GradeReviewUpdate = () => {
  const dispatch = useAppDispatch();

  const [assignmentGrade, setAssignmentGrade] = React.useState(null); // [IAssignmentGrade
  const [assignment, setAssignment] = React.useState(null);
  const [course, setCourse] = React.useState(null);
  const [gradeReview, setGradeReview] = React.useState(null);
  const [gradeComposition, setGradeComposition] = React.useState(null);
  const [comment, setComment] = React.useState(null);
  const [comments, setComments] = React.useState([]);

  const navigate = useNavigate();
  const location = useLocation();

  const { id } = useParams<'id'>();

  const searchParams = new URLSearchParams(location.search);
  const assignmentGradeId = searchParams.get('assignmentGradeId');

  const isNew = Boolean(assignmentGradeId);

  const account = useAppSelector(state => state.authentication.account);
  const gradeReviewEntity = useAppSelector(state => state.gradeReview.entity);
  const loading = useAppSelector(state => state.gradeReview.loading);
  const updating = useAppSelector(state => state.gradeReview.updating);
  const updateSuccess = useAppSelector(state => state.gradeReview.updateSuccess);

  const handleClose = () => {
    // window.history.back();
    return;
  };

  useEffect(() => {
    setGradeReview(gradeReviewEntity);

    const getAssignment = (_assignmentGrade, __gradeReview) => {
      axios.get(`/api/assignments/${_assignmentGrade.assignment.id}`).then(response => {
        const data: any = response.data;
        setAssignmentGrade(_assignmentGrade);

        setAssignment(data);

        setCourse(data.course);
        setGradeComposition(data.gradeComposition);

        const _gradeReview = {
          ...gradeReviewEntity,
          ...__gradeReview,
          assigmentId: data?.id,
          studentId: _assignmentGrade?.studentId,
          currentGrade: gradeReviewEntity?.currentGrade ?? _assignmentGrade?.grade,
          gradeCompositionId: data?.gradeComposition?.id,
          assimentGradeId: _assignmentGrade?.id,
          isFinal: false,
          courseId: data?.course?.id,
        };

        setGradeReview(_gradeReview);
      });
    };

    const getAssigmentGrade = (_assignmentGradeId, _gradeReview) => {
      axios.get(`/api/assignment-grades/${_assignmentGradeId}`).then(response => {
        const data: any = response.data;
        getAssignment(data, _gradeReview);
      });
    };

    if (isNew) {
      if (account.authorities.includes(AUTHORITIES.TEACHER)) {
        history.back();
      }

      getAssigmentGrade(assignmentGradeId, null);
      dispatch(reset());
    } else {
      dispatch(getEntity(id)).then(res => {
        const payload: any = res.payload;

        if (payload.data.comment) {
          setComments(JSON.parse(payload.data.comment) as any[]);
        }
        getAssigmentGrade(payload.data.assimentGradeId, payload.data);
      });
    }
  }, [assignmentGradeId, id]);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...gradeReview,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity)).then(res => {
        const payload: any = res.payload;
        sendNotificationCreateGradeReview('gradeReviewCreated', payload.data.id, 'notification');
      });
    } else {
      dispatch(updateEntity(entity)).then(res => {
        sendNotificationSubmitGradeReview('gradeReviewCompleted', entity.id, entity.studentId, 'notification');
      });
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...gradeReview,
        };

  const sendComment = () => {
    if (!comment) {
      return;
    }

    const _comments = [...comments];
    _comments.push({ author: `${account.firstName} ${account.lastName}`, content: comment, login: account.login });
    setComments(_comments);
    setComment('');
    const entity = {
      ...gradeReview,
      comment: JSON.stringify(_comments),
    };
    axios.put(`/api/grade-reviews/${gradeReview.id}`, entity);
    if (account.authorities.includes(AUTHORITIES.TEACHER)) {
      sendNotificationTeacherCommentOnGradeReview('newComment', gradeReview.id, gradeReview.studentId, 'notification');
    }
    if (account.authorities.includes(AUTHORITIES.STUDENT)) {
      sendNotificationStudentCommentOnGradeReview('newComment', gradeReview.id, 'notification');
    }
  };

  return (
    <>
      <div className="grade-review-container">
        <Row>
          <Col md="8" className="mt-2 mb-2">
            <h2 id="webApp.gradeReview.home.createOrEditLabel" data-cy="GradeReviewCreateUpdateHeading">
              <Translate contentKey="webApp.gradeReview.detail.title">Create or edit a GradeReview</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="mb-3">
          <div className="assignment-info d-flex flex-column">
            <div className="d-flex flex-row gap-2">
              <b>{translate('webApp.course.code')}:</b>
              <span>{course?.code}</span>
            </div>

            <div className="d-flex flex-row gap-2">
              <b>{translate('webApp.course.name')}:</b>
              <span>{course?.name}</span>
            </div>

            <div className="d-flex flex-row gap-2">
              <b>{translate('webApp.assignment.detail.title')}:</b>
              <span>{assignment?.name}</span>
            </div>

            <div className="d-flex flex-row gap-2">
              <b>{translate('webApp.gradeComposition.detail.title')}:</b>
              <span>
                {gradeComposition?.scale} ({gradeComposition?.type})
              </span>
            </div>

            <div className="d-flex flex-row gap-2">
              <b>{gradeReview?.finalGrade ? translate('webApp.gradeReview.oldGrade') : translate('webApp.gradeReview.currentGrade')}:</b>
              <span>{gradeReview?.currentGrade}</span>
            </div>

            {!account.authorities.includes(AUTHORITIES.STUDENT) && (
              <div className="d-flex flex-row gap-2">
                <b>{translate('webApp.gradeReview.studentId')}:</b>
                <span>{gradeReview?.studentId}</span>
              </div>
            )}

            {gradeReviewEntity.expectationGrade && (
              <div className="d-flex flex-row gap-2">
                <b>{translate('webApp.gradeReview.expectationGrade')}:</b>
                <span>{gradeReview?.expectationGrade}</span>
              </div>
            )}

            {gradeReviewEntity.studentExplanation && (
              <div className="d-flex flex-row gap-2">
                <b>{translate('webApp.gradeReview.studentExplanation')}:</b>
                <span>{gradeReview?.studentExplanation}</span>
              </div>
            )}

            {/* {gradeReviewEntity.teacherComment && account.authorities.includes(AUTHORITIES.STUDENT) && (
            <div className="d-flex flex-row gap-2">
              <b>{translate('webApp.gradeReview.teacherComment')}:</b>
              <span>{gradeReview?.teacherComment}</span>
            </div>
          )} */}

            {!isNew && account.authorities.includes(AUTHORITIES.STUDENT) && (
              <div className="d-flex flex-row gap-2">
                <b>{translate('webApp.gradeReview.finalGrade')}:</b>
                <span>{gradeReview?.finalGrade ?? '...'}</span>
              </div>
            )}
          </div>
        </Row>
        <Row>
          <Col md="12">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
                {account.authorities.includes(AUTHORITIES.STUDENT) && isNew && (
                  <ValidatedField
                    label={translate('webApp.gradeReview.expectationGrade')}
                    id="grade-review-expectationGrade"
                    name="expectationGrade"
                    data-cy="expectationGrade"
                    type="text"
                    validate={{
                      required: { value: true, message: translate('entity.validation.required') },
                      validate: v => isNumber(v) || translate('entity.validation.number'),
                    }}
                    readOnly={!isNew}
                  />
                )}
                {account.authorities.includes(AUTHORITIES.STUDENT) && isNew && (
                  <ValidatedField
                    label={translate('webApp.gradeReview.studentExplanation')}
                    id="grade-review-studentExplanation"
                    name="studentExplanation"
                    data-cy="studentExplanation"
                    type="text"
                    validate={{
                      required: { value: true, message: translate('entity.validation.required') },
                    }}
                    readOnly={!isNew}
                  />
                )}
                {!account.authorities.includes(AUTHORITIES.STUDENT) && (
                  <ValidatedField
                    label={translate('webApp.gradeReview.finalGrade')}
                    id="grade-review-finalGrade"
                    name="finalGrade"
                    data-cy="finalGrade"
                    type="text"
                    validate={{
                      required: { value: true, message: translate('entity.validation.required') },
                      validate: v => isNumber(v) || translate('entity.validation.number'),
                    }}
                  />
                )}
                {/* {!account.authorities.includes(AUTHORITIES.STUDENT) && (
                <ValidatedField
                  label={translate('webApp.gradeReview.teacherComment')}
                  id="grade-review-teacherComment"
                  name="teacherComment"
                  data-cy="teacherComment"
                  type="text"
                />
              )} */}
                <div className="d-flex justify-content-end">
                  <Button
                    id="cancel-save"
                    data-cy="entityCreateCancelButton"
                    replace
                    className="btn btn-outline-dark"
                    color="outline-dark"
                    onClick={() => {
                      history.back();
                    }}
                  >
                    <span className="d-none d-md-inline">
                      <Translate contentKey="entity.action.back">Back</Translate>
                    </span>
                  </Button>
                  &nbsp;
                  {(isNew || account.authorities.includes(AUTHORITIES.TEACHER)) && (
                    <Button
                      color="primary"
                      id="save-entity"
                      data-cy="entityCreateSaveButton"
                      type="submit"
                      disabled={updating}
                      className="btn btn-success"
                    >
                      {account.authorities.includes(AUTHORITIES.STUDENT) ? (
                        <span>
                          <Translate contentKey="webApp.gradeReview.send">Send</Translate>
                        </span>
                      ) : (
                        <span>
                          <Translate contentKey="webApp.gradeReview.submit">Submit</Translate>
                        </span>
                      )}
                    </Button>
                  )}
                </div>
              </ValidatedForm>
            )}
          </Col>
        </Row>
      </div>
      {!isNew && (
        <div className="comment-container">
          <span>{translate('webApp.gradeReview.comment')}</span>
          <div className="comments-container mb-3 mt-3">
            {comments.map((c, index) => (
              <div
                key={index}
                className={
                  c.login === account.login
                    ? 'd-flex justify-content-end align-items-center gap-2'
                    : 'd-flex justify-content-start align-items-center gap-2'
                }
              >
                {c.login !== account.login && <b>{c.author}</b>}
                <div className="comment-item">
                  <span>{c.content}</span>
                </div>
              </div>
            ))}
          </div>
          <InputTextarea className="w-100" value={comment} onChange={e => setComment(e.target.value)} autoResize={true}></InputTextarea>
          <div className="d-flex justify-content-end">
            <button className="mt-2 btn btn-primary" onClick={() => sendComment()}>
              <span>{translate('webApp.gradeReview.send')}</span>
            </button>
          </div>
        </div>
      )}
    </>
  );
};

export default GradeReviewUpdate;
