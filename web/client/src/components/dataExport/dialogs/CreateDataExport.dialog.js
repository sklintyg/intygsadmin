import React, { useState } from 'react';
import { Button, FormGroup, Input, Label, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';
import modalContainer from '../../modalContainer/modalContainer';
import { compose } from 'recompose';
import * as actions from '../../../store/actions/dataExport';
import { connect } from 'react-redux';
import styled from 'styled-components';
import { ErrorSection, ErrorWrapper } from '../../styles/iaLayout';
import IaAlert, { alertType } from '../../alert/Alert';
import { IaTypo04 } from '../../styles/iaTypography';
import { getErrorMessageModal } from '../../../store/reducers/users';
import { getMessage } from '../../../messages/messages';

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
`;

const initialDataExport = {
  hsaId: '',
  organizationNumber: '',
  personId: '',
  phoneNumber: '',
};

const CreateDataExport = ({ handleClose, isOpen, onComplete, createDataExport, errorMessage, clearError }) => {
  const [newDataExport, setNewDataExport] = useState(initialDataExport);

  const onChange = (prop) => (value) => {
    setNewDataExport({ ...newDataExport, [prop]: value });
  };

  const createSendObject = () => {
    return {
      hsaId: newDataExport.hsaId,
      organizationNumber: newDataExport.organizationNumber,
      personId: newDataExport.personId,
      phoneNumber: newDataExport.phoneNumber
    };
  };

  const send = () => {
    const func = createDataExport(createSendObject());

    func
      .then(() => {
        cancel();
        onComplete();
      })
      .catch(() => {});
  };

  const cancel = () => {
    setNewDataExport(initialDataExport);
    handleClose();
  };

  const enableSaveBtn = () => {
    const fields = ['hsaId', 'organizationNumber', 'personId', 'phoneNumber'];

    let enable = fields.reduce((accumulator, currentValue) => {
      return accumulator && newDataExport[currentValue];
    }, true);

    return enable;
  };

  return (
    <Modal isOpen={isOpen} size={'md'} backdrop={true} toggle={cancel}>
      <ModalHeader toggle={cancel}> {getMessage(`dataExport.create.modalHeader`)}</ModalHeader>
      <StyledBody>
        <FormGroup>
          <Label for="dataExportCreatorHSAId">
            <IaTypo04>{getMessage(`dataExport.create.careProviderHsaId`)}</IaTypo04>
          </Label>
          <Input
            id="hsaId"
            value={newDataExport.hsaId}
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
            value={newDataExport.organizationNumber}
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
            value={newDataExport.personId}
            placeholder={getMessage(`dataExport.create.representativePersonIdPlaceholder`)}
            maxLength={200}
            onChange={(e) => onChange('personId')(e.target.value)}
          />
        </FormGroup>

        <FormGroup>
          <Label for="dataExportPhoneNumber">
            <IaTypo04>{getMessage(`dataExport.create.representativePhoneNumber`)}</IaTypo04>
          </Label>
          <Input
            id="phoneNumber"
            value={newDataExport.telephoneNUmber}
            placeholder={getMessage(`dataExport.create.representativePhoneNumberPlaceholder`)}
            maxLength={200}
            onChange={(e) => onChange('phoneNumber')(e.target.value)}
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
  );
}

const mapStateToProps = (state) => {
  return { errorMessage: getErrorMessageModal(state) };
};

export const CreateDataExportId = 'createDataExport';

export default compose(
  connect(
    mapStateToProps,
    { ...actions }
  ),
  modalContainer(CreateDataExportId)
)(CreateDataExport);
