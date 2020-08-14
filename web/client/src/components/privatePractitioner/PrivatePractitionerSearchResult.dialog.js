import React from 'react'
import {Button, Modal, ModalBody, ModalFooter, ModalHeader} from 'reactstrap'
import modalContainer from '../modalContainer/modalContainer'
import { compose } from 'recompose'
import styled from "styled-components";
import DisplayDate from "../displayDateTime/DisplayDate";

const PrivatePractitionerSearchResult = ({ handleClose, isOpen, data }) => {

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
      <Modal id={'privatePractitionerSearchResultId'} isOpen={isOpen} size={'lg'} backdrop={true} toggle={handleClose}>
        <ModalHeader toggle={handleClose}>Privatläkaren finns registrerad i Webcert</ModalHeader>
        <ModalBody>
          <FlexDiv>
            <h5>
              Privatläkare:
            </h5>
            <span>
              {text.hsaId}
            </span>
          </FlexDiv>
          <FlexDiv>
            <h5>
              Namn:
            </h5>
            <span>
              {text.name}
            </span>
          </FlexDiv>
          <FlexDiv>
            <h5>
              Bolagsnamn:
            </h5>
            <span>
              {text.careproviderName}
            </span>
          </FlexDiv>
          <FlexDiv>
            <h5>
              Epost:
            </h5>
            <span>
              {text.email}
            </span>
          </FlexDiv>
          <FlexDiv>
            <h5>
              Registrerad:
            </h5>
            <span>
              <DisplayDate date={text.registrationDate} />
            </span>
          </FlexDiv>
        </ModalBody>
        <ModalFooter className="no-border">
          <Button
            id="closeModal"
            color={'default'}
            onClick={handleClose}>
            Stäng
          </Button>
        </ModalFooter>
      </Modal>
  )
}

export const PrivatePractitionerSearchResultId = 'privatePractitionerSearchResult'

export default compose(
  modalContainer(PrivatePractitionerSearchResultId)
)(PrivatePractitionerSearchResult)
