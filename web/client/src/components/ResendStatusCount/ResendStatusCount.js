import React, {useEffect} from 'react'
import {compose} from 'recompose'
import {connect} from 'react-redux'
import * as actions from '../../store/actions/intygInfo'
import styled from 'styled-components'
import {getErrorMessage, getIsFetching} from '../../store/reducers/countStatus.reducer'
import IaAlert, {alertType} from "../alert/Alert";
import {resendCaregiverStatusCount, resendCertificateStatusCount, resendUnitsStatusCount} from "../../api/intygInfo.api";

const PreviewDiv = styled.div`
  display: flex;
  flex-direction: column;
  margin-bottom: 32px;
`

const ResendStatusCount = ({ statusFor,
  certificateIds,
  unitIds,
  careGiverId,
  statuses,
  start,
  end }) => {
  const [error, setError] = React.useState(false)
  const [count, setCount] = React.useState(undefined)
  const [max, setMax] = React.useState(undefined)

  useEffect(() => {
    let request;
    if (statusFor === '0') {
      request = resendCertificateStatusCount({ certificateIds, statuses })
    }
    if (statusFor === '1') {
      request = resendCaregiverStatusCount({
        careGiverId,
        start,
        end,
        statuses,
      })
    }
    if (statusFor === '2') {
      request = resendUnitsStatusCount({
        unitIds,
        start,
        end,
        statuses,
      })
    }

    request.then((response) => {
        setError(false)
        setCount(response.count)
        setMax(response.max)
    }).catch(() => {
      setError(true)
      setCount(undefined)
      setMax(undefined)
    });
  }, [statusFor, careGiverId, unitIds, start, end, statuses, certificateIds]);

  if (count === 0 && !error) {
    return <IaAlert type={alertType.ERROR}>Det finns inga händelser att skicka om</IaAlert>
  }

  if (!count || error) {
    return <IaAlert type={alertType.ERROR}>Kunde inte hämta antal händelser för omsändning</IaAlert>
  }

  if (count > max) {
    return <IaAlert type={alertType.ERROR}>Det finns för många händelser att skicka om. Begränsa perioden. Antal händelser: {count}</IaAlert>
  }

  return (
    <PreviewDiv>
      <strong>Antal händelser för omsändning</strong>
      <span>{count}</span>
    </PreviewDiv>
  )
}

const mapStateToProps = (state) => {
  return {
    isFetching: getIsFetching,
    errorMessage: getErrorMessage,
  }
}

export default compose(
  connect(
    mapStateToProps,
    actions
  )
)(ResendStatusCount)
