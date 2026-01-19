import { vi } from 'vitest'
import { functionToTest, mockStore } from '@/testUtils/actionUtils'
import * as actions from './bannerList.actions'
import * as api from '@/api/bannerList.api'

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
          sortDirection: 'ASC',
        },
      },
    })
  })

  afterEach(() => {
    vi.restoreAllMocks()
  })

  describe('fetchBannerList', () => {
    test('success', () => {
      const response = [{}]

      vi.spyOn(api, 'fetchBannerList').mockResolvedValue(response)

      const expectedActions = [
        { type: actions.FETCH_BANNERLIST_REQUEST },
        { type: actions.FETCH_BANNERLIST_SUCCESS, response, sortColumn: 'createdAt', sortDirection: 'DESC' },
      ]

      return functionToTest(store, () => actions.fetchBannerList({ sortColumn: 'createdAt', sortDirection: 'DESC' }), expectedActions)
    })

    test('load sort from store', () => {
      const response = [{}]

      vi.spyOn(api, 'fetchBannerList').mockResolvedValue(response)

      const expectedActions = [
        { type: actions.FETCH_BANNERLIST_REQUEST },
        { type: actions.FETCH_BANNERLIST_SUCCESS, response, sortColumn: 'createdAtNow', sortDirection: 'ASC' },
      ]

      return functionToTest(store, () => actions.fetchBannerList(), expectedActions)
    })

    test('failure', () => {
      const response = [{}]

      vi.spyOn(api, 'fetchBannerList').mockRejectedValue(response)

      const expectedActions = [{ type: actions.FETCH_BANNERLIST_REQUEST }, { type: actions.FETCH_BANNERLIST_FAILURE, payload: response }]

      return functionToTest(store, () => actions.fetchBannerList({ sortColumn: 'createdAt', sortDirection: 'DESC' }), expectedActions)
    })
  })
})
