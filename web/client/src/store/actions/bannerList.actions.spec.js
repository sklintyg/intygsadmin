import {functionToTest, mockStore} from '../../testUtils/actionUtils'
import * as actions from './bannerList.actions'
import * as api from '../../api/bannerList.api'

describe('bannerList actions', () => {
  let store

  beforeEach(() => {
    store = mockStore({
      bannerList: {
        bannerList: {
          content: {},
          errorMessage: null,
          isFetching: false,
          sortColumn: 'createdAtNow',
          sortDirection: 'ASC'
        }
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
        {type: actions.FETCH_BANNERLIST_REQUEST},
        {type: actions.FETCH_BANNERLIST_SUCCESS, response, sortColumn: 'createdAt', sortDirection: 'DESC'},
      ]

      return functionToTest(
        store,
        () => actions.fetchBannerList({sortColumn: 'createdAt', sortDirection: 'DESC'}),
        expectedActions
      )
    })

    test('load sort from store', () => {
      const response = [{}]

      api.fetchBannerList = () => {
        return Promise.resolve(response)
      }

      const expectedActions = [
        {type: actions.FETCH_BANNERLIST_REQUEST},
        {type: actions.FETCH_BANNERLIST_SUCCESS, response, sortColumn: 'createdAtNow', sortDirection: 'ASC'},
      ]

      return functionToTest(
        store,
        () => actions.fetchBannerList(),
        expectedActions
      )
    })

    test('failure', () => {
      const response = [{}]

      api.fetchBannerList = () => {
        return Promise.reject(response)
      }

      const expectedActions = [
        {type: actions.FETCH_BANNERLIST_REQUEST},
        {type: actions.FETCH_BANNERLIST_FAILURE, payload: response},
      ]

      return functionToTest(
        store,
        () => actions.fetchBannerList({sortColumn: 'createdAt', sortDirection: 'DESC'}),
        expectedActions
      )
    })
  })
})
