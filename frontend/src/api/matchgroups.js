const root = 'http://localhost:4567/';

export const getMatchGroups = (state) => {
  return fetch(`${root}matchgroups/schedule`, {
    headers: {
      'Content-Type': 'application/json',
      Authorization: state.app.loggedIn.authorizationToken,
    },
  }).then((response) => {
    return response.json();
  });
};
