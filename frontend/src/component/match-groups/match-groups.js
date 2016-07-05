import {createElement, Element} from 'react';
import {FormattedMessage} from 'react-intl';
import {connect} from 'react-redux';
// import map from 'lodash/fp/map';
import styles from './match-groups.css';
import {SubmitBtn} from '../button';
import {getMatchGroups} from '../../action/matchgroups';
import {withRouter} from 'react-router';

const MatchGroups = withRouter(({
  getMatchGroups,
}) : Element => (
  <div className={styles.matchGroupPage}>
    <div>
    <button onClick={() => getMatchGroups()}>FETCH</button>
    </div>
    <div className={styles.matchGroupTitle}>
      <FormattedMessage
        id='title'
        defaultMessage='This weeks match groups'
      />
    </div>
    <div>
      <br/>
      <div className={styles.matchGroups}>
        <div className={styles.matchGroupCourt}>
          <FormattedMessage
            id='court #1'
            defaultMessage='Court #1'
          />
          <br/>
          <div className={styles.matchGroupCourtTeams}>
            <div>
              <FormattedMessage
                id='team1'
                defaultMessage='Team 1: Karan, Aman'
              />
            </div>
            <div>
              <FormattedMessage
                id='team2'
                defaultMessage='Team 2: Trevor, David'
              />
            </div>
          </div>
          <div className={styles.matchGroupCourtTeams}>
            <div>
              <FormattedMessage
                id='team3'
                defaultMessage='Team 3: Karan, Aman '
              />
            </div>
          </div>
        </div>
          <div className={styles.matchGroupInfo}>
            <FormattedMessage
              id='match1'
              defaultMessage='Team 1 vs Team 2 @ 8:30pm'
            />
            <div className={styles.matchGroupResultsFORM}>
              <FormattedMessage
                id='matchResults3'
                defaultMessage='Results: '
              />
            <select>
              <option value=''>Select a team...</option>
                <option value='Team1'>Team1</option>
                <option value='Team2'>Team2</option>
            </select>
              </div>
            </div>
          <div className={styles.matchGroupInfo}>
            <FormattedMessage
              id='match2'
              defaultMessage='Team 1 vs Team 3 @ 8:45pm'
            />
              <div className={styles.matchGroupResultsFORM}>
              <FormattedMessage
                id='matchResults3'
                defaultMessage='Results: '
              />
              <select>
                <option value=''>Select a team...</option>
                  <option value='Team1'>Team1</option>
                  <option value='Team3'>Team3</option>
              </select>
              </div>
            </div>
            <div className={styles.matchGroupInfo}>
            <FormattedMessage
              id='matchResults3'
              defaultMessage='Team 2 vs Team 3 @9:00pm'
            />
              <div className={styles.matchGroupResultsFORM}>
              <FormattedMessage
                id='matchResults3'
                defaultMessage='Results: '
              />
              <select>
                <option value=''>Select a team...</option>
                  <option value='Team2'>Team2</option>
                  <option value='Team3'>Team3</option>
              </select>
              </div>
            </div>
            <SubmitBtn type='submit'>Report Match Results</SubmitBtn>
      </div>
      <div className={styles.matchGroups}>
        <div className={styles.matchGroupCourt}>
          <FormattedMessage
            id='matchGroup2'
            defaultMessage='Court #2'
          />
          <br/>
          <div className={styles.matchGroupCourtTeams}>
            <div>
              <FormattedMessage
                id='team1'
                defaultMessage='Team 4: Marcus, Marie'
              />
            </div>
            <div>
              <FormattedMessage
                id='team2'
                defaultMessage='Team 5: Will, Danica'
              />
            </div>
          </div>
          <div className={styles.matchGroupCourtTeams}>
            <div>
              <FormattedMessage
                id='team3'
                defaultMessage='Team 6: Margerie, Nichols '
              />
            </div>
          </div>
        </div>
          <div>
            <div className={styles.matchGroupInfo}>
            <FormattedMessage
              id='match1'
              defaultMessage='Team 4 vs Team 5 @8:30pm'
            />
            <div className={styles.matchGroupResultsFORM}>
              <FormattedMessage
                id='matchResults3'
                defaultMessage='Results: '
              />
              <select>
                <option value=''>Select a team...</option>
                  <option value='Team4'>Team4</option>
                  <option value='Team5'>Team5</option>
              </select>
              </div>
            </div>
            <div className={styles.matchGroupInfo}>
            <FormattedMessage
              id='match2'
              defaultMessage='Team 4 vs Team 6 @8:30pm'
            />
              <div className={styles.matchGroupResultsFORM}>
              <FormattedMessage
                id='matchResults3'
                defaultMessage='Results: '
              />
              <select>
                <option value=''>Select a team...</option>
                  <option value='Team4'>Team4</option>
                  <option value='Team6'>Team6</option>
              </select>
              </div>
            </div>
            <div className={styles.matchGroupInfo}>
              <FormattedMessage
                id='match3'
                defaultMessage='Team 5 vs Team 6 @8:30pm'
              />
              <div className={styles.matchGroupResultsFORM}>
              <FormattedMessage
                id='matchResults3'
                defaultMessage='Results: '
              />
              <select>
                <option value=''>Select a team...</option>
                  <option value='Team4'>Team4</option>
                  <option value='Team6'>Team6</option>
              </select>
              </div>
            </div>
          </div>
          <SubmitBtn type='submit'>Report Match Results</SubmitBtn>
      </div>
      <div className={styles.matchGroups}>
        <div className={styles.matchGroupCourt}>
          <FormattedMessage
            id='matchGroup3'
            defaultMessage='Court #3'
          />
          <br/>
          <div className={styles.matchGroupCourtTeams}>
            <div>
              <FormattedMessage
                id='team1'
                defaultMessage='Team 7: Marcus, Marie'
              />
            </div>
            <div>
              <FormattedMessage
                id='team2'
                defaultMessage='Team 8: Will, Danica'
              />
            </div>
          </div>
          <div className={styles.matchGroupCourtTeams}>
            <div>
              <FormattedMessage
                id='team3'
                defaultMessage='Team 9: Margerie, Nichols '
              />
            </div>
            <div>
              <FormattedMessage
                id='team3'
                defaultMessage='Team 10: Margerie, Nichols '
              />
            </div>
          </div>
        </div>
          <div>
            <div className={styles.matchGroupInfo}>
            <FormattedMessage
              id='match1'
              defaultMessage='Team 7 vs Team 8 @9:00pm'
            />
            <div className={styles.matchGroupResultsFORM}>
              <FormattedMessage
                id='matchResults3'
                defaultMessage='Results: '
              />
              <select>
                <option value=''>Select a team...</option>
                  <option value='Team7'>Team7</option>
                  <option value='Team8'>Team8</option>
              </select>
              </div>
            </div>
            <div className={styles.matchGroupInfo}>
            <FormattedMessage
              id='match2'
              defaultMessage='Team 7 vs Team 9 @9:00pm'
            />
              <div className={styles.matchGroupResultsFORM}>
              <FormattedMessage
                id='matchResults3'
                defaultMessage='Results: '
              />
              <select>
                <option value=''>Select a team...</option>
                  <option value='Team7'>Team7</option>
                  <option value='Team9'>Team9</option>
              </select>
              </div>
            </div>
            <div className={styles.matchGroupInfo}>
              <FormattedMessage
                id='match3'
                defaultMessage='Team 9 vs Team W @9:00pm'
              />
              <div className={styles.matchGroupResultsFORM}>
              <FormattedMessage
                id='matchResults3'
                defaultMessage='Results: '
              />
              <select>
                <option value=''>Select a team...</option>
                  <option value='Team9'>Team9</option>
                  <option value='TeamW'>TeamW</option>
              </select>
              </div>
            </div>
            <div className={styles.matchGroupInfo}>
              <FormattedMessage
                id='match4'
                defaultMessage='Team 10 vs Team D @9:00pm'
              />
              <div className={styles.matchGroupResultsFORM}>
              <FormattedMessage
                id='matchResults4'
                defaultMessage='Results: '
              />
              <select>
                <option value=''>Select a team...</option>
                  <option value='Team10'>Team10</option>
                  <option value='TeamD'>TeamD</option>
              </select>
              </div>
            </div>
            <SubmitBtn type='submit'>Report Match Results</SubmitBtn>
          </div>
          <br/>
      </div>
    </div>
  </div>
));

export default connect(
  (state) => ({
    teams: state.app.teams,
    login: state.app.loggedIn,
  }),
  {getMatchGroups}
)(MatchGroups);
