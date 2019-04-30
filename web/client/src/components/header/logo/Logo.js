import React from 'react'
import styled from 'styled-components'
import { HeaderSectionContainer } from '../styles'
import IbColors from '../../styles/IbColors'
import { NavLink } from 'react-router-dom'

const ComponentWrapper = styled(HeaderSectionContainer)`

  flex: 1;
  height: 100%;
  min-width: 200px;
  max-width: 253px;
  background-color: ${IbColors.IB_COLOR_19}
  justify-content: center;
  padding: 0 30px;

  border-image: linear-gradient(to left, rgba(0, 0, 0, 0.1), rgba(42, 48, 78, 0.1)) 1 100%;
  color: ${IbColors.IB_COLOR_20};

`
const SvgLogo = styled.div`
  width: 180px;
  height: 26px;
  background: no-repeat center url(data:image/svg+xml;base64,);
`
const Logo = ({ className }) => {
  return (
    <ComponentWrapper className={className}>
      <NavLink to="/bestallningar">
        <SvgLogo />
      </NavLink>
    </ComponentWrapper>
  )
}

export default Logo
