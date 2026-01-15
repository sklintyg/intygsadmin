import React from 'react'
import ListPagination from '../styles/ListPagination'
import { connect } from 'react-redux'
import * as actions from '../../store/actions/users'
import { getUsersList } from '../../store/reducers/users'
import UsersList from './UsersList'
import LoadingSpinner from '../loadingSpinner'
import { getIsFetching } from '../../store/reducers/bannerList.reducer'
import RemoveUser from './dialogs/RemoveUser.dialog'
import CreateUser from './dialogs/CreateUser.dialog'

const Users = ({ usersList, fetchUsersList, isFetching }) => {
  const handlePageChange = (pageNumber) => {
    fetchList(pageNumber)
  }

  const fetchList = (pageIndex) => {
    const pageIndexZeroBased = pageIndex - 1
    fetchUsersList({ pageIndex: pageIndexZeroBased })
  }

  const onActionComplete = () => {
    fetchUsersList()
  }

  return (
    <>
      <RemoveUser onComplete={onActionComplete} />
      <CreateUser onComplete={onActionComplete} />
      <UsersList />
      <ListPagination list={usersList} handlePageChange={handlePageChange} />
      {isFetching && !usersList.length && <LoadingSpinner loading={isFetching} message={'Hämtar administratörer'} />}
    </>
  )
}

const mapStateToProps = (state) => {
  return {
    usersList: getUsersList(state),
    isFetching: getIsFetching(state),
  }
}

export default connect(mapStateToProps, actions)(Users)
