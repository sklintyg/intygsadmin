import React from 'react'
import IaColors from '../styles/iaColors'
import { DocIcon } from '../styles/iaSvgIcons'
import PageHeader from "../styles/PageHeader";

const IntegratedUnitsPageHeader = () => {
  return (
    <PageHeader
      header="Integrerade enheter"
      subHeader="Här kan du söka efter enheter för att se om de är integrerade med Webcert eller exportera en fil med alla integrerade enheter."
      icon={<DocIcon color={IaColors.IA_COLOR_02} />} />
  )
}

export default IntegratedUnitsPageHeader
