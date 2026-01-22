const DisplayDate = ({ date }) => {
  return new Date(date).toLocaleDateString('sv-SE')
}

export default DisplayDate
