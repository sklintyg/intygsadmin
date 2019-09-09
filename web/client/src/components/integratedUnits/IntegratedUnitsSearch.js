import React, { useState, useEffect, createRef } from 'react'
import { connect } from 'react-redux'
import { compose } from 'recompose'
import { Button, UncontrolledTooltip, Input } from 'reactstrap'
import * as modalActions from '../../store/actions/modal'
import IntegratedUnitSearchResult from './IntegratedUnitSearchResult.dialog'
import { IntegratedUnitSearchResultId } from './IntegratedUnitSearchResult.dialog'
import { IaTypo03 } from "../styles/iaTypography"
import styled from "styled-components"
import * as actions from '../../store/actions/integratedUnits'
import { getIntegratedUnit, getIsFetching, getErrorMessage } from "../../store/reducers/integratedUnits";
import colors from "../styles/iaColors";
import LoadingSpinner from "../loadingSpinner";
import { validateIntegratedUnit, COULD_NOT_FIND_UNIT  } from "./IntegratedUnitsValidator";

const SpinnerWrapper = styled.div`
  position: relative;
`

const PageHeader = styled.div`
    padding: 12px 0 4px;
    &:first-of-type {
      padding: 4px 0;
    }
`

const PageSearchRow = styled.div`
  padding: 20px 0 10px 0;
`

const FlexDiv = styled.div`
  display: flex;
  margin-bottom: 8px;
  > span {
    flex: 0 0 150px;
    &:first-of-type {
      flex: 0 0 50px;
    }
  }
`

const ValidationMessage = styled.div`
  color: ${colors.IA_COLOR_16};
`

const Container = styled.div`
  &.error {
    > input,
    > input:focus,
    > span button,
    > span button:focus {
      border-color: ${colors.IA_COLOR_16};
    }
  }
`

const searchInput = {
  width: '180px'
}

const IntegratedUnitsSearch = ({ openModal, fetchIntegratedUnit, integratedUnit, isFetching, errorMessage }) => {
  const [searchString, setSearchString] = useState('')
  const [validationSearchMessage, setValidationSearchMessage] = useState(undefined)
  const [searchResult, setSearchResult] = useState(undefined)
  const inputRef = createRef();

  const searchIntegratedUnit = (hsaId) => {
    setSearchResult(undefined)
    if (hsaId === '') {
      setValidationSearchMessage(COULD_NOT_FIND_UNIT)
    } else {
      fetchIntegratedUnit(hsaId)
    }
  }

  useEffect(() => inputRef.current.focus(), [inputRef]);

  useEffect(() => {
    setValidationSearchMessage(validateIntegratedUnit(integratedUnit.enhetsId, errorMessage))
  }, [integratedUnit, errorMessage])

  useEffect(() => {
    setSearchResult(integratedUnit.enhetsId)
  }, [integratedUnit])

  useEffect(() => {
    if (searchResult !== undefined
      && validationSearchMessage === undefined) {
      let text = {
        unit: integratedUnit.enhetsId,
        unitName: integratedUnit.enhetsNamn,
        healthcareProvidersId: integratedUnit.vardgivarId,
        healthcareProvidersName: integratedUnit.vardgivarNamn,
        addedDate: integratedUnit.skapadDatum,
        checkedDate: integratedUnit.senasteKontrollDatum
      }
      openModal(IntegratedUnitSearchResultId, {text})
      setSearchString('')
    }
  }, [searchResult, integratedUnit, validationSearchMessage, openModal])

  return (
    <>
      <SpinnerWrapper>
        <LoadingSpinner loading={isFetching} message={'Söker'} />
      </SpinnerWrapper>
      <IntegratedUnitSearchResult/>
      <PageSearchRow>
        <PageHeader>
          <IaTypo03>Ange HSA-id för enhet</IaTypo03>
        </PageHeader>
        <FlexDiv>
          <Container className={validationSearchMessage !== undefined ? 'error' : ''}>
            <Input
              id={'searchInput'}
              placeholder='SE1234567890-1X23'
              value={searchString}
              onChange={(e) => setSearchString(e.target.value)}
              innerRef={inputRef}
              style={searchInput}
            />
          </Container>
          <Button id={'searchBtn'} onClick={() => searchIntegratedUnit(searchString)} color={'success'}>
            Sök enhet
          </Button>
          <UncontrolledTooltip placement='auto' target='searchBtn'>
            Öppnar ett modalfönster med information om enheten.
          </UncontrolledTooltip>
        </FlexDiv>
        <ValidationMessage id={'validationSearchMessageId'}>{validationSearchMessage}</ValidationMessage>
      </PageSearchRow>
    </>
  )
}

const mapStateToProps = (state) => {
  return {
    integratedUnit: getIntegratedUnit(state),
    isFetching: getIsFetching(state),
    errorMessage: getErrorMessage(state)
  }
}

export default compose(
  connect(
    mapStateToProps,
    { ...actions, ...modalActions }
  )
)(IntegratedUnitsSearch)
