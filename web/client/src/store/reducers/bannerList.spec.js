import reducer, { BestallningListDefaultState } from './bannerList'
import * as actions from '../actions/bannerList'
import { createStore } from 'redux'

describe('bannerlist reducer', () => {
  let stateBefore = {
    listBestallningarByFilter: {
      AKTUELLA: {
        bannerList: { ...BestallningListDefaultState },
        errorMessage: null,
        isFetching: false,
      },
      AVVISADE: {
        bannerList: { ...BestallningListDefaultState },
        errorMessage: null,
        isFetching: false,
      },
      KLARA: {
        bannerList: { ...BestallningListDefaultState },
        errorMessage: null,
        isFetching: false,
      },
    },
  }

  let store
  beforeEach(() => {
    store = createStore(reducer)
  })

  test('should return default', () => {
    expect(reducer(undefined, {})).toEqual(stateBefore)
  })

  test('should return statebefore for aktuella', () => {
    expect(store.getState().listBestallningarByFilter.AKTUELLA).toEqual(stateBefore.listBestallningarByFilter.AKTUELLA)
  })

  test('should add item in bestallninList for aktuella', () => {
    let action = {
      type: actions.FETCH_BANNERLIST_SUCCESS,
      categoryFilter: 'AKTUELLA',
      response: { id: 1 },
    }

    store.dispatch(action)

    let stateAfter = { ...stateBefore.listBestallningarByFilter.AKTUELLA, bannerList: { id: 1 } }
    expect(store.getState().listBestallningarByFilter.AKTUELLA).toEqual(stateAfter)
  })
})
