import * as api from '../../api/intygInfo.api'
import { getIsFetching } from '../reducers/intygInfo'
import { fetchIntygInfoList } from './intygInfoList'

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
      dispatch(fetchIntygInfoList({ pageIndex: 0 }))

      return dispatch({
        type: FETCH_INTYG_INFO_SUCCESS,
        response: response,
      })
    },
    (errorResponse) => {
      return dispatch({
        type: FETCH_INTYG_INFO_FAILURE,
        payload: errorResponse,
      })
    }
  )
}

export const RESEND_CERTIFICATE_STATUS_REQUEST = 'RESEND_CERTIFICATE_STATUS_REQUEST'
export const RESEND_CERTIFICATE_STATUS_SUCCESS = 'RESEND_CERTIFICATE_STATUS_SUCCESS'
export const RESEND_CERTIFICATE_STATUS_FAILURE = 'RESEND_CERTIFICATE_STATUS_FAILURE'

export const resendCertificateStatus = ({ certificateIds, statuses }) => (dispatch) => {
  dispatch({
    type: RESEND_CERTIFICATE_STATUS_REQUEST,
  })

  return api.resendCertificateStatus({ certificateIds, statuses }).then(
    (response) => {
      dispatch({
        type: RESEND_CERTIFICATE_STATUS_SUCCESS,
        response: response,
      })
    },
    (errorResponse) => {
      return dispatch({
        type: RESEND_CERTIFICATE_STATUS_FAILURE,
        payload: errorResponse,
      })
    }
  )
}
