import {createElement, Element} from 'react';
import {FormattedMessage} from 'react-intl';
import {connect} from 'react-redux';
import {reduxForm} from 'redux-form';
import {addTeam, updateTeamStatus} from '../../action/teams';
import {SubmitBtn} from '../button';
import {withRouter} from 'react-router';
import {getTeamInfo} from '../../action/users';
import map from 'lodash/fp/map';
import styles from './profile.css';
import Heading from '../heading/heading';
import classNames from 'classnames';
import isEmpty from 'lodash/fp/isEmpty';
import sortBy from 'lodash/fp/sortBy';
const validate = (values, userInfo) => {
  const errors = {};
  if (!values.secondPlayerId) {
    errors.secondPlayerId = 'Required';
  }
  if (userInfo.userId === values.secondPlayerId) {
    errors.secondPlayerId = 'Cannot be same person';
  }
  return errors;
};
const validateUpdateStatus = (values) => {
  const errors = {};
  if (!values.playTime) {
    errors.playTime = 'Required';
  }
  if (!values.teamId) {
    errors.teamId = 'Required';
  }
  return errors;
};
const playTimes = [{
  time: '8:00 pm',
  value: 'TIME_SLOT_A',
}, {
  time: '9:30 pm',
  value: 'TIME_SLOT_B',
}, {
  time: 'Not Playing',
  value: 'NONE',
}];
const UpdateAttendanceForm = reduxForm({
  form: 'updateTeam',
  fields: ['teamId', 'playTime'],
})(({
  fields: {teamId, playTime},
  teams,
  handleSubmit,
}) => (
  <form
    className={styles.formHorizontal}
    onSubmit={handleSubmit}
  >
  <div className={classNames(styles.formGroup)}>
    <label
      className={classNames(styles.colXsTitle)}
    >
      <FormattedMessage
        id='selectTeam'
        defaultMessage='SelectTeam'
      />
    </label>
    <select
      className={classNames(styles.goodForm, {
        [styles.errorForm]: teamId.error &&
                            teamId.touched})}
      {...teamId}
    >
      <option value=''>Select a team...</option>
      {map((teams) => (
        <option value={teams.teamId}key={teams.teamId}>
          {teams.firstPlayer.name} {teams.secondPlayer.name}
        }
        </option>
      ), teams)}
    </select>
    {teamId.touched && teamId.error &&
      <div className={classNames(styles.errorMsg)}>
      <Heading kind='error'>
            {teamId.error}
          </Heading>
      </div>}
  </div>
  <div className={classNames(styles.formGroup)}>
    <label
      className={classNames(styles.colXsTitle)}
    >
      <FormattedMessage
        id='Select a Time'
        defaultMessage='Time'
      />
    </label>
    <select
      className={classNames(styles.goodForm, {
        [styles.errorForm]: playTime.error &&
                            playTime.touched})}
      {...playTime}
    >
      <option value=''>Select a Time...</option>
      {map((playTimes) => (
        <option value={playTimes.value}key={playTimes.value}>
          {playTimes.time}
        </option>
      ), playTimes)}
    </select>
    {playTime.touched && playTime.error &&
      <div className={classNames(styles.errorMsg)}>
      <Heading kind='error'>
            {playTime.error}
          </Heading>
      </div>}
  </div>
    <div className={classNames(styles.center)}>
      <SubmitBtn type='submit'>Update Attendance</SubmitBtn>
    </div>
  </form>
));
const CreateTeamForm = reduxForm({
  form: 'teamCreate',
  fields: ['secondPlayerId'],
  validate,
})(({
  fields: {secondPlayerId},
  players,
  handleSubmit,
}) => (
  <form
    className={styles.formHorizontal}
    onSubmit={handleSubmit}
  >
    <div className={classNames(styles.formGroup)}>
      <label
        className={classNames(styles.colXsTitle)}
      >
        <FormattedMessage
          id='secondPlayerId'
          defaultMessage='Select Team Members Name'
        />
      </label>
      <select
        className={classNames(styles.goodForm, {
          [styles.errorForm]: secondPlayerId.error &&
                              secondPlayerId.touched})}
        {...secondPlayerId}
      >
        <option value=''>Select a player...</option>
        {map((player) => (
          <option value={player.userId}key={player.userId}>
            {player.firstName} {player.userId}
          </option>
        ), players)}
      </select>
      {secondPlayerId.touched && secondPlayerId.error &&
        <div className={classNames(styles.errorMsg)}>
          <Heading kind='error'>
            {secondPlayerId.error}
          </Heading>
        </div>}
    </div>
    <div className={classNames(styles.center)}>
      <SubmitBtn type='submit'>Create Team</SubmitBtn>
    </div>
  </form>
));
const displayMyInfo = (userInfo) => (
  <div>
    <div className={styles.profileDetailsField}>
      <FormattedMessage
        id='firstName'
        defaultMessage='Name: '
      />
    </div>
    <div className={styles.profileDetails}>
      {userInfo.firstName} {userInfo.lastName}
    </div>
    <br/>
    <div className={styles.profileDetailsField}>
      <FormattedMessage
        id='phoneNumber'
        defaultMessage='Phone Number: '
      />
    </div>
    <div className={styles.profileDetails}>
      {userInfo.phoneNumber}
    </div>
    <br/>
    <div className={styles.profileDetailsField}>
      <FormattedMessage
        id='emailAddress'
        defaultMessage='Email: '
      />
    </div>
    <div className={styles.profileDetails}>
      {userInfo.emailAddress}
    </div>
  </div>
);
const getTime = (time) => {
  if (time === 'TIME_SLOT_A') {
    return '8:30';
  } else if (time === 'TIME_SLOT_B') {
    return '9:30';
  }
  return 'NONE';
};
const displayTeamInfo = map((teamInfo) => (
  <div
    key={teamInfo.teamId}
    className={styles.table}
  >
    <div className={styles.profileDetailsField}>
      <FormattedMessage
        id='firstPlayer'
        defaultMessage='Teamed up With: '
      />
    </div>
    <div className={styles.profileDetails}>
      {teamInfo.secondPlayer.name}
    </div>
    <br/>
    <div className={styles.profileDetailsField}>
      <FormattedMessage
        id='playTime'
        defaultMessage='Preferred PlayTime: '
      />
    </div>
    <div className={styles.profileDetails}>
      {getTime(teamInfo.playTime)}
    </div>
  </div>
));
const CreateTeam = withRouter(({
  addTeam,
  players,
  teams,
  router,
  login,
  userInfo,
  teamInfo,
  getTeamInfo,
  updateTeamStatus,
}) : Element => (
  <div className={styles.createTeam}>
      <div className={styles.sectionHeaders}>
        <FormattedMessage
          id='myInfo'
          defaultMessage='My Profile'
        />
      </div>
    {displayMyInfo(userInfo)}
    <div className={styles.sectionHeaders}>
      <FormattedMessage
        id='myTeams'
        defaultMessage='My Teams'
      />
    </div>
    {displayTeamInfo(teamInfo)}
    <div className={styles.sectionHeaders}>
      <FormattedMessage
        id='teamAttendance'
        defaultMessage='Update Attendance'
      />
    </div>
    <UpdateAttendanceForm
      teams={teamInfo}
      userInfo={userInfo}
      onSubmit={(props) => {
        const errors = validateUpdateStatus(props);
        if (!isEmpty(errors)) {
          return Promise.reject(errors);
        }
        return updateTeamStatus({
          ...props,
          authorizationToken: login.authorizationToken,
        }).then(() => {
          getTeamInfo().then(() => {
            router.push('/ladder');
          });
        });
      }}
    />
  <div className={styles.sectionHeaders}>
    <FormattedMessage
      id='createTeam'
      defaultMessage='Create Team'
    />
   </div>
    <CreateTeamForm
      players={players}
      teams={teams}
      userInfo={userInfo}
      onSubmit={(props) => {
        const errors = validate(props, userInfo);
        if (!isEmpty(errors)) {
          return Promise.reject(errors);
        }
        return addTeam({
          ...props,
          firstPlayerId: userInfo.userId,
        }, login).then(() => {
          getTeamInfo().then(() => {
            router.push('/ladder');
          });
        }).catch((errors) => {
          // errors = {'secondPlayerId': 'team exists'}
          return Promise.reject(errors);
        });
      }}
    />
  </div>
));
export default connect(
  (state) => ({
    players: sortBy('firstName', state.app.players),
    teams: state.app.teams,
    login: state.app.loggedIn,
    userInfo: state.app.userInfo,
    teamInfo: state.app.teamInfo,
  }), {
    addTeam,
    displayMyInfo,
    getTeamInfo,
    updateTeamStatus}
)(CreateTeam);
