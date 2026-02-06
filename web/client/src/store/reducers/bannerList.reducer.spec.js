import reducer, { BannerListDefaultState } from './bannerList.reducer'
import * as actions from '../actions/bannerList.actions'
import { createStore } from 'redux'

describe('bannerlist reducer', () => {
  let stateBefore = {
    bannerList: { ...BannerListDefaultState },
    errorMessage: null,
    isFetching: false,
  }

  let store
  beforeEach(() => {
    store = createStore(reducer)
  })

  test('should return default', () => {
    expect(reducer(undefined, {})).toEqual(stateBefore)
  })

  test('should return statebefore', () => {
    expect(store.getState()).toEqual(stateBefore)
  })

  test('should add item in bannerList', () => {
    let action = {
      type: actions.FETCH_BANNERLIST_SUCCESS,
      response: {
        content: [
          {
            id: '7b8320c1-3a47-4fe5-aed9-15f9c60db357',
            createdAt: '2019-05-15 11:11:19',
            application: 'REHABSTOD',
            message: 'Past test messageREHABSTOD',
            displayFrom: '2019-04-27 11:11:19',
            displayTo: '2019-05-02 11:11:19',
            priority: 'LAG',
            status: 'FINISHED',
          },
        ],
        page: {
          empty: false,
          first: true,
          last: true,
          number: 0,
          numberOfElements: 1,
          totalElements: 1,
          size: 10,
          pageable: {
            sort: { sorted: true, unsorted: false, empty: false },
            offset: 0,
            pageSize: 10,
            pageNumber: 0,
            paged: true,
            unpaged: false,
          },
          totalPages: 1,
        },
      },
      sortColumn: 'createdAt',
      sortDirection: 'DESC',
    }

    store.dispatch(action)

    let stateAfter = {
      ...stateBefore,
      bannerList: {
        content: [
          {
            id: '7b8320c1-3a47-4fe5-aed9-15f9c60db357',
            createdAt: '2019-05-15 11:11:19',
            application: 'REHABSTOD',
            message: 'Past test messageREHABSTOD',
            displayFrom: '2019-04-27 11:11:19',
            displayTo: '2019-05-02 11:11:19',
            priority: 'LAG',
            status: 'FINISHED',
          },
        ],
        limit: 10,
        numberOfElements: 1,
        pageIndex: 0,
        sortColumn: 'createdAt',
        sortDirection: 'DESC',
        totalElements: 1,
        start: 1,
        end: 1,
      },
    }

    expect(store.getState()).toEqual(stateAfter)
  })
})
