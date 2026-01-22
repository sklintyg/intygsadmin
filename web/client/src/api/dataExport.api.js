import * as utils from './utils'

export const createDataExport = (dataExport) => utils.makeServerPost('dataExport', dataExport)

export const updateDataExport = (dataExport) => utils.makeServerPost(`dataExport/update`, dataExport)

export const eraseDataExport = (terminationId) => utils.makeServerPost(`dataExport/${terminationId}/erase`, {}, { emptyBody: true })

export const resendDataExportKey = (terminationId) => utils.makeServerPost(`dataExport/${terminationId}/resendkey`, {}, { emptyBody: true })

export const fetchDataExportList = ({ pageIndex, sortColumn, sortDirection }) => {
  const finalPageIndex = pageIndex ?? 0
  const finalSortColumn = sortColumn ?? 'createdAt'
  const finalSortDirection = sortDirection ?? 'DESC'

  return utils.makeServerRequest(
    utils.buildUrlFromParams('dataExport', {
      page: finalPageIndex,
      size: 10,
      sort: `${finalSortColumn},${finalSortDirection}`,
    })
  )
}
