import {createElement, Element} from 'react';
import {connect} from 'react-redux';
import {Well} from 'react-bootstrap';
import {createAction} from 'redux-actions';

import styles from './admin.css';
import LadderOverride from './ladder/ladder-override';
import PlayerOverride from './players/player-override';
import TeamOverride from './teams/team-override';
import {requestPDF} from '../../action/ladder';

const updateView = createAction('ADMIN_VIEW');

const ToggleView = ({adminView}) => {
  if (adminView === 'ladder') {
    return <div><LadderOverride/></div>;
  } else if (adminView === 'player') {
    return <div><PlayerOverride/></div>;
  } else if (adminView === 'team') {
    return <div><TeamOverride/></div>;
  }
  return <div></div>;
};

const ViewSwitch = (props) => (dispatch) => {
  dispatch(updateView(props));
};

const Admin = ({
  adminView,
  ViewSwitch,
  requestPDF,
}) : Element => (
  <Well className={`${styles.ladderTableContainer} table-responsive`}>
    <div>
    <button onClick={() => ViewSwitch('ladder')}>LadderOverride</button>
    <button onClick={() => ViewSwitch('player')}>PlayerOverride</button>
    <button onClick={() => ViewSwitch('team')}>TeamOverride</button>
    <button
      onClick={() => requestPDF()}
    >Request PDF Email</button>
    </div>
    <div>
        <ToggleView
          adminView={adminView}
        />
    </div>

  </Well>
);

export default connect(
  (state) => ({
    adminView: state.app.adminView,
  }), {
    ViewSwitch,
    requestPDF,
  }
)(Admin);
