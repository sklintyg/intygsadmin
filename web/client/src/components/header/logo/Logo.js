import React from 'react'
import styled from 'styled-components'
import { HeaderSectionContainer } from '../styles'
import IaColors from '../../styles/iaColors'
import {LogoIcon} from '../../styles/iaSvgIcons'
import {IaTypo09} from '../../styles/iaTypography'

const ComponentWrapper = styled(HeaderSectionContainer)`
  height: 100%;
  background-color: ${IaColors.IA_COLOR_01};
  padding: 0 30px;

  border-image: linear-gradient(to left, rgba(0, 0, 0, 0.1), rgba(42, 48, 78, 0.1)) 1 100%;
  color: ${IaColors.IA_COLOR_03};

`
const SvgLogo = styled.div`
  background-color: ${IaColors.IA_COLOR_00};
  width: 30px;
  height: 30px;
  border-radius: 50%;
  fill: ${IaColors.IA_COLOR_01};
`
const LogoIconWrapper = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
`

const LogoText = styled(IaTypo09)`
  padding-left: 10px;
  color: ${IaColors.IA_COLOR_03};
`

const Logo = ({ className }) => {
  return (
    <ComponentWrapper>
      <SvgLogo><LogoIconWrapper><LogoIcon/></LogoIconWrapper></SvgLogo>
      <LogoText className={className}>Intygsadmin</LogoText>
    </ComponentWrapper>
  )
}

export default Logo
