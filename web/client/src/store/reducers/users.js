import { combineReducers } from 'redux'
import { buildClientError } from './util'
import * as ActionConstants from '../actions/users.constants'

export const UsersListDefaultState = {
  content: [],
  pageIndex: 0,
  start: 0,
  end: 0,
  numberOfElements: 0,
  totalElements: 0,
  sortColumn: 'createdAt',
  sortDirection: 'DESC',
}

const usersList = (state = UsersListDefaultState, action) => {
  switch (action.type) {
    case ActionConstants.FETCH_USERS_LIST_SUCCESS:
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
    case ActionConstants.FETCH_USERS_LIST_FAILURE:
      return UsersListDefaultState
    default:
      return state
  }
}

const isFetching = (state = false, action) => {
  switch (action.type) {
    case ActionConstants.FETCH_USERS_LIST_REQUEST:
      return true
    case ActionConstants.FETCH_USERS_LIST_SUCCESS:
    case ActionConstants.FETCH_USERS_LIST_FAILURE:
      return false
    default:
      return state
  }
}

const errorMessage = (state = null, action) => {
  switch (action.type) {
    case ActionConstants.FETCH_USERS_LIST_FAILURE:
      return buildClientError(action.payload, 'error.usersList').message
    case ActionConstants.FETCH_USERS_LIST_REQUEST:
    case ActionConstants.FETCH_USERS_LIST_SUCCESS:
      return null
    default:
      return state
  }
}

const errorMessageCreateUpdate = (state = null, action) => {
  switch (action.type) {
    case ActionConstants.UPDATE_USER_FAILURE:
      return buildClientError(action.payload, 'error.userUpdate').message
    case ActionConstants.CREATE_USER_FAILURE:
      return buildClientError(action.payload, 'error.userCreate').message
    case ActionConstants.UPDATE_USER_REQUEST:
    case ActionConstants.UPDATE_USER_SUCCESS:
    case ActionConstants.CREATE_USER_REQUEST:
    case ActionConstants.CREATE_USER_SUCCESS:
    case ActionConstants.USER_CLEAR_ERROR:
      return null
    default:
      return state
  }
}

export default combineReducers({
  usersList,
  isFetching,
  errorMessage,
  errorMessageCreateUpdate,
})

export const getUsersList = (state) => state.users.usersList

export const getIsFetching = (state) => state.users.isFetching

export const getErrorMessage = (state) => state.users.errorMessage

export const getErrorMessageModal = (state) => state.users.errorMessageCreateUpdate

export const getSortOrder = (state) => {
  return {
    sortColumn: state.users.usersList.sortColumn,
    sortDirection: state.users.usersList.sortDirection,
  }
}
