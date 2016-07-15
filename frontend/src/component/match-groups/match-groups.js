import {createElement, Element} from 'react';
import {connect} from 'react-redux';
import {
  getMatchGroups,
  generateMatchGroups,
  reportMatchResults,
  regenerateMatchGroups} from '../../action/matchgroups';
import {withRouter} from 'react-router';
import {reduxForm} from 'redux-form';
import {FormattedMessage} from 'react-intl';
import {SubmitBtn} from '../button';
import {Form, Panel, Well} from 'react-bootstrap';

import Heading from '../heading/heading';
import map from 'lodash/fp/map';
import isEmpty from 'lodash/fp/isEmpty';
import reduce from 'lodash/fp/reduce';
import find from 'lodash/fp/find';
import classNames from 'classnames';
import styles from './match-groups.css';

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

const validate = (values) => {
  const errors = {};
  if (values.teamId1 === values.teamId2) {
    errors.teamId1 = 'Cannot be same team';
    errors.teamId2 = 'Cannot be same team';
  } else if (values.teamId1 === values.teamId3) {
    errors.teamId1 = 'Cannot be same team';
    errors.teamId3 = 'Cannot be same team';
  } else if (values.teamId2 === values.teamId3) {
    errors.teamId2 = 'Cannot be same team';
    errors.teamId3 = 'Cannot be same team';
  }
  return errors;
};

const matchGroupTeams = ({matchGroup, teams}) => {
  const teamIds = matchGroup.teamId4 ? [
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
  fields: ['teamId1', 'teamId2', 'teamId3'],
  validate,
})(({
  fields: {teamId1, teamId2, teamId3},
  matchTeams,
  handleSubmit,
}) => (
  <Form horizontal onSubmit={handleSubmit}>
  <div className={classNames(styles.formGroup)}>
    <label
      className={classNames(styles.colXsTitle)}
    >
      <FormattedMessage
        id='teamId1'
        defaultMessage='Rank 1:'
      />
    </label>
    <select
      className={classNames(styles.goodForm, {
        [styles.errorForm]: teamId1.error &&
                            teamId1.touched})}
      {...teamId1}
    >
      <option value=''>Select a team...</option>
      {map((teams) => (
        <option value={teams.teamId}key={teams.teamId}>
          {teams.firstPlayer.name} & {teams.secondPlayer.name}
        </option>
      ), matchTeams)}
    </select>
    {teamId1.touched && teamId1.error &&
      <div className={classNames(styles.errorMsg)}>
      <Heading kind='error'>
            {teamId1.error}
          </Heading>
      </div>}
  </div>
  <div className={classNames(styles.formGroup)}>
    <label
      className={classNames(styles.colXsTitle)}
    >
      <FormattedMessage
        id='teamId2'
        defaultMessage='Rank 2:'
      />
    </label>
    <select
      className={classNames(styles.goodForm, {
        [styles.errorForm]: teamId2.error &&
                            teamId2.touched})}
      {...teamId2}
    >
      <option value=''>Select a team...</option>
      {map((teams) => (
        <option value={teams.teamId}key={teams.teamId}>
          {teams.firstPlayer.name} & {teams.secondPlayer.name}
        </option>
      ), matchTeams)}
    </select>
    {teamId2.touched && teamId2.error &&
      <div className={classNames(styles.errorMsg)}>
      <Heading kind='error'>
            {teamId2.error}
          </Heading>
      </div>}
  </div>
  <div className={classNames(styles.formGroup)}>
    <label
      className={classNames(styles.colXsTitle)}
    >
      <FormattedMessage
        id='teamId3'
        defaultMessage='Rank 3:'
      />
    </label>
    <select
      className={classNames(styles.goodForm, {
        [styles.errorForm]: teamId3.error &&
                            teamId3.touched})}
      {...teamId3}
    >
      <option value=''>Select a team...</option>
      {map((teams) => (
        <option value={teams.teamId}key={teams.teamId}>
          {teams.firstPlayer.name} & {teams.secondPlayer.name}
        </option>
      ), matchTeams)}
    </select>
    {teamId3.touched && teamId3.error &&
      <div className={classNames(styles.errorMsg)}>
      <Heading kind='error'>
            {teamId3.error}
          </Heading>
      </div>}
  </div>
  <div className={classNames(styles.center)}>
    <SubmitBtn type='submit'>Submit Results</SubmitBtn>
  </div>
  </Form>
));

