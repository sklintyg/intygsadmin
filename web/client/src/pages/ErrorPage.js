import React from 'react';
import {FlexColumnContainer, ScrollingContainer, WorkareaContainer} from "../components/styles/iaLayout";
import styled from "styled-components";
import {ErrorPageIcon} from "../components/styles/iaSvgIcons";
import ErrorMessageFormatter from '../messages/ErrorMessageFormatter'

const CustomScrollingContainer = styled(ScrollingContainer)`
  max-width: none;
`
const PageContainer = styled(WorkareaContainer)`
  margin: auto;
  width: 100%;
  max-width: 500px;
  display: flex;
  flex-direction: column;
  min-height: calc(100vh - 290px);
  padding-bottom: 60px;
  justify-content: center;
  align-items: center;

`
const ErrorPage = ({match}) => {

  let activeError = {title: 'Tekniskt fel', message: 'Ett tekniskt fel uppstod.', logId: match.params.logId};

  switch (match.params.errorCode) {
  case 'LOGIN_FEL002':
    activeError.title = 'Behörighet saknas';
    activeError.message = '	Du saknar behörighet för att logga in.';
    break;
  case 'LOGIN_FEL004':
    activeError.title = 'Tekniskt fel';
    activeError.message = 'Det uppstod ett tekniskt fel när din behörighet skulle kontrolleras.';
    break;
  case 'LOGIN_FEL001':
    activeError.title = 'Ett fel uppstod vid inloggningen';
    activeError.message = 'Autentiseringen misslyckades.';
    break;
  case 'UNAUTHORIZED':
    activeError.title = 'Behörighet saknas';
    activeError.message =
      'Autentiseringen misslyckades.';

    break;
  case 'NOT_FOUND':
    activeError.title = 'Sidan du söker finns inte';
    activeError.message = 'Kontrollera att den länk du använder är korrekt.';
    break;
  default:
    break;
  }
  return (
    <FlexColumnContainer>
      <CustomScrollingContainer>
        <PageContainer>
          <ErrorPageIcon />
          <br />
          <ErrorMessageFormatter error={activeError} />
        </PageContainer>
      </CustomScrollingContainer>
    </FlexColumnContainer>
  )
};

export default ErrorPage;
