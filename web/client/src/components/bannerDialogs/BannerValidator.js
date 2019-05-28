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
  if (banner.displayFrom && banner.displayTo) {
    let valid = validateFromDateBeforeToDate(banner.displayFrom, banner.displayTo, banner.displayFromTime, banner.displayToTime)
    if (valid === 'toDateBeforeFrom') {
      invalid = { ...invalid, displayFrom: ''}
      invalid = { ...invalid, displayTo: 'Ändra visningsperioden. Startdatumet ska ligga före slutdatumet.'}
    }
    if (valid === 'toTimeBeforeFrom') {
      invalid = { ...invalid, displayFromTime: ''}
      invalid = { ...invalid, displayToTime: 'Ändra visningsperioden. Starttiden ska ligga före sluttiden.'}
    }
  }
  return invalid
}

const validateDate = (value) => {
  if (typeof value === 'string' && !(value.match(/(\d{4}-(\d{2})-(\d{2}))/) && value.length === 10)) {
    return 'Ange datum i formatet åååå-mm-dd.'
  } else {
    let date = new Date(value)
    if (isNaN(date.getHours())) {
      return 'Ange ett giltigt datum.'
    }
  }
  return 'ok'
}

const validateTime = (value) => {
  if (value.match('^([0-1][0-9]|2[0-3]):([0-5][0-9])$')) {
    return 'ok'
  } else if (value.match(/(\d{2}:(\d{2}))/)) {
    return 'Ange en giltig tid.'
  }
  return 'Ange tid i formatet hh:mm'
}

const validateFromDateBeforeToDate = (fromDate, toDate, fromTime, toTime) => {
  let from = new Date(fromDate)
  let to = new Date(toDate)
  if (from.getHours && !isNaN(from.getHours()) && to.getHours && !isNaN(to.getHours())) {
    if (from > to) {
      return 'toDateBeforeFrom'
    }
    //Om datumen är samma och tid är satt, lägg på tid och verifiera igen.
    if (fromTime && toTime && validateTime(fromTime) === 'ok' && validateTime(toTime) === 'ok') {
      let fromDate = new Date(from);
      fromDate.setHours(fromTime.split(':')[0])
      fromDate.setMinutes(fromTime.split(':')[1])
      let toDate = new Date(to);
      toDate.setHours(toTime.split(':')[0])
      toDate.setMinutes(toTime.split(':')[1])
      if (fromDate > toDate) {
        return 'toTimeBeforeFrom'
      }
    }
  }

  return 'ok'
}
