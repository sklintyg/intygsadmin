import { combineReducers } from 'redux'
import { buildClientError } from './util'
import * as ActionConstants from '../actions/dataExport.constants'

export const DataExportListDefaultState = {
  content: [],
  pageIndex: 0,
  start: 0,
  end: 0,
  numberOfElements: 0,
  totalElements: 0,
  sortColumn: 'createdAt',
  sortDirection: 'DESC',
}

const dataExportList = (state = DataExportListDefaultState, action) => {
  switch (action.type) {
    case ActionConstants.FETCH_DATA_EXPORT_LIST_SUCCESS:
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
    case ActionConstants.FETCH_DATA_EXPORT_LIST_FAILURE:
      return DataExportListDefaultState
    default:
      return state
  }
}

const isFetching = (state = false, action) => {
  switch (action.type) {
    case ActionConstants.CREATE_DATA_EXPORT_REQUEST:
      return true
    case ActionConstants.CREATE_DATA_EXPORT_SUCCESS:
    case ActionConstants.CREATE_DATA_EXPORT_FAILURE:
      return false
    default:
      return state
  }
}

const errorMessageCreateDataExport = (state = null, action) => {
  switch (action.type) {
    case ActionConstants.CREATE_DATA_EXPORT_FAILURE:
      return {
        code: action.payload.error.errorCode,
        error: buildClientError(action.payload, 'error.dataExport').message,
      }
    case ActionConstants.CREATE_DATA_EXPORT_REQUEST:
    case ActionConstants.CREATE_DATA_EXPORT_SUCCESS:
      return null
    default:
      return state
  }
}

const errorMessageUpdateDataExport = (state = null, action) => {
  switch (action.type) {
    case ActionConstants.UPDATE_DATA_EXPORT_FAILURE:
      return {
        code: action.payload.error.errorCode,
        error: buildClientError(action.payload, 'error.dataExport').message,
      }
    case ActionConstants.UPDATE_DATA_EXPORT_REQUEST:
    case ActionConstants.UPDATE_DATA_EXPORT_SUCCESS:
      return null
    default:
      return state
  }
}

const errorMessageEraseDataExport = (state = null, action) => {
  switch (action.type) {
    case ActionConstants.ERASE_DATA_EXPORT_FAILURE:
      return {
        code: action.payload.error.errorCode,
        error: buildClientError(action.payload, 'error.dataExport').message,
      }
    case ActionConstants.ERASE_DATA_EXPORT_REQUEST:
    case ActionConstants.ERASE_DATA_EXPORT_SUCCESS:
      return null
    default:
      return state
  }
}

const errorMessageResendDataExportKey = (state = null, action) => {
  switch (action.type) {
    case ActionConstants.RESEND_DATA_EXPORT_KEY_FAILURE:
      return {
        code: action.payload.error.errorCode,
        error: buildClientError(action.payload, 'error.dataExport').message,
      }
    case ActionConstants.RESEND_DATA_EXPORT_KEY_REQUEST:
    case ActionConstants.RESEND_DATA_EXPORT_KEY_SUCCESS:
      return null
    default:
      return state
  }
}

const errorMessageFetchDataExportList = (state = null, action) => {
  switch (action.type) {
    case ActionConstants.FETCH_DATA_EXPORT_LIST_FAILURE:
      return {
        code: action.payload.error.errorCode,
        error: buildClientError(action.payload, 'error.dataExport').message,
      }
    case ActionConstants.FETCH_DATA_EXPORT_LIST_REQUEST:
    case ActionConstants.FETCH_DATA_EXPORT_LIST_SUCCESS:
      return null
    default:
      return state
  }
}

export default combineReducers({
  dataExportList,
  isFetching,
  errorMessageCreateDataExport,
  errorMessageUpdateDataExport,
  errorMessageEraseDataExport,
  errorMessageResendDataExportKey,
  errorMessageFetchDataExportList,
})

// Selectors

export const getIsFetching = (state) => state.dataExport.isFetching

export const getErrorMessageCreateDataExport = (state) => state.dataExport.errorMessageCreateDataExport

export const getErrorMessageUpdateDataExport = (state) => state.dataExport.errorMessageUpdateDataExport

export const getErrorMessageEraseDataExport = (state) => state.dataExport.errorMessageEraseDataExport

export const getErrorMessageResendDataExportKey = (state) => state.dataExport.errorMessageResendDataExportKey

export const getErrorMessageFetchDataExportList = (state) => state.dataExport.errorMessageFetchDataExportList

export const getDataExportList = (state) => state.dataExport.dataExportList

export const getSortOrder = (state) => {
  return {
    sortColumn: state.dataExport.dataExportList.sortColumn,
    sortDirection: state.dataExport.dataExportList.sortDirection,
  }
}
