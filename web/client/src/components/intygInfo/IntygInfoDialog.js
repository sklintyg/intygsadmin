import React from 'react'
import {Modal, ModalBody, ModalHeader, Button, ModalFooter, Table} from 'reactstrap'
import modalContainer from '../modalContainer/modalContainer'
import { compose } from 'recompose'
import {IaTypo02, IaTypo03, IaTypo05} from "../styles/iaTypography";
import colors from "../styles/iaColors";
import DisplayDateTime from "../displayDateTime/DisplayDateTime";
import styled from "styled-components";
import {getMessage} from "../../messages/messages";
import * as actions from "../../store/actions/intygInfo";
import {connect} from "react-redux";
import UncontrolledTooltip from "reactstrap/lib/UncontrolledTooltip";

const RowWrapper = styled.div`
  margin-bottom: 30px;
  
  tr:first-of-type > td {
    border-top: 1px solid #dee2e6
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

const InfoBox = ({title, value, noValue}) => {

  return (
    <InfoBoxWrapper>
      <InfoBoxTitle>{title}</InfoBoxTitle>
      <InfoBoxValue>{value ? <DisplayDateTime date={value} includeSeconds={true} /> : noValue}</InfoBoxValue>
    </InfoBoxWrapper>
  )
}

const Kompletteringar = ({intygInfo}) => {

  if (!intygInfo.sentToRecipient) {
    return '-';
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

const AdminQuestions = ({intygInfo}) => {

  if (!intygInfo.sentToRecipient) {
    return '-';
  }

  return (
    <>
      <AdminQuestionsSent intygInfo={intygInfo} />
      <AdminQuestionsReceived intygInfo={intygInfo} />
    </>
  )
}

const AdminQuestionsSent = ({intygInfo}) => {

  if (!intygInfo.sentToRecipient) {
    return '-';
  }

  if (intygInfo.administrativaFragorSent === 0) {
    return (<div>0 administrativa frågor från vården</div>)
  }

  if (intygInfo.administrativaFragorSent === 1) {
    return (<div>1 administrativ fråga av vården - {intygInfo.administrativaFragorSentAnswered} besvarad</div>)
  }

  return (
    <div>
      {intygInfo.administrativaFragorSent} administrativa frågor från vården - {intygInfo.administrativaFragorSentAnswered} besvarad(e)
    </div>
  )
}

const AdminQuestionsReceived = ({intygInfo}) => {

  if (!intygInfo.sentToRecipient) {
    return '-';
  }

  if (intygInfo.administrativaFragorReceived === 0) {
    return (<div>0 administrativa frågor från intygsutfärdaren</div>)
  }

  if (intygInfo.administrativaFragorReceived === 1) {
    return (<div>1 administrativ fråga av intygsutfärdaren - {intygInfo.administrativaFragorReceivedAnswered} besvarad</div>)
  }

  return (
    <div>
      {intygInfo.administrativaFragorReceived} administrativa frågor från intygsutfärdaren - {intygInfo.administrativaFragorReceivedAnswered} besvarad(e)
    </div>
  )
}

const IntygEventRow = ({event, fetchIntygInfo}) => {

  const VisaIntyg = () => {
    const openIntyg = () => {
      fetchIntygInfo(event.data.intygsId)
    }

    const btnId = event.source + '-' + event.data.intygsId

    return (
      <>
        <Button onClick={openIntyg} size="sm" id={btnId} color="default">Visa intyget</Button>
        <UncontrolledTooltip placement="auto" target={`${btnId}`}>
          Intyg-id: {event.data.intygsId}
        </UncontrolledTooltip>
      </>
    )
  }

  return (
    <tr>
      <TableTD><DisplayDateTime date={event.date} /></TableTD>
      <TableTD>{getMessage(`intygInfo.source.${event.source}`)}</TableTD>
      <TableTD>{getMessage(`intygInfo.eventType.${event.type}`, event.data)}</TableTD>
      <TableTD>{event.data && event.data.intygsId ? (<VisaIntyg />) : ''}</TableTD>
    </tr>
  )
}

const IntygInfoDialog = ({ handleClose, isOpen, data, fetchIntygInfo }) => {

  if (!data) {
    return null
  }

  const { intygInfo } = data

  return (
      <Modal id={'intygInfoDialogId'} isOpen={isOpen} size={'xl'} backdrop={true} toggle={handleClose}>
        <ModalHeader toggle={handleClose}>Intygsinformation för ID: {intygInfo.intygId}</ModalHeader>
        <ModalBody>

          <RowWrapper>
            <IaTypo03 color={colors.IA_COLOR_01}>
              {intygInfo.intygType}
              <IntygVersion>
                {intygInfo.intygVersion}
              </IntygVersion>
            </IaTypo03>
          </RowWrapper>

          <RowWrapperBox>
            <InfoBox title='Utkastet skapades' value={intygInfo.draftCreated} noValue='Okänt' />
            <InfoBox title='Intyget signerades' value={intygInfo.signedDate} noValue='Ej signerat' />
            <InfoBox title='Intygstjänsten tog emot intyget' value={intygInfo.receivedDate} noValue='Ej mottagit' />
            <InfoBox
              title='Intyg skickades till intygsmottagaren'
              value={intygInfo.sentToRecipient}
              noValue={intygInfo.numberOfRecipients  > 0 ? 'Ej skickat' : 'Finns ingen mottagare'} />
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
              {
                intygInfo.events &&
                intygInfo.events.map((row, index) => (<IntygEventRow key={index} event={row} fetchIntygInfo={fetchIntygInfo} />))
              }
              </tbody>
            </Table>
          </RowWrapper>

          <RowWrapper>
          <IaTypo03>Övrig intygsuppgifter</IaTypo03>
          <Table striped>
            <tbody>
              <tr>
                <TableTH>Signerat av</TableTH>
                <TableTD>{intygInfo.signedByName}</TableTD>
                {
                  intygInfo.signedByEmail ?
                    <TableTD>E-mail: {intygInfo.signedByEmail}</TableTD>
                    :
                    <TableTD>HSA-ID: {intygInfo.signedByHsaId}</TableTD>
                }
              </tr>
              <tr>
                <TableTH>Vårdenhet</TableTH>
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
                <TableTD><AdminQuestions intygInfo={intygInfo} /> </TableTD>
                <TableTD />
              </tr>
              <tr>
                <TableTH>Utfärdande enhet</TableTH>
                <TableTD>{intygInfo.inWebcert ? 'Webcert' : 'Annan'}</TableTD>
                <TableTD />
              </tr>
            </tbody>
          </Table>
          </RowWrapper>

        </ModalBody>
        <ModalFooter>
          <Button id={'closeBtn'} onClick={handleClose} color={'default'}>
            Stäng
          </Button>
        </ModalFooter>
      </Modal>
  )
}

export const intygInfoDialogId = 'intygInfoDialog'

export default compose(
  modalContainer(intygInfoDialogId),
  connect(null, actions)
)(IntygInfoDialog)
