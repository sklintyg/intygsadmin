import React, { useEffect, useState } from 'react'
import { Button, FormGroup, Input, Label, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap'
import modalContainer from '../../modalContainer/modalContainer'
import { useDispatch, useSelector } from 'react-redux'
import { updateDataExport } from '../../../store/actions/dataExport'
import styled from 'styled-components'
import { ErrorSection, ErrorWrapper } from '../../styles/iaLayout'
import IaAlert, { alertType } from '../../alert/Alert'
import { IaTypo04 } from '../../styles/iaTypography'
import { getMessage } from '../../../messages/messages'
import { getErrorMessageUpdateDataExport } from '../../../store/reducers/dataExport'

const StyledBody = styled(ModalBody)`
  input[type='text']:disabled {
    color: #6a6a6a;
    background-color: #f2f2f2;
    cursor: not-allowed;
  }

  input {
    width: 100%;
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

const defaultDataExport = {
  terminationId: '',
  created: '',
  status: '',
  creatorName: '',
  creatorHSAId: '',
  hsaId: '',
  organizationNumber: '',
  personId: '',
  emailAddress: '',
  phoneNumber: '',
}

const UpdateDataExport = ({ handleClose, isOpen, onComplete, data }) => {
  const dispatch = useDispatch()
  const errorMessage = useSelector(getErrorMessageUpdateDataExport)
  const originalDataExport = data ? data.dataExport : defaultDataExport
  const [updatedDataExport, setUpdatedDataExport] = useState(originalDataExport)

  useEffect(() => {
    if (
      data &&
      (updatedDataExport.terminationId !== originalDataExport.terminationId || updatedDataExport.status !== originalDataExport.status)
    ) {
      setUpdatedDataExport({ ...originalDataExport })
    }
  }, [originalDataExport, updatedDataExport, data])

  const onChange = (prop) => (value) => {
    setUpdatedDataExport({ ...updatedDataExport, [prop]: value })
  }

  const sendUpdatedDataExport = () => {
    dispatch(updateDataExport(updatedDataExport))
      .then(() => {
        cancel()
        onComplete()
      })
      .catch(() => {})
  }

  const cancel = () => {
    handleClose()
  }

  const enableUpdateBtn = () => {
    const fields = ['hsaId', 'personId', 'emailAddress', 'phoneNumber']
    return fields.reduce((accumulator, currentValue) => {
      return accumulator || updatedDataExport[currentValue] !== originalDataExport[currentValue]
    }, false)
  }

  return (
    <Modal isOpen={isOpen} size={'md'} backdrop={true} toggle={cancel}>
      <ModalHeader toggle={cancel}> {getMessage(`dataExport.update.modalHeader`)}</ModalHeader>
      <StyledBody>
        <p>
          Här kan du ändra uppgifter i dataexporten. Endast uppgifter som kan ändras är redigerbara. Mejl och sms kommer att på nytt skickas
          ut till mottagaren (organisationsrepresentanten).
        </p>
        <FormGroup>
          <Label for="dataExportCreatorHSAId">
            <IaTypo04>{getMessage(`dataExport.create.careProviderHsaId`)}</IaTypo04>
          </Label>
          <Input
            id="hsaId"
            value={updatedDataExport.hsaId}
            placeholder={getMessage(`dataExport.create.careProviderHsaIdPlaceholder`)}
            maxLength={200}
            onChange={(e) => onChange('hsaId')(e.target.value)}
          />
        </FormGroup>
        <FormGroup>
          <Label for="dataExportOrganizationNumber">
            <IaTypo04>{getMessage(`dataExport.create.organizationNumber`)}</IaTypo04>
          </Label>
          <Input
            id="organizationNumber"
            disabled={true}
            value={updatedDataExport.organizationNumber}
            placeholder={getMessage(`dataExport.create.organizationNumberPlaceholder`)}
            maxLength={200}
            onChange={(e) => onChange('organizationNumber')(e.target.value)}
          />
        </FormGroup>
        <FormGroup>
          <Label for="dataExportRepresentativePersonId">
            <IaTypo04>{getMessage(`dataExport.create.representativePersonId`)}</IaTypo04>
          </Label>
          <Input
            id="personId"
            value={updatedDataExport.personId}
            placeholder={getMessage(`dataExport.create.representativePersonIdPlaceholder`)}
            maxLength={200}
            onChange={(e) => onChange('personId')(e.target.value)}
          />
        </FormGroup>
        <FormGroup>
          <Label for="dataExportEmailAddress">
            <IaTypo04>{getMessage(`dataExport.create.representativeEmailAddress`)}</IaTypo04>
          </Label>
          <Input
            id="emailAddress"
            value={updatedDataExport.emailAddress}
            maxLength={200}
            onChange={(e) => onChange('emailAddress')(e.target.value)}
          />
        </FormGroup>
        <FormGroup>
          <Label for="dataExportPhoneNumber">
            <IaTypo04>{getMessage(`dataExport.create.representativePhoneNumber`)}</IaTypo04>
          </Label>
          <Input
            id="phoneNumber"
            value={updatedDataExport.phoneNumber}
            placeholder={getMessage(`dataExport.create.representativePhoneNumberPlaceholder`)}
            maxLength={200}
            onChange={(e) => onChange('phoneNumber')(e.target.value)}
          />
        </FormGroup>
      </StyledBody>
      <ErrorSection>
        {errorMessage !== null && (
          <ErrorWrapper>
            <IaAlert type={alertType.ERROR}>Kunde inte ändra dataexporten på grund av tekniskt fel. Prova igen om en stund.</IaAlert>
          </ErrorWrapper>
        )}
      </ErrorSection>
      <ModalFooter className="no-border">
        <Button
          id="updateDataExport"
          disabled={!enableUpdateBtn()}
          color={'primary'}
          onClick={() => {
            sendUpdatedDataExport()
          }}>
          {getMessage(`dataExport.update.change`)}
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

export const UpdateDataExportId = 'changeDataExport'

export default modalContainer(UpdateDataExportId)(UpdateDataExport)
