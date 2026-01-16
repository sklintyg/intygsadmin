import * as api from '../../api/users.api'
import { getIsFetching, getSortOrder } from '../reducers/users'
import {
  CREATE_USER_FAILURE,
  CREATE_USER_REQUEST,
  CREATE_USER_SUCCESS,
  FETCH_USERS_LIST_FAILURE,
  FETCH_USERS_LIST_REQUEST,
  FETCH_USERS_LIST_SUCCESS,
  REMOVE_USER_FAILURE,
  REMOVE_USER_REQUEST,
  REMOVE_USER_SUCCESS,
  UPDATE_USER_FAILURE,
  UPDATE_USER_REQUEST,
  UPDATE_USER_SUCCESS,
  USER_CLEAR_ERROR,
} from './users.constants'

export {
  FETCH_USERS_LIST_REQUEST,
  FETCH_USERS_LIST_SUCCESS,
  FETCH_USERS_LIST_FAILURE,
  REMOVE_USER_REQUEST,
  REMOVE_USER_SUCCESS,
  REMOVE_USER_FAILURE,
  CREATE_USER_REQUEST,
  CREATE_USER_SUCCESS,
  CREATE_USER_FAILURE,
  UPDATE_USER_REQUEST,
  UPDATE_USER_SUCCESS,
  UPDATE_USER_FAILURE,
  USER_CLEAR_ERROR,
}

export const fetchUsersList = (request) => (dispatch, getState) => {
  if (getIsFetching(getState())) {
    return Promise.resolve()
  }

  dispatch({
    type: FETCH_USERS_LIST_REQUEST,
  })

  let requestParams = request

  if (!request || !request.sortColumn) {
    const sortOrder = getSortOrder(getState())

    requestParams = {
      ...request,
      ...sortOrder,
    }
  }

  return api.fetchUsersList(requestParams).then(
    (response) => {
      return dispatch({
        type: FETCH_USERS_LIST_SUCCESS,
        response: response,
        sortColumn: requestParams.sortColumn,
        sortDirection: requestParams.sortDirection,
      })
    },
    (errorResponse) => {
      return dispatch({
        type: FETCH_USERS_LIST_FAILURE,
        payload: errorResponse,
      })
    }
  )
}

export const removeUser = (id) => (dispatch, getState) => {
  if (getIsFetching(getState())) {
    return Promise.resolve()
  }

  dispatch({
    type: REMOVE_USER_REQUEST,
  })

  return api.removeUser(id).then(
    (response) => {
      return dispatch({
        type: REMOVE_USER_SUCCESS,
        response: response,
      })
    },
    (errorResponse) => {
      dispatch({
        type: REMOVE_USER_FAILURE,
        payload: errorResponse,
      })

      throw errorResponse
    }
  )
}

export const createUser = (user) => (dispatch, getState) => {
  if (getIsFetching(getState())) {
    return Promise.resolve()
  }

  dispatch({
    type: CREATE_USER_REQUEST,
  })

  return api.createUser(user).then(
    (response) => {
      return dispatch({
        type: CREATE_USER_SUCCESS,
        response: response,
      })
    },
    (errorResponse) => {
      dispatch({
        type: CREATE_USER_FAILURE,
        payload: errorResponse,
      })
      return Promise.reject()
    }
  )
}

export const updateUser = (user, id) => (dispatch, getState) => {
  if (getIsFetching(getState())) {
    return Promise.resolve()
  }

  dispatch({
    type: UPDATE_USER_REQUEST,
  })

  return api.updateUser(user, id).then(
    (response) => {
      return dispatch({
        type: UPDATE_USER_SUCCESS,
        response: response,
      })
    },
    (errorResponse) => {
      dispatch({
        type: UPDATE_USER_FAILURE,
        payload: errorResponse,
      })

      return Promise.reject()
    }
  )
}

export const clearError = () => ({
  type: USER_CLEAR_ERROR,
})
