let api;

if (process.env.NODE_ENV === 'production' || false) {
  api = require('./real/bannerList.api')
} else {
  api = require('./mock/bannerList.api');
}

export const fetchBannerList = (bannerListRequest) => {

  let {pageIndex, sortColumn, sortDirection} = bannerListRequest

  if(!pageIndex) {
    pageIndex = 0
  }

  if(!sortColumn) {
    sortColumn = 'CREATEDBY'
  }

  if(!sortDirection) {
    sortDirection = 'DESC'
  }

  return api.fetchBannerList({...bannerListRequest, pageIndex, sortColumn, sortDirection})
}
