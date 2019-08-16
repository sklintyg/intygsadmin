import { applyMiddleware, compose, createStore } from 'redux'
import thunk from 'redux-thunk';
import rootReducer from './reducers'
import { createHashHistory } from 'history'
import { routerMiddleware } from 'connected-react-router'

export const history = createHashHistory();

export default function configureStore(preloadedState) {
  const middlewares = [thunk, routerMiddleware(history)];
  const middlewareEnhancer = applyMiddleware(...middlewares);

  const enhancers = [middlewareEnhancer];
  const composedEnhancers = compose(...enhancers);

  const store = createStore(rootReducer(history), preloadedState, composedEnhancers);

  return store
}
