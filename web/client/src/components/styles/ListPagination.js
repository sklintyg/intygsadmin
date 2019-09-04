import React from 'react'
import Pagination from 'react-js-pagination'
import styled from 'styled-components'
import iaColors from './iaColors'

const Wrapper = styled.div`
  padding: 20px 0 10px 0;

  & .page-item .page-link {
    text-decoration: none;
    color: ${iaColors.IA_COLOR_06}
    background-color: ${iaColors.IA_COLOR_07}
  }

  & .page-item .page-link:hover {
    background-color: transparent;
    color: ${iaColors.IA_COLOR_05}
  }

  & .page-item.active .page-link:hover {
    color: ${iaColors.IA_COLOR_05}
    background-color: transparent;
  }

  & .page-item.active .page-link {
    background-color: transparent;
    color: ${iaColors.IA_COLOR_05}
  }

  & .page-item.disabled .page-link {
    color: ${iaColors.IA_COLOR_10}
    background-color: ${iaColors.IA_COLOR_07}
  }
  `

const ListPagination = ({list, handlePageChange}) => {
  if (!list.content || list.content.length < 1) {
    return null
  }

  const pageIndex = !list.pageIndex ? 1 : list.pageIndex + 1
  return (
    <>
      <Wrapper>
        <Pagination
          activePage={pageIndex}
          itemsCountPerPage={list.limit}
          totalItemsCount={list.totalElements}
          pageRangeDisplayed={10}
          hideFirstLastPages={true}
          prevPageText="Föregående"
          nextPageText="Nästa"
          itemClass="page-item"
          linkClass="page-link"
          onChange={handlePageChange}
        />
      </Wrapper>
    </>
  )
}

export default ListPagination
