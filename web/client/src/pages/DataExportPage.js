import React from 'react'
import { CustomScrollingContainer, FlexColumnContainer, PageContainer } from '../components/styles/iaLayout'
import MenuBar from '../components/iaMenu/MenuBar'
import DataExportPageHeader from '../components/dataExport/DataExportPageHeader'
import DataExport from '../components/dataExport/DataExport'

const DataExportPage = () => {
  return (
    <FlexColumnContainer>
      <MenuBar />
      <CustomScrollingContainer>
        <DataExportPageHeader />
        <PageContainer>
          <DataExport />
        </PageContainer>
      </CustomScrollingContainer>
    </FlexColumnContainer>
  )
}

export default DataExportPage
