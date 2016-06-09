import express from 'express';

const app = express();

const players = [];

app.use((req, res, next) => {
  res.set('Access-Control-Allow-Origin', '*');
  next();
});

app.post('/players/new', (req, res) => {
  players.push({
    firstName: Math.random(),
    lastName: Math.random(),
    email: `player@${Math.random()}.com`,
  });
  res.status(200).send(JSON.stringify({players}));
});

app.get('/players', (req, res) => {
  res.status(200).send(JSON.stringify({players}));
});

app.get('/teams', (req, res) => {
  res.status(200).send(JSON.stringfy({players}));
});

app.listen(6789);
