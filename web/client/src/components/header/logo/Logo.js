import React from 'react'
import styled from 'styled-components'
import { HeaderSectionContainer } from '../styles'
import IaColors from '../../styles/IaColors'
import {LogoIcon} from '../../styles/IaSvgIcons'

const ComponentWrapper = styled(HeaderSectionContainer)`
  height: 100%;
  background-color: ${IaColors.AG_COLOR_01};
  padding: 0 30px;

  border-image: linear-gradient(to left, rgba(0, 0, 0, 0.1), rgba(42, 48, 78, 0.1)) 1 100%;
  color: ${IaColors.AG_COLOR_03};

`
const SvgLogo = styled.div`
  background-color: ${IaColors.AG_COLOR_00};
  width: 30px;
  height: 30px;
  border-radius: 50%;
  fill: ${IaColors.AG_COLOR_01};
`
const LogoIconWrapper = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
`

const LogoText = styled.div`
  padding-left: 10px;
  color: ${IaColors.AG_COLOR_00};
`

const Logo = ({ className }) => {
  return (
    <ComponentWrapper className={className}>
        <SvgLogo><LogoIconWrapper><LogoIcon/></LogoIconWrapper></SvgLogo><LogoText>Administrationsgränssnitt</LogoText>
    </ComponentWrapper>
  )
}

export default Logo
