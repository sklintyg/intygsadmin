import React from 'react'
import { connect } from 'react-redux'
import { compose, lifecycle } from 'recompose'
import styled from 'styled-components'
import TableSortHead from '../styles/TableSortHead'
import { Button, Table, UncontrolledTooltip } from 'reactstrap'
import * as actions from '../../store/actions/dataExport'
import * as modalActions from '../../store/actions/modal'
import IaAlert, { alertType } from '../alert/Alert'
import dataExport, { getErrorMessage, getIsFetching, getDataExportList } from '../../store/reducers/dataExport'
import DataExportStatus from './DataExportStatus'
import { ClearIcon, Create } from '../styles/iaSvgIcons'
import { CreateDataExportId } from './dialogs/CreateDataExport.dialog'
import { DataExportStatusListId } from './dialogs/DataExportStatusList.dialog'
import { DeleteDataExportId } from './dialogs/DeleteDataExport.dialog'
import DisplayDateTime from '../displayDateTime/DisplayDateTime'

const ResultLine = styled.div`
  padding: 20px 0 10px 0;
`

const ButtonWrapper = styled.div`
  display: inline-block;
`

const Wrapper = styled.div`
  & th:last-child {
    width: 1%;
  }
`

const DisabledButton = styled(Button)`
  pointer-events: none;
`

const DisabledChange = ({ dataExportId }) => (
  <ButtonWrapper id={`changeBtnDiv${dataExportId}`}>
    <DisabledButton className="change-btn" id={`changeBtn${dataExportId}`} disabled={true} color="primary">
      <Create /> Ändra
    </DisabledButton>
    <UncontrolledTooltip trigger="hover" placement="top" target={`changeBtnDiv${dataExportId}`}>
      Öppnar ett dialogfönster där du kan ändra informationen som registrerats i skapa export-modalen.
    </UncontrolledTooltip>
  </ButtonWrapper>
)

const ActiveChange = ({ dataExportId, openChangeModal }) => (
  <ButtonWrapper id={`changeBtnDiv${dataExportId}`}>
    <Button
      className="change-btn"
      id={`changeBtn${dataExportId}`}
      onClick={() => {
        openChangeModal(dataExportId)
      }}
      color="primary">
      <Create /> Ändra
    </Button>
    <UncontrolledTooltip trigger="hover" placement="top" target={`changeBtnDiv${dataExportId}`}>
      Öppnar ett dialogfönster där du kan ändra informationen som registrerats i skapa export-modalen.
    </UncontrolledTooltip>
  </ButtonWrapper>
)

const DisabledDelete = ({ dataExportId }) => (
  <ButtonWrapper id={`endBtnDiv${dataExportId}`}>
    <DisabledButton className="end-btn" id={`endBtn${dataExportId}`} disabled={true} color="default">
      <ClearIcon /> Radera
    </DisabledButton>
    <UncontrolledTooltip trigger="hover" placement="top" target={`endBtnDiv${dataExportId}`}>
      Öppnar modal/ dialogfönster där du bekräftar radering av intyg.
    </UncontrolledTooltip>
  </ButtonWrapper>
)

const ActiveDelete = ({ dataExportId, openDeleteModal }) => (
  <>
    <Button
      className="delete-btn"
      id={`deleteBtn${dataExportId}`}
      onClick={() => {
        openDeleteModal(dataExportId)
      }}
      color="default">
      <ClearIcon /> Radera
    </Button>
    <UncontrolledTooltip trigger="hover" placement="top" target={`deleteBtn${dataExportId}`}>
      Öppnar modal/ dialogfönster där du bekräftar radering av intyg.
    </UncontrolledTooltip>
  </>
)

const DisplayStatus = ({ dataExportId, openStatusModal }) => (
  <ButtonWrapper id={`statusBtnDiv${dataExportId}`}>
    <Button
      className="status-btn"
      id={`statusBtn${dataExportId}`}
      onClick={() => {
        openStatusModal(dataExportId)
      }}
      color="primary">
      Visa statusar
    </Button>
    <UncontrolledTooltip trigger="hover" placement="top" target={`statusBtnDiv${dataExportId}`}>
      Öppnar modal som visar alla tidigare statusar för dataexport.
    </UncontrolledTooltip>
  </ButtonWrapper>
)

