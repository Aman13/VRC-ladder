import express from 'express';
import jwt from 'jwt-simple';
import {json} from 'body-parser';

const JWT_SECRET='jews';

const app = express();

const players = [];

const checkAuth = (req, res, next) => {
  if (req.headers.authorization) {
    try {
      const decoded = jwt.decode(req.headers.authorization, JWT_SECRET);
      req.auth = decoded;
      next();
    } catch (e) {
      next(e);
    }
  } else {
    next('FAIL NO AUTH');
  }
};

app.use((req, res, next) => {
  res.set('Access-Control-Allow-Origin', '*');
  res.set('Access-Control-Allow-Headers', 'content-type');
  next();
});

app.use(json());

app.post('/login', (req, res) => {
  if (req.body.username === 'lolaman' && req.body.password === 'secretk') {
    res.status(200).send(jwt.encode({username: 'aman'}, JWT_SECRET));
  } else {
    res.status(401).send('NO LOGIN U');
  }
});

app.get('/secret', checkAuth, (req, res) => {
  res.status(200).send('SECRET!!', req.auth.username);
});

app.post('/players/new', (req, res) => {
  console.log('WE GOT', req.body);
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
