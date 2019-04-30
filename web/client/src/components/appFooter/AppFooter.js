import React from 'react'
import styled from 'styled-components'
import ibValues from '../styles/IbValues'
import ibColors from '../styles/IbColors'
import { IbTypo14, IbTypo07 } from '../styles/IbTypography'
import { Button } from 'reactstrap'
import { openModal } from '../../store/actions/modal'
import { CookieModalId } from '../cookieModal/CookieModal'
import ExternalLink from '../externalLink/ExternalLink'
import { connect } from 'react-redux'
import { compose } from 'recompose'

const FooterOuterContainer = styled.div`
  background-color: ${ibColors.IB_COLOR_28};
  box-shadow: inset 1px 4px 9px -6px #000;
  width: 100%;
  padding: 39px 10px;
`
const FooterContainer = styled.div`
  display: flex;
  flex-direction: row;

  margin: auto;
  width: 100%;
  max-width: ${ibValues.maxContentWidth};
`
const FooterSection = styled.section`
  padding: 10px 90px 10px 10px;
`

const Text = styled(IbTypo07)`
  margin-bottom: 0;
`

const InternLank = styled(Button)`
  color: ${ibColors.IB_COLOR_17} !important;
  text-decoration: underline !important;
  &:hover {
    color: ${ibColors.IB_COLOR_21} !important;
  }
`

const AppFooter = ({openModal}) => {
  const openCookieDialog = () => openModal(CookieModalId)

  return (
    <FooterOuterContainer>
      <FooterContainer>
        <FooterSection>
          <IbTypo14 as="h3">Intygsadmin är en tjänst som drivs av Inera AB</IbTypo14>
          <Text as="p">Box 11703, 118 93 Stockholm</Text>
          <Text as="p">
            <ExternalLink href="https://www.inera.se">
              www.inera.se
            </ExternalLink>
          </Text>
        </FooterSection>
        <FooterSection>
          <IbTypo14>Kundtjänst</IbTypo14>
          <Text as="p">
            <ExternalLink href="https://www.inera.se/kundservice/kontakta-oss/">
              Kontakt och support
            </ExternalLink>
          </Text>
        </FooterSection>
        <FooterSection>
          <IbTypo14>Läs mer om inloggningen</IbTypo14>
          <Text as="p">
            <ExternalLink href="https://www.inera.se/intygsbestallning/inloggning">
              SITHS-kort
            </ExternalLink>
          </Text>
        </FooterSection>
      </FooterContainer>
      <FooterContainer>
        <FooterSection>
          <Text as="p">
            Intygsadmin använder kakor (cookies).{' '}
            <InternLank color="link" onClick={openCookieDialog}>
              Läs mer om kakor
            </InternLank>
          </Text>
        </FooterSection>
      </FooterContainer>
    </FooterOuterContainer>
  )
}

export default compose(
  connect(
    null,
    { openModal }
  )
)(AppFooter)
