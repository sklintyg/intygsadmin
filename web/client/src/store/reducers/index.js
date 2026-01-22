import { combineReducers } from 'redux'
import user from './user'
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
import countStatus from './countStatus.reducer'

const appReducer = combineReducers({
  user,
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
  countStatus,
})

const rootReducer = (state, action) => {
  if (action.payload && action.payload.response && action.payload.response.status === 401) {
    state = undefined
  }
  return appReducer(state, action)
}

export default rootReducer
