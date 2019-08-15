import PropTypes from 'prop-types'
import IaAlert from '../alert/Alert'

const FetchError = ({message}) => (
  < IaAlert
type = {alertType.ERROR} > {message} < /IaAlert>
)
;

FetchError.propTypes = {
  message: PropTypes.string
};

export default FetchError;