const ResultFormFour = reduxForm({
  form: 'resultFour',
  fields: ['teamId1', 'teamId2', 'teamId3', 'teamId4'],
})(({
  fields: {teamId1, teamId2, teamId3, teamId4},
  matchTeams,
  handleSubmit,
}) => (
  <Form horizontal onSubmit={handleSubmit}>
  <div className={classNames(styles.formGroup)}>
    <label
      className={classNames(styles.colXsTitle)}
    >
      <FormattedMessage
        id='teamId1'
        defaultMessage='Rank 1:'
      />
    </label>
    <select
      className={classNames(styles.goodForm, {
        [styles.errorForm]: teamId1.error &&
                            teamId1.touched})}
      {...teamId1}
    >
      <option value=''>Select a team...</option>
      {map((teams) => (
        <option value={teams.teamId}key={teams.teamId}>
          {teams.firstPlayer.name} & {teams.secondPlayer.name}
        </option>
      ), matchTeams)}
    </select>
    {teamId1.touched && teamId1.error &&
      <div className={classNames(styles.errorMsg)}>
      <Heading kind='error'>
            {teamId1.error}
          </Heading>
      </div>}
  </div>
  <div className={classNames(styles.formGroup)}>
    <label
      className={classNames(styles.colXsTitle)}
    >
      <FormattedMessage
        id='teamId2'
        defaultMessage='Rank 2:'
      />
    </label>
    <select
      className={classNames(styles.goodForm, {
        [styles.errorForm]: teamId2.error &&
                            teamId2.touched})}
      {...teamId2}
    >
      <option value=''>Select a team...</option>
      {map((teams) => (
        <option value={teams.teamId}key={teams.teamId}>
          {teams.firstPlayer.name} & {teams.secondPlayer.name}
        </option>
      ), matchTeams)}
    </select>
    {teamId2.touched && teamId2.error &&
      <div className={classNames(styles.errorMsg)}>
      <Heading kind='error'>
            {teamId2.error}
          </Heading>
      </div>}
  </div>
  <div className={classNames(styles.formGroup)}>
    <label
      className={classNames(styles.colXsTitle)}
    >
      <FormattedMessage
        id='teamId3'
        defaultMessage='Rank 3:'
      />
    </label>
    <select
      className={classNames(styles.goodForm, {
        [styles.errorForm]: teamId3.error &&
                            teamId3.touched})}
      {...teamId3}
    >
      <option value=''>Select a team...</option>
      {map((teams) => (
        <option value={teams.teamId}key={teams.teamId}>
          {teams.firstPlayer.name} & {teams.secondPlayer.name}
        </option>
      ), matchTeams)}
    </select>
    {teamId3.touched && teamId3.error &&
      <div className={classNames(styles.errorMsg)}>
      <Heading kind='error'>
            {teamId3.error}
          </Heading>
      </div>}
  </div>
  <div className={classNames(styles.formGroup)}>
    <label
      className={classNames(styles.colXsTitle)}
    >
      <FormattedMessage
        id='teamId4'
        defaultMessage='Rank 4:'
      />
    </label>
    <select
      className={classNames(styles.goodForm, {
        [styles.errorForm]: teamId4.error &&
                            teamId4.touched})}
      {...teamId4}
    >
      <option value=''>Select a team...</option>
      {map((teams) => (
        <option value={teams.teamId}key={teams.teamId}>
          {teams.firstPlayer.name} & {teams.secondPlayer.name}
        </option>
      ), matchTeams)}
    </select>
    {teamId4.touched && teamId4.error &&
      <div className={classNames(styles.errorMsg)}>
      <Heading kind='error'>
            {teamId4.error}
          </Heading>
      </div>}
  </div>
  <div className={classNames(styles.center)}>
    <SubmitBtn type='submit'>Submit Results</SubmitBtn>
  </div>
  </Form>
));

const MatchGroupForms = ({matchGroup, teams, reportMatchResults}) => {
  const matchTeams = matchGroupTeams({matchGroup, teams});
  return (
    <Well>
      <Panel header={`Court ID: ${matchGroup.matchGroupId}`} bsStyle='primary'>
        <div className={classNames(styles.teamNames)}>
          {matchTeams.map((team) => (
            <div key={team.teamId}>
              {team.firstPlayer.name} & {team.secondPlayer.name} & {team.teamId}
            </div>
          ))}
        </div>
      </Panel>
      <Panel header='Result Submission' bsStyle='primary'>
      {matchGroup.teamId4 ?
          <ResultFormFour
            matchTeams={matchTeams}
            onSubmit={(props) => {
              const errors = validate(props);
              if (!isEmpty(errors)) {
                return Promise.reject(errors);
              }
              return reportMatchResults({
                results: props,
                matchGroupId: matchGroup.matchGroupId}).then(() => {
                  return Promise.resolve();
                }).catch((errors) => {
                  return Promise.reject(errors);
                });
            }}
          />  :
          <ResultFormThree
            matchTeams={matchTeams}
            onSubmit={(props) => {
              const errors = validate(props);
              if (!isEmpty(errors)) {
                return Promise.reject(errors);
              }
              return reportMatchResults({
                results: props,
                matchGroupId: matchGroup.matchGroupId}).then(() => {
                  return Promise.resolve();
                }).catch((errors) => {
                  return Promise.reject(errors);
                });
            }}
          />}
        </Panel>
    </Well>
  );
};

const MatchGroups = withRouter(({
  getMatchGroups,
  generateMatchGroups,
  teams,
  matchGroups,
  regenerateMatchGroups,
  reportMatchResults,
}) : Element => (
  <div className={styles.matchGroupPage}>
    <div>
      <button onClick={() => generateMatchGroups()}>GENERATE</button>
      <button onClick={() => regenerateMatchGroups()}>REGENERATE</button>
      <button onClick={() => getMatchGroups()}>FETCH</button>
    </div>
    <div>
    {matchGroups.map((matchGroup) => (
      <MatchGroupForms
        key={matchGroup.matchGroupId}
        matchGroup={matchGroup}
        reportMatchResults={reportMatchResults}
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
    generateMatchGroups,
    reportMatchResults,
    regenerateMatchGroups,
  }
)(MatchGroups);
