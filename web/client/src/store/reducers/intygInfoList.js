import { combineReducers } from 'redux'
import { buildClientError } from "./util";
import * as ActionConstants from '../actions/intygInfoList'

export const IntygInfoListDefaultState = {
  content: [],
  pageIndex: 0,
  start: 0,
  end: 0,
  numberOfElements: 0,
  totalElements: 0,
  sortColumn: 'createdAt',
  sortDirection: 'DESC',
}

const intygInfoList = (state = IntygInfoListDefaultState, action) => {
  switch (action.type) {
    case ActionConstants.FETCH_INTYG_INFO_LIST_SUCCESS:
      return action.response
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
  errorMessage
})

export const getIntygInfoList = (state) => state.intygInfoList.intygInfoList

export const getIsFetching = (state) => state.intygInfoList.isFetching

export const getErrorMessage = (state) => state.intygInfoList.errorMessage

export const getSortOrder = (state) => {return {sortColumn: state.intygInfoList.intygInfoList.sortColumn, sortDirection: state.intygInfoList.intygInfoList.sortDirection}}
