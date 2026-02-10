import { useEffect } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import { requestPollUpdate, startPoll, stopPoll } from '../../store/actions/sessionPoll'

const SessionPollerContainer = () => {
  const dispatch = useDispatch()
  const isAuthenticated = useSelector((state) => state.user.isAuthenticated)

  useEffect(() => {
    if (isAuthenticated) {
      dispatch(requestPollUpdate())
      dispatch(startPoll())
    }
    return () => {
      if (isAuthenticated) {
        dispatch(stopPoll())
      }
    }
  }, [isAuthenticated, dispatch])

  return null
}

export default SessionPollerContainer
