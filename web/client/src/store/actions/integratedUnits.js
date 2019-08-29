import * as api from '../../api/integratedUnits.api'
import { getIsFetching, getIsFetchingIntegratedUnitsFile } from '../reducers/integratedUnits'

export const FETCH_INTEGRATED_UNIT_REQUEST = 'FETCH_INTEGRATED_UNIT_REQUEST'
export const FETCH_INTEGRATED_UNIT_SUCCESS = 'FETCH_INTEGRATED_UNIT_SUCCESS'
export const FETCH_INTEGRATED_UNIT_FAILURE = 'FETCH_INTEGRATED_UNIT_FAILURE'

export const FETCH_INTEGRATED_UNITS_FILE_REQUEST = 'FETCH_INTEGRATED_UNITS_FILE_REQUEST'
export const FETCH_INTEGRATED_UNITS_FILE_SUCCESS = 'FETCH_INTEGRATED_UNITS_FILE_SUCCESS'
export const FETCH_INTEGRATED_UNITS_FILE_FAILURE = 'FETCH_INTEGRATED_UNITS_FILE_FAILURE'

export const fetchIntegratedUnit = (hsaId) => (dispatch, getState) => {
  if (getIsFetching(getState())) {
    return Promise.resolve()
  }

  dispatch({
    type: FETCH_INTEGRATED_UNIT_REQUEST,
  })

  return api.fetchIntegratedUnit(hsaId).then(
    (response) => {
      return dispatch({
        type: FETCH_INTEGRATED_UNIT_SUCCESS,
        response: response,
      })
    },
    (errorResponse) => {
      dispatch({
        type: FETCH_INTEGRATED_UNIT_FAILURE,
        payload: errorResponse,
      })

      return Promise.reject();
    }
  )
}

export const fetchIntegratedUnitsFile = () => (dispatch, getState) => {
  if (getIsFetchingIntegratedUnitsFile(getState())) {
    return Promise.resolve()
  }

  dispatch({
    type: FETCH_INTEGRATED_UNITS_FILE_REQUEST,
  })

  return api.fetchIntegratedUnitsFile().then(
    (response) => {
      dispatch({
        type: FETCH_INTEGRATED_UNITS_FILE_SUCCESS,
      })

      return response
    },
    (errorResponse) => {
      dispatch({
        type: FETCH_INTEGRATED_UNITS_FILE_FAILURE,
        payload: errorResponse,
      })

      return Promise.reject();
    }
  )
}
