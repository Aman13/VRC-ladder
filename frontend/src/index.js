import {createElement, Element} from 'react';
import {Provider} from 'react-redux';
import {IntlProvider} from 'react-intl';
import {Router, Route, IndexRoute, browserHistory} from 'react-router';
import {getUser} from './action/users';
import {getTeams} from './action/teams';
import MatchGroup from './component/matchgroup/matchgroup';
import SignUp from './component/signup/signup';
import Ladder from './component/ladder/ladder';
import CreateTeam from './component/create-team/create-team';
import {Nav, NavItem, Navbar, Grid} from 'react-bootstrap';
import {LinkContainer} from 'react-router-bootstrap';
import styles from './index.css';
import LogIn from './component/login/login';

const Layout = ({children}) => (
  <div>
    <div>
      <Navbar fixedTop className={styles.upperNavbar}>
        <Navbar.Header>
          <Navbar.Brand>
            <LinkContainer to='/'>
              <img src={require('url?limit=10000!./../src/public/logo.png')} />
            </LinkContainer>
          </Navbar.Brand>
          <Navbar.Toggle />
        </Navbar.Header>
        <Navbar.Collapse>
          <Nav pullLeft>
            <p className={styles.navbarLogo}>Vancouver Racquets Club</p>
          </Nav>
          <Nav pullRight className={styles.navItem}>
            <LinkContainer to='/'>
              <NavItem >Log in</NavItem>
            </LinkContainer>
            <LinkContainer to='/create-team'>
              <NavItem >Create team</NavItem>
            </LinkContainer>
            <LinkContainer to='/ladder'>
              <NavItem >Ladder</NavItem>
            </LinkContainer>
            <LinkContainer to='/signup'>
              <NavItem >Sign up</NavItem>
            </LinkContainer>
            <LinkContainer to='/matchgroup'>
              <NavItem >Match Groups</NavItem>
            </LinkContainer>
          </Nav>
        </Navbar.Collapse>
      </Navbar>

      <Navbar fixedTop className={styles.lowerNavbar}>
        <Nav className={styles.lowerNavContainer}>
          <Navbar.Text className={styles.lowerNavbarHeading}>
          Weekly Doubles Leaderboard
          </Navbar.Text>
        </Nav>
      </Navbar>
    </div>
    <Grid>
      {children}
    </Grid>
  </div>
);

export default ({store}) : Element => (
  <Provider store={store}>
    <IntlProvider messages={{}} defaultLocale='en-US'>
      <Router history={browserHistory}>
        <Route path='/' component={Layout}>
          <IndexRoute component={LogIn}/>
          <Route path='/signup' component={SignUp}/>
          <Route path='/login' component={LogIn}/>
          <Route path='/create-team' component={CreateTeam}/>
          <Route path='/matchgroup' component={MatchGroup}/>
          <Route
            path='/ladder'
            component={Ladder}
            onEnter={(nextState, replace, callback) => {
              store.dispatch(getUser()).then(callback);
              store.dispatch(getTeams()).then(callback);
            }}
          />
        </Route>
      </Router>
    </IntlProvider>
  </Provider>
);
