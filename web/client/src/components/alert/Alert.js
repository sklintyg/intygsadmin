import React from 'react';
import PropTypes from "prop-types";
import {InfoIcon, Security, Check, ErrorOutline, Warning} from "../styles/IbSvgIcons";
import IbColors from "../styles/IbColors";
import styled from 'styled-components'

export const alertType = {
  INFO: 'info',
  SEKRETESS: 'sekretess',
  OBSERVANDUM: 'observandum',
  CONFIRM: 'confirm',
  ERROR: 'error'
}

function getColor(type) {
  switch (type) {
  case alertType.INFO:
    return {text:IbColors.IB_COLOR_06, background:IbColors.IB_COLOR_03};
  case alertType.SEKRETESS:
    return {text:IbColors.IB_COLOR_05, background:IbColors.IB_COLOR_02};
  case alertType.OBSERVANDUM:
    return {text:IbColors.IB_COLOR_05, background:IbColors.IB_COLOR_02};
  case alertType.CONFIRM:
    return {text:IbColors.IB_COLOR_30, background:IbColors.IB_COLOR_29};
  case alertType.ERROR:
    return {text:IbColors.IB_COLOR_04, background:IbColors.IB_COLOR_01};
  default:
    return {text:'#fff', background:'#000'};
  }
}

const Alert = styled.div`
  color: ${props => getColor(props.type).text};
  background-color: ${props => getColor(props.type).background};
  position: relative;
  border-radius: 4px;
  display: inline-block;
  svg {
    position: absolute;
    top: 2px;
    width: 16px;
  }
  > div {
    margin-left: 20px;
  }
  padding: 4px 8px;
`

const IbAlert = ({type, children, className}) => {
  function getIcon(type) {
    switch (type) {
    case alertType.INFO:
      return <InfoIcon color={IbColors.IB_COLOR_06} />;
    case alertType.SEKRETESS:
      return <Security color={IbColors.IB_COLOR_05} />;
    case alertType.OBSERVANDUM:
      return <ErrorOutline color={IbColors.IB_COLOR_05} />;
    case alertType.CONFIRM:
      return <Check color={IbColors.IB_COLOR_30} />;
    case alertType.ERROR:
      return <Warning color={IbColors.IB_COLOR_04} />;
    default:
      return null;
    }
  }

  return (
    <Alert type={type} className={className}>
      {getIcon(type)}
      <div>{children}</div>
    </Alert>
  );
}

IbAlert.propTypes = {
  type: PropTypes.oneOf(Object.values(alertType)).isRequired
}

export default IbAlert;
