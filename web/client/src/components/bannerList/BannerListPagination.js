import React from 'react'
import Pagination from 'react-js-pagination'
import styled from 'styled-components'
import ibColors from '../styles/iaColors'

const Wrapper = styled.div`
  padding: 20px 0 10px 0;

  & .page-item .page-link {
    text-decoration: none;
    color: ${ibColors.IA_COLOR_06}
  }

  & .page-item .page-link:hover {
    background-color: transparent;
    color: ${ibColors.IA_COLOR_05}
  }

  & .page-item.active .page-link:hover {
    color: ${ibColors.IA_COLOR_05}
    background-color: transparent;
  }

  & .page-item.active .page-link {
    background-color: transparent;
    color: ${ibColors.IA_COLOR_05}
  }

  & .page-item.disabled .page-link {
    color: ${ibColors.IA_COLOR_10}
  }
  `

const BannerListPagination = props => {
  if (!props.bannerList.content || props.bannerList.content.length < 1) {
    return null
  }

  const pageIndex = !props.bannerList.pageIndex ? 1 : props.bannerList.pageIndex + 1
  return (
    <>
      <Wrapper>
        <Pagination
          activePage={pageIndex}
          itemsCountPerPage={props.bannerList.limit}
          totalItemsCount={props.bannerList.totalElements}
          pageRangeDisplayed={10}
          hideFirstLastPages={true}
          prevPageText="Föregående"
          nextPageText="Nästa"
          itemClass="page-item"
          linkClass="page-link"
          onChange={props.handlePageChange}
        />
      </Wrapper>
    </>
  )
}

export default BannerListPagination
