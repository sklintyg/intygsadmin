import React from 'react'
import IaColors from '../styles/iaColors'
import { DocIcon } from '../styles/iaSvgIcons'
import PageHeader from "../styles/PageHeader";

const PrivatePractitionerPageHeader = () => {
  return (
    <PageHeader
      header="Privatläkare"
      subHeader="Här kan du söka efter privatläkare för att se information, om de är registrerade i Webcert. Du kan även exportera en fil med alla registrerade privatläkare."
      icon={<DocIcon color={IaColors.IA_COLOR_02} />} />
  )
}

export default PrivatePractitionerPageHeader
