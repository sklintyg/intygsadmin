import React from 'react';
import ListPagination from "../styles/ListPagination";
import {compose} from "recompose";
import {connect} from "react-redux";
import * as actions from "../../store/actions/users";
import {getUsersList} from "../../store/reducers/users";
import LoadingSpinner from "../loadingSpinner";
import {getIsFetching} from "../../store/reducers/bannerList.reducer"; //TODO: Change to data export implementation
import CreateDataExport from "../dataExport/dialogs/CreateDataExport.dialog";
import UsersList from "../users/UsersList";

const DataExports = ({usersList, fetchUsersList, isFetching}) => {

  const handlePageChange = (pageNumber) => {
    fetchList(pageNumber)
  }

  const fetchList = (pageIndex) => {
    const pageIndexZeroBased = pageIndex - 1
    fetchUsersList({pageIndex: pageIndexZeroBased});
  }

  const onActionComplete = () => {
    fetchUsersList() //TODO: Fetch data exports
  }

  return (
    <>
      <CreateDataExport onComplete={onActionComplete}/>
      <UsersList /> {/* TODO: Change to DataExportsList*/}
      <ListPagination list={usersList} handlePageChange={handlePageChange} />
      {isFetching && !usersList.length && <LoadingSpinner loading={isFetching} message={'Hämtar data exporter'} />}
    </>
  );
}

const mapStateToProps = (state) => {
  return {
    usersList: getUsersList(state),
    isFetching: getIsFetching(state),
  };
};

export default compose(
  connect(
    mapStateToProps,
    actions
  )
)(DataExports);
