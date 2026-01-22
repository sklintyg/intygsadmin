import React, { useCallback, useEffect } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import styled from 'styled-components'
import TableSortHead from '../styles/TableSortHead'
import { Table } from 'reactstrap'
import * as actions from '../../store/actions/intygInfoList'
import DisplayDateTime from '../displayDateTime/DisplayDateTime'
import IaAlert, { alertType } from '../alert/Alert'
import { getErrorMessage, getIntygInfoList } from '../../store/reducers/intygInfoList'

const ResultLine = styled.div`
  padding: 20px 0 10px 0;
`

const IntygInfoHistoryList = () => {
  const dispatch = useDispatch()
  const intygInfoList = useSelector(getIntygInfoList)
  const errorMessage = useSelector(getErrorMessage)

  const fetchIntygInfoList = useCallback((params) => dispatch(actions.fetchIntygInfoList(params)), [dispatch])

  useEffect(() => {
    const { sortColumn, sortDirection } = intygInfoList
    if (sortColumn !== undefined) {
      fetchIntygInfoList({ sortColumn, sortDirection })
    }
  }, [fetchIntygInfoList, intygInfoList])

  if (errorMessage) {
    return (
      <ResultLine>
        <IaAlert type={alertType.ERROR}>{errorMessage}</IaAlert>
      </ResultLine>
    )
  }

  if (intygInfoList.content && intygInfoList.content.length === 0) {
    if (intygInfoList.totalElements === 0) {
      return <ResultLine></ResultLine>
    }
  }

  const handleSort = (newSortColumn) => {
    let { sortColumn, sortDirection } = intygInfoList
    if (sortColumn === newSortColumn) {
      sortDirection = intygInfoList.sortDirection === 'DESC' ? 'ASC' : 'DESC'
    } else {
      sortColumn = newSortColumn
    }

    fetchIntygInfoList({ sortColumn, sortDirection })
  }

  return (
    <>
      <ResultLine>
        Visar {intygInfoList.start}-{intygInfoList.end} av {intygInfoList.totalElements} sökningar
      </ResultLine>
      <Table striped id="intygInfoHistoryTable">
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
              sortId="employeeName"
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
          {intygInfoList.content &&
            intygInfoList.content.map((intygInfo) => (
              <tr key={intygInfo.id}>
                <td>
                  <DisplayDateTime date={intygInfo.createdAt} includeSeconds={true} />
                </td>
                <td>{intygInfo.employeeName}</td>
                <td>{intygInfo.intygId}</td>
              </tr>
            ))}
        </tbody>
      </Table>
    </>
  )
}

export default IntygInfoHistoryList
