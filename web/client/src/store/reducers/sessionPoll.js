import {combineReducers} from 'redux'
import {GET_POLL_FAIL, GET_POLL_REQUEST, GET_POLL_SUCCESS, SET_POLL_HANDLE} from '../actions/sessionPoll'

const pending = (state = false, action) => {
  switch (action.type) {
  case GET_POLL_REQUEST:
    return true
  case GET_POLL_SUCCESS:
  case GET_POLL_FAIL:
    return false
  default:
    return state
  }
}

const handle = (state = null, action) => {
  switch (action.type) {
  case SET_POLL_HANDLE:
    return action.payload
  default:
    return state
  }
}

const INITIAL_STATE = {
  hasSession: false,
  authenticated: false,
  secondsUntilExpire: 0,
}

const sessionState = (state = INITIAL_STATE, action) => {
  switch (action.type) {
  case GET_POLL_SUCCESS:
    return {
      ...state,
      hasSession: action.payload.sessionState.hasSession,
      authenticated: action.payload.sessionState.authenticated,
      secondsUntilExpire: action.payload.sessionState.secondsUntilExpire,
    }
  case GET_POLL_FAIL:
    return INITIAL_STATE
  default:
    return state
  }
}

export default combineReducers({
  pending,
  handle,
  sessionState,
})
