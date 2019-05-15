import { combineReducers } from 'redux'
import { buildClientError } from './util'
import * as ActionConstants from '../actions/bannerList.actions'

export const BannerListDefaultState = {
  content: [],
  pageIndex: 0,
  start: 0,
  end: 0,
  numberOfElements: 0,
  totalElements: 0,
  sortColumn: 'createdAt',
  sortDirection: 'DESC',
}

const bannerList = (state = { ...BannerListDefaultState }, action) => {
  switch (action.type) {
    case ActionConstants.FETCH_BANNERLIST_SUCCESS:
      return { ...state,
        content: action.response.content,
        numberOfElements: action.response.numberOfElements,
        start: action.response.number * action.response.size,
        end: action.response.number * action.response.size + action.response.numberOfElements,
        totalElements: action.response.totalElements
      }
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
  bannerList,
  isFetching,
  errorMessage,
})

export default bannerListReducer

// Selectors

export const getBannerList = (state) => state.bannerList.bannerList

export const getIsFetching = (state) => state.bannerList.isFetching

export const getErrorMessage = (state) => state.bannerList.errorMessage
