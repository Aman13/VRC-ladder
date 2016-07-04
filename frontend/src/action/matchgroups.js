import {
  getMatchGroups as getMatchGroupsAPI,
  generateMatchGroups as generateMatchGroupsAPI} from '../api/matchgroups';
import {syncMatchGroups} from './types';

export const getMatchGroups = () => (dispatch, getState) => {
  const state = getState();
  return getMatchGroupsAPI(state).then((response) => {
    if (response.error) {
      return Promise.reject();
    }
    console.log(response);
    dispatch(syncMatchGroups(response.matchGroups));
    return Promise.resolve();
  }).catch((error) => {
    return Promise.reject(error);
  });
};

export const generateMatchGroups = () => (dispatch, getState) => {
  const state = getState();
  return generateMatchGroupsAPI(state).then(() => {
    return Promise.resolve();
  }).catch((error) => {
    return Promise.reject(error);
  });
};
