import React from 'react'
import { Button, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap'
import modalContainer from '../../modalContainer/modalContainer'
import * as actions from '../../../store/actions/dataExport'
import { connect } from 'react-redux'
import styled from 'styled-components'
import { ErrorSection, ErrorWrapper } from '../../styles/iaLayout'
import IaAlert, { alertType } from '../../alert/Alert'
import { getErrorMessageResendDataExportKey } from '../../../store/reducers/dataExport'
import { getMessage } from '../../../messages/messages'

const StyledBody = styled(ModalBody)`
  .form-control {
    width: 100%;
  }

  .form-group {
    margin-bottom: 15px;
  }

  h5 {
    padding: 12px 0 4px;
    &:first-of-type {
      padding: 4px 0;
    }
  }

  label {
    display: block;
  }
`

const ResendDataExportKey = ({ handleClose, isOpen, onComplete, resendDataExportKey, errorMessage, data }) => {
  const sendResendDataExportKey = () => {
    const func = resendDataExportKey(data.terminationId)

    func
      .then(() => {
        cancel()
        onComplete()
      })
      .catch(() => {})
  }

  const cancel = () => {
    handleClose()
  }

  return (
    <Modal isOpen={isOpen} size={'md'} backdrop={true} toggle={cancel}>
      <ModalHeader toggle={cancel}> {getMessage(`dataExport.resend.modalHeader`)}</ModalHeader>
      <StyledBody>
        <p>Här kan du skicka ny kryptonyckel till mottagaren.</p>
        <div>{getMessage(`dataExport.resend.representativePhoneNumber`, data)}</div>
      </StyledBody>
      <ErrorSection>
        {errorMessage !== null && (
          <ErrorWrapper>
            <IaAlert type={alertType.ERROR}>Kryptonyckel kunde inte skickas. Prova igen om en stund.</IaAlert>
          </ErrorWrapper>
        )}
      </ErrorSection>
      <ModalFooter className="no-border">
        <Button
          id="resendDataExportKey"
          color={'primary'}
          onClick={() => {
            sendResendDataExportKey()
          }}>
          {getMessage(`dataExport.resend.send`)}
        </Button>
        <Button
          id="closeModal"
          color={'default'}
          onClick={() => {
            cancel()
          }}>
          {getMessage(`dataExport.resend.abort`)}
        </Button>
      </ModalFooter>
    </Modal>
  )
}

const mapStateToProps = (state) => {
  return { errorMessage: getErrorMessageResendDataExportKey(state) }
}

export const ResendDataExportKeyId = 'resendDataExportKey'

export default connect(mapStateToProps, { ...actions })(modalContainer(ResendDataExportKeyId)(ResendDataExportKey))
