import React , { useState }  from 'react';
import { Button, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';
import modalContainer from '../../modalContainer/modalContainer';
import { compose } from 'recompose';
import * as actions from '../../../store/actions/dataExport';
import { connect } from 'react-redux';
import styled from 'styled-components';
import { ErrorSection, ErrorWrapper } from '../../styles/iaLayout';
import IaAlert, { alertType } from '../../alert/Alert';
import { getErrorMessageEraseDataExport } from "../../../store/reducers/dataExport";
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

const EraseDataExport = ({ handleClose, isOpen, onComplete, eraseDataExport, errorMessage, data }) => {

  const sendEraseDataExport = () => {
    const func = eraseDataExport(data.terminationId);

    func
      .then(() => {
        cancel();
        onComplete();
      })
      .catch(() => {});
  };

  const cancel = () => {
    handleClose();
  };

  return (
    <Modal isOpen={isOpen} size={'md'} backdrop={true} toggle={cancel}>
      <ModalHeader toggle={cancel}> {getMessage(`dataExport.erase.modalHeader`)}</ModalHeader>
      <StyledBody>
        <p>
          Observera! Säkerställ med mottagaren av dataexporten att den mottagit både exporten och kryptonyckeln innan intygen raderas.

          Raderade intyg kan ej återskapas.

          Sker raderingen av intyg för en privatläkare raderas även privatläkarkontot.
        </p>
        <div>{getMessage(`dataExport.erase.careProviderHsaId`, data)}</div>
        <div>{getMessage(`dataExport.erase.organizationNumber`, data)}</div>
        <div>{getMessage(`dataExport.erase.representativePersonId`, data)}</div>
        <div>{getMessage(`dataExport.erase.representativePhoneNumber`, data)}</div>
      </StyledBody>
      <ErrorSection>
        {errorMessage !== null && (
          <ErrorWrapper>
            <IaAlert type={alertType.ERROR}>
              Dataexporten kunde inte tas bort på grund av ett tekniskt fel. Prova igen om en stund.
            </IaAlert>
          </ErrorWrapper>
        )}
      </ErrorSection>
      <ModalFooter className="no-border">
        <Button
          id="eraseDataExport"
          color={'primary'}
          onClick={() => {
            sendEraseDataExport()
          }}>
          {getMessage(`dataExport.erase.delete`)}
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
  return { errorMessage: getErrorMessageEraseDataExport(state) };
};

export const EraseDataExportId = 'eraseDataExport';

export default compose(
  connect(
    mapStateToProps,
    { ...actions }
  ),
  modalContainer(EraseDataExportId)
)(EraseDataExport);
