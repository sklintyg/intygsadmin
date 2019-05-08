import React, {Fragment} from 'react';
import * as PropTypes from "prop-types";
import {ActionButton} from "../styles";
import {LogoutIcon} from "../../styles/IaSvgIcons";

const Logout = ({logoutUrl}) =>
  (
    <Fragment>
      <a href={logoutUrl}>
        <ActionButton id="logoutBtn">

          <LogoutIcon />
          <br />
          Logga ut </ActionButton>
      </a>
    </Fragment>
  )

Logout.propTypes = {
  logoutUrl: PropTypes.string
}

export default Logout;
