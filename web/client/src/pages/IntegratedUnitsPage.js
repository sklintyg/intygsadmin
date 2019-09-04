import React from 'react'
import { FlexColumnContainer, CustomScrollingContainer, PageContainer } from '../components/styles/iaLayout'
import IntegratedUnitsPageHeader from '../components/integratedUnits/IntegratedUnitsPageHeader'
import IntegratedUnitsSearch from '../components/integratedUnits/IntegratedUnitsSearch'
import IntegratedUnitsExport from '../components/integratedUnits/IntegratedUnitsExport'
import MenuBar from '../components/iaMenu/MenuBar';

const IntegratedUnitsPage = () => {
  return (
    <FlexColumnContainer>
      <MenuBar/>
      <CustomScrollingContainer>
        <IntegratedUnitsPageHeader />
        <PageContainer>
          <IntegratedUnitsSearch />
          <IntegratedUnitsExport />
        </PageContainer>
      </CustomScrollingContainer>
    </FlexColumnContainer>
  )
}

export default IntegratedUnitsPage
