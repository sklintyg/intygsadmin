import React from 'react';
import { connect } from 'react-redux';
import { compose, lifecycle } from 'recompose';
import styled from 'styled-components';
import TableSortHead from '../styles/TableSortHead';
import { Table } from 'reactstrap';
import * as actions from '../../store/actions/dataExport';
import * as modalActions from '../../store/actions/modal';
import IaAlert, { alertType } from '../alert/Alert';
import { getErrorMessage, getIsFetching, getDataExportList } from '../../store/reducers/dataExport';
import DisplayDateTime from '../displayDateTime/DisplayDateTime';

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
  if (errorMessage) {
    return (
      <ResultLine>
        <IaAlert type={alertType.ERROR}>{errorMessage}</IaAlert>
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
          </tr>
        </thead>
        <tbody>
          {dataExportList.content &&
            dataExportList.content.map((dataExport, index) => (
              <tr key={dataExport.id}>
                <td>
                  <DisplayDateTime date={dataExport.created} />
                </td>
                <td>{dataExport.creatorName}</td>
                <td>{dataExport.terminationId}</td>
                <td>{dataExport.status}</td>
                <td>{dataExport.hsaId}</td>
                <td>{dataExport.organizationNumber}</td>
                <td>{dataExport.personId}</td>
                <td id={"emailAddress-" + index} className={"emailAddress"} title={dataExport.emailAddress}>{dataExport.emailAddress}</td>
                <td>{dataExport.phoneNumber}</td>
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
    errorMessage: getErrorMessage(state),
  };
};

export default compose(
  connect(
    mapStateToProps,
    { ...actions, ...modalActions }
  ),
  lifecycle(lifeCycleValues)
)(DataExportList);
