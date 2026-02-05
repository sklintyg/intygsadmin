import React from 'react'
import ListPagination from '../styles/ListPagination'
import { useAppDispatch, useAppSelector } from '@/store/hooks'
import { fetchUsersList } from '@/store/actions/users'
import { getUsersList } from '@/store/reducers/users'
import UsersList from './UsersList'
import LoadingSpinner from '../loadingSpinner'
import { getIsFetching } from '@/store/reducers/bannerList.reducer'
import RemoveUser from './dialogs/RemoveUser.dialog'
import CreateUser from './dialogs/CreateUser.dialog'

const Users = () => {
  const dispatch = useAppDispatch()
  const usersList = useAppSelector(getUsersList)
  const isFetching = useAppSelector(getIsFetching)

  const handlePageChange = (pageNumber) => {
    fetchList(pageNumber)
  }

  const fetchList = (pageIndex) => {
    const pageIndexZeroBased = pageIndex - 1
    const { sortColumn, sortDirection } = usersList
    dispatch(fetchUsersList({ pageIndex: pageIndexZeroBased, sortColumn, sortDirection }))
  }

  const onActionComplete = () => {
    dispatch(fetchUsersList())
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

export default Users
