import { combineReducers } from 'redux';
import { buildClientError } from './util';
import * as ActionConstants from '../actions/dataExport';

export const DataExportListDefaultState = {
  content: [],
  pageIndex: 0,
  start: 0,
  end: 0,
  numberOfElements: 0,
  totalElements: 0,
  sortColumn: 'createdAt',
  sortDirection: 'DESC'
};

const dataExportList = (state = DataExportListDefaultState, action) => {
  switch (action.type) {
    case ActionConstants.FETCH_DATA_EXPORT_LIST_SUCCESS:
      return {
        ...state,
        content: action.response.content,
        numberOfElements: action.response.numberOfElements,
        pageIndex: action.response.number,
        start: action.response.number * action.response.size + 1,
        end: action.response.number * action.response.size + action.response.numberOfElements,
        totalElements: action.response.totalElements,
        sortColumn: action.sortColumn,
        sortDirection: action.sortDirection
      };
    case ActionConstants.FETCH_DATA_EXPORT_LIST_FAILURE:
      return DataExportListDefaultState;
    default:
      return state;
  }
}

const isFetching = (state = false, action) => {
  switch (action.type) {
    case ActionConstants.CREATE_DATA_EXPORT_REQUEST:
      return true;
    case ActionConstants.CREATE_DATA_EXPORT_SUCCESS:
    case ActionConstants.CREATE_DATA_EXPORT_FAILURE:
      return false;
    default:
      return state;
  }
}

const errorMessage = (state = null, action) => {
  switch (action.type) {
    case ActionConstants.CREATE_DATA_EXPORT_FAILURE:
      return {
        code: action.payload.error.errorCode,
        error: buildClientError(action.payload, 'error.dataExport').message,
      }
    case ActionConstants.CREATE_DATA_EXPORT_REQUEST:
    case ActionConstants.CREATE_DATA_EXPORT_SUCCESS:
      return null;
    default:
      return state;
  }
}

export default combineReducers({
  dataExportList,
  isFetching,
  errorMessage
});

// Selectors

export const getIsFetching = (state) => state.dataExport.isFetching;

export const getErrorMessage = (state) => state.dataExport.errorMessage;

export const getDataExportList = (state) => state.dataExport.dataExportList;

export const getSortOrder = (state) => {
  return {
    sortColumn: state.dataExport.dataExportList.sortColumn,
    sortDirection: state.dataExport.dataExportList.sortDirection,
  };
}

