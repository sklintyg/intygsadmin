import * as api from '../../api/intygInfo.api'
import { createAPIReducer } from '../../api/utils'
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

export const resendCertificateStatus = createAPIReducer('RESEND_CERTIFICATE_STATUS', api.resendCertificateStatus)
export const resendUnitsStatus = createAPIReducer('RESEND_UNIT_STATUS', api.resendUnitsStatus)
export const resendCaregiverStatus = createAPIReducer('RESEND_CAREGIVER_STATUS', api.resendCaregiverStatus)
