export const validateBanner = (banner) => {
  let invalid = {}
  if (banner.displayTo) {
    let displayToValid = validateDate(banner.displayTo)
    if (displayToValid !== 'ok') {
      invalid = { ...invalid, displayTo: displayToValid }
    }
  }
  if (banner.displayFrom) {
    let displayFromValid = validateDate(banner.displayFrom)
    if (displayFromValid !== 'ok') {
      invalid = { ...invalid, displayFrom: displayFromValid }
    }
  }
  if (banner.displayFromTime) {
    let valid = validateTime(banner.displayFromTime)
    if (valid !== 'ok') {
      invalid = { ...invalid, displayFromTime: valid }
    }
  }
  if (banner.displayToTime) {
    let valid = validateTime(banner.displayToTime)
    if (valid !== 'ok') {
      invalid = { ...invalid, displayToTime: valid }
    }
  }
  return invalid
}

const validateDate = (value) => {
  if (typeof value === 'string' && !(value.match(/(\d{4}-(\d{2})-(\d{2}))/) && value.length === 10)) {
    return 'wrongFormat'
  } else {
    let date = new Date(value)
    if (isNaN(date.getHours())) {
      return 'invalidDate'
    }
  }
  return 'ok'
}

const validateTime = (value) => {
  if (value.match('^([0-1][0-9]|2[0-3]):([0-5][0-9])$')) {
    return 'ok'
  } else if (value.match(/(\d{2}:(\d{2}))/)) {
    return 'invalidTime'
  }
  return 'invalidFormat'
}
