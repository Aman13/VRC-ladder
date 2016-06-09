# Vancouver Racquet Club Ladder System

## CMPT 373 - Team Alpha
------------------------

This project is split up by the individual components, the frontend and the backend. The frontend is written in JavaScript and the backend is written in Java. This project is under the Berkeley Software Distribution (BSD) license and was developed for the Vancouver Racquet Club (VRC).

------------
### Frontend

#### Instructions (For OS X):
1) Start a terminal session in the frontend directory (<project-dir>/frontend)
2) Download dependencies:

   brew install vips \
   --with-webp \
   --without-pango \
   --without-python \
   --without-pygobject3 \
   --with-graphicsmagick

  npm install

3) Start local development server:

  npm run dev
  env PORT=8080 npm run dev

4) Run the production build

	npm run build

TODO - Add instructions for Linux (and possibly Windows)

#### Citations:
All webpack settings, and redux settings are from various guides online. Below is a list of online resources that were read or used.
- https://developer.mozilla.org/en/docs/Web/API/Window/localStorage
- https://github.com/lodash/lodash/wiki/FP-Guide
- https://github.com/JedWatson/classnames
- https://github.com/css-modules/css-modules
- https://github.com/peterramsing/lost/wiki/Installation
- https://github.com/reactjs/react-redux/blob/master/docs/api.md#connectmapstatetoprops-mapdispatchtoprops-mergeprops-options
- https://github.com/fmoo/react-typeahead
- https://github.com/reactjs/reselect
- http://redux-form.com/5.2.3/#/api/reduxForm?_k=cvo2xg
- https://lodash.com/docs
- https://github.com/acdlite/redux-actions
- https://github.com/reactjs/react-redux/blob/master/docs/api.md#connectmapstatetoprops-mapdispatchtoprops-mergeprops-options
- http://flowtype.org/
- https://github.com/zalmoxisus/redux-devtools-extension
- https://github.com/gaearon/redux-thunk
- https://github.com/zalmoxisus/redux-devtools-extension
- https://github.com/zippyui/react-button
- https://blog.risingstack.com/the-react-way-getting-started-tutorial/
- http://paletton.com/#uid=13L0u0kllll5wtvdqpqtghgKwdb

-----------
### Backend

#### Instructions:
1) Start a terminal session in the backend directory (<project-dir>/backend)
2) Run "gradle clean run"

#### Citations:
- src\main\java\ca\sfu\cmpt373\alpha\vrctextui\Menu.java: Idea based on design from Summer 2016 CMPT213 As0.
