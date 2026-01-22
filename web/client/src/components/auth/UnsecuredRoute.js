import React from 'react'
import { Navigate } from 'react-router-dom'
import { useSelector } from 'react-redux'
import Loading from './Loading'
import AppConstants from '../../AppConstants'

const UnsecuredRoute = ({ component: Component, isErrorPage, ...rest }) => {
  const isAuthenticated = useSelector((state) => state.user.isAuthenticated)
  const isLoading = useSelector((state) => state.user.isLoading)

  if (isLoading) {
    return <Loading />
  }

  if (isAuthenticated && !isErrorPage) {
    return <Navigate to={AppConstants.DEFAULT_PAGE} replace />
  }

  return <Component {...rest} />
}

export default UnsecuredRoute
