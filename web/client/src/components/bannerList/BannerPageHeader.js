import React from 'react'
import BannerActionBar from './BannerActionBar'
import iaValues from '../styles/iaValues'
import styled from 'styled-components'
import IaColors from '../styles/iaColors'
import { IaTypo09 } from '../styles/iaTypography'
import { InfoIcon } from '../styles/iaSvgIcons'

const CenterContainer = styled.div`
  margin: auto;
  width: 100%;
  max-width: ${iaValues.maxContentWidth};
  padding: 10px 30px 20px;
`
/*  > div {
    display: flex;
    > span,
    > a {
      color: ${IaColors.IA_COLOR_07};
      font-weight: 400;
      font-size: 12px;
      display: inline-block;
      margin-right: 50px;
      position: relative;
      svg {
        position: absolute;
        left: -17px;
        width: 14px;
        height: 16px;
      }
    }
    > a {
      text-decoration: underline;
      &:hover {
        color: ${IaColors.IB_COLOR_21};
        svg {
          fill: ${IaColors.IB_COLOR_21};
        }
      }
    }
  }
*/
const HeaderContainer = styled.div`
  box-shadow: 0px 5px 9px -6px #000;
  position: relative;
  margin-left: -10px;
  margin-right: -10px;
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
              <IconWrapper><InfoIcon /></IconWrapper>
            </PageHeaderRow>
            <PageHeaderCol>
              <IaTypo09>Driftbanner</IaTypo09>
              Skapa och redigera driftbanners som informerar användaren om kommande eller pågående händelser.
            </PageHeaderCol>
          </PageHeaderRow>
          <BannerActionBar />
        </ButtonRow>
      </CenterContainer>
    </HeaderContainer>
  )
}

export default BannerPageHeader
