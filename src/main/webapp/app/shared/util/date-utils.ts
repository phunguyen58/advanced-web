import dayjs from 'dayjs';

import { APP_LOCAL_DATETIME_FORMAT } from 'app/config/constants';

export const convertDateTimeFromServer = date => (date ? dayjs(date).format(APP_LOCAL_DATETIME_FORMAT) : null);

export const convertDateTimeToServer = date => (date ? dayjs(date).toDate() : null);

export const displayDefaultDateTime = () => dayjs().startOf('day').format(APP_LOCAL_DATETIME_FORMAT);

export const displayDefaultDateTimeCreateClass = () => dayjs().add(14, 'day').startOf('day').format(APP_LOCAL_DATETIME_FORMAT);

export const defaultDateTime = () => dayjs(dayjs().startOf('day').format(APP_LOCAL_DATETIME_FORMAT)).toDate();
