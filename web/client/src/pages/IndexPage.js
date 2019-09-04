import React from 'react'
import styled from 'styled-components'
import { IaTypo01, IaTypo05 } from '../components/styles/iaTypography'
import { FlexColumnContainer, CustomScrollingContainer, Section, PageContainer } from '../components/styles/iaLayout'
import IaAlert, { alertType } from '../components/alert/Alert'
import LoginOptions from '../components/loginOptions'

const CustomPageContainer = styled(PageContainer)`
  flex-direction: row;
`

const Wrapper = styled.div`
  max-width: 650px;
  padding-left: 40px;
`

const HomePage = ({ match }) => {
  let title = 'Välkommen till Intygsadmin'
  let message = null

  switch (match.params.code) {
  case 'LOGIN_FEL001':
    title = 'Inloggningen misslyckades';
    message = 'Autentiseringen misslyckades.';
    break;
  case 'LOGIN_FEL002':
    title = 'Inloggningen misslyckades';
    message = 'Du saknar behörighet för att logga in.';
    break;
  case 'LOGIN_FEL003':
    title = 'Du är utloggad';
    message = 'Du har blivit utloggad på grund av inaktivitet. Om du vill fortsätta använda Intygsadmin behöver du logga in igen. ';
    break;
  default:
    break;
  }

  return (
    <FlexColumnContainer>
      <CustomScrollingContainer>
        <CustomPageContainer>
          <Wrapper>
            <IaTypo01 as="h1" id="indexTitle">{title}</IaTypo01>
            {message && (
              <Section>
                <IaAlert type={alertType.INFO} id="indexAlertMessage">
                  {message}
                </IaAlert>
              </Section>
            )}

            <IaTypo05 as="p">
              I Intygsadmin kan du som jobbar med förvaltning av Intygstjänster skapa och hantera driftbanners som informerar
              Intygstjänsters användare om kommande eller pågående händelser.
            </IaTypo05>

            <Section />
            <LoginOptions />
          </Wrapper>
        </CustomPageContainer>
      </CustomScrollingContainer>
    </FlexColumnContainer>
  )
}

export default HomePage
