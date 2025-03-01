import React from 'react'
import styled from 'styled-components/macro'
import {PageHeaderContainer} from '../styles/iaLayout'
import iaColors from '../styles/iaColors'
import {Nav, Navbar} from 'reactstrap'
import MenuBarButton from './MenuBarButton'
import {compose} from 'recompose'
import {connect} from 'react-redux'

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

const MenuBar = ({ userRole }) => {
  const menu = [
    {
      id: 'intyginfo',
      to: '/intygInfo',
      text: 'Intygsinformation',
      enabled: true
    },
    {
      id: 'banners',
      to: '/banner',
      text: 'Driftbanner',
      enabled: true
    },
    {
      id: 'administratorer',
      to: '/administratorer',
      text: 'Administratörer',
      enabled: userRole === 'FULL'
    },
    {
      id: 'integratedUnits',
      to: '/integratedUnits',
      text: 'Integrerade enheter',
      enabled: true
    },
    {
      id: 'privatePractitioner',
      to: '/privatePractitioner',
      text: 'Registrerade privatläkare',
      enabled: true
    },
    {
      id: 'dataExport',
      to: '/dataExport',
      text: 'Dataexport',
      enabled: userRole === 'FULL'
    },
    {
      id: 'resend',
      to: '/resend',
      text: 'Omsändning',
      enabled: userRole === 'FULL'
    }
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

const mapStateToProps = (state) => {
  return {
    ...state.user
  }
};

export default compose(
  connect(
    mapStateToProps,
    null
  )
)(MenuBar)
