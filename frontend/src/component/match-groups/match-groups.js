import {createElement, Element} from 'react';
import {connect} from 'react-redux';
import {getMatchGroups, generateMatchGroups} from '../../action/matchgroups';
import {withRouter} from 'react-router';
import {reduxForm} from 'redux-form';
import {FormattedMessage} from 'react-intl';
import {SubmitBtn} from '../button';

import Heading from '../heading/heading';
import map from 'lodash/fp/map';
import reduce from 'lodash/fp/reduce';
import find from 'lodash/fp/find';
import classNames from 'classnames';
import styles from '../profile/profile.css';

const getTeams = (state) => {
  const matchGroups = state.app.matchGroups;
  const teams = state.app.teams;

  const teamIds = reduce((output, {teamId1, teamId2, teamId3, teamId4}) => {
    return teamId4 ? [
      ...output,
      teamId1,
      teamId2,
      teamId3,
      teamId4,
    ] : [
      ...output,
      teamId1,
      teamId2,
      teamId3,
    ];
  }, [], matchGroups);

  return map(
    (teamId) => find({teamId}, teams),
    teamIds
  );
};

const matchGroupTeams = ({matchGroup, teams}) => {
  const teamIds = matchGroup.teamid4 ? [
    matchGroup.teamId1,
    matchGroup.teamId2,
    matchGroup.teamId3,
    matchGroup.teamId4] : [
      matchGroup.teamId1,
      matchGroup.teamId2,
      matchGroup.teamId3];
  return map(
    (teamId) => find({teamId}, teams),
    teamIds
  );
};

