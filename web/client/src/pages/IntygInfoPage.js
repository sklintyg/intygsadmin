import React from 'react'
import { FlexColumnContainer, CustomScrollingContainer, PageContainer } from '../components/styles/iaLayout'
import MenuBar from '../components/iaMenu/MenuBar';
import IntygInfoPageHeader from "../components/inygInfo/IntygInfoPageHeader";
import IntygInfoSearch from "../components/inygInfo/IntygInfoSearch";
import IntygInfoHistory from "../components/inygInfo/IntygInfoHistory";

const IntygInfoPage = () => {
  return (
    <FlexColumnContainer>
      <MenuBar/>
      <CustomScrollingContainer>
        <IntygInfoPageHeader />
        <PageContainer>
          <IntygInfoSearch />
          <IntygInfoHistory />
        </PageContainer>
      </CustomScrollingContainer>
    </FlexColumnContainer>
  )
}

export default IntygInfoPage
