import React from 'react'
import { FlexColumnContainer, CustomScrollingContainer, PageContainer } from '../components/styles/iaLayout'
import PaginatedListContainer from '../components/bannerList/PaginatedListContainer'
import BannerPageHeader from '../components/bannerList/BannerPageHeader'
import MenuBar from '../components/iaMenu/MenuBar';


const BannerPage = () => {
  return (
    <FlexColumnContainer>
    <MenuBar/>
      <CustomScrollingContainer>
        <BannerPageHeader />
        <PageContainer>
          <PaginatedListContainer />
        </PageContainer>
      </CustomScrollingContainer>
    </FlexColumnContainer>
  )
}

export default BannerPage
