import React, { useState } from 'react'
import { Button, FormGroup, Input, Label, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap'
import modalContainer from '../../modalContainer/modalContainer'
import { compose } from 'recompose'
import * as actions from '../../../store/actions/dataExport'
import { connect } from 'react-redux'
import styled from 'styled-components'
import { ErrorSection, ErrorWrapper } from '../../styles/iaLayout'
import IaAlert, { alertType } from '../../alert/Alert'
import { IaTypo04 } from '../../styles/iaTypography'
import { getErrorMessageModal } from '../../../store/reducers/users'
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

const initialDataExport = {
  organizationNumber: '',
  personalNumber: '',
  email: '',
  telephoneNUmber: '',
  exportDate: '',
}

const DeleteDataExport = ({ handleClose, isOpen, onComplete, createUser, errorMessage, clearError }) => {
  const [newDataExport, setNewDataExport] = useState(initialDataExport)

  const onChange = (prop) => (value) => {
    setNewDataExport({ ...newDataExport, [prop]: value })
  }

  const createSendObject = () => {
    return {
      organizationNumber: newDataExport.organizationNumber,
    }
  }

  const send = () => {
    const func = createUser(createSendObject()) //TODO: Change and create data export

    func
      .then(() => {
        cancel()
        onComplete()
      })
      .catch(() => {})
  }

  const cancel = () => {
    clearError()
    setNewDataExport(initialDataExport)
    handleClose()
  }

  const enableSaveBtn = () => {
    const fields = ['organizationNumber', 'personalNumber', 'email', 'telephoneNUmber', 'exportDate']

    let enable = fields.reduce((accumulator, currentValue) => {
      return accumulator && newDataExport[currentValue]
    }, true)

    return enable
  }

  return (
    <Modal isOpen={isOpen} size={'md'} backdrop={true} toggle={cancel}>
      <ModalHeader toggle={cancel}> {getMessage(`dataExport.create.organizationNumber`)}</ModalHeader>
      <StyledBody>
        <FormGroup>
          <Label for="dataExportOrganizationNumber">
            <IaTypo04>{getMessage(`dataExport.create.organizationNumber`)}</IaTypo04>
          </Label>
          <Input
            id="dataExportOrganizationNumber"
            value={newDataExport.organizationNumber}
            maxLength={200}
            onChange={(e) => onChange('organizationNumber')(e.target.value)}
          />
        </FormGroup>

        <FormGroup>
          <Label for="dataExportPersonalNumber">
            <IaTypo04>{getMessage(`dataExport.create.personalNumber`)}</IaTypo04>
          </Label>
          <Input
            id="dataExportPersonalNumber"
            value={newDataExport.personalNumber}
            maxLength={200}
            onChange={(e) => onChange('personalNumber')(e.target.value)}
          />
        </FormGroup>

        <FormGroup>
          <Label for="dataExportEmail">
            <IaTypo04>{getMessage(`dataExport.create.email`)}</IaTypo04>
          </Label>
          <Input id="dataExportEmail" value={newDataExport.email} maxLength={200} onChange={(e) => onChange('email')(e.target.value)} />
        </FormGroup>

        <FormGroup>
          <Label for="dataExportTelephoneNUmber">
            <IaTypo04>{getMessage(`dataExport.create.telephoneNUmber`)}</IaTypo04>
          </Label>
          <Input
            id="dataExportTelephoneNUmber"
            value={newDataExport.telephoneNUmber}
            maxLength={200}
            onChange={(e) => onChange('telephoneNUmber')(e.target.value)}
          />
        </FormGroup>

        <FormGroup>
          <Label for="dataExportExportDate">
            <IaTypo04>{getMessage(`dataExport.create.exportDate`)}</IaTypo04>
          </Label>
          <Input
            id="dataExportExportDate"
            value={newDataExport.exportDate}
            maxLength={200}
            onChange={(e) => onChange('exportDate')(e.target.value)}
          />
        </FormGroup>
      </StyledBody>
      <ErrorSection>
        {errorMessage && (
          <ErrorWrapper>
            <IaAlert type={alertType.ERROR}>{errorMessage}</IaAlert>
          </ErrorWrapper>
        )}
      </ErrorSection>
      <ModalFooter className="no-border">
        <Button
          id="saveDataExport"
          disabled={!enableSaveBtn()}
          color={'primary'}
          onClick={() => {
            send()
          }}>
          {getMessage(`dataExport.create.save`)}
        </Button>
        <Button
          id="closeModal"
          color={'default'}
          onClick={() => {
            cancel()
          }}>
          Avbryt
        </Button>
      </ModalFooter>
    </Modal>
  )
}

const mapStateToProps = (state) => {
  return {
    errorMessage: getErrorMessageModal(state),
  }
}

export const DeleteDataExportId = 'deleteDataExport'

export default compose(
  connect(
    mapStateToProps,
    { ...actions }
  ),
  modalContainer(DeleteDataExportId)
)(DeleteDataExport)
