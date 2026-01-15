import React, { Fragment, useEffect } from 'react'
import { HashRouter, Route, Routes, useLocation } from 'react-router-dom'
import { useDispatch } from 'react-redux'
import HomePage from './pages/IndexPage'
import BannerPage from './pages/BannerPage'
import IntegratedUnitsPage from './pages/IntegratedUnitsPage'
import PrivatePractitionerPage from './pages/PrivatePractitionerPage'
import Header from './components/header'
import { getUser } from './store/actions/user'
import SecuredRoute from './components/auth/SecuredRoute'
import UnsecuredRoute from './components/auth/UnsecuredRoute'
import { closeAllModals } from './store/actions/modal'
import ErrorPage from './pages/ErrorPage'
import ErrorModal from './components/errorModal'
import TestLinks from './components/TestLinks/TestLinks'
import SessionPoller from './components/sessionPoller'
import { fetchAppConfig } from './store/actions/appConfig'
import IntygInfoPage from './pages/IntygInfoPage'
import UsersPage from './pages/UsersPage'
import DataExportPage from './pages/DataExportPage'
import ResendPage from './pages/ResendPage'

const AppContent = () => {
  const dispatch = useDispatch()
  const location = useLocation()

  useEffect(() => {
    dispatch(fetchAppConfig())
    dispatch(getUser())
  }, [dispatch])

  useEffect(() => {
    dispatch(closeAllModals())
  }, [dispatch, location])

  return (
    <Fragment>
      <SessionPoller />
      {process.env.NODE_ENV !== 'production' && <TestLinks />}
      <Header />
      <ErrorModal />
      <Routes>
        <Route path="/" element={<UnsecuredRoute component={HomePage} exact />} />
        <Route path="/loggedout/:code" element={<UnsecuredRoute component={HomePage} />} />
        <Route path="/banner" element={<SecuredRoute component={BannerPage} />} />
        <Route path="/integratedUnits" element={<SecuredRoute component={IntegratedUnitsPage} />} />
        <Route path="/privatePractitioner" element={<SecuredRoute component={PrivatePractitionerPage} />} />
        <Route path="/intygInfo" element={<SecuredRoute component={IntygInfoPage} />} />
        <Route path="/administratorer" element={<SecuredRoute component={UsersPage} />} />
        <Route path="/dataExport" element={<SecuredRoute component={DataExportPage} />} />
        <Route path="/resend" element={<SecuredRoute component={ResendPage} />} />
        <Route path="/exit/:errorCode/:logId?" element={<UnsecuredRoute component={ErrorPage} isErrorPage />} />
      </Routes>
    </Fragment>
  )
}

const App = () => {
  return (
    <HashRouter>
      <AppContent />
    </HashRouter>
  )
}

export default App
