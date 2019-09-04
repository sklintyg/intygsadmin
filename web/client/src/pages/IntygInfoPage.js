import React from 'react'
import { FlexColumnContainer, CustomScrollingContainer, PageContainer } from '../components/styles/iaLayout'
import PaginatedListContainer from '../components/bannerList/PaginatedListContainer'
import MenuBar from '../components/iaMenu/MenuBar';
import IntygInfoPageHeader from "../components/inygInfo/IntygInfoPageHeader";
import IntygInfoSearch from "../components/inygInfo/IntygInfoSearch";

const IntygInfoPage = () => {
  return (
    <FlexColumnContainer>
      <MenuBar/>
      <CustomScrollingContainer>
        <IntygInfoPageHeader />
        <PageContainer>
          <IntygInfoSearch />
        </PageContainer>
      </CustomScrollingContainer>
    </FlexColumnContainer>
  )
}

export default IntygInfoPage
