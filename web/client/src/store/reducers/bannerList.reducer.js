import { combineReducers } from 'redux'
import { buildClientError } from './util'
import * as ActionConstants from '../actions/bannerList.constants'

export const BannerListDefaultState = {
  content: [],
  pageIndex: 0,
  start: 0,
  end: 0,
  numberOfElements: 0,
  totalElements: 0,
  limit: 10,
  sortColumn: 'createdAt',
  sortDirection: 'DESC',
}

const bannerList = (state = { ...BannerListDefaultState }, action) => {
  switch (action.type) {
    case ActionConstants.FETCH_BANNERLIST_SUCCESS:
      return {
        ...state,
        content: action.response.content,
        numberOfElements: action.response.content.length,
        pageIndex: action.response.page.number,
        start: action.response.page.number * action.response.page.size + 1,
        end: action.response.page.number * action.response.page.size + action.response.content.length,
        totalElements: action.response.page.totalElements,
        sortColumn: action.sortColumn,
        sortDirection: action.sortDirection,
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

export const getSortOrder = (state) => {
  return { sortColumn: state.bannerList.bannerList.sortColumn, sortDirection: state.bannerList.bannerList.sortDirection }
}

export const getIsFetching = (state) => state.bannerList.isFetching

export const getErrorMessage = (state) => state.bannerList.errorMessage
