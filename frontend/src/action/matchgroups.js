import {
  getMatchGroups as getMatchGroupsAPI,
  generateMatchGroups as generateMatchGroupsAPI,
  reportMatchResults as reportMatchResultsAPI,
  regenerateMatchGroups as regenerateMatchGroupsAPI} from '../api/matchgroups';
import {syncMatchGroups} from './types';

export const getMatchGroups = () => (dispatch, getState) => {
  const state = getState();
  return getMatchGroupsAPI(state).then((response) => {
    if (response.error) {
      return Promise.reject();
    }
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

export const regenerateMatchGroups = () => (dispatch, getState) => {
  const state = getState();
  return regenerateMatchGroupsAPI(state).then(() => {
    return Promise.resolve();
  }).catch((error) => {
    return Promise.reject(error);
  });
};

export const reportMatchResults = (props) => (dispatch, getState) => {
  const state = getState();
  return reportMatchResultsAPI(props, state);
};
