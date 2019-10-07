import * as utils from "./utils";

export const fetchBannerList = ({pageIndex, sortColumn, sortDirection}) => {

  if(!pageIndex) {
    pageIndex = 0
  }

  if(!sortColumn) {
    sortColumn = 'createdAt'
  }

  if(!sortDirection) {
    sortDirection = 'DESC'
  }

  return utils.makeServerRequest(utils.buildUrlFromParams('banner', {
    page: pageIndex,
    size: 10,
    sort: `${sortColumn},${sortDirection}`
  }));
}
