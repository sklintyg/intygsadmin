import { combineReducers } from 'redux'
import { buildClientError } from './util'
import * as ActionConstants from '../actions/banner'

const banner = (state = {}, action) => {
  switch (action.type) {
    case ActionConstants.REMOVE_BANNER_SUCCESS:
    case ActionConstants.CREATE_BANNER_SUCCESS:
    case ActionConstants.UPDATE_BANNER_SUCCESS:
      return action.response
    default:
      return state
  }
}

const isFetching = (state = false, action) => {
  switch (action.type) {
    case ActionConstants.REMOVE_BANNER_REQUEST:
    case ActionConstants.CREATE_BANNER_REQUEST:
    case ActionConstants.UPDATE_BANNER_REQUEST:
    case ActionConstants.FETCH_FUTURE_REQUEST:
      return true
    case ActionConstants.REMOVE_BANNER_SUCCESS:
    case ActionConstants.REMOVE_BANNER_FAILURE:
    case ActionConstants.CREATE_BANNER_SUCCESS:
    case ActionConstants.CREATE_BANNER_FAILURE:
    case ActionConstants.UPDATE_BANNER_SUCCESS:
    case ActionConstants.UPDATE_BANNER_FAILURE:
    case ActionConstants.FETCH_FUTURE_SUCCESS:
    case ActionConstants.FETCH_FUTURE_FAILURE:
      return false
    default:
      return state
  }
}

const errorMessage = (state = null, action) => {
  switch (action.type) {
    case ActionConstants.REMOVE_BANNER_FAILURE:
    case ActionConstants.CREATE_BANNER_FAILURE:
    case ActionConstants.UPDATE_BANNER_FAILURE:
    case ActionConstants.FETCH_FUTURE_FAILURE:
      return buildClientError(action.payload, 'error.banner').message
    case ActionConstants.REMOVE_BANNER_REQUEST:
    case ActionConstants.REMOVE_BANNER_SUCCESS:
    case ActionConstants.CREATE_BANNER_REQUEST:
    case ActionConstants.CREATE_BANNER_SUCCESS:
    case ActionConstants.UPDATE_BANNER_REQUEST:
    case ActionConstants.UPDATE_BANNER_SUCCESS:
    case ActionConstants.FETCH_FUTURE_SUCCESS:
    case ActionConstants.FETCH_FUTURE_REQUEST:
      return null
    default:
      return state
  }
}

const futureBanners = (state = {}, action) => {
  switch (action.type) {
    case ActionConstants.FETCH_FUTURE_SUCCESS:
      return action.response
    default:
      return state
  }
}

export default combineReducers({
  banner,
  isFetching,
  errorMessage,
  futureBanners,
})

// Selectors

export const getBanner = (state) => state.banner.banner

export const getIsFetching = (state) => state.banner.isFetching

export const getErrorMessage = (state) => state.banner.errorMessage

export const getFutureBanners = (state) => state.banner.futureBanners
