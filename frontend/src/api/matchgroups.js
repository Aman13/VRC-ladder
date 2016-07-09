<<<<<<< HEAD
<<<<<<< HEAD
const root = 'http://vrcladder.p76biyyfpm.us-west-2.elasticbeanstalk.com/';

export const getMatchGroups = (state) => {
  return fetch(`${root}matchgroups`, {
    headers: {
      'Content-Type': 'application/json',
      Authorization: state.app.loggedIn.authorizationToken,
    },
  }).then((response) => {
    return response.json();
  });
};

export const generateMatchGroups = (state) => {
  return fetch(`${root}matchgroups/generate`, {
    method: 'POST',
=======
=======
>>>>>>> ada1d380d5aa4707c20721c1fadf63aee93648b1
const root = 'http://localhost:4567/';

export const getMatchGroups = (state) => {
  return fetch(`${root}matchgroups/schedule`, {
<<<<<<< HEAD
>>>>>>> ada1d380d5aa4707c20721c1fadf63aee93648b1
=======
>>>>>>> ada1d380d5aa4707c20721c1fadf63aee93648b1
    headers: {
      'Content-Type': 'application/json',
      Authorization: state.app.loggedIn.authorizationToken,
    },
  }).then((response) => {
    return response.json();
  });
};
