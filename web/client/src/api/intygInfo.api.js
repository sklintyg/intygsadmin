import * as utils from './utils'

export const fetchIntygInfo = (intygsId) => utils.makeServerRequest(`intygInfo/${intygsId}`)

export const resendNotificationStatus = (notificationId) => utils.makeServerPost(`status/${notificationId}`, { notificationId })

export const resendCertificateStatus = ({ certificateIds, statuses }) =>
  utils.makeServerPost(`status/certificates`, { certificateIds, statuses })

export const resendUnitsStatus = ({ unitIds, start, end, statuses, activationTime }) =>
  utils.makeServerPost(`status/units`, { unitIds, start, end, statuses, activationTime })

export const resendCaregiverStatus = ({ careGiverId, start, end, statuses, activationTime }) =>
  utils.makeServerPost(`status/caregiver/${careGiverId}`, { start, end, statuses, activationTime })

export const resendCertificateStatusCount = ({ certificateIds, statuses }) =>
  utils.makeServerPost(`status/count/certificates`, { certificateIds, statuses })

export const resendUnitsStatusCount = ({ unitIds, start, end, statuses }) =>
  utils.makeServerPost(`status/count/units`, { start, end, statuses, unitIds })

export const resendCaregiverStatusCount = ({ careGiverId, start, end, statuses }) =>
  utils.makeServerPost(`status/count/caregiver/${careGiverId}`, { start, end, statuses })

export const fetchIntygInfoList = ({ pageIndex, sortColumn, sortDirection }) => {
  const finalPageIndex = pageIndex ?? 0
  const finalSortColumn = sortColumn ?? 'createdAt'
  const finalSortDirection = sortDirection ?? 'DESC'

  return utils.makeServerRequest(
    utils.buildUrlFromParams('intygInfo', {
      page: finalPageIndex,
      size: 10,
      sort: `${finalSortColumn},${finalSortDirection}`,
    })
  )
}
