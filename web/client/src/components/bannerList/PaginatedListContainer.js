import React from 'react'
import * as actions from '../../store/actions/bannerList.actions'
import BannerListContainer from './BannerListContainer'
import { connect } from 'react-redux'
import ListPagination from '../styles/ListPagination'
import { getBannerList } from '../../store/reducers/bannerList.reducer'

const PaginatedListContainer = ({ bannerList, fetchBannerList }) => {
  const handlePageChange = (pageNumber) => {
    fetchList(pageNumber)
  }

  const fetchList = (pageIndex) => {
    const pageIndexZeroBased = pageIndex - 1
    fetchBannerList({ pageIndex: pageIndexZeroBased })
  }

  return (
    <>
      <BannerListContainer />
      <ListPagination list={bannerList} handlePageChange={handlePageChange} />
    </>
  )
}

const mapStateToProps = (state) => {
  return {
    bannerList: getBannerList(state),
  }
}

export default connect(mapStateToProps, actions)(PaginatedListContainer)
