import React, { Fragment } from "react";
import * as actions from "../../store/actions/bannerList.actions";
import BannerListContainer from "./BannerListContainer";
import { compose } from "recompose";
import { connect } from "react-redux";
import { withRouter } from "react-router-dom";
import BannerListPagination from "./BannerListPagination";
import { getBannerList } from "../../store/reducers/bannerList.reducer";

const PaginatedListContainer = ({ bannerList, fetchBannerList }) => {

  const handlePageChange = (pageNumber) => {
    fetchList(pageNumber)
  }

  const fetchList = (pageIndex) => {
    const pageIndexZeroBased = pageIndex - 1
    fetchBannerList({pageIndex: pageIndexZeroBased});
  }

  return (
    <Fragment>
      <BannerListContainer />
      <BannerListPagination bannerList={bannerList} handlePageChange={handlePageChange}/>
    </Fragment>
  );
};

const mapStateToProps = (state) => {
  return {
    bannerList: getBannerList(state)
  };
};

export default compose(
  withRouter,
  connect(
    mapStateToProps,
    actions
  )
)(PaginatedListContainer);
