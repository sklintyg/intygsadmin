import { combineReducers } from 'redux'
import user from './user'
import { connectRouter } from 'connected-react-router'
import modal from './modal'
import sessionPoll from './sessionPoll'
import appConfig from './appConfig'
import bannerList from './bannerList.reducer'
import banner from './banner'
import integratedUnits from './integratedUnits'
import privatePractitioner from './privatePractitioner'
import intygInfo from './intygInfo'
import intygInfoList from './intygInfoList'
import users from './users'
import dataExport from './dataExport'

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
    privatePractitioner,
    intygInfo,
    intygInfoList,
    users,
    dataExport,
  })

const reducers = (history) => (state, action) => {
  if (action.payload && action.payload.response && action.payload.response.status === 401) {
    state = undefined
  }
  return appReducer(history)(state, action)
}

export default reducers
