import { applyMiddleware, compose, createStore } from 'redux'
import thunk from 'redux-thunk';
import rootReducer from './reducers'
import { createHashHistory } from 'history'
import { routerMiddleware } from 'connected-react-router'

export const history = createHashHistory();

export default function configureStore(preloadedState) {
  const middlewares = [thunk, routerMiddleware(history)];
  const middlewareEnhancer = applyMiddleware(...middlewares);

  const enhancers = [middlewareEnhancer,
    window.__REDUX_DEVTOOLS_EXTENSION__ && window.__REDUX_DEVTOOLS_EXTENSION__()]; //TODO: Added to make Redux DevTools work
  const composedEnhancers = compose(...enhancers);

  const store = createStore(rootReducer(history), preloadedState, composedEnhancers);

  return store
}
