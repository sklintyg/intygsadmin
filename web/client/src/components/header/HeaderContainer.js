import { connect } from 'react-redux'
import Header from './Header'

const mapStateToProps = (state, _ownProps) => {
  return {
    ...state.user,
  }
}
export default connect(mapStateToProps, null)(Header)
