import { compose } from 'recompose'
import { connect } from 'react-redux'
import LoginOptions from './LoginOptions'
import { getErrorMessage, getIsFetching, getSettings} from '../../store/reducers/appConfig'

const mapStateToProps = (state, ownProps) => {
  return {
    settings: getSettings(state),
    isFetching: getIsFetching(state),
    errorMessage: getErrorMessage(state),
  }
}

export default compose(
  connect(
    mapStateToProps,
    null
  )
)(LoginOptions)
