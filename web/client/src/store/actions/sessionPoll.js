import { pollSession } from '../../api/userApi'
import AppConstants from '../../AppConstants'

export const GET_POLL_REQUEST = 'GET_POLL_REQUEST'
export const GET_POLL_SUCCESS = 'GET_POLL_SUCCESS'
export const GET_POLL_FAIL = 'GET_POLL_FAIL'
export const SET_POLL_HANDLE = 'SET_POLL_HANDLE'

const executePollRequest = (dispatch) => {
  dispatch({
    type: GET_POLL_REQUEST,
  })

  const timeoutRedirect = () => {
    console.warn('No longer authenticated - redirecting to start view')
    window.location.href = AppConstants.TIMEOUT_REDIRECT_URL
    //We dont want to just change route, but force a full app reload to clear all state, making sure frontend-state is in sync with backend state.
    window.location.reload()
  }

  return pollSession()
    .then((json) => {
      dispatch({
        type: GET_POLL_SUCCESS,
        payload: json,
      })

      if (!json.sessionState.authenticated) {
        timeoutRedirect()
      }
    })
    .catch((errorResponse) => {
      timeoutRedirect()
      return dispatch({
        type: GET_POLL_FAIL,
        payload: errorResponse,
      })
    })
}

export const startPoll = () => {
  return (dispatch, getState) => {
    //Already scheduled
    if (getState().sessionPoll.handle) {
      return
    }
    const pollHandle = setInterval(() => {
      executePollRequest(dispatch)
    }, AppConstants.POLL_SESSION_INTERVAL_MS)

    dispatch({
      type: SET_POLL_HANDLE,
      payload: { handle: pollHandle },
    })
  }
}
export const requestPollUpdate = () => {
  return (dispatch) => {
    executePollRequest(dispatch)
  }
}
export const stopPoll = () => {
  return (dispatch, getState) => {
    if (getState().sessionPoll.handle) {
      clearInterval(getState().sessionPoll.handle)
    }

    dispatch({
      type: SET_POLL_HANDLE,
      payload: { handle: null },
    })
  }
}
