import {createElement} from 'react';
import {Form, Panel} from 'react-bootstrap';
import {reduxForm} from 'redux-form';
import styles from './result-form.css';
import classNames from 'classnames';
import {FormattedMessage} from 'react-intl';
import {SubmitBtn} from '../button';
import Heading from '../heading/heading';
import map from 'lodash/fp/map';
import isEmpty from 'lodash/fp/isEmpty';

// TODO: make this work for 4 team matchgroup forms
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

const mapInitialStateToProps = (state) => {
  return {
    initialValues: state.app.matchResults,
  };
};

const generateRankingSubmissionRow = (teams, teamId, rankNumber) => (
  <div className={classNames(styles.formGroup)}>
    <label className={classNames(styles.colXsTitle)}>
      <FormattedMessage
        id={`teamId ${rankNumber}`}
        defaultMessage={`TeamId ${rankNumber}:`}
      />
    </label>
    <select
      className={classNames(styles.goodForm, {
        [styles.errorForm]: teamId.error &&
                            teamId.touched})}
      {...teamId}
    >
      <option value=''>Select a team...</option>
      {map((team) => (
        <option value={team.teamId}key={team.teamId}>
          {team.firstPlayer.name} & {team.secondPlayer.name}
        </option>
      ), teams)}
    </select>
    {teamId.touched && teamId.error &&
      <div className={classNames(styles.errorMsg)}>
      <Heading kind='error'>
            {teamId.error}
          </Heading>
      </div>}
  </div>);

const ResultFormRows = reduxForm({
  fields: ['teamId1', 'teamId2', 'teamId3', 'teamId4'],
}, mapInitialStateToProps)(({
  fields: {teamId1, teamId2, teamId3, teamId4},
  matchTeams,
  handleSubmit,
}) => (
  <Form horizontal onSubmit={handleSubmit}>
    {generateRankingSubmissionRow(matchTeams, teamId1, 1)}
    {generateRankingSubmissionRow(matchTeams, teamId2, 2)}
    {generateRankingSubmissionRow(matchTeams, teamId3, 3)}
    {matchTeams.length === 4 ?
      generateRankingSubmissionRow(matchTeams, teamId4, 4) :
      null}
    <div className={classNames(styles.center)}>
      <SubmitBtn type='submit'>Submit Results</SubmitBtn>
    </div>
  </Form>
));

export const ResultForm = (
  formName,
  matchGroup,
  matchGroupTeams,
  reportMatchResults) => {
  return (
    <Panel header='Result Submission' bsStyle='primary'>
      <ResultFormRows
        form={formName}
        matchTeams={matchGroupTeams}
        onSubmit={
          (props) => {
            const errors = validate(props);
            if (!isEmpty(errors)) {
              return Promise.reject(errors);
            }
            return reportMatchResults({
              results: props,
              matchGroupId: matchGroup.matchGroupId,
            }).then(() => {
              window.alert('Results Submitted Successfully!');
              return Promise.resolve();
            }).catch((errors) => {
              window.alert('There was an error submitting results');
              return Promise.reject(errors);
            });
          }
        }
      />
    </Panel>
  );
};
