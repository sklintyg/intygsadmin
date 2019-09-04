import { functionToTest, mockStore } from '../../testUtils/actionUtils'
import * as actions from './intygInfoList'
import * as api from '../../api/intygInfo.api'

describe('intygInfoList actions', () => {
  let store

  beforeEach(() => {
    store = mockStore({
      intygInfoList: {
        intygInfoList: {
          content: {},
          errorMessage: null,
          isFetching: false,
          sortColumn: 'createdAtNow',
          sortDirection: 'ASC'
        }
      },
    })
  })

  describe('fetchIntygInfoList', () => {
    test('success', () => {
      const response = [{}]

      api.fetchIntygInfoList = () => {
        return Promise.resolve(response)
      }

      const expectedActions = [
        { type: actions.FETCH_INTYG_INFO_LIST_REQUEST },
        { type: actions.FETCH_INTYG_INFO_LIST_SUCCESS, response, sortColumn: 'createdAt', sortDirection: 'DESC' },
      ]

      return functionToTest(
        store,
        () => actions.fetchIntygInfoList({ sortColumn: 'createdAt', sortDirection: 'DESC' }),
        expectedActions
      )
    })

    test('load sort from store', () => {
      const response = [{}]

      api.fetchIntygInfoList = () => {
        return Promise.resolve(response)
      }

      const expectedActions = [
        { type: actions.FETCH_INTYG_INFO_LIST_REQUEST },
        { type: actions.FETCH_INTYG_INFO_LIST_SUCCESS, response, sortColumn: 'createdAtNow', sortDirection: 'ASC' },
      ]

      return functionToTest(
        store,
        () => actions.fetchIntygInfoList(),
        expectedActions
      )
    })

    test('failure', () => {
      const response = [{}]

      api.fetchIntygInfoList = () => {
        return Promise.reject(response)
      }

      const expectedActions = [
        { type: actions.FETCH_INTYG_INFO_LIST_REQUEST },
        { type: actions.FETCH_INTYG_INFO_LIST_FAILURE, payload: response },
      ]

      return functionToTest(
        store,
        () => actions.fetchIntygInfoList({ sortColumn: 'createdAt', sortDirection: 'DESC' }),
        expectedActions
      )
    })
  })
})
