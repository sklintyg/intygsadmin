import React from 'react'
import { useDispatch } from 'react-redux'
import { AddIcon } from '../styles/iaSvgIcons'
import IaColors from '../styles/iaColors'
import { Button, UncontrolledTooltip } from 'reactstrap'
import * as modalActions from '../../store/actions/modal'
import { CreateUserId } from './dialogs/CreateUser.dialog'

const UsersActionBar = () => {
  const dispatch = useDispatch()

  const addUser = () => {
    dispatch(modalActions.openModal(CreateUserId, { user: undefined }))
  }

  return (
    <>
      <Button id="addUserBtn" onClick={addUser} color={'success'}>
        <AddIcon color={IaColors.IA_COLOR_00} /> Lägg till administratör
      </Button>
      <UncontrolledTooltip trigger="hover" placement="auto" target="addUserBtn">
        Öppnar ett dialogfönster där du kan lägga till en ny administratör.
      </UncontrolledTooltip>
    </>
  )
}

export default UsersActionBar
