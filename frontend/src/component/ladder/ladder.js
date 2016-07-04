import {createElement, Element} from 'react';
import {FormattedMessage} from 'react-intl';
import {connect} from 'react-redux';
import map from 'lodash/fp/map';
import sortBy from 'lodash/fp/sortBy';
import styles from './ladder.css';

// import Heading from '../heading/heading';
// const orderPlayers = map((player) => (
//   <div key={player.userId}>
//     {player.firstName} {player.lastName}
//   </div>
// ));

const getTime = (time) => {
  if (time === 'TIME_SLOT_A') {
    return '8:30';
  } else if (time === 'TIME_SLOT_B') {
    return '9:30';
  }
  return 'NONE';
};

const orderTeams = map((team) => (
  <div className={`panel panel-default ${styles.panel}`}>
    <div className={`panel-heading ${styles.panelHeading}`}>
      <span className='pull-left'>Rank {team.ladderPosition}</span>
      <span className='pull-right'>{getTime(team.playTime)}</span>
      <br />
    </div>
    <div className={`panel-body ${styles.panelBody}`}>
      <div className={styles.entry}>
        <FormattedMessage
          id='player1'
          defaultMessage='First Player: '
        />
        {team.firstPlayer.name}
      </div>
      <div className={styles.entry}>
        <FormattedMessage
          id='player2'
          defaultMessage='Second Player: '
        />
        {team.secondPlayer.name}
      </div>
    </div>
  </div>
));

const Ladder = ({
  teams,
}) : Element => (
  <div className={styles.center}>
    {orderTeams(teams)}
  </div>
);

export default connect(
  (state) => ({
    players: sortBy('firstName', state.app.players),
    teams: sortBy('ladderPosition', state.app.teams),
  }),
  {}
)(Ladder);
