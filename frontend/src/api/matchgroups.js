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
    headers: {
      'Content-Type': 'application/json',
      Authorization: state.app.loggedIn.authorizationToken,
    },
  }).then((response) => {
    return response.json();
  });
};

export const regenerateMatchGroups = (state) => {
  return fetch(`${root}ladder/regenerate`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      Authorization: state.app.loggedIn.authorizationToken,
    },
  }).then((response) => {
    return response.json();
  });
};

export const reportMatchResults = (props, state) => {
  return fetch(`${root}matchgroups/${props.matchGroupId}/scores`, {
    method: 'PUT',
    body: JSON.stringify(props.results),
    headers: {
      'Content-Type': 'application/json',
      Authorization: state.app.loggedIn.authorizationToken,
    },
  }).then((response) => {
    return response.json();
  });
};
