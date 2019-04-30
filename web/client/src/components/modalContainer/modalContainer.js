import {connect} from "react-redux";
import {closeModal, openModal} from "../../store/actions/modal";

const mapStateToProps = (modalId) => (state) => {
  return {
    isOpen: !!state.modal[modalId],
    data: state.modal[modalId + 'Data']
  }
};
// expose selected dispatchable methods to App props
const mapDispatchToProps = (modalId) => (dispatch) => {
  return {
    handleOpen: () => dispatch(openModal(modalId)),
    handleClose: () => dispatch(closeModal(modalId))
  }
};

const modalContainer = (modalId) =>
  connect(mapStateToProps(modalId), mapDispatchToProps(modalId));

export default modalContainer;
