import * as utils from './utils'

export const createDataExport = (dataExport) => utils.makeServerPut('dataExport', dataExport)

export const removeDataExport = (dataExportId) => utils.makeServerDelete(`dataExport/${dataExportId}`, {}, { emptyBody: true })

export const updateDataExport = (dataExport, dataExportId) => utils.makeServerPost(`dataExport/${dataExportId}`, dataExport)

export const fetchDataExportList = ({ pageIndex, sortColumn, sortDirection }) => {
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
    utils.buildUrlFromParams('dataExport', {
      page: pageIndex,
      size: 10,
      sort: `${sortColumn},${sortDirection}`,
    })
  )
}

export const fetchDataExportStatusList = ({ dataExportId }) => {
  return utils.makeServerRequest(
    utils.buildUrlFromParams( `dataExport/${dataExportId}/status`, {})
  )
}