const ResultFormThree = reduxForm({
  form: 'resultFour',
  fields: ['rank1', 'rank2', 'rank3'],
})(({
  fields: {rank1, rank2, rank3},
  matchTeams,
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
        id='rank1'
        defaultMessage='Rank 1:'
      />
    </label>
    <select
      className={classNames(styles.goodForm, {
        [styles.errorForm]: rank1.error &&
                            rank1.touched})}
      {...rank1}
    >
      <option value=''>Select a team...</option>
      {map((teams) => (
        <option value={teams.teamId}key={teams.teamId}>
          {teams.firstPlayer.name} & {teams.secondPlayer.name}
        </option>
      ), matchTeams)}
    </select>
    {rank1.touched && rank1.error &&
      <div className={classNames(styles.errorMsg)}>
      <Heading kind='error'>
            {rank1.error}
          </Heading>
      </div>}
  </div>
  <div className={classNames(styles.formGroup)}>
    <label
      className={classNames(styles.colXsTitle)}
    >
      <FormattedMessage
        id='rank2'
        defaultMessage='Rank 2:'
      />
    </label>
    <select
      className={classNames(styles.goodForm, {
        [styles.errorForm]: rank2.error &&
                            rank2.touched})}
      {...rank2}
    >
      <option value=''>Select a team...</option>
      {map((teams) => (
        <option value={teams.teamId}key={teams.teamId}>
          {teams.firstPlayer.name} & {teams.secondPlayer.name}
        </option>
      ), matchTeams)}
    </select>
    {rank2.touched && rank2.error &&
      <div className={classNames(styles.errorMsg)}>
      <Heading kind='error'>
            {rank2.error}
          </Heading>
      </div>}
  </div>
  <div className={classNames(styles.formGroup)}>
    <label
      className={classNames(styles.colXsTitle)}
    >
      <FormattedMessage
        id='rank3'
        defaultMessage='Rank 3:'
      />
    </label>
    <select
      className={classNames(styles.goodForm, {
        [styles.errorForm]: rank3.error &&
                            rank3.touched})}
      {...rank3}
    >
      <option value=''>Select a team...</option>
      {map((teams) => (
        <option value={teams.teamId}key={teams.teamId}>
          {teams.firstPlayer.name} & {teams.secondPlayer.name}
        </option>
      ), matchTeams)}
    </select>
    {rank3.touched && rank3.error &&
      <div className={classNames(styles.errorMsg)}>
      <Heading kind='error'>
            {rank3.error}
          </Heading>
      </div>}
  </div>
  <div className={classNames(styles.center)}>
    <SubmitBtn type='submit'>Submit Results</SubmitBtn>
  </div>
  </form>
));

const ResultFormFour = reduxForm({
  form: 'resultFour',
  fields: ['rank1', 'rank2', 'rank3', 'rank4'],
})(({
  fields: {rank1, rank2, rank3, rank4},
  matchTeams,
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
        id='rank1'
        defaultMessage='Rank 1:'
      />
    </label>
    <select
      className={classNames(styles.goodForm, {
        [styles.errorForm]: rank1.error &&
                            rank1.touched})}
      {...rank1}
    >
      <option value=''>Select a team...</option>
      {map((teams) => (
        <option value={teams.teamId}key={teams.teamId}>
          {teams.firstPlayer.name} & {teams.secondPlayer.name}
        </option>
      ), matchTeams)}
    </select>
    {rank1.touched && rank1.error &&
      <div className={classNames(styles.errorMsg)}>
      <Heading kind='error'>
            {rank1.error}
          </Heading>
      </div>}
  </div>
  <div className={classNames(styles.formGroup)}>
    <label
      className={classNames(styles.colXsTitle)}
    >
      <FormattedMessage
        id='rank2'
        defaultMessage='Rank 2:'
      />
    </label>
    <select
      className={classNames(styles.goodForm, {
        [styles.errorForm]: rank2.error &&
                            rank2.touched})}
      {...rank2}
    >
      <option value=''>Select a team...</option>
      {map((teams) => (
        <option value={teams.teamId}key={teams.teamId}>
          {teams.firstPlayer.name} & {teams.secondPlayer.name}
        </option>
      ), matchTeams)}
    </select>
    {rank2.touched && rank2.error &&
      <div className={classNames(styles.errorMsg)}>
      <Heading kind='error'>
            {rank2.error}
          </Heading>
      </div>}
  </div>
  <div className={classNames(styles.formGroup)}>
    <label
      className={classNames(styles.colXsTitle)}
    >
      <FormattedMessage
        id='rank3'
        defaultMessage='Rank 3:'
      />
    </label>
    <select
      className={classNames(styles.goodForm, {
        [styles.errorForm]: rank3.error &&
                            rank3.touched})}
      {...rank3}
    >
      <option value=''>Select a team...</option>
      {map((teams) => (
        <option value={teams.teamId}key={teams.teamId}>
          {teams.firstPlayer.name} & {teams.secondPlayer.name}
        </option>
      ), matchTeams)}
    </select>
    {rank3.touched && rank3.error &&
      <div className={classNames(styles.errorMsg)}>
      <Heading kind='error'>
            {rank3.error}
          </Heading>
      </div>}
  </div>
  <div className={classNames(styles.formGroup)}>
    <label
      className={classNames(styles.colXsTitle)}
    >
      <FormattedMessage
        id='rank4'
        defaultMessage='Rank 4:'
      />
    </label>
    <select
      className={classNames(styles.goodForm, {
        [styles.errorForm]: rank4.error &&
                            rank4.touched})}
      {...rank4}
    >
      <option value=''>Select a team...</option>
      {map((teams) => (
        <option value={teams.teamId}key={teams.teamId}>
          {teams.firstPlayer.name} & {teams.secondPlayer.name}
        </option>
      ), matchTeams)}
    </select>
    {rank4.touched && rank4.error &&
      <div className={classNames(styles.errorMsg)}>
      <Heading kind='error'>
            {rank4.error}
          </Heading>
      </div>}
  </div>
  <div className={classNames(styles.center)}>
    <SubmitBtn type='submit'>Submit Results</SubmitBtn>
  </div>
  </form>
));

const MatchGroupForms = ({matchGroup, teams}) => {
  const matchTeams = matchGroupTeams({matchGroup, teams});
  return (
    <div>
      <div>
        {matchTeams.map((team) => (
          <div key={team.teamId}>
            {team.firstPlayer.name} & {team.secondPlayer.name} & {team.teamId}
          </div>
        ))}
      </div>
      {matchGroup.teamId4 ?
        <div>
          <ResultFormFour
            teams={matchTeams}
            onSubmit={(props) => {
            }}
          />
        </div> :
        <div>
          <ResultFormThree
            matchTeams={matchTeams}
            onSubmit={(props) => {
            }}
          />
        </div>}
    </div>
  );
};

const MatchGroups = withRouter(({
  getMatchGroups,
  generateMatchGroups,
  teams,
  matchGroups,
}) : Element => (
  <div className={styles.matchGroupPage}>
    <div>
      <button onClick={() => generateMatchGroups()}>GENERATE</button>
      <button onClick={() => getMatchGroups()}>FETCH</button>
    </div>
    <div>
    {matchGroups.map((matchGroup) => (
      <MatchGroupForms
        key={matchGroup.matchGroupId}
        matchGroup={matchGroup}
        teams={teams}
      />
    ))}
    </div>
  </div>
));

export default connect(
  (state) => ({
    login: state.app.loggedIn,
    matchGroups: state.app.matchGroups,
    teams: getTeams(state),
  }), {
    getMatchGroups,
    generateMatchGroups}
)(MatchGroups);
