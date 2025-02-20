import React from 'react'
import MenuBar from '../components/iaMenu/MenuBar'
import PageHeader from '../components/styles/PageHeader'
import IaColors from '../components/styles/iaColors'
import { CustomScrollingContainer, FlexColumnContainer, PageContainer } from '../components/styles/iaLayout'
import { DocIcon } from '../components/styles/iaSvgIcons'

const ResendPage = () => {
  return (
    <FlexColumnContainer>
      <MenuBar />
      <CustomScrollingContainer>
        <PageHeader
          header="Skapa omsändning"
          subHeader="Här kan du skapa en omsändning av händelser för både enskilda intygs-id och för hela vårdgivare/vårdenheter. Administrerade omsändningar visas i tabellen."
          icon={<DocIcon color={IaColors.IA_COLOR_02} />}
        />
        <PageContainer>{/* TODO: Add page content */}</PageContainer>
      </CustomScrollingContainer>
    </FlexColumnContainer>
  )
}

export default ResendPage
