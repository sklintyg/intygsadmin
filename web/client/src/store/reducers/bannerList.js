import { combineReducers } from 'redux'
import { buildClientError } from './util'
import * as ActionConstants from '../actions/bannerList'

export const BannerListDefaultState = {
  listData: [],
  pageIndex: 0,
  totalPages: 0,
  numberOfElements: 0,
  totalElements: 0,
  sortColumn: 'ID',
  sortDirection: 'ASC',
}

const listData = (state = { ...BannerListDefaultState }, action) => {
  switch (action.type) {
    case ActionConstants.FETCH_BANNERLIST_SUCCESS:
      return action.response
    default:
      return state
  }
}

const isFetching = (state = false, action) => {

  switch (action.type) {
    case ActionConstants.FETCH_BANNERLIST_REQUEST:
      return true
    case ActionConstants.FETCH_BANNERLIST_SUCCESS:
    case ActionConstants.FETCH_BANNERLIST_FAILURE:
      return false
    default:
      return state
  }
}

const errorMessage = (state = null, action) => {

  switch (action.type) {
    case ActionConstants.FETCH_BANNERLIST_FAILURE:
      return buildClientError(action.payload, 'error.bannerlist').message
    case ActionConstants.FETCH_BANNERLIST_REQUEST:
    case ActionConstants.FETCH_BANNERLIST_SUCCESS:
      return null
    default:
      return state
  }
}

const bannerListReducer = combineReducers({
  listData,
  isFetching,
  errorMessage,
})

export default bannerListReducer

// Selectors

export const getBannerList = (state) => state.bannerList.listData

export const getIsFetching = (state) => state.bannerList.isFetching

export const getErrorMessage = (state) => state.bannerList.errorMessage
