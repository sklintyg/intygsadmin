import React from 'react'
import {connect} from 'react-redux'
import {compose} from 'recompose'
import {AddIcon} from '../styles/iaSvgIcons'
import IaColors from '../styles/iaColors'
import {Button, UncontrolledTooltip} from 'reactstrap'
import * as modalActions from '../../store/actions/modal'
import {CreateBannerId} from '../bannerDialogs/CreateBanner.dialog'

const BannerActionBar = ({ openModal }) => {
  const addBanner = () => {
    openModal(CreateBannerId, {banner: undefined})
  }

  return (
    <>
      <Button id="addBannerBtn" onClick={addBanner} color={'success'}>
        <AddIcon color={IaColors.IA_COLOR_00} /> Skapa driftbanner
      </Button>
      <UncontrolledTooltip trigger='hover' placement="auto" target="addBannerBtn">
        Öppnar ett dialogfönster där du kan lägga till en ny driftbanner.
      </UncontrolledTooltip>
    </>
  )
}

export default compose(
  connect(
    null,
    { ...modalActions }
  )
)(BannerActionBar)
