import React from 'react'
import {CustomScrollingContainer, FlexColumnContainer, PageContainer} from '../components/styles/iaLayout'
import MenuBar from '../components/iaMenu/MenuBar';
import DataExportPageHeader from "../components/dataExport/DataExportPageHeader";
import DataExports from "../components/dataExport/DataExports";

const DataExportPage = () => {
  return (
    <FlexColumnContainer>
      <MenuBar/>
      <CustomScrollingContainer>
        <DataExportPageHeader />
        <PageContainer>
          <DataExports />
        </PageContainer>
      </CustomScrollingContainer>
    </FlexColumnContainer>
  )
}

export default DataExportPage
