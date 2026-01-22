import React from 'react'
import { NavLink } from 'react-router-dom'
import { useSelector } from 'react-redux'

const TestLinks = () => {
  const sessionState = useSelector((state) => state.sessionPoll.sessionState)

  return (
    <nav>
      <a href="/welcome.html">welcome</a> | <NavLink to="/">start</NavLink> | <NavLink to="/banner">banners</NavLink> |
      <span> session-status: {JSON.stringify(sessionState)}</span>
    </nav>
  )
}

export default TestLinks
