import {createAction} from 'redux-actions';

export const syncPlayers = createAction('PLAYER_SYNC');
export const syncTeams = createAction('TEAM_SYNC');
export const syncUserInfo = createAction('USER_INFO_SYNC');
export const syncTeamInfo = createAction('TEAM_INFO_SYNC');
export const syncMatchGroups = createAction('MATCH_GROUPS_SYNC');
