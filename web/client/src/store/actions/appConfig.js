import * as api from '../../api/appConfigApi'
import {getIsFetching} from '../reducers/appConfig'

export const FETCH_APPCONFIG_REQUEST = 'FETCH_APPCONFIG_REQUEST'
export const FETCH_APPCONFIG_SUCCESS = 'FETCH_APPCONFIG_SUCCESS'
export const FETCH_APPCONFIG_FAILURE = 'FETCH_APPCONFIG_FAILURE'

export const fetchAppConfig = () => (dispatch, getState) => {
  if (getIsFetching(getState())) {
    return Promise.resolve()
  }

  dispatch({
    type: FETCH_APPCONFIG_REQUEST,
  })

  return api.fetchAppConfig().then(
    (response) => {
      dispatch({
        type: FETCH_APPCONFIG_SUCCESS,
        response: response,
      })
    },
    (errorResponse) => {
      dispatch({
        type: FETCH_APPCONFIG_FAILURE,
        payload: errorResponse,
      })
    }
  )
}
