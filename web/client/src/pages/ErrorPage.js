import React from 'react';
import {FlexColumnContainer, ScrollingContainer, WorkareaContainer} from "../components/styles/ibLayout";
import AppFooter from "../components/appFooter/AppFooter";
import styled from "styled-components";
import {ErrorPageIcon} from "../components/styles/IbSvgIcons";
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
    activeError.message = 'Du saknar behörighet för att ta del av den aktuella förfrågan eller beställningen. För att ta del av en förfrågan eller beställning krävs ett medarbetaruppdrag med ändamål vård och behandling på den vårdenhet dit beställningen inkommit.';
    break;
  case 'LOGIN_FEL004':
    activeError.title = 'Tekniskt fel';
    activeError.message = 'Det uppstod ett tekniskt fel när din behörighet skulle kontrolleras.';
    break;
  case 'LOGIN_FEL001':
    activeError.title = 'Ett fel uppstod vid inloggningen';
    activeError.message = '';
    break;
  case 'UNAUTHORIZED':
    activeError.title = 'Behörighet saknas';
    activeError.message =
      'Du saknar behörighet för att ta del av den aktuella beställningen. För att ta del av en beställning krävs ett medarbetaruppdrag för vård och behandling på den vårdenhet dit beställningen inkommit. Om du anser dig ha behörighet att se beställningen, kontrollera att du har valt rätt vårdenhet och försök sedan att öppna beställningen igen.';

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
        <AppFooter />
      </CustomScrollingContainer>
    </FlexColumnContainer>
  )
};

export default ErrorPage;
