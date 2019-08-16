import React from 'react'
import styled from 'styled-components/macro'
import { PageHeaderContainer } from '../styles/iaLayout'
import iaColors from '../styles/iaColors'
import { Navbar, Nav } from 'reactstrap'
import MenuBarButton from './MenuBarButton'
import { compose } from 'recompose'
import { connect } from 'react-redux'

const Wrapper = styled.div`
  background-color: ${iaColors.IA_COLOR_04};

  & .navbar-ib {
    padding: 6px 30px;
  }

  & .navbar-ib li {
    display: inline-block;
  }

  & .navbar-nav {
    flex-direction: row;
  }
`

const MenuBar = ({ stat }) => {
  const menu = [
    {
      to: '/intygsinformation',
      text: 'Intygsinformation',
      enabled: false
    },
    {
      to: '/banner',
      text: 'Driftbanner',
      enabled: true
    },
    {
      to: '/administratorer',
      text: 'Administratörer',
      enabled: false
    },
  ]

  return (
    <Wrapper>
      <PageHeaderContainer>
        <Navbar className="navbar-ib">
          <Nav navbar>
            {menu.map((menuItem) => (
              <MenuBarButton key={menuItem.text} menuItem={menuItem} />
            ))}
          </Nav>
        </Navbar>
      </PageHeaderContainer>
    </Wrapper>
  )
}

export default compose(
  connect(
    null,
    null
  )
)(MenuBar)
