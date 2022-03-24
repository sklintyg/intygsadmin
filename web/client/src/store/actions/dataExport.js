import * as api from '../../api/dataExport.api'
import { getIsFetching } from '../reducers/dataExport'
import { getSortOrder } from '../reducers/dataExport'

export const CREATE_DATA_EXPORT_REQUEST = 'CREATE_DATA_EXPORT_REQUEST'
export const CREATE_DATA_EXPORT_SUCCESS = 'CREATE_DATA_EXPORT_SUCCESS'
export const CREATE_DATA_EXPORT_FAILURE = 'CREATE_DATA_EXPORT_FAILURE'

export const REMOVE_DATA_EXPORT_REQUEST = 'REMOVE_DATA_EXPORT_REQUEST'
export const REMOVE_DATA_EXPORT_SUCCESS = 'REMOVE_DATA_EXPORT_SUCCESS'
export const REMOVE_DATA_EXPORT_FAILURE = 'REMOVE_DATA_EXPORT_FAILURE'

export const UPDATE_DATA_EXPORT_REQUEST = 'UPDATE_DATA_EXPORT_REQUEST'
export const UPDATE_DATA_EXPORT_SUCCESS = 'UPDATE_DATA_EXPORT_SUCCESS'
export const UPDATE_DATA_EXPORT_FAILURE = 'UPDATE_DATA_EXPORT_FAILURE'

export const FETCH_DATA_EXPORT_REQUEST = 'FETCH_DATA_EXPORT_REQUEST'
export const FETCH_DATA_EXPORT_SUCCESS = 'FETCH_DATA_EXPORT_SUCCESS'
export const FETCH_DATA_EXPORT_FAILURE = 'FETCH_DATA_EXPORT_FAILURE'

export const FETCH_DATA_EXPORT_LIST_REQUEST = 'FETCH_DATA_EXPORT_LIST_REQUEST'
export const FETCH_DATA_EXPORT_LIST_SUCCESS = 'FETCH_DATA_EXPORT_LIST_SUCCESS'
export const FETCH_DATA_EXPORT_LIST_FAILURE = 'FETCH_DATA_EXPORT_LIST_FAILURE'

export const FETCH_DATA_EXPORT_STATUS_LIST_REQUEST = 'FETCH_DATA_EXPORT_STATUS_LIST_REQUEST'
export const FETCH_DATA_EXPORT_STATUS_LIST_SUCCESS = 'FETCH_DATA_EXPORT_STATUS_LIST_SUCCESS'
export const FETCH_DATA_EXPORT_STATUS_LIST_FAILURE = 'FETCH_DATA_EXPORT_STATUS_LIST_FAILURE'

export const fetchDataExportList = (request) => (dispatch, getState) => {
  if (getIsFetching(getState())) {
    return Promise.resolve()
  }

  dispatch({
    type: FETCH_DATA_EXPORT_LIST_REQUEST,
  })

  let requestParams = request

  if (!request || !request.sortColumn) {
    const sortOrder = getSortOrder(getState())

    requestParams = {
      ...request,
      ...sortOrder,
    }
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

export const fetchDataExportStatusList = (dataExportId) => (dispatch, getState) => {
  if (getIsFetching(getState())) {
    return Promise.resolve()
  }

  dispatch({
    type: FETCH_DATA_EXPORT_STATUS_LIST_REQUEST,
  })

  return api.fetchDataExportStatusList(dataExportId).then(
    (response) => {
      dispatch({
        type: FETCH_DATA_EXPORT_STATUS_LIST_SUCCESS,
        response: response,
      })
    },
    (errorResponse) => {
      dispatch({
        type: FETCH_DATA_EXPORT_STATUS_LIST_FAILURE,
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

  dispatch({
    type: CREATE_DATA_EXPORT_REQUEST,
  })

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

export const removeDataExport = (id) => (dispatch, getState) => {
  if (getIsFetching(getState())) {
    return Promise.resolve()
  }

  dispatch({
    type: REMOVE_DATA_EXPORT_REQUEST,
  })

  return api.removeDataExport(id).then(
    (response) => {
      return dispatch({
        type: REMOVE_DATA_EXPORT_SUCCESS,
        response: response,
      })
    },
    (errorResponse) => {
      dispatch({
        type: REMOVE_DATA_EXPORT_FAILURE,
        payload: errorResponse,
      })

      throw errorResponse
    }
  )
}

export const updateDataExport = (dataExport, id) => (dispatch, getState) => {
  if (getIsFetching(getState())) {
    return Promise.resolve()
  }

  dispatch({
    type: UPDATE_DATA_EXPORT_REQUEST,
  })

  return api.updateDataExport(dataExport, id).then(
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
