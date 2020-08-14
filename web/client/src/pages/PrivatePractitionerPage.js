import React from 'react'
import { FlexColumnContainer, CustomScrollingContainer, PageContainer } from '../components/styles/iaLayout'
import PrivatePractitionerPageHeader from '../components/privatePractitioner/PrivatePractitionerPageHeader'
import PrivatePractitionerSearch from '../components/privatePractitioner/PrivatePractitionerSearch'
import PrivatePractitionerExport from '../components/privatePractitioner/PrivatePractitionerExport'
import MenuBar from '../components/iaMenu/MenuBar';

const PrivatePractitionerPage = () => {
  return (
    <FlexColumnContainer>
      <MenuBar/>
      <CustomScrollingContainer>
        <PrivatePractitionerPageHeader />
        <PageContainer>
          <PrivatePractitionerSearch />
          <PrivatePractitionerExport />
        </PageContainer>
      </CustomScrollingContainer>
    </FlexColumnContainer>
  )
}

export default PrivatePractitionerPage
