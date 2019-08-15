import * as api from '../../api/bannerList.api'
import {getIsFetching, getSortOrder} from '../reducers/bannerList.reducer'

export const FETCH_BANNERLIST_REQUEST = 'FETCH_BANNERLIST_REQUEST'
export const FETCH_BANNERLIST_SUCCESS = 'FETCH_BANNERLIST_SUCCESS'
export const FETCH_BANNERLIST_FAILURE = 'FETCH_BANNERLIST_FAILURE'

export const fetchBannerList = (bannerRequest) => (dispatch, getState) => {
  if (getIsFetching(getState())) {
    return Promise.resolve()
  }

  dispatch({
    type: 'FETCH_BANNERLIST_REQUEST',
  })

  let requestParams = bannerRequest;

  if (!bannerRequest || !bannerRequest.sortColumn) {
    const sortOrder = getSortOrder(getState())

    requestParams = {
      ...bannerRequest,
      ...sortOrder
    }
  }

  return api.fetchBannerList(requestParams).then(
    (response) => {
      dispatch({
        type: 'FETCH_BANNERLIST_SUCCESS',
        response: response,
        sortColumn: requestParams.sortColumn,
        sortDirection: requestParams.sortDirection
      })
    },
    (errorResponse) => {
      dispatch({
        type: 'FETCH_BANNERLIST_FAILURE',
        payload: errorResponse,
      })

      return Promise.reject();
    }
  )
}
