import React from 'react'
import styled from 'styled-components'
import { IaTypo01, IaTypo05 } from '../components/styles/iaTypography'
import { FlexColumnContainer, ScrollingContainer, Section, WorkareaContainer } from '../components/styles/iaLayout'
import ibValues from '../components/styles/iaValues'
import IaAlert, { alertType } from '../components/alert/Alert'
import LoginOptions from '../components/loginOptions'

const CustomScrollingContainer = styled(ScrollingContainer)`
  max-width: none;
`
const PageContainer = styled(WorkareaContainer)`
  margin: auto;
  width: 100%;
  max-width: ${ibValues.maxContentWidth};
  display: flex;
  flex-direction: row;
  min-height: calc(100vh - 290px);
  padding-bottom: 60px;

  img {
    width: 100%;
    padding-bottom: 20px;
  }
`
const Wrapper = styled.div`
  max-width: 650px;
  padding-left: 40px;
`

const HomePage = ({ match }) => {
  let method = match.params.method
  return (
    <FlexColumnContainer>
      <CustomScrollingContainer>
        <PageContainer>
          <Wrapper>
            <IaTypo01 as="h1">Välkommen till Intygsadmin!</IaTypo01>
            <IaTypo05 as="p">
              I Intygsadmin kan du som jobbar med förvaltning av Intygstjänster skapa och hantera driftbanners som informerar
              Intygstjänsters användare om kommande eller pågående händelser.
            </IaTypo05>
            {method === 't' && (
              <Section>
                <IaTypo05 as="h2">Du är utloggad</IaTypo05>
                <IaAlert type={alertType.OBSERVANDUM}>
                  Du har blivit utloggad på grund av inaktivitet. Om du vill fortsätta använda Intygsadmin behöver du logga in igen.
                </IaAlert>
              </Section>
            )}

            <Section />
            <LoginOptions />
          </Wrapper>
        </PageContainer>
      </CustomScrollingContainer>
    </FlexColumnContainer>
  )
}

export default HomePage
