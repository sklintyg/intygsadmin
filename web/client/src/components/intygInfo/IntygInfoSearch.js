import React, {createRef, useEffect, useState} from 'react'
import {Button, Form, Input, UncontrolledTooltip} from 'reactstrap'
import {IaTypo03} from "../styles/iaTypography"
import styled from "styled-components"
import colors from "../styles/iaColors";
import {getMessage} from "../../messages/messages";
import {getErrorMessage, getIntygInfo, getIsFetching} from "../../store/reducers/intygInfo";
import {compose} from "recompose";
import {connect} from "react-redux";
import * as actions from "../../store/actions/intygInfo";
import * as modalActions from "../../store/actions/modal";
import IntygInfoDialog, {intygInfoDialogId} from "./IntygInfoDialog";
import LoadingSpinner from "../loadingSpinner";

const SpinnerWrapper = styled.div`
  position: relative;
`

const PageHeader = styled.div`
    padding: 12px 0 4px;
    &:first-of-type {
      padding: 4px 0;
    }
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
  width: '300px'
}

const IntygInfoSearch = ({ openModal, fetchIntygInfo, intygInfo, isFetching, errorMessage }) => {
  const [searchString, setSearchString] = useState('')
  const [validationSearchMessage, setValidationSearchMessage] = useState(errorMessage)
  const [searchResult, setSearchResult] = useState(undefined)
  const inputRef = createRef();

  const searchIntygInfo = (event, intygsId) => {
    event.preventDefault()
    setSearchResult(undefined)
    if (intygsId === '') {
      setValidationSearchMessage(getMessage('intygInfo.intygsId.wrongformat'))
    } else {
      setValidationSearchMessage(undefined);
      fetchIntygInfo(intygsId)
    }
  }

  useEffect(() => inputRef.current.focus(), [inputRef]);

  useEffect(() => {
    setValidationSearchMessage(errorMessage)
  }, [errorMessage])

  useEffect(() => {
    setSearchResult(intygInfo.intygId)
  }, [intygInfo])

  useEffect(() => {
    if (searchResult !== undefined
      && !validationSearchMessage) {
      openModal(intygInfoDialogId, {intygInfo})
      setSearchString('')
    }
  }, [searchResult, intygInfo, validationSearchMessage, openModal])

  return (
    <>
      <SpinnerWrapper>
        <LoadingSpinner loading={isFetching} message={'Söker'} />
      </SpinnerWrapper>
      <PageHeader>
        <IaTypo03>Ange intygets ID</IaTypo03>
      </PageHeader>
      <IntygInfoDialog />
      <Form onSubmit={(event) => searchIntygInfo(event, searchString)}>
        <FlexDiv>
          <Container className={validationSearchMessage ? 'error' : ''}>
            <Input
              id={'searchInput'}
              placeholder='a92703da-c032-4833-b052-bdb6f54e0bf5'
              value={searchString}
              onChange={(e) => setSearchString(e.target.value)}
              style={searchInput}
              innerRef={inputRef}
            />
          </Container>
          <Button id={'searchBtn'} color={'success'}>
            Sök intyg
          </Button>
          <UncontrolledTooltip trigger='hover' placement='auto' target='searchBtn' >
            Öppnar ett modalfönster med information om intyget.
          </UncontrolledTooltip>
        </FlexDiv>
      </Form>
      <ValidationMessage id={'validationSearchMessageId'}>{validationSearchMessage}</ValidationMessage>
    </>
  )
}

const mapStateToProps = (state) => {
  return {
    intygInfo: getIntygInfo(state),
    isFetching: getIsFetching(state),
    errorMessage: getErrorMessage(state)
  }
}

export default compose(
  connect(
    mapStateToProps,
    { ...actions, ...modalActions }
  )
)(IntygInfoSearch)