const DataExportList = ({ dataExportList, errorMessage, openModal, ...otherProps }) => {
  if (errorMessage) {
    return (
      <ResultLine>
        <IaAlert type={alertType.ERROR}>{errorMessage}</IaAlert>
      </ResultLine>
    )
  }

  if (dataExportList.content && dataExportList.content.length === 0) {
    if (dataExportList.totalElements === 0) {
      return (
        <ResultLine>
          <IaAlert type={alertType.INFO}>Det finns inga dataexporter att visa ännu.</IaAlert>
        </ResultLine>
      )
    }
  }

  const handleSort = (newSortColumn) => {
    let { sortColumn, sortDirection } = dataExportList
    if (sortColumn === newSortColumn) {
      sortDirection = dataExportList.sortDirection === 'DESC' ? 'ASC' : 'DESC'
    } else {
      sortColumn = newSortColumn
    }

    fetchData({ ...otherProps, sortColumn, sortDirection })
  }

  const openDeleteModal = (dataExportId) => {
    openModal(DeleteDataExportId, { dataExportId })
  }

  const openStatusModal = (dataExportId) => {
    openModal(DataExportStatusListId, { dataExportId })
  }

  const openChangeModal = (dataExport) => {
    openModal(CreateDataExportId, { dataExport })
  }

  return (
    <Wrapper>
      <ResultLine>
        Visar {dataExportList.start}-{dataExportList.end} av {dataExportList.totalElements} exporter
      </ResultLine>
      <Table striped id="dataExportTable">
        <thead>
          <tr>
            <TableSortHead
              currentSortColumn={dataExportList.sortColumn}
              currentSortDirection={dataExportList.sortDirection}
              text="Tidpunkt"
              sortId="createdAt"
              onSort={handleSort}
            />
            <TableSortHead
              currentSortColumn={dataExportList.sortColumn}
              currentSortDirection={dataExportList.sortDirection}
              text="Administratör"
              sortId="administratorName"
              onSort={handleSort}
            />
            <TableSortHead
              currentSortColumn={dataExportList.sortColumn}
              currentSortDirection={dataExportList.sortDirection}
              text="Dataexport-ID"
              sortId="id"
              onSort={handleSort}
            />
            <TableSortHead
              currentSortColumn={dataExportList.sortColumn}
              currentSortDirection={dataExportList.sortDirection}
              text="Status"
              sortId="status"
              onSort={handleSort}
            />
            <th />
            <th />
            <th />
          </tr>
        </thead>
        <tbody>
          {dataExportList.content &&
            dataExportList.content.map((dataExport) => (
              <tr key={dataExport.id}>
                <td>
                  <DisplayDateTime date={dataExport.createdAt} />
                </td>
                <td>{dataExport.administratorName}</td>
                <td>{dataExport.id}</td>
                <td>{dataExport.status}</td>
                <td>
                  <DisplayStatus dataExportId={dataExport.id} openStatusModal={openStatusModal} />
                </td>
                <td>
                  {dataExport.status !== DataExportStatus.CRYPTO_KEY_DELIVERED ? (
                    <ActiveChange dataExportId={dataExport.id} openChangeModal={openChangeModal} />
                  ) : (
                    <DisabledChange dataExportId={dataExport.id} />
                  )}
                </td>
                <td>
                  {dataExport.status !== DataExportStatus.CRYPTO_KEY_DELIVERED ? (
                    <ActiveDelete dataExportId={dataExport.id} openDeleteModal={openDeleteModal} />
                  ) : (
                    <DisabledDelete dataExportId={dataExport.id} />
                  )}
                </td>
              </tr>
            ))}
        </tbody>
      </Table>
    </Wrapper>
  )
}

const fetchData = ({ fetchDataExportList, sortColumn, sortDirection }) => {
  fetchDataExportList({ sortColumn, sortDirection })
}

const lifeCycleValues = {
  componentDidMount() {
    fetchData(this.props)
  },
}

const mapStateToProps = (state) => {
  return {
    dataExportList: getDataExportList(state),
    isFetching: getIsFetching(state),
    errorMessage: getErrorMessage(state),
  }
}

export default compose(
  connect(
    mapStateToProps,
    { ...actions, ...modalActions }
  ),
  lifecycle(lifeCycleValues)
)(DataExportList)
