import { validateDateFormat, validateTimeFormat, validateFromDateBeforeToDate } from '../../utils/validation'

export const validateBanner = (banner, futureBanners) => {
  let validations = {}
  if (banner.displayTo) {
    let displayToValid = validateDateFormat(banner.displayTo)
    if (displayToValid !== 'ok') {
      validations = { ...validations, displayTo: displayToValid }
    }
  }
  if (banner.displayFrom) {
    let displayFromValid = validateDateFormat(banner.displayFrom)
    if (displayFromValid !== 'ok') {
      validations = { ...validations, displayFrom: displayFromValid }
    }
  }
  if (banner.displayFromTime) {
    let valid = validateTimeFormat(banner.displayFromTime)
    if (valid !== 'ok') {
      validations = { ...validations, displayFromTime: valid }
    }
  }
  if (banner.displayToTime) {
    let valid = validateTimeFormat(banner.displayToTime)
    if (valid !== 'ok') {
      validations = { ...validations, displayToTime: valid }
    }
  }
  if (banner.displayFrom && banner.displayTo) {
    let validFromBeforeTo = validateFromDateBeforeToDate(banner.displayFrom, banner.displayTo, banner.displayFromTime, banner.displayToTime)
    if (validFromBeforeTo === 'ok') {
      let validRange = validateRangeForBanner(
        banner.displayFrom,
        banner.displayTo,
        banner.displayFromTime,
        banner.displayToTime,
        futureBanners,
        banner.id
      )
      if (validRange !== 'ok') {
        validations = { ...validations, displayTo: validRange, displayFrom: '', displayToTime: '', displayFromTime: '' }
      }
    }
    if (validFromBeforeTo === 'toDateBeforeFrom') {
      validations = { ...validations, displayFrom: '', displayTo: 'Ändra visningsperioden. Startdatumet ska ligga före slutdatumet.' }
    }
    if (validFromBeforeTo === 'toTimeBeforeFrom') {
      validations = { ...validations, displayFromTime: '', displayToTime: 'Ändra visningsperioden. Starttiden ska ligga före sluttiden.' }
    }
  }
  return validations
}

const validateRangeForBanner = (fromDate, toDate, fromTime, toTime, futureBanners, bannerId) => {
  let from = new Date(fromDate + 'T' + fromTime)
  let to = new Date(toDate + 'T' + toTime)
  let today = new Date()
  if (to < today) {
    return 'Ändra visningsperioden. Den angivna perioden har redan infallit.'
  }

  if (futureBanners.length > 0) {
    for (var i = 0; i < futureBanners.length; i++) {
      let bf = new Date(futureBanners[i].displayFrom)
      let bt = new Date(futureBanners[i].displayTo)
      if (futureBanners[i].id !== bannerId && bt >= from && bf <= to) {
        return 'Ändra visningsperioden. Det finns redan en driftbanner som infaller i denna period.'
      }
    }
  }

  return 'ok'
}
