import React from 'react'
import iaValues from '../styles/iaValues'
import iaColors from '../styles/iaColors'
import styled from 'styled-components'
import { IaTypo01, IaTypo05 } from '../styles/iaTypography'

const CenterContainer = styled.div`
  margin: auto;
  width: 100%;
  max-width: ${iaValues.maxContentWidth};
  padding: 10px 30px 20px;
`

const HeaderContainer = styled.div`
  box-shadow: 0px 5px 9px -6px #000;
  position: relative;
  background-color: ${iaColors.IA_COLOR_00}
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

const PageHeader = ({ header, subHeader, icon, actionBar}) => {
  return (
    <HeaderContainer>
      <CenterContainer>
        <ButtonRow>
          <PageHeaderRow>
            <PageHeaderRow>
              <IconWrapper>{icon}</IconWrapper>
            </PageHeaderRow>
            <PageHeaderCol>
              <IaTypo01>{header}</IaTypo01>
              <IaTypo05>{subHeader}</IaTypo05>
            </PageHeaderCol>
          </PageHeaderRow>
          {actionBar}
        </ButtonRow>
      </CenterContainer>
    </HeaderContainer>
  )
}

export default PageHeader
