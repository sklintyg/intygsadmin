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
  careProviderHsaId: '',
  organizationNumber: '',
  representativePersonId: '',
  representativePhoneNumber: '',
};

const CreateDataExport = ({ handleClose, isOpen, onComplete, createDataExport, errorMessage, clearError }) => {
  const [newDataExport, setNewDataExport] = useState(initialDataExport);

  const onChange = (prop) => (value) => {
    setNewDataExport({ ...newDataExport, [prop]: value });
  };

  const createSendObject = () => {
    return {
      careProviderHsaId: newDataExport.careProviderHsaId,
      organizationNumber: newDataExport.organizationNumber,
      representativePersonId: newDataExport.representativePersonId,
      representativePhoneNumber: newDataExport.representativePhoneNumber,
    }
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
    const fields = ['careProviderHsaId', 'organizationNumber', 'representativePersonId', 'representativePhoneNumber'];

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
          <Label for="dataExportCareProviderHsaId">
            <IaTypo04>{getMessage(`dataExport.create.careProviderHsaId`)}</IaTypo04>
          </Label>
          <Input
            id="dataExportCareProviderHsaId"
            value={newDataExport.careProviderHsaId}
            maxLength={200}
            onChange={(e) => onChange('careProviderHsaId')(e.target.value)}
          />
        </FormGroup>
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
          <Label for="dataExportRepresentativePersonId">
            <IaTypo04>{getMessage(`dataExport.create.representativePersonId`)}</IaTypo04>
          </Label>
          <Input
            id="dataExportRepresentativePersonId"
            value={newDataExport.representativePersonId}
            maxLength={200}
            onChange={(e) => onChange('representativePersonId')(e.target.value)}
          />
        </FormGroup>

        <FormGroup>
          <Label for="dataExportRepresentativePhoneNumber">
            <IaTypo04>{getMessage(`dataExport.create.representativePhoneNumber`)}</IaTypo04>
          </Label>
          <Input
            id="representativePhoneNumber"
            value={newDataExport.telephoneNUmber}
            maxLength={200}
            onChange={(e) => onChange('representativePhoneNumber')(e.target.value)}
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
