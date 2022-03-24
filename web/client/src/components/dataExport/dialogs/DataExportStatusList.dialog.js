import React from 'react'
import { Button, Table, FormGroup, Input, Label, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap'
import modalContainer from '../../modalContainer/modalContainer'
import { compose } from 'recompose'
import * as actions from '../../../store/actions/dataExport'
import { connect } from 'react-redux'
import styled from 'styled-components'
import { ErrorSection, ErrorWrapper } from '../../styles/iaLayout'
import IaAlert, { alertType } from '../../alert/Alert'
import dataExport, { getErrorMessage, getIsFetching, getDataExportStatusList } from '../../../store/reducers/dataExport'
import { getMessage } from '../../../messages/messages'
import DisplayDateTime from '../../displayDateTime/DisplayDateTime'

const StyledBody = styled(ModalBody)`
  .form-control {
    width: 100%;
  }

  h5 {
    padding: 12px 0 4px;
    &:first-of-type {
      padding: 4px 0;
    }
  }
`

const ResultLine = styled.div`
  padding: 20px 0 10px 0;
`

const Wrapper = styled.div`
  & th:last-child {
    width: 1%;
  }
`

const DataExportStatusList = ({ dataExportStatusList, dataExportId, handleClose, isOpen, errorMessage }) => {
  const cancel = () => {
    handleClose()
  }

  if (dataExportStatusList.content && dataExportStatusList.content.length === 0) {
    if (dataExportStatusList.totalElements === 0) {
      return (
        <ResultLine>
          <IaAlert type={alertType.INFO}>Det finns inga statusar att visa ännu.</IaAlert>
        </ResultLine>
      )
    }
  }

  const fetchData = ({ dataExportId }) => {
    actions.fetchDataExportStatusList({ dataExportId: dataExportId })
  }

  fetchData(dataExportId)

  return (
    <Modal isOpen={isOpen} size={'md'} backdrop={true} toggle={cancel}>
      <ModalHeader toggle={cancel}> {getMessage(`dataExport.create.modalHeader`)}</ModalHeader>
      <StyledBody>
        <Wrapper>
          <Table striped id="dataExportStatusTable">
            <thead>
              <tr>
                <th>Tidpunkt</th>
                <th>Status</th>
              </tr>
            </thead>
            <tbody>
              {dataExportStatusList.content &&
                dataExportStatusList.content.map((dataExportStatus) => (
                  <tr key={dataExportStatus.id}>
                    <td>
                      <DisplayDateTime date={dataExportStatus.time} />
                    </td>
                    <td>{dataExportStatus.status}></td>
                  </tr>
                ))}
            </tbody>
          </Table>
        </Wrapper>
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
    dataExportStatusList: getDataExportStatusList(state),
    isFetching: getIsFetching(state),
    errorMessage: getErrorMessage(state),
  }
}

export const DataExportStatusListId = 'dataExportStatusList'

export default compose(
  connect(
    mapStateToProps,
    { ...actions }
  ),
  modalContainer(DataExportStatusListId)
)(DataExportStatusList)
