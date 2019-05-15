import * as utils from "./utils";

export const fetchBannerList = ({pageIndex, sortColumn, sortDirection}) =>
    utils.makeServerRequest(utils.buildUrlFromParams('banner', {
      pageIndex: pageIndex,
      limit: 10,
      sortColumn: sortColumn,
      sortDirection: sortDirection
    }));
