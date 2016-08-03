const root = 'http://vrcladder.p76biyyfpm.us-west-2.elasticbeanstalk.com/';

export const updateLadder = (teams, state) => {
  return fetch(`${root}ladder/rearrange`, {
    method: 'PUT',
    body: JSON.stringify(teams),
    headers: new Headers({
      'Content-Type': 'application.json',
      Authorization: state.app.loggedIn.authorizationToken,
    }),
  }).then((response) => {
    const body = response.json();
    if (response.ok) {
      return Promise.resolve(body);
    }
    return Promise.reject(body);
  });
};