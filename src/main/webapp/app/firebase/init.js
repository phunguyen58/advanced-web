// Import the functions you need from the SDKs you need
import { initializeApp } from 'firebase/app';
import config from './config.json';

// const { apiKey, appId, authDomain, measurementId, messagingSenderId, projectId, storageBucket } = config;

// const firebaseConfig = {
//   apiKey,
//   authDomain,
//   projectId,
//   storageBucket,
//   messagingSenderId,
//   appId,
//   measurementId,
// };

const firebaseConfig = {
  apiKey: 'AIzaSyARBu-uV9OVqA_Dr1jc_qBtoLucrinBZwA',
  authDomain: 'advanced-web-auth.firebaseapp.com',
  projectId: 'advanced-web-auth',
  storageBucket: 'advanced-web-auth.appspot.com',
  messagingSenderId: '548001319923',
  appId: '1:548001319923:web:995c24c7806e303db5a086',
  measurementId: 'G-1Y2S4VBD86',
};

// Initialize Firebase
export const app = initializeApp(firebaseConfig);
