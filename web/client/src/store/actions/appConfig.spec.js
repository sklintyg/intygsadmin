import { vi } from 'vitest'
import { functionToTest, mockStore } from '@/testUtils/actionUtils'
import * as actions from './appConfig'
import * as api from '../../api/appConfigApi'

describe('appConfig actions', () => {
  let store

  beforeEach(() => {
    store = mockStore({
      appConfig: {},
    })
  })

  afterEach(() => {
    vi.restoreAllMocks()
  })

  describe('fetchAppConfig', () => {
    test('success', () => {
      const response = { config: '123' }

      vi.spyOn(api, 'fetchAppConfig').mockResolvedValue(response)

      const expectedActions = [{ type: actions.FETCH_APPCONFIG_REQUEST }, { type: actions.FETCH_APPCONFIG_SUCCESS, response }]

      return functionToTest(store, () => actions.fetchAppConfig(), expectedActions)
    })

    test('failure', () => {
      const response = { config: '123' }

      vi.spyOn(api, 'fetchAppConfig').mockRejectedValue(response)

      const expectedActions = [{ type: actions.FETCH_APPCONFIG_REQUEST }, { type: actions.FETCH_APPCONFIG_FAILURE, payload: response }]

      return functionToTest(store, () => actions.fetchAppConfig(), expectedActions)
    })
  })
})
