import {GET_USER, GET_USER_FAILURE, GET_USER_SUCCESS,} from "../actions/user";
import {buildClientError} from "./util";

const INITIAL_STATE = {
  isLoading: true,
  isAuthenticated: false,
  activeError: null
};

export default (state = INITIAL_STATE, action) => {
  switch (action.type) {
  case GET_USER:
    return {...state, isLoading: true}
  case GET_USER_SUCCESS:
    return {
      ...state,
      isLoading: false,
      isAuthenticated: true,
      activeError: null,
      name: action.payload.name,
      userRole: action.payload.intygsadminRole ? action.payload.intygsadminRole : '',
      authoritiesTree: action.payload.authoritiesTree,
      logoutUrl: action.payload.logoutUrl
    }
  case GET_USER_FAILURE:
    return {
      ...state,
      isLoading: false,
      isAuthenticated: false,
      activeError: buildClientError(action.payload, 'error.user')
    }
  default:
    return state;
  }
}
