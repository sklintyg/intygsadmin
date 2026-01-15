const dateShowPeriodOptions = {
  year: 'numeric',
  month: '2-digit',
  day: '2-digit',
  hour: '2-digit',
  minute: '2-digit',
}
const dateShowPeriodOptionsIncludingSeconds = {
  year: 'numeric',
  month: '2-digit',
  day: '2-digit',
  hour: '2-digit',
  minute: '2-digit',
  second: '2-digit',
}

const DisplayDateTime = ({ date, includeSeconds = false }) => {
  if (includeSeconds) {
    return new Date(date).toLocaleString('sv-SE', dateShowPeriodOptionsIncludingSeconds)
  }

  return new Date(date).toLocaleString('sv-SE', dateShowPeriodOptions)
}

export default DisplayDateTime
