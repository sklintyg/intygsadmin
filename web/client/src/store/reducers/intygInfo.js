import { combineReducers } from 'redux'
import { buildClientError } from "./util";
import * as ActionConstants from '../actions/intygInfo'

const intygInfo = (state = {}, action) => {
  switch (action.type) {
    case ActionConstants.FETCH_INTYG_INFO_SUCCESS:
      return action.response
    case ActionConstants.FETCH_INTYG_INFO_REQUEST:
    case ActionConstants.FETCH_INTYG_INFO_FAILURE:
      return {}
    default:
      return state
  }
}

const isFetching = (state = false, action) => {
  switch (action.type) {
    case ActionConstants.FETCH_INTYG_INFO_REQUEST:
      return true
    case ActionConstants.FETCH_INTYG_INFO_SUCCESS:
    case ActionConstants.FETCH_INTYG_INFO_FAILURE:
      return false
    default:
      return state
  }
}

const errorMessage = (state = null, action) => {
  switch (action.type) {
    case ActionConstants.FETCH_INTYG_INFO_FAILURE:
      return buildClientError(action.payload, 'error.intygInfo').message
    case ActionConstants.FETCH_INTYG_INFO_SUCCESS:
    case ActionConstants.FETCH_INTYG_INFO_REQUEST:
      return null
    default:
      return state
  }
}


export default combineReducers({
  intygInfo,
  isFetching,
  errorMessage
})

export const getIntygInfo = (state) => state.intygInfo.intygInfo

export const getIsFetching = (state) => state.intygInfo.isFetching

export const getErrorMessage = (state) => state.intygInfo.errorMessage
