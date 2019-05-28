import React, {useState} from 'react'
import { Button, Modal, ModalBody, ModalHeader, ModalFooter } from 'reactstrap'
import modalContainer from '../modalContainer/modalContainer'
import { compose } from 'recompose'
import * as actions from '../../store/actions/banner'
import { connect } from 'react-redux'
import IaAlert, { alertType } from '../alert/Alert'
import {ErrorSection, ErrorWrapper } from '../styles/iaLayout'

const RemoveBanner = ({ handleClose, isOpen, onComplete, data }) => {

  const [errorActive, setErrorActive] = useState(false)

  if (!data) {
    return null
  }

  const { text, removeBanner, bannerId } = data

  const remove = () => {
    setErrorActive(false)
    removeBanner(bannerId).then(() => {
      handleClose()
      onComplete()
    }).catch((data)=>{
      console.log(data)
      setErrorActive(true)
    })
  }

  return (
    <>
      <Modal isOpen={isOpen} size={'md'} backdrop={true} toggle={handleClose}>
        <ModalHeader toggle={handleClose}>Avsluta driftbanner</ModalHeader>
        <ModalBody>
          <div dangerouslySetInnerHTML={{ __html: text }} />
        </ModalBody>
        <ErrorSection>
          {errorActive && (
            <ErrorWrapper>
              <IaAlert type={alertType.ERROR}>
                Driftbannern kunde inte tas bort på grund av ett tekniskt fel. Prova igen om en stund.
              </IaAlert>
            </ErrorWrapper>
          )}
        </ErrorSection>
        <ModalFooter className="no-border">
          <Button
            color={'primary'}
            onClick={() => {
              remove()
            }}>
            Avsluta
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

export const RemoveBannerId = 'removeBanner'

export default compose(
  connect(
    null,
    { ...actions }
  ),
  modalContainer(RemoveBannerId)
)(RemoveBanner)
