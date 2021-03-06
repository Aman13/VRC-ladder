import {createElement, Element} from 'react';
import {FormattedMessage} from 'react-intl';
import {connect} from 'react-redux';
import {reduxForm} from 'redux-form';
import {withRouter} from 'react-router';
import {updateUser,
  getCurrentActiveUserInfo as getUserInfo,
} from '../../action/users';
import styles from './updateProfile.css';
import Heading from '../heading/heading';
import isEmpty from 'lodash/fp/isEmpty';
import classNames from 'classnames';

import {
  Well, Col, ControlLabel, Button, FormControl, FormGroup, Form,
} from 'react-bootstrap';

const validate = (values) => {
  const errors = {};

  if (!values.firstName) {
    errors.firstName = 'Required';
  }

  if (!values.lastName) {
    errors.lastName = 'Required';
  }

  if (!values.password) {
    errors.password = 'Required';
  }

  if (!values.confirmPassword) {
    errors.confirmPassword = 'Required';
  }

  if  (!/^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i.test(values.emailAddress)) {
    errors.emailAddress = 'Invalid email address';
  }
  if (values.password !== values.confirmPassword) {
    errors.password = 'Password does not match';
  }
  return errors;
};

const parseUser = (props, userInfo) => {
  if (!props.userId) {
    props.userId = userInfo.userId;
  }
  if (!props.firstName) {
    props.firstName = userInfo.name;
  }
  if (!props.lastName) {
    props.lastName = userInfo.lastName;
  }
  if (!props.emailAddress) {
    props.emailAddress = userInfo.emailAddress;
  }
  if (!props.phoneNumber) {
    props.phoneNumber = userInfo.phoneNumber;
  }
  if (!props.password) {
    props.password = userInfo.password;
  }

  const user = props;
  delete user.confirmPassword;
  return ({
    ...user,
    userRole: 'PLAYER',
    middleName: '',
  });
};

const mapInitialStateToProps = (state) => {
  return {
    initialValues: {...state.app.userInfo,
      firstName: state.app.userInfo.name.split(' ')[0],
      lastName: state.app.userInfo.name.split(' ')[1],
    },
  };
};

const formEnhancer = reduxForm({
  form: 'updateProfile',
  fields: [
    'firstName',
    'lastName',
    'emailAddress',
    'phoneNumber',
    'password',
    'confirmPassword'],
  validate,
}, mapInitialStateToProps);

const FormError = ({touched, error}) => {
  if (touched && error) {
    return (
      <div className={classNames(styles.errorMsg)}>
        <Heading kind='error'>
          {error}
        </Heading>
      </div>
    );
  }
  return null;
};

const UpdateProfileInfoForm = ({
 fields: {
   firstName,
   lastName,
   emailAddress,
   phoneNumber,
   password,
   confirmPassword},
   handleSubmit}) => (
 <Form horizontal onSubmit={handleSubmit}>
   <div>
    <FormGroup>
       <Col componentClass={ControlLabel} sm={4}>First Name</Col>
       <Col sm={4}>
         <FormControl
           type='firstName'
           placeholder='eg. Jane'
           {...firstName}
         />
       </Col>
       <Col sm={3}><FormError {...firstName}/></Col>
     </FormGroup>

     <FormGroup>
       <Col componentClass={ControlLabel} sm={4}>Last Name</Col>
       <Col sm={4}>
         <FormControl type='lastName' placeholder='eg. Doe' {...lastName} />
       </Col>
       <Col sm={3}><FormError {...lastName}/></Col>
     </FormGroup>

     <FormGroup>
       <Col componentClass={ControlLabel} sm={4}>Email</Col>
       <Col sm={5}>
         <FormControl
           type='emailAddress'
           placeholder='eg. jane.doe@example.com'
           {...emailAddress}
         />
       </Col>
       <Col sm={3}><FormError {...emailAddress}/></Col>
     </FormGroup>

     <FormGroup>
       <Col componentClass={ControlLabel} sm={4}>Phone Number</Col>
       <Col sm={4}>
         <FormControl
           type='phoneNumber'
           placeholder='eg. 6041234567'
           {...phoneNumber}
         />
       </Col>
       <Col sm={3}><FormError {...phoneNumber}/></Col>
     </FormGroup>

     <FormGroup>
       <Col componentClass={ControlLabel} sm={4}>Password</Col>
       <Col sm={5}>
         <FormControl type='password' placeholder='Password' {...password} />
       </Col>
       <Col sm={3}><FormError {...password}/></Col>
     </FormGroup>

     <FormGroup>
       <Col componentClass={ControlLabel} sm={4}>Confirm Password</Col>
       <Col sm={5}>
         <FormControl
           type='password'
           placeholder='Password confirmation'
           {...confirmPassword}
         />
        </Col>
       <Col sm={3}><FormError {...confirmPassword}/></Col>
     </FormGroup>

     <div className={classNames(styles.center)}>
      <Button bsStyle='primary' bsSize='large' type='submit'>Confirm Update
      </Button>
     </div>
   </div>
 </Form>
);

const UpdateProfileForm = formEnhancer(UpdateProfileInfoForm);

const checkErrors = (responseErrors) => {
  const errors = {};
  if (responseErrors.emailAddress === false) {
    errors.emailAddress = 'A user with this email already exists!';
  }
  if (responseErrors.emailAddress === 'invalid') {
    errors.emailAddress = 'This email Address is not valid';
  }
  if (responseErrors.phoneNumber === 'invalid') {
    errors.phoneNumber = 'This phone number is not valid';
  }
  return errors;
};

const updateAccount = withRouter(({
  updateUser,
  getUserInfo,
  router,
  userInfo,
}) : Element => (
  <div className={styles.center}>
    <Well>
        <Heading>
          <FormattedMessage
            id='Update-profile'
            defaultMessage='Edit Your Account'
          />
        </Heading>
      <UpdateProfileForm
        onSubmit={(props) => {
          const errors = validate(props);
          const info = parseUser(props, userInfo);
          if (!isEmpty(errors)) {
            return Promise.reject(errors);
          }
          return updateUser(info).then(() => {
            // TODO: pause on a warning/alert here!
            getUserInfo().then(() => {
              router.push('/profile');
            });
          }).catch((response) => {
            return response.then(function(bodyContent) {
              const errors = checkErrors(bodyContent);
              return Promise.reject(errors);
            });
          });
        }}
      />
    </Well>
  </div>

));

export default connect(
  (state) => ({
    players: state.app.players,
    userInfo: state.app.userInfo,
  }),
  {updateUser,
    getUserInfo,
  }
)(updateAccount);
