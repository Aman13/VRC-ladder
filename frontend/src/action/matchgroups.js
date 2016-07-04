import {getMatchGroups as getMatchGroupsAPI} from '../api/matchgroups';
import {syncMatchGroups} from './types';

export const getMatchGroups = () => (dispatch, getState) => {
  const state = getState();
  return getMatchGroupsAPI(state).then((response) => {
    if (response.error) {
      return Promise.reject();
    }
    console.log(response);
    dispatch(syncMatchGroups(response.courts));
    return Promise.resolve();
  }).catch((error) => {
    return Promise.reject(error);
  });
};
