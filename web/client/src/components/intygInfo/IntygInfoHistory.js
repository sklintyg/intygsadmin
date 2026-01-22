import React from 'react'
import IntygInfoHistoryList from './IntygInfoHistoryList'
import { IaTypo03 } from '../styles/iaTypography'
import ListPagination from '../styles/ListPagination'
import { useAppDispatch, useAppSelector } from '../../store/hooks'
import { fetchIntygInfoList } from '../../store/actions/intygInfoList'
import { getIntygInfoList } from '../../store/reducers/intygInfoList'
import styled from 'styled-components'

const Wrapper = styled.div`
  margin: 45px 0 0 0;
`

const IntygInfoHistory = () => {
  const dispatch = useAppDispatch()
  const intygInfoList = useAppSelector(getIntygInfoList)

  const handlePageChange = (pageNumber) => {
    fetchList(pageNumber)
  }

  const fetchList = (pageIndex) => {
    const pageIndexZeroBased = pageIndex - 1
    dispatch(fetchIntygInfoList({ pageIndex: pageIndexZeroBased }))
  }

  return (
    <Wrapper>
      <IaTypo03>Senaste sökningar</IaTypo03>
      <IntygInfoHistoryList />
      <ListPagination list={intygInfoList} handlePageChange={handlePageChange} />
    </Wrapper>
  )
}

export default IntygInfoHistory
