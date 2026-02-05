import React from 'react'
import { fetchBannerList } from '@/store/actions/bannerList.actions'
import BannerListContainer from './BannerListContainer'
import { useAppDispatch, useAppSelector } from '@/store/hooks'
import ListPagination from '../styles/ListPagination'
import { getBannerList } from '@/store/reducers/bannerList.reducer'

const PaginatedListContainer = () => {
  const dispatch = useAppDispatch()
  const bannerList = useAppSelector(getBannerList)

  const handlePageChange = (pageNumber) => {
    fetchList(pageNumber)
  }

  const fetchList = (pageIndex) => {
    const pageIndexZeroBased = pageIndex - 1
    const { sortColumn, sortDirection } = bannerList
    dispatch(fetchBannerList({ pageIndex: pageIndexZeroBased, sortColumn, sortDirection }))
  }

  return (
    <>
      <BannerListContainer />
      <ListPagination list={bannerList} handlePageChange={handlePageChange} />
    </>
  )
}

export default PaginatedListContainer
