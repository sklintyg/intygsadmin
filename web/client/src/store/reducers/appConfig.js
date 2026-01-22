import { combineReducers } from 'redux'
import { buildClientError } from './util'
import * as ActionConstants from '../actions/appConfig.constants'

export const CONFIG_DEFAULT_STATE = {
  versionInfo: {
    buildVersion: 'Hämtar aktuell version...',
  },
}

const settings = (state = CONFIG_DEFAULT_STATE, action) => {
  switch (action.type) {
    case ActionConstants.FETCH_APPCONFIG_SUCCESS:
      return action.response
    default:
      return state
  }
}

const isFetching = (state = false, action) => {
  switch (action.type) {
    case ActionConstants.FETCH_APPCONFIG_REQUEST:
      return true
    case ActionConstants.FETCH_APPCONFIG_SUCCESS:
    case ActionConstants.FETCH_APPCONFIG_FAILURE:
      return false
    default:
      return state
  }
}

const errorMessage = (state = null, action) => {
  switch (action.type) {
    case ActionConstants.FETCH_APPCONFIG_FAILURE:
      return buildClientError(action.payload, 'error.appconfig')
    case ActionConstants.FETCH_APPCONFIG_REQUEST:
    case ActionConstants.FETCH_APPCONFIG_SUCCESS:
      return null
    default:
      return state
  }
}

export default combineReducers({
  settings,
  isFetching,
  errorMessage,
})

// Selectors
export const getSettings = (state) => state.appConfig.settings

export const getIsFetching = (state) => state.appConfig.isFetching

export const getErrorMessage = (state) => state.appConfig.errorMessage
