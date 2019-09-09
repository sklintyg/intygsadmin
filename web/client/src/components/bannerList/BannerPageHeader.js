import React from 'react'
import IaColors from '../styles/iaColors'
import BannerActionBar from './BannerActionBar'
import {InfoIcon} from '../styles/iaSvgIcons'
import PageHeader from "../styles/PageHeader";

const BannerPageHeader = () => {
  return (
    <PageHeader
      header="Driftbanner"
      subHeader="Skapa och redigera driftbanners som informerar användaren om kommande eller pågående händelser."
      icon={<InfoIcon color={IaColors.IA_COLOR_02} />}
      actionBar={<BannerActionBar />}
    />
  )
}

export default BannerPageHeader
