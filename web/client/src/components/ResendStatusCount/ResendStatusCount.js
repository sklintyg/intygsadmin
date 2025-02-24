import React from 'react'
import { compose, lifecycle } from 'recompose'
import { connect } from 'react-redux'
import * as actions from '../../store/actions/intygInfo'
import styled from 'styled-components'
import { getStatusCount, getIsFetching, getErrorMessage } from '../../store/reducers/countStatus.reducer'

const PreviewDiv = styled.div`
  display: flex;
  flex-direction: column;
  margin-bottom: 32px;
`

const ResendStatusCount = ({ count, isFetching, errorMessage }) => {
  if (errorMessage) {
    return <p style={{ color: 'red' }}>Kunde inte hämta antal händelser för omsändning</p>
  }

  if (!count) {
    return null
  }

  return (
    <PreviewDiv>
      <strong>Antal händelser för omsändning</strong>
      <span>{count}</span>
    </PreviewDiv>
  )
}

const lifeCycleValues = {
  componentDidMount() {
    const {
      statusFor,
      resendCertificateStatusCount,
      resendUnitsStatusCount,
      resendCaregiverStatusCount,
      certificateIds,
      unitIds,
      careGiverId,
      statuses,
      start,
      end,
    } = this.props
    if (statusFor === '0') {
      resendCertificateStatusCount({ certificateIds, statuses })
    }
    if (statusFor === '1') {
      resendCaregiverStatusCount({
        careGiverId,
        start,
        end,
        statuses,
      })
    }
    if (statusFor === '2') {
      resendUnitsStatusCount({
        unitIds,
        start,
        end,
        statuses,
      })
    }
  },
}

const mapStateToProps = (state) => {
  return {
    count: getStatusCount(state),
    isFetching: getIsFetching,
    errorMessage: getErrorMessage,
  }
}

export default compose(
  connect(
    mapStateToProps,
    actions
  ),
  lifecycle(lifeCycleValues)
)(ResendStatusCount)
