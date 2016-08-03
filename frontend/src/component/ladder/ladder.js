import {createElement, Element} from 'react';
import {Well} from 'react-bootstrap';
import {connect} from 'react-redux';
import {Typeahead} from 'react-typeahead';
import sortBy from 'lodash/fp/sortBy';
import styles from './ladder.css';
const getTime = (time) => {
  if (time === 'TIME_SLOT_A') {
    return 'ATTENDING';
  } else if (time === 'TIME_SLOT_B') {
    return 'ATTENDING';
  }
  return 'N/A';
};

const getGameTimeStyle = (time) => {
  if (time === 'TIME_SLOT_A' || time === 'TIME_SLOT_B') {
    return styles.ladderAttendingGameTime;
  }
  return styles.ladderNotAttendingGameTime;
};

const getLadderRowStyle = (team) => {
  const currentUserId = team.loggedIn;
  if (team.firstPlayer.userId === currentUserId ||
    team.secondPlayer.userId === currentUserId) {
    return styles.currentUserLadderRow;
  }
  return styles.ladderTeamRow;
};

const orderTeams = (team) => {
  return (
    <tr className={getLadderRowStyle(team)}>
      <td className={styles.ladderTeamPlace}>
        <span>{team.ladderPosition}</span>
      </td>
      <td className={styles.ladderTeamPlayer}>
        <span>{team.firstPlayer.name}</span>
      </td>
      <td className={styles.ladderTeamPlayer}>
        <span>{team.secondPlayer.name}</span>
      </td>
      <td className={getGameTimeStyle(team.playTime)}>
        <span>{getTime(team.playTime)}</span>
      </td>
    </tr>);
};

const test = (teams, loggedIn) => {
  teams.map((option) => {
    option.bothPlayers =
    `${option.firstPlayer.name} ${option.secondPlayer.name} default`;
    option.loggedIn = loggedIn.userId;
  });
  return teams;
};

const testDisplay = ({options, displayOption}) => {
  const sortedTeams = sortBy('ladderPosition', options);
  return (
    <table className={styles.ladderTable}>
      <thead>
        <tr className={styles.ladderTableHeading}>
          <th className={styles.ladderTableHeadingCol}>
            <h3 className={styles.columnHeadline}>#</h3>
          </th>
          <th className={styles.ladderTableHeadingCol}>
            <h3 className={styles.columnHeadline}>First Player</h3>
          </th>
          <th className={styles.ladderTableHeadingCol}>
            <h3 className={styles.columnHeadline}>Second Player</h3>
          </th>
          <th className={styles.ladderTableHeadingCol}>
            <h3 className={styles.columnHeadline}>Game Time</h3>
          </th>
        </tr>
      </thead>
      <tbody className={styles.ladderTableBody}>
        {sortedTeams.map(displayOption)}
      </tbody>
    </table>
  );
};

const Ladder = ({
  teams,
  loggedIn,
}) : Element => {
  return (<Well className={`${styles.ladderTableContainer} table-responsive`}>
  <Typeahead
    options={test(teams, loggedIn)}
    filterOption='bothPlayers'
    showOptionsWhenEmpty
    displayOption={orderTeams}
    customListComponent={testDisplay}
  />
  </Well>);
};

export default connect(
  (state) => ({
    players: sortBy('firstName', state.app.players),
    teams: sortBy('ladderPosition', state.app.teams),
    loggedIn: state.app.loggedIn,
  }),
  {}
)(Ladder);
