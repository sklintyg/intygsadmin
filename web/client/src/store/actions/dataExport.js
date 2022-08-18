import * as api from '../../api/dataExport.api';
import { getIsFetching } from '../reducers/dataExport';
import { getSortOrder } from '../reducers/dataExport';

export const CREATE_DATA_EXPORT_REQUEST = 'CREATE_DATA_EXPORT_REQUEST';
export const CREATE_DATA_EXPORT_SUCCESS = 'CREATE_DATA_EXPORT_SUCCESS';
export const CREATE_DATA_EXPORT_FAILURE = 'CREATE_DATA_EXPORT_FAILURE';

export const ERASE_DATA_EXPORT_REQUEST = 'ERASE_DATA_EXPORT_REQUEST';
export const ERASE_DATA_EXPORT_SUCCESS = 'ERASE_DATA_EXPORT_SUCCESS';
export const ERASE_DATA_EXPORT_FAILURE = 'ERASE_DATA_EXPORT_FAILURE';

export const RESEND_DATA_EXPORT_KEY_REQUEST = 'RESEND_DATA_EXPORT_KEY_REQUEST';
export const RESEND_DATA_EXPORT_KEY_SUCCESS = 'RESEND_DATA_EXPORT_KEY_SUCCESS';
export const RESEND_DATA_EXPORT_KEY_FAILURE = 'RESEND_DATA_EXPORT_KEY_FAILURE';

export const FETCH_DATA_EXPORT_LIST_REQUEST = 'FETCH_DATA_EXPORT_LIST_REQUEST';
export const FETCH_DATA_EXPORT_LIST_SUCCESS = 'FETCH_DATA_EXPORT_LIST_SUCCESS';
export const FETCH_DATA_EXPORT_LIST_FAILURE = 'FETCH_DATA_EXPORT_LIST_FAILURE';

export const fetchDataExportList = (request) => (dispatch, getState) => {
  if (getIsFetching(getState())) {
    return Promise.resolve();
  }

  dispatch({ type: FETCH_DATA_EXPORT_LIST_REQUEST });

  let requestParams = request;

  if (!request || !request.sortColumn) {
    const sortOrder = getSortOrder(getState());

    requestParams = { ...request, ...sortOrder };
  }

  return api.fetchDataExportList(requestParams).then(
    (response) => {
      dispatch({
        type: FETCH_DATA_EXPORT_LIST_SUCCESS,
        response: response,
        sortColumn: requestParams.sortColumn,
        sortDirection: requestParams.sortDirection
      });
    },
    (errorResponse) => {
      dispatch({
        type: FETCH_DATA_EXPORT_LIST_FAILURE,
        payload: errorResponse
      });

      return Promise.reject();
    }
  );
};

export const createDataExport = (dataExport) => (dispatch, getState) => {
  if (getIsFetching(getState())) {
    return Promise.resolve();
  }

  dispatch({ type: CREATE_DATA_EXPORT_REQUEST });

  return api.createDataExport(dataExport).then(
    (response) => {
      return dispatch({
        type: CREATE_DATA_EXPORT_SUCCESS,
        response: response,
      });
    },
    (errorResponse) => {
      dispatch({
        type: CREATE_DATA_EXPORT_FAILURE,
        payload: errorResponse,
      });
      return Promise.reject();
    }
  );
};

export const eraseDataExport = (terminationId) => (dispatch, getState) => {
  if (getIsFetching(getState())) {
    return Promise.resolve();
  }

  dispatch({ type: ERASE_DATA_EXPORT_REQUEST });

  return api.eraseDataExport(terminationId).then(
    (response) => {
      return dispatch({
        type: ERASE_DATA_EXPORT_SUCCESS,
        response: response
      });
    },
    (errorResponse) => {
      dispatch({
        type: ERASE_DATA_EXPORT_FAILURE,
        payload: errorResponse
      });
      return Promise.reject();
    }
  );
};

export const resendDataExportKey = (terminationId) => (dispatch, getState) => {
  if (getIsFetching(getState())) {
    return Promise.resolve();
  }

  dispatch({ type: RESEND_DATA_EXPORT_KEY_REQUEST });

  return api.resendDataExportKey(terminationId).then(
    (response) => {
      return dispatch({
        type: RESEND_DATA_EXPORT_KEY_SUCCESS,
        response: response
      });
    },
    (errorResponse) => {
      dispatch({
        type: RESEND_DATA_EXPORT_KEY_FAILURE,
        payload: errorResponse
      });
      return Promise.reject();
    }
  );
};
