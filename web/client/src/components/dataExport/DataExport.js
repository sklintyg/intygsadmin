import React from 'react';
import { compose } from 'recompose';
import { connect } from 'react-redux';
import * as actions from '../../store/actions/dataExport';
import { getDataExportList } from '../../store/reducers/dataExport';
import DataExportList from './DataExportList';
import LoadingSpinner from '../loadingSpinner';
import { getIsFetching } from '../../store/reducers/dataExport';
import CreateDataExport from '../dataExport/dialogs/CreateDataExport.dialog';

const DataExport = ({ dataExportList, fetchDataExportList, isFetching }) => {

  const onActionComplete = () => {
    fetchDataExportList();
  };

  return (
    <>
      <CreateDataExport onComplete={onActionComplete} />
      <DataExportList />
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
