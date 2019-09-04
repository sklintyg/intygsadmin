import React from 'react'
import IaColors from '../styles/iaColors'
import { DocIcon } from '../styles/iaSvgIcons'
import PageHeader from "../styles/PageHeader";

const IntygInfoPageHeader = () => {
  return (
    <PageHeader
      header="Intygsinformation"
      subHeader="Här kan du leta fram ett intyg i felsökningssyfte. Framgångsrika sökningar loggas."
      icon={<DocIcon color={IaColors.IA_COLOR_02} />} />
  )
}

export default IntygInfoPageHeader
