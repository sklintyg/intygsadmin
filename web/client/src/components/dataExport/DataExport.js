import React from 'react';
import { compose } from 'recompose';
import { connect } from 'react-redux';
import * as actions from '../../store/actions/dataExport';
import { getDataExportList } from '../../store/reducers/dataExport';
import DataExportList from './DataExportList';
import LoadingSpinner from '../loadingSpinner';
import { getIsFetching } from '../../store/reducers/dataExport';
import CreateDataExport from '../dataExport/dialogs/CreateDataExport.dialog';
import UpdateDataExport from './dialogs/UpdateDataExport.dialog';
import EraseDataExport from '../dataExport/dialogs/EraseDataExport.dialog';
import ResendDataExportKey from '../dataExport/dialogs/ResendDataExportKey.dialog';
import ListPagination from "../styles/ListPagination";

const DataExport = ({ dataExportList, fetchDataExportList, isFetching }) => {

  const handlePageChange = (pageNumber) => {
    fetchList(pageNumber)
  }

  const fetchList = (pageIndex) => {
    const pageIndexZeroBased = pageIndex - 1
    fetchDataExportList({pageIndex: pageIndexZeroBased});
  }

  const onActionComplete = () => {
    fetchDataExportList();
  };

  const onActionCompleteNoPageChange = () => {
    fetchDataExportList({pageIndex: dataExportList.pageIndex});
  };

  return (
    <>
      <CreateDataExport onComplete={onActionComplete} />
      <UpdateDataExport onComplete={onActionCompleteNoPageChange} />
      <EraseDataExport onComplete={onActionCompleteNoPageChange} />
      <ResendDataExportKey onComplete={onActionCompleteNoPageChange} />
      <DataExportList />
      <ListPagination list={dataExportList} handlePageChange={handlePageChange} />
      {isFetching && !dataExportList.length && <LoadingSpinner loading={isFetching} message={'Hämtar data exporter'} />}
    </>
  );
}

const mapStateToProps = (state) => {
  return {
    dataExportList: getDataExportList(state),
    isFetching: getIsFetching(state),
  };
}

export default compose(
  connect(
    mapStateToProps,
    actions
  )
)(DataExport);
