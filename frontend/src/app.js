/* global localStorage */
/* global fetch */

import {createElement, Element} from 'react';
import {connect} from 'react-redux';
import {createAction} from 'redux-actions';

import SignUp from './component/signup/signup';
import CreateTeam from './component/create-team/create-team';
import Ladder from './component/ladder/ladder';
import styles from './app.css';

const restoreState = createAction('STATE_RESTORE');
const saveState = createAction('STATE_SAVE');
const syncPlayers = createAction('PLAYER_SYNC');
const root = 'http://localhost:6789/';

const restoreFromBrowser = () => (dispatch) => {
  const state = JSON.parse(localStorage.getItem('yolo'));
  dispatch(restoreState(state));
};

const saveToBrowser = () => (dispatch, getState) => {
  const state = getState().app;
  localStorage.setItem('yolo', JSON.stringify(state));
  dispatch(saveState());
};

const addPlayerOnServer = () => (dispatch) => {
  fetch(`${root}players/new`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      // 'Authorization': 'xxxx',
    },
    body: JSON.stringify({username: 'aman', password: 'balls'}),
  }).then((response) => {
    return response.json();
  }).then((users) => {
    dispatch(syncPlayers(users));
  });
};

const fetchPlayersFromServer = () => (dispatch) => {
  fetch(`${root}players`).then((response) => {
    return response.json();
  }).then((users) => {
    dispatch(syncPlayers(users));
  });
};

const App = ({
  restoreFromBrowser,
  saveToBrowser,
  fetchPlayersFromServer,
  addPlayerOnServer,
}) : Element => (
  <div className={styles.app}>
    <button onClick={fetchPlayersFromServer}>FETCH</button>
    <button onClick={addPlayerOnServer}>ADD</button>
    <button onClick={saveToBrowser}>SAVE</button>
    <button onClick={restoreFromBrowser}>RESTORE</button>
    <SignUp/>
    <CreateTeam/>
    <Ladder/>
  </div>
);

export default connect(
  (state) => ({
    players: state.app.players,
    teams: state.app.teams,
  }), {
    restoreFromBrowser,
    saveToBrowser,
    fetchPlayersFromServer,
    addPlayerOnServer,
  }
)(App);
