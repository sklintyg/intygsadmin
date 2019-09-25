import React from 'react'
import { FlexColumnContainer, CustomScrollingContainer, PageContainer } from '../components/styles/iaLayout'
import MenuBar from '../components/iaMenu/MenuBar';
import IntygInfoPageHeader from "../components/intygInfo/IntygInfoPageHeader";
import IntygInfoSearch from "../components/intygInfo/IntygInfoSearch";
import IntygInfoHistory from "../components/intygInfo/IntygInfoHistory";

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
