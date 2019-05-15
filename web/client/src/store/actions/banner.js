import * as api from '../../api/bannerApi'
import { getIsFetching } from '../reducers/banner'

export const CREATE_BANNER_REQUEST = 'CREATE_BANNER_REQUEST'
export const CREATE_BANNER_SUCCESS = 'CREATE_BANNER_SUCCESS'
export const CREATE_BANNER_FAILURE = 'CREATE_BANNER_FAILURE'

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
