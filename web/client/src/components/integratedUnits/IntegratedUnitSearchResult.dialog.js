import React from 'react'
import { Modal, ModalBody, ModalHeader } from 'reactstrap'
import modalContainer from '../modalContainer/modalContainer'
import { compose } from 'recompose'
import * as actions from '../../store/actions/banner'
import { connect } from 'react-redux'
import styled from "styled-components";

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
    <>
      <Modal id={'integratedUnitSearchResultId'} isOpen={isOpen} size={'lg'} backdrop={true} >
        <ModalHeader toggle={handleClose}>Enheten är integrerad med Webcert!</ModalHeader>
        <ModalBody>
          <FlexDiv>
            <h5>
              Enhet:
            </h5>
            <span>
              {text.unit}
            </span>
          </FlexDiv>
          <FlexDiv>
            <h5>
              Enhetsnamn:
            </h5>
            <span>
              {text.unitName}
            </span>
          </FlexDiv>
          <FlexDiv>
            <h5>
              Vårdgivar-id:
            </h5>
            <span>
              {text.healthcareProvidersId}
            </span>
          </FlexDiv>
          <FlexDiv>
            <h5>
              Vårdgivar namn:
            </h5>
            <span>
              {text.healthcareProvidersName}
            </span>
          </FlexDiv>
          <FlexDiv>
            <h5>
              Tillagd:
            </h5>
            <span>
              {text.addedDate}
            </span>
          </FlexDiv>
          <FlexDiv>
            <h5>
              Senast kontrollerad:
            </h5>
            <span>
              {text.checkedDate}
            </span>
          </FlexDiv>
        </ModalBody>
      </Modal>
    </>
  )
}

export const IntegratedUnitSearchResultId = 'integratedUnitSearchResult'

export default compose(
  connect(
    null,
    { ...actions }
  ),
  modalContainer(IntegratedUnitSearchResultId)
)(IntegratedUnitSearchResult)
