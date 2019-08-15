import {connect} from 'react-redux'
import {compose, lifecycle} from 'recompose'
import {requestPollUpdate, startPoll, stopPoll} from '../../store/actions/sessionPoll'

const SessionPollerContainer = () => {
  //This is a non-visual component
  return null
}

const lifeCycleValues = {
  componentDidMount() {
    if (this.props.isAuthenticated) {
      this.props.startPolling()
    }
  },

  componentDidUpdate() {
    if (this.props.isAuthenticated) {
      this.props.requestPollUpdateNow()
      this.props.startPolling()
    }
  },

  componentWillUnmount() {
    this.props.stopPolling()
  },
}

const mapDispatchToProps = (dispatch) => ({
  requestPollUpdateNow: () => dispatch(requestPollUpdate()),
  startPolling: () => dispatch(startPoll()),
  stopPolling: () => dispatch(stopPoll()),
})

const mapStateToProps = (state) => ({
  isAuthenticated: state.user.isAuthenticated,
})

export default compose(
  connect(
    mapStateToProps,
    mapDispatchToProps
  ),
  lifecycle(lifeCycleValues)
)(SessionPollerContainer)
