import React from 'react';
import { connect } from 'react-redux';
import { compose, lifecycle } from 'recompose';
import styled from 'styled-components';
import TableSortHead from '../styles/TableSortHead';
import {Button, Table, UncontrolledTooltip} from 'reactstrap';
import * as actions from '../../store/actions/dataExport';
import * as modalActions from '../../store/actions/modal';
import IaAlert, { alertType } from '../alert/Alert';
import { getErrorMessageFetchDataExportList, getIsFetching, getDataExportList } from '../../store/reducers/dataExport';
import DisplayDateTime from '../displayDateTime/DisplayDateTime';
import {ClearIcon, LoadIcon, Create as CreateIcon} from "../styles/iaSvgIcons";
import { EraseDataExportId } from './dialogs/EraseDataExport.dialog';
import { ResendDataExportKeyId } from './dialogs/ResendDataExportKey.dialog';
import { UpdateDataExportId } from "./dialogs/UpdateDataExport.dialog";

const ResultLine = styled.div`
  padding: 20px 0 10px 0;
`;

const Wrapper = styled.div`
  & th:last-child {
    width: 1%;
  }

  .emailAddress {
    max-width: 125px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }
`;

const DataExportList = ({ dataExportList, errorMessage, openModal, ...otherProps }) => {
  if (errorMessage !== null) {
    return (
      <ResultLine>
        <IaAlert type={alertType.ERROR}>Information om de senaste dataexporterna kunde inte visas på grund av ett tekniskt fel. Prova igen om en stund.</IaAlert>
      </ResultLine>
    );
  }

  if (dataExportList.content && dataExportList.content.length === 0) {
    if (dataExportList.totalElements === 0) {
      return (
        <ResultLine>
          <IaAlert type={alertType.INFO}>Det finns inga dataexporter att visa ännu.</IaAlert>
        </ResultLine>
      );
    }
  }

  const openEraseModal = (terminationId, hsaId, organizationNumber, personId, phoneNumber) => {
    openModal(EraseDataExportId, { terminationId, hsaId, organizationNumber, personId, phoneNumber })
  };

  const openUpdateModal = (dataExport) => {
    openModal(UpdateDataExportId, { dataExport })
  };

  const openResendKeyModal = (terminationId, hsaId, organizationNumber, personId, phoneNumber) => {
    openModal(ResendDataExportKeyId, { terminationId, hsaId, organizationNumber, personId, phoneNumber })
  };

  const updateAvailable = (status) => {
    return status === 'Skapad' || status === 'Hämtar intyg' || status === 'Intyg hämtade' || status === 'Intygstexter hämtade' ||
      status === 'Uppladdat' || status === 'Notifiering skickad' || status === 'Påminnelse skickad'
  }

  const eraseAvailable = (status) => {
    return status === 'Kryptonyckel skickad' || status === "Kryptonyckel skickad igen"
  }

  const handleSort = (newSortColumn) => {
    let { sortColumn, sortDirection } = dataExportList;
    if (sortColumn === newSortColumn) {
      sortDirection = dataExportList.sortDirection === 'DESC' ? 'ASC' : 'DESC';
    } else {
      sortColumn = newSortColumn;
    }

    fetchData({ ...otherProps, sortColumn, sortDirection });
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
              sortId="creatorName"
              onSort={handleSort}
            />
            <TableSortHead
              currentSortColumn={dataExportList.sortColumn}
              currentSortDirection={dataExportList.sortDirection}
              text="Dataexport-ID"
              sortId="terminationId"
              onSort={handleSort}
            />
            <TableSortHead
              currentSortColumn={dataExportList.sortColumn}
              currentSortDirection={dataExportList.sortDirection}
              text="Status"
              sortId="status"
              onSort={handleSort}
            />
            <TableSortHead
              currentSortColumn={dataExportList.sortColumn}
              currentSortDirection={dataExportList.sortDirection}
              text="HSA-ID"
              sortId="careProviderHsaId"
              onSort={handleSort}
            />
            <TableSortHead
              currentSortColumn={dataExportList.sortColumn}
              currentSortDirection={dataExportList.sortDirection}
              text="Organisationsnr"
              sortId="organizationNumber"
              onSort={handleSort}
            />
            <TableSortHead
              currentSortColumn={dataExportList.sortColumn}
              currentSortDirection={dataExportList.sortDirection}
              text="Personnummer"
              sortId="representativePersonId"
              onSort={handleSort}
            />
            <TableSortHead
              currentSortColumn={dataExportList.sortColumn}
              currentSortDirection={dataExportList.sortDirection}
              text="Epostadress"
              sortId="representativeEmailAddress"
              onSort={handleSort}
            />
            <TableSortHead
              currentSortColumn={dataExportList.sortColumn}
              currentSortDirection={dataExportList.sortDirection}
              text="Mobilnummer"
              sortId="representativePhoneNumber"
              onSort={handleSort}
            />
            <th />
          </tr>
        </thead>
        <tbody>
          {dataExportList.content &&
            dataExportList.content.map((dataExport, index) => (
              <tr key={dataExport.terminationId}>
                <td>
                  <DisplayDateTime date={dataExport.created} />
                </td>
                <td>{dataExport.creatorName}</td>
                <td>{dataExport.terminationId}</td>
                <td>
                  {dataExport.status}
                  <LoadIcon
                    onClick={() => {
                      openResendKeyModal(dataExport.terminationId, dataExport.hsaId, dataExport.organizationNumber, dataExport.personId, dataExport.phoneNumber)}}
                    visibility={dataExport.status === 'Kryptonyckel skickad' || dataExport.status === 'Kryptonyckel skickad igen' ? "visible" : "hidden"}
                    id={`resendKey${dataExport.terminationId}`}
                    cursor={'pointer'}
                />
                  <UncontrolledTooltip trigger='hover' placement="top" target={`resendKey${dataExport.terminationId}`}>
                    Öppnar modal där du kan göra nytt utskick av om kryptonyckel.
                  </UncontrolledTooltip>
                </td>
                <td>{dataExport.hsaId}</td>
                <td>{dataExport.organizationNumber}</td>
                <td>{dataExport.personId}</td>
                <td id={"emailAddress-" + index} className={"emailAddress"} title={dataExport.emailAddress}>{dataExport.emailAddress}</td>
                <td>{dataExport.phoneNumber}</td>
                {updateAvailable(dataExport.status) &&
                  <td>
                    <Button className='change-btn'
                            id={`changeBtn${dataExport.terminationId}`}
                            onClick={() => {
                              openUpdateModal(dataExport)
                            }} color="primary">
                      <CreateIcon /> Ändra
                    </Button>
                    <UncontrolledTooltip trigger='hover' placement="top" target={`changeBtn${dataExport.terminationId}`}>
                      Öppnar modal där du kan ändra informationen som registrerats i skapa export-modalen.
                    </UncontrolledTooltip>
                  </td>
                }
                {eraseAvailable(dataExport.status) &&
                  <td>
                    <Button className='end-btn' id={`endBtn${dataExport.terminationId}`}
                      onClick={() => {
                        openEraseModal(dataExport.terminationId, dataExport.hsaId, dataExport.organizationNumber, dataExport.personId,
                        dataExport.phoneNumber)
                      }} color="primary">
                      <ClearIcon /> Avsluta
                    </Button>
                    <UncontrolledTooltip trigger='hover' placement="top" target={`endBtn${dataExport.terminationId}`}>
                      Öppnar ett dialogfönster där du kan radera all data kring ett avslut.
                    </UncontrolledTooltip>
                  </td>
                }
                {!updateAvailable(dataExport.status) && !eraseAvailable(dataExport.status) &&
                  <td />
                }
              </tr>
            ))}
        </tbody>
      </Table>
    </Wrapper>
  );
};

const fetchData = ({ fetchDataExportList, sortColumn, sortDirection }) => {
  fetchDataExportList({ sortColumn, sortDirection });
};

const lifeCycleValues = {
  componentDidMount() {
    fetchData(this.props)
  }
};

const mapStateToProps = (state) => {
  return {
    dataExportList: getDataExportList(state),
    isFetching: getIsFetching(state),
    errorMessage: getErrorMessageFetchDataExportList(state),
  };
};

export default compose(
  connect(
    mapStateToProps,
    { ...actions, ...modalActions }
  ),
  lifecycle(lifeCycleValues)
)(DataExportList);
