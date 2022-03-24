import React from 'react'
import ListPagination from '../styles/ListPagination'
import { compose } from 'recompose'
import { connect } from 'react-redux'
import * as actions from '../../store/actions/dataExport'
import { getDataExportList, getDataExportStatusList } from '../../store/reducers/dataExport'
import DataExportList from './DataExportList'
import LoadingSpinner from '../loadingSpinner'
import { getIsFetching } from '../../store/reducers/dataExport'
import CreateDataExport from '../dataExport/dialogs/CreateDataExport.dialog'

const DataExport = ({ dataExportList, fetchDataExportList, isFetching }) => {
  const handlePageChange = (pageNumber) => {
    fetchList(pageNumber)
  }

  const fetchList = (pageIndex) => {
    const pageIndexZeroBased = pageIndex - 1
    fetchDataExportList({ pageIndex: pageIndexZeroBased })
  }

  const onActionComplete = () => {
    fetchDataExportList()
  }

  return (
    <>
      <CreateDataExport onComplete={onActionComplete} />
      <DataExportList />
      <ListPagination list={dataExportList} handlePageChange={handlePageChange} />
      {isFetching && !dataExportList.length && <LoadingSpinner loading={isFetching} message={'Hämtar data exporter'} />}
    </>
  )
}

const mapStateToProps = (state) => {
  return {
    dataExportList: getDataExportList(state),
    isFetching: getIsFetching(state),
  }
}

export default compose(
  connect(
    mapStateToProps,
    actions
  )
)(DataExport)
