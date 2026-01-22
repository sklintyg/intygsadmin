import React from 'react'
import { Button, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap'
import modalContainer from '../modalContainer/modalContainer'
import styled from 'styled-components'
import DisplayDateTime from '../displayDateTime/DisplayDateTime'

const IntegratedUnitSearchResult = ({ handleClose, isOpen, data }) => {
  const FlexDiv = styled.div`
    display: flex;
    > h5 {
      line-height: 1.5;
      flex: 0 0 140px;
    }
  `

  if (!data) {
    return null
  }

  const { text } = data

  return (
    <Modal id={'integratedUnitSearchResultId'} isOpen={isOpen} size={'lg'} backdrop={true} toggle={handleClose}>
      <ModalHeader toggle={handleClose}>Enheten är integrerad med Webcert!</ModalHeader>
      <ModalBody>
        <FlexDiv>
          <h5>Enhet:</h5>
          <span>{text.unit}</span>
        </FlexDiv>
        <FlexDiv>
          <h5>Enhetsnamn:</h5>
          <span>{text.unitName}</span>
        </FlexDiv>
        <FlexDiv>
          <h5>Vårdgivar-id:</h5>
          <span>{text.healthcareProvidersId}</span>
        </FlexDiv>
        <FlexDiv>
          <h5>Vårdgivar namn:</h5>
          <span>{text.healthcareProvidersName}</span>
        </FlexDiv>
        <FlexDiv>
          <h5>Tillagd:</h5>
          <span>
            <DisplayDateTime date={text.addedDate} includeSeconds={true} />
          </span>
        </FlexDiv>
        <FlexDiv>
          <h5>Senast kontrollerad:</h5>
          <span>
            <DisplayDateTime date={text.checkedDate} includeSeconds={true} />
          </span>
        </FlexDiv>
      </ModalBody>
      <ModalFooter className="no-border">
        <Button id="closeModal" color={'default'} onClick={handleClose}>
          Stäng
        </Button>
      </ModalFooter>
    </Modal>
  )
}

export const IntegratedUnitSearchResultId = 'integratedUnitSearchResult'

export default modalContainer(IntegratedUnitSearchResultId)(IntegratedUnitSearchResult)
