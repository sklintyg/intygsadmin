import React, { useState, useEffect } from 'react'
import { Button, UncontrolledTooltip, Input } from 'reactstrap'
import IntegratedUnitSearchResult from './IntegratedUnitSearchResult.dialog'
import { IntegratedUnitSearchResultId } from './IntegratedUnitSearchResult.dialog'
import { IaTypo03 } from "../styles/iaTypography"
import styled from "styled-components"
import colors from "../styles/iaColors";
import { validateIntegratedUnit, validateIntegratedUnitsFile, COULD_NOT_FIND_UNIT  } from "./IntegratedUnitsValidator";

const Wrapper = styled.div`
  & th:last-child {
    width: 1%;
  }
`

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
  width: '150px'
}

const IntygInfoSearch = ({ openModal, fetchIntegratedUnit, integratedUnit, isFetching, errorMessage, isFetchingIntegratedUnitsFile, errorMessageIntegratedUnitsFile }) => {
  const [searchString, setSearchString] = useState('')
  const [validationSearchMessage, setValidationSearchMessage] = useState(undefined)
  const [searchResult, setSearchResult] = useState(undefined)

  const searchIntegratedUnit = (hsaId) => {
    setSearchResult(undefined)
    if (hsaId === "") {
      setValidationSearchMessage(COULD_NOT_FIND_UNIT)
    } else {
      fetchIntegratedUnit(hsaId)
    }
  }

  useEffect(() => {
    setValidationSearchMessage(validateIntegratedUnit(integratedUnit.enhetsId, errorMessage))
  }, [integratedUnit, errorMessage])

  useEffect(() => {
    setValidationExportMessage(validateIntegratedUnitsFile(errorMessageIntegratedUnitsFile))
  }, [errorMessageIntegratedUnitsFile])

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
    }
  }, [searchResult, integratedUnit, validationSearchMessage, openModal])

  return (
    <Wrapper>
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
    </Wrapper>
  )
}

export default IntygInfoSearch
