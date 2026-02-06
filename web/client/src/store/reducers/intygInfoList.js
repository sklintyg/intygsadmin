import { combineReducers } from 'redux'
import { buildClientError } from './util'
import * as ActionConstants from '../actions/intygInfoList.constants'

export const IntygInfoListDefaultState = {
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

const intygInfoList = (state = IntygInfoListDefaultState, action) => {
  switch (action.type) {
    case ActionConstants.FETCH_INTYG_INFO_LIST_SUCCESS:
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
    case ActionConstants.FETCH_INTYG_INFO_LIST_FAILURE:
      return IntygInfoListDefaultState
    default:
      return state
  }
}

const isFetching = (state = false, action) => {
  switch (action.type) {
    case ActionConstants.FETCH_INTYG_INFO_LIST_REQUEST:
      return true
    case ActionConstants.FETCH_INTYG_INFO_LIST_SUCCESS:
    case ActionConstants.FETCH_INTYG_INFO_LIST_FAILURE:
      return false
    default:
      return state
  }
}

const errorMessage = (state = null, action) => {
  switch (action.type) {
    case ActionConstants.FETCH_INTYG_INFO_LIST_FAILURE:
      return buildClientError(action.payload, 'error.intygInfoList').message
    case ActionConstants.FETCH_INTYG_INFO_LIST_SUCCESS:
    case ActionConstants.FETCH_INTYG_INFO_LIST_REQUEST:
      return null
    default:
      return state
  }
}

export default combineReducers({
  intygInfoList,
  isFetching,
  errorMessage,
})

export const getIntygInfoList = (state) => state.intygInfoList.intygInfoList

export const getIsFetching = (state) => state.intygInfoList.isFetching

export const getErrorMessage = (state) => state.intygInfoList.errorMessage

export const getSortOrder = (state) => {
  return { sortColumn: state.intygInfoList.intygInfoList.sortColumn, sortDirection: state.intygInfoList.intygInfoList.sortDirection }
}
