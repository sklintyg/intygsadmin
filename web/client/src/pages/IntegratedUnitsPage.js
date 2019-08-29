import React from 'react'
import { FlexColumnContainer, ScrollingContainer, WorkareaContainer } from '../components/styles/iaLayout'
import styled from 'styled-components'
import ibValues from '../components/styles/iaValues'
import IntegratedUnitsPageHeader from '../components/integratedUnits/IntegratedUnitsPageHeader'
import IntegratedUnitsSearchAndExportPage from '../components/integratedUnits/IntegratedUnitsSearchAndExportPage'
import MenuBar from '../components/iaMenu/MenuBar';

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
const IntegratedUnitsPage = () => {
  return (
    <FlexColumnContainer>
    <MenuBar/>
      <CustomScrollingContainer>
        <IntegratedUnitsPageHeader />
        <PageContainer>
          <IntegratedUnitsSearchAndExportPage />
        </PageContainer>
      </CustomScrollingContainer>
    </FlexColumnContainer>
  )
}

export default IntegratedUnitsPage
