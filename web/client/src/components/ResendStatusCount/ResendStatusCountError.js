import React, {useEffect} from 'react'
import {compose} from 'recompose'
import {connect} from 'react-redux'
import * as actions from '../../store/actions/intygInfo'
import styled from 'styled-components'
import {getErrorMessage, getIsFetching, getStatusCount, getStatusMaxCount} from '../../store/reducers/countStatus.reducer'
import IaAlert, {alertType} from "../alert/Alert";
import {resendCaregiverStatusCount, resendCertificateStatusCount, resendUnitsStatusCount} from "../../api/intygInfo.api";

const PreviewDiv = styled.div`
  display: flex;
  flex-direction: column;
  margin-bottom: 32px;
`

const ResendStatusCount = ({ count, max, statusFor,
  certificateIds,
  unitIds,
  careGiverId,
  statuses,
  start,
  end }) => {
  const [error, setError] = React.useState(false)

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
      console.log("response: ", response.count)
      if(response.count !== 0) {
        setError(false)

      }
      else(
        setError(false)
      )
    })
    .catch(() => {
      console.log("error: ")

      setError(true)
    });
  }, []);

  if (count === 0 && !error) {
    return <IaAlert type={alertType.ERROR}>Det finns inga händelser att skicka om</IaAlert>
  }

  if (!count || error) {
    return <IaAlert type={alertType.ERROR}>Kunde inte hämta antal händelser för omsändning</IaAlert>
  }

  if (count === 0) {
    return <IaAlert type={alertType.ERROR}>Det finns inga händelser att skicka om</IaAlert>
  }

  if (count > max) {
    return <IaAlert type={alertType.ERROR}>Det finns för många händelser att skicka om. Begränsa perioden. Antal händelser: {count}</IaAlert>
  }

  return (
    <PreviewDiv>
      <IaAlert type={alertType.CONFIRM}>Antal händelser för omsändning: {count}</IaAlert>
    </PreviewDiv>
  )
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
  )
)(ResendStatusCount)
