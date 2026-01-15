import React, { useState } from 'react'
import { Button, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap'
import modalContainer from '../modalContainer/modalContainer'
import styled from 'styled-components'
import DisplayDate from '../displayDateTime/DisplayDate'
import { ErrorSection, ErrorWrapper } from '../styles/iaLayout'
import IaAlert, { alertType } from '../alert/Alert'
import { getErrorMessageUnregisterPrivatePractitioner } from '../../store/reducers/privatePractitioner'
import { connect } from 'react-redux'
import * as actions from '../../store/actions/privatePractitioner'

const FlexDiv = styled.div`
  display: flex;
  > h5 {
    line-height: 1.5;
    flex: 0 0 140px;
  }
`

const PrivatePractitionerSearchResult = ({ unregisterPrivatePractitioner, handleClose, isOpen, data }) => {
  const [errorActive, setErrorActive] = useState(false)

  if (!data) {
    return null
  }

  const { text } = data

  const unregister = (hsaId) => {
    setErrorActive(false)
    unregisterPrivatePractitioner(hsaId)
      .then(() => {
        cancel()
      })
      .catch(() => {
        setErrorActive(true)
      })
  }

  const cancel = () => {
    setErrorActive(false)
    handleClose()
  }

  return (
    <Modal id={'privatePractitionerSearchResultId'} isOpen={isOpen} size={'lg'} backdrop={true} toggle={cancel}>
      <ModalHeader toggle={cancel}>Privatläkaren finns registrerad i Webcert</ModalHeader>
      <ModalBody>
        <FlexDiv>
          <h5>Privatläkare:</h5>
          <span>{text.hsaId}</span>
        </FlexDiv>
        <FlexDiv>
          <h5>Namn:</h5>
          <span>{text.name}</span>
        </FlexDiv>
        <FlexDiv>
          <h5>Bolagsnamn:</h5>
          <span>{text.careproviderName}</span>
        </FlexDiv>
        <FlexDiv>
          <h5>Epost:</h5>
          <span>{text.email}</span>
        </FlexDiv>
        <FlexDiv>
          <h5>Registrerad:</h5>
          <span>
            <DisplayDate date={text.registrationDate} />
          </span>
        </FlexDiv>
        <FlexDiv>
          <h5>Utfärdade intyg:</h5>
          <span>{text.hasCertificates}</span>
        </FlexDiv>
      </ModalBody>
      <ErrorSection>
        {errorActive && (
          <ErrorWrapper>
            <IaAlert type={alertType.ERROR}>
              Avregistreringen kunde inte genomföras på grund av tekniskt fel. Prova igen om en stund.
            </IaAlert>
          </ErrorWrapper>
        )}
      </ErrorSection>
      <ModalFooter className="no-border">
        <Button
          id={'unregisterPrivatePractitionerBtn'}
          color={'primary'}
          disabled={text.hasCertificates !== 'Nej'}
          onClick={() => {
            unregister(text.hsaId)
          }}>
          Avregistrera
        </Button>
        <Button id="closeModal" color={'default'} onClick={cancel}>
          Stäng
        </Button>
      </ModalFooter>
    </Modal>
  )
}

const mapStateToProps = (state) => {
  return {
    errorMessage: getErrorMessageUnregisterPrivatePractitioner(state),
  }
}

export const PrivatePractitionerSearchResultId = 'privatePractitionerSearchResult'

export default connect(mapStateToProps, { ...actions })(modalContainer(PrivatePractitionerSearchResultId)(PrivatePractitionerSearchResult))
