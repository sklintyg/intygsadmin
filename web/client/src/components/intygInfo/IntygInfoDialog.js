import React, {useState} from 'react'
import {connect} from 'react-redux'
import {Button, Modal, ModalBody, ModalFooter, ModalHeader, Table} from 'reactstrap'
import UncontrolledTooltip from 'reactstrap/lib/UncontrolledTooltip'
import {compose} from 'recompose'
import styled from 'styled-components'
import {getMessage} from '../../messages/messages'
import * as actions from '../../store/actions/intygInfo'
import DisplayDateTime from '../displayDateTime/DisplayDateTime'
import modalContainer from '../modalContainer/modalContainer'
import colors from '../styles/iaColors'
import {IaTypo02, IaTypo03, IaTypo05} from '../styles/iaTypography'
import {resendCertificateStatus, resendNotificationStatus} from "../../api/intygInfo.api";

const BodyHeight = styled(ModalBody)`
  height: 60vh;
  overflow: auto;
`

const RowWrapper = styled.div`
  margin-bottom: 30px;

  tr:first-of-type > td {
    border-top: 1px solid #dee2e6;
  }
`

const RowWrapperBox = styled.div`
  margin-bottom: 10px;
`

const InfoBoxWrapper = styled.div`
  border: 1px solid ${colors.IA_COLOR_15};
  border-radius: 4px;
  margin-right: 20px;
  margin-bottom: 20px;
  display: inline-block;
`

const InfoBoxTitle = styled(IaTypo05)`
  border-bottom: 1px solid ${colors.IA_COLOR_15};
  color: ${colors.IA_COLOR_06};
  height: 30px;
  padding: 0 15px;
  display: flex;
  justify-content: center;
  flex-direction: column;
`

const InfoBoxValue = styled(IaTypo03)`
  height: 40px;
  color: ${colors.IA_COLOR_06};
  padding: 0 15px;
  display: flex;
  justify-content: center;
  flex-direction: column;
`

const TableTH = styled.td`
  font-weight: 500;
  font-size: 14px;
  color: ${colors.IA_COLOR_06};
`

const TableTD = styled.td`
  font-weight: 400;
  font-size: 14px;
  color: ${colors.IA_COLOR_06};
`

const IntygVersion = styled(IaTypo02)`
  display: inline-block;
  margin-left: 15px;
  color: ${colors.IA_COLOR_01};
`

const ModalFooterContainer = styled.div`
  display: flex;
  justify-content: space-between;
  width: 100%;
`

const InfoBox = ({ title, value, noValue }) => {
  return (
    <InfoBoxWrapper>
      <InfoBoxTitle>{title}</InfoBoxTitle>
      <InfoBoxValue>{value ? <DisplayDateTime date={value} includeSeconds={true} /> : noValue}</InfoBoxValue>
    </InfoBoxWrapper>
  )
}

const Kompletteringar = ({ intygInfo }) => {
  if (!intygInfo.sentToRecipient) {
    return '-'
  }

  if (intygInfo.komletteingar === 0) {
    return '0 kompletteringsbegäran'
  }

  return (
    <>
      {intygInfo.kompletteringar} kompletteringsbegäran - {intygInfo.kompletteringarAnswered} besvarad(e)
    </>
  )
}

const AdminQuestions = ({ intygInfo }) => {
  if (!intygInfo.sentToRecipient) {
    return '-'
  }

  return (
    <>
      <AdminQuestionsSent intygInfo={intygInfo} />
      <AdminQuestionsReceived intygInfo={intygInfo} />
    </>
  )
}

const AdminQuestionsSent = ({ intygInfo }) => {
  if (!intygInfo.sentToRecipient) {
    return '-'
  }

  if (intygInfo.administrativaFragorSent === 0) {
    return <div>0 administrativa frågor från vården</div>
  }

  if (intygInfo.administrativaFragorSent === 1) {
    return <div>1 administrativ fråga av vården - {intygInfo.administrativaFragorSentAnswered} besvarad</div>
  }

  return (
    <div>
      {intygInfo.administrativaFragorSent} administrativa frågor från vården - {intygInfo.administrativaFragorSentAnswered} besvarad(e)
    </div>
  )
}

