import React, {Fragment} from 'react'
import {HashRouter, Switch} from 'react-router-dom'
import HomePage from './pages/IndexPage'
import BannerPage from './pages/BannerPage'
import IntegratedUnitsPage from './pages/IntegratedUnitsPage'
import PrivatePractitionerPage from './pages/PrivatePractitionerPage'
import Header from './components/header'
import {getUser} from './store/actions/user'
import {connect} from 'react-redux'
import {compose, lifecycle} from 'recompose'
import SecuredRoute from './components/auth/SecuredRoute'
import UnsecuredRoute from './components/auth/UnsecuredRoute'
import {history} from './store/configureStore'
import {ConnectedRouter} from 'connected-react-router'
import {closeAllModals} from './store/actions/modal'
import ErrorPage from './pages/ErrorPage'
import ErrorModal from './components/errorModal'
import TestLinks from './components/TestLinks/TestLinks'
import SessionPoller from './components/sessionPoller'
import {fetchAppConfig} from './store/actions/appConfig'
import IntygInfoPage from "./pages/IntygInfoPage";
import UsersPage from "./pages/UsersPage";
import DataExportPage from "./pages/DataExportPage";
import ResendPage from "./pages/ResendPage";

const App = () => {
  return (
    <ConnectedRouter history={history}>
      <HashRouter>
        <Fragment>
          <SessionPoller />
          {process.env.NODE_ENV !== 'production' && <TestLinks />}
          <Header />
          <ErrorModal />
          <Switch>
            <UnsecuredRoute exact path="/" component={HomePage} />
            <UnsecuredRoute path="/loggedout/:code" component={HomePage} />
            <SecuredRoute path="/banner" component={BannerPage} />
            <SecuredRoute path="/integratedUnits" component={IntegratedUnitsPage} />
            <SecuredRoute path="/privatePractitioner" component={PrivatePractitionerPage} />
            <SecuredRoute path="/intygInfo" component={IntygInfoPage} />
            <SecuredRoute path="/administratorer" component={UsersPage} />
            <SecuredRoute path="/dataExport" component={DataExportPage} />
            <SecuredRoute path="/resend" component={ResendPage} />
            <UnsecuredRoute path="/exit/:errorCode/:logId?" isErrorPage={true} component={ErrorPage} />
          </Switch>
        </Fragment>
      </HashRouter>
    </ConnectedRouter>
  )
}

const lifeCycleValues = {
  UNSAFE_componentWillMount() {
    this.props.fetchAppConfig()
    this.props.getUser()
  },
  componentDidMount() {
    this.unlisten = history.listen(() => {
      this.props.closeAllModals()
    })
  },
  componentWillUnmount() {
    this.unlisten()
  },
}

// expose selected dispachable methods to App props
const mapDispatchToProps = (dispatch, ownProps) => {
  return {
    fetchAppConfig: () => dispatch(fetchAppConfig()),
    getUser: () => dispatch(getUser()),
    closeAllModals: () => dispatch(closeAllModals()),
  }
}

// enhance APP using compose with connect and lifecycle so we can use them in APp
export default compose(
  connect(
    null,
    mapDispatchToProps
  ),
  lifecycle(lifeCycleValues)
)(App)
