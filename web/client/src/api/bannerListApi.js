let api;

if (process.env.NODE_ENV === 'production' || true) {
  api = require('./real/bannerListApi')
} else {
  api = require('./mock/bannerListApi');
}

export const fetchBannerList = (bannerListRequest) => {

  let {pageIndex, sortColumn, sortDirection} = bannerListRequest
/*
  if(!pageIndex) {
    pageIndex = 0
  }

  if(!sortColumn) {
    sortColumn = 'ANKOMST_DATUM'
  }

  if(!sortDirection) {
    sortDirection = 'DESC'
  }
*/
  return api.fetchBestallningList({...bannerListRequest, pageIndex, sortColumn, sortDirection})
}
