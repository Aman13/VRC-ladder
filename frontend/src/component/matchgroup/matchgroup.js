import {createElement, Element} from 'react';
import {connect} from 'react-redux';
import {getMatchGroups} from '../../action/matchgroups';
import {withRouter} from 'react-router';

const MatchGroup = withRouter(({
  getMatchGroups,
}) : Element => (
  <div>
  <button onClick={() => getMatchGroups()}>FETCH</button>
  </div>
));

export default connect(
  (state) => ({
    teams: state.app.teams,
    login: state.app.loggedIn,
  }),
  {getMatchGroups}
)(MatchGroup);
