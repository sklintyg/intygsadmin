import * as api from '../../api/intygInfo.api'
import { getIsFetching } from '../reducers/intygInfo'
import {fetchIntygInfoList} from "./intygInfoList";

export const FETCH_INTYG_INFO_REQUEST = 'FETCH_INTYG_INFO_REQUEST'
export const FETCH_INTYG_INFO_SUCCESS = 'FETCH_INTYG_INFO_SUCCESS'
export const FETCH_INTYG_INFO_FAILURE = 'FETCH_INTYG_INFO_FAILURE'

export const fetchIntygInfo = (intygsId) => (dispatch, getState) => {
  if (getIsFetching(getState())) {
    return Promise.resolve()
  }

  dispatch({
    type: FETCH_INTYG_INFO_REQUEST,
  })

  return api.fetchIntygInfo(intygsId).then(
    (response) => {

      dispatch(fetchIntygInfoList({pageIndex: 0 }));

      return dispatch({
        type: FETCH_INTYG_INFO_SUCCESS,
        response: response,
      })
    },
    (errorResponse) => {
      dispatch({
        type: FETCH_INTYG_INFO_FAILURE,
        payload: errorResponse,
      })

      return Promise.reject();
    }
  )
}
