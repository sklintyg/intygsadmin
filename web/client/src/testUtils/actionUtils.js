import { configureStore } from '@reduxjs/toolkit'

const createMockReducer = (preloadedState = {}) => {
  return (state = preloadedState, action) => {
    if (Object.keys(preloadedState).length === 0) {
      return preloadedState
    }
    const keys = Object.keys(preloadedState)
    return keys.reduce((acc, key) => {
      acc[key] = action.type ? action.response || action.payload || preloadedState[key] : preloadedState[key]
      return acc
    }, {})
  }
}

export const mockStore = (preloadedState = {}) => {
  const dispatchedActions = []

  const captureActionsMiddleware = () => (next) => (action) => {
    if (typeof action !== 'function') {
      dispatchedActions.push(action)
    }
    return next(action)
  }

  const store = configureStore({
    reducer: createMockReducer(preloadedState),
    preloadedState,
    middleware: (getDefaultMiddleware) =>
      [captureActionsMiddleware].concat(
        getDefaultMiddleware({
          serializableCheck: false,
          immutableCheck: false,
        })
      ),
  })

  store.getActions = () => dispatchedActions
  store.clearActions = () => {
    dispatchedActions.length = 0
  }

  return store
}

export const functionToTest = (store, actionHelper, expectedActions) => {
  const expectFunc = () => {
    expect(store.getActions()).toEqual(expectedActions)
    return Promise.resolve()
  }

  return store.dispatch(actionHelper()).then(expectFunc).catch(expectFunc)
}

export const syncronousActionTester = (store, actionHelper, expectedActions) => {
  store.dispatch(actionHelper())
  if (expectedActions) {
    expect(store.getActions()).toEqual(expectedActions)
  } else {
    return store.getActions()
  }
}
