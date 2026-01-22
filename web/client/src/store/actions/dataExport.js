import * as api from '../../api/dataExport.api'
import { getIsFetching, getSortOrder } from '../reducers/dataExport'
import {
  CREATE_DATA_EXPORT_FAILURE,
  CREATE_DATA_EXPORT_REQUEST,
  CREATE_DATA_EXPORT_SUCCESS,
  ERASE_DATA_EXPORT_FAILURE,
  ERASE_DATA_EXPORT_REQUEST,
  ERASE_DATA_EXPORT_SUCCESS,
  FETCH_DATA_EXPORT_LIST_FAILURE,
  FETCH_DATA_EXPORT_LIST_REQUEST,
  FETCH_DATA_EXPORT_LIST_SUCCESS,
  RESEND_DATA_EXPORT_KEY_FAILURE,
  RESEND_DATA_EXPORT_KEY_REQUEST,
  RESEND_DATA_EXPORT_KEY_SUCCESS,
  UPDATE_DATA_EXPORT_FAILURE,
  UPDATE_DATA_EXPORT_REQUEST,
  UPDATE_DATA_EXPORT_SUCCESS,
} from './dataExport.constants'

export {
  CREATE_DATA_EXPORT_REQUEST,
  CREATE_DATA_EXPORT_SUCCESS,
  CREATE_DATA_EXPORT_FAILURE,
  UPDATE_DATA_EXPORT_REQUEST,
  UPDATE_DATA_EXPORT_SUCCESS,
  UPDATE_DATA_EXPORT_FAILURE,
  ERASE_DATA_EXPORT_REQUEST,
  ERASE_DATA_EXPORT_SUCCESS,
  ERASE_DATA_EXPORT_FAILURE,
  RESEND_DATA_EXPORT_KEY_REQUEST,
  RESEND_DATA_EXPORT_KEY_SUCCESS,
  RESEND_DATA_EXPORT_KEY_FAILURE,
  FETCH_DATA_EXPORT_LIST_REQUEST,
  FETCH_DATA_EXPORT_LIST_SUCCESS,
  FETCH_DATA_EXPORT_LIST_FAILURE,
}

export const fetchDataExportList = (request) => (dispatch, getState) => {
  if (getIsFetching(getState())) {
    return Promise.resolve()
  }

  dispatch({ type: FETCH_DATA_EXPORT_LIST_REQUEST })

  let requestParams = request

  if (!request || !request.sortColumn) {
    const sortOrder = getSortOrder(getState())

    requestParams = { ...request, ...sortOrder }
  }

  return api.fetchDataExportList(requestParams).then(
    (response) => {
      dispatch({
        type: FETCH_DATA_EXPORT_LIST_SUCCESS,
        response: response,
        sortColumn: requestParams.sortColumn,
        sortDirection: requestParams.sortDirection,
      })
    },
    (errorResponse) => {
      dispatch({
        type: FETCH_DATA_EXPORT_LIST_FAILURE,
        payload: errorResponse,
      })

      return Promise.reject()
    }
  )
}

export const createDataExport = (dataExport) => (dispatch, getState) => {
  if (getIsFetching(getState())) {
    return Promise.resolve()
  }

  dispatch({ type: CREATE_DATA_EXPORT_REQUEST })

  return api.createDataExport(dataExport).then(
    (response) => {
      return dispatch({
        type: CREATE_DATA_EXPORT_SUCCESS,
        response: response,
      })
    },
    (errorResponse) => {
      dispatch({
        type: CREATE_DATA_EXPORT_FAILURE,
        payload: errorResponse,
      })
      return Promise.reject()
    }
  )
}

export const eraseDataExport = (terminationId) => (dispatch, getState) => {
  if (getIsFetching(getState())) {
    return Promise.resolve()
  }

  dispatch({ type: ERASE_DATA_EXPORT_REQUEST })

  return api.eraseDataExport(terminationId).then(
    (response) => {
      return dispatch({
        type: ERASE_DATA_EXPORT_SUCCESS,
        response: response,
      })
    },
    (errorResponse) => {
      dispatch({
        type: ERASE_DATA_EXPORT_FAILURE,
        payload: errorResponse,
      })
      return Promise.reject()
    }
  )
}

export const updateDataExport = (terminationId, dataExport) => (dispatch, getState) => {
  if (getIsFetching(getState())) {
    return Promise.resolve()
  }

  dispatch({ type: UPDATE_DATA_EXPORT_REQUEST })

  return api.updateDataExport(terminationId, dataExport).then(
    (response) => {
      return dispatch({
        type: UPDATE_DATA_EXPORT_SUCCESS,
        response: response,
      })
    },
    (errorResponse) => {
      dispatch({
        type: UPDATE_DATA_EXPORT_FAILURE,
        payload: errorResponse,
      })
      return Promise.reject()
    }
  )
}

export const resendDataExportKey = (terminationId) => (dispatch, getState) => {
  if (getIsFetching(getState())) {
    return Promise.resolve()
  }

  dispatch({ type: RESEND_DATA_EXPORT_KEY_REQUEST })

  return api.resendDataExportKey(terminationId).then(
    (response) => {
      return dispatch({
        type: RESEND_DATA_EXPORT_KEY_SUCCESS,
        response: response,
      })
    },
    (errorResponse) => {
      dispatch({
        type: RESEND_DATA_EXPORT_KEY_FAILURE,
        payload: errorResponse,
      })
      return Promise.reject()
    }
  )
}
