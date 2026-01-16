import React, { useState } from 'react'
import { Button, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap'
import modalContainer from '../../modalContainer/modalContainer'
import IaAlert, { alertType } from '../../alert/Alert'
import { ErrorSection, ErrorWrapper } from '../../styles/iaLayout'
import { useAppDispatch } from '../../../store/hooks'
import { removeUser as removeUserAction } from '../../../store/actions/users'

const RemoveUser = ({ handleClose, isOpen, onComplete, data }) => {
  const dispatch = useAppDispatch()
  const [errorActive, setErrorActive] = useState(false)

  if (!data) {
    return null
  }

  const { userId } = data

  const remove = () => {
    setErrorActive(false)
    dispatch(removeUserAction(userId))
      .then(() => {
        handleClose()
        onComplete()
      })
      .catch(() => {
        setErrorActive(true)
      })
  }

  return (
    <>
      <Modal isOpen={isOpen} size={'md'} backdrop={true} toggle={handleClose}>
        <ModalHeader toggle={handleClose}>Ta bort administratör</ModalHeader>
        <ModalBody>
          <div>
            Att ta bort en administratör innebär att personen inte längre har tillgång till Intygsadmin. Personen kommer också att tas bort
            från översikten.
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
            id="confirmBtn"
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

export default modalContainer(RemoveUserId)(RemoveUser)
