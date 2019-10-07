import * as api from './real/bannerList.api'

export const fetchBannerList = (bannerListRequest) => {

  let {pageIndex, sortColumn, sortDirection} = bannerListRequest

  if(!pageIndex) {
    pageIndex = 0
  }

  if(!sortColumn) {
    sortColumn = 'createdAt'
  }

  if(!sortDirection) {
    sortDirection = 'DESC'
  }

  return api.fetchBannerList({...bannerListRequest, pageIndex, sortColumn, sortDirection})
}
