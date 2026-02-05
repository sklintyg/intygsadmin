import React, { Fragment, useCallback, useEffect } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import * as actions from '../../store/actions/bannerList.actions'
import { getBannerList, getErrorMessage, getIsFetching } from '@/store/reducers/bannerList.reducer'
import CreateBanner from '../bannerDialogs/CreateBanner.dialog'
import RemoveBanner from '../bannerDialogs/RemoveBanner.dialog'
import BannerList from './BannerList'
import styled from 'styled-components'
import LoadingSpinner from '../loadingSpinner'

const ListWrapper = styled.div`
  position: relative;
`

const BannerListContainer = () => {
  const dispatch = useDispatch()
  const isFetching = useSelector(getIsFetching)
  const errorMessage = useSelector(getErrorMessage)
  const bannerList = useSelector(getBannerList)

  const fetchBannerList = useCallback((params) => dispatch(actions.fetchBannerList(params)), [dispatch])

  useEffect(() => {
    const { sortColumn, sortDirection } = bannerList
    if (sortColumn !== undefined && bannerList.content.length === 0) {
      fetchBannerList({ sortColumn, sortDirection })
    }
  }, [fetchBannerList, bannerList.sortColumn, bannerList.sortDirection])

  const handleSort = (newSortColumn) => {
    let { sortColumn, sortDirection, pageIndex } = bannerList
    if (sortColumn === newSortColumn) {
      sortDirection = bannerList.sortDirection === 'DESC' ? 'ASC' : 'DESC'
    } else {
      sortColumn = newSortColumn
    }

    fetchBannerList({ sortColumn, sortDirection, pageIndex })
  }

  const onActionComplete = () => {
    const { sortColumn, sortDirection } = bannerList
    fetchBannerList({ sortColumn, sortDirection })
  }

  return (
    <Fragment>
      <ListWrapper>
        <RemoveBanner onComplete={onActionComplete} />
        <CreateBanner onComplete={onActionComplete} />
        <BannerList bannerList={bannerList} errorMessage={errorMessage} onSort={handleSort} />
        {isFetching && !bannerList.length && <LoadingSpinner loading={isFetching} message={'Laddar sidan'} />}
      </ListWrapper>
    </Fragment>
  )
}

export default BannerListContainer
