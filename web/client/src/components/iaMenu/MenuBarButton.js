import {NavLink} from 'react-router-dom'
import styled from 'styled-components/macro'
import iaColors from '../styles/iaColors'
import {NavItem} from 'reactstrap'

const Wrapper = styled.span`
  & a {
    display: inline-block;
    color: ${iaColors.IA_COLOR_03};
    line-height: normal;
    background-color: transparent;
    border: 1px solid transparent;
    border-radius: 22.5px;
    padding: 5px 15px;
    margin: 0 26px 0 0;
    text-decoration: none;
  }

  & a:hover,
  & a:hover,
  & a:hover:focus {
    background-color: ${iaColors.IA_COLOR_01};
    border-color: ${iaColors.IA_COLOR_05};
    color: ${iaColors.IA_COLOR_03};
    text-decoration: none;
  }

  & a.active,
  & a.active:hover,
  & a.active:focus {
    background-color: ${iaColors.IA_COLOR_05};
    border-color: ${iaColors.IA_COLOR_05};
    color: ${iaColors.IA_COLOR_03};
    text-decoration: none;
  }
`

const MenuBarButton = ({menuItem}) => {

  if (!menuItem.enabled) {
    return null
  }

  return (
    < Wrapper >
    < NavItem
  key = {menuItem.text} >
    < NavLink
  to = {menuItem.to}
  id = {'MenuBar-' +menuItem.text} >
    {menuItem.text}
    < /NavLink>
    < /NavItem>
    < /Wrapper>
)
}

export default MenuBarButton
