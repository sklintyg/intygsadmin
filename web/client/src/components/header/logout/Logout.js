import React from 'react';
import * as PropTypes from "prop-types";
import styled from "styled-components";
import {ActionButton} from "../styles";
import {LogoutIcon} from "../../styles/iaSvgIcons";

const ALogoutUrl = styled.a`
  height: 100%
`

const Logout = ({logoutUrl}) =>
  (
    <ALogoutUrl href={logoutUrl}>
      <ActionButton id="logoutBtn">
        <LogoutIcon />
        <br />
        Logga ut
      </ActionButton>
    </ALogoutUrl>
  )

Logout.propTypes = {
  logoutUrl: PropTypes.string
}

export default Logout;
