import { functionToTest, mockStore } from '../../testUtils/actionUtils'
import * as actions from './bannerList.actions'
import * as api from '../../api/bannerList.api'

describe('bannerList actions', () => {
  let store

  beforeEach(() => {
    store = mockStore({
      bannerList: {
        listData: {},
        errorMessage: null,
        isFetching: false,
      },
    })
  })

  describe('fetchBannerList', () => {
    test('success', () => {
      const response = [{}]

      api.fetchBannerList = () => {
        return Promise.resolve(response)
      }

      const expectedActions = [
        { type: actions.FETCH_BANNERLIST_REQUEST },
        { type: actions.FETCH_BANNERLIST_SUCCESS, response },
      ]

      return functionToTest(
        store,
        () => actions.fetchBannerList({ sortColumn: '', sortDirection: '' }),
        expectedActions
      )
    })

    test('failure', () => {
      const response = [{}]

      api.fetchBannerList = () => {
        return Promise.reject(response)
      }

      const expectedActions = [
        { type: actions.FETCH_BANNERLIST_REQUEST },
        { type: actions.FETCH_BANNERLIST_FAILURE, payload: response },
      ]

      return functionToTest(
        store,
        () => actions.fetchBannerList({ sortColumn: '', sortDirection: '' }),
        expectedActions
      )
    })
  })
})
