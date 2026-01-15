import React from 'react'
import { Navigate } from 'react-router-dom'
import { useSelector } from 'react-redux'
import Loading from './Loading'

const SecuredRoute = ({ component: Component, ...rest }) => {
  const isAuthenticated = useSelector((state) => state.user.isAuthenticated)
  const isLoading = useSelector((state) => state.user.isLoading)

  if (!isAuthenticated && isLoading) {
    return <Loading />
  }

  if (!isAuthenticated) {
    return <Navigate to="/" replace />
  }

  return <Component {...rest} />
}

export default SecuredRoute
