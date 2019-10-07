import * as api from './real/intygInfo.api'

export const fetchIntygInfo = (intygsId) => api.fetchIntygInfo(intygsId);

export const fetchIntygInfoList = (requestParams) => {

  let {pageIndex, sortColumn, sortDirection} = requestParams

  if(!pageIndex) {
    pageIndex = 0
  }

  if(!sortColumn) {
    sortColumn = 'createdAt'
  }

  if(!sortDirection) {
    sortDirection = 'DESC'
  }

  return api.fetchIntygInfoList({...requestParams, pageIndex, sortColumn, sortDirection})
}
