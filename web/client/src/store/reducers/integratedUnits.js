import { combineReducers } from 'redux'
import { buildClientError } from "./util";
import * as ActionConstants from '../actions/integratedUnits'

const integratedUnit = (state = {}, action) => {
  switch (action.type) {
    case ActionConstants.FETCH_INTEGRATED_UNIT_SUCCESS:
      return action.response
    case ActionConstants.FETCH_INTEGRATED_UNIT_FAILURE:
      return {}
    default:
      return state
  }
}

const isFetching = (state = false, action) => {
  switch (action.type) {
    case ActionConstants.FETCH_INTEGRATED_UNIT_REQUEST:
      return true
    case ActionConstants.FETCH_INTEGRATED_UNIT_SUCCESS:
    case ActionConstants.FETCH_INTEGRATED_UNIT_FAILURE:
      return false
    default:
      return state
  }
}

const errorMessage = (state = null, action) => {
  switch (action.type) {
    case ActionConstants.FETCH_INTEGRATED_UNIT_FAILURE:
      return buildClientError(action.payload, 'error.integratedUnit').message
    case ActionConstants.FETCH_INTEGRATED_UNIT_SUCCESS:
    case ActionConstants.FETCH_INTEGRATED_UNIT_REQUEST:
      return null
    default:
      return state
  }
}

const isFetchingIntegratedUnitsFile = (state = false, action) => {
  switch (action.type) {
    case ActionConstants.FETCH_INTEGRATED_UNITS_FILE_REQUEST:
      return true
    case ActionConstants.FETCH_INTEGRATED_UNITS_FILE_SUCCESS:
    case ActionConstants.FETCH_INTEGRATED_UNITS_FILE_FAILURE:
      return false
    default:
      return state
  }
}

const errorMessageIntegratedUnitsFile = (state = null, action) => {
  switch (action.type) {
    case ActionConstants.FETCH_INTEGRATED_UNITS_FILE_FAILURE:
      return buildClientError(action.payload, 'error.integratedUnitsFileExport').message
    case ActionConstants.FETCH_INTEGRATED_UNITS_FILE_SUCCESS:
    case ActionConstants.FETCH_INTEGRATED_UNITS_FILE_REQUEST:
      return null
    default:
      return state
  }
}

export default combineReducers({
  integratedUnit,
  isFetching,
  errorMessage,
  isFetchingIntegratedUnitsFile,
  errorMessageIntegratedUnitsFile
})

export const getIntegratedUnit = (state) => state.integratedUnits.integratedUnit

export const getIsFetching = (state) => state.integratedUnits.isFetching

export const getErrorMessage = (state) => state.integratedUnits.errorMessage

export const getIsFetchingIntegratedUnitsFile = (state) => state.integratedUnits.isFetchingIntegratedUnitsFile

export const getErrorMessageIntegratedUnitsFile = (state) => state.integratedUnits.errorMessageIntegratedUnitsFile
