import React from 'react'
//import * as actions from '../../store/actions/banner'
import { connect } from 'react-redux'
import { Button } from 'reactstrap'
import { compose } from 'recompose'
import styled from 'styled-components'
import { AddIcon } from '../styles/iaSvgIcons'
import IaColors from '../styles/iaColors'
import * as modalActions from '../../store/actions/modal'

const StyledButton = styled(Button)`
  margin-right: 16px;
`
const BannerActionBar = ({ openModal }) => {
  const addBanner = () => {}

  return (
    <>
      <StyledButton onClick={addBanner} color={'primary'}>
        <AddIcon color={IaColors.IA_COLOR_00} /> Ny driftbanner
      </StyledButton>
    </>
  )
}

const actions = null // temp
export default compose(
  connect(
    null,
    { ...actions, ...modalActions }
  )
)(BannerActionBar)
