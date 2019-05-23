import React from 'react'
import PropTypes from 'prop-types'
import { compose, lifecycle } from 'recompose'
import { connect } from 'react-redux'
import { withRouter } from 'react-router-dom'
import * as actions from '../../store/actions/bannerList.actions'
import { getBannerList, getErrorMessage, getIsFetching } from '../../store/reducers/bannerList.reducer'
import CreateBanner from '../bannerDialogs/CreateBanner.dialog'
import RemoveBanner from '../bannerDialogs/RemoveBanner.dialog'
import BannerList from './BannerList'
import styled from 'styled-components'
import LoadingSpinner from '../loadingSpinner'

const ListWrapper = styled.div`
  position: relative;
`

const BannerListContainer = (props) => {
  const { isFetching, errorMessage, bannerList } = props

  const handleSort = (newSortColumn) => {
    let { sortColumn, sortDirection } = bannerList
    if (sortColumn === newSortColumn) {
      sortDirection = bannerList.sortDirection === 'DESC' ? 'ASC' : 'DESC'
    } else {
      sortColumn = newSortColumn
    }

    fetchData({ ...props, sortColumn, sortDirection })
  }

  const onActionComplete = () => {
    fetchData(props)
  }

  return (
    <>
      <ListWrapper>
        <RemoveBanner onComplete={onActionComplete}/>
        <CreateBanner onComplete={onActionComplete}/>
        <BannerList bannerList={bannerList} errorMessage={errorMessage} onSort={handleSort} onActionComplete={onActionComplete} />
        {isFetching && !bannerList.length && <LoadingSpinner loading={isFetching} message={'Laddar driftbannerlista'} />}
      </ListWrapper>
    </>
  )
}

BannerListContainer.propTypes = {
  errorMessage: PropTypes.string,
  bannerList: PropTypes.object,
  isFetching: PropTypes.bool,
}

const fetchData = ({ fetchBannerList, sortColumn, sortDirection }) => {
  fetchBannerList({ sortColumn, sortDirection })
}

const lifeCycleValues = {
  componentDidMount() {
    fetchData(this.props)
  },
}

const mapStateToProps = (state) => {
  return {
    bannerList: getBannerList(state),
    isFetching: getIsFetching(state),
    errorMessage: getErrorMessage(state)
  }
}

export default compose(
  withRouter,
  connect(
    mapStateToProps,
    actions
  ),
  lifecycle(lifeCycleValues)
)(BannerListContainer)
