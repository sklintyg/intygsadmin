import { combineReducers } from 'redux'

export const CountStatusDefaultState = {
  count: 0,
  max: 0,
}

const countStatus = (state = CountStatusDefaultState, action) => {
  switch (action.type) {
    case 'RESEND_CERTIFICATE_STATUS_COUNT_SUCCESS':
    case 'RESEND_UNIT_STATUS_COUNT_SUCCESS':
    case 'RESEND_CAREGIVER_STATUS_COUNT_SUCCESS':
      return { ...state, count: action.response.count, countMax: action.response.max }
    case 'RESEND_CERTIFICATE_STATUS_COUNT_FAILURE':
    case 'RESEND_UNIT_STATUS_COUNT_FAILURE':
    case 'RESEND_CAREGIVER_STATUS_COUNT_FAILURE':
      return CountStatusDefaultState
    default:
      return state
  }
}

const isFetching = (state = false, action) => {
  switch (action.type) {
    case 'RESEND_CERTIFICATE_STATUS_COUNT_REQUEST':
    case 'RESEND_UNIT_STATUS_COUNT_REQUEST':
    case 'RESEND_CAREGIVER_STATUS_COUNT_REQUEST':
      return true
    case 'RESEND_CERTIFICATE_STATUS_COUNT_SUCCESS':
    case 'RESEND_UNIT_STATUS_COUNT_SUCCESS':
    case 'RESEND_CAREGIVER_STATUS_COUNT_SUCCESS':
    case 'RESEND_CERTIFICATE_STATUS_COUNT_FAILURE':
    case 'RESEND_UNIT_STATUS_COUNT_FAILURE':
    case 'RESEND_CAREGIVER_STATUS_COUNT_FAILURE':
      return false
    default:
      return state
  }
}

const errorMessage = (state = null, action) => {
  switch (action.type) {
    case 'RESEND_CERTIFICATE_STATUS_COUNT_FAILURE':
    case 'RESEND_UNIT_STATUS_COUNT_FAILURE':
    case 'RESEND_CAREGIVER_STATUS_COUNT_FAILURE':
      return 'Failed to load event count'
    case 'RESEND_CERTIFICATE_STATUS_COUNT_SUCCESS':
    case 'RESEND_UNIT_STATUS_COUNT_SUCCESS':
    case 'RESEND_CAREGIVER_STATUS_COUNT_SUCCESS':
    case 'RESEND_CERTIFICATE_STATUS_COUNT_REQUEST':
    case 'RESEND_UNIT_STATUS_COUNT_REQUEST':
    case 'RESEND_CAREGIVER_STATUS_COUNT_REQUEST':
      return null
    default:
      return state
  }
}

export default combineReducers({ countStatus, isFetching, errorMessage })

// Selectors

export const getStatusCount = (state) => state.countStatus.countStatus.count

export const getStatusMaxCount = (state) => state.countStatus.countStatus.countMax

export const getIsFetching = (state) => state.countStatus.isFetching

export const getErrorMessage = (state) => state.countStatus.errorMessage
