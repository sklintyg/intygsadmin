import React, {useState} from 'react'
import {Button, Modal, ModalBody, ModalFooter, ModalHeader} from 'reactstrap'
import modalContainer from '../../modalContainer/modalContainer'
import {compose} from 'recompose'
import IaAlert, {alertType} from '../../alert/Alert'
import {ErrorSection, ErrorWrapper} from '../../styles/iaLayout'
import {connect} from "react-redux";
import * as actions from "../../../store/actions/users";

const RemoveUser = ({ handleClose, isOpen, onComplete, data, removeUser }) => {

  const [errorActive, setErrorActive] = useState(false)

  if (!data) {
    return null
  }

  const { userId } = data

  const remove = () => {
    setErrorActive(false)
    removeUser(userId).then(() => {
      handleClose()
      onComplete()
    }).catch(() => {
      setErrorActive(true)
    })
  }

  return (
    <>
      <Modal isOpen={isOpen} size={'md'} backdrop={true} toggle={handleClose}>
        <ModalHeader toggle={handleClose}>Ta bort administratör</ModalHeader>
        <ModalBody>
          <div>
            Att ta bort en administratör innebär att personen inte längre har tillgång till Intygsadmin. Personen kommer också att tas bort från översikten.
          </div>
        </ModalBody>
        <ErrorSection>
          {errorActive && (
            <ErrorWrapper>
              <IaAlert type={alertType.ERROR}>
                Kunde inte ta bort administratören på grund av ett tekniskt fel. Prova igen om en stund.
              </IaAlert>
            </ErrorWrapper>
          )}
        </ErrorSection>
        <ModalFooter className="no-border">
          <Button
            id='confirmBtn'
            color={'primary'}
            onClick={() => {
              remove()
            }}>
            Ta bort
          </Button>
          <Button
            color={'default'}
            onClick={() => {
              setErrorActive(false)
              handleClose()
            }}>
            Avbryt
          </Button>
        </ModalFooter>
      </Modal>
    </>
  )
}

export const RemoveUserId = 'removeUser'

export default compose(
  connect(
    null,
    { ...actions }
  ),
  modalContainer(RemoveUserId)
)(RemoveUser)
