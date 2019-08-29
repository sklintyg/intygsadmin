import React from 'react'
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

const IntegratedUnitsPageHeader = ({ props }) => {
  return (
    <HeaderContainer>
      <CenterContainer>
        <ButtonRow>
          <PageHeaderRow>
            <PageHeaderRow>
              <IconWrapper><InfoIcon color={IaColors.IA_COLOR_02} /></IconWrapper>
            </PageHeaderRow>
            <PageHeaderCol>
              <IaTypo01>Integrerade enheter</IaTypo01>
              <IaTypo05>Här kan du söka efter enheter för att se om de är integrerade med Webcert eller exportera en fil med alla integrerade enheter.</IaTypo05>
            </PageHeaderCol>
          </PageHeaderRow>
        </ButtonRow>
      </CenterContainer>
    </HeaderContainer>
  )
}

export default IntegratedUnitsPageHeader
