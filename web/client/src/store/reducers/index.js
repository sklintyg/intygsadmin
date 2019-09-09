import {combineReducers} from 'redux'
import user from './user'
import {connectRouter} from 'connected-react-router'
import modal from './modal'
import sessionPoll from './sessionPoll'
import appConfig from './appConfig'
import bannerList from './bannerList.reducer'
import banner from './banner'
import integratedUnits from './integratedUnits'
import intygInfo from './intygInfo'
import intygInfoList from './intygInfoList'

const appReducer = (history) =>
  combineReducers({
    user,
    router: connectRouter(history),
    modal,
    sessionPoll,
    appConfig,
    bannerList,
    banner,
    integratedUnits,
    intygInfo,
    intygInfoList
  })

const reducers = (history) => (state, action) => {
  if (action.payload && action.payload.response && action.payload.response.status === 401) {
    state = undefined
  }
  return appReducer(history)(state, action)
}

export default reducers
