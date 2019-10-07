import React, {useState} from 'react'
import colors from '../styles/iaColors'
import Toggler from '../toggler/Toggler'
import styled from 'styled-components'

const HelpDiv = styled.div`
  &.visible {
    display: block;
  }
  &.hidden {
    display: none;
  }

  border-radius: 2px;
  background-color: ${colors.IA_COLOR_15};
  max-height: 160px;
  padding: 12px 18px;
  overflow-y: auto;
  margin-bottom: 12px;
  h5 {
    color: ${colors.IA_COLOR_06}
    padding: 4px 0;
  }
  p {
    color: ${colors.IA_COLOR_06}
    margin: 0;
  }
`

const HelpRoof = styled.div`
  margin-top: -25px;
  &.visible {
    display: block;
  }
  &.hidden {
    display: none;
  }
  height: 20px;
  width: 20px;
  border: 10px solid ${colors.IA_COLOR_15};
  margin-left: 20px;
  border-top: 10px solid #fff;
  border-left: 10px solid #fff;
  border-right: 10px solid #fff;
`

const HelpChevron = ({ label, children }) => {
  const [prioHelpExpanded, setPrioHelpExpanded] = useState(false)
  const prioHelpToggler = () => {
    setPrioHelpExpanded(!prioHelpExpanded)
  }
  return (
    <>
      <h5>
        {label}
        <Toggler expanded={prioHelpExpanded} handleToggle={prioHelpToggler} />
      </h5>
      <HelpRoof className={prioHelpExpanded ? 'visible' : 'hidden'} />
      <HelpDiv className={prioHelpExpanded ? 'visible' : 'hidden'}>
        {children}
      </HelpDiv>
    </>
  )
}

export default HelpChevron
