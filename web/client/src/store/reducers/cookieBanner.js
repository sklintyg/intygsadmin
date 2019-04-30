import { ACCEPT_COOKIE_BANNER } from '../actions/cookieBanner'

const INITIAL_STATE = localStorage.getItem('COOKIE_BANNER_ACCEPTED')

export default (state = INITIAL_STATE, action) => {
  switch (action.type) {
    case ACCEPT_COOKIE_BANNER:
      if(action.payload !== localStorage.getItem('COOKIE_BANNER_ACCEPTED')) {
        localStorage.setItem('COOKIE_BANNER_ACCEPTED', action.payload)
      }
      return action.payload
    default:
      return state
  }
}

export const getCookieBannerAccepted = (state) => state.cookieBanner
