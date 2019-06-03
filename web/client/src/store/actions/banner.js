import * as api from '../../api/banner.api'
import { getIsFetching } from '../reducers/banner'

export const CREATE_BANNER_REQUEST = 'CREATE_BANNER_REQUEST'
export const CREATE_BANNER_SUCCESS = 'CREATE_BANNER_SUCCESS'
export const CREATE_BANNER_FAILURE = 'CREATE_BANNER_FAILURE'

export const REMOVE_BANNER_REQUEST = 'REMOVE_BANNER_REQUEST'
export const REMOVE_BANNER_SUCCESS = 'REMOVE_BANNER_SUCCESS'
export const REMOVE_BANNER_FAILURE = 'REMOVE_BANNER_FAILURE'

export const UPDATE_BANNER_REQUEST = 'UPDATE_BANNER_REQUEST'
export const UPDATE_BANNER_SUCCESS = 'UPDATE_BANNER_SUCCESS'
export const UPDATE_BANNER_FAILURE = 'UPDATE_BANNER_FAILURE'

export const FETCH_FUTURE_REQUEST = 'FETCH_FUTURE_REQUEST'
export const FETCH_FUTURE_SUCCESS = 'FETCH_FUTURE_SUCCESS'
export const FETCH_FUTURE_FAILURE = 'FETCH_FUTURE_FAILURE'

export const createBanner = (banner) => (dispatch, getState) => {
  if (getIsFetching(getState())) {
    return Promise.resolve()
  }

  dispatch({
    type: CREATE_BANNER_REQUEST,
  })

  return api.createBanner(banner).then(
    (response) => {
      dispatch({
        type: CREATE_BANNER_SUCCESS,
        response: response,
      })
    },
    (errorResponse) => {
      dispatch({
        type: CREATE_BANNER_FAILURE,
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
    type: REMOVE_BANNER_REQUEST,
  })

  return api.removeBanner(id).then(
    (response) => {
      dispatch({
        type: REMOVE_BANNER_SUCCESS,
        response: response,
      })
    },
    (errorResponse) => {
      dispatch({
        type: REMOVE_BANNER_FAILURE,
        payload: errorResponse,
      })

      throw errorResponse
    }
  )
}


export const updateBanner = (banner, id) => (dispatch, getState) => {
  if (getIsFetching(getState())) {
    return Promise.resolve()
  }

  dispatch({
    type: UPDATE_BANNER_REQUEST,
  })

  return api.updateBanner(banner, id).then(
    (response) => {
      dispatch({
        type: UPDATE_BANNER_SUCCESS,
        response: response,
      })
    },
    (errorResponse) => {
      dispatch({
        type: UPDATE_BANNER_FAILURE,
        payload: errorResponse,
      })
    }
  )
}

export const fetchFutureBanners = (application) => (dispatch, getState) => {
  if (getIsFetching(getState())) {
    return Promise.resolve()
  }

  dispatch({
    type: FETCH_FUTURE_REQUEST,
  })

  return api.fetchFutureBanners(application).then(
    (response) => {
      dispatch({
        type: FETCH_FUTURE_SUCCESS,
        response: response,
      })
    },
    (errorResponse) => {
      dispatch({
        type: FETCH_FUTURE_FAILURE,
        payload: errorResponse,
      })
    }
  )
}
