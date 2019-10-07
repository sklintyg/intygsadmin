import PropTypes from 'prop-types';

const DisplayDate = ({ date}) => {

  return  (
    new Date(date).toLocaleDateString('sv-SE')
  )
}

DisplayDate.propTypes = {
  date: PropTypes.string.isRequired
}

export default DisplayDate
