import {compose} from "recompose";
import {connect} from "react-redux";
import SelectEnhet from "./SelectEnhet";
import {selectEnhet} from "../../store/actions/user";

const mapStateToProps = (state, ownProps) => {
  return {
    authoritiesTree: state.user.authoritiesTree,
    unitContext: state.user.unitContext,
    activeError: state.user.activeError
  }
};
// expose selected dispatchable methods to App props
const mapDispatchToProps = (dispatch, ownProps) => {
  return {
    selectEnhet: (hsaId) => dispatch(selectEnhet(hsaId))
  }
};
export default compose(
  connect(mapStateToProps, mapDispatchToProps)
)(SelectEnhet);

