import React from 'react'
import {NavLink} from 'react-router-dom'
import {compose} from 'recompose'
import {connect} from 'react-redux'
import {Button} from 'reactstrap'

// Links to easily navigate between sections in dev mode
const TestLinks = ({ sessionState }) => {

  const clearBannerCookie = () => {
    localStorage.removeItem('COOKIE_BANNER_ACCEPTED');
  }

  return (
    <nav>
      <a href="/welcome.html">welcome</a> | <NavLink exact to="/">start</NavLink> | <NavLink to="/valj-enhet">valj-enhet</NavLink> | <Button onClick={()=>clearBannerCookie()}>Clear cookiebanner</Button> |
      <span> session-status: {JSON.stringify(sessionState)}</span>
    </nav>
  )
}
const mapStateToProps = (state) => ({
  sessionState: state.sessionPoll.sessionState,
})

export default compose(
  connect(
    mapStateToProps,
    null
  )
)(TestLinks)
