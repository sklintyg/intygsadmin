import * as api from '../../api/intygInfo.api'
import { getIsFetching, getSortOrder } from '../reducers/intygInfoList'
import { FETCH_INTYG_INFO_LIST_FAILURE, FETCH_INTYG_INFO_LIST_REQUEST, FETCH_INTYG_INFO_LIST_SUCCESS } from './intygInfoList.constants'

export { FETCH_INTYG_INFO_LIST_REQUEST, FETCH_INTYG_INFO_LIST_SUCCESS, FETCH_INTYG_INFO_LIST_FAILURE }

export const fetchIntygInfoList = (request) => (dispatch, getState) => {
  if (getIsFetching(getState())) {
    return Promise.resolve()
  }

  dispatch({
    type: FETCH_INTYG_INFO_LIST_REQUEST,
  })

  let requestParams = request

  if (!request || !request.sortColumn) {
    const sortOrder = getSortOrder(getState())

    requestParams = {
      ...request,
      ...sortOrder,
    }
  }

  return api.fetchIntygInfoList(requestParams).then(
    (response) => {
      return dispatch({
        type: FETCH_INTYG_INFO_LIST_SUCCESS,
        response: response,
        sortColumn: requestParams.sortColumn,
        sortDirection: requestParams.sortDirection,
      })
    },
    (errorResponse) => {
      return dispatch({
        type: FETCH_INTYG_INFO_LIST_FAILURE,
        payload: errorResponse,
      })
    }
  )
}
