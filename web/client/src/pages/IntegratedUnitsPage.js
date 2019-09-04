import React from 'react'
import { FlexColumnContainer, CustomScrollingContainer, PageContainer } from '../components/styles/iaLayout'
import IntegratedUnitsPageHeader from '../components/integratedUnits/IntegratedUnitsPageHeader'
import IntegratedUnitsSearchAndExportPage from '../components/integratedUnits/IntegratedUnitsSearchAndExportPage'
import MenuBar from '../components/iaMenu/MenuBar';

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
