import React from 'react'
import {connect} from 'react-redux'
import {compose} from 'recompose'
import {AddIcon} from '../styles/iaSvgIcons'
import IaColors from '../styles/iaColors'
import {Button, UncontrolledTooltip} from 'reactstrap'
import * as modalActions from '../../store/actions/modal'
import {CreateUserId} from "./dialogs/CreateUser.dialog";

const UsersActionBar = ({ openModal }) => {
  const addUser = () => {
    openModal(CreateUserId, {user: undefined})
  }

  return (
    <>
      <Button id="addUserBtn" onClick={addUser} color={'success'}>
        <AddIcon color={IaColors.IA_COLOR_00} /> Lägg till administratör
      </Button>
      <UncontrolledTooltip placement="auto" target="addUserBtn">
        Öppnar ett dialogfönster där du kan lägga till en ny administratör.
      </UncontrolledTooltip>
    </>
  )
}

export default compose(
  connect(
    null,
    { ...modalActions }
  )
)(UsersActionBar)