const AdminQuestionsReceived = ({ intygInfo }) => {
  if (!intygInfo.sentToRecipient) {
    return '-'
  }

  if (intygInfo.administrativaFragorReceived === 0) {
    return <div>0 administrativa frågor från intygsutfärdaren</div>
  }

  if (intygInfo.administrativaFragorReceived === 1) {
    return <div>1 administrativ fråga av intygsutfärdaren - {intygInfo.administrativaFragorReceivedAnswered} besvarad</div>
  }

  return (
    <div>
      {intygInfo.administrativaFragorReceived} administrativa frågor från intygsutfärdaren -{' '}
      {intygInfo.administrativaFragorReceivedAnswered} besvarad(e)
    </div>
  )
}

const IntygEventRow = ({ event, fetchIntygInfo, setMessage}) => {
  const VisaIntyg = () => {
    const openIntyg = () => {
      fetchIntygInfo(event.data.intygsId)
    }

    const btnId = event.source + '-' + event.data.intygsId

    return (
      <>
        <Button onClick={openIntyg} size="sm" id={btnId} color="default">
          Visa intyget
        </Button>
        <UncontrolledTooltip trigger="hover" placement="auto" target={`${btnId}`}>
          Intyg-id: {event.data.intygsId}
        </UncontrolledTooltip>
      </>
    )
  }

  return (
    <tr>
      <TableTD>
        <DisplayDateTime date={event.date} />
      </TableTD>
      <TableTD>{getMessage(`intygInfo.source.${event.source}`)}</TableTD>
      <TableTD>{getMessage(`intygInfo.eventType.${event.type}`, event.data) + (event.data && event.data.status ? getEventStatus(event.data.status) : "")}</TableTD>
      <TableTD>{(event.data && event.data.status && event.data.status !== "RESEND" && event.data.notificationId ?
        <Button
          id={'closeBtn'}
          onClick={() => {
            handleResend('notification', event.data.notificationId, setMessage)
          }}
          color={'default'}>
          Skicka om
        </Button> : "")}
      </TableTD>
      <TableTD>{event.data && event.data.intygsId ? <VisaIntyg /> : ''}</TableTD>
    </tr>
  )
}

const getEventStatus = (status) => {
  if (status === undefined) {
      return ""
  }

  let convertedStatus = "misslyckad"
  if (status === "SUCCESS") {
    convertedStatus = "lyckad"
  }

  if (status === "RESEND") {
    convertedStatus = "omsändning"
  }


  return " (" + convertedStatus + ")"
}

