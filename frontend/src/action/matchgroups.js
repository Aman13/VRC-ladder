<<<<<<< HEAD
import {
  getMatchGroups as getMatchGroupsAPI,
  generateMatchGroups as generateMatchGroupsAPI} from '../api/matchgroups';
=======
import {getMatchGroups as getMatchGroupsAPI} from '../api/matchgroups';
>>>>>>> ada1d380d5aa4707c20721c1fadf63aee93648b1
import {syncMatchGroups} from './types';

export const getMatchGroups = () => (dispatch, getState) => {
  const state = getState();
  return getMatchGroupsAPI(state).then((response) => {
    if (response.error) {
      return Promise.reject();
    }
    console.log(response);
<<<<<<< HEAD
    dispatch(syncMatchGroups(response.matchGroups));
    return Promise.resolve();
  }).catch((error) => {
    return Promise.reject(error);
  });
};

export const generateMatchGroups = () => (dispatch, getState) => {
  const state = getState();
  return generateMatchGroupsAPI(state).then(() => {
=======
    dispatch(syncMatchGroups(response.courts));
>>>>>>> ada1d380d5aa4707c20721c1fadf63aee93648b1
    return Promise.resolve();
  }).catch((error) => {
    return Promise.reject(error);
  });
};
