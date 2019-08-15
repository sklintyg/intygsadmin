import * as actions from './user'
import * as api from '../../api/userApi';
import {functionToTest, mockStore} from '../../testUtils/actionUtils';

describe('actions', () => {
  let store;

  beforeEach(() => {
    store = mockStore({})
  })

  describe('getUser', () => {
    it('success', () => {
      const reponse = [{name: 'test'}]

      api.fetchAnvandare = () => {
        return Promise.resolve(reponse);
      }

      const expectedActions = [
        {type: actions.GET_USER},
        {type: actions.GET_USER_SUCCESS, payload: reponse}
      ]

      return functionToTest(store, actions.getUser, expectedActions)
    })

    it('failure', () => {
      api.fetchAnvandare = () => {
        return Promise.reject({message: 'failed', errorCode: 'ERRORCODE'});
      }

      const expectedActions = [
        {type: actions.GET_USER},
        {type: actions.GET_USER_FAILURE, payload: {message: 'failed', errorCode: 'ERRORCODE'}}
      ]

      return functionToTest(store, actions.getUser, expectedActions)
    })
  })

})
