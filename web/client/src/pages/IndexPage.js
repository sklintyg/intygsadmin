import React from 'react'
import landing from './landningssida-min.png'
import styled from 'styled-components'
import { IbTypo02, IbTypo07 } from '../components/styles/IbTypography'
import { FlexColumnContainer, ScrollingContainer, Section, WorkareaContainer } from '../components/styles/ibLayout'
import ibValues from '../components/styles/IbValues'
import { Col, Container, Row } from 'reactstrap'
import IbAlert, { alertType } from '../components/alert/Alert'
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
const HomePage = ({ match }) => {
  let method = match.params.method
  return (
    <FlexColumnContainer>
      <CustomScrollingContainer>
        <PageContainer>
          <Container>
            <Row>
              <Col xs="12" md="7">
                <img src={landing} alt="Landningssida med illustration av stetoskop" />
              </Col>
              <Col xs="12" md="5">
                <IbTypo02 as="h1">Välkommen till Intygsadmin!</IbTypo02>
                <IbTypo07 as="p">
                  Intygsadmin är en tjänst för att hantera förfrågningar och beställningar av medicinska utlåtanden och intyg till
                  vården.
                </IbTypo07>
                {method === 't' && (
                  <Section>
                    <IbAlert type={alertType.OBSERVANDUM}>
                      Du har blivit utloggad från Intygsadmin på grund av inaktivitet. Om du vill fortsätta använda Intygsadmin
                      behöver du logga in igen.
                    </IbAlert>
                  </Section>
                )}

                <Section>
                  <IbTypo07 as="p">
                    För att logga in behöver du ett giltigt e-tjänstekort (exempelvis SITHS-kort) samt behörighet att ta del av
                    förfrågningar och beställningar för din vårdenhet.
                  </IbTypo07>
                  <IbAlert type={alertType.INFO}>
                    De förfrågningar och beställningar som hanteras i Intygsadmin är journalhandlingar och all aktivitet i tjänsten
                    loggas i enlighet med Patientdatalagen.
                  </IbAlert>
                </Section>
                <Section/>
                <LoginOptions />

              </Col>
            </Row>
          </Container>
        </PageContainer>
      </CustomScrollingContainer>
    </FlexColumnContainer>
  )
}

export default HomePage
