import * as utils from './utils'

export const fetchIntygInfo = (intygsId) => utils.makeServerRequest(`intygInfo/${intygsId}`)

export const resendCertificateStatus = ({ certificateIds, status }) =>
  utils.makeServerPost(`status/certificates`, { certificateIds, status })

export const resendUnitsStatus = ({ unitIds, start, end, status, activationTime }) =>
  utils.makeServerPost(`status/units`, { unitIds, start, end, status, activationTime })

export const resendCaregiverStatus = ({ careGiverId, start, end, status, activationTime }) =>
  utils.makeServerPost(`/caregiver/${careGiverId}`, { start, end, status, activationTime })

export const fetchIntygInfoList = ({ pageIndex, sortColumn, sortDirection }) => {
  if (!pageIndex) {
    pageIndex = 0
  }

  if (!sortColumn) {
    sortColumn = 'createdAt'
  }

  if (!sortDirection) {
    sortDirection = 'DESC'
  }

  return utils.makeServerRequest(
    utils.buildUrlFromParams('intygInfo', {
      page: pageIndex,
      size: 10,
      sort: `${sortColumn},${sortDirection}`,
    })
  )
}
