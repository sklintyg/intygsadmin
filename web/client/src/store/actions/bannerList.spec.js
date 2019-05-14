import { functionToTest, mockStore } from '../../testUtils/actionUtils'
import * as actions from '../actions/bannerList'
import * as api from '../../api/bannerListApi'

describe('beställningList actions', () => {
  let store

  beforeEach(() => {
    store = mockStore({
      bannerList: {
        listBestallningarByFilter: { AKTUELLA: { isFetching: false } },
      },
    })
  })

  describe('fetchBestallningList', () => {
    test('success', () => {
      const response = [{}]

      api.fetchBestallningList = () => {
        return Promise.resolve(response)
      }

      const expectedActions = [
        { type: actions.FETCH_BANNERLIST_REQUEST, categoryFilter: 'AKTUELLA' },
        { type: actions.FETCH_BANNERLIST_SUCCESS, categoryFilter: 'AKTUELLA', response },
      ]

      return functionToTest(
        store,
        () => actions.fetchBestallningList({ categoryFilter: 'AKTUELLA', textFilter: '', sortColumn: '', sortDirection: '' }),
        expectedActions
      )
    })

    test('failure', () => {
      const response = [{}]

      api.fetchBestallningList = () => {
        return Promise.reject(response)
      }

      const expectedActions = [
        { type: actions.FETCH_BANNERLIST_REQUEST, categoryFilter: 'AKTUELLA' },
        { type: actions.FETCH_BANNERLIST_FAILURE, categoryFilter: 'AKTUELLA', payload: response },
      ]

      return functionToTest(
        store,
        () => actions.fetchBestallningList({ categoryFilter: 'AKTUELLA', textFilter: '', sortColumn: '', sortDirection: '' }),
        expectedActions
      )
    })
  })
})
