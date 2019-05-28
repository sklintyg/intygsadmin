import React from 'react'
import { connect } from 'react-redux'
import { compose } from 'recompose'
import styled from 'styled-components'
import { AddIcon } from '../styles/iaSvgIcons'
import IaColors from '../styles/iaColors'
import { Button } from 'reactstrap'
import * as modalActions from '../../store/actions/modal'
import { CreateBannerId } from '../bannerDialogs/CreateBanner.dialog'

const StyledButton = styled(Button)`
  margin-right: 16px;
`
const BannerActionBar = ({ openModal }) => {
  const addBanner = () => {
    openModal(CreateBannerId)
  }

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
