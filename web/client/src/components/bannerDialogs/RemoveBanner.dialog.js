import React from 'react'
import { Button, Modal, ModalBody, ModalHeader, ModalFooter } from 'reactstrap'
import modalContainer from '../modalContainer/modalContainer'
import { compose } from 'recompose'
import * as actions from '../../store/actions/banner'
import { connect } from 'react-redux'

const RemoveBanner = ({ handleClose, isOpen, onComplete, data }) => {
  if(!data){
    return null
  }

  const {text, removeBanner, bannerId} = data

  const remove = () => {
    removeBanner(bannerId).then(()=>{
      handleClose()
      onComplete()
    })
  }

  return (
    <>
      <Modal isOpen={isOpen} size={'md'} backdrop={true} toggle={handleClose}>
        <ModalHeader toggle={handleClose}>Avsluta driftbanner</ModalHeader>
        <ModalBody>
          <div dangerouslySetInnerHTML={{__html: text}}></div>
        </ModalBody>
        <ModalFooter>
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
