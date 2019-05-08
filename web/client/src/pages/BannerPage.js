import React from 'react'
import { FlexColumnContainer, ScrollingContainer, WorkareaContainer } from '../components/styles/ibLayout2'
import styled from 'styled-components'
import ibValues from '../components/styles/ibValues2'
import { IaTypo06 } from '../components/styles/iaTypography2'
import IaColors from '../components/styles/iaColors2'

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
          <IaTypo06 as="h1" color={IaColors.IA_COLOR_06}>Banner</IaTypo06>
          <p>Text.</p>
        </PageContainer>
      </CustomScrollingContainer>
    </FlexColumnContainer>
  )
}

export default BannerPage
