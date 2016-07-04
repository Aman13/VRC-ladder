import {createElement, Element} from 'react';
import {connect} from 'react-redux';
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
  </div>
));

export default connect(
  (state) => ({
    teams: state.app.teams,
    login: state.app.loggedIn,
    matchGroups: state.app.matchGroups,
  }), {
    getMatchGroups,
    generateMatchGroups}
)(MatchGroup);
