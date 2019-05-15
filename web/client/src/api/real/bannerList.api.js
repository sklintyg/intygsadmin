import * as utils from "./utils";

export const fetchBestallningList = ({categoryFilter, textFilter, pageIndex, sortColumn, sortDirection}) =>
    utils.makeServerRequest(utils.buildUrlFromParams('bestallningar', {
      category: categoryFilter,
      textSearch: textFilter,
      pageIndex: pageIndex,
      limit: 10,
      sortColumn: sortColumn,
      sortDirection: sortDirection
    }));
