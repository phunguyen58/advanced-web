import SockJS from 'sockjs-client';

import Stomp from 'webstomp-client';
import { Observable } from 'rxjs';
import { Storage } from 'react-jhipster';

import { websocketActivityMessage } from 'app/modules/administration/administration.reducer';
import { getAccount, logoutSession } from 'app/shared/reducers/authentication';

let stompClient = null;

let subscriber = null;
let connection: Promise<any>;
let connectedPromise: any = null;
export let listener: Observable<any>;
let listenerObserver: any;
let alreadyConnectedOnce = false;

const createConnection = (): Promise<any> => new Promise(resolve => (connectedPromise = resolve));

const createListener = (): Observable<any> =>
  new Observable(observer => {
    listenerObserver = observer;
  });

export const sendActivity = (page: string) => {
  connection?.then(() => {
    stompClient?.send(
      '/topic/activity', // destination
      JSON.stringify({ page }), // body
      {} // header
    );
  });
};

export const sendNotificationStudent = (message: string, userSenderLogin: string, userReceiverLogin) => {
  if (stompClient !== null && stompClient.connected) {
    stompClient.send(
      '/notification-student', // destination
      JSON.stringify({ message, userSenderLogin, userReceiverLogin }), // body
      {} // header
    );
  }
};

export const sendNotificationTeacher = (message: string, userSenderLogin: string, userReceiverLogin) => {
  if (stompClient !== null && stompClient.connected) {
    stompClient.send(
      '/notification-teacher', // destination
      JSON.stringify({ message, userSenderLogin, userReceiverLogin }), // body
      {} // header
    );
  }
};

export const sendNotificationCreateGradeReview = (message: string, gradeReviewId: number, type: string) => {
  if (stompClient !== null && stompClient.connected) {
    stompClient.send(
      '/notification-teacher', // destination
      JSON.stringify({ message, gradeReviewId, type }), // body
      {} // header
    );
  }
};

export const sendNotificationStudentCommentOnGradeReview = (message: string, gradeReviewId: number, type: string) => {
  if (stompClient !== null && stompClient.connected) {
    stompClient.send(
      '/notification-teacher', // destination
      JSON.stringify({ message, gradeReviewId, type }), // body
      {} // header
    );
  }
};

export const sendNotificationSubmitGradeReview = (
  message: string,
  gradeReviewId: number,
  courseId: string,
  studentId: string,
  type: string
) => {
  if (stompClient !== null && stompClient.connected) {
    stompClient.send(
      '/notification-student', // destination
      JSON.stringify({ message, gradeReviewId, studentId, type, courseId }), // body
      {} // header
    );
  }
};

export const sendNotificationTeacherCommentOnGradeReview = (message: string, gradeReviewId: number, studentId: string, type: string) => {
  if (stompClient !== null && stompClient.connected) {
    stompClient.send(
      '/notification-student', // destination
      JSON.stringify({ message, gradeReviewId, studentId, type }), // body
      {} // header
    );
  }
};

export const sendNotificationFinalizeGradeComposition = (message: string, courseId: number, type: string) => {
  if (stompClient !== null && stompClient.connected) {
    stompClient.send(
      '/notification-finalize-grade-composition', // destination
      JSON.stringify({ message, courseId, type }), // body
      {} // header
    );
  }
};

const subscribe = () => {
  connection.then(() => {
    subscriber = stompClient.subscribe('/topic/tracker', data => {
      listenerObserver.next(JSON.parse(data.body));
    });
    subscriber = stompClient.subscribe('/notification-student', data => {
      listenerObserver.next(JSON.parse(data.body));
    });
    subscriber = stompClient.subscribe('/notification-teacher', data => {
      listenerObserver.next(JSON.parse(data.body));
    });
    subscriber = stompClient.subscribe('/notification-finalize-grade-composition', data => {
      listenerObserver.next(JSON.parse(data.body));
    });
  });
};

const connect = () => {
  if (connectedPromise !== null || alreadyConnectedOnce) {
    // the connection is already being established
    return;
  }
  connection = createConnection();
  listener = createListener();

  // building absolute path so that websocket doesn't fail when deploying with a context path
  const loc = window.location;
  const baseHref = document.querySelector('base').getAttribute('href').replace(/\/$/, '');

  const headers = {};
  let url = '//' + loc.host + baseHref + '/websocket/tracker';
  const authToken = Storage.local.get('jhi-authenticationToken') || Storage.session.get('jhi-authenticationToken');
  if (authToken) {
    url += '?access_token=' + authToken;
  }
  const socket = new SockJS(url);
  stompClient = Stomp.over(socket, { protocols: ['v12.stomp'] });

  stompClient.connect(headers, () => {
    connectedPromise('success');
    connectedPromise = null;
    sendActivity(window.location.pathname);
    alreadyConnectedOnce = true;
  });
};

const disconnect = () => {
  if (stompClient !== null) {
    if (stompClient.connected) {
      stompClient.disconnect();
    }
    stompClient = null;
  }
  alreadyConnectedOnce = false;
};

const receive = () => listener;

const unsubscribe = () => {
  if (subscriber !== null) {
    subscriber.unsubscribe();
  }
  listener = createListener();
};

export default store => next => action => {
  if (getAccount.fulfilled.match(action)) {
    connect();
    if (!alreadyConnectedOnce) {
      subscribe();
      receive().subscribe(activity => {
        return store.dispatch(websocketActivityMessage(activity));
      });
    }
  } else if (getAccount.rejected.match(action) || action.type === logoutSession().type) {
    unsubscribe();
    disconnect();
  }
  return next(action);
};
