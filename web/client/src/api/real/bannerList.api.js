import * as utils from "./utils";

export const fetchBannerList = ({pageIndex, sortColumn, sortDirection}) =>
  utils.makeServerRequest(utils.buildUrlFromParams('banner', {
    page: pageIndex,
    size: 10,
    sort: `${sortColumn},${sortDirection}`
  }));