const IntygInfoDialog = ({ handleClose, isOpen, data, fetchIntygInfo }) => {

  const [message, setMessage] = useState('')

  if (!data) {
    return null
  }

  const { intygInfo } = data

  function displayEvent(event) {
    if (event.type === 'IS005') {
      return !intygInfo.testCertificate
    }
    return true
  }

  return (
    <Modal id={'intygInfoDialogId'} isOpen={isOpen} size={'xl'} backdrop={true} toggle={() => handleOnClose(handleClose, setMessage)}>
      <ModalHeader toggle={() => handleOnClose(handleClose, setMessage)}>Intygsinformation för ID: {intygInfo.intygId}</ModalHeader>
      <BodyHeight>
        <RowWrapper>
          <IaTypo03 color={colors.IA_COLOR_01}>
            {intygInfo.intygType}
            <IntygVersion>{intygInfo.intygVersion}</IntygVersion>
          </IaTypo03>
        </RowWrapper>

        <RowWrapperBox>
          <InfoBox title="Utkastet skapades" value={intygInfo.draftCreated} noValue="Okänt" />
          <InfoBox title="Intyget signerades" value={intygInfo.signedDate} noValue="Ej signerat" />
          <InfoBox title="Intygstjänsten tog emot intyget" value={intygInfo.receivedDate} noValue="Ej mottagit" />
          <InfoBox
            title="Intyget skickades till intygsmottagaren"
            value={intygInfo.sentToRecipient}
            noValue={intygInfo.numberOfRecipients > 0 ? 'Ej skickat' : 'Finns ingen mottagare'}
          />
        </RowWrapperBox>

        <RowWrapper>
          <IaTypo03>Intygshistorik</IaTypo03>
          <Table striped>
            <thead>
              <tr>
                <th>Tid</th>
                <th>Källa</th>
                <th>Händelse</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {intygInfo.events &&
                intygInfo.events
                  .filter((row) => {
                    return displayEvent(row)
                  })
                .map((row, index) => (
                  <IntygEventRow key={index} event={row} fetchIntygInfo={fetchIntygInfo} setMessage={setMessage} />
                ))}
            </tbody>
          </Table>
        </RowWrapper>

        <RowWrapper>
          <IaTypo03>Övriga intygsuppgifter</IaTypo03>
          <Table striped>
            <tbody>
              <tr>
                <TableTH>Signerat av</TableTH>
                <TableTD>{intygInfo.signedByName}</TableTD>
                <TableTD>HSA-ID: {intygInfo.signedByHsaId}</TableTD>
              </tr>
              <tr>
                <TableTH>Utfärdande Vårdenhet/enhet</TableTH>
                <TableTD>{intygInfo.careUnitName}</TableTD>
                <TableTD>HSA-ID: {intygInfo.careUnitHsaId}</TableTD>
              </tr>
              <tr>
                <TableTH>Vårdgivare</TableTH>
                <TableTD>{intygInfo.careGiverName}</TableTD>
                <TableTD>HSA-ID: {intygInfo.careGiverHsaId}</TableTD>
              </tr>
              <tr>
                <TableTH>Kompletteringsbegäran</TableTH>
                <TableTD>
                  <Kompletteringar intygInfo={intygInfo} />
                </TableTD>
                <TableTD />
              </tr>
              <tr>
                <TableTH>Administrativa frågor</TableTH>
                <TableTD>
                  <AdminQuestions intygInfo={intygInfo} />{' '}
                </TableTD>
                <TableTD />
              </tr>
              <tr>
                <TableTH>Utfärdande system</TableTH>
                <TableTD>{intygInfo.createdInWC ? 'Webcert' : 'Annat än Webcert'}</TableTD>
                <TableTD />
              </tr>
              <tr>
                <TableTH>Valideringsperson</TableTH>
                <TableTD>{intygInfo.testCertificate ? 'Ja' : 'Nej'}</TableTD>
                <TableTD />
              </tr>
            </tbody>
          </Table>
        </RowWrapper>
      </BodyHeight>
      <ModalFooter>
        <ModalFooterContainer>
          <Button id={'closeBtn'} onClick={() => handleOnClose(handleClose, setMessage)} color={'default'}>
            Stäng
          </Button>
          {message && (
            <p style={{ color: message === 'Omsändningen lyckades.' ? 'green' : 'red' }}>
              {message}
            </p>
          )}
          <Button
            id={'resendEvents'}
            onClick={() => {
              handleResend('certificate', intygInfo.intygId, setMessage)
            }}
            color={'default'}>
            Skicka om alla
          </Button>
        </ModalFooterContainer>
      </ModalFooter>
    </Modal>
  )
}

const handleResend = (type, id, setMessage) => {
  let request;
  if (type === 'certificate') {
    request = resendCertificateStatus({
      certificateIds: id.split(',').map((i) => i.trim()),
      statuses: ['SUCCESS', 'FAILURE'],
    });
  } else if (type === 'notification') {
    request = resendNotificationStatus(
      id
    );
  }

  request.then((response) => {
    if(response.count === 0) {
      setMessage('Omsändningen misslyckades. Försök igen.');
    }
    else(
      setMessage('Omsändningen lyckades.')
    )
  })
  .catch(() => {
    setMessage('Omsändningen misslyckades. Försök igen.');
  });
};

const handleOnClose = (handleClose, setMessage) => {
  handleClose()
  setMessage('')
}

export const intygInfoDialogId = 'intygInfoDialog'

export default compose(
  modalContainer(intygInfoDialogId),
  connect(
    null,
    actions
  )
)(IntygInfoDialog)
