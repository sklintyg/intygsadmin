export const validateDateFormat = (value) => {
  if (typeof value === 'string' && !(value.match('(\\d{4}-(\\d{2})-(\\d{2}))') && value.length === 10)) {
    return 'Ange datum i formatet åååå-mm-dd.'
  } else {
    let date = new Date(value)
    if (isNaN(date.getHours())) {
      return 'Ange ett giltigt datum.'
    }
  }
  return 'ok'
}

export const validateTimeFormat = (value) => {
  if (value.match('^([0-1][0-9]|2[0-3]):([0-5][0-9])$')) {
    return 'ok'
  } else if (value.match(/(\d{2}:(\d{2}))/)) {
    return 'Ange en giltig tid.'
  }
  return 'Ange tid i formatet hh:mm'
}

export const validateFromDateBeforeToDate = (fromDate, toDate, fromTime, toTime) => {
  let from = new Date(fromDate)
  let to = new Date(toDate)
  if (from.getHours && !isNaN(from.getHours()) && to.getHours && !isNaN(to.getHours())) {
    if (from > to) {
      return 'toDateBeforeFrom'
    }
    //Om datumen är ok och tid är satt, lägg på tid och verifiera igen.
    if (fromTime && toTime && validateTimeFormat(fromTime) === 'ok' && validateTimeFormat(toTime) === 'ok') {
      from = new Date(fromDate + 'T' + fromTime)
      to = new Date(toDate + 'T' + toTime)
      if (from > to) {
        return 'toTimeBeforeFrom'
      }
    }
  }

  return 'ok'
}
