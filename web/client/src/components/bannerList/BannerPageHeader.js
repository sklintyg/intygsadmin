import React from 'react'
import BannerActionBar from './BannerActionBar'
import iaValues from '../styles/iaValues'
import styled from 'styled-components'
import IaColors from '../styles/iaColors'
import { IaTypo01, IaTypo05 } from '../styles/iaTypography'
import { InfoIcon } from '../styles/iaSvgIcons'

const CenterContainer = styled.div`
  margin: auto;
  width: 100%;
  max-width: ${iaValues.maxContentWidth};
  padding: 10px 30px 20px;
`

const HeaderContainer = styled.div`
  box-shadow: 0px 5px 9px -6px #000;
  position: relative;
`

const ButtonRow = styled.div`
  display: flex;
  .left {
    flex: 1 0;
  }
  padding-top: 10px;
  margin-top: 10px;
  align-items: center;
  justify-content: space-between;
`

const PageHeaderRow = styled.div`
  display: flex;
  flex-direction: row;
  height: 100%;
  align-items: center;
`

const PageHeaderCol = styled.div`
  display: flex;
  flex-direction: column;
`

const IconWrapper = styled.div`
  padding-right: 10px;
`

const BannerPageHeader = ({ props }) => {
  return (
    <HeaderContainer>
      <CenterContainer>
        <ButtonRow>
          <PageHeaderRow>
            <PageHeaderRow>
              <IconWrapper><InfoIcon color={IaColors.IA_COLOR_02} /></IconWrapper>
            </PageHeaderRow>
            <PageHeaderCol>
              <IaTypo01>Driftbanner</IaTypo01>
              <IaTypo05>Skapa och redigera driftbanners som informerar användaren om kommande eller pågående händelser.</IaTypo05>
            </PageHeaderCol>
          </PageHeaderRow>
          <BannerActionBar />
        </ButtonRow>
      </CenterContainer>
    </HeaderContainer>
  )
}

export default BannerPageHeader
