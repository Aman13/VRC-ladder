import {createElement, Element} from 'react';
import {connect} from 'react-redux';
<<<<<<< HEAD
import {getMatchGroups, generateMatchGroups} from '../../action/matchgroups';
import {withRouter} from 'react-router';

const displayMatchGroups = (teams, matchGroups) => {

};

const MatchGroup = withRouter(({
  getMatchGroups,
  generateMatchGroups,
  teams,
  matchGroups,
}) : Element => (
  <div>
  <button onClick={() => generateMatchGroups()}>GENERATE</button>
  <button onClick={() => getMatchGroups()}>FETCH</button>
  {displayMatchGroups(teams, matchGroups)}
=======
import {getMatchGroups} from '../../action/matchgroups';
import {withRouter} from 'react-router';

const MatchGroup = withRouter(({
  getMatchGroups,
}) : Element => (
  <div>
  <button onClick={() => getMatchGroups()}>FETCH</button>
>>>>>>> ada1d380d5aa4707c20721c1fadf63aee93648b1
  </div>
));

export default connect(
  (state) => ({
    teams: state.app.teams,
    login: state.app.loggedIn,
<<<<<<< HEAD
    matchGroups: state.app.matchGroups,
  }), {
    getMatchGroups,
    generateMatchGroups}
=======
  }),
  {getMatchGroups}
>>>>>>> ada1d380d5aa4707c20721c1fadf63aee93648b1
)(MatchGroup);
