import React from 'react'
import { connect } from 'react-redux'
import {compose, lifecycle} from 'recompose'
import styled from 'styled-components'
import TableSortHead from '../styles/TableSortHead'
import { Table } from 'reactstrap'
import * as actions from '../../store/actions/intygInfoList'
import DisplayDateTime from '../displayDateTime/DisplayDateTime';
import IaAlert, {alertType} from "../alert/Alert";
import {getErrorMessage, getIntygInfoList, getIsFetching} from "../../store/reducers/intygInfoList";

const ResultLine = styled.div`
  padding: 20px 0 10px 0;
`

const Wrapper = styled.div`
  & th:last-child {
    width: 1%;
  }
`

const IntygInfoHistoryList = ({ intygInfoList, onSort, errorMessage }) => {
  if (errorMessage) {
    return (
      <IaAlert type={alertType.ERROR}>{errorMessage}</IaAlert>
    )
  }

  const handleSort = (sortColumn) => {
    onSort(sortColumn)
  }

  return (
    <Wrapper>
      <ResultLine>
        Visar {intygInfoList.start}-{intygInfoList.end} av {intygInfoList.totalElements} sökningar
      </ResultLine>
      <Table striped>
        <thead>
          <tr>
            <TableSortHead
              currentSortColumn={intygInfoList.sortColumn}
              currentSortDirection={intygInfoList.sortDirection}
              text="Tidpunkt"
              sortId="createdAt"
              onSort={handleSort}
            />
            <TableSortHead
              currentSortColumn={intygInfoList.sortColumn}
              currentSortDirection={intygInfoList.sortDirection}
              text="Administratör"
              sortId="user"
              onSort={handleSort}
            />
            <TableSortHead
              currentSortColumn={intygInfoList.sortColumn}
              currentSortDirection={intygInfoList.sortDirection}
              text="Intygs-ID"
              sortId="intygId"
              onSort={handleSort}
            />
          </tr>
        </thead>
        <tbody id={'IntygInfoListTable'}>
        { intygInfoList.content && intygInfoList.content.map((intygInfo) => (
          <tr key={intygInfo.intygId}>
            <td><DisplayDateTime date={intygInfo.createdAt} includeSeconds={true} /></td>
            <td>{intygInfo.user}</td>
            <td>{intygInfo.intygId}</td>
          </tr>
        ))}
        </tbody>
      </Table>
    </Wrapper>
  )
}

const fetchData = ({ fetchIntygInfoList, sortColumn, sortDirection }) => {
  fetchIntygInfoList({ sortColumn, sortDirection })
}

const lifeCycleValues = {
  componentDidMount() {
    fetchData(this.props)
  },
}

const mapStateToProps = (state) => {
  return {
    intygInfoList: getIntygInfoList(state),
    isFetching: getIsFetching(state),
    errorMessage: getErrorMessage(state)
  }
}

export default compose(
  connect(
    mapStateToProps,
    { ...actions }
  ),
  lifecycle(lifeCycleValues)
)(IntygInfoHistoryList)
