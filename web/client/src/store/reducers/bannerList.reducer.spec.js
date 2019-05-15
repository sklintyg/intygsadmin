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
        listData: [{ id: 1 }],
        numberOfElements: 0,
        pageIndex: 0,
        sortColumn: 'ID',
        sortDirection: 'ASC',
        totalElements: 0,
        totalPages: 0,
      },
    }

    store.dispatch(action)

    let stateAfter = {
      ...stateBefore,
      bannerList: {
        listData: [{ id: 1 }],
        numberOfElements: 0,
        pageIndex: 0,
        sortColumn: 'ID',
        sortDirection: 'ASC',
        totalElements: 0,
        totalPages: 0,
      },
    }

    expect(store.getState()).toEqual(stateAfter)
  })
})
