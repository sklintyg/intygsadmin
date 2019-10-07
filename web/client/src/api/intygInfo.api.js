import * as utils from './utils'

export const fetchIntygInfo = (intygsId) => utils.makeServerRequest(`intygInfo/${intygsId}`)

export const fetchIntygInfoList = ({pageIndex, sortColumn, sortDirection}) => {
  if(!pageIndex) {
    pageIndex = 0
  }

  if(!sortColumn) {
    sortColumn = 'createdAt'
  }

  if(!sortDirection) {
    sortDirection = 'DESC'
  }

  return utils.makeServerRequest(utils.buildUrlFromParams('intygInfo', {
    page: pageIndex,
    size: 10,
    sort: `${sortColumn},${sortDirection}`
  }));
}
