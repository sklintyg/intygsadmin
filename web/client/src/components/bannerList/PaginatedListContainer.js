import React from "react";
import * as actions from "../../store/actions/bannerList";
import BannerListContainer from "./BannerListContainer";
import { compose } from "recompose";
import { connect } from "react-redux";
import { withRouter } from "react-router-dom";
import BannerListPagination from "./BannerListPagination";
import {
  getBannerList
} from "../../store/reducers/bannerList";

const PaginatedListContainer = (props) => {
  const { bannerList } = props;

  const handlePageChange = (pageNumber) => {
    fetchList(pageNumber)
  }

  const fetchList = (pageIndex) => {
    const pageIndexZeroBased = pageIndex - 1
    props.fetchBannerList({pageIndex: pageIndexZeroBased});
  }

  return (
    <>
      <BannerListContainer />
      <BannerListPagination bannerList={bannerList} handlePageChange={handlePageChange}/>
    </>
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
