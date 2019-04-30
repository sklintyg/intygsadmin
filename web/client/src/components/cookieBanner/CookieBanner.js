import React, { Fragment } from 'react'
import IbColors from '../styles/IbColors'
import styled from 'styled-components'
import { Button } from 'reactstrap'
import { CookieModalId } from '../cookieModal/CookieModal'
import * as modalActions from '../../store/actions/modal'
import { connect } from 'react-redux'
import { compose } from 'recompose'
import { acceptCookieBanner } from '../../store/actions/cookieBanner'
import { getCookieBannerAccepted } from '../../store/reducers/cookieBanner'

const CookieBannerWrapper = styled.div`
  display: flex;
  flex-direction: row;
  margin: 0;
  top: 0;
  position: fixed;
  left: 0;
  right: 0;
  z-index: 1020;
  padding: 10px 20px;
  color: ${IbColors.IB_COLOR_07};
  background: ${IbColors.IB_COLOR_03};

  div {
    flex: 1 1 auto;
  }

  & .cookieButton {
    flex: 0 1 auto;
    align-self: center;
  }
`

const CookieBanner = ({ openModal, acceptCookieBanner, cookieBannerAccepted }) => {
  const onBannerButtonClick = () => {
    acceptCookieBanner('true')
  }

  const openCookieModal = () => openModal(CookieModalId)

  if (cookieBannerAccepted === 'true') {
    return null
  }
  return (
    <Fragment>
      <CookieBannerWrapper>
        <div>
          Vi använder kakor (cookies) för att den här webbplatsen ska fungera på ett bra sätt för dig. Genom att logga in accepterar du vår
          användning av kakor.{' '}
          <Button color="link" onClick={openCookieModal}>
            Läs mer om kakor
          </Button>
        </div>
        <div className="cookieButton">
          <Button color={'success'} onClick={() => onBannerButtonClick()}>Jag godkänner</Button>
        </div>
      </CookieBannerWrapper>
    </Fragment>
  )
}

const mapStateToProps = (state) => {
  return {
    cookieBannerAccepted: getCookieBannerAccepted(state),
  }
}

export default compose(
  connect(
    mapStateToProps,
    { ...modalActions, acceptCookieBanner }
  )
)(CookieBanner)
