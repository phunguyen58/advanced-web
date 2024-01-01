import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { joinAClass } from '../course/course.reducer';
import { Translate } from 'react-jhipster';

export const InvitationLink = () => {
  const dispatch = useAppDispatch();
  const { invitationCode } = useParams<'invitationCode'>();
  let isSuccessful = false;
  let isFailure = false;
  let idCourse = '';
  useEffect(() => {
    if (invitationCode) {
      dispatch(joinAClass(invitationCode))
        .then(response => {
          const payload = response.payload as { data: { id: string } };
          idCourse = payload.data.id;
          window.location.href = `/course/${idCourse}`;
          isSuccessful = true;
        })
        .catch(err => {
          isFailure = true;
        });
    }
  }, [dispatch, invitationCode]);
  return (
    <div className="p-4">
      <h2>Joining Course with invitation code: {invitationCode}</h2>
      <span>isSuccessful: {isSuccessful}</span>
      <span>isFailure: {isFailure}</span>
      {isSuccessful && (
        <span>
          <Translate contentKey="webApp.course.coursejoinedsuccessfullyLink"></Translate>
        </span>
      )}
      {isFailure && (
        <span>
          <Translate contentKey="webApp.course.coursejoinedfailure"></Translate>
        </span>
      )}
    </div>
  );
};

export default InvitationLink;
