import React from 'react'
import { useDispatch } from 'react-redux'
import { AddIcon } from '../styles/iaSvgIcons'
import IaColors from '../styles/iaColors'
import { Button, UncontrolledTooltip } from 'reactstrap'
import * as modalActions from '../../store/actions/modal'
import { CreateBannerId } from '../bannerDialogs/CreateBanner.dialog'

const BannerActionBar = () => {
  const dispatch = useDispatch()

  const addBanner = () => {
    dispatch(modalActions.openModal(CreateBannerId, { banner: undefined }))
  }

  return (
    <>
      <Button id="addBannerBtn" onClick={addBanner} color={'success'}>
        <AddIcon color={IaColors.IA_COLOR_00} /> Skapa driftbanner
      </Button>
      <UncontrolledTooltip trigger="hover" placement="auto" target="addBannerBtn">
        Öppnar ett dialogfönster där du kan lägga till en ny driftbanner.
      </UncontrolledTooltip>
    </>
  )
}

export default BannerActionBar
