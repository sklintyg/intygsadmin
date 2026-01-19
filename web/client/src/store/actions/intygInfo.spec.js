import { vi } from 'vitest'
import { functionToTest, mockStore } from '@/testUtils/actionUtils'
import * as actions from './intygInfo'
import * as api from '../../api/intygInfo.api'
import * as intygInfoList from './intygInfoList'

describe('intygInfo actions', () => {
  let store

  beforeEach(() => {
    store = mockStore({
      intygInfo: {},
    })
  })

  afterEach(() => {
    vi.restoreAllMocks()
  })

  describe('fetchIntegratedUnit', () => {
    test('success', () => {
      const intygsId = 'IntygsId'
      const response = [{}]

      vi.spyOn(api, 'fetchIntygInfo').mockResolvedValue(response)

      vi.spyOn(intygInfoList, 'fetchIntygInfoList').mockReturnValue({
        type: 'TEMP_ACTION',
      })

      const expectedActions = [
        { type: actions.FETCH_INTYG_INFO_REQUEST },
        { type: 'TEMP_ACTION' },
        { type: actions.FETCH_INTYG_INFO_SUCCESS, response },
      ]

      return functionToTest(store, () => actions.fetchIntygInfo(intygsId), expectedActions)
    })

    test('failure', () => {
      const intygsId = 'IntygsId'
      const response = [{}]

      vi.spyOn(api, 'fetchIntygInfo').mockRejectedValue(response)

      const expectedActions = [{ type: actions.FETCH_INTYG_INFO_REQUEST }, { type: actions.FETCH_INTYG_INFO_FAILURE, payload: response }]

      return functionToTest(store, () => actions.fetchIntygInfo(intygsId), expectedActions)
    })
  })
})
