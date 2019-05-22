import * as api from '../../api/banner.api'
import { getIsFetching } from '../reducers/banner'

export const CREATE_BANNER_REQUEST = 'CREATE_BANNER_REQUEST'
export const CREATE_BANNER_SUCCESS = 'CREATE_BANNER_SUCCESS'
export const CREATE_BANNER_FAILURE = 'CREATE_BANNER_FAILURE'

export const REMOVE_BANNER_REQUEST = 'REMOVE_BANNER_REQUEST'
export const REMOVE_BANNER_SUCCESS = 'REMOVE_BANNER_SUCCESS'
export const REMOVE_BANNER_FAILURE = 'REMOVE_BANNER_FAILURE'

export const createBanner = (banner) => (dispatch, getState) => {
  if (getIsFetching(getState())) {
    return Promise.resolve()
  }

  dispatch({
    type: 'CREATE_BANNER_REQUEST',
  })

  return api.createBanner(banner).then(
    (response) => {
      dispatch({
        type: 'CREATE_BANNER_SUCCESS',
        response: response,
      })
    },
    (errorResponse) => {
      dispatch({
        type: 'CREATE_BANNER_FAILURE',
        payload: errorResponse,
      })
    }
  )
}

export const removeBanner = (id) => (dispatch, getState) => {
  if (getIsFetching(getState())) {
    return Promise.resolve()
  }

  dispatch({
    type: 'REMOVE_BANNER_REQUEST',
  })

  return api.removeBanner(id).then(
    (response) => {
      dispatch({
        type: 'REMOVE_BANNER_SUCCESS',
        response: response,
      })
    },
    (errorResponse) => {
      dispatch({
        type: 'REMOVE_BANNER_FAILURE',
        payload: errorResponse,
      })

      throw errorResponse
    }
  )
}
