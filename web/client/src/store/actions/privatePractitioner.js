import * as api from '../../api/privatePractitioner.api'
import {getIsFetching, getIsFetchingPrivatePractitionerFile} from '../reducers/privatePractitioner'

export const FETCH_PRIVATE_PRACTITIONER_REQUEST = 'FETCH_PRIVATE_PRACTITIONER_REQUEST'
export const FETCH_PRIVATE_PRACTITIONER_SUCCESS = 'FETCH_PRIVATE_PRACTITIONER_SUCCESS'
export const FETCH_PRIVATE_PRACTITIONER_FAILURE = 'FETCH_PRIVATE_PRACTITIONER_FAILURE'

export const FETCH_PRIVATE_PRACTITIONER_FILE_REQUEST = 'FETCH_PRIVATE_PRACTITIONER_FILE_REQUEST'
export const FETCH_PRIVATE_PRACTITIONER_FILE_SUCCESS = 'FETCH_PRIVATE_PRACTITIONER_FILE_SUCCESS'
export const FETCH_PRIVATE_PRACTITIONER_FILE_FAILURE = 'FETCH_PRIVATE_PRACTITIONER_FILE_FAILURE'

export const fetchPrivatePractitioner = (hsaId) => (dispatch, getState) => {
  if (getIsFetching(getState())) {
    return Promise.resolve()
  }

  dispatch({
    type: FETCH_PRIVATE_PRACTITIONER_REQUEST,
  })

  return api.fetchPrivatePractitioner(hsaId).then(
    (response) => {
      return dispatch({
        type: FETCH_PRIVATE_PRACTITIONER_SUCCESS,
        response: response,
      })
    },
    (errorResponse) => {
      return dispatch({
        type: FETCH_PRIVATE_PRACTITIONER_FAILURE,
        payload: errorResponse,
      })
    }
  )
}

export const fetchPrivatePractitionerFile = () => (dispatch, getState) => {
  if (getIsFetchingPrivatePractitionerFile(getState())) {
    return Promise.resolve()
  }

  dispatch({
    type: FETCH_PRIVATE_PRACTITIONER_FILE_REQUEST,
  })

  return api.fetchPrivatePractitionerFile().then(
    (response) => {
      dispatch({
        type: FETCH_PRIVATE_PRACTITIONER_FILE_SUCCESS,
      })

      return response
    },
    (errorResponse) => {
      return dispatch({
        type: FETCH_PRIVATE_PRACTITIONER_FILE_FAILURE,
        payload: errorResponse,
      })
    }
  )
}
