import configureMockStore from 'redux-mock-store'
import thunk from 'redux-thunk'

export const functionToTest = (store, actionHelper, expectedActions) => {

  const expectFunc = () => {
    // return of async actions
    expect(store.getActions()).toEqual(expectedActions)

    return Promise.resolve();
  };

  return store.dispatch(actionHelper())
    .then(expectFunc)
    .catch(expectFunc);
}


export const syncronousActionTester = (store, actionHelper, expectedActions) => {
  store.dispatch(actionHelper())
  if (expectedActions) {
    expect(store.getActions()).toEqual(expectedActions)
  } else {
    return store.getActions()
  }
};

export const routerAction = (url) => (
  {
  "payload": {"args": [url], "method": "push"},
  "type": "@@router/CALL_HISTORY_METHOD"}
  );

const middlewares = [thunk]
export const mockStore = configureMockStore(middlewares)
