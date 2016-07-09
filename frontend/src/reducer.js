import {handleActions} from 'redux-actions';
import {combineReducers} from 'redux';
import {reducer as form} from 'redux-form';

const app = handleActions({

  USER_INFO_SYNC: (state, {payload}) => {
    return {
      ...state,
      userInfo: payload,
    };
  },

  TEAM_INFO_SYNC: (state, {payload}) => {
    return {
      ...state,
      teamInfo: payload,
    };
  },

  USER_LOGIN: (state, {payload}) => {
    return {
      ...state,
      loggedIn: payload,
    };
  },

  PLAYER_SYNC: (state, {payload}) => {
    return {
      ...state,
      players: payload,
    };
  },

  TEAM_SYNC: (state, {payload}) => {
    return {
      ...state,
      teams: payload,
    };
  },

  MATCH_GROUPS_SYNC: (state, {payload}) => {
    return {
      ...state,
      matchGroups: payload,
    };
  },

  PLAYER_ADD: (state, action) => {
    const players = state.players.slice();
    players.push(action.payload);
    return {
      ...state,
      players: players,
    };
  },

  TEAM_ADD: (state, action) => {
    const teams = state.teams.slice();
    teams.push(action.payload);
    return {
      ...state,
      teams: teams,
    };
  },

}, {
  players: [],
  teams: [],
  loggedIn: {
    authorizationToken: '',
    userId: '',
  },
  userInfo: [],
  teamInfo: [],
<<<<<<< HEAD
<<<<<<< HEAD
  matchGroups: [],
=======
>>>>>>> ada1d380d5aa4707c20721c1fadf63aee93648b1
=======
>>>>>>> ada1d380d5aa4707c20721c1fadf63aee93648b1
});

export default combineReducers({
  app,
  form,
});
