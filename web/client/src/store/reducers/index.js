import {combineReducers} from 'redux'
import user from './user'
import {connectRouter} from 'connected-react-router'
import modal from './modal'
import sessionPoll from './sessionPoll'
import cookieBanner from './cookieBanner'
import appConfig from './appConfig'

const appReducer = (history) =>
  combineReducers({
    user,
    router: connectRouter(history),
    modal,
    sessionPoll,
    cookieBanner,
    appConfig
  })

const reducers = (history) => (state, action) => {
  if (action.payload && action.payload.response && action.payload.response.status === 401) {
    state = undefined
  }
  return appReducer(history)(state, action)
}

export default reducers
