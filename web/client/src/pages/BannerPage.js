import React from 'react'
import { FlexColumnContainer, ScrollingContainer, WorkareaContainer } from '../components/styles/ibLayout'
import styled from 'styled-components'
import ibValues from '../components/styles/IbValues'
import { IbTypo06 } from '../components/styles/IbTypography'
import IbColors from '../components/styles/IbColors'

const CustomScrollingContainer = styled(ScrollingContainer)`
  max-width: none;
`
const PageContainer = styled(WorkareaContainer)`
  margin: auto;
  width: 100%;
  max-width: ${ibValues.maxContentWidth};
  display: flex;
  flex-direction: column;
  min-height: calc(100vh - 290px);
  padding-bottom: 60px;

  img {
    width: 100%;
    padding-bottom: 20px;
  }
`
const BannerPage = () => {
  return (
    <FlexColumnContainer>
      <CustomScrollingContainer>
        <PageContainer>
          <IbTypo06 as="h1" color={IbColors.IB_COLOR_07}>Banner</IbTypo06>
          <p>Text.</p>
        </PageContainer>
      </CustomScrollingContainer>
    </FlexColumnContainer>
  )
}

export default BannerPage
