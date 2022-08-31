import { combineReducers } from 'redux'
import { buildClientError } from "./util";
import * as ActionConstants from '../actions/privatePractitioner'

const privatePractitioner = (state = {}, action) => {
  switch (action.type) {
    case ActionConstants.FETCH_PRIVATE_PRACTITIONER_SUCCESS:
      return action.response
    case ActionConstants.FETCH_PRIVATE_PRACTITIONER_FAILURE:
      return {}
    default:
      return state
  }
}

const isFetching = (state = false, action) => {
  switch (action.type) {
    case ActionConstants.FETCH_PRIVATE_PRACTITIONER_REQUEST:
      return true
    case ActionConstants.FETCH_PRIVATE_PRACTITIONER_SUCCESS:
    case ActionConstants.FETCH_PRIVATE_PRACTITIONER_FAILURE:
      return false
    default:
      return state
  }
}

const errorMessage = (state = null, action) => {
  switch (action.type) {
    case ActionConstants.FETCH_PRIVATE_PRACTITIONER_FAILURE:
      return buildClientError(action.payload, 'error.privatePractitioner').message
    case ActionConstants.FETCH_PRIVATE_PRACTITIONER_SUCCESS:
    case ActionConstants.FETCH_PRIVATE_PRACTITIONER_REQUEST:
      return null
    default:
      return state
  }
}

const errorMessageUnregisterPrivatePractitioner = (state = null, action) => {
  switch (action.type) {
  case ActionConstants.UNREGISTER_PRIVATE_PRACTITIONER_FAILURE:
    return buildClientError(action.payload, 'error.privatePractitioner.unregister').message
  case ActionConstants.UNREGISTER_PRIVATE_PRACTITIONER_REQUEST:
  case ActionConstants.UNREGISTER_PRIVATE_PRACTITIONER_SUCCESS:
    return null;
  default:
    return state;
  }
};

const isFetchingPrivatePractitionerFile = (state = false, action) => {
  switch (action.type) {
    case ActionConstants.FETCH_PRIVATE_PRACTITIONER_FILE_REQUEST:
      return true
    case ActionConstants.FETCH_PRIVATE_PRACTITIONER_FILE_SUCCESS:
    case ActionConstants.FETCH_PRIVATE_PRACTITIONER_FILE_FAILURE:
      return false
    default:
      return state
  }
}

const errorMessagePrivatePractitionerFile = (state = null, action) => {
  switch (action.type) {
    case ActionConstants.FETCH_PRIVATE_PRACTITIONER_FILE_FAILURE:
      return buildClientError(action.payload, 'error.privatePractitionerFileExport').message
    case ActionConstants.FETCH_PRIVATE_PRACTITIONER_FILE_SUCCESS:
    case ActionConstants.FETCH_PRIVATE_PRACTITIONER_FILE_REQUEST:
      return null
    default:
      return state
  }
}

export default combineReducers({
  privatePractitioner,
  isFetching,
  errorMessage,
  isFetchingPrivatePractitionerFile,
  errorMessagePrivatePractitionerFile,
  errorMessageUnregisterPrivatePractitioner
})

export const getPrivatePractitioner = (state) => state.privatePractitioner.privatePractitioner

export const getIsFetching = (state) => state.privatePractitioner.isFetching

export const getErrorMessage = (state) => state.privatePractitioner.errorMessage

export const getIsFetchingPrivatePractitionerFile = (state) => state.privatePractitioner.isFetchingPrivatePractitionerFile

export const getErrorMessagePrivatePractitionerFile = (state) => state.privatePractitioner.errorMessagePrivatePractitionerFile

export const getErrorMessageUnregisterPrivatePractitioner = (state) => state.privatePractitioner.errorMessageUnregisterPrivatePractitioner
