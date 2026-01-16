import React from 'react'
import ReactPaginate from 'react-paginate'
import styled from 'styled-components'
import iaColors from './iaColors'

const Wrapper = styled.div`
  padding: 20px 0 10px 0;

  & .page-item {
    text-decoration: none;
    color: ${iaColors.IA_COLOR_06};
    background-color: ${iaColors.IA_COLOR_07};
    padding: 0.375rem 0.75rem;
    display: block;
    border: 1px solid #dee2e6;
    cursor: pointer;
  }

  & .page-item {
    background-color: transparent;
    color: ${iaColors.IA_COLOR_05};
  }

  & .page-item.active {
    color: ${iaColors.IA_COLOR_05};
    background-color: transparent;
  }

  & .page-item.active {
    background-color: transparent;
    color: ${iaColors.IA_COLOR_05};
  }

  & .page-item.disabled {
    color: ${iaColors.IA_COLOR_10};
    background-color: ${iaColors.IA_COLOR_07};
    cursor: not-allowed;
  }
`

const ListPagination = ({ list, handlePageChange }) => {
  if (!list.content || list.content.length < 1) {
    return null
  }

  const pageIndex = !list.pageIndex ? 0 : list.pageIndex
  const pageCount = Math.ceil(list.totalElements / list.limit)

  const handlePaginationClick = (event) => {
    handlePageChange(event.selected + 1)
  }

  return (
    <Wrapper>
      <ReactPaginate
        previousLabel="Föregående"
        nextLabel="Nästa"
        breakLabel="..."
        pageCount={pageCount}
        marginPagesDisplayed={2}
        pageRangeDisplayed={5}
        onPageChange={handlePaginationClick}
        forcePage={pageIndex}
        containerClassName="pagination"
        pageClassName="page-item"
        pageLinkClassName="page-link"
        previousClassName="page-item"
        previousLinkClassName="page-link"
        nextClassName="page-item"
        nextLinkClassName="page-link"
        breakClassName="page-item"
        breakLinkClassName="page-link"
        activeClassName="active"
        disabledClassName="disabled"
      />
    </Wrapper>
  )
}

export default ListPagination
