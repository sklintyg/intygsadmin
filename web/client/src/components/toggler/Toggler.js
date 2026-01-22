import React from 'react'
import styled from 'styled-components'
import { CollapseIcon, ExpandIcon } from '../styles/iaSvgIcons'

const TogglerTag = styled.button`
  padding: 4px;
  border: none;
  background: transparent;
`

const Toggler = ({ expanded, handleToggle }) => (
  <TogglerTag onClick={handleToggle}>{expanded ? <ExpandIcon /> : <CollapseIcon />}</TogglerTag>
)

export default Toggler
