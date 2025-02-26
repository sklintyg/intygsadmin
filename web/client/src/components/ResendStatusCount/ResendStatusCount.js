import React from 'react'
import {compose, lifecycle} from 'recompose'
import {connect} from 'react-redux'
import * as actions from '../../store/actions/intygInfo'
import styled from 'styled-components'
import {getErrorMessage, getIsFetching, getStatusCount, getStatusMaxCount} from '../../store/reducers/countStatus.reducer'

const PreviewDiv = styled.div`
  display: flex;
  flex-direction: column;
  margin-bottom: 32px;
`

const ResendStatusCount = ({ count, max }) => {
  if (count === 0) {
    return <p style={{color: 'red'}}>Det finns inga händelser att skicka om</p>
  }
  if (!count) {
    return <p style={{color: 'red'}}>Kunde inte hämta antal händelser för omsändning</p>
  }

  if (count > max) {
    return <p style={{color: 'red'}}>Du försöker skicka om fler omsändningar än tillåtet. Är du säker att du vill utföra omskicket?</p>
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
    max: getStatusMaxCount(state),
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
