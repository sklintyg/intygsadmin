import {compose} from "recompose";
import {connect} from "react-redux";
import Header from "./Header";

const mapStateToProps = (state, ownProps) => {
  return {
    ...state.user
  }
};
export default compose(
  connect(mapStateToProps, null)
)(Header);

