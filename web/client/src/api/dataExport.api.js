
import * as utils from './utils';

export const createDataExport = (dataExport) => utils.makeServerPost('dataExport', dataExport);

export const eraseDataExport = (terminationId) => utils.makeServerPost(`dataExport/${terminationId}/erase`, {}, {emptyBody: true});


export const fetchDataExportList = ({ pageIndex, sortColumn, sortDirection }) => {
  if (!pageIndex) {
    pageIndex = 0;
  }

  if (!sortColumn) {
    sortColumn = 'createdAt';
  }

  if (!sortDirection) {
    sortDirection = 'DESC';
  }

  return utils.makeServerRequest(
    utils.buildUrlFromParams('dataExport', {
      page: pageIndex,
      size: 10,
      sort: `${sortColumn},${sortDirection}`
    })
  );
};

