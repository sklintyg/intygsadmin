import {functionToTest, mockStore} from '../../testUtils/actionUtils'
import * as actions from './appConfig'
import * as api from '../../api/appConfigApi'

describe('appConfig actions', () => {
  let store

  beforeEach(() => {
    store = mockStore({
      appConfig: {},
    })
  })

  describe('fetchAppConfig', () => {
    test('success', () => {
      const response = {config: '123'}

      api.fetchAppConfig = () => {
        return Promise.resolve(response)
      }

      const expectedActions = [
        { type: actions.FETCH_APPCONFIG_REQUEST },
        { type: actions.FETCH_APPCONFIG_SUCCESS, response},
      ]

      return functionToTest(
        store,
        () => actions.fetchAppConfig(),
        expectedActions
      )
    })

    test('failure', () => {
      const response = {config: '123'}

      api.fetchAppConfig = () => {
        return Promise.reject(response)
      }

      const expectedActions = [
        { type: actions.FETCH_APPCONFIG_REQUEST },
        { type: actions.FETCH_APPCONFIG_FAILURE, payload: response },
      ]

      return functionToTest(
        store,
        () => actions.fetchAppConfig(),
        expectedActions
      )
    })
  })
})
