import React, { createRef, useCallback, useEffect, useState } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import { Button, Form, UncontrolledTooltip } from 'reactstrap'
import * as modalActions from '../../store/actions/modal'
import PrivatePractitionerSearchResult, { PrivatePractitionerSearchResultId } from './PrivatePractitionerSearchResult.dialog'
import { IaTypo03 } from '../styles/iaTypography'
import styled from 'styled-components'
import * as actions from '../../store/actions/privatePractitioner'
import { getErrorMessage, getIsFetching, getPrivatePractitioner } from '../../store/reducers/privatePractitioner'
import colors from '../styles/iaColors'
import LoadingSpinner from '../loadingSpinner'
import { COULD_NOT_FIND_PRIVATE_PRACTITIONER, validatePrivatePractitioner } from './PrivatePractitionerValidator'
import HsaInput from '../styles/HsaInput'

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

const PrivatePractitionerSearch = () => {
  const dispatch = useDispatch()
  const privatePractitioner = useSelector(getPrivatePractitioner)
  const isFetching = useSelector(getIsFetching)
  const errorMessage = useSelector(getErrorMessage)

  const fetchPrivatePractitioner = (hsaId) => dispatch(actions.fetchPrivatePractitioner(hsaId))
  const openModal = useCallback((modalId, props) => dispatch(modalActions.openModal(modalId, props)), [dispatch])

  const [searchString, setSearchString] = useState('')
  const [validationSearchMessage, setValidationSearchMessage] = useState(undefined)
  const [searchResult, setSearchResult] = useState(undefined)
  const inputRef = createRef()

  const searchPrivatePractitioner = (event, hsaId) => {
    event.preventDefault()
    setSearchResult(undefined)
    if (hsaId === '') {
      setValidationSearchMessage(COULD_NOT_FIND_PRIVATE_PRACTITIONER)
    } else {
      fetchPrivatePractitioner(hsaId)
    }
  }

  useEffect(() => inputRef.current.focus(), [inputRef])

  useEffect(() => {
    setValidationSearchMessage(validatePrivatePractitioner(privatePractitioner.hsaId, errorMessage))
  }, [privatePractitioner, errorMessage])

  useEffect(() => {
    setSearchResult(privatePractitioner.hsaId)
  }, [privatePractitioner])

  useEffect(() => {
    if (searchResult !== undefined && validationSearchMessage === undefined) {
      let text = {
        hsaId: privatePractitioner.hsaId,
        name: privatePractitioner.name,
        careproviderName: privatePractitioner.careproviderName,
        email: privatePractitioner.email,
        registrationDate: privatePractitioner.registrationDate,
        hasCertificates: privatePractitioner.hasCertificates,
      }
      openModal(PrivatePractitionerSearchResultId, { text })
      setSearchString('')
    }
  }, [searchResult, privatePractitioner, validationSearchMessage, openModal])

  return (
    <>
      <SpinnerWrapper>
        <LoadingSpinner loading={isFetching} message={'Söker'} />
      </SpinnerWrapper>
      <PrivatePractitionerSearchResult />
      <PageSearchRow>
        <PageHeader>
          <IaTypo03>Ange HSA-id eller personnummer för privatläkare</IaTypo03>
        </PageHeader>
        <Form onSubmit={(event) => searchPrivatePractitioner(event, searchString)}>
          <FlexDiv>
            <Container className={validationSearchMessage !== undefined ? 'error' : ''}>
              <HsaInput id="searchInput" value={searchString} inputRef={inputRef} onChange={setSearchString} />
            </Container>
            <Button id={'searchBtn'} color={'success'}>
              Sök privatläkare
            </Button>
            <UncontrolledTooltip trigger="hover" placement="auto" target="searchBtn">
              Öppnar ett modalfönster med information om privatläkare.
            </UncontrolledTooltip>
          </FlexDiv>
        </Form>
        <ValidationMessage id={'validationSearchMessageId'}>{validationSearchMessage}</ValidationMessage>
      </PageSearchRow>
    </>
  )
}

export default PrivatePractitionerSearch
