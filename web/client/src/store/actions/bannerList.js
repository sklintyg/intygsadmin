import * as api from '../../api/bannerListApi'
import { getIsFetching } from '../reducers/bannerList'

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

  return api.fetchBannerList(bannerRequest).then(
    (response) => {
      dispatch({
        type: 'FETCH_BANNERLIST_SUCCESS',
        response: response,
      })
    },
    (errorResponse) => {
      dispatch({
        type: 'FETCH_BANNERLIST_FAILURE',
        payload: errorResponse,
      })
    }
  )
}
